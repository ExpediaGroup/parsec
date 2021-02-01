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

(ns parsec.expressions
  (:require [clojure.math.numeric-tower :as math]
            [clj-time.core :as time])
  (:use [parsec.helpers]))

(defn expression-transform
  "Transforms an expression token into a function with a row argument."
  [expression]
  (fn [row context] (eval-expression expression row context)))

(defn negation-transform
  "Transforms a negation-operation token into a function."
  [expr1]
  (fn [row context]
    (let [v1 (eval-expression expr1 row context)]
      (cond
        (number? v1) (- v1)
        (nil? v1) nil
        :else (throw (get-unsupported-exception "Negation" v1))))))

(defn addition-transform
  "Transforms a addition-operation token into a function."
  [expr1 expr2]
  (fn [row context]
    (let [v1 (eval-expression expr1 row context)
          v2 (eval-expression expr2 row context)]
      (cond
        (and (number? v1) (number? v2)) (+ v1 v2)
        (or (string? v1) (string? v2)) (str v1 v2)
        (or (nil? v1) (nil? v2)) nil
        :else (throw (get-unsupported-exception "Addition" v1 v2))))))

(defn subtraction-transform
  "Transforms a subtraction-operation token into a function."
  [expr1 expr2]
  (fn [row context]
    (let [v1 (eval-expression expr1 row context)
          v2 (eval-expression expr2 row context)]
      (cond
        (and (number? v1) (number? v2)) (- v1 v2)
        (or (nil? v1) (nil? v2)) nil
        (and (date? v1) (date? v2)) (time/interval v2 v1)
        :else (throw (get-unsupported-exception "Subtraction" v1 v2))))))

(defn multiplication-transform
  "Transforms a multiplication-operation token into a function."
  [expr1 expr2]
  (fn [row context]
    (let [v1 (eval-expression expr1 row context)
          v2 (eval-expression expr2 row context)]
      (cond
        (and (number? v1) (number? v2)) (* v1 v2)
        (or (nil? v1) (nil? v2)) nil
        :else (throw (get-unsupported-exception "Multiplication" v1 v2))))))

(defn division-transform
  "Transforms a division-operation token into a function."
  [expr1 expr2]
  (fn [row context]
    (let [v1 (eval-expression expr1 row context)
          v2 (eval-expression expr2 row context)]
      (cond
        (and
          (number? v1)
          (number? v2)) (if (zero? v2)
                                  (cond
                                    (zero? v1) Double/NaN
                                    (pos? v1) Double/POSITIVE_INFINITY
                                    (neg? v1) Double/NEGATIVE_INFINITY)
                                  ; Happy path
                                  (/ v1 v2))
        (or (nil? v1) (nil? v2)) nil
        :else (throw (get-unsupported-exception "Division" v1 v2))))))

(defn modulus-transform
  "Transforms a modulus-operation token into a function."
  [expr1 expr2]
  (fn [row context]
    (let [v1 (eval-expression expr1 row context)
          v2 (eval-expression expr2 row context)]
      (cond
        (and (number? v1) (number? v2)) (if (zero? v2)
                                          Double/NaN
                                          (mod v1 v2))
        (or (nil? v1) (nil? v2)) nil
        :else (throw (get-unsupported-exception "Modulus" v1 v2))))))


(defn exponent-transform
  "Transforms a exponent-operation token into a function."
  [expr1 expr2]
  (fn [row context]
    (let [v1 (eval-expression expr1 row context)
          v2 (eval-expression expr2 row context)]
      (cond
        (and (number? v1) (number? v2)) (math/expt v1 v2)
        (or (nil? v1) (nil? v2)) nil
        :else (throw (get-unsupported-exception "Exponent" v1 v2))))))

(defn join-identifier-transform
  "Transforms a join-identifier into a function."
  [alias column]
  (fn [row context]
    (let [join-targets (:join-targets context)
          target (get join-targets alias)]
      (if (nil? target)
        (throw (Exception. (str "Unknown join alias: " alias)))
        (get target column)))))
