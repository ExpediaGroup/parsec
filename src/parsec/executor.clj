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

(ns parsec.executor
  (:require [instaparse.core :as insta])
  (:require [parsec.statements :as statements]
            [parsec.expressions :as expressions]
            [parsec.boolean-expressions :as boolean-expressions]
            [parsec.functions :as functions])
  (:import (parsec.functions ParsecUdf)))

(defn query-engine
  "Compiles a series of Parsec statements."
  [& statements]
  (apply comp (reverse statements)))

(def execution-transformer
  {;; Query is a list of statements, where the result
   ;; of the first statement is the input to the 2nd
   ;; statement, etc.
   :query                        query-engine

   ;; Input statements produce a new dataset
   :input-statement              statements/input-statement
   :output-statement             statements/output-statement

   ;; Def / Set statements
   :def-statement                statements/def-statement
   :set-statement                statements/set-statement

   ; Statements are transformed into (fn [dataset]),
   ; and output a dataset to be chained to the next statement.
   :head-statement               statements/head-statement
   :tail-statement               statements/tail-statement
   :reverse-statement            statements/reverse-statement
   :rownumber-statement          statements/rownumber-statement
   :select-statement             statements/select-statement
   :unselect-statement           statements/unselect-statement
   :filter-statement             statements/filter-statement
   :sort-statement               statements/sort-statement
   :rename-statement             statements/rename-statement
   :assignment-statement         statements/assignment-statement
   :stats-statement              statements/stats-statement
   :sleep-statement              statements/sleep-statement
   :sample-statement             statements/sample-statement
   :pivot-statement              statements/pivot-statement
   :unpivot-statement            statements/unpivot-statement
   :project-statement            statements/project-statement
   :union-statement              statements/union-statement
   :distinct-statement           statements/distinct-statement
   :join-statement               statements/join-statement
   :benchmark-statement          statements/benchmark-statement

   ; Expressions are transformed into (fn [row dataset])
   ; and output a value
   :expression                   expressions/expression-transform
   :negation-operation           expressions/negation-transform
   :addition-operation           expressions/addition-transform
   :subtraction-operation        expressions/subtraction-transform
   :multiplication-operation     expressions/multiplication-transform
   :division-operation           expressions/division-transform
   :modulus-operation            expressions/modulus-transform
   :exponent-operation           expressions/exponent-transform
   :join-identifier              expressions/join-identifier-transform

   :not-operation                boolean-expressions/not-transform
   :and-operation                boolean-expressions/and-transform
   :or-operation                 boolean-expressions/or-transform
   :xor-operation                boolean-expressions/xor-transform

   :equals-expression            boolean-expressions/equals-transform
   :not-equals-expression        boolean-expressions/not-equals-transform
   :less-than-expression         boolean-expressions/less-than-transform
   :greater-than-expression      boolean-expressions/greater-than-transform
   :less-or-equals-expression    boolean-expressions/less-or-equals-transform
   :greater-or-equals-expression boolean-expressions/greater-or-equals-transform

   :function                     functions/function-transform

   :function-impl                (fn [fn-args impl]
                                   ;; Return a UDF type containing the argument names and implementation
                                   ;; When invoked with a seq of args and the context, will convert the args into a map
                                   ;; Implementation should accept [row context] -- like a standard :expression
                                   (ParsecUdf.
                                     fn-args
                                     (fn [args context]
                                       (let [args-map (zipmap fn-args args)]
                                         (impl args-map context)))))

   ;; Special functions
   :isexist-function             functions/isexist-transform
   })

(defn compile-query
  "Compile a parsed Parsec expression tree into functions."
  [parsed-tree]
  (->> parsed-tree
       ;; Ensure each query ends with an output-statement
       ;; to store the result into the context.datastore
       (map-indexed
         (fn [i query]
           (if (= :output-statement (nth (last query) 0))
             query
             (conj query [:output-statement :datastore { :auto true }])
             )))
       ;; Transform the tree tokens into functions
       (insta/transform execution-transformer)))

(defn execute
  "Executes a compiled Parsec expression tree (containing
  one or more queries). An initial context object is passed
  through the tree and is updated with results along the way.
  Returns the modified context."
  [compiled-tree initial-context]
  (let [context' (reduce
                   (fn [context query] (query (dissoc context :current-dataset)))
                   initial-context
                   compiled-tree)]
    context'))
