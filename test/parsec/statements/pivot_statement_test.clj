(ns parsec.statements.pivot-statement-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [pivot-statement]]
            [parsec.functions.aggregationfunctions :refer [mean-transform count-transform max-transform]]))

(deftest pivot-statement-parser-test

  (testing-parser! "with no args" :pivot-statement "pivot")
  (testing-parser! "with no per" :pivot-statement "pivot x")
  (testing-parser! "with no by" :pivot-statement "pivot x per y")
  (testing-parser! "with no per" :pivot-statement "pivot x by y")

  (testing-parser
    "with one column" :pivot-statement
    "pivot x per y by z" [:pivot-statement
                          [:pivot-terms [:x [:expression :x]]]
                          [:pivot-per-terms [:expression :y]]
                          [:pivot-groupby [:z [:expression :z]]]])

  (testing-parser
    "with two columns" :pivot-statement
    "pivot w, x per y by z" [:pivot-statement
                          [:pivot-terms [:w [:expression :w]] [:x [:expression :x]]]
                          [:pivot-per-terms [:expression :y]]
                          [:pivot-groupby [:z [:expression :z]]]])

  (testing-parser
    "with two per" :pivot-statement
    "pivot fn(x) per u, v by z" [:pivot-statement
                                 [:pivot-terms [(keyword "fn(x)") [:expression [:function :fn [:expression :x]]]]]
                                 [:pivot-per-terms [:expression :u] [:expression :v]]
                                 [:pivot-groupby [:z [:expression :z]]]])

  (testing-parser
    "with two per and mean" :pivot-statement
    "pivot mean(x) per u, v by z" [:pivot-statement
                                 [:pivot-terms [(keyword "mean(x)") [:expression [:function :mean [:expression :x]]]]]
                                 [:pivot-per-terms [:expression :u] [:expression :v]]
                                 [:pivot-groupby [:z [:expression :z]]]])
  (testing-parser
    "with two per and conditional mean" :pivot-statement
    "pivot mean(x,x>0) per u, v by z" [:pivot-statement
                                  [:pivot-terms [(keyword "mean(x,x>0)") [:expression [:function :mean [:expression :x] [:expression [:greater-than-expression :x 0]]]]]]
                                  [:pivot-per-terms [:expression :u] [:expression :v]]
                                  [:pivot-groupby [:z [:expression :z]]]])
  (testing-parser
    "with two groups" :pivot-statement
    "pivot w, x per y by z, zz" [:pivot-statement
                             [:pivot-terms [:w [:expression :w]] [:x [:expression :x]]]
                             [:pivot-per-terms [:expression :y]]
                             [:pivot-groupby [:z [:expression :z]] [:zz [:expression :zz]]]])

  (testing-parser
    "with one assigned column" :pivot-statement
    "pivot x=mean(value) per y by z" [:pivot-statement
                          [:pivot-terms [:x [:expression [:function :mean [:expression :value]]]]]
                          [:pivot-per-terms [:expression :y]]
                          [:pivot-groupby [:z [:expression :z]]]]))


(deftest pivot-statement-test

  (testing-statement
    "with one aggregation assignment, one per, and one grouping"
    (pivot-statement [:pivot-terms
                      [:x (count-transform [:expression 1] always-predicate)]]
                     [:pivot-per-terms :col3 ]
                     [:pivot-groupby :col2])
    test-dataset1
    '({:col2 2, :5 1, :3 2} {:col2 5, :5 1, :3 1}))

  (testing-statement
    "with one aggregation assignment, one per, and two groupings"
    (pivot-statement [:pivot-terms
                      [:x (count-transform [:expression 1] always-predicate)]]
                     [:pivot-per-terms :col3 ]
                     [:pivot-groupby :col2 :col4])
    test-dataset1
    '({:col2 2, :col4 4, :3 1}
      {:col2 2, :col4 3, :3 1}
      {:col2 2, :col4 2, :5 1,}
      {:col2 5, :col4 1, :5 1}
      {:col2 5, :col4 0, :3 1}))

  (testing-statement
    ;input mock | pivot x=count(*),y=max(col4) per col3 by col2
    "with two aggregation assignments, one per, and one grouping"
    (pivot-statement [:pivot-terms
                      [:x (count-transform [:expression 1] always-predicate)]
                      [:y (max-transform :col4 always-predicate)]]
                     [:pivot-per-terms :col3 ]
                     [:pivot-groupby :col2])
    test-dataset1
    (list
      {:col2 2, (keyword "3: x") 2, (keyword "3: y") 4, (keyword "5: x") 1, (keyword "5: y") 2}
      {:col2 5, (keyword "3: x") 1, (keyword "3: y") 0, (keyword "5: x") 1, (keyword "5: y") 1}))

  (testing-statement
    ;input mock | pivot count(*) per col2 - col1 by col3
    "with one aggregation assignment, one per expression, and one grouping"
    (pivot-statement [:pivot-terms
                      [:x (count-transform [:expression 1] always-predicate)]]
                     [:pivot-per-terms (fn [row _] (- (:col2 row) (:col1 row)))]
                     [:pivot-groupby :col3])
    test-dataset1
    (list {:col3 3, :0 2, :1 1}
          {:col3 5, (keyword "-1") 1, :1 1})))
