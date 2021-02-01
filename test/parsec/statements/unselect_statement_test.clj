(ns parsec.statements.unselect-statement-test
  (:require [clojure.test :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [unselect-statement]]))

(deftest unselect-statement-parser-test
  (testing-parser!
    "with no argument" :unselect-statement
    "unselect")

  (testing-parser
    "with one column" :unselect-statement
    "unselect a" [:unselect-statement :a])

  (testing-parser
    "with multiple columns" :unselect-statement
    "unselect a, b, c" [:unselect-statement :a :b :c]))

(deftest unselect-statement-test
  (testing-statement
    "with one column"
    (unselect-statement :col1)
    test-dataset1
    '({:col2 2, :col3 3, :col4 4}
       {:col2 2, :col3 3, :col4 3}
       {:col2 2, :col3 5, :col4 2}
       {:col2 5, :col3 5, :col4 1}
       {:col2 5, :col3 3, :col4 0}))

  (testing-statement
    "with two columns"
    (unselect-statement :col1 :col3)
    test-dataset1
    '({:col2 2, :col4 4}
       {:col2 2, :col4 3}
       {:col2 2, :col4 2}
       {:col2 5, :col4 1}
       {:col2 5, :col4 0}))

  (testing-statement
    "with non-existing column"
    (unselect-statement :bogus)
    test-dataset1
    test-dataset1))
