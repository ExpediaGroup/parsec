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

(ns parsec.input.http
  (:require [parsec.helpers :refer :all]
            [parsec.parsers.csv :as csv :only [parse-csv]]
            [parsec.parsers.json :as json :only [parse-json]]
            [parsec.parsers.xml :as xml :only [parse-xml]]
            [clojure.string :refer [lower-case split-lines]]
            [http.async.client :as http]
            [json-path :as json-path]))

(def default-client-options {:follow-redirects true})

(defn get-parser
  "Gets a parser function which either parses the body of the HTTP response, or does nothing. Shares options with the http request."
  [parser-name options]
  (case parser-name
    nil #(list %)
    "json" #(let [parsed (json/parse-json (:body %))]
             (if (:jsonpath options)
               (json-path/at-path (:jsonpath options) parsed)
               parsed))
    "jsonlines" #(map json/parse-json (split-lines (:body %)))
    "csv" #(csv/parse-csv (:body %) options)
    "xml" #(xml/parse-xml (:body %) options)
    (throw (Exception. (str "Unrecognized parser \"" parser-name "\"")))))

; Options:
; :uri
; :method
; :body
; :connection-timeout
; :request-timeout
; :follow-redirects
; :user
; :password
; :parser

(defn input-transform
  "Executes an HTTP request."
  [options]
  (fn [context]
    (let [options' (apply-default-http-options (eval-expression-without-row options context) default-client-options)
          uri (:uri options')
          method (lower-case (or (:method options') "get"))
          parser (get-parser (:parser options') options')]
      (if (nil? uri)
        (throw (Exception. "input type HTTP requires a \"uri\" option"))
        (with-open [client (mapply http/create-client options')]
          (let [method-fn (case method
                            "head" http/HEAD
                            "get" http/GET
                            "post" http/POST
                            "put" http/PUT
                            "patch" http/PATCH
                            "delete" http/DELETE
                            "options" http/OPTIONS
                            http/GET)
                response (mapply (partial method-fn client uri) options')]
            (http/await response)
            (let [status (http/status response)
                  result {:body         (http/string response)
                          :headers      (http/headers response)
                          :status       (:code status)
                          :msg          (:msg status)
                          :protocol     (:protocol status)
                          :content-type (http/content-type response)}
                  parsed-result (parser result)]
              (assoc context :current-dataset parsed-result))))))))
