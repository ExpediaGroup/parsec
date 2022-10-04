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

(ns parsec.statements
  (:require [taoensso.timbre :as timbre]
            [criterium.core :as crit]
            [incanter.stats :as stats]

            [parsec.input.bigquery :as bigquery :only [input-transform]]
            [parsec.input.graphite :as graphite :only [input-transform]]
            [parsec.input.http :as http :only [input-transform]]
            [parsec.input.influxdb :as influxdb :only [input-transform]]
            [parsec.input.jdbc :as jdbc :only [input-transform]]
            [parsec.input.mock :as mock :only [input-transform]]
            [parsec.input.mongodb :as mongodb :only [input-transform]]
            [parsec.input.s3 :as s3 :only [input-transform]]
            [parsec.input.smb :as smb :only [input-transform]]

            [parsec.helpers :refer :all])

  (:import (java.util Collection)))

(timbre/refer-timbre)



(defn dataset-statement
  "Wrapper for statements that operate only on the current dataset. Returns a new
  context with an updated :current-dataset value."
  [statement]
  (fn [context]
    (let [dataset (:current-dataset context)]
      (assoc context :current-dataset (statement dataset)))))

(defn input-statement
  "Loads a new dataset."
  [type options]
  (case type
    
    :datastore
    (fn [context]
      (let [options' (eval-expression-without-row options context)
            name (keyword (:name options'))
            datastore (:datastore context)
            dataset (get datastore name)]
        (assoc context :current-dataset (:data dataset))))

    :bigquery
    (bigquery/input-transform options)
    
    :docs
    (docs/input-transform options)

    :graphite
    (graphite/input-transform options)

    :http
    (http/input-transform options)

    :influxdb
    (influxdb/input-transform options)

    :jdbc
    (jdbc/input-transform options)
    
    :mock
    (mock/input-transform options)

    :mongodb
    (mongodb/input-transform options)

    :s3
    (s3/input-transform options)

    :smb
    (smb/input-transform options)

    (throw (Exception. (str "Unsupported Input type: " type)))))

(defn output-statement
  "Outputs the current dataset and passes it through unchanged."
  [type options]
  (fn [context]
    (let [dataset (doall (:current-dataset context))
          options' (eval-expression-without-row options context)]
      (if (nil? dataset)
        context
        (case type

          ;; Default output type, stores the current dataset in the context.
          :datastore (let [name (:name options')
                           name (if (or (true? (:auto options')) (nil? name))
                                  (let [non-temp (filter #(false? (:temporary %)) (vals (:datastore context)))]
                                    (str (count non-temp)))
                                  (:name options'))
                           temp (:temporary options')]
                       (assoc-in context [:datastore (keyword name)]
                                 {:name      name
                                  :temporary (or temp false)
                                  :count     (count dataset)
                                  :columns   (dataset->columns dataset)
                                  :data      dataset}))

          ;; Ignore current dataset
          :ignore context

          (throw (Exception. (str "Unsupported Output type: " type))))))))

(defn sleep-statement
  "Sleeps for N milliseconds (defaults to 1 second)."
  ([] (sleep-statement 1000))
  ([n] (fn [context]
         (let [n' (eval-expression-without-row n context)]
           (do
             (Thread/sleep n')
             context)))))

(defn head-statement
  "Takes the first N rows."
  ([] (head-statement 1))
  ([num] (dataset-statement #(take num %))))

(defn tail-statement
  "Takes the last N rows."
  ([] (tail-statement 1))
  ([num] (if (zero? num)
           ;; Shortcut when N = 0
           (fn [context] (assoc context :current-dataset '()))
           (dataset-statement #(take-last num %)))))

(defn reverse-statement
  "Reverses the dataset."
  []
  (dataset-statement reverse))

(defn union-statement
  "Unions two datasets together."
  [mode query]
  (fn [context]
    ; Execute inner query using the current context
    (let [inner-context (query context)
          unioned-dataset (concat (:current-dataset context) (:current-dataset inner-context))
          final-dataset (if (= :distinct mode) (distinct unioned-dataset) unioned-dataset)]
      (assoc context :current-dataset final-dataset))))

(defn distinct-statement
  "Removes duplicate rows in the dataset."
  []
  (dataset-statement distinct))

(defn rownumber-statement
  "Stores the current index (0-based) of each row in the dataset in a given column (_index by default)."
  ([] (rownumber-statement :_index))
  ([column]
   (letfn [(a [index row] (assoc row column index))]
     (dataset-statement #(map-indexed a %)))))

(defn sort-statement
  "Sorts the dataset by one or more keys."
  [& columns]
  (dataset-statement
    (fn [dataset]
      (letfn [(sort-recur
                [cols result]
                (if (empty? cols)
                  result
                  (recur (butlast cols)
                         (let [c (last cols)
                               key (c 0)
                               order (c 1)
                               cmp (if (= order :order-ascending)
                                     compare
                                     #(compare %2 %))]
                           (sort-by key cmp result)))))]
        (sort-recur columns dataset)))))

(defn select-statement
  "Leaves only the specified columns in each row."
  [& columns]
  (dataset-statement #(map (fn [row] (select-keys row columns)) %)))

(defn unselect-statement
  "Removes the specified columns from each row."
  [& columns]
  (dataset-statement #(map (fn [row] (apply dissoc row columns)) %)))

(defn filter-statement
  "Filters the dataset on a predicate expression."
  [predicate]
  (fn [context]
    (let [dataset (:current-dataset context)
          filtered-dataset (filter (fn [row] (predicate row context)) dataset)]
      (assoc context :current-dataset filtered-dataset))))

(defn def-statement
  "Defines user-defined functions in the context. Functions accept any number of arguments and return a single value."
  [& def-terms]
  ;; def-terms is a list of function definitions
  ;; e.g. ([:name [:arg1 :arg2] #<clojure.lang.AFunction$1@437ed27d>]
  ;;        ...)
  (fn [context]
    (let [functions (:functions context)
          evaluate-defs (fn [functions [name udf]]
                          (assoc functions name {:name name :udf udf}))]
      ;; Reduce the list of def terms and store the functions
      ;; :current-dataset is not changed
      (assoc context :functions (reduce evaluate-defs functions def-terms)))))

(defn set-statement
  "Sets variables in the context to the value of an expression.
  Expressions must be dataset/context based, not row-based.
  An optional predicate can used to filter rows used in calculating
  the value of the expression."
  ;; Provide a default predicate
  ([set-terms] (set-statement set-terms nil))
  ([set-terms predicate]
    ;; Sets is a list of in-order assignments
    ;; e.g. ([[:variable "@x"] #<clojure.lang.AFunction$1@437ed27d>]
    ;;       [[:variable "@y] #<clojure.lang.AFunction$1@38dd4fd5>])
   (let [sets (rest set-terms)]
     (fn [context]
       (let [variables (:variables context)
             dataset (:current-dataset context)
             filtered-dataset (if (nil? predicate)
                                dataset
                                (filter #(predicate % context) dataset))
             temp-context (assoc context :current-dataset filtered-dataset)
             evaluate-sets (fn [variables [[_ var-name] expr]]
                             ;; Create 2nd temp-context with updated variables and evaluate
                             (let [temp-context' (assoc temp-context :variables variables)
                                   value (expr nil temp-context')]
                               (assoc variables var-name value)))]
         ;; Reduce the list of set terms and store the updated variables
         ;; :current-dataset is not changed
         (assoc context :variables (reduce evaluate-sets variables sets)))))))

(defn assignment-statement
  "Assigns new values to keys in each row, optionally filtered by a predicate."
  ([assignment-terms] (assignment-statement assignment-terms nil))
  ([assignment-terms predicate]
   (let [;; Assignments is a list of in-order assignments
         ;; e.g. ([:x #<clojure.lang.AFunction$1@437ed27d>]
         ;;       [:y #<clojure.lang.AFunction$1@36d54d6b>]
         ;;       [:z #<clojure.lang.AFunction$1@38dd4fd5>])
         assignments (rest assignment-terms)]
     (fn [context]
       (let [dataset (:current-dataset context)]
         (letfn [(evaluate-assignments
                   [row-index row]
                   (if (or (nil? predicate) (predicate row context))
                     (reduce
                       (fn [updated-row [assignment-key assignment-expression]]
                         ; Create a temporary context with the current-row-index set
                         (let [temp-context (assoc context :current-row-index row-index)]
                           (assoc updated-row assignment-key (assignment-expression updated-row temp-context))))
                       row assignments)
                     row))]
           (assoc context :current-dataset (map-indexed evaluate-assignments dataset))))))))

(defn stats-statement
  "Statistical calculation over the dataset."
  ([stats-assignment-terms] (stats-statement stats-assignment-terms {}))
  ([stats-assignment-terms stats-groupby]
   (let [;; Assignments is a list of in-order assignments
         ;; e.g. ([:x #<clojure.lang.AFunction$1@437ed27d>]
         ;;       [:y #<clojure.lang.AFunction$1@36d54d6b>]
         ;;       [:z #<clojure.lang.AFunction$1@38dd4fd5>])
         assignments (rest stats-assignment-terms)
         groups (rest stats-groupby)]
     (fn [context]
       (let [dataset (:current-dataset context)
             ;; Groups the dataset rows into a key hash and list of rows
             ;; e.g. {{:x 1} [{:x 1} {:x 1} {:x 1}]
             ;;       {:x 2} [{:x 2} {:x 2} {:x 2}]}
             grouped (group-by (fn [row] (reduce
                                           (fn [result term]
                                             (let [expr (if (vector? term) (last term) term)
                                                   label (if (vector? term) (first term) term)
                                                   group-value (eval-expression expr row context)]
                                               (assoc result label group-value))) {} groups)) dataset)

             ;; Evaluate each group in turn, with a temp-context
             new-dataset (letfn [(evaluate-group
                                   [row-index [group-hash group-rows]]
                                   ;; Apply each assignment in order on top of the group hash
                                   ;; Yields one row per group, including the keys from the group-hash and the assignments
                                   (reduce
                                     (fn [result [assignment-key assignment-expression]]
                                       (let [temp-context (assoc context :current-dataset group-rows :current-row-index row-index)]
                                         (assoc result assignment-key (assignment-expression result temp-context))))
                                     group-hash assignments))]
                           (map-indexed evaluate-group grouped))]

         (assoc context :current-dataset new-dataset))))))

(defn rename-statement
  "Renames specific keys in each row, optionally filtered by a predicate."
  ;; rename-map is a map of original key to new key
  ;; e.g. {:x :y, :y :z}
  ([rename-map] (rename-statement rename-map nil))
  ([rename-map predicate]
   (fn [context]
     (let [dataset (:current-dataset context)]
       (letfn [(evaluate-renames
                 [row]
                 (if (or (nil? predicate) (predicate row context))
                   (clojure.set/rename-keys row rename-map)
                   row))]
         (assoc context :current-dataset (map evaluate-renames dataset)))))))

(defn sample-statement
  "Selects a sample of a given size from the dataset."
  [expr]
  (fn [context]
    (let [dataset (:current-dataset context)
          size (eval-expression-without-row expr context)
          output-dataset (stats/sample dataset :size size)]
      (assoc context :current-dataset output-dataset))))

(defn pivot-statement
  "Pivot the dataset."
  [pivot-terms pivot-per-terms pivot-groupby]
  (let [terms (rest pivot-terms)
        per-terms (rest pivot-per-terms)
        per-formatter (if (= 1 (count terms))
                        (fn [per _] (keyword (clojure.string/join ", " per)))
                        (fn [per term] (keyword (str (clojure.string/join ", " per) ": " (if (fn? term) (str term) (name term))))))
        groups (rest pivot-groupby)]
    (fn [context]
      (let [dataset (:current-dataset context)
            ;; Groups the dataset rows into a key hash and list of rows
            ;; e.g. {{:x 1} [{:x 1} {:x 1} {:x 1}]
            ;;       {:x 2} [{:x 2} {:x 2} {:x 2}]}
            grouped (group-by (fn [row] (reduce
                                          (fn [result term]
                                            (let [expr (if (vector? term) (last term) term)
                                                  label (if (vector? term) (first term) term)
                                                  group-value (eval-expression expr row context)]
                                              (assoc result label group-value))) {} groups)) dataset)

            ;; Evaluate each group in turn, with a temp-context
            new-dataset (letfn [(evaluate-group
                                  [row-index [group-hash group-rows]]
                                  (let [temp-context (assoc context :current-dataset group-rows :current-row-index row-index)
                                        ;; Calculate Per combinations for each group
                                        ;; Per-groups consist of a vector of per-values, and a list of matching rows
                                        per-groups (group-by
                                                     (fn [row] (reduce
                                                                 (fn [result pterm]
                                                                   (let [per-value (eval-expression pterm row temp-context)]
                                                                     (conj result per-value))) [] per-terms))
                                                     group-rows)

                                        final (reduce (fn [result [per per-rows]]
                                                        (let [temp-context (assoc context :current-dataset per-rows :current-row-index row-index)]
                                                          (reduce (fn [result term]
                                                                    (let [expr (if (vector? term) (last term) term)
                                                                          label (if (vector? term) (first term) term)]
                                                                      (assoc result
                                                                        (per-formatter per label)
                                                                        (eval-expression expr (first per-rows) temp-context))))
                                                                  result terms)))
                                                      group-hash per-groups)]
                                    final))]

                          ; Evaluate each grouping, concat the resulting rows
                          (map-indexed evaluate-group grouped))]

        (assoc context :current-dataset new-dataset)))))

(defn unpivot-statement
  "Unpivot the dataset."
  [unpivot-terms unpivot-per-terms unpivot-groupby]
  (let [term (last unpivot-terms)
        per-term (last unpivot-per-terms)
        groups (rest unpivot-groupby)]
    (fn [context]
      (let [dataset (:current-dataset context)
            ;; Groups the dataset rows into a key hash and list of rows
            ;; e.g. {{:x 1} [{:x 1} {:x 1} {:x 1}]
            ;;       {:x 2} [{:x 2} {:x 2} {:x 2}]}
            grouped (group-by (fn [row] (select-keys row groups)) dataset)

            ;; Evaluate each group in turn, with a temp-context
            new-dataset (letfn [(evaluate-group
                                  [[group-hash group-rows]]
                                  (let [final (reduce (fn [result row]
                                                        (let [row' (apply dissoc row groups)]
                                                          (reduce (fn [result key]
                                                                    (conj result (assoc group-hash
                                                                                   per-term (name key)
                                                                                   term (get row' key))))
                                                                  result (keys row'))))
                                                      '() group-rows)]
                                    final))]

                          ; Evaluate each grouping, concat the resulting rows
                          (mapcat evaluate-group grouped))]

        (assoc context :current-dataset new-dataset)))))

(defn project-statement
  "Replaces the current-dataset with the result of an expression. Supports sequential of maps, or a single map."
  [expr]
  (fn [context]
    (let [result (eval-expression-without-row expr context)]
      (cond
        ;; If map, wrap in a vector as a single row
        (map? result)
        (assoc context :current-dataset [result])

        ;; Sequential/collections -- treat each item as a row, and convert to a map if needed
        ;; If each row isn't a map already, wrap in a map with column name "value"
        (or (sequential? result) (instance? Collection result))
        (let [result' (if (every? map? result)
                        result
                        (map #(hash-map :value %) result))]
          (assoc context :current-dataset result'))

        ;; Unsupported
        :else
        (throw (Exception. (str "Unable to project a value of type: " (type result))))))))

(defn get-join-source
  "Returns a tuple of a dataset and alias"
  [[type value alias] context]
  (case type
    ;; Reference Source -- Dataset loaded from datastore
    :join-reference-source
    [(:data (get (:datastore context) value)) value]

    ;; Inline Source -- Execute inline query using current context
    :join-inline-source
    [(:current-dataset (value context)) alias]))

(defn is-equijoin?
  "Returns true if all join-terms are equijoins"
  [join-terms]
  (every? #(= :equi-join-term (first %)) join-terms))

(defn merge-with-priority
  [s1 s2 ^Boolean prefer-s1]
  (if (true? prefer-s1)
    (merge s2 s1)
    (merge s1 s2)))

(defn join-impl-nested-loops
  "Nested loops join implementation"
  [context join-type left left-alias right right-alias join-terms left-priority]
  (case join-type
    :FULL
    ; Union LEFT and RIGHT join
    (distinct (concat
                (join-impl-nested-loops context :LEFT left left-alias right right-alias join-terms left-priority)
                (join-impl-nested-loops context :LEFT right right-alias left left-alias join-terms (not left-priority))))
    :RIGHT
    ; Swap table order and do a LEFT join
    (join-impl-nested-loops context :LEFT right right-alias left left-alias join-terms (not left-priority))

    :CROSS
    (let [result (map (fn [left-row]
                        (map #(merge-with-priority left-row % left-priority) right))
                      left)]
      (flatten result))

    ; Else..
    (let [result (map (fn [left-row]
                        (let [temp-context (assoc context :join-targets {left-alias left-row})
                              matches (filter
                                        (fn [right-row]
                                          (let [temp-context (assoc-in temp-context [:join-targets right-alias] right-row)
                                                temp-row (merge-with-priority left-row right-row left-priority)
                                                join-terms' (map (fn [join-term] (join-term temp-row temp-context)) join-terms)]
                                            (every? true? join-terms')))
                                        right)]
                          (if (> (count matches) 0)
                            (map #(merge-with-priority left-row % left-priority) matches)
                            (if (= :LEFT join-type)
                              left-row
                              []))))
                      left)]
      (flatten result))))

(defn join-statement
  "Joins the current-dataset with another dataset."
  [join-type join-alias join-source join-terms]
  (fn [context]
    (let [left (:current-dataset context)
          left-alias (last join-alias)
          [right right-alias] (get-join-source join-source context)
          join-terms (rest join-terms)
          equijoin (is-equijoin? join-terms)
          join-terms' (map (fn [[type & args]]
                             (case type
                               :theta-join-term
                               (first args)

                               :equi-join-term
                               (let [lhs (first args)
                                     rhs (last args)]
                                 (fn [row context] (= (lhs row context) (rhs row context)))))) join-terms)]
      (let [new-dataset (join-impl-nested-loops context join-type left left-alias right right-alias join-terms' true)]
        (assoc context :current-dataset new-dataset)))))

(defn benchmark-statement
  "Benchmarks a given query and returns a dataset of information."
  [expr options]
  (fn [context]
    (let [query-string (eval-expression-without-row expr context)
          options' (eval-expression-without-row options context)
          execute-query (resolve 'parsec.core/execute-query)
          bench-results (crit/quick-benchmark (execute-query query-string) options')
          performance (map #(:performance %) (:results bench-results))
          summary (with-out-str (crit/report-result bench-results [:os :runtime :verbose]))
          output-dataset [(assoc (dissoc bench-results :runtime-details :os-details :results)
                            :performance performance
                            :summary summary)]]
      (assoc context :current-dataset output-dataset))))
