;; Copyright 2020 Expedia, Inc.
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;     https://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

(ns parsec.api.service
  (:gen-class)
  (:require [parsec.core :refer [execute-query validate-query]]
            [parsec.api.config :as config]
            [parsec.api.helpers :refer [add-shutdown-hook]]
            [clojure.string :refer [join]])

  (:require [compojure.core :refer [context defroutes rfn routes POST]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.util.response :refer [response]]
            [ring.adapter.jetty :refer [run-jetty]]
            [metrics.ring.expose :refer [expose-metrics-as-json]]
            [metrics.ring.instrument :refer [instrument]])

  (:require [cheshire.generate :refer [add-encoder]]
            [clj-time.coerce :as time-coerce]
            [clj-time.core :as time])

  (:import (org.joda.time DateTime DateMidnight Interval ReadablePeriod)
           (clojure.lang AFunction)
           (org.bson.types ObjectId)))

;; Custom DateTime encoders for clj-time/JodaTime
(doall
  (map #(add-encoder
         %
         (fn [c jsonGenerator]
           (.writeString jsonGenerator (time-coerce/to-string c))))
       [DateTime DateMidnight]))

(add-encoder
  Interval
  (fn [c jsonGenerator]
    (.writeNumber jsonGenerator (time/in-millis c))))

(add-encoder
  ReadablePeriod
  (fn [period jsonGenerator]
    (.writeString jsonGenerator (.toString period))))

(add-encoder
  AFunction
  (fn [fn jsonGenerator]
    (.writeString jsonGenerator (.toString fn))))

(add-encoder
  parsec.functions.ParsecUdf
  (fn [udf jsonGenerator]
    (.writeString jsonGenerator (str "(" (join ", " (map name (:args udf))) ") -> " (:implementation udf)))))

(add-encoder
  ObjectId
  (fn [objectId jsonGenerator]
    (.writeString jsonGenerator (.toHexString objectId))))

(defroutes
  api-routes
  (context
    "/api" []
    (POST "/execute" request
      (let [context (execute-query (get-in request [:body "query"]))
            context (-> context
                        ;; Provide a filtered set of non-temporary
                        (assoc :dataSets (filter #(false? (:temporary %)) (vals (:datastore context))))
                        (dissoc :datastore :current-dataset))]
        (response context)))

    (POST "/validate" request
      (response
        (validate-query
          (get-in request [:body "query"]))))))

(defroutes
  site-routes
  (route/resources "/")

  ;; Default to index
  (rfn []
    (let [home (io/resource "public/index.html")]
      (if (nil? home)
        "Website doesn't exist."
        (slurp home)))))

(def app
  (-> (routes api-routes site-routes)
      (wrap-json-body)
      (wrap-json-response)
      (wrap-defaults api-defaults)
      (expose-metrics-as-json)
      (instrument)
      (wrap-cors :access-control-allow-origin [#".*"]
                 :access-control-allow-methods [:get :put :post]
                 :access-control-allow-headers ["Content-Type"])))


(add-shutdown-hook #(println "Shutting down Parsec"))

(defn -main
  "Run embedded jetty service."
  [& args]
  (run-jetty app {:port (:port config/parsec-config)}))
