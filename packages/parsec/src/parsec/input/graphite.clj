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

(ns parsec.input.graphite
  (:require [parsec.helpers :refer :all]
            [clj-time.coerce :as timec]
            [http.async.client :as http]
            [clojure.data.json :as json]
            [clojurewerkz.urly.core :as urly]))

(def default-client-options
  {:follow-redirects true
   :keep-alive       false})

(defn process-target
  "Converts a single Graphite target to Parsec"
  [{:keys [target datapoints]}]
  (map (fn [data]
         (let [value (first data)
               time (nth data 1)]
           { :_time (timec/from-long (* 1000 time))
            (keyword target) value })
         ) datapoints))

(defn process-results
  "Converts Graphite JSON results to Parsec."
  [results]
  (let [targets (map process-target results)]
    (sort-by :_time (reduce clojure.set/join targets))))

; Options:
; :uri (e.g. http://graphite)
; :targets (list of strings)
; :from
; :until
(defn input-transform
  "Queries the Graphite API for metrics."
  [options]
  (fn [context]
    (let [options' (eval-expression-without-row options context)
          {:keys [uri targets from until]} options']
      (cond
        (nil? uri) (throw (Exception. "input type Graphite requires a \"uri\" option"))
        (nil? targets) (throw (Exception. "input type Graphite requires a \"targets\" option"))
        :else (with-open [client (mapply http/create-client (apply-default-http-options options' default-client-options))]
                (let [targets (if (string? targets) [targets] targets)
                      uri' (-> uri (urly/url-like) (.mutatePath  "/render") (str))
                      response (http/GET client uri' :query { :format "json" :target (vec targets) :from from :until until })]
                  (http/await response)
                  (let [status (http/status response)
                        body (http/string response)
                        statusCode (:code status)
                        msg (:msg status)]
                    (case statusCode
                      200 (let [results (json/read-str body :key-fn keyword)
                                converted-results (process-results results)]
                            (assoc context :current-dataset converted-results))

                      ; else
                      (throw (Exception. (str "Error executing Graphite query: " statusCode " " msg)))))))))))
