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

(ns parsec.core
  (:require [parsec.parser :as parser]
            [parsec.executor :as executor]
            [parsec.functions :refer [function-transform]]
            [parsec.helpers :refer :all]
            [parsec.helpers.functional :refer :all]
            [taoensso.timbre :as timbre]
            [criterium.core :as crit]
            [clojure.walk :as walk]
            [fipp.clojure]))

(timbre/refer-timbre)

(timbre/set-level! :trace)
(timbre/set-config!
  {
   :fmt-output-fn (fn [{:keys [level message timestamp ns]}]
                    ;; <timestamp> <LEVEL> [<ns>] - <message>
                    (format "%s %s [%s]\n%s"
                            timestamp (-> level name clojure.string/upper-case) ns (or message "")))
   })

(def initial-context {:datastore {}})

(defn ttms
  [f & args]
  (let [[ns result] (criterium.core/time-body (apply f args))]
    [(/ ns 1000000) result]))

(defn get-memory-usage
  [] (zipmap [:free :total :max] (crit/memory)))

(defn pretty-print-tree
  [parsed-tree]
  (with-out-str
    (fipp.clojure/pprint parsed-tree)))

(defn validate-query
  "Validates a query the parsed expression tree without executing it. Returns a context without any datastore"
  [query]
  (try
    (let [[parse-time parsed-tree] (ttms parser/parse query)]
      {:parsedTree       parsed-tree
       :prettyParsedTree (pretty-print-tree parsed-tree)
       :memory           (get-memory-usage)
       :performance      {:parse parse-time}
       :errors           []})
    (catch Exception e {:errors [(mask-password-in-string (.toString e))]})))

(defn execute-query
  "Execute a query and return the resulting context"
  ([query] (execute-query query initial-context))
  ([query initial-context]
   (try
     (let [[parse-time parsed-tree] (ttms parser/parse query)]
       (try
         (let [[compile-time compiled-tree] (ttms executor/compile-query parsed-tree)
               [execute-time context] (ttms executor/execute compiled-tree initial-context)]
           (assoc context :parsedTree parsed-tree
                          :prettyParsedTree (pretty-print-tree parsed-tree)
                          :memory (get-memory-usage)
                          :performance {:parse   parse-time
                                        :compile compile-time
                                        :execute execute-time
                                        :total   (+ parse-time compile-time execute-time)}
                          :errors []))
         (catch Exception e {:parsedTree       parsed-tree
                             :prettyParsedTree (pretty-print-tree parsed-tree)
                             :datastore        []
                             :performance      {:parse parse-time}
                             :errors           [(.toString e)]})))
     (catch Exception e {:datastore []
                         :errors    [(mask-password-in-string (.toString e))]}))))

; Load function implementations
(load "functions/aggregationfunctions")
(load "functions/conditionalfunctions")
(load "functions/datefunctions")
(load "functions/functionalfunctions")
(load "functions/isfunctions")
(load "functions/listfunctions")
(load "functions/mapfunctions")
(load "functions/mathfunctions")
(load "functions/miscfunctions")
(load "functions/parsingfunctions")
(load "functions/stringfunctions")
(load "functions/typefunctions")

;; Function implementation of exec()
;; Execute queries and return the context/dataset/variable
;; Defined here to avoid circular depenencies
(defmethod function-transform
  :exec [_ & args]
  (fn [row context]
    (let [args' (map #(eval-expression % row context) args)
          [query options] args'
          new-context (cond
                        ;; Execute with shared context
                        (true? (:context options))
                        (execute-query query context)

                        (map? (:context options))
                        (execute-query query (:context options))

                        ;; Execute with new, isolated context
                        :else (execute-query query))]
      (when (not (nil? query))
        (cond
          ;; { :dataset "dataset-name" }, return data for dataset
          (:dataset options)
          (-> (:datastore new-context)
              (get (keyword (:dataset options)))
              (get :data))

          ;; { :variable "variable-name" }, return value of variable
          (:variable options)
          (get (:variables new-context) (:variable options))

          ;; Anything else, return entire context
          :else new-context)))))

;; Java interop methods
(defrecord ParsecMemoryUsage [free total max])
(defrecord ParsecPerformance [parse compile execute total])
(defrecord ParsecResultDataset [name temporary count columns data])
(defrecord ParsecResult [parsedTree prettyParsedTree ^ParsecMemoryUsage memory ^ParsecPerformance performance dataSets variables errors])

(defn convert-dataset-for-output
  [dataset]
  (let [data (walk/stringify-keys (:data dataset))
        columns (walk/stringify-keys (:columns dataset))
        dataset (assoc dataset :data data :columns columns)]
    (map->ParsecResultDataset dataset)))

(defn -validateQuery ^ParsecResult [query] (map->ParsecResult (validate-query query)))
(defn -executeQuery ^ParsecResult [query]
  (let [context (execute-query query)
        datasets (filter #(false? (:temporary %)) (vals (:datastore context)))
        datasets (map convert-dataset-for-output datasets)
        memory (:memory context)
        performance (:performance context)
        context (-> context
                    ;; Provide a filtered set of non-temporary
                    (assoc :dataSets datasets
                           :performance (when-not (nil? performance) (map->ParsecPerformance performance))
                           :memory (when-not (nil? memory) (map->ParsecMemoryUsage memory)))
                    (dissoc :datastore :current-dataset))]
    (map->ParsecResult context)))

(gen-class
  :name "parsec.core.Parsec"
  :prefix "-"
  :main false
  :methods [#^{:static true} [executeQuery [String] parsec.core.ParsecResult]
            #^{:static true} [validateQuery [String] parsec.core.ParsecResult]])
