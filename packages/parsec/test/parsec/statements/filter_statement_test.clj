(ns parsec.statements.filter-statement-test
  (:require [clojure.test :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [filter-statement]]))

(deftest filter-statement-parser-test
  (testing-parser!
    "with no argument" :filter-statement
    "filter")

  (testing-parser
    "with an identifier" :filter-statement
    "filter a" [:filter-statement [:expression :a]])

  (testing-parser
    "with a boolean expression" :filter-statement
    "filter x==y" [:filter-statement [:expression [:equals-expression :x :y]]]))


(deftest filter-statement-test
  (testing-statement
    "with all rows passing filter"
    (filter-statement (fn [row dataset] true))
    test-dataset1
    test-dataset1)

  (testing-statement
    "with no rows passing filter"
    (filter-statement (fn [row dataset] (> (:col1 row) 400)))
    test-dataset1
    '())

  (testing-statement
    "with some rows in filter"
    (filter-statement (fn [row dataset] (= (:col4 row) 2)))
    test-dataset1
    (filter (fn [row] (= 2 (:col4 row))) test-dataset1))

  (testing-statement
    "with nils in filter"
    (filter-statement (fn [row dataset] (nil? (:col1 row))))
    test-dataset2
    '({:col1 nil, :col2 2, :col3 5}
       {:col1 nil, :col2 nil, :col3 5, :col4 1})))
