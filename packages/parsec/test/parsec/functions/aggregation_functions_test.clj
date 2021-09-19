(ns parsec.functions.aggregation-functions-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.functions :refer :all]
            [parsec.functions.aggregationfunctions :refer :all]))

(defn get-x [row _] (:x row))

(deftest count-test
  (testing "with number"
    (is (zero? ((count-transform 1 always-predicate) {} {:current-dataset '()})))
    (is (= 1 ((count-transform 1 always-predicate) {} {:current-dataset '({})})))
    (is (= 4 ((count-transform 1 always-predicate) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})}))))

  (testing "with identifier"
    (is (= 4 ((count-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:x 2} {:x 2} {:x 3})})))
    (is (= 3 ((count-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:y 2} {:x 4} {:x 6})})))
    (is (zero? ((count-transform get-x always-predicate) {} {:current-dataset '({:y 1} {:z 2} {:q 4} {:r 6})})))
    (is (= 2 ((count-transform get-x always-predicate) {} {:current-dataset '({:x nil} {:x 1} {:x nil} {:x 6})}))))

  (testing "with multiple types"
    (is (= 4 ((count-transform get-x always-predicate) {} {:current-dataset '({:x "string"} {:x 3.243} {:x nil} {:x 0.5} {:x true})}))))

  (testing "with predicate"
    (is (= 3 ((count-transform get-x (fn [row _] (<= 2 (:x row)))) {} {:current-dataset '({:x 1} {:x 2} {:x 2} {:x 3})})))
    (is (= 2 ((count-transform get-x (fn [row _] (number? (:x row)))) {} {:current-dataset '({:x "string"} {:x 3.243} {:x nil} {:x 0.5} {:x true})}))))

  (testing "with no rows"
    (is (zero? ((count-transform get-x always-predicate) {} {:current-dataset '()})))
    (is (zero? ((count-transform get-x (fn [_ _] false)) {} {:current-dataset '({:x -1} {:x 7.2} {:x 1.2} {:x -8.4})})))))

(deftest distinctcount-test
  (testing "with number"
    (is (zero? ((distinctcount-transform 1 always-predicate) {} {:current-dataset '()})))
    (is (= 1 ((distinctcount-transform 1 always-predicate) {} {:current-dataset '({})})))
    (is (= 1 ((distinctcount-transform 1 always-predicate) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})}))))

  (testing "with identifier"
    (is (= 3 ((distinctcount-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:x 2} {:x 2} {:x 3})})))
    (is (= 3 ((distinctcount-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:y 2} {:x 4} {:x 6})})))
    (is (zero? ((distinctcount-transform get-x always-predicate) {} {:current-dataset '({:y 1} {:z 2} {:q 4} {:r 6})})))
    (is (= 2 ((distinctcount-transform get-x always-predicate) {} {:current-dataset '({:x nil} {:x 1} {:x nil} {:x 6})})))
    (is (= 2 ((distinctcount-transform get-x always-predicate) {} {:current-dataset '({:x 6} {:x nil} {:x 1} {:x nil} {:x 6})}))))

  (testing "with predicate"
    (is (= 2 ((distinctcount-transform get-x (fn [row _] (< 2 (:x row)))) {} {:current-dataset '({:x 1} {:x 3} {:x 4} {:x 4} {:x 3})}))))

  (testing "with multiple types"
    (is (= 4 ((distinctcount-transform get-x always-predicate) {} {:current-dataset '({:x "string"} {:x 3.243} {:x nil} {:x 0.5} {:x true})}))))

  (testing "with no rows"
    (is (zero? ((distinctcount-transform get-x always-predicate) {} {:current-dataset '()})))
    (is (zero? ((distinctcount-transform get-x (fn [_ _] false)) {} {:current-dataset '({:x -1} {:x 7.2} {:x 1.2} {:x -8.4})})))))

(deftest max-test
  (testing "with number"
    (is (= 4 ((max-transform 4 always-predicate) {} {:current-dataset '({})}))))

  (testing "with happy path"
    (is (zero? ((max-transform get-x always-predicate) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})})))
    (is (= 7 ((max-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:x 2} {:x 4} {:x 7})})))
    (is (= 6 ((max-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:y 100} {:x 4} {:x 6})})))
    (is (= -1 ((max-transform get-x always-predicate) {} {:current-dataset '({:x -1} {:x -2} {:x -4} {:x -6})}))))

  (testing "with predicate"
    (is (= 2 ((max-transform get-x (fn [row _] (< (:x row) 3))) {} {:current-dataset '({:x 1} {:x 2} {:x 3} {:x 4})}))))

  (testing "with nil"
    (is (= 10.4252 ((max-transform get-x always-predicate) {} {:current-dataset '({:x 0.134} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})})))
    (is (= 3.243 ((max-transform get-x always-predicate) {} {:current-dataset '({:x nil} {:x 0.134} {:x nil} {:x 3.243} {:x nil} {:x 0.5} {:x -10.4252})}))))

  (testing "with no rows"
    (is (nil? ((max-transform get-x always-predicate) {} {:current-dataset '()})))
    (is (nil? ((max-transform get-x (fn [_ _] false)) {} {:current-dataset '({:x -1} {:x 7.2} {:x 1.2} {:x -8.4})}))))

  (testing "with exception"
    (is (thrown? Exception ((max-transform get-x always-predicate) {} {:current-dataset '({:x "string"} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})})))))

(deftest min-test
  (testing "with number"
    (is (= 4 ((min-transform 4 always-predicate) {} {:current-dataset '({})}))))

  (testing "with happy path"
    (is (zero? ((min-transform get-x always-predicate) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})})))
    (is (= 2 ((min-transform get-x always-predicate) {} {:current-dataset '({:x 5} {:x 2} {:x 4} {:x 6})})))
    (is (= 1 ((min-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:y 0} {:x 4} {:x 6})})))
    (is (= -6 ((min-transform get-x always-predicate) {} {:current-dataset '({:x -1} {:x -2} {:x -4} {:x -6})}))))

  (testing "with predicate"
    (is (= 4 ((min-transform get-x (fn [row _] (> (:x row) 2))) {} {:current-dataset '({:x 1} {:x 7} {:x 5} {:x 4})}))))

  (testing "with nil"
    (is (= 0.134 ((min-transform get-x always-predicate) {} {:current-dataset '({:x 0.134} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})})))
    (is (= -10.4252 ((min-transform get-x always-predicate) {} {:current-dataset '({:x nil} {:x 0.134} {:x nil} {:x 3.243} {:x nil} {:x 0.5} {:x -10.4252})}))))

  (testing "with no rows"
    (is (nil? ((min-transform get-x always-predicate) {} {:current-dataset '()})))
    (is (nil? ((min-transform get-x (fn [_ _] false)) {} {:current-dataset '({:x -1} {:x 7.2} {:x 1.2} {:x -8.4})}))))

  (testing "with exception"
    (is (thrown? Exception ((min-transform get-x always-predicate) {} {:current-dataset '({:x "string"} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})})))))

(deftest mean-test
  (testing "with number"
    (is (= 4.0 ((mean-transform 4 always-predicate) {} {:current-dataset '({})}))))

  (testing "with happy path"
    (is (= 0.0 ((mean-transform get-x always-predicate) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})})))
    (is (= 3.25 ((mean-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:x 2} {:x 4} {:x 6})})))
    (is (= 3.25 ((mean-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:x 2} {:x 4} {:x 6})})))
    (is (= 2.75 ((mean-transform get-x always-predicate) {} {:current-dataset '({:x -1} {:x 2} {:x 4} {:x 6})}))))

  (testing "with nil"
    (is (= 3.57555 ((mean-transform get-x always-predicate) {} {:current-dataset '({:x 0.134} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})})))
    (is (= 3.57555 ((mean-transform get-x always-predicate) {} {:current-dataset '({:x nil} {:x 0.134} {:x nil} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})}))))

  (testing "with no rows"
    (is (nil? ((mean-transform get-x always-predicate) {} {:current-dataset '()})))
    (is (nil? ((mean-transform get-x (fn [_ _] false)) {} {:current-dataset '({:x -1} {:x 7.2} {:x 1.2} {:x -8.4})}))))

  (testing "with exception"
    (is (thrown? Exception ((mean-transform get-x always-predicate) {} {:current-dataset '({:x "string"} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})})))))

(deftest geometricmean-test
  (testing "with number"
    (is (= 4.0 ((geometricmean-transform 4 always-predicate) {} {:current-dataset '({})}))))

  (testing "with happy path"
    (is (= 0.0 ((geometricmean-transform get-x always-predicate) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})})))
    (is (= 2.6321480259049848 ((geometricmean-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:x 2} {:x 4} {:x 6})})))
    (is (= 9.000000000000002 ((geometricmean-transform get-x always-predicate) {} {:current-dataset '({:x 9} {:x 9} {:x 9})})))
    (is (= 4.0 ((geometricmean-transform get-x always-predicate) {} {:current-dataset '({:x 16} {:x 2} {:x 4} {:x 2})}))))

  (testing "with nil"
    (is (= 5.039684199579492 ((geometricmean-transform get-x always-predicate) {} {:current-dataset '({:x 16} {:x 2} {:x 4} {:x nil})}))))

  (testing "with no rows"
    (is (nil? ((geometricmean-transform get-x always-predicate) {} {:current-dataset '()})))
    (is (nil? ((geometricmean-transform get-x (fn [_ _] false)) {} {:current-dataset '({:x -1} {:x 7.2} {:x 1.2} {:x -8.4})}))))

  (testing "with exception"
    (is (thrown? Exception ((geometricmean-transform get-x always-predicate) {} {:current-dataset '({:x "string"} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})})))))

(deftest stddev-pop-test
  (testing "with number"
    (is (= 0.0 ((stddev-pop-transform 4 always-predicate) {} {:current-dataset '({})}))))

  (testing "with happy path"
    (is (= 0.0 ((stddev-pop-transform get-x always-predicate) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})})))
    (is (= 1.920286436967152 ((stddev-pop-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:x 2} {:x 4} {:x 6})})))
    (is (= 3.766629793329841 ((stddev-pop-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:x -2} {:x -4} {:x 6})})))
    (is (= 4.918015351745051 ((stddev-pop-transform get-x always-predicate) {} {:current-dataset '({:x -1} {:x 7.2} {:x 1.4} {:x -6.5})}))))

  (testing "with nil"
    (is (= 4.133145558469965 ((stddev-pop-transform get-x always-predicate) {} {:current-dataset '({:x 0.134} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})})))
    (is (= 4.133145558469965 ((stddev-pop-transform get-x always-predicate) {} {:current-dataset '({:x nil} {:x 0.134} {:x nil} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})}))))

  (testing "with no rows"
    (is (nil? ((stddev-pop-transform get-x always-predicate) {} {:current-dataset '()})))
    (is (nil? ((stddev-pop-transform get-x (fn [_ _] false)) {} {:current-dataset '({:x -1} {:x 7.2} {:x 1.2} {:x -8.4})}))))

  (testing "with exception"
    (is (thrown? Exception ((stddev-pop-transform get-x always-predicate) {} {:current-dataset '({:x "string"} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})})))))

(deftest stddev-samp-test
  (testing "with number"
    (is (Double/isNaN ((stddev-samp-transform 4 always-predicate) {} {:current-dataset '({})}))))

  (testing "with happy path"
    (is (= 0.0 ((stddev-samp-transform get-x always-predicate) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})})))
    (is (= 2.217355782608345 ((stddev-samp-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:x 2} {:x 4} {:x 6})})))
    (is (= 4.349329450233296 ((stddev-samp-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:x -2} {:x -4} {:x 6})})))
    (is (= 5.678834974417436 ((stddev-samp-transform get-x always-predicate) {} {:current-dataset '({:x -1} {:x 7.2} {:x 1.4} {:x -6.5})}))))

  (testing "with nil"
    (is (= 4.772545401565081 ((stddev-samp-transform get-x always-predicate) {} {:current-dataset '({:x 0.134} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})})))
    (is (= 4.772545401565081 ((stddev-samp-transform get-x always-predicate) {} {:current-dataset '({:x nil} {:x 0.134} {:x nil} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})}))))

  (testing "with no rows"
    (is (nil? ((stddev-samp-transform get-x always-predicate) {} {:current-dataset '()})))
    (is (nil? ((stddev-samp-transform get-x (fn [_ _] false)) {} {:current-dataset '({:x -1} {:x 7.2} {:x 1.2} {:x -8.4})}))))

  (testing "with exception"
    (is (thrown? Exception ((stddev-samp-transform get-x always-predicate) {} {:current-dataset '({:x "string"} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})})))))

(deftest sum-test
  (testing "with number"
    (is (= 4 ((sum-transform 4 always-predicate) {} {:current-dataset '({})})))
    (is (= 8 ((sum-transform 4 always-predicate) {} {:current-dataset '({:y 1} {:x 1})}))))

  (testing "with happy path"
    (is (zero? ((sum-transform get-x always-predicate) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})})))
    (is (= 10 ((sum-transform get-x always-predicate) {} {:current-dataset '({:x 10})})))
    (is (= 13 ((sum-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:x 2} {:x 4} {:x 6})})))
    (is (= 1 ((sum-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:x -2} {:x -4} {:x 6})})))
    (is (= -1.0 ((sum-transform get-x always-predicate) {} {:current-dataset '({:x -1} {:x 7.2} {:x 1.2} {:x -8.4})}))))

  (testing "with predicate"
    (is (= 7 ((sum-transform get-x (fn [row _] (> (:x row) 2))) {} {:current-dataset '({:x 1} {:x 2} {:x 3} {:x 4})}))))

  (testing "with no rows"
    (is (zero? ((sum-transform get-x always-predicate) {} {:current-dataset '()})))
    (is (zero? ((sum-transform get-x (fn [_ _] false)) {} {:current-dataset '({:x -1} {:x 7.2} {:x 1.2} {:x -8.4})}))))

  (testing "with nil"
    (is (= 14.3022 ((sum-transform get-x always-predicate) {} {:current-dataset '({:x 0.134} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})})))
    (is (= 14.3022 ((sum-transform get-x always-predicate) {} {:current-dataset '({:x nil} {:x 0.134} {:x nil} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})}))))

  (testing "with exception"
    (is (thrown? Exception ((sum-transform get-x always-predicate) {} {:current-dataset '({:x "string"} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})})))))

(deftest first-test
  (testing "with number"
    (is (nil? ((first-transform 1 always-predicate) {} {:current-dataset '()})))
    (is (= 1 ((first-transform 1 always-predicate) {} {:current-dataset '({})})))
    (is (= 1 ((first-transform 1 always-predicate) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})}))))

  (testing "with identifier"
    (is (= 1 ((first-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:x 2} {:x 2} {:x 3})})))
    (is (= 2 ((first-transform get-x always-predicate) {} {:current-dataset '({:y 1} {:x 2} {:x 4} {:x 6})})))
    (is (nil? ((first-transform get-x always-predicate) {} {:current-dataset '({:y 1} {:z 2} {:q 4} {:r 6})})))
    (is (= 5 ((first-transform get-x always-predicate) {} {:current-dataset '({:x nil} {:x nil} {:x 5} {:x 6})}))))

  (testing "with multiple types"
    (is (= "string" ((first-transform get-x always-predicate) {} {:current-dataset '({:x "string"} {:x 3.243} {:x nil} {:x 0.5} {:x true})})))
    (is (= true ((first-transform get-x always-predicate) {} {:current-dataset '({:x true} {:x 3.243} {:x nil} {:x 0.5} {:x "string"})}))))

  (testing "with predicate"
    (is (= 4 ((first-transform get-x (fn [row _] (> (:x row) 2))) {} {:current-dataset '({:x 1} {:x 2} {:x 4} {:x 3})})))
    (is (= 3.243 ((first-transform get-x (fn [row _] (number? (:x row)))) {} {:current-dataset '({:x "string"} {:x 3.243} {:x nil} {:x 0.5} {:x true})}))))

  (testing "with no rows"
    (is (nil? ((first-transform get-x always-predicate) {} {:current-dataset '()})))
    (is (nil? ((first-transform get-x (fn [_ _] false)) {} {:current-dataset '({:x -1} {:x 7.2} {:x 1.2} {:x -8.4})})))))

(deftest last-test
  (testing "with number"
    (is (nil? ((last-transform 1 always-predicate) {} {:current-dataset '()})))
    (is (= 1 ((last-transform 1 always-predicate) {} {:current-dataset '({})})))
    (is (= 1 ((last-transform 1 always-predicate) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})}))))

  (testing "with identifier"
    (is (= 3 ((last-transform get-x always-predicate) {} {:current-dataset '({:x 1} {:x 2} {:x 2} {:x 3})})))
    (is (= 6 ((last-transform get-x always-predicate) {} {:current-dataset '({:y 1} {:x 2} {:x 4} {:x 6})})))
    (is (nil? ((last-transform get-x always-predicate) {} {:current-dataset '({:y 1} {:z 2} {:q 4} {:r 6})})))
    (is (= 6 ((last-transform get-x always-predicate) {} {:current-dataset '({:x nil} {:x 5} {:x 6} {:x nil})}))))

  (testing "with multiple types"
    (is (= true ((last-transform get-x always-predicate) {} {:current-dataset '({:x "string"} {:x 3.243} {:x nil} {:x 0.5} {:x true})})))
    (is (= "string" ((last-transform get-x always-predicate) {} {:current-dataset '({:x true} {:x 3.243} {:x nil} {:x 0.5} {:x "string"})}))))

  (testing "with predicate"
    (is (= 3 ((last-transform get-x (fn [row _] (> (:x row) 2))) {} {:current-dataset '({:x 1} {:x 2} {:x 4} {:x 3})})))
    (is (= 0.5 ((last-transform get-x (fn [row _] (number? (:x row)))) {} {:current-dataset '({:x "string"} {:x 3.243} {:x nil} {:x 0.5} {:x true})}))))

  (testing "with no rows"
    (is (nil? ((last-transform get-x always-predicate) {} {:current-dataset '()})))
    (is (nil? ((last-transform get-x (fn [_ _] false)) {} {:current-dataset '({:x -1} {:x 7.2} {:x 1.2} {:x -8.4})})))))

(deftest percentile-test
  (testing "with number"
    (is (= 4.0 ((percentile-transform 4 100 always-predicate) {} {:current-dataset '({})})))
    (is (= 5.0 ((percentile-transform 5 100 always-predicate) {} {:current-dataset '({:y 1} {:x 1})}))))

  (testing "with zeros"
    (is (= 0.0 ((percentile-transform get-x 0 always-predicate) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})})))
    (is (= 0.0 ((percentile-transform get-x 50 always-predicate) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})})))
    (is (= 0.0 ((percentile-transform get-x 90 always-predicate) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})})))
    (is (= 0.0 ((percentile-transform get-x 100 always-predicate) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})}))))

  (testing "happy path"
    (is (= 1.0 ((percentile-transform get-x 0 always-predicate) {} {:current-dataset '({:x 1} {:x 2} {:x 3} {:x 4})})))
    (is (= 2.5 ((percentile-transform get-x 50 always-predicate) {} {:current-dataset '({:x 1} {:x 2} {:x 3} {:x 4})})))
    (is (= 3.25 ((percentile-transform get-x 75 always-predicate) {} {:current-dataset '({:x 1} {:x 2} {:x 3} {:x 4})})))
    (is (= 4.0 ((percentile-transform get-x 100 always-predicate) {} {:current-dataset '({:x 1} {:x 2} {:x 3} {:x 4})}))))

  (testing "with predicate"
    (is (= 3.5 ((percentile-transform get-x 50 (fn [row _] (< 2 (:x row)))) {} {:current-dataset '({:x 1} {:x 2} {:x 3} {:x 4})}))))

  (testing "with nil"
    (is (= 3.7 ((percentile-transform get-x 90 always-predicate) {} {:current-dataset '({:x 1} {:x nil} {:x 2} {:x 3} {:x 4})})))
    (is (= nil ((percentile-transform get-x 99 always-predicate) {} {:current-dataset '({:x nil} {:x nil} {:x nil} {:x nil} {:x nil})}))))

  (testing "with no rows"
    (is (nil? ((percentile-transform get-x 50 always-predicate) {} {:current-dataset '()})))
    (is (nil? ((percentile-transform get-x 50 (fn [_ _] false)) {} {:current-dataset '({:x -1} {:x 7.2} {:x 1.2} {:x -8.4})}))))

  (testing "with exception"
    (is (thrown? Exception ((percentile-transform get-x get-x always-predicate) {} {:current-dataset '({:x 1} {:x 2} {:x 3} {:x 4} {:x 5})})))
    (is (thrown? Exception ((percentile-transform get-x 90 always-predicate) {} {:current-dataset '({:x "string"} {:x 3.243} {:x nil} {:x 0.5} {:x 10.4252})})))))

(deftest pluck-test
  (testing "with number"
    (is (nil? ((pluck-transform 1 always-predicate) {} {:current-dataset '()})))
    (is (= [1] ((pluck-transform 1) {} {:current-dataset '({})})))
    (is (= [1] ((pluck-transform 1 always-predicate) {} {:current-dataset '({})})))
    (is (= [1 1 1 1] ((pluck-transform 1) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})}))))

  (testing "with identifier"
    (is (= [1 2 2 3] ((pluck-transform get-x) {} {:current-dataset '({:x 1} {:x 2} {:x 2} {:x 3})})))
    (is (= [2 4 6] ((pluck-transform get-x) {} {:current-dataset '({:y 1} {:x 2} {:x 4} {:x 6})})))
    (is (= nil ((pluck-transform get-x) {} {:current-dataset '({:y 1} {:z 2} {:q 4} {:r 6})})))
    (is (= [5 6] ((pluck-transform get-x) {} {:current-dataset '({:x nil} {:x 5} {:x 6} {:x nil})}))))

  (testing "with multiple types"
    (is (= ["string" 3.243 0.5 true] ((pluck-transform get-x) {} {:current-dataset '({:x "string"} {:x 3.243} {:x nil} {:x 0.5} {:x true})})))
    (is (= [true 3.243 0.5 "string"] ((pluck-transform get-x) {} {:current-dataset '({:x true} {:x 3.243} {:x nil} {:x 0.5} {:x "string"})}))))

  (testing "with predicate"
    (is (= [4 3] ((pluck-transform get-x (fn [row _] (> (:x row) 2))) {} {:current-dataset '({:x 1} {:x 2} {:x 4} {:x 3})})))
    (is (= [3.243 0.5] ((pluck-transform get-x (fn [row _] (number? (:x row)))) {} {:current-dataset '({:x "string"} {:x 3.243} {:x nil} {:x 0.5} {:x true})})))))

(deftest every-test
  (testing "with constant"
    (is (nil? ((every-transform 1 always-predicate) {} {:current-dataset '()})))
    (is (false? ((every-transform 1 always-predicate) {} {:current-dataset '({})})))
    (is (false? ((every-transform false always-predicate) {} {:current-dataset '({})})))
    (is (true? ((every-transform true always-predicate) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})}))))

  (testing "with identifier"
    (is (true? ((every-transform get-x always-predicate) {} {:current-dataset '({:x true} {:x true} {:x true} {:x true})})))
    (is (false? ((every-transform get-x always-predicate) {} {:current-dataset '({:x true} {:x false} {:x true} {:x true})})))
    (is (false? ((every-transform get-x always-predicate) {} {:current-dataset '({:x true} {:x true} {:x true} {:x false})})))
    (is (false? ((every-transform get-x always-predicate) {} {:current-dataset '({:x false} {:x true} {:x true} {:x true})}))))

  (testing "with predicate"
    (is (true? ((every-transform get-x (fn [row _] (> (:y row) 2))) {} {:current-dataset '({:x true :y 1} {:x false :y 2} {:x true :y 3} {:x true :y 4})})))
    (is (false? ((every-transform get-x (fn [row _] (> (:y row) 2))) {} {:current-dataset '({:x true :y 1} {:x true :y 2} {:x false :y 3} {:x true :y 4})}))))

  (testing "with no rows"
    (is (nil? ((every-transform get-x always-predicate) {} {:current-dataset '()})))
    (is (nil? ((every-transform get-x (fn [_ _] false)) {} {:current-dataset '({:x -1} {:x 7.2} {:x 1.2} {:x -8.4})})))))

(deftest some-test
  (testing "with constant"
    (is (nil? ((some-transform 1 always-predicate) {} {:current-dataset '()})))
    (is (false? ((some-transform 1 always-predicate) {} {:current-dataset '({})})))
    (is (false? ((some-transform false always-predicate) {} {:current-dataset '({})})))
    (is (true? ((some-transform true always-predicate) {} {:current-dataset '({:x 0} {:x 0} {:x 0} {:x 0})}))))

  (testing "with identifier"
    (is (true? ((some-transform get-x always-predicate) {} {:current-dataset '({:x true} {:x true} {:x true} {:x true})})))
    (is (true? ((some-transform get-x always-predicate) {} {:current-dataset '({:x true} {:x false} {:x true} {:x true})})))
    (is (true? ((some-transform get-x always-predicate) {} {:current-dataset '({:x true} {:x false} {:x true} {:x false})})))
    (is (false? ((some-transform get-x always-predicate) {} {:current-dataset '({:x false} {:x false} {:x false} {:x false})}))))

  (testing "with predicate"
    (is (true? ((some-transform get-x (fn [row _] (> (:y row) 2))) {} {:current-dataset '({:x true :y 1} {:x false :y 2} {:x false :y 3} {:x true :y 4})})))
    (is (false? ((some-transform get-x (fn [row _] (> (:y row) 2))) {} {:current-dataset '({:x true :y 1} {:x true :y 2} {:x false :y 3} {:x false :y 4})}))))

  (testing "with no rows"
    (is (nil? ((some-transform get-x always-predicate) {} {:current-dataset '()})))
    (is (nil? ((some-transform get-x (fn [_ _] false)) {} {:current-dataset '({:x -1} {:x 7.2} {:x 1.2} {:x -8.4})})))))
