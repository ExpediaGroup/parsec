(ns parsec.expressions.expressions-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.expressions :refer :all]
            [clj-time.core :as time]))


(deftest eval-expression-test
  (testing "with number"
    (is (= 4 (eval-expression 4 {} nil))))

  (testing "with string"
    (is (= "hello" (eval-expression "hello" {} nil))))

  (testing "with boolean"
    (is (= true (eval-expression true {} nil))))

  (testing "with fn"
    (is (= {:x 1} (eval-expression (fn [row dataset] row) {:x 1} nil)))
    (is (= "hello" (eval-expression (fn [row dataset] "hello") {} nil))))

  (testing "with keyword"
    (is (= 1 (eval-expression :x {:x 1} nil)))
    (is (= 2 (eval-expression :y {:x 1, :y 2} nil)))
    (is (nil? (eval-expression :x {} nil)))))

(deftest expression-transform-test
  (testing "happy path"
    (is (= "hello" ((expression-transform "hello") {} nil))))

  (testing "with keyword"
    (is (= 1 ((expression-transform :x) {:x 1} nil))))

  (testing "with function"
    (is (= 1 ((expression-transform (fn [row dataset] (row :x))) {:x 1} nil)))))

(deftest negation-transform-test
  (testing "happy path"
    (is (= -1 ((negation-transform 1) {} nil))))

  (testing "with keyword"
    (is (= -2 ((negation-transform :x) {:x 2} nil))))

  (testing "with function"
    (is (= -3 ((negation-transform (fn [row dataset] (row :x))) {:x 3} nil))))

  (testing "with nil"
    (is (nil? ((negation-transform nil) {} nil))))

  (testing "unsupported data types"
    (is (thrown? Exception ((negation-transform false) {} nil)))
    (is (thrown? Exception ((negation-transform "hello") {} nil)))))

(deftest addition-transform-test
  (testing "numbers, happy path"
    (is (= 3 ((addition-transform 1 2) {} nil))))

  (testing "numbers, with keywords"
    (is (= 5 ((addition-transform :x :y) {:x 2 :y 3} nil))))

  (testing "numbers, with function"
    (is (= 10 ((addition-transform 7 (fn [row dataset] (row :x))) {:x 3} nil))))

  (testing "numbers, with nil"
    (is (nil? ((addition-transform 10 nil) {} nil)))
    (is (nil? ((addition-transform nil 10) {} nil))))

  (testing "strings, happy path"
    (is (= "hello world" ((addition-transform "hello " "world") {} nil))))

  (testing "strings, with nil"
    (is (= "hello" ((addition-transform "hello" nil) {} nil)))
    (is (= "hello" ((addition-transform nil "hello") {} nil))))

  (testing "string plus number"
    (is (= "hello99" ((addition-transform "hello" 99) {} nil))))

  (testing "number plus string"
    (is (= "0world" ((addition-transform 0 "world") {} nil))))

  (testing "string plus boolean"
    (is (= "nottrue" ((addition-transform "not" true) {} nil))))

  (testing "boolean plus boolean"
    (is (thrown? Exception ((addition-transform false true) {} nil)))))

(deftest subtraction-transform-test
  (testing "numbers, happy path"
    (is (= 3 ((subtraction-transform 5 2) {} nil))))

  (testing "numbers, with keywords"
    (is (= 7 ((subtraction-transform :x :y) {:x 10 :y 3} nil))))

  (testing "numbers, with function"
    (is (= 4 ((subtraction-transform 7 (fn [row _] (row :x))) {:x 3} nil))))

  (testing "numbers, with nil"
    (is (nil? ((subtraction-transform 10 nil) {} nil)))
    (is (nil? ((subtraction-transform nil 10) {} nil))))

  (let [now (time/now)]
    (testing "with datetimes"
      (is (= (time/interval now now) ((subtraction-transform now now) {} nil)))
      (is (= (time/interval (time/today-at-midnight) now) ((subtraction-transform now (time/today-at-midnight)) {} nil))))
    )
  (testing "unsupported data types"
    (is (thrown? Exception ((subtraction-transform false true) {} nil)))
    (is (thrown? Exception ((subtraction-transform "hello" "world") {} nil)))
    (is (thrown? Exception ((subtraction-transform 40 "1") {} nil)))
    (is (thrown? Exception ((subtraction-transform "100" 1) {} nil)))
    (is (thrown? Exception ((subtraction-transform 40 (time/now)) {} nil)))
    (is (thrown? Exception ((subtraction-transform (time/now) "1") {} nil)))))

(deftest multiplication-transform-test
  (testing "numbers, happy path"
    (is (= 10 ((multiplication-transform 5 2) {} nil))))

  (testing "numbers, with keywords"
    (is (= 30 ((multiplication-transform :x :y) {:x 10 :y 3} nil))))

  (testing "numbers, with function"
    (is (= 21 ((multiplication-transform 7 (fn [row dataset] (row :x))) {:x 3} nil))))

  (testing "numbers, with nil"
    (is (nil? ((multiplication-transform 10 nil) {} nil)))
    (is (nil? ((multiplication-transform nil 10) {} nil))))

  (testing "unsupported data types"
    (is (thrown? Exception ((multiplication-transform false true) {} nil)))
    (is (thrown? Exception ((multiplication-transform "hello" "world") {} nil)))
    (is (thrown? Exception ((multiplication-transform 40 "1") {} nil)))
    (is (thrown? Exception ((multiplication-transform "100" 1) {} nil)))))

(deftest division-transform-test
  (testing "numbers, happy path"
    (is (= 5/2 ((division-transform 5 2) {} nil)))
    (is (= Double/POSITIVE_INFINITY ((division-transform 5 0) {} nil)))
    (is (= Double/NEGATIVE_INFINITY ((division-transform -5 0) {} nil)))
    (is (Double/isNaN ((division-transform 0 0) {} nil))))

  (testing "numbers, with keywords"
    (is (= 10 ((division-transform :x :y) {:x 30 :y 3} nil))))

  (testing "numbers, with function"
    (is (= 21/3 ((division-transform 21 (fn [row dataset] (row :x))) {:x 3} nil))))

  (testing "numbers, with nil"
    (is (nil? ((division-transform 10 nil) {} nil)))
    (is (nil? ((division-transform nil 10) {} nil))))

  (testing "unsupported data types"
    (is (thrown? Exception ((division-transform false true) {} nil)))
    (is (thrown? Exception ((division-transform "hello" "world") {} nil)))
    (is (thrown? Exception ((division-transform 40 "1") {} nil)))
    (is (thrown? Exception ((division-transform "100" 1) {} nil)))))

(deftest modulus-transform-test
  (testing "numbers, happy path"
    (is (= 2 ((modulus-transform 8 3) {} nil)))
    (is (= 0 ((modulus-transform 0 1) {} nil)))
    (is (Double/isNaN ((modulus-transform 5 0) {} nil))))

  (testing "numbers, with keywords"
    (is (= 3 ((modulus-transform :x :y) {:x 30 :y 9} nil))))

  (testing "numbers, with function"
    (is (= 4 ((modulus-transform 20 (fn [row dataset] (row :x))) {:x 8} nil))))

  (testing "numbers, with nil"
    (is (nil? ((modulus-transform 10 nil) {} nil)))
    (is (nil? ((modulus-transform nil 10) {} nil))))

  (testing "unsupported data types"
    (is (thrown? Exception ((modulus-transform false true) {} nil)))
    (is (thrown? Exception ((modulus-transform "hello" "world") {} nil)))
    (is (thrown? Exception ((modulus-transform 40 "1") {} nil)))
    (is (thrown? Exception ((modulus-transform "100" 1) {} nil)))))

(deftest exponent-transform-test
  (testing "numbers, happy path"
    (is (= 8 ((exponent-transform 2 3) {} nil)))
    (is (= 1 ((exponent-transform 2 0) {} nil)))
    (is (= 0 ((exponent-transform 0 1) {} nil)))
    (is (= 1/8 ((exponent-transform 2 -3) {} nil)))
    (is (= 1.5874010519681996 ((exponent-transform 2 2/3) {} nil))))

  (testing "numbers, with keywords"
    (is (= 27 ((exponent-transform :x :y) {:x 3 :y 3} nil))))

  (testing "numbers, with function"
    (is (= 16 ((exponent-transform 4 (fn [row dataset] (row :x))) {:x 2} nil))))

  (testing "numbers, with nil"
    (is (nil? ((exponent-transform 10 nil) {} nil)))
    (is (nil? ((exponent-transform nil 10) {} nil))))

  (testing "unsupported data types"
    (is (thrown? Exception ((exponent-transform false true) {} nil)))
    (is (thrown? Exception ((exponent-transform "hello" "world") {} nil)))
    (is (thrown? Exception ((exponent-transform 40 "1") {} nil)))
    (is (thrown? Exception ((exponent-transform "100" 1) {} nil)))))
