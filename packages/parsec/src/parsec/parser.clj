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

(ns parsec.parser
  (:require [instaparse.core :as insta]
            [taoensso.timbre :as timbre]
            [clojure.set :refer [map-invert]])
  (:use [clojure.test :only (is)]
        [parsec.helpers])
  (:import (org.apache.commons.text StringEscapeUtils)))

(timbre/refer-timbre)

(def whitespace
  (insta/parser
    "whitespace = #'\\s+'"))

(def whitespace-or-comments
  (insta/parser
    "ws-or-comments = #'\\s+' | comments
     comments = (block-comment | line-comment)+
     block-comment = '/*' inside-block-comment* '*/'
     inside-block-comment =  !( '*/' | '/*' ) #'.' | block-comment
     line-comment = #'//(.*?)(\n|$)'"

    :auto-whitespace whitespace))

(def parsec-parser
  (time (insta/parser (clojure.java.io/resource "grammar.ebnf")
                      :string-ci true
                      :auto-whitespace whitespace-or-comments)))

(defn trim-string
  [quote-char string]
  (letfn [(trim [s] (subs s (count quote-char) (- (count s) (count quote-char))))
          (quotes [s] (clojure.string/replace s (str "\\" quote-char) quote-char))
          (unescape [s] (StringEscapeUtils/unescapeJava s))]
    (-> string
        trim
        quotes
        unescape)))

(defn simplify-function
  "Cleans and simplifies a :function node"
  [name & args]
  (condp = name

    ;; exec()
    :exec (if (= 1 (count args))
            (conj (into [:function :exec] args) [:expression {}])
            (into [:function :exec] args))

    ;; count()
    :count (if (= (first args) [:expression [:star]])
             (into [:function :count [:expression 1]] (rest args))
             (into [:function :count] args))

    ;; Aliases for various functions (left function is alias for right function):
    :avg (into [:function :mean] args)
    :cumulativeavg (into [:function :cumulativemean] args)
    :len (into [:function :length] args)
    :stddev_pop (into [:function :stddevp] args)
    :stddev_samp (into [:function :stddev] args)

    :lmean (into [:function :listmean] args)
    :lmax (into [:function :listmax] args)
    :lmin (into [:function :listmin] args)

    ;; Round with one argument - round to zero decimals
    :round (case (count args)
             1 [:function :round (first args) [:expression 0]]
             2 (into [:function :round] args)
             (throw (Exception. "round() accepts 1 or 2 arguments.")))

    ;; If case has odd number of arguments, treat the last as :else
    :case (let [args' (if (odd? (count args))
                        (conj (vec (butlast args)) [:expression true] (last args))
                        args)]
            (into [:function :case] args'))

    ;; Rewrite coalesce() in terms of case()
    :coalesce (reduce
                (fn [l i] (conj l [:expression [:not-equals-expression i [:expression :nil]]] i))
                [:function :case] args)

    ;; Provide defaults for random()
    :random (case (count args)
              0 [:function :random [:expression 0] [:expression 1]]
              1 [:function :random [:expression 0] (first args)]
              2 (into [:function :random] args)
              (throw (Exception. "random() accepts 0, 1, or 2 arguments.")))

    ;; Rewrite isToday() in terms of isBetween()
    :istoday (if (= 1 (count args))
               (conj (into [:function :isbetween2] args)
                     [:expression [:function :today]]
                     [:expression [:function :adddays [:expression [:function :today]] [:expression 1]]])
               (throw (Exception. "isToday() accepts 1 argument.")))

    ;; Rewrite isYesterday() in terms of isBetween()
    :isyesterday (if (= 1 (count args))
                   (conj (into [:function :isbetween2] args)
                         [:expression [:function :yesterday]]
                         [:expression [:function :today]])
                   (throw (Exception. "isYesterday() accepts 1 argument.")))

    ;; Rewrite isTomorrow() in terms of isBetween()
    :istomorrow (if (= 1 (count args))
                  (conj (into [:function :isbetween2] args)
                        [:expression [:function :tomorrow]]
                        [:expression [:function :adddays [:expression [:function :tomorrow]] [:expression 1]]])
                  (throw (Exception. "isYesterday() accepts 1 argument.")))

    ;; Rewrite isThisWeek() in terms of isBetween()
    :isthisweek (if (= 1 (count args))
                  (conj (into [:function :isbetween2] args)
                        [:expression [:function :startofweek [:expression [:function :today]]]]
                        [:expression [:function :addweeks [:function :startofweek [:expression [:function :today]]] [:expression 1]]])
                  (throw (Exception. "isThisWeek() accepts 1 argument.")))

    ;; Rewrite isLastWeek() in terms of isBetween()
    :islastweek (if (= 1 (count args))
                  (conj (into [:function :isbetween2] args)
                        [:expression [:function :minusweeks [:function :startofweek [:expression [:function :today]]] [:expression 1]]]
                        [:expression [:function :startofweek [:expression [:function :today]]]])
                  (throw (Exception. "isLastWeek() accepts 1 argument.")))

    ;; Rewrite isNextWeek() in terms of isBetween()
    :isnextweek (if (= 1 (count args))
                  (conj (into [:function :isbetween2] args)
                        [:expression [:function :addweeks [:function :startofweek [:expression [:function :today]]] [:expression 1]]]
                        [:expression [:function :addweeks [:function :startofweek [:expression [:function :today]]] [:expression 2]]])
                  (throw (Exception. "isNextWeek() accepts 1 argument.")))

    ;; Rewrite isThisMonth() in terms of isBetween()
    :isthismonth (if (= 1 (count args))
                   (conj (into [:function :isbetween2] args)
                         [:expression [:function :startofmonth [:expression [:function :today]]]]
                         [:expression [:function :addmonths [:function :startofmonth [:expression [:function :today]]] [:expression 1]]])
                   (throw (Exception. "isThisMonth() accepts 1 argument.")))

    ;; Rewrite isLastMonth() in terms of isBetween()
    :islastmonth (if (= 1 (count args))
                   (conj (into [:function :isbetween2] args)
                         [:expression [:function :minusmonths [:function :startofmonth [:expression [:function :today]]] [:expression 1]]]
                         [:expression [:function :startofmonth [:expression [:function :today]]]])
                   (throw (Exception. "isLastMonth() accepts 1 argument.")))

    ;; Rewrite isNextMonth() in terms of isBetween()
    :isnextmonth (if (= 1 (count args))
                   (conj (into [:function :isbetween2] args)
                         [:expression [:function :addmonths [:function :startofmonth [:expression [:function :today]]] [:expression 1]]]
                         [:expression [:function :addmonths [:function :startofmonth [:expression [:function :today]]] [:expression 2]]])
                   (throw (Exception. "isNextMonth() accepts 1 argument.")))


    ;; join()
    :join (case (count args)
            1 [:function :join (first args) ""]
            2 (into [:function :join] args))

    ;; Else do nothing
    (into [:function name] args)))

(defn simplify-aggregate-function
  "Cleans aggregate functions of one variable: adds a default predicate if not provided"
  ;; Rewrite fn(x) into fn(x, true)
  ([f x] [f x [:expression true]])
  ([f x predicate] [f x predicate]))

(defn simplify-aggregate2-function
  "Cleans aggregate functions of two variables: adds a default predicate if not provided"
  ;; Rewrite fn(x, y) into fn(x, y, true)
  ([f x y] [f x y [:expression true]])
  ([f x y predicate] [f x y predicate]))

(defn simplify-remove-statement
  "Rewrite the REMOVE statement as FILTER."
  [expr]
  [:filter-statement [:expression [:not-operation expr]]])

(defn simplify-output-statement
  "Simplifies the OUTPUT statement.  Adds a default output type if there isn't one."
  [arg & rest]
  ;; Ensures there is a default output type - :datastore
  (if (keyword? arg)
    (into [:output-statement arg] rest)
    (into [:output-statement :datastore arg] rest)))

(defn simplify-temp-statement
  "Replaces the TEMP statement with OUTPUT and the appropriate options."
  [name]
  [:output-statement :datastore {:name name :temporary true}])

(defn simplify-union-statement
  "Defaults to UNION DISTINCT if not specified"
  [& args]
  (if (= 1 (count args))
    (into [:union-statement :distinct] args)
    [:union-statement (ffirst args) (nth args 1)]))

(def cleanup-transformer
  {;; Basic data types and constants
   :TRUE                 #(do true)
   :FALSE                #(do false)
   :nil                  #(do nil)
   :boolean              identity
   :single-quoted-string (partial trim-string "'")
   :double-quoted-string (partial trim-string "\"")
   :triple-quoted-string (partial trim-string "'''")
   :string               identity
   :percent              #(do (keyword "percent"))
   :number               to-number
   :identifier           #(keyword %)
   :backtick-identifier  #(keyword (trim-string "`" %))
   :list                 (fn [& args] (into [:function :tolist] args))
   :map-item             (fn [key value] [(name key) value])
   :map                  (fn [& args] (apply concat [:function :tomap] args))

   :options              (fn [& options] (concat [:function :tolowercasemap] (map #(if (keyword? %) (name %) %) options)))

   ;; Statements
   :output-statement     simplify-output-statement
   :temp-statement       simplify-temp-statement
   :remove-statement     simplify-remove-statement

   :sort-column          (fn [column order] [column (order 0)])

   :set-term             (fn [v expr] [v expr])
   :assignment-term      (fn [id expr] [id expr])
   :auto-assignment-term (fn [expr] [(keyword (expr-to-string expr)) expr])
   :rename-terms         (fn [& terms] (map-invert (apply hash-map (flatten terms))))
   :rename-term          (fn [id1 id2] [id1 id2])

   :where-term           (fn [predicate] predicate)

   :union-statement      simplify-union-statement

   :join-type            (fn [& type] (if (empty? type) :NATURAL (ffirst type)))
   :join-identifier      (fn [id1 id2] [:join-identifier (keyword id1) (keyword id2)])

   :def-term             (fn [& args] (into [] args))

   ;; Functions
   :function-name        #(keyword (clojure.string/lower-case %))
   :function-args        (fn [& args] (into [] args))
   :function             simplify-function
   })

(defn clean-tree
  "Cleans an simplifies an expression tree"
  [tree]
  (insta/transform cleanup-transformer tree))

(defn raw-parse
  "Parse a Parsec query into an unsimplified expression tree."
  ([query] (raw-parse query :Q))
  ([query start]
   (let [tree (parsec-parser query :start start)]
     (if (insta/failure? tree)
       (throw (Exception. (with-out-str (print tree))))
       tree))))

(defn parse
  "Parse a Parsec query into an expression tree and simplify."
  ([query] (parse query :Q))
  ([query start] (-> query
                     (raw-parse start)
                     clean-tree)))

;(use 'criterium.core)
;(with-progress-reporting
;  (bench
;    (parsec-parser "input mock | col1=   5") :verbose))
