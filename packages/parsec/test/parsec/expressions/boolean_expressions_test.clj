(ns parsec.expressions.boolean-expressions-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.boolean-expressions :refer :all]
            [clj-time.core :as time]))

(deftest not-transform-test
  (testing "with booleans"
    (is (true? ((not-transform false) {} nil)))
    (is (false? ((not-transform true) {} nil))))

  (testing "with numbers"
    (is (true? ((not-transform 0) {} nil)))
    (is (false? ((not-transform 1) {} nil)))
    (is (false? ((not-transform 100) {} nil))))

  (testing "with nil"
    (is (nil? ((not-transform nil) {} nil))))

  (testing "unsupported data types"
    (is (thrown? Exception ((not-transform "x") {} nil)))))

(deftest and-transform-test
  (testing "happy path"
    (is (true? ((and-transform true true) {} nil))))

  (testing "with booleans"
    (is (false? ((and-transform false false) {} nil)))
    (is (false? ((and-transform false true) {} nil)))
    (is (false? ((and-transform true false) {} nil))))

  (testing "with numbers"
    (is (true? ((and-transform 1 1) {} nil)))
    (is (false? ((and-transform 1 0) {} nil)))
    (is (false? ((and-transform 0 0) {} nil)))
    (is (false? ((and-transform 0 0) {} nil)))
    (is (true? ((and-transform 100 1) {} nil)))
    (is (false? ((and-transform 0 30) {} nil))))

  (testing "with mixed numbers"
    (is (true? ((and-transform 1 true) {} nil)))
    (is (true? ((and-transform true 1) {} nil)))
    (is (false? ((and-transform 0 true) {} nil))))

  (testing "with nil"
    (is (nil? ((and-transform true nil) {} nil)))
    (is (nil? ((and-transform nil true) {} nil))))

  (testing "unsupported data types"
    (is (thrown? Exception ((and-transform 10 "x") {} nil)))
    (is (thrown? Exception ((and-transform true "x") {} nil)))
    (is (thrown? Exception ((and-transform "y" true) {} nil)))
    (is (thrown? Exception ((and-transform "x" "y") {} nil)))))

(deftest or-transform-test
  (testing "happy path"
    (is (true? ((or-transform true true) {} nil))))

  (testing "with booleans"
    (is (false? ((or-transform false false) {} nil)))
    (is (true? ((or-transform false true) {} nil)))
    (is (true? ((or-transform true false) {} nil))))

  (testing "with numbers"
    (is (true? ((or-transform 1 1) {} nil)))
    (is (true? ((or-transform 1 0) {} nil)))
    (is (false? ((or-transform 0 0) {} nil)))
    (is (false? ((or-transform 0 0) {} nil)))
    (is (true? ((or-transform 100 1) {} nil)))
    (is (true? ((or-transform 0 30) {} nil))))

  (testing "with mixed numbers"
    (is (true? ((or-transform 1 false) {} nil)))
    (is (true? ((or-transform false 1) {} nil)))
    (is (true? ((or-transform false 10) {} nil)))
    (is (true? ((or-transform 0 true) {} nil)))
    (is (false? ((or-transform 0 false) {} nil))))

  (testing "with nil"
    (is (nil? ((or-transform true nil) {} nil)))
    (is (nil? ((or-transform nil true) {} nil))))

  (testing "unsupported data types"
    (is (thrown? Exception ((or-transform 10 "x") {} nil)))
    (is (thrown? Exception ((or-transform true "x") {} nil)))
    (is (thrown? Exception ((or-transform "y" true) {} nil)))
    (is (thrown? Exception ((or-transform "x" "y") {} nil)))))

(deftest xor-transform-test
  (testing "with booleans"
    (is (false? ((xor-transform true true) {} nil)))
    (is (false? ((xor-transform false false) {} nil)))
    (is (true? ((xor-transform false true) {} nil)))
    (is (true? ((xor-transform true false) {} nil))))

  (testing "with numbers"
    (is (false? ((xor-transform 1 1) {} nil)))
    (is (true? ((xor-transform 1 0) {} nil)))
    (is (false? ((xor-transform 0 0) {} nil)))
    (is (false? ((xor-transform 0 0) {} nil)))
    (is (false? ((xor-transform 100 1) {} nil)))
    (is (true? ((xor-transform 0 30) {} nil))))

  (testing "with mixed numbers"
    (is (true? ((xor-transform 1 false) {} nil)))
    (is (true? ((xor-transform false 1) {} nil)))
    (is (true? ((xor-transform false 10) {} nil)))
    (is (true? ((xor-transform 0 true) {} nil)))
    (is (false? ((xor-transform 0 false) {} nil))))

  (testing "with nil"
    (is (nil? ((xor-transform true nil) {} nil)))
    (is (nil? ((xor-transform nil true) {} nil))))

  (testing "unsupported data types"
    (is (thrown? Exception ((xor-transform 10 "x") {} nil)))
    (is (thrown? Exception ((xor-transform true "x") {} nil)))
    (is (thrown? Exception ((xor-transform "y" true) {} nil)))
    (is (thrown? Exception ((xor-transform "x" "y") {} nil)))))

(deftest equals-transform-test
  (testing "with booleans"
    (is (true? ((equals-transform true true) {} nil)))
    (is (true? ((equals-transform false false) {} nil)))
    (is (false? ((equals-transform false true) {} nil)))
    (is (false? ((equals-transform true false) {} nil))))

  (testing "with numbers"
    (is (true? ((equals-transform 0 0) {} nil)))
    (is (true? ((equals-transform 1 1) {} nil)))
    (is (false? ((equals-transform 1 0) {} nil)))
    (is (true? ((equals-transform 0 0.0) {} nil)))
    (is (true? ((equals-transform 100 200/2) {} nil))))

  (testing "with strings"
    (is (true? ((equals-transform "blue" "blue") {} nil)))
    (is (false? ((equals-transform "red" "blue") {} nil)))
    (is (false? ((equals-transform "abc" "Abc") {} nil)))
    (is (true? ((equals-transform "" "") {} nil)))
    (is (false? ((equals-transform " " "") {} nil))))

  (testing "with dates"
    (let [now (time/now)]
      (is (true? ((equals-transform (time/today) (time/today)) {} nil)))
      (is (true? ((equals-transform now now) {} nil)))
      (is (false? ((equals-transform now (time/plus now (time/millis 1))) {} nil)))
      (is (false? ((equals-transform (time/today) now) {} nil)))))

  (testing "with mixed types"
    (is (false? ((equals-transform 1 false) {} nil)))
    (is (true? ((equals-transform 1 true) {} nil)))
    (is (true? ((equals-transform 0 false) {} nil)))
    (is (false? ((equals-transform 0 true) {} nil)))

    (is (false? ((equals-transform false 1) {} nil)))
    (is (true? ((equals-transform true 1) {} nil)))
    (is (true? ((equals-transform false 0) {} nil)))
    (is (false? ((equals-transform true 0) {} nil)))

    (is (false? ((equals-transform false "red") {} nil)))
    (is (false? ((equals-transform "red" true) {} nil)))
    (is (false? ((equals-transform "20" 20) {} nil))))

  (testing "with nil"
    (is (true? ((equals-transform nil nil) {} nil)))
    (is (false? ((equals-transform true nil) {} nil)))
    (is (false? ((equals-transform nil true) {} nil)))))

(deftest not-equals-transform-test
  (testing "with booleans"
    (is (false? ((not-equals-transform true true) {} nil)))
    (is (false? ((not-equals-transform false false) {} nil)))
    (is (true? ((not-equals-transform false true) {} nil)))
    (is (true? ((not-equals-transform true false) {} nil))))

  (testing "with numbers"
    (is (false? ((not-equals-transform 0 0) {} nil)))
    (is (false? ((not-equals-transform 1 1) {} nil)))
    (is (true? ((not-equals-transform 1 0) {} nil)))
    (is (false? ((not-equals-transform 0 0.0) {} nil)))
    (is (false? ((not-equals-transform 100 200/2) {} nil))))

  (testing "with strings"
    (is (false? ((not-equals-transform "blue" "blue") {} nil)))
    (is (true? ((not-equals-transform "red" "blue") {} nil)))
    (is (true? ((not-equals-transform "abc" "Abc") {} nil)))
    (is (false? ((not-equals-transform "" "") {} nil)))
    (is (true? ((not-equals-transform " " "") {} nil))))

  (testing "with dates"
    (let [now (time/now)]
      (is (false? ((not-equals-transform (time/today) (time/today)) {} nil)))
      (is (false? ((not-equals-transform now now) {} nil)))
      (is (true? ((not-equals-transform now (time/plus now (time/millis 1))) {} nil)))
      (is (true? ((not-equals-transform (time/today) now) {} nil)))))

  (testing "with mixed types"
    (is (true? ((not-equals-transform 1 false) {} nil)))
    (is (false? ((not-equals-transform 1 true) {} nil)))
    (is (false? ((not-equals-transform 0 false) {} nil)))
    (is (true? ((not-equals-transform 0 true) {} nil)))

    (is (true? ((not-equals-transform false 1) {} nil)))
    (is (false? ((not-equals-transform true 1) {} nil)))
    (is (false? ((not-equals-transform false 0) {} nil)))
    (is (true? ((not-equals-transform true 0) {} nil)))

    (is (true? ((not-equals-transform false "red") {} nil)))
    (is (true? ((not-equals-transform "red" true) {} nil)))
    (is (true? ((not-equals-transform "20" 20) {} nil))))

  (testing "with nil"
    (is (false? ((not-equals-transform nil nil) {} nil)))
    (is (true? ((not-equals-transform true nil) {} nil)))
    (is (true? ((not-equals-transform nil true) {} nil)))))

(deftest less-than-transform-test
  (testing "with numbers"
    (is (false? ((less-than-transform 1 0) {} nil)))
    (is (false? ((less-than-transform 1 1) {} nil)))
    (is (true? ((less-than-transform -5 1) {} nil)))
    (is (true? ((less-than-transform 1.0 2.0) {} nil)))
    (is (false? ((less-than-transform 100 200/2) {} nil))))

  (testing "with strings"
    (is (false? ((less-than-transform "blue" "blue") {} nil)))
    (is (true? ((less-than-transform "abc" "bdc") {} nil)))
    (is (false? ((less-than-transform "banana" "apple") {} nil)))
    (is (true? ((less-than-transform "" "a") {} nil)))
    (is (false? ((less-than-transform "x" "") {} nil))))

  (testing "with booleans"
    (is (nil? ((less-than-transform true true) {} nil)))
    (is (nil? ((less-than-transform false false) {} nil)))
    (is (nil? ((less-than-transform false true) {} nil)))
    (is (nil? ((less-than-transform true false) {} nil))))

  (testing "with dates"
    (let [now (time/now)]
      (is (true? ((less-than-transform (time/today-at-midnight) now) {} nil)))
      (is (false? ((less-than-transform now (time/today-at-midnight)) {} nil)))
      (is (true? ((less-than-transform now (time/plus now (time/millis 1))) {} nil)))
      (is (false? ((less-than-transform (time/today-at-midnight) (time/today-at-midnight)) {} nil)))))

  (testing "with mixed types"
    (is (nil? ((less-than-transform 1 false) {} nil)))
    (is (nil? ((less-than-transform 0 true) {} nil)))
    (is (nil? ((less-than-transform false "red") {} nil)))
    (is (nil? ((less-than-transform "red" true) {} nil)))
    (is (nil? ((less-than-transform "20" 20) {} nil))))

  (testing "with nil"
    (is (nil? ((less-than-transform 1 nil) {} nil)))
    (is (nil? ((less-than-transform nil nil) {} nil)))
    (is (nil? ((less-than-transform nil 3) {} nil)))))

(deftest greater-than-transform-test
  (testing "with numbers"
    (is (true? ((greater-than-transform 1 0) {} nil)))
    (is (false? ((greater-than-transform 1 1) {} nil)))
    (is (false? ((greater-than-transform -5 1) {} nil)))
    (is (false? ((greater-than-transform 1.0 2.0) {} nil)))
    (is (false? ((greater-than-transform 100 200/2) {} nil))))

  (testing "with strings"
    (is (false? ((greater-than-transform "blue" "blue") {} nil)))
    (is (false? ((greater-than-transform "abc" "bdc") {} nil)))
    (is (true? ((greater-than-transform "banana" "apple") {} nil)))
    (is (false? ((greater-than-transform "" "a") {} nil)))
    (is (true? ((greater-than-transform "x" "") {} nil))))

  (testing "with booleans"
    (is (nil? ((greater-than-transform true true) {} nil)))
    (is (nil? ((greater-than-transform false false) {} nil)))
    (is (nil? ((greater-than-transform false true) {} nil)))
    (is (nil? ((greater-than-transform true false) {} nil))))

  (testing "with dates"
    (let [now (time/now)]
      (is (false? ((greater-than-transform (time/today-at-midnight) now) {} nil)))
      (is (true? ((greater-than-transform now (time/today-at-midnight)) {} nil)))
      (is (true? ((greater-than-transform now (time/minus now (time/millis 1))) {} nil)))
      (is (false? ((greater-than-transform (time/today-at-midnight) (time/today-at-midnight)) {} nil)))))

  (testing "with mixed types"
    (is (nil? ((greater-than-transform 1 false) {} nil)))
    (is (nil? ((greater-than-transform 0 true) {} nil)))
    (is (nil? ((greater-than-transform false "red") {} nil)))
    (is (nil? ((greater-than-transform "red" true) {} nil)))
    (is (nil? ((greater-than-transform "20" 20) {} nil))))

  (testing "with nil"
    (is (nil? ((greater-than-transform 1 nil) {} nil)))
    (is (nil? ((greater-than-transform nil nil) {} nil)))
    (is (nil? ((greater-than-transform nil 3) {} nil)))))

(deftest less-or-equals-transform-test
  (testing "with numbers"
    (is (false? ((less-or-equals-transform 1 0) {} nil)))
    (is (true? ((less-or-equals-transform 1 1) {} nil)))
    (is (true? ((less-or-equals-transform -5 1) {} nil)))
    (is (true? ((less-or-equals-transform 1.0 2.0) {} nil)))
    (is (true? ((less-or-equals-transform 100 200/2) {} nil))))

  (testing "with strings"
    (is (true? ((less-or-equals-transform "blue" "blue") {} nil)))
    (is (true? ((less-or-equals-transform "abc" "bdc") {} nil)))
    (is (false? ((less-or-equals-transform "banana" "apple") {} nil)))
    (is (true? ((less-or-equals-transform "" "a") {} nil)))
    (is (false? ((less-or-equals-transform "x" "") {} nil))))

  (testing "with booleans"
    (is (nil? ((less-or-equals-transform true true) {} nil)))
    (is (nil? ((less-or-equals-transform false false) {} nil)))
    (is (nil? ((less-or-equals-transform false true) {} nil)))
    (is (nil? ((less-or-equals-transform true false) {} nil))))

  (testing "with dates"
    (let [now (time/now)]
      (is (true? ((less-or-equals-transform (time/today-at-midnight) now) {} nil)))
      (is (false? ((less-or-equals-transform now (time/today-at-midnight)) {} nil)))
      (is (true? ((less-or-equals-transform now (time/plus now (time/millis 1))) {} nil)))
      (is (true? ((less-or-equals-transform (time/today-at-midnight) (time/today-at-midnight)) {} nil)))))

  (testing "with mixed types"
    (is (nil? ((less-or-equals-transform 1 false) {} nil)))
    (is (nil? ((less-or-equals-transform 0 true) {} nil)))
    (is (nil? ((less-or-equals-transform false "red") {} nil)))
    (is (nil? ((less-or-equals-transform "red" true) {} nil)))
    (is (nil? ((less-or-equals-transform "20" 20) {} nil))))

  (testing "with nil"
    (is (nil? ((less-or-equals-transform 1 nil) {} nil)))
    (is (nil? ((less-or-equals-transform nil nil) {} nil)))
    (is (nil? ((less-or-equals-transform nil 3) {} nil)))))

(deftest greater-or-equals-transform-test
  (testing "with numbers"
    (is (true? ((greater-or-equals-transform 1 0) {} nil)))
    (is (true? ((greater-or-equals-transform 1 1) {} nil)))
    (is (false? ((greater-or-equals-transform -5 1) {} nil)))
    (is (false? ((greater-or-equals-transform 1.0 2.0) {} nil)))
    (is (true? ((greater-or-equals-transform 100 200/2) {} nil))))

  (testing "with strings"
    (is (true? ((greater-or-equals-transform "blue" "blue") {} nil)))
    (is (false? ((greater-or-equals-transform "abc" "bdc") {} nil)))
    (is (true? ((greater-or-equals-transform "banana" "apple") {} nil)))
    (is (false? ((greater-or-equals-transform "" "a") {} nil)))
    (is (true? ((greater-or-equals-transform "x" "") {} nil))))

  (testing "with booleans"
    (is (nil? ((greater-or-equals-transform true true) {} nil)))
    (is (nil? ((greater-or-equals-transform false false) {} nil)))
    (is (nil? ((greater-or-equals-transform false true) {} nil)))
    (is (nil? ((greater-or-equals-transform true false) {} nil))))

  (testing "with dates"
    (let [now (time/now)]
      (is (false? ((greater-or-equals-transform (time/today-at-midnight) now) {} nil)))
      (is (true? ((greater-or-equals-transform now (time/today-at-midnight)) {} nil)))
      (is (true? ((greater-or-equals-transform now (time/minus now (time/millis 1))) {} nil)))
      (is (true? ((greater-or-equals-transform (time/today-at-midnight) (time/today-at-midnight)) {} nil)))))

  (testing "with mixed types"
    (is (nil? ((greater-or-equals-transform 1 false) {} nil)))
    (is (nil? ((greater-or-equals-transform 0 true) {} nil)))
    (is (nil? ((greater-or-equals-transform false "red") {} nil)))
    (is (nil? ((greater-or-equals-transform "red" true) {} nil)))
    (is (nil? ((greater-or-equals-transform "20" 20) {} nil))))

  (testing "with nil"
    (is (nil? ((greater-or-equals-transform 1 nil) {} nil)))
    (is (nil? ((greater-or-equals-transform nil nil) {} nil)))
    (is (nil? ((greater-or-equals-transform nil 3) {} nil)))))
