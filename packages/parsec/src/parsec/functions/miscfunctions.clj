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

(ns parsec.functions.miscfunctions
  (:require [parsec.functions :refer [function-transform]]
            [parsec.helpers :refer :all]
            [parsec.helpers.functional :refer :all]
            [criterium.core :refer [runtime-details os-details]])
  (:import (java.net InetAddress)))

;; Miscellaneous functions
(defmethod function-transform
  :random [_ & args] (fn-with-args-handles-nil #(+ % (rand (- %2 %))) args))

(defmethod function-transform
  :rank [& _] (fn [_ context] (:current-row-index context)))

(defmethod function-transform
  :hostname [& _] (fn [_ _] (hostname)))

(defmethod function-transform
  :ip [& _] (fn [_ _] (.getHostAddress (. InetAddress getLocalHost))))

(defmethod function-transform
  :runtime [& _] (fn [_ _] (runtime-details)))

(defmethod function-transform
  :os [& _] (fn [_ _] (os-details)))
