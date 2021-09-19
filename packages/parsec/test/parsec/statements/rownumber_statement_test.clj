(ns parsec.statements.rownumber-statement-test
  (:require [clojure.test :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [rownumber-statement]]))

(deftest rownumber-statement-parser-test
  (testing-parser
    "with no argument" :rownumber-statement
    "rownumber" [:rownumber-statement])

  (testing-parser
    "with argument" :rownumber-statement
    "rownumber x" [:rownumber-statement :x])

  (testing-parser!
    "with non-identifier" :rownumber-statement
    "rownumber 10"))

(deftest rownumber-statement-test
  (testing-statement
    "with no argument"
    (rownumber-statement)
    test-dataset1
    (map-indexed (fn [index row] (assoc row :_index index)) test-dataset1))

  (testing-statement
    "with an argument"
    (rownumber-statement :x)
    test-dataset1
    (map-indexed (fn [index row] (assoc row :x index)) test-dataset1)))
