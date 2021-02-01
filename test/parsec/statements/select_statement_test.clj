(ns parsec.statements.select-statement-test
  (:require [clojure.test :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [select-statement]]))

(deftest select-statement-parser-test
  (testing-parser!
    "with no argument" :select-statement
    "select")

  (testing-parser
    "with one column" :select-statement
    "select a" [:select-statement :a])

  (testing-parser
    "with multiple columns" :select-statement
    "select a, b, c" [:select-statement :a :b :c]))


(deftest select-statement-test
  (testing-statement
    "with one column"
    (select-statement :col1)
    test-dataset1
    '({:col1 1}
       {:col1 2}
       {:col1 3}
       {:col1 4}
       {:col1 5}))

  (testing-statement
    "with two columns"
    (select-statement :col1 :col3)
    test-dataset1
    '({:col1 1, :col3 3}
       {:col1 2, :col3 3}
       {:col1 3, :col3 5}
       {:col1 4, :col3 5}
       {:col1 5, :col3 3}))

  (testing-statement
    "with non-existing column"
    (select-statement :bogus)
    test-dataset1
    '({} {} {} {} {})))
