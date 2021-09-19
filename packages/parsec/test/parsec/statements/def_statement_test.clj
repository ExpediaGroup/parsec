(ns parsec.statements.def-statement-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [def-statement]]))

(deftest def-statement-parser-test
  (testing-parser!
    "with incomplete def" :def-statement
    "DEF x =")

  (testing-parser
    "with optional parens" :def-statement
    "DEF one = -> 1" [:def-statement [:one [:function-impl [] [:expression 1]]]])

  (testing-parser
    "with no arguments" :def-statement
    "DEF one = () -> 1" [:def-statement [:one [:function-impl [] [:expression 1]]]])

  (testing-parser
    "with one argument" :def-statement
    "DEF square = (x) -> x * x" [:def-statement [:square [:function-impl [:x] [:expression [:multiplication-operation :x :x]]]]])

  (testing-parser
    "with two arguments" :def-statement
    "DEF power = (x, y) -> x ^ y" [:def-statement [:power [:function-impl [:x :y] [:expression [:exponent-operation :x :y]]]]])

  (testing-parser
    "with two arguments no parens" :def-statement
    "DEF power = x, y -> x ^ y" [:def-statement [:power [:function-impl [:x :y] [:expression [:exponent-operation :x :y]]]]])

  (testing-parser
    "with two functions defined" :def-statement
    "DEF square = (x) -> x * x, cube = (y) -> y * y * y" [:def-statement [:square [:function-impl [:x] [:expression [:multiplication-operation :x :x]]]]
                                                          [:cube
                                                           [:function-impl [:y]
                                                            [:expression
                                                             [:multiplication-operation
                                                              [:multiplication-operation :y :y]
                                                              :y]]]]]))


(deftest def-statement-test
  (testing-statement
    "with current-dataset unchanged"
    (def-statement [:one [] (fn [_ _] 1)])
    test-dataset2
    test-dataset2)

  (testing-statement-context
    "with one set"
    (def-statement [:one [] (fn [_ _] 1)])
    test-dataset1
    (fn [context] (let [functions (:functions context)]
                    (= 1 (count functions)))))

  (testing-statement-context
    "with two sets"
    (def-statement [:one [] (fn [_ _] 1)] [:two [] (fn [_ _] 2)])
    test-dataset1
    (fn [context] (let [functions (:functions context)]
                    (and (map? (get functions :one))
                         (map? (get functions :two))))))

  (testing-query-context
    "function with one argument"
    "def x = (x) -> x + 100 | set @y = x(1), @z = x(9)"
    {}
    (fn [context] (let [variables (:variables context)
                        y (get variables "@y")
                        z (get variables "@z")]
                    (and (= 101 y) (= 109 z))))))
