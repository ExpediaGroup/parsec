(ns parsec.helpers.maths-test
  (:require [clojure.test :refer :all]
            [parsec.helpers.maths :refer :all]
            [parsec.test-helpers :refer :all]))

(deftest geometricMean-test
  (testing "with nil"
    (is (nil? (geometricMean nil))))
  (testing "with list"
    (is (= 1.8171205928321397 (geometricMean '(1 2 3)))))
  (testing "with vector"
    (is (= 1.8171205928321397 (geometricMean [1 2 3])))))

(deftest mode-test
  (testing "with nil"
    (is (nil? (mode nil))))
  (testing "with list"
    (is (nil? (mode '()))))
  (testing "with list"
    (is (= 1 (mode '(1)))))
  (testing "with list"
    (is (= 1 (mode '(1 2 1 3)))))
  (testing "with vector"
    (is (= 2 (mode [1 2 2 3]))))
  (testing "with multiple modes"
    (is (= 4 (mode [3 2 4 1 2 4 2 3 5 4])))))

(deftest stddev-pop-test
  (testing "with nil"
    (is (nil? (stddev-pop nil))))
  (testing "with list"
    (is (= 0.816496580927726 (stddev-pop '(1 2 3)))))
  (testing "with vector"
    (is (= 0.816496580927726 (stddev-pop [1 2 3]))))
  (testing "with vector"
    (is (= 128.00156249046339 (stddev-pop [-1 0 1 100 330])))))

(deftest round-test
  (testing "with 0 precision"
    (is (= 0 (round 0 0)))
    (is (= 2 (round 2.1 0)))
    (is (= 3 (round 2.5 0)))
    (is (= 3 (round 2.9 0)))
    (is (= 2N (round 9/5 0))))
  (testing "with precision"
    (is (= 2 (round 2.129 0)))
    (is (= 2.5 (round 2.545 1)))
    (is (= 2.55 (round 2.549 2)))
    (is (= 2.549 (round 2.5493 3))))
  (testing "with more precision than available"
    (is (= 1.0 (round 1 2)))
    (is (= 3.14 (round 3.14 5))))
  (testing "with 1 precision"
    (is (= 3.1 (round 3.14 1))))
  (testing "with rounding up"
    (is (= 2.8 (round 2.78 1)))
    (is (= 2.78 (round 2.781 2))))
  (testing "with NaN/Infinity"
    (is (Double/isNaN (round Double/NaN 2)))
    (is (Double/isNaN (round 2 Double/NaN)))
    (is (= 2.45562 (round 2.45562 Double/POSITIVE_INFINITY)))
    (is (= 0.0 (round 2 Double/NEGATIVE_INFINITY)))
    (is (Double/isInfinite (round Double/POSITIVE_INFINITY 2)))
    (is (Double/isInfinite (round Double/NEGATIVE_INFINITY 2))))
  (testing "with negative precision"
    (is (= 0.0 (round 1 -1)))
    (is (= 0.0 (round 2.25 -2)))))
