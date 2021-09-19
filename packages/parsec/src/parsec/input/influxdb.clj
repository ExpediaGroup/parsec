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

(ns parsec.input.influxdb
  (:require [parsec.helpers :refer :all]
            [clj-time.format :as timef]
            [http.async.client :as http]
            [clojure.data.json :as json]))

(def default-client-options
  {:follow-redirects true
   :keep-alive       false})

(defn process-series
  "Converts a single InfluxDB series to Parsec"
  [series]
  (let [{ :keys [name columns values tags] } series
        column-keys (map keyword columns)
        tags (if (nil? tags) {} tags)
        result (->> values
                    (map #(zipmap column-keys %))
                    (map #(if (:time %) (assoc % :time (timef/parse (:time %))) %))
                    (map #(merge % tags)))]
    result))

(defn process-results
  "Converts influxdb results to Parsec."
  [[result & more]]
  (if (not-empty more)
    (throw (Exception. (str "Multiple InfluxDB queries are not supported; please execute one query at a time")))
    (if (contains? result :error)
      (throw (Exception. (str "Error in InfluxDB query: " (:error result))))
      (map process-series (:series result)))))

; Options:
; :uri
; :db
; :query
; :user
; :password
(defn input-transform
  "Executes an InfluxQL query."
  [options]
  (fn [context]
    (let [options' (eval-expression-without-row options context)
          {:keys [uri
                  db
                  query]} options']
      (cond
        (nil? uri) (throw (Exception. "input type InfluxDB requires a \"uri\" option"))
        (nil? query) (throw (Exception. "input type InfluxDB requires a \"query\" option"))
        :else (with-open [client (mapply http/create-client (apply-default-http-options options' default-client-options))]
                (let [response (http/GET client uri :query {:db db :q query})]
                  (http/await response)
                  (let [status (http/status response)
                        body (http/string response)
                        parsed-body (json/read-str body :key-fn keyword)
                        statusCode (:code status)
                        msg (:msg status)]
                    (case statusCode
                      400 (throw (Exception. (str "Error executing InfluxDB query: " (:error parsed-body))))

                      200 (let [results (:results parsed-body)
                                converted-results (flatten (process-results results))]
                            (assoc context :current-dataset converted-results))

                      ; else
                      (throw (Exception. (str "Error executing InfluxDB query: " statusCode " " msg)))))))))))
