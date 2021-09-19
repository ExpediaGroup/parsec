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

(ns parsec.functions.stringfunctions
  (:require [parsec.functions :refer [function-transform]]
             [parsec.helpers :refer :all]
            [parsec.helpers.functional :refer :all]
            [parsec.helpers.strings :as strings]
            [pandect.core :as pandect])
  (:import (org.apache.commons.codec.binary Base64)))


;; String Functions
(defmethod function-transform
  :trim [_ & args] (fn-with-one-arg-handles-nil (partial only-strings clojure.string/trim) args))
(defmethod function-transform
  :ltrim [_ & args] (fn-with-one-arg-handles-nil (partial only-strings clojure.string/triml) args))
(defmethod function-transform
  :rtrim [_ & args] (fn-with-one-arg-handles-nil (partial only-strings clojure.string/trimr) args))
(defmethod function-transform
  :uppercase [_ & args] (fn-with-one-arg-handles-nil (partial only-strings clojure.string/upper-case) args))
(defmethod function-transform
  :lowercase [_ & args] (fn-with-one-arg-handles-nil (partial only-strings clojure.string/lower-case) args))
(defmethod function-transform
  :substring [_ & args] (fn-with-args-handles-nil (partial only-strings strings/subs2) args))
(defmethod function-transform
  :substr [_ & args] (fn-with-args-handles-nil (partial only-strings strings/substr) args))
(defmethod function-transform
  :indexof [_ & args] (fn-with-args-handles-nil #(only-strings clojure.string/index-of % (to-string %2)) args))
(defmethod function-transform
  :lastindexof [_ & args] (fn-with-args-handles-nil #(only-strings clojure.string/last-index-of % (to-string %2)) args))
(defmethod function-transform
  :startswith [_ & args] (fn-with-args-handles-nil #(only-strings clojure.string/starts-with? % (to-string %2)) args))
(defmethod function-transform
  :endswith [_ & args] (fn-with-args-handles-nil #(only-strings clojure.string/ends-with? % (to-string %2)) args))
(defmethod function-transform
  :join [_ & args] (fn-with-args-handles-nil (partial only-sequentials #(clojure.string/join %2 %)) args))
(defmethod function-transform
  :split [_ & args] (fn-with-args-handles-nil #(clojure.string/split % (re-pattern %2)) args))

;; Replacements functions
(defmethod function-transform
  :replace [_ & args]
  (fn [row context]
    (let [s (eval-expression (nth args 0) row context)
          match (eval-expression (nth args 1) row context)
          replacement (eval-expression (nth args 2) row context)]
      (only-strings clojure.string/replace-first s (to-string match) (to-string replacement)))))
(defmethod function-transform
  :replaceall [_ & args]
  (fn [row context]
    (let [s (eval-expression (nth args 0) row context)
          match (eval-expression (nth args 1) row context)
          replacement (eval-expression (nth args 2) row context)]
      (only-strings clojure.string/replace s (to-string match) (to-string replacement)))))


;; Encode / Decode functions
(defmethod function-transform
  :urlencode [_ & args] (fn-with-args-handles-nil (partial only-strings urlencode) args))
(defmethod function-transform
  :urldecode [_ & args] (fn-with-args-handles-nil (partial only-strings urldecode) args))

(defmethod function-transform
  :base64encode [_ & args] (fn-with-one-arg-handles-nil #(only-strings (fn [s] (Base64/encodeBase64String (.getBytes s "UTF-8"))) %) args))
(defmethod function-transform
  :base64decode [_ & args] (fn-with-one-arg-handles-nil #(only-strings (fn [s] (String. (Base64/decodeBase64 ^String s) "UTF-8")) %) args))


;; Digest functions
(defmethod function-transform
  :adler32 [_ & args] (fn-with-args-handles-nil pandect/adler32 args))
(defmethod function-transform
  :crc32 [_ & args] (fn-with-args-handles-nil pandect/crc32 args))

(defmethod function-transform
  :gost [_ & args] (fn-with-args-handles-nil pandect/gost args))
(defmethod function-transform
  :hmac_gost [_ & args] (fn-with-args-handles-nil pandect/gost-hmac args))

(defmethod function-transform
  :md5 [_ & args] (fn-with-args-handles-nil pandect/md5 args))
(defmethod function-transform
  :hmac_md5 [_ & args] (fn-with-args-handles-nil pandect/md5-hmac args))

(defmethod function-transform
  :ripemd128 [_ & args] (fn-with-args-handles-nil pandect/ripemd128 args))
(defmethod function-transform
  :hmac_ripemd128 [_ & args] (fn-with-args-handles-nil pandect/ripemd128-hmac args))
(defmethod function-transform
  :ripemd256 [_ & args] (fn-with-args-handles-nil pandect/ripemd256 args))
(defmethod function-transform
  :hmac_ripemd256 [_ & args] (fn-with-args-handles-nil pandect/ripemd256-hmac args))
(defmethod function-transform
  :ripemd320 [_ & args] (fn-with-args-handles-nil pandect/ripemd320 args))
(defmethod function-transform
  :hmac_ripemd320 [_ & args] (fn-with-args-handles-nil pandect/ripemd320-hmac args))

(defmethod function-transform
  :sha1 [_ & args] (fn-with-args-handles-nil pandect/sha1 args))
(defmethod function-transform
  :hmac_sha1 [_ & args] (fn-with-args-handles-nil pandect/sha1-hmac args))

(defmethod function-transform
  :sha3_224 [_ & args] (fn-with-args-handles-nil pandect/keccak-224 args))
(defmethod function-transform
  :hmac_sha3_224 [_ & args] (fn-with-args-handles-nil pandect/keccak-224-hmac args))
(defmethod function-transform
  :sha3_256 [_ & args] (fn-with-args-handles-nil pandect/keccak-256 args))
(defmethod function-transform
  :hmac_sha3_256 [_ & args] (fn-with-args-handles-nil pandect/keccak-256-hmac args))
(defmethod function-transform
  :sha3_384 [_ & args] (fn-with-args-handles-nil pandect/keccak-384 args))
(defmethod function-transform
  :hmac_sha3_384 [_ & args] (fn-with-args-handles-nil pandect/keccak-384-hmac args))
(defmethod function-transform
  :sha3_512 [_ & args] (fn-with-args-handles-nil pandect/keccak-512 args))
(defmethod function-transform
  :hmac_sha3_512 [_ & args] (fn-with-args-handles-nil pandect/keccak-512-hmac args))


(defmethod function-transform
  :sha256 [_ & args] (fn-with-args-handles-nil pandect/sha256 args))
(defmethod function-transform
  :hmac_sha256 [_ & args] (fn-with-args-handles-nil pandect/sha256-hmac args))
(defmethod function-transform
  :sha384 [_ & args] (fn-with-args-handles-nil pandect/sha384 args))
(defmethod function-transform
  :hmac_sha384 [_ & args] (fn-with-args-handles-nil pandect/sha384-hmac args))
(defmethod function-transform
  :sha512 [_ & args] (fn-with-args-handles-nil pandect/sha512 args))
(defmethod function-transform
  :hmac_sha512 [_ & args] (fn-with-args-handles-nil pandect/sha512-hmac args))

(defmethod function-transform
  :siphash [_ & args] (fn-with-args-handles-nil pandect/siphash args))
(defmethod function-transform
  :siphash48 [_ & args] (fn-with-args-handles-nil pandect/siphash48 args))

(defmethod function-transform
  :tiger [_ & args] (fn-with-args-handles-nil pandect/tiger args))
(defmethod function-transform
  :hmac_tiger [_ & args] (fn-with-args-handles-nil pandect/tiger-hmac args))

(defmethod function-transform
  :whirlpool [_ & args] (fn-with-args-handles-nil pandect/whirlpool args))
(defmethod function-transform
  :hmac_whirlpool [_ & args] (fn-with-args-handles-nil pandect/whirlpool-hmac args))

