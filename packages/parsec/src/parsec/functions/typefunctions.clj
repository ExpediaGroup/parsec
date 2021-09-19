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

(ns parsec.functions.typefunctions
  (:require [parsec.functions :refer [function-transform]]
            [parsec.helpers :refer :all]
            [parsec.helpers.functional :refer :all]
            [clojure.string :refer [lower-case]]))

; type()
(defmethod function-transform
  :type [_ & args] (fn-with-one-arg #(if (nil? %) nil (.getName (type %))) args))

;; Conversion Functions
(defmethod function-transform
  :toboolean [_ & args] (fn-with-one-arg to-boolean args))
(defmethod function-transform
  :tostring [_ & args] (fn-with-args to-string args))
(defmethod function-transform
  :tonumber [_ & args] (fn-with-one-arg to-number args))
(defmethod function-transform
  :tointeger [_ & args] (fn-with-one-arg to-integer args))
(defmethod function-transform
  :todouble [_ & args] (fn-with-one-arg to-double args))
(defmethod function-transform
  :todate [_ & args] (fn-with-args to-date args))

(defmethod function-transform
  :tolist [_ & args]
  (fn [row context]
    (map #(eval-expression % row context) args)))

(defmethod function-transform
  :tomap [_ & args]
  (let [keys (take-nth 2 args)
        vals (take-nth 2 (rest args))]
    (fn [row context]
      (let [keys' (map #(keyword (eval-expression % row context)) keys)
            vals' (map #(eval-expression % row context) vals)]
        (zipmap keys' vals')))))

;; tomap() but with lowercase keys
(defmethod function-transform
  :tolowercasemap [_ & args]
  (let [keys (take-nth 2 args)
        vals (take-nth 2 (rest args))]
    (fn [row context]
      (let [keys' (map #(keyword (lower-case (eval-expression % row context))) keys)
            vals' (map #(eval-expression % row context) vals)]
        (zipmap keys' vals')))))
