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

(ns parsec.docs.symbols)

(def tokens
  '({:name ";"
     :altName "query separator"
     :type "symbol"
     :syntax ["query1; query2", "query1; query2; query3 ..."]
     :description ["The query separator is used to separate two distinct Parsec queries. The queries will be executed sequentially with a shared context."]
     :examples [{:q "input mock; input mock"}]}
    {:name "/* */"
     :altName "comment block"
     :type "symbol"
     :syntax ["/* ... */"]
     :description ["Comments in Parsec follow the general C style /* ... */ block comment form and are ignored by the parser.
                    
                    Nested comment blocks are supported, provided they are balanced."]
     :examples [{:q "/* My query */ input mock"}
                {:q "input mock | /* filter col1 == 1 | */ sort col1 desc"}]}))