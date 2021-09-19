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

(ns parsec.functions.functionalfunctions
  (:require [parsec.functions :refer [function-transform]]
            [parsec.helpers :refer :all]
            [parsec.helpers.functional :refer :all])
  (:import (parsec.functions ParsecUdf)))

;; Functional Functions
(defmethod function-transform
  :apply [_ f & args]
  (fn [row context]
    (let [f' (f row context)]
      (if (instance? ParsecUdf f')
        (let [impl (:implementation f')
              args (map #(eval-expression % row context) args)]
          (impl args context))
        (throw (Exception. "apply() requires a user-defined function as its first argument"))))))

(defmethod function-transform
  :map [_ f & args]
  (fn [row context]
    (let [f' (f row context)
          args' (map #(eval-expression % row context) args)
          list (first args')]
      (if (instance? ParsecUdf f')
        (when (and (not (nil? list)) (sequential? list))
          (let [impl (:implementation f')]
            (map (fn [el] (impl [el] context)) list)))
        (throw (Exception. "map() requires a user-defined function as its first argument"))))))

(defmethod function-transform
  :mapcat [_ f & args]
  (fn [row context]
    (let [f' (f row context)
          args' (map #(eval-expression % row context) args)
          list (first args')]
      (if (instance? ParsecUdf f')
        (when (and (not (nil? list)) (sequential? list))
          (let [impl (:implementation f')]
            (mapcat2 (fn [el] (impl [el] context)) list)))
        (throw (Exception. "mapcat() requires a user-defined function as its first argument"))))))

(defmethod function-transform
  :mapvalues [_ f & args]
  (fn [row context]
    (let [f' (f row context)
          args' (map #(eval-expression % row context) args)
          map' (first args')]
      (if (instance? ParsecUdf f')
        (when (map? map')
          (let [impl (:implementation f')]
            (mapmapv (fn [el] (impl [el] context)) map')))
        (throw (Exception. "mapvalues() requires a user-defined function as its first argument"))))))

(defmethod function-transform
  :filter [_ f & args]
  (fn [row context]
    (let [f' (f row context)
          args' (map #(eval-expression % row context) args)
          list (first args')]
      (if (instance? ParsecUdf f')
        (when (and (not (nil? list)) (sequential? list))
          (let [impl (:implementation f')]
            (filter (fn [el] (impl [el] context)) list)))
        (throw (Exception. "filter() requires a user-defined function as its first argument"))))))
