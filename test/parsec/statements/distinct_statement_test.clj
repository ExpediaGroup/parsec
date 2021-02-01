(ns parsec.statements.distinct-statement-test
  (:require [clojure.test :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [distinct-statement]]))

(deftest distinct-statement-parser-test
  (testing-parser
    "with no argument" :distinct-statement
    "distinct" [:distinct-statement]))


(deftest distinct-statement-test
  (testing-statement
    "with test-dataset1"
    (distinct-statement)
    test-dataset1
    (distinct test-dataset1))

  (testing-statement
    "with test-dataset2"
    (distinct-statement)
    test-dataset2
    (distinct test-dataset2))

  (testing-statement
    "with duplicated rows"
    (distinct-statement)
    test-dataset2
    (distinct (concat test-dataset2 test-dataset2)))

  (testing-statement
    "with more duplicated rows"
    (distinct-statement)
    (concat test-dataset1 test-dataset2)
    (distinct (concat test-dataset1 test-dataset2 test-dataset2 test-dataset1)))

  (testing-statement
    "with slightly different rows"
    (distinct-statement)
    '({:d 1 :a 1 :c 1}
       {:a 1 :b 1 :c 1 :d 1}
       {:a 2 :b 1 :c 1 :d 1}
       {:a 4 :b 1 :c 1 :d 1}
       {:b 1 :c 1 :d 1}
       {:a 4 :b 1 :c 1 :d 1 :e 3})
    (distinct '({:d 1 :a 1 :c 1}
                 {:a 1 :b 1 :c 1 :d 1}
                 {:a 2 :b 1 :c 1 :d 1}
                 {:a 1 :c 1 :d 1}
                 {:a 2 :b 1 :d 1 :c 1}
                 {:a 4 :b 1 :c 1 :d 1}
                 {:b 1 :c 1 :d 1}
                 {:a 4 :b 1 :c 1 :d 1 :e 3}
                 {:a 2 :b 1 :c 1 :d 1}))))
