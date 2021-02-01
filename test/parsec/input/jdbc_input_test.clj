(ns parsec.input.jdbc-input-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.input.jdbc :refer :all]
            [clojure.java.jdbc :as j]))

(deftest get-operation-test
  (testing "with nil"
    (is (= j/query (get-operation nil))))
  (testing "with empty string"
    (is (nil? (get-operation ""))))
  (testing "with invalid operation"
    (is (nil? (get-operation "bork"))))
  (testing "with select"
    (is (= j/query (get-operation "select"))))
  (testing "with query"
    (is (= j/query (get-operation "query"))))
  (testing "with insert"
    (is (= j/insert! (get-operation "insert"))))
  (testing "with update"
    (is (= j/update! (get-operation "update"))))
  (testing "with delete"
    (is (= j/delete! (get-operation "delete"))))
  (testing "with execute"
    (is (= j/execute! (get-operation "execute"))))
  (testing "with create"
    (is (= j/execute! (get-operation "create")))))

(deftest get-classname-test
  (testing "with nil"
    (is (nil? (get-classname nil nil))))
  (testing "with empty string"
    (is (= "" (get-classname "" ""))))
  (testing "with classname"
    (is (= "bork" (get-classname "bork" "jdbc:bork"))))
  (testing "with invalid JDBC uri"
    (is (nil? (get-classname nil "jdbc:bork://bork"))))
  (testing "with MySQL uri"
    (is (= "com.mysql.jdbc.Driver" (get-classname nil "jdbc:mysql://servername:3306/database"))))
  (testing "with SQL Server uri"
    (is (= "com.microsoft.sqlserver.jdbc.SQLServerDriver" (get-classname nil "jdbc:sqlserver://SERVERNAME:1433;databaseName=DATABASE;user=USER;"))))
  (testing "with jtds SQL Server uri"
    (is (= "net.sourceforge.jtds.jdbc.Driver" (get-classname nil "jdbc:jtds:sqlserver://SERVERNAME:1433;databaseName=DATABASE;user=USER;"))))
  (testing "with Oracle thin uri"
    (is (= "oracle.jdbc.OracleDriver" (get-classname nil "jdbc:oracle:thin:USERNAME/PASSWORD@//SERVERNAME:1566/DATABASE"))))
  (testing "with Hive"
    (is (= "org.apache.hadoop.hive.jdbc.HiveDriver" (get-classname nil "jdbc:hive://SERVERNAME:10000/default?mapred.job.queue.name=QUEUENAME"))))
  (testing "with Hive2"
    (is (= "org.apache.hive.jdbc.HiveDriver" (get-classname nil "jdbc:hive2://SERVERNAME:10001/default?mapred.job.queue.name=QUEUENAME"))))
  (testing "with Postgres"
    (is (= "org.postgresql.Driver" (get-classname nil "jdbc:postgresql://SERVERNAME:5432/DATABASE?user=USER&password=******;"))))
  (testing "with Redshift"
    (is (= "com.amazon.redshift.jdbc42.Driver" (get-classname nil "jdbc:redshift://SERVERNAME:5439/DATABASE")))))
