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

(ns parsec.functions.listfunctions
  (:require [parsec.functions :refer [function-transform]]
            [parsec.helpers :refer :all]
            [parsec.helpers.functional :refer :all]
            [incanter.stats :as stats]
            [parsec.helpers.maths :as maths]))

;; List Functions
(defmethod function-transform
  :length [_ & args] (fn-with-one-arg
                       (fn [x]
                         (cond
                           (string? x) (count x)
                           (sequential? x) (count x)
                           :else nil)) args))

(defmethod function-transform
  :index [_ & args] (fn-with-args #(if (in? (range (count %)) %2) (nth % %2) nil) args))
(defmethod function-transform
  :contains [_ & args] (fn-with-args (fn [coll key] (in? coll key)) args))
(defmethod function-transform
  :reverse [_ & args] (fn-with-one-arg (fn [x]
                                         (cond
                                           (string? x) (clojure.string/reverse x)
                                           (sequential? x) (reverse x)
                                           :else nil)) args))
(defmethod function-transform
  :peek [_ & args] (fn-with-one-arg (partial only-sequentials first) args))
(defmethod function-transform
  :peeklast [_ & args] (fn-with-one-arg (partial only-sequentials last) args))
(defmethod function-transform
  :pop [_ & args] (fn-with-args (partial only-sequentials-as-lists pop) args))
(defmethod function-transform
  :push [_ & args] (fn-with-args (partial only-sequentials (fn [coll & args] (concat coll (apply list args)))) args))
(defmethod function-transform
  :concat [_ & args] (fn-with-args (fn [& args] (apply concat (map #(if (sequential? %) % (list %)) args))) args))
(defmethod function-transform
  :distinct [_ & args] (fn-with-args (partial only-sequentials distinct) args))

(defmethod function-transform
  :flatten [_ & args] (fn-with-args-handles-nil (partial mapcat #(if (sequential? %) % (vector %))) (take 1 args)))
(defmethod function-transform
  :flattendeep [_ & args] (fn-with-args-handles-nil flatten (take 1 args)))

(defmethod function-transform
  :range [_ & args]
  (fn-with-args (fn ([start end step] (range start end (if (zero? step) 1 step)))
                  ([start end] (range start end))
                  ([start] (range start))) args))

(defmethod function-transform
  :listmean [_ & args] (fn-with-args (partial only-sequentials stats/mean) args))
(defmethod function-transform
  :liststddev [_ & args] (fn-with-args (partial only-sequentials stats/sd) args))
(defmethod function-transform
  :liststddevp [_ & args] (fn-with-args (partial only-sequentials maths/stddev-pop) args))
(defmethod function-transform
  :listmax [_ & args] (fn-with-args (partial only-sequentials #(if (empty? %) nil (apply max %))) args))
(defmethod function-transform
  :listmin [_ & args] (fn-with-args (partial only-sequentials #(if (empty? %) nil (apply min %))) args))
