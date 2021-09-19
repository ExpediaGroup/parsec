(ns parsec.statements.head-statement-test
  (:require [clojure.test :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [head-statement]]))

(deftest head-statement-parser-test
  (testing-parser
    "with no argument" :head-statement
    "head" [:head-statement])

  (testing-parser
    "with argument" :head-statement
    "head 10" [:head-statement 10])

  (testing-parser
    "with 0 argument" :head-statement
    "head 0" [:head-statement 0]))


(deftest head-statement-test
  (testing-statement
    "with happy path"
    (head-statement 2)
    test-dataset1
    '({:col1 1, :col2 2, :col3 3, :col4 4}
       {:col1 2, :col2 2, :col3 3, :col4 3}))

  (testing-statement
    "with 0 argument"
    (head-statement 0)
    test-dataset1
    '())

  (testing-statement
    "with no argument"
    (head-statement)
    test-dataset1
    (take 1 test-dataset1))

  (testing-statement
    "with argument larger than dataset"
    (head-statement 1000)
    test-dataset1
    test-dataset1))
