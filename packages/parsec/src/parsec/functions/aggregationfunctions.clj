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

(ns parsec.functions.aggregationfunctions
  (:require [parsec.functions :refer [function-transform]]
            [parsec.helpers :refer :all]
            [parsec.helpers.functional :refer :all]
            [parsec.helpers.maths :as maths]
            [incanter.stats :as stats]))

(defn general-stats-transform
  "General-purpose transform for applying numerical stats functions
  to the entire dataset.  Nil values are excluded automatically,
  but non-supported data types may throw exceptions."
  [function expression predicate value-when-empty]
  (fn [_ context]
    (let [dataset (:current-dataset context)
          filtered-dataset (filter #(predicate % context) dataset)
          values (map #(eval-expression expression % context) filtered-dataset)
          filtered-values (remove nil? values)]
      (if (empty? (take 1 filtered-values))
        value-when-empty
        (function filtered-values)))))

(defn mean-transform
  "Transform for arithmetic mean (average) of a dataset."
  ([expression]
   (mean-transform expression always-predicate))
  ([expression predicate]
   (general-stats-transform stats/mean expression predicate nil)))

(defn count-transform
  "Transform for counting the number of non-null rows (or expressions) in a dataset."
  ([expression]
   (count-transform expression always-predicate))
  ([expression predicate]
   (general-stats-transform count expression predicate 0)))

(defn cumulativemean-transform
  "Transform for cumulative mean of a dataset. Returns a list."
  ([expression]
   (cumulativemean-transform expression always-predicate))
  ([expression predicate]
   (general-stats-transform stats/cumulative-mean expression predicate nil)))

(defn distinctcount-transform
  "Transform for counting the number of distinct, non-null expressions in a dataset."
  ([expression]
   (distinctcount-transform expression always-predicate))
  ([expression predicate]
   (general-stats-transform #(count (set %)) expression predicate 0)))

(defn geometricmean-transform
  "Transform for geometric mean of a dataset."
  ([expression]
   (geometricmean-transform expression always-predicate))
  ([expression predicate]
   (general-stats-transform maths/geometricMean expression predicate nil)))

(defn max-transform
  "Transform for maximum value of a dataset."
  ([expression]
   (max-transform expression always-predicate))
  ([expression predicate]
   (general-stats-transform #(apply max %) expression predicate nil)))

(defn median-transform
  "Transform for median value of a dataset."
  ([expression]
   (median-transform expression always-predicate))
  ([expression predicate]
   (general-stats-transform stats/median expression predicate nil)))

(defn min-transform
  "Transform for minimum value of a dataset."
  ([expression]
   (min-transform expression always-predicate))
  ([expression predicate]
   (general-stats-transform #(apply min %) expression predicate nil)))

(defn mode-transform
  "Transform for mode value of a dataset."
  ([expression]
   (mode-transform expression always-predicate))
  ([expression predicate]
   (general-stats-transform maths/mode expression predicate nil)))

(defn pluck-transform
  "Creates a list by evalutating an expression against each row."
  ([expression]
   (pluck-transform expression always-predicate))
  ([expression predicate]
   (general-stats-transform #(apply list %) expression predicate nil)))

(defn stddev-pop-transform
  "Transform for population standard deviation of a dataset."
  ([expression]
   (stddev-pop-transform expression always-predicate))
  ([expression predicate]
   (general-stats-transform maths/stddev-pop expression predicate nil)))

(defn stddev-samp-transform
  "Transform for sample standard deviation of a dataset."
  ([expression]
   (stddev-samp-transform expression always-predicate))
  ([expression predicate]
   (general-stats-transform stats/sd expression predicate nil)))

(defn sum-transform
  "Transform for sum of a dataset."
  ([expression]
   (sum-transform expression always-predicate))
  ([expression predicate]
   (general-stats-transform #(reduce + %) expression predicate 0)))

(defn first-transform
  "Transform for first element of a dataset."
  ([expression]
   (first-transform expression always-predicate))
  ([expression predicate]
   (general-stats-transform first expression predicate nil)))

(defn last-transform
  "Transform for last element of a dataset."
  ([expression]
   (last-transform expression always-predicate))
  ([expression predicate]
   (general-stats-transform last expression predicate nil)))

(defn every-transform
  "Transform for whether an expression is true for every row in the dataset."
  ([expression]
   (every-transform expression always-predicate))
  ([expression predicate]
   (general-stats-transform (partial every? true?) expression predicate nil)))

(defn some-transform
  "Transform for whether an expression is true for some row in the dataset."
  ([expression]
   (some-transform expression always-predicate))
  ([expression predicate]
   (general-stats-transform (partial any-pred? true?) expression predicate nil)))


(defn percentile-transform
  "Transform for percentile of a dataset and a given percentage.  Uses linear interpolation method.
    The percentage can be an non-row expression (e.g. number, aggregate function, context-based expression).
    Excludes nils from the calculation and returns nil if there are no non-nil values."
  ([expression percent-expression]
   (percentile-transform expression percent-expression always-predicate))
  ([expression percent-expression predicate]
   (fn [_ context]
     (let [dataset (:current-dataset context)
           filtered-dataset (filter #(predicate % context) dataset)
           percent (/ (eval-expression-without-row percent-expression context) 100)
           values (map #(eval-expression expression % context) filtered-dataset)
           filtered-values (remove nil? values)]
       (if (zero? (count filtered-values))
         nil
         (stats/quantile filtered-values :probs percent))))))


;; Aggregation Functions
(defmethod function-transform
  :mean [_ & args] (apply mean-transform args))
(defmethod function-transform
  :count [_ & args] (apply count-transform args))
(defmethod function-transform
  :cumulativemean [_ & args] (apply cumulativemean-transform args))
(defmethod function-transform
  :distinctcount [_ & args] (apply distinctcount-transform args))
(defmethod function-transform
  :every [_ & args] (apply every-transform args))
(defmethod function-transform
  :first [_ & args] (apply first-transform args))
(defmethod function-transform
  :geometricmean [_ & args] (apply geometricmean-transform args))
(defmethod function-transform
  :last [_ & args] (apply last-transform args))
(defmethod function-transform
  :max [_ & args] (apply max-transform args))
(defmethod function-transform
  :median [_ & args] (apply median-transform args))
(defmethod function-transform
  :min [_ & args] (apply min-transform args))
(defmethod function-transform
  :mode [_ & args] (apply mode-transform args))
(defmethod function-transform
  :percentile [_ & args] (apply percentile-transform args))
(defmethod function-transform
  :pluck [_ & args] (apply pluck-transform args))
(defmethod function-transform
  :some [_ & args] (apply some-transform args))
(defmethod function-transform
  :stddevp [_ & args] (apply stddev-pop-transform args))
(defmethod function-transform
  :stddev [_ & args] (apply stddev-samp-transform args))
(defmethod function-transform
  :sum [_ & args] (apply sum-transform args))
