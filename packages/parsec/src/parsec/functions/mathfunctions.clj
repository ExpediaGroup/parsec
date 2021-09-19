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

(ns parsec.functions.mathfunctions
  (:require [parsec.functions :refer [function-transform]]
            [parsec.helpers :refer :all]
            [parsec.helpers.functional :refer :all]
            [parsec.helpers.maths :as maths]
            [clojure.math.numeric-tower :as math]
            [clojure.algo.generic.math-functions :as algo-math])
  (:import (java.lang Math)))

;; Math Functions
(defmethod function-transform
  :e [& _] (fn [_ _] (Math/E)))
(defmethod function-transform
  :pi [& _] (fn [_ _] (Math/PI)))
(defmethod function-transform
  :nan [& _] (fn [_ _] (Double/NaN)))
(defmethod function-transform
  :infinity [& _] (fn [_ _] (Double/POSITIVE_INFINITY)))
(defmethod function-transform
  :neginfinity [& _] (fn [_ _] (Double/NEGATIVE_INFINITY)))
(defmethod function-transform
  :ceil [_ & args] (fn-with-one-arg-handles-nil math/ceil args))
(defmethod function-transform
  :floor [_ & args] (fn-with-one-arg-handles-nil math/floor args))
(defmethod function-transform
  :sign [_ & args] (fn-with-one-arg-handles-nil #(if (Double/isNaN %) Double/NaN (algo-math/sgn %)) args))
(defmethod function-transform
  :abs [_ & args] (fn-with-one-arg-handles-nil math/abs args))
(defmethod function-transform
  :sqrt [_ & args] (fn-with-one-arg-handles-nil math/sqrt args))
(defmethod function-transform
  :pow [_ & args] (fn-with-two-args-handles-nil math/expt args))
(defmethod function-transform
  :round [_ & args] (fn-with-two-args-handles-nil maths/round args))
(defmethod function-transform
  :gcd [_ & args] (fn-with-args-handles-nil (partial when-integer-args math/gcd) args))
(defmethod function-transform
  :lcm [_ & args] (fn-with-args-handles-nil (partial when-integer-args math/lcm) args))
(defmethod function-transform
  :ln [_ & args] (fn-with-one-arg-handles-nil (partial when-number-args #(Math/log %)) args))
(defmethod function-transform
  :log [_ & args] (fn-with-one-arg-handles-nil (partial when-number-args #(Math/log10 %)) args))

;; Trig functions
(defmethod function-transform
  :degrees [_ & args] (fn-with-one-arg-handles-nil #(Math/toDegrees %) args))
(defmethod function-transform
  :radians [_ & args] (fn-with-one-arg-handles-nil #(Math/toRadians %) args))
(defmethod function-transform
  :sin [_ & args] (fn-with-one-arg-handles-nil #(Math/sin %) args))
(defmethod function-transform
  :cos [_ & args] (fn-with-one-arg-handles-nil #(Math/cos %) args))
(defmethod function-transform
  :tan [_ & args] (fn-with-one-arg-handles-nil #(Math/tan %) args))
(defmethod function-transform
  :sinh [_ & args] (fn-with-one-arg-handles-nil #(Math/sinh %) args))
(defmethod function-transform
  :cosh [_ & args] (fn-with-one-arg-handles-nil #(Math/cosh %) args))
(defmethod function-transform
  :tanh [_ & args] (fn-with-one-arg-handles-nil #(Math/tanh %) args))
(defmethod function-transform
  :asin [_ & args] (fn-with-one-arg-handles-nil #(Math/asin %) args))
(defmethod function-transform
  :acos [_ & args] (fn-with-one-arg-handles-nil #(Math/acos %) args))
(defmethod function-transform
  :atan [_ & args] (fn-with-one-arg-handles-nil #(Math/atan %) args))
(defmethod function-transform
  :atan2 [_ & args] (fn-with-two-args-handles-nil #(Math/atan2 % %2) args))


;; Within() and within%()
(defmethod function-transform
  :within [_ & args]
  (fn-with-args-handles-nil
    (fn [operand1 operand2 tolerance]
      (<= (Math/abs ^Double (- operand1 operand2)) tolerance)) args))

(defmethod function-transform
  :within% [_ & args]
  (fn-with-args-handles-nil
    (fn [operand1 operand2 percent]
      (let [tolerance (* percent operand2)]
        (<= (Math/abs ^Double (- operand1 operand2)) tolerance))) args))


;; Greastest() and Least()
(defmethod function-transform
  :greatest [_ & args] (fn-with-args-handles-nil max args))

(defmethod function-transform
  :least [_ & args] (fn-with-args-handles-nil min args))


;; Probability functions

(defmethod function-transform
  :pdfnormal [_ & args] (fn-with-args-handles-nil (fn ([x] (incanter.stats/pdf-normal x))
                                                    ([x mean sd] (incanter.stats/pdf-normal x :mean mean :sd sd))) args))
(defmethod function-transform
  :pdfpoisson [_ & args] (fn-with-args-handles-nil (fn ([x] (incanter.stats/pdf-poisson x))
                                                     ([x lambda] (incanter.stats/pdf-poisson x :lambda lambda))) args))
(defmethod function-transform
  :pdfuniform [_ & args] (fn-with-args-handles-nil (fn ([x] (incanter.stats/pdf-uniform x))
                                                     ([x min max] (incanter.stats/pdf-uniform x :min (double min) :max (double max)))) args))

(defmethod function-transform
  :cdfnormal [_ & args] (fn-with-args-handles-nil (fn ([x] (incanter.stats/cdf-normal x))
                                         ([x mean sd] (incanter.stats/cdf-normal x :mean mean :sd sd))) args))
(defmethod function-transform
  :cdfpoisson [_ & args] (fn-with-args-handles-nil (fn ([x] (incanter.stats/cdf-poisson x))
                                        ([x lambda] (incanter.stats/cdf-poisson x :lambda lambda))) args))
(defmethod function-transform
  :cdfuniform [_ & args] (fn-with-args-handles-nil (fn ([x] (incanter.stats/cdf-uniform x))
                                                     ([x min max] (incanter.stats/cdf-uniform x :min min :max max))) args))
