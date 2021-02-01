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

(ns parsec.functions.isfunctions
  (:require [parsec.functions :refer [function-transform]]
            [parsec.helpers :refer :all]
            [parsec.helpers.functional :refer :all]
            [clojure.math.numeric-tower :as math]))

(defmethod function-transform
  :isnull [_ & args] (fn-with-one-arg nil? args))
(defmethod function-transform
  :isnan [_ & args] (fn-with-one-arg #(if (number? %) (Double/isNaN %) false) args))
(defmethod function-transform
  :isinfinite [_ & args] (fn-with-one-arg #(if (number? %) (Double/isInfinite %) false) args))
(defmethod function-transform
  :isfinite [_ & args] (fn-with-one-arg #(if (number? %) (<= (math/abs %) Double/MAX_VALUE) false) args))
(defmethod function-transform
  :isdate [_ & args] (fn-with-one-arg date? args))
(defmethod function-transform
  :isboolean [_ & args] (fn-with-one-arg boolean? args))
(defmethod function-transform
  :isnumber [_ & args] (fn-with-one-arg number? args))
(defmethod function-transform
  :isinteger [_ & args] (fn-with-one-arg integer? args))
(defmethod function-transform
  :isdouble [_ & args] (fn-with-one-arg double? args))
(defmethod function-transform
  :isratio [_ & args] (fn-with-one-arg ratio? args))
(defmethod function-transform
  :isstring [_ & args] (fn-with-one-arg string? args))
(defmethod function-transform
  :islist [_ & args] (fn-with-one-arg sequential? args))
(defmethod function-transform
  :ismap [_ & args] (fn-with-args map? args))
(defmethod function-transform
  :isempty [_ & args] (fn-with-args is-empty? args))
