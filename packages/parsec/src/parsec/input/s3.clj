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

(ns parsec.input.s3
  (:require [parsec.helpers :refer :all]
            [parsec.parsers.csv :as csv :only [parse-csv]]
            [parsec.parsers.json :as json :only [parse-json]]
            [parsec.parsers.xml :as xml :only [parse-xml]]
            [clj-time.coerce :as timec]
            [clojure.string :as str]
            [aws.sdk.s3 :as s3]
            [clojure.java.io :as io])
  (:import (java.io InputStream)
           (java.util.zip GZIPInputStream ZipInputStream)
           (org.apache.commons.io IOUtils)))

(defn input-stream->str
  "Copies an input stream to a string, closing the input stream"
  [^InputStream input-stream ^String encoding]
  (let [str (IOUtils/toString input-stream encoding)]
    str))

(defn get-parser
  "Gets a parser function which either parses the body of the response, or does nothing. Shares options with the http request."
  [parser-name options]
  (case parser-name
    nil identity
    "json" #(json/parse-json %)
    "jsonlines" #(map json/parse-json (str/split-lines %))
    "csv" #(csv/parse-csv % options)
    "xml" #(xml/parse-xml % options)
    (throw (Exception. (str "Unrecognized parser \"" parser-name "\"")))))

(defn parse-s3-uri
  "gets the bucket, prefix and object from ther uri"
  [uri]
  ;; Ignore nil or empty strings
  (when-not (empty? uri)
    (map
      ;; Map empty strings to nil
      #(when-not (empty? %) %)
      (drop 1 (re-find #"s3:\/\/([^:\/]+)(?:\/(.*\/)?(.*))*" uri)))))

(defn build-aws-credentials
  "creates a map with the credentials.  Uses token if provided."
  [accessKeyId secretAccessKey token]
  (if (nil? token)
    {:access-key accessKeyId :secret-key secretAccessKey}
    {:access-key accessKeyId :secret-key secretAccessKey :token token}))

(defn list-s3-buckets
  "Returns a list of buckets for the given credentials"
  [credentials]
  (s3/list-buckets credentials))

(defn list-s3-objects
  "Returns a list of objects with a bucket, with an optional prefix"
  [credentials bucket prefix object max-keys delimiter marker-flag]
  (let [result (if (true? marker-flag)
                 (s3/list-objects credentials bucket {:marker (str prefix object) :max-keys max-keys :delimiter delimiter})
                 (s3/list-objects credentials bucket {:prefix prefix :max-keys max-keys :delimiter delimiter}))]
    (if (nil? delimiter)
      (:objects result)
      [result])))

(defn get-s3-object
  "Returns an object from S3"
  [credentials bucket key compression parser]
  (let [response (s3/get-object credentials bucket key)
        content-stream (:content response)
        content (case compression
                  :gzip (input-stream->str (GZIPInputStream. content-stream) "UTF-8")
                  :zip (input-stream->str (ZipInputStream. content-stream) "UTF-8")
                  (slurp content-stream))
        result (assoc response :content content)]
    (.close content-stream)
    (if parser
      (parser (:content result))
      [result])))

(defn get-s3-objects
  "Returns a list of S3 objects"
  [credentials bucket prefix object max-keys compression delimiter marker-flag parser]
  (let [object-list (list-s3-objects credentials bucket prefix object max-keys delimiter marker-flag)
        object-list (if (not (nil? delimiter)) (:objects (first object-list)) object-list)
        objects (pmap #(get-s3-object credentials bucket (:key %) compression parser) object-list)]
    ;; Concat all the objects together (or their parsed contents)
    (apply concat objects)))

(defn input-transform
  "Loads an S3 s3n:// URI"
  [options]
  (fn [context]
    (let [{:keys [^String uri bucket prefix object
                  accesskeyid secretaccesskey token
                  op operation maxkeys delimiter gzip zip parser] :as options'} (eval-expression-without-row options context)

          credentials (build-aws-credentials accesskeyid secretaccesskey token)
          operation (or operation op)

          ;; Default of 10 keys maximum to avoid unbounded requests
          maxkeys (or maxkeys 10)

          ;; Parse URI, or use separate keys if provided
          [bucket' prefix' object'] (parse-s3-uri uri)
          bucket (or bucket bucket')
          prefix (or prefix prefix')
          object (or object object')

          compression (cond
                        gzip :gzip
                        zip :zip
                        :else :none)

          parser (when parser (get-parser parser options'))

          result (cond
                   ;; If operation is provided, it takes precedence
                   (contains? #{"list-buckets" "buckets"}  operation)
                   (list-s3-buckets credentials)
                   (contains? #{"list-objects" "objects" "ls"} operation)
                   (list-s3-objects credentials bucket prefix object maxkeys delimiter false)
                   (contains? #{"list-objects-from" "list-from" "lf"} operation)
                   (list-s3-objects credentials bucket prefix object maxkeys delimiter true)
                   (contains? #{"get-objects" "get" } operation)
                   (get-s3-objects credentials bucket prefix object maxkeys compression delimiter false parser)
                   (contains? #{"get-objects-from" "get-from" "gf" } operation)
                   (get-s3-objects credentials bucket prefix object maxkeys compression delimiter true parser)
                   (contains? #{"get-object" "object" "obj" } operation)
                   (get-s3-object credentials bucket (str prefix object) compression parser)

                   ;; Auto-detect operation
                   (and bucket object)
                   (get-s3-object credentials bucket (str prefix object) compression parser)

                   bucket
                   (list-s3-objects credentials bucket prefix object maxkeys delimiter false)

                   :else
                   (list-s3-buckets credentials))]
      (assoc context :current-dataset result))))
