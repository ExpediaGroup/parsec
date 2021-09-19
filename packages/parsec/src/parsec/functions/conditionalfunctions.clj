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

(ns parsec.functions.conditionalfunctions
  (:require [parsec.functions :refer [function-transform]]
            [parsec.helpers :refer :all]
            [parsec.helpers.functional :refer :all]))

(defn if-transform
  "Implementation for If(predicate,whenTrue,whenFalse). If whenTrue/whenFalse are missing, they will be null"
  [[predicate, whenTrue, whenFalse]]
  (fn [row context]
    (let [p (eval-expression predicate row context)]
      (cond
        (or (nil? p)
            (false? p)
            (= 0 p)) (eval-expression whenFalse row context)
        :else (eval-expression whenTrue row context)))))

(defn case-transform
  "Implementation for case(p1,v1,p2,v2,..)"
  [args]
  (if (odd? (count args))
    (throw (Exception. "case() requires an even-number of arguments, in pairs of: predicate, whenTrue"))
    ;(apply cond args)
    (fn [row context]
      (letfn [(case-test
                [args]
                (if (empty? args)
                  nil
                  (let [predicate (nth args 0)
                        p (eval-expression predicate row context)
                        whenTrue (nth args 1)]
                    (if (or (nil? p) (false? p) (= 0 p))
                      (recur (nthrest args 2))
                      (eval-expression whenTrue row context)))))]
        (case-test args)))))

;; Conditional Functions
(defmethod function-transform
  :if [_ & args] (if-transform args))
(defmethod function-transform
  :case [_ & args] (case-transform args))
