(ns parsec.functions.misc-functions-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.functions :refer :all]
            [parsec.functions.miscfunctions :refer :all]))

(defn get-x [row _] (:x row))

(deftest random-test
  (let [random (partial function-transform :random)]
    (testing "with nil"
      (is (nil? ((random 0 nil) {} nil)))
      (is (nil? ((random nil nil) {} nil))))
    (testing "with number"
      (let [a ((random 0 0) {} nil)]
        (is (= 0.0 a)))
      (let [a ((random 0 1) {} nil)]
        (is (>= a 0))
        (is (< a 1)))
      (let [a ((random 0 10) {} nil)]
        (is (>= a 0))
        (is (< a 10)))
      (let [a ((random 5 10) {} nil)]
        (is (>= a 5))
        (is (< a 10))))
    (testing "with string"
      (is (thrown? Exception ((random "") {} nil)))
      (is (thrown? Exception ((random "non-null") {} nil))))
    (testing "with boolean"
      (is (thrown? Exception ((random false) {} nil)))
      (is (thrown? Exception ((random true) {} nil))))))
