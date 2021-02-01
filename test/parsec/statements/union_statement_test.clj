(ns parsec.statements.union-statement-test
  (:require [clojure.test :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [union-statement]]))

(deftest union-statement-parser-test
  (testing-parser
    "with implied mode" :union-statement
    "union input mock" [:union-statement :distinct [:query [:input-statement :mock '(:function :tolowercasemap)]]])
  (testing-parser
    "with implied mode and parentheses" :union-statement
    "union (input mock)" [:union-statement :distinct [:query [:input-statement :mock '(:function :tolowercasemap)]]])
  (testing-parser
    "with distinct mode" :union-statement
    "union distinct input mock" [:union-statement :distinct [:query [:input-statement :mock '(:function :tolowercasemap)]]])
  (testing-parser
    "with distinct mode and parentheses" :union-statement
    "union distinct (input mock)" [:union-statement :distinct [:query [:input-statement :mock '(:function :tolowercasemap)]]])
  (testing-parser
    "with all mode" :union-statement
    "union all input mock" [:union-statement :all [:query [:input-statement :mock '(:function :tolowercasemap)]]])
  (testing-parser
    "with all mode and parentheses" :union-statement
    "union all (input mock)" [:union-statement :all [:query [:input-statement :mock '(:function :tolowercasemap)]]])
  (testing-parser
    "with all mode and multiple statements" :union-statement
    "union all (input mock | reverse)" [:union-statement :all [:query [:input-statement :mock '(:function :tolowercasemap)] [:reverse-statement]]]))

(deftest union-statement-test
  (testing-statement
    "with all test-dataset1 twice"
    (union-statement :all (fn [_] {:current-dataset test-dataset1}))
    test-dataset1
    (concat test-dataset1 test-dataset1))

  (testing-statement
    "with all test-dataset1 and test-dataset2"
    (union-statement :all (fn [_] {:current-dataset test-dataset2}))
    test-dataset1
    (concat test-dataset1 test-dataset2))

  (testing-statement
    "with distinct test-dataset1 and test-dataset2"
    (union-statement :distinct (fn [_] {:current-dataset test-dataset2}))
    test-dataset1
    (concat test-dataset1 test-dataset2))

  (testing-statement
    "with distinct test-dataset1 twice"
    (union-statement :distinct (fn [_] {:current-dataset test-dataset1}))
    test-dataset1
    test-dataset1))
