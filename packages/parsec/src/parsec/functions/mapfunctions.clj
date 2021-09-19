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

(ns parsec.functions.mapfunctions
  (:require [parsec.functions :refer [function-transform]]
            [parsec.helpers :refer :all]
            [parsec.helpers.functional :refer :all]
            [clojure.core.incubator :refer [dissoc-in]]))

;; Map Functions
(defmethod function-transform
  :get [_ & args] (fn-with-args (fn
                                  ([m key] (if (sequential? key)
                                             (get-in m (map keyword key))
                                             (get m (keyword key))))
                                  ([m key default] (if (sequential? key)
                                                     (get-in m (map keyword key) default)
                                                     (get m (keyword key) default)))) args))
(defmethod function-transform
  :set [_ & args] (fn-with-args (fn [m key val] (if (sequential? key)
                                                  (assoc-in m (map #(keyword (to-string %)) key) val)
                                                  (assoc m (keyword (to-string key)) val))) args))
(defmethod function-transform
  :delete [_ & args] (fn-with-args (fn [m key] (if (sequential? key)
                                                 (dissoc-in m (map #(keyword (to-string %)) key))
                                                 (dissoc m (keyword (to-string key))))) args))

(defmethod function-transform
  :merge [_ & args] (fn-with-args (fn [& args] (let [maps (filter map? args)]
                                                 (when (count maps)
                                                   (apply merge maps)))) args))

(defmethod function-transform
  :keys [_ & args] (fn-with-args-handles-nil #(let [k (keys %)]
                                   (if (nil? k)
                                     []
                                     (map clojure.core/name k))) args))
(defmethod function-transform
  :values [_ & args] (fn-with-args-handles-nil #(let [v (vals %)] (if (nil? v) [] v)) args))
