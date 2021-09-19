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

(ns parsec.helpers
  (:require [clojure.edn :as edn]
            [incanter.core :as ic]
            [clj-time.format :as timef]
            [clj-time.coerce :as timec]
            [clojure.data.json :as json])
  (:import (org.joda.time ReadableDateTime)
           (java.net URLEncoder URLDecoder InetAddress)))

;;
;; Types / Type Conversions
;;

(defn date?
  "Returns true if the input is a DateTime, else false."
  [x]
  (instance? ReadableDateTime x))

(defn to-number
  "Attempts to convert a value to a number, using a ratio if possible; returns nil if it fails."
  ([x modifier] (if (= modifier :percent)
                  (let [number (to-number x)]
                    (when number (/ number 100)))))
  ([x]
   (cond
     (nil? x) x
     (number? x) x
     (date? x) (timec/to-long x)
     (string? x) (let [parsed (try (Long/parseLong x)
                                   (catch NumberFormatException e
                                     (try (Double/parseDouble x)
                                          (catch NumberFormatException e
                                            (try (edn/read-string x)
                                                 (catch NumberFormatException e nil))))))]
                   (when (number? parsed)
                     (rationalize parsed)
                     ;; Else nil
                     ))
     :else nil)))


(defn to-integer
  "Converts a value to a long integer; returns nil if it cannot be converted."
  [x]
  (cond
    (nil? x) x
    (integer? x) x
    (number? x) (long x)
    (date? x) (timec/to-long x)
    (string? x) (let [number (to-number x)]
                  (when-not (nil? number)
                    (long number)
                    ;; Else nil
                    ))
    :else nil))

(defn to-double
  "Converts a value to a double; returns nil if it cannot be converted."
  [x]
  (cond
    (nil? x) x
    (double? x) x
    (number? x) (double x)
    (date? x) (double (timec/to-long x))
    (string? x) (let [number (to-number x)]
                  (when-not (nil? number)
                    (double number)
                    ;; Else nil
                    ))
    :else nil))

(defn to-string
  "Converts a value to a string."
  ([x]
    (cond
      (nil? x) x
      (string? x) x
      (ratio? x) (str (double x))
      (sequential? x) (json/write-str x)
      (map? x) (json/write-str x)
      :else (str x)))
  ([x format]
     (cond
       (date? x) (if (string? format)
                   (timef/unparse (timef/with-zone (timef/formatter format) (.getZone x)) x)
                   (throw (Exception. "Date formatter must be in the form of a string. See http://www.joda.org/joda-time/apidocs/org/joda/time/format/DateTimeFormat.html")))
       :else (throw (Exception. (str "Unable to format " (type x) " with a format string."))))))

(defn to-boolean
  "Converts a value to a boolean if possible; returns nil if it cannot be converted."
  [value]
  (cond
    (boolean? value) value
    (number? value) (not (zero? value))
    (and (string? value)
         (= (.toLowerCase value) "true")) (boolean true)
    (and (string? value)
         (= (.toLowerCase value) "false")) false
    :else nil))

(defn try-to-boolean
  "Tries to convert a value to to a boolean; returns the original value if not possible."
  [value]
  (let [value' (to-boolean value)]
    (if (boolean? value') value' value)))

(defn to-date
  "Converts a value to a date.  Supports strings (using a format string) and
  epoch milliseconds."
  ([value] (to-date value nil))
  ([value format]
  (cond
    (date? value) value
    (string? value) (try (if (nil? format)
                           (timef/parse value)
                           (timef/parse (timef/formatter format) value))
                         (catch Exception _ nil))
    (number? value) (timec/from-long (long value))
    :else nil)))

(defn when-number-args
  "Helper method that returns nil if any argument is not an number, else executes f with all arguments"
  [f & args]
  (when (every? number? args) (apply f args)))

(defn when-integer-args
  "Helper method that returns nil if any argument is not an integer, else executes f with all arguments"
  [f & args]
  (when (every? integer? args) (apply f args)))

(defn only-strings
  "Helper method that returns nil if the first argument is not a string, else executes f with all arguments"
  [f x & args]
  (when (string? x) (apply f x args)))

(defn only-sequentials
  "Helper method that returns nil if the first argument is not a sequential, else executes f with all arguments"
  [f x & args]
  (when (sequential? x) (apply f x args)))

(defn only-sequentials-as-lists
  "Helper method that returns nil for any non-sequential input, and converts non-lists into lists"
  [f x & args]
  (when (sequential? x)
    (let [x' (if (list? x) x (apply list x))]
      (apply f x' args))))

(defn only-maps
  "Helper method that returns nil if the first argument is not a map, else executes f with all arguments"
  [f x & args]
  (when (map? x) (apply f x args)))

(defn only-maps-or-sequentials
  "Helper method that combines only-maps and only-sequentials"
  [f x & args]
  (when (or (sequential? x) (map? x)) (apply f x args)))

;;
;; Useful Functions
;;

(defn always-predicate
  "Row/Context predicate that always returns true"
  [_ _] true)

(def any-pred? (comp boolean some))

(defn in?
  "true if seq contains elm; if a string is provided, does a substring search"
  [seq-or-str elm]
  (cond
    (and (string? seq-or-str) (not (nil? elm))) (.contains ^String seq-or-str (to-string elm))
    (sequential? seq-or-str) (if (some #(= elm %) seq-or-str)
                               true
                               false)
    :else false))

(defn is-empty?
  "Returns true if the input is 'empty': for collections if length is zero; for maps, if no keys; for strings, if blank?; null or zero return true, all others false."
  [x]
  (cond
    (nil? x) true
    (number? x) (zero? x)
    (string? x) (clojure.string/blank? x)
    (sequential? x) (zero? (count x))
    (map? x) (nil? (keys x))
    :else false))

(defn mapmapv
  "Maps a function onto the values of a map"
  [f map]
  (when map
    (into {} (for [[k v] map] [k (f v)]))))

(defn mapply
  "Map-apply: Like apply, but for key/value pairs from a map."
  [f & args]
  (apply f (apply concat (butlast args) (last args))))

(defn mapcat2
  "Like mapcat, but doesn't break for non-ISeq elements"
  [fn coll]
  (let [fn' (comp #(if (sequential? %) % (vector %)) fn)]
    (mapcat fn' coll)))

(defmacro xor
  ([a b]
   `(let [a# ~a
          b# ~b]
      (if a#
        (if b# false true)
        (if b# true false)))))

(defn eval-expression
  "Evaluates an expression with a current row and context."
  [expression row context]
  (cond
    (keyword? expression) (get row expression)
    (fn? expression) (expression row context)

    ;; Variables
    (and (vector? expression) (= :variable (first expression)))
    (get-in context [:variables (peek expression)])

    ;; else Number, String, Boolean
    :else expression))

(defn eval-expression-without-row
  "Evaluates an expression with a context but without a specific row.  An exception will be thrown if an identifier is used."
  [expression context]
  (cond
    (keyword? expression) (throw (Exception. "Cannot reference row values in this expression."))
    (fn? expression) (expression nil context)

    ;; Variables
    (and (vector? expression) (= :variable (first expression)))
    (get-in context [:variables (peek expression)])

    ;; else Number, String, Boolean
    :else expression))

;; TODO: Refactor for N args
(defn get-unsupported-exception
  ([name expr1]
   (Exception.
     (str name " unsupported on data type ["
          (type expr1) "]: " expr1)))
  ([name expr1 expr2]
   (Exception.
     (str name " unsupported on data types ["
          (type expr1) ", " (type expr2) "]: " expr1 " " expr2))))

(defn incanter-to-maplist
  "Converts an Incanter Dataset to a List of Maps"
  [dataset]
  (let [colnames (ic/col-names dataset)]
    (map (fn [row] (zipmap colnames row)
           ) (ic/to-list dataset))))

(defn dataset->columns
  "Returns the columns in a dataset"
  [dataset]
  (reduce (fn [columns row] (apply conj columns (keys row))) #{} dataset))

(defn expr-to-string
  "Reverse-parse an expression tree to a string"
  ;; TODO: Finish implementation
  ([expr] (expr-to-string expr false))
  ([expr wrap]
   (cond
     (keyword? expr) (name expr)
     (vector? expr) (case (first expr)
                      :expression (if (true? wrap)
                                    (str "(" (expr-to-string (last expr) true) ")")
                                    (expr-to-string (last expr) true))

                      :not-operation (str "!" (expr-to-string (nth expr 1) true))
                      :and-operation (str (expr-to-string (nth expr 1) true) " and " (expr-to-string (nth expr 2) true))
                      :or-operation (str (expr-to-string (nth expr 1) true) " or " (expr-to-string (nth expr 2) true))
                      :xor-operation (str (expr-to-string (nth expr 1) true) " xor " (expr-to-string (nth expr 2) true))

                      :negation-operation (str "-" (expr-to-string (nth expr 1) true))
                      :addition-operation (str (expr-to-string (nth expr 1) true) "+" (expr-to-string (nth expr 2) true))
                      :subtraction-operation (str (expr-to-string (nth expr 1) true) "-" (expr-to-string (nth expr 2) true))
                      :multiplication-operation (str (expr-to-string (nth expr 1) true) "*" (expr-to-string (nth expr 2) true))
                      :division-operation (str (expr-to-string (nth expr 1) true) "/" (expr-to-string (nth expr 2) true))

                      :equals-expression (str (expr-to-string (nth expr 1) true) "==" (expr-to-string (nth expr 2) true))
                      :not-equals-expression (str (expr-to-string (nth expr 1) true) "!=" (expr-to-string (nth expr 2) true))
                      :greater-than-expression (str (expr-to-string (nth expr 1) true) ">" (expr-to-string (nth expr 2) true))
                      :less-than-expression (str (expr-to-string (nth expr 1) true) "<" (expr-to-string (nth expr 2) true))
                      :greater-or-equals-expression (str (expr-to-string (nth expr 1) true) ">=" (expr-to-string (nth expr 2) true))
                      :less-or-equals-expression (str (expr-to-string (nth expr 1) true) "<=" (expr-to-string (nth expr 2) true))

                      :function (str (name (nth expr 1)) "(" (clojure.string/join "," (map expr-to-string (nthrest expr 2))) ")")

                      :isexist-function (str "isexist(" (expr-to-string (last expr)) ")")


                      ;; Else list
                      (str "[" (clojure.string/join "," (map str expr)) "]"))
     :else (str expr))))

(defn replace-password
  [suffix [_ prefix password suffix2]]
  (let [suffix (or suffix suffix2)
        passwordLength (count password)
        mask (apply str (map (fn [_] "*") (range passwordLength)))]
    (str prefix mask suffix)))

(defn mask-password-in-string
  "Finds and masks passwords in a string."
  [s]
  (-> s
      (clojure.string/replace #"(password\s*=\s*\'{3})(.*?([^\\]|\\\\))\'{3}" (partial replace-password "'''")) ; password='''***'''
      (clojure.string/replace #"(password\s*=\s*\")(.*?([^\\]|\\\\))\"" (partial replace-password "\"")) ; password="***"
      (clojure.string/replace #"(password\s*=\s*\'(?!\'{2}))(.*?)(?!=[^\\]|\\\\)\'" (partial replace-password "'")) ; password='***'
      (clojure.string/replace #"(\".*?password\s*=)([^\"\'].+?)([;\"])" (partial replace-password nil)) ; "...password=***;"
      (clojure.string/replace #"(\'.*?password\s*=)([^\"\'].+?)([;'])" (partial replace-password nil)) ; '...password=***;'
      (clojure.string/replace #"((?:['\"]|\'{3})?password(?:['\"]|\'{3})?\s*:\s*\")(.*?([^\\]|\\\\))\"" (partial replace-password "\"")) ; password: "***"
      (clojure.string/replace #"((?:['\"]|\'{3})?password(?:['\"]|\'{3})?\s*:\s*'{3})(.*?)(?!=[^\\]|\\\\)'{3}" (partial replace-password "'''")) ; password: '''***'''
      (clojure.string/replace #"((?:['\"]|\'{3})?password(?:['\"]|\'{3})?\s*:\s*'(?!\'{2}))(.*?([^\\]|\\\\))'" (partial replace-password "'")))) ; password: '***'


;;
;; HTTP Related
;;

(defn apply-default-http-options
  "Merges options with defaults, and wraps auth options if provided."
  [{:keys [user username password] :as options} default-options]
  (let [user (or user username)
        options' (merge default-options options)]
    ;; Wrap user/password inside :auth
    ;; Preemptive authentication by default
    (if user
      (-> options'
          (assoc :auth {:user user :password password :preemptive true})
          (dissoc :user :username :password))
      options')))

(defn urldecode
  "URL Encodes a string"
  [str]
  (when str
    (URLDecoder/decode str "UTF-8")))

(defn urlencode
  "URL Encodes a string"
  [str]
  (when str
    (URLEncoder/encode str "UTF-8")))

(defn hostname []
  (let [addr (. InetAddress getLocalHost)]
    (.getHostName addr)))

(defn add-shutdown-hook
  "Registers a new virtual-machine shutdown hook"
  [f]
  (.addShutdownHook (Runtime/getRuntime) (Thread. f)))

(defn string->stream
  ([s] (string->stream s "UTF-8"))
  ([s encoding]
   (-> s
       (.getBytes encoding)
       (java.io.ByteArrayInputStream.))))
