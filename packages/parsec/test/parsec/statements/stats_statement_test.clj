(ns parsec.statements.stats-statement-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [stats-statement]]
            [parsec.functions.aggregationfunctions :refer [mean-transform]]))

(deftest stats-statement-parser-test

  (testing-parser! "with no args" :stats-statement "stats")
  (testing-parser! "with identifier but no assignment" :stats-statement "stats x")
  (testing-parser! "with identifier and by but no assignment" :stats-statement "stats x by y")

  (testing-parser
    "one column, no group by" :stats-statement
    "stats x=mean(y)" [:stats-statement
                      [:stats-assignment-terms
                       [:x [:expression [:function :mean [:expression :y]]]]]])

  (testing-parser
    "two columns, no group by" :stats-statement
    "stats x=mean(z), y=x+y" [:stats-statement
                             [:stats-assignment-terms [:x [:expression [:function :mean [:expression :z]]]]
                              [:y [:expression [:addition-operation :x :y]]]]])

  (testing-parser
    "one column, group by one column" :stats-statement
    "stats x=mean(y) by z" [:stats-statement
                           [:stats-assignment-terms [:x [:expression [:function :mean [:expression :y]]]]]
                           [:stats-groupby [:z [:expression :z]]]])

  (testing-parser
    "one column, group by two columns" :stats-statement
    "stats x=mean(y) by z, n" [:stats-statement
                              [:stats-assignment-terms [:x [:expression [:function :mean [:expression :y]]]]]
                              [:stats-groupby [:z [:expression :z]] [:n [:expression :n]]]])

  (testing-parser
    "two columns, group by three columns" :stats-statement
    "stats x=mean(j), z=mean(k) by a, b, c" [:stats-statement
                                           [:stats-assignment-terms
                                            [:x [:expression [:function :mean [:expression :j]]]]
                                            [:z [:expression [:function :mean [:expression :k]]]]]
                                           [:stats-groupby
                                            [:a [:expression :a]]
                                            [:b [:expression :b]]
                                            [:c [:expression :c]]]])

  (testing-parser
    "one column, group by one aliased column" :stats-statement
    "stats x=mean(y) by z1=z" [:stats-statement
                           [:stats-assignment-terms [:x [:expression [:function :mean [:expression :y]]]]]
                           [:stats-groupby [:z1 [:expression :z]]]])

  (testing-parser
    "one column, group by one aliased expression" :stats-statement
    "stats x=mean(y) by z=1+2" [:stats-statement
                              [:stats-assignment-terms [:x [:expression [:function :mean [:expression :y]]]]]
                              [:stats-groupby [:z [:expression [:addition-operation 1 2]]]]]))

(deftest stats-statement-test
  (testing-statement
    "with one assignment, no grouping"
    (stats-statement [:stats-assignment-terms [:x (fn [row context] 1)]])
    test-dataset1
    '({:x 1}))

  (testing-statement
    "with one assignment, one grouping"
    (stats-statement [:stats-assignment-terms [:x (fn [row context] 4)]] [:stats-groupby [:col2 (fn [row context] (:col2 row))]])
    test-dataset1
    '({:col2 2, :x 4} {:col2 5, :x 4}))

  (testing-statement
    "with two assignments, one grouping"
    (stats-statement [:stats-assignment-terms
                      [:x (fn [row context] (:col2 row))]
                      [:y (fn [row context] (+ 2 (:x row)))]]
                     [:stats-groupby [:col2 (fn [row context] (:col2 row))]])
    test-dataset1
    '({:col2 2, :x 2, :y 4} {:col2 5, :x 5, :y 7}))

  (testing-statement
    "with one assignment not included in the grouping, one grouping"
    (stats-statement [:stats-assignment-terms
                      [:x (fn [row context] (:col3 row))]]
                     [:stats-groupby [:col2 (fn [row context] (:col2 row))]])
    test-dataset1
    '({:col2 2, :x nil} {:col2 5, :x nil}))

  (testing-statement
    "with one aggregation assignment, one grouping"
    (stats-statement [:stats-assignment-terms
                      [:x (mean-transform (fn [row context] (:col1 row)) always-predicate)]]
                     [:stats-groupby [:col2 (fn [row context] (:col2 row))]])
    test-dataset1
    '({:col2 2, :x 2.0} {:col2 5, :x 4.5}))

  (testing-statement
    "with one aggregation assignment, one grouping expression"
    (stats-statement [:stats-assignment-terms
                      [:x (mean-transform (fn [row context] (:col1 row)) always-predicate)]]
                     [:stats-groupby [:z (fn [row context] (+ 2 (:col2 row)))]])
    test-dataset1
    '({:z 4, :x 2.0} {:z 7, :x 4.5})))
