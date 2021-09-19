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

(ns parsec.functions
  (:require [parsec.helpers :refer :all]))

(defn isexist-transform
  [column]
  (fn [row _] (contains? row column)))

;; Defines a multimethod to handle each supported function
;; A defmethod must be added for each function
;; Maps from a keyword to a (fn [row context])
;; Extra arguments are ignored (generally)
(defmulti function-transform (fn [name & _] name))

;; Create a type for storing UDF definitions/implementations
(defrecord ParsecUdf [args implementation])

;; Multimethod default--runs if no other matching function found
;; Check for user-defined functions
(defmethod function-transform
  :default [name & args]
  (fn [row context]
    ;; Check for user-defined functions in the context
    (let [functions (:functions context)
          function (get functions name)]
      (if (nil? function)
        ;; Couldn't find a UDF, throw an error
        (throw (Exception. (str "Unknown function: " name)))

        ;; Evaluate UDF: evaluate args, map args to names, then execute
        (let [eval-args (map #(eval-expression % row context) args)
              udf (get function :udf)
              impl (get udf :implementation)]
          (impl eval-args context))))))
