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

(ns parsec.parsers.csv
  (:require [parsec.helpers :refer :all]
            [clojure-csv.core :as csv]
            [semantic-csv.core :as scsv]))

(defn fix-csv-options
  [key value]
  (cond
    (= (name key) "delimiter") [key (first (char-array value))]
    (= (name key) "quote") [:quote-char (first (char-array value))]
    (= (name key) "eol") [:end-of-line value]
    :else [key value]))

(defn get-csv-options
  [options] (reduce-kv #(apply (partial assoc %1) (fix-csv-options %2 %3)) {} options))

(defn get-csv-headers
  "Gets a list of header or nil: if not defined or true, returns nil (use first row); if list, uses list as headers; if false, autogenerates column names."
  [options rows]
  (when (contains? options :headers))
  (let [headers (:headers options)]
    (cond
      (sequential? headers) headers
      (true? headers) nil
      (false? headers) (let [number-of-columns (if (zero? (count rows))
                                                 0
                                                 (apply max (map count rows)))]
                         (map #(str "col" %) (range 0 number-of-columns)))
      ;; Why??
      :else nil
      )))

(defn parse-csv
  "Reads CSV document"
  ([csv] (parse-csv csv {}))
  ([csv options]
   (when csv
     (let [options' (get-csv-options options)
           rows (mapply (partial csv/parse-csv csv) options')
           headers (get-csv-headers options rows)
           mappify-options (if (nil? headers) {} {:header headers})]
       (if (false? (:mappify options))
         rows
         (scsv/mappify mappify-options rows))))))
