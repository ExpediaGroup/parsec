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

(ns parsec.input.mongodb
  (:require [parsec.helpers :refer :all]
            [monglorious.core :as mongo]))

(defn process-results
  "Converts MongoDB results to Parsec dataset."
  [results]
  (cond
    (number? results) (list {:count results})
    (map? results) (list results)
    (and (sequential? results) (every? string? results)) (map (fn [s] {:name s}) results)
    :else results))

; Options:
; :uri
; :query
(defn input-transform
  "Executes an MongoDB query."
  [options]
  (fn [context]
    (let [options' (eval-expression-without-row options context)
          {:keys [uri
                  query]} options']
      (cond
        (nil? uri) (throw (Exception. "input type MongoDB requires a \"uri\" option"))
        (nil? query) (throw (Exception. "input type MongoDB requires a \"query\" option"))
        :else (let [response (mongo/execute uri query)]
                (assoc context :current-dataset (process-results response)))))))
