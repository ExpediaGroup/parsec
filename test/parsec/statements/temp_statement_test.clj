(ns parsec.statements.temp-statement-test
  (:require [clojure.test :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]))

(deftest temp-statement-parser-test
  (testing-parser! "with unsupported assignment" :temp-statement "temp=x")

  (testing-parser
    "with happy path" :temp-statement
    "temp x" [:output-statement :datastore {:name :x :temporary true}]))
