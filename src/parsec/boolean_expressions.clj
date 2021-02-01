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

(ns parsec.boolean-expressions
  (:require [clojure.edn :as edn]
            [clj-time.core :as time])
  (:use [parsec.helpers]))

(defn not-transform
  "Transforms a not-operation token into a function."
  [expr1]
  (fn [row context]
    (let [v1 (try-to-boolean (eval-expression expr1 row context))]
      (cond
        (nil? v1) nil
        (boolean? v1) (not v1)
        :else (throw (get-unsupported-exception "Boolean NOT" v1))))))

(defn and-transform
  "Transforms an and-operation token into a function."
  [expr1 expr2]
  (fn [row context]
    (let [v1 (try-to-boolean (eval-expression expr1 row context))
          v2 (try-to-boolean (eval-expression expr2 row context))]
      (cond
        (or (nil? v1) (nil? v2)) nil
        (and (boolean? v1) (boolean? v2)) (and v1 v2)
        :else (throw (get-unsupported-exception "Boolean AND" v1 v2))))))

(defn or-transform
  "Transforms an or-operation token into a function."
  [expr1 expr2]
  (fn [row context]
    (let [v1 (try-to-boolean (eval-expression expr1 row context))
          v2 (try-to-boolean (eval-expression expr2 row context))]
      (cond
        (or (nil? v1) (nil? v2)) nil
        (and (boolean? v1) (boolean? v2)) (or v1 v2)
        :else (throw (get-unsupported-exception "Boolean OR" v1 v2))))))

(defn xor-transform
  "Transforms an xor-operation token into a function."
  [expr1 expr2]
  (fn [row context]
    (let [v1 (try-to-boolean (eval-expression expr1 row context))
          v2 (try-to-boolean (eval-expression expr2 row context))]
      (cond
        (or (nil? v1) (nil? v2)) nil
        (and (boolean? v1) (boolean? v2)) (xor v1 v2)
        :else (throw (get-unsupported-exception "Boolean XOR" v1 v2))))))

(defn equals-transform
  "Transforms an equals-expression token into a function."
  [expr1 expr2]
  (fn [row context]
    (let [v1 (eval-expression expr1 row context)
          v2 (eval-expression expr2 row context)]
      (cond
        (or (and (boolean? v1) (number? v2))
            (and (number? v1) (boolean? v2))) (= (try-to-boolean v1) (try-to-boolean v2))
        (and (number? v1) (number? v2)) (== v1 v2)
        :else (= v1 v2)))))

(defn not-equals-transform
  "Transforms an not-equals-expression token into a function."
  [expr1 expr2]
  (fn [row context]
    (let [v1 (eval-expression expr1 row context)
          v2 (eval-expression expr2 row context)]
      (cond
        (or (and (boolean? v1) (number? v2))
            (and (number? v1) (boolean? v2))) (not= (try-to-boolean v1) (try-to-boolean v2))
        (and (number? v1) (number? v2)) (not (== v1 v2))
        :else (not= v1 v2)))))

(defn less-than-transform
  "Transforms an less-than-expression token into a function."
  [expr1 expr2]
  (fn [row context]
    (let [v1 (eval-expression expr1 row context)
          v2 (eval-expression expr2 row context)]
      (cond
        (and (string? v1) (string? v2)) (if (neg? (compare v1 v2)) true false)
        (and (number? v1) (number? v2)) (< v1 v2)
        (and (date? v1) (date? v2)) (time/before? v1 v2)
        :else nil))))

(defn greater-than-transform
  "Transforms an greater-than-expression token into a function."
  [expr1 expr2]
  (fn [row context]
    (let [v1 (eval-expression expr1 row context)
          v2 (eval-expression expr2 row context)]
      (cond
        (and (string? v1) (string? v2)) (if (pos? (compare v1 v2)) true false)
        (and (number? v1) (number? v2)) (> v1 v2)
        (and (date? v1) (date? v2)) (time/after? v1 v2)
        :else nil))))

(defn less-or-equals-transform
  "Transforms an less-or-equals-expression token into a function."
  [expr1 expr2]
  (fn [row context]
    (let [v1 (eval-expression expr1 row context)
          v2 (eval-expression expr2 row context)]
      (cond
        (and (string? v1) (string? v2)) (if (<= (compare v1 v2) 0) true false)
        (and (number? v1) (number? v2)) (<= v1 v2)
        (and (date? v1) (date? v2)) (or (= v1 v2) (time/before? v1 v2))
        :else nil))))

(defn greater-or-equals-transform
  "Transforms an greater-or-equals-expression token into a function."
  [expr1 expr2]
  (fn [row context]
    (let [v1 (eval-expression expr1 row context)
          v2 (eval-expression expr2 row context)]
      (cond
        (and (string? v1) (string? v2)) (if (>= (compare v1 v2) 0) true false)
        (and (number? v1) (number? v2)) (>= v1 v2)
        (and (date? v1) (date? v2)) (or (= v1 v2) (time/after? v1 v2))
        :else nil))))
