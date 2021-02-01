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

(ns parsec.input.bigquery
  (:require [parsec.helpers :refer :all])
  (:import (com.google.cloud.bigquery BigQueryOptions QueryJobConfiguration BigQuery$JobOption)
           (com.google.auth.oauth2 ServiceAccountCredentials)
           (com.google.api.services.bigquery.model QueryRequest)))

(defn fieldvaluelist->row
  [schema fieldValueList]
  (reduce-kv
    (fn [row schema value]
      (let [colName (keyword (.getName schema))
            type (.getType schema)
            typedValue (case (.name type)
                         "INT64" (.getLongValue value)
                         "INTEGER" (.getLongValue value)
                         "STRING" (.getStringValue value)
                         "NUMERIC" (.getNumericValue value)
                         "FLOAT" (.getDoubleValue value)
                         "FLOAT64" (.getDoubleValue value)
                         "BOOLEAN" (.getBooleanValue value)
                         "BOOL" (.getBooleanValue value)
                         "TIMESTAMP" (.getTimestampValue value)
                         (.getValue value))]
        (assoc row colName typedValue)))
    {}
    (zipmap schema fieldValueList)))

(defn tableresult->map
  "Converts BigQuery TableResult to Parsec dataset."
  [table-result]
  (let [schema (.. table-result (getSchema) (getFields))
        fieldValueList (iterator-seq (.. table-result (iterateAll) (iterator)))
        rows (map (partial fieldvaluelist->row schema) fieldValueList)]
    rows))

(defn build-bigquery
  "Builds a new BigQuery instance"
  [credentials]
  (let [builder (BigQueryOptions/newBuilder)
        _ (.setCredentials builder credentials)
        bigquery (.getService (.build builder))] bigquery))


; Options:
; :uri
; :query
(defn input-transform
  "Executes an BigQuery query."
  [options]
  (fn [context]
    (let [options' (eval-expression-without-row options context)
          {:keys [query
                  credentials]} options']
      (cond
        (nil? credentials) (throw (Exception. "input type BigQuery requires a \"credentials\" option"))
        (nil? query) (throw (Exception. "input type BigQuery requires a \"query\" option"))
        :else (let [builder (BigQueryOptions/newBuilder)
                    credentials (ServiceAccountCredentials/fromStream (string->stream credentials))
                    bigquery (build-bigquery credentials)
                    qb (QueryJobConfiguration/newBuilder query)
                    qjc (.build qb)
                    options (into-array BigQuery$JobOption '())
                    table-result (.query bigquery qjc options)
                    result' (tableresult->map table-result)]
                (assoc context :current-dataset result'))))))
