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

(ns parsec.helpers.functional
  (:require [parsec.helpers :refer :all]))

(defn fn-with-args
  "Creates a (fn [row context]) that wraps a given function of N variables."
  [f args]
  (fn [row context] (apply f (map #(eval-expression % row context) args))))

(defn fn-with-args-handles-nil
  "Creates a (fn [row context]) that wraps a given function of N variables. If any of the args are nil, returns nil."
  [f args]
  (fn [row context] (let [args' (map #(eval-expression % row context) args)]
                      (if (some nil? args') nil (apply f args')))))

(defn fn-with-one-arg
  "Creates a (fn [row context]) that wraps a given function of one variable."
  [f args]
  (let [first-arg (first args)]
    (fn [row context] (f (eval-expression first-arg row context)))))

(defn fn-with-one-arg-handles-nil
  "Creates a (fn [row context]) that wraps a given function of one variable. If the args are nil, returns nil."
  [f args]
  (let [first-arg (first args)]
    (fn [row context]
      (let [x (eval-expression first-arg row context)]
        (when-not (nil? x)
          (f x)
          ;; Else nil
          )))))

;; TODO: Generalize this function for N args.
(defn fn-with-two-args-handles-nil
  "Creates a (fn [row context]) that wraps a given function of two variables. If either arg is nil, returns nil."
  [f args]
  (let [first-arg (nth args 0)
        second-arg (nth args 1)]
    (fn [row context]
      (let [x (eval-expression first-arg row context)
            y (eval-expression second-arg row context)]
        (when-not (or (nil? x) (nil? y))
          (f x y)
          ;; Else nil
          )))))
