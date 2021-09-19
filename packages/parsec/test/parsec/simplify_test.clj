(ns parsec.simplify-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.parser :refer :all]))

(deftest count-test
  (testing-parser
    "with count(*)" :expression
    "count(*)" [:expression
                [:function :count [:expression 1]]]))

(deftest len-simplify-test
  (testing-parser
    "with identifier" :expression
    "len(col1)" [:expression [:function :length [:expression :col1]]]))

(deftest avg-simplify-test
  (testing-parser
    "with identifier" :expression
    "avg(col1)" [:expression [:function :mean [:expression :col1]]]))

(deftest cumulativeavg-simplify-test
  (testing-parser
    "with identifier" :expression
    "cumulativeavg(col1)" [:expression [:function :cumulativemean [:expression :col1]]]))

(deftest stddev_pop-simplify-test
  (testing-parser
    "with identifier" :expression
    "stddev_pop(col1)" [:expression [:function :stddevp [:expression :col1]]]))

(deftest stddev_samp-simplify-test
  (testing-parser
    "with identifier" :expression
    "stddev_samp(col1)" [:expression [:function :stddev [:expression :col1]]]))

(deftest case-simplify-test
  (testing-parser
    "with even arguments" :expression
    "case(x, 1, y, 2)" [:expression
                        [:function :case
                         [:expression :x]
                         [:expression 1]
                         [:expression :y]
                         [:expression 2]]])

  (testing-parser
    "with odd-number of arguments" :expression
    "case(x, 1, 2)" [:expression
                     [:function :case
                      [:expression :x]
                      [:expression 1]
                      [:expression true]
                      [:expression 2]]]))

(deftest coalesce-simplify-test
  (testing-parser
    "with one argument" :expression
    "coalesce(x)" [:expression
                   [:function :case
                    [:expression [:not-equals-expression [:expression :x] [:expression :nil]]]
                    [:expression :x]]])
  (testing-parser
    "with two arguments" :expression
    "coalesce(x,y)" [:expression
                     [:function :case
                      [:expression [:not-equals-expression [:expression :x] [:expression :nil]]]
                      [:expression :x]
                      [:expression [:not-equals-expression [:expression :y] [:expression :nil]]]
                      [:expression :y]]]))

(deftest random-simplify-test
  (testing-parser
    "with no arguments" :expression
    "random()" [:expression [:function :random [:expression 0] [:expression 1]]])

  (testing-parser
    "with one arguments" :expression
    "random(10)" [:expression [:function :random [:expression 0] [:expression 10]]])

  (testing-parser
    "with two arguments" :expression
    "random(10,20)" [:expression [:function :random [:expression 10] [:expression 20]]]))

(deftest round-simplify-test
    (testing-parser
      "with round() and no precision" :expression
      "round(col1)" [:expression
                     [:function :round
                      [:expression :col1]
                      [:expression 0]]])

    (testing-parser
      "with round() and precision" :expression
      "round(col1, 4)" [:expression
                        [:function :round
                         [:expression :col1]
                         [:expression 4]]]))

(deftest istoday-simplify-test
  (testing-parser
    "with happy path" :expression
    "istoday(x)" [:expression [:function :isbetween2
                               [:expression :x]
                               [:expression [:function :today]]
                               [:expression [:function :adddays [:expression [:function :today]] [:expression 1]]]]]))

(deftest isyesterday-simplify-test
  (testing-parser
    "with happy path" :expression
    "isyesterday(x)" [:expression [:function :isbetween2
                                   [:expression :x]
                                   [:expression [:function :yesterday]]
                                   [:expression [:function :today]]]]))
(deftest istomorrow-simplify-test
  (testing-parser
    "with happy path" :expression
    "istomorrow(x)" [:expression [:function :isbetween2
                                  [:expression :x]
                                  [:expression [:function :tomorrow]]
                                  [:expression [:function :adddays [:expression [:function :tomorrow]] [:expression 1]]]]]))
