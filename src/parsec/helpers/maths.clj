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

(ns parsec.helpers.maths
  (:require [clojure.math.numeric-tower :as math])
  (:import (cern.jet.stat.tdouble DoubleDescriptive)
           (cern.colt.list.tdouble DoubleArrayList)
           (java.lang Math)))

(defn geometricMean
  "Returns the geometric mean of a sequence"
  [x]
  (when-not (nil? x)
    (DoubleDescriptive/geometricMean (DoubleArrayList. (double-array x)))))

(defn stddev-pop
  "Returns the population standard deviation of a sequence"
  [x]
  (when-not (nil? x)
    (math/sqrt
      (DoubleDescriptive/variance
        (count x)
        (reduce + x)
        (DoubleDescriptive/sumOfSquares (DoubleArrayList. (double-array x)))))))

(defn round
  "Returns the number rounded to some number of digits"
  [value precision]
  (cond
    (Double/isNaN value) value
    (Double/isInfinite value) value
    (= Double/POSITIVE_INFINITY precision) value
    (= Double/NEGATIVE_INFINITY precision) 0.0
    (zero? precision) (math/round value)
    :else (let [factor (Math/pow 10 precision)]
            (/ (Math/round ^Double (* value factor)) factor))))

(defn mode
  "Returns the mode of a sequence"
  [x]
  (first (last (sort-by second (frequencies x)))))
