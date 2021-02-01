(ns parsec.statements.reverse-statement-test
  (:require [clojure.test :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [reverse-statement]]))

(deftest reverse-statement-parser-test
  (testing-parser
    "with no argument" :reverse-statement
    "reverse" [:reverse-statement]))


(deftest reverse-statement-test
  (testing-statement
    "with test-dataset1"
    (reverse-statement)
    test-dataset1
    (reverse test-dataset1))

  (testing-statement
    "with test-dataset2"
    (reverse-statement)
    test-dataset2
    (reverse test-dataset2)))
