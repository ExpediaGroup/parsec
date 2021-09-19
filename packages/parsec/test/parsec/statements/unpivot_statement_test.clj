(ns parsec.statements.unpivot-statement-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [pivot-statement]]
            [parsec.functions.aggregationfunctions :refer [count-transform max-transform]]))

(deftest unpivot-statement-parser-test

  (testing-parser! "with no args" :unpivot-statement "unpivot")
  (testing-parser! "with no per" :unpivot-statement "unpivot x")
  (testing-parser! "with no by" :unpivot-statement "unpivot x per y")
  (testing-parser! "with no per" :unpivot-statement "unpivot x by y")

  (testing-parser
    "with one groupby" :unpivot-statement
    "unpivot x per y by z" [:unpivot-statement
                          [:unpivot-terms :x]
                          [:unpivot-per-terms :y]
                          [:unpivot-groupby :z]])

  (testing-parser
    "with two groupbys" :unpivot-statement
    "unpivot x per y by z, zz" [:unpivot-statement
                            [:unpivot-terms :x]
                            [:unpivot-per-terms :y]
                            [:unpivot-groupby :z :zz]]))

;(deftest pivot-statement-test
;
;  (testing-statement
;    "with one aggregation assignment, one per, and one grouping"
;    (pivot-statement [:pivot-terms
;                      [:x (count-transform [:expression 1] always-predicate)]]
;                     [:pivot-per-terms :col3 ]
;                     [:pivot-groupby :col2])
;    test-dataset1
;    '({:col2 2, :5 1, :3 2} {:col2 5, :5 1, :3 1}))
;
;  (testing-statement
;    "with one aggregation assignment, one per, and two groupings"
;    (pivot-statement [:pivot-terms
;                      [:x (count-transform [:expression 1] always-predicate)]]
;                     [:pivot-per-terms :col3 ]
;                     [:pivot-groupby :col2 :col4])
;    test-dataset1
;    '({:col2 2, :col4 4, :3 1}
;      {:col2 2, :col4 3, :3 1}
;      {:col2 2, :col4 2, :5 1,}
;      {:col2 5, :col4 1, :5 1}
;      {:col2 5, :col4 0, :3 1}))
;
;  (testing-statement
;    ;input mock | pivot x=count(*),y=max(col4) per col3 by col2
;    "with two aggregation assignments, one per, and one grouping"
;    (pivot-statement [:pivot-terms
;                      [:x (count-transform [:expression 1] always-predicate)]
;                      [:y (max-transform :col4 always-predicate)]]
;                     [:pivot-per-terms :col3 ]
;                     [:pivot-groupby :col2])
;    test-dataset1
;    (list
;      {:col2 2, (keyword "3: x") 2, (keyword "3: y") 4, (keyword "5: x") 1, (keyword "5: y") 2}
;      {:col2 5, (keyword "3: x") 1, (keyword "3: y") 0, (keyword "5: x") 1, (keyword "5: y") 1}))
;
;  (testing-statement
;    ;input mock | pivot count(*) per col2 - col1 by col3
;    "with one aggregation assignment, one per expression, and one grouping"
;    (pivot-statement [:pivot-terms
;                      [:x (count-transform [:expression 1] always-predicate)]]
;                     [:pivot-per-terms (fn [row _] (- (:col2 row) (:col1 row)))]
;                     [:pivot-groupby :col3])
;    test-dataset1
;    (list {:col3 3, :0 2, :1 1}
;          {:col3 5, (keyword "-1") 1, :1 1})))
