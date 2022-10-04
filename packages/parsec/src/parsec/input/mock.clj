;; Copyright 2022 Expedia, Inc.
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

(ns parsec.input.mock
  (:require [incanter.datasets :as datasets]
            [parsec.helpers :refer [eval-expression-without-row
                                    incanter-to-maplist to-number]]))

(def mock-dataset
  '({:col1 1, :col2 2, :col3 3, :col4 4}
     {:col1 2, :col2 2, :col3 3, :col4 3}
     {:col1 3, :col2 2, :col3 5, :col4 2}
     {:col1 4, :col2 5, :col3 5, :col4 1}
     {:col1 5, :col2 5, :col3 3, :col4 0}))

; Options:
; :name
; :n

(defn input-transform
  "Returns a mock dataset"
  [options]
  (fn [context]
      (let [options' (eval-expression-without-row options context)
            n (to-number (options' :n))
            name (options' :name)]
        (let [mock (cond
                     (not (nil? name))
                     ;; Incanter dataset
                     (incanter-to-maplist (datasets/get-dataset (keyword (:name options')) :incanter-home (:incanterHome options')))

                     ;; Large mock dataset with N rows
                     (not (nil? n))
                     (map (fn [i] {:col1 i,
                                   :col2 (- n i),
                                   :col3 (mod i 5),
                                   :col4 (mod i 8)
                                   :col5 (Math/cos i)}) (range n))


                     ;; Else default mock dataset
                     :else
                     mock-dataset)]

          (assoc context :current-dataset mock)))))
