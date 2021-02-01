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

(ns parsec.functions.parsingfunctions
  (:require [parsec.functions :refer [function-transform]]
            [parsec.helpers :refer :all]
            [parsec.helpers.functional :refer :all]
            [parsec.parsers.csv :as csv :only [parse-csv]]
            [parsec.parsers.json :as json :only [parse-json]]
            [parsec.parsers.xml :as xml :only [parse-xml]]
            [json-path :as json-path]))

;; Parsing Functions
(defmethod function-transform
  :parsejson [_ & args]
  (fn-with-args json/parse-json args))

(defmethod function-transform
  :parsecsv [_ & args]
  (fn-with-args csv/parse-csv args))

(defmethod function-transform
  :parsexml [_ & args]
  (fn-with-args xml/parse-xml args))

(defmethod function-transform
  :jsonpath [_ & args]
  (fn-with-args (partial only-maps-or-sequentials (fn [obj path] (json-path/at-path path obj))) args))

