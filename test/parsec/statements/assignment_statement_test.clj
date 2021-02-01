(ns parsec.statements.assignment-statement-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [assignment-statement]]
            [parsec.functions.aggregationfunctions :refer [mean-transform]]))

(deftest assignment-statement-parser-test
  (testing-parser!
    "with incomplete assignment" :assignment-statement
    "x =")

  (testing-parser
    "with one assignment" :assignment-statement
    "x = 1" [:assignment-statement [:assignment-terms [:x [:expression 1]]]])

  (testing-parser
    "with one assignment to null" :assignment-statement
    "x = null" [:assignment-statement [:assignment-terms [:x [:expression nil]]]])

  (testing-parser
    "with one assignment to string" :assignment-statement
    "col1 = 'hello'" [:assignment-statement [:assignment-terms [:col1 [:expression "hello"]]]])

  (testing-parser
    "with two assignments" :assignment-statement
    "x = 1, y = 2" [:assignment-statement
                    [:assignment-terms
                     [:x [:expression 1]]
                     [:y [:expression 2]]]])

  (testing-parser
    "with a boolean expression" :assignment-statement
    "x = true or false" [:assignment-statement
                         [:assignment-terms
                          [:x [:expression [:or-operation true false]]]]])

  (testing-parser
    "with one assignment and where" :assignment-statement
    "x = 1 where y > 1" [:assignment-statement
                         [:assignment-terms
                          [:x [:expression 1]]]
                         [:expression [:greater-than-expression :y 1]]]))


(deftest assignment-statement-test
  (testing-statement
    "with one assignment"
    (assignment-statement [:assignment-terms [:x (fn [row dataset] 1)]])
    test-dataset1
    (map #(assoc % :x 1) test-dataset1))

  (testing-statement
    "with two assignments"
    (assignment-statement [:assignment-terms [:x (fn [row dataset] 1)] [:y (fn [row dataset] 2)]])
    test-dataset1
    (map #(apply assoc % [:x 1 :y 2]) test-dataset1))

  (testing-statement
    "with different data types"
    (assignment-statement [:assignment-terms [:col1 (fn [row dataset] true)] [:col2 (fn [row dataset] "clojure")]])
    test-dataset1
    (map #(apply assoc % [:col1 true :col2 "clojure"]) test-dataset1))

  (testing-statement
    "with keyword"
    (assignment-statement [:assignment-terms [:col2 (fn [row dataset] (row :col1))]])
    test-dataset1
    (map #(assoc % :col2 (% :col1)) test-dataset1))

  (testing-statement
    "with chained keyword assignments"
    (assignment-statement [:assignment-terms [:col2 (fn [row dataset] (row :col1))] [:col3 (fn [row dataset] (row :col2))]])
    test-dataset1
    (map #(assoc % :col2 (% :col1) :col3 (% :col1)) test-dataset1))

  (testing-statement
    "with one assignment and where"
    (assignment-statement [:assignment-terms [:x (fn [row dataset] 1)]] (fn [row dataset] (if (>= (:col1 row) 3) true false)))
    test-dataset1
    '({:col1 1, :col2 2, :col3 3, :col4 4}
       {:col1 2, :col2 2, :col3 3, :col4 3}
       {:x 1, :col1 3, :col2 2, :col3 5, :col4 2}
       {:x 1, :col1 4, :col2 5, :col3 5, :col4 1}
       {:x 1, :col1 5, :col2 5, :col3 3, :col4 0}))

  (testing-statement
    "with one assignment and where no match"
    (assignment-statement [:assignment-terms [:x (fn [row dataset] true)]] (fn [row dataset] false))
    test-dataset1
    test-dataset1)

  (testing-statement
    "with nil"
    (assignment-statement [:assignment-terms [:col1 (fn [row dataset] nil)]])
    test-dataset1
    (map #(assoc % :col1 nil) test-dataset1))

  (testing-statement
    "with assignment of aggregate function"
    (assignment-statement [:assignment-terms
                           [:x (mean-transform (fn [row context] (:col1 row)) always-predicate)]])
    test-dataset1
    (map #(assoc % :x 3.0) test-dataset1)))
