(ns parsec.statements.set-statement-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [set-statement]]))

(deftest set-statement-parser-test
  (testing-parser!
    "with incomplete set" :set-statement
    "SET @x =")

  (testing-parser
    "with one set" :set-statement
    "SET @x = 1" [:set-statement [:set-terms [[:variable "@x"] [:expression 1]]]])

  (testing-parser
    "with prime variable name" :set-statement
    "SET @x' = 1" [:set-statement [:set-terms [[:variable "@x'"] [:expression 1]]]])

  (testing-parser
    "with one set to null" :set-statement
    "SET @abc = null" [:set-statement [:set-terms [[:variable "@abc"] [:expression nil]]]])

  (testing-parser
    "with one set to string" :set-statement
    "SET @col1 = 'hello'" [:set-statement [:set-terms [[:variable "@col1"] [:expression "hello"]]]])

  (testing-parser
    "with two sets" :set-statement
    "SET @x = 1, @y = 2" [:set-statement
                          [:set-terms [[:variable "@x"] [:expression 1]]
                           [[:variable "@y"] [:expression 2]]]])

  (testing-parser
    "with a boolean expression" :set-statement
    "SET @x = true or false" [:set-statement
                              [:set-terms [[:variable "@x"] [:expression [:or-operation true false]]]]])

  (testing-parser
    "with one set and where" :set-statement
    "SET @x = first(col1) where y > 1" [:set-statement
                                        [:set-terms [[:variable "@x"] [:expression [:function :first [:expression :col1]]]]]
                                        [:expression [:greater-than-expression :y 1]]])
  (testing-parser
    "with first-order function set" :set-statement
    "SET @inc = x -> x + 1" [:set-statement [:set-terms [[:variable "@inc"] [:expression [:function-impl [:x] [:expression [:addition-operation :x 1]]]]]]])

  (testing-parser
    "with two first-order function set" :set-statement
    "SET @inc = x -> x + 1, @dec = x -> x - 1" [:set-statement
                                                [:set-terms
                                                 [[:variable "@inc"]
                                                  [:expression
                                                   [:function-impl [:x] [:expression [:addition-operation :x 1]]]]]
                                                 [[:variable "@dec"]
                                                  [:expression
                                                   [:function-impl [:x] [:expression [:subtraction-operation :x 1]]]]]]]))


(deftest set-statement-test
  (testing-statement
    "with current-dataset unchanged"
    (set-statement [:set-terms [[:variable "@x"] (fn [_ _] 1)]])
    test-dataset2
    test-dataset2)

  (testing-statement-context
    "with one set"
    (set-statement [:set-terms [[:variable "@x"] (fn [_ _] 1)]])
    test-dataset1
    (fn [context] (let [variables (:variables context)]
                    (= 1 (get variables "@x")))))

  (testing-statement-context
    "with two sets"
    (set-statement [:set-terms [[:variable "@x"] (fn [_ _] 1)] [[:variable "@y"] (fn [_ _] 2)]])
    test-dataset1
    (fn [context] (let [variables (:variables context)]
                    (and (= 1 (get variables "@x"))
                         (= 2 (get variables "@y"))))))

  (testing-statement-context
    "with different data types"
    (set-statement [:set-terms [[:variable "@col1"] (fn [_ _] true)] [[:variable "@col2"] (fn [_ _] "clojure")]])
    test-dataset1
    (fn [context] (let [variables (:variables context)]
                    (and (= true (get variables "@col1"))
                         (= "clojure" (get variables "@col2"))))))

  (testing-statement-context
    "with nil"
    (set-statement [:set-terms [[:variable "@x"] (fn [_ _] nil)]])
    test-dataset1
    (fn [context] (let [variables (:variables context)]
                    (nil? (get variables "@x")))))

  (testing-statement-context
    "with one set and where"
    (set-statement [:set-terms
                    [[:variable "@x"]
                     (fn [_ context] (count (:current-dataset context)))]]
                   (fn [row _] (> (:col1 row) 2)))
    test-dataset1
    (fn [context] (let [variables (:variables context)]
                    (= 3 (get variables "@x")))))

  (testing-statement-context
    "with one set and where no rows"
    (set-statement [:set-terms [[:variable "@x"]
                                (fn [_ context] (count (:current-dataset context)))]]
                   (fn [row _] (> (:col1 row) 1000)))
    test-dataset1
    (fn [context] (let [variables (:variables context)]
                    (= 0 (get variables "@x")))))

  (testing-statement-context
    "with chained sets"
    (set-statement [:set-terms [[:variable "@x"] (fn [_ _] 1)] [[:variable "@y"] (fn [_ context] (+ 20 (get-in context [:variables "@x"])))]])
    test-dataset1
    (fn [context] (let [variables (:variables context)]
                    (and (= 1 (get variables "@x"))
                         (= 21 (get variables "@y"))))))

  (testing-statement-context
    "with three chained sets"
    (set-statement [:set-terms
                    [[:variable "@x"] (fn [_ _] 9)]
                    [[:variable "@y"] (fn [_ context] (* 2 (get-in context [:variables "@x"])))]
                    [[:variable "@z"] (fn [_ context] (+ (get-in context [:variables "@x"])
                                                         (get-in context [:variables "@y"])))]])
    test-dataset1
    (fn [context] (let [variables (:variables context)]
                    (and (= 9 (get variables "@x"))
                         (= 18 (get variables "@y"))
                         (= 27 (get variables "@z"))
                         (= test-dataset1 (:current-dataset context)))))))
