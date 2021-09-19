(ns parsec.statements.sample-statement-test
  (:require [clojure.test :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [sample-statement]]))

(deftest sample-statement-parser-test
  (testing-parser!
    "with no argument"
    :sample-statement
    "sample")

  (testing-parser
    "with an argument"
    :sample-statement
    "sample 100"
    [:sample-statement [:expression 100]]))

(deftest sample-statement-test

  (testing "argument smaller than dataset"
    (let [stmt (sample-statement (fn [_ _] 2))
          context {:current-dataset test-dataset1}
          result (:current-dataset (stmt context))]
      (is (= 2 (count result)))
      ;; Ensure each row in the result was in the original dataset
      (is (every? true? (map (fn [row] (some #(= row %) test-dataset1)) result)))))

  (testing "argument larger than dataset"
    (let [stmt (sample-statement (fn [_ _] 20))
          context {:current-dataset test-dataset1}
          result (:current-dataset (stmt context))]
      (is (= 20 (count result)))
      ;; Ensure each row in the result was in the original dataset
      (is (every? true? (map (fn [row] (some #(= row %) test-dataset1)) result))))))
