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

(ns parsec.input.jdbc
  (:require [taoensso.timbre :as timbre]
            [incanter.core :as ic]
            [clojure.string :as str]
            [clojure.java.jdbc :as j]
            [parsec.helpers :refer :all]
            [clj-time.coerce :as timec])
  (:import (java.sql Date Timestamp DriverManager)))

;; Automatically convert java.sql.Date to DateTime
(extend-protocol j/IResultSetReadColumn
  Date
  (result-set-read-column [val _ _]
    (timec/from-sql-date val)))

;; Automatically convert java.sql.Timestamp to DateTime
(extend-protocol j/IResultSetReadColumn
  Timestamp
  (result-set-read-column [val _ _]
    (timec/from-sql-time val)))

(def classname-map
  {
   "postgresql"     "org.postgresql.Driver"
   "mysql"          "com.mysql.jdbc.Driver"
   "sqlserver"      "com.microsoft.sqlserver.jdbc.SQLServerDriver"
   "jtds:sqlserver" "net.sourceforge.jtds.jdbc.Driver"
   "oracle:oci"     "oracle.jdbc.OracleDriver"
   "oracle:thin"    "oracle.jdbc.OracleDriver"
   "derby"          "org.apache.derby.jdbc.EmbeddedDriver"
   "hsqldb"         "org.hsqldb.jdbcDriver"
   "h2"             "org.h2.Driver"
   "sqlite"         "org.sqlite.JDBC"
   "db2"            "com.ibm.db2.jcc.DB2Driver"
   "cassandra"      "org.apache.cassandra.cql.jdbc.CassandraDriver"
   "hive"           "org.apache.hadoop.hive.jdbc.HiveDriver"
   "hive2"          "org.apache.hive.jdbc.HiveDriver"
   "teradata"       "com.teradata.jdbc.TeraDriver"
   "redshift"       "com.amazon.redshift.jdbc42.Driver"
   "qubole"         "com.qubole.jdbc.jdbc41.core.QDriver"
   "presto"         "com.teradata.presto.jdbc42.Driver"
   })

(def query-operation-map
  {
   "select"  j/query
   "query"   j/query
   "insert"  j/insert!
   "update"  j/update!
   "delete"  j/delete!
   "create"  j/execute!
   "execute" j/execute!
   })

(defn get-operation
    "Helper method for determining the correct operation"
    [name]
    (let [operation (or name "query")]
        (get query-operation-map (clojure.string/lower-case operation))))

(defn get-classname
  "Helper method for determining the class name of the correct JDBC driver."
  [classname uri]
  (cond
    classname classname
    uri (let [classes (seq classname-map)
              matches (filter #(str/starts-with? uri (str "jdbc:" (first %) ":")) classes)]
          (if (empty? matches)
            nil
            (second (first matches))))
    :else nil))

; Options:
; :query
; :uri
; :classname
; :user / :username
; :password
; :operation

(defn input-transform
  "Executes a JDBC query."
  [options]
  (fn [context]
    (let [options' (eval-expression-without-row options context)
          {:keys [query
                  uri
                  classname
                  user username password
                  operation]} options'
          classname (get-classname classname uri)
          user' (or user username)
          ;; If query type is not specified it's assumed to be a SELECT query.
          operation' (get-operation operation)]

      ;; Load JDBC driver class
      (when classname
        (Class/forName classname))
      ;; Execute
      (with-open [^java.sql.Connection connection (if (nil? user')
                                                    (DriverManager/getConnection uri)
                                                    (DriverManager/getConnection uri user' password))]
        ;; Execute JDBC method
        (cond
          (= operation' j/query)
          (let [result (operation' { :connection connection } [query] {:identifiers identity})]
            (assoc context :current-dataset result))

          (= operation' j/execute!)
          (let [result (operation' { :connection connection } [query] {:identifiers identity})]
            (assoc context :current-dataset []))

          (or (= operation' j/insert!)
              (= operation' j/update!)
              (= operation' j/delete!)) (throw (Exception. "Unsupported JDBC operation.  Please use the \"execute\" operation instead."))

          ;; For j/execute!
          :else (throw (Exception. "Unsupported JDBC operation.")))))))
