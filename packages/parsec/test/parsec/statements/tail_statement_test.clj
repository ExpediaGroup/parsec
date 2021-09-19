(ns parsec.statements.tail-statement-test
  (:require [clojure.test :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [tail-statement]]))

(deftest tail-statement-parser-test
  (testing-parser
    "with no argument" :tail-statement
    "tail" [:tail-statement])

  (testing-parser
    "with argument" :tail-statement
    "tail 10" [:tail-statement 10])

  (testing-parser
    "with 0 argument" :tail-statement
    "tail 0" [:tail-statement 0]))


(deftest tail-statement-test
  (testing-statement
    "with happy path"
    (tail-statement 2)
    test-dataset1
    '({:col1 4, :col2 5, :col3 5, :col4 1}
       {:col1 5, :col2 5, :col3 3, :col4 0}))

  (testing-statement
    "with 0 argument"
    (tail-statement 0)
    test-dataset1
    '())

  (testing-statement
    "with no argument"
    (tail-statement)
    test-dataset1
    (take-last 1 test-dataset1))

  (testing-statement
    "with overly-large argument"
    (tail-statement 1000)
    test-dataset1
    test-dataset1))
