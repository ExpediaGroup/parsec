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

(ns parsec.input.docs
  (:require [parsec.docs.functions :as functions]
            [parsec.docs.inputs :as inputs]
            [parsec.docs.literals :as literals]
            [parsec.docs.operators :as operators]
            [parsec.docs.statements :as statements]
            [parsec.docs.symbols :as symbols]))

; Options:
; TBD

(defn input-transform
  "Returns documentation data for Parsec"
  [_options]
  (fn [context]
    (let [docs (concat functions/tokens
                       inputs/tokens
                       literals/tokens
                       operators/tokens
                       statements/tokens
                       symbols/tokens)]

      (assoc context :current-dataset docs))))
