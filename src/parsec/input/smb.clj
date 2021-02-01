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

(ns parsec.input.smb
  (:require
    [parsec.helpers :refer :all]
    [parsec.parsers.csv :as csv :only [parse-csv]]
    [parsec.parsers.json :as json :only [parse-json]]
    [parsec.parsers.xml :as xml :only [parse-xml]]
    [clojure.string :as str]
    [clj-time.coerce :as timec])
  (:import (jcifs.smb NtlmPasswordAuthentication SmbFile)
           (org.apache.commons.io IOUtils)))

(defn get-parser
  "Gets a parser function which either parses the body of the response, or does nothing. Shares options with the http request."
  [parser-name options]
  (case parser-name
    nil? #(list %)
    "json" #(json/parse-json (:body %))
    "jsonlines" #(map json/parse-json (str/split-lines (:body %)))
    "csv" #(csv/parse-csv (:body %) options)
    "xml" #(xml/parse-xml (:body %) options)
    (throw (Exception. (str "Unrecognized parser \"" parser-name "\"")))))

; Options:
; :uri
; :user
; :password
; :parser
(defn input-transform
  "Loads a smb:// URL"
  [options]
  (fn [context]
    (let [{:keys [^String uri user password parser] :as options'} (eval-expression-without-row options context)
          parser (get-parser parser options')
          smbfile (if-not (nil? user)
                    (SmbFile. uri (NtlmPasswordAuthentication. "" user password))
                    (SmbFile. uri))
          isFile (.isFile smbfile)
          path (.getPath smbfile)
          smbname (.getName smbfile)
          attributes (.getAttributes smbfile)
          createdTime (.createTime smbfile)
          lastModifiedTime (.getLastModified smbfile)
          length (.length smbfile)
          filelist (when-not isFile (into [] (.list smbfile)))
          body (when isFile (with-open [ios (.getInputStream smbfile)] (IOUtils/toString ios)))]
      (let [result {:name             smbname
                    :path             path
                    :isfile           isFile
                    :body             body
                    :attributes       attributes
                    :createdTime      (timec/from-long createdTime)
                    :lastModifiedTime (timec/from-long lastModifiedTime)
                    :length           length
                    :files            filelist
                    }
            parsed-result (parser result)]
        (assoc context :current-dataset parsed-result)))))
