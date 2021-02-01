(ns parsec.statements.project-statement-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [project-statement]]))

(deftest project-statement-parser-test
  (testing-parser!
    "with missing expression" :project-statement
    "project")

  (testing-parser
    "with literal" :project-statement
    "project 1" [:project-statement [:expression 1]])

  (testing-parser
    "with string" :project-statement
    "project 'hello'" [:project-statement [:expression "hello"]])

  (testing-parser
    "with list of integers" :project-statement
    "project [1, 2, 3]"
    [:project-statement
     [:expression
      [:function
       :tolist
       [:expression 1]
       [:expression 2]
       [:expression 3]]]])

  (testing-parser
    "with list of maps" :project-statement
    "project [{a: 1, b: 1}, {a: 2, b: 2}]"
    [:project-statement
     [:expression
      [:function
       :tolist
       [:expression
        [:function :tomap "a" [:expression 1] "b" [:expression 1]]]
       [:expression
        [:function :tomap "a" [:expression 2] "b" [:expression 2]]]]]])

  (testing-parser
    "with function call" :project-statement
    "project parsecsv(col1)" [:project-statement [:expression [:function :parsecsv [:expression :col1]]]]))

(deftest project-statement-test
  (testing-statement
    "with map"
    (project-statement (fn [_ _] { :a 1 :b 2}))
    test-dataset1
    '({ :a 1 :b 2}))

  (testing-statement
    "with list of maps"
    (project-statement (fn [_ _] [{ :a 1 :b 2}]))
    test-dataset1
    '({ :a 1 :b 2}))

  (testing-statement
    "with list of maps 2"
    (project-statement (fn [_ _] [{ :a 1 :b 2} { :a 3 :b 4}]))
    test-dataset1
    '({ :a 1 :b 2} { :a 3 :b 4}))

  (testing-statement
    "with list of integers"
    (project-statement (fn [_ _] [1 2 3]))
    test-dataset1
    '({ :value 1 } { :value 2 } { :value 3 }))

  (testing-statement
    "with list of strings"
    (project-statement (fn [_ _] ["a" "b" "c"]))
    test-dataset1
    '({ :value "a" } { :value "b" } { :value "c" }))

  (testing-statement!
    "with string"
    (project-statement (fn [_ _] "hello world"))
    test-dataset1)

  (testing-statement!
    "with number"
    (project-statement (fn [_ _] 2))
    test-dataset1)
  )
