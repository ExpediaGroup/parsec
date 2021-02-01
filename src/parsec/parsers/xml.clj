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

(ns parsec.parsers.xml
  (:require [parsec.helpers :refer :all]
            [clojure.data.xml :as xml]
            [clj-xpath.core :as xpath]))

(defn convert-xml-node
  [flatten node]
  (let [children (force (:children node))
        text-node (first (filter #(= :#text (:tag %)) children))
        child-nodes (remove #(= :#text (:tag %)) children)
        result (:attrs node)
        result (if-not (nil? text-node) (assoc result (:tag node) (:text text-node)) result)
        result (cond
                 (true? flatten) (apply merge result (map (partial convert-xml-node flatten) child-nodes))
                 (not (empty? child-nodes)) (assoc result :children (map (partial convert-xml-node flatten) child-nodes))
                 :else result)]
    result))

(defn parse-xml
  "Reads XML document."
  ([xml] (parse-xml xml { :xpath "/" }))
  ([xml options-or-xpath]
   (when xml
     ;; 2nd argument can be map or string; string is treated as xpath
     (let [options (if (string? options-or-xpath) { :xpath options-or-xpath } options-or-xpath)
           { :keys [xpath raw flatten] :as options } options
           flatten (if (nil? flatten) true flatten)]
       (if (true? raw)
         ;; Raw mode--deconstructed XML
         (xml/parse-str xml)
         (let [doc (xpath/xml->doc xml)
               nodes (xpath/$x xpath doc)]
           (map (partial convert-xml-node flatten) nodes)))))))
