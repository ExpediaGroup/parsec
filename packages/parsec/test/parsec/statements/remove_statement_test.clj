(ns parsec.statements.remove-statement-test
  (:require [clojure.test :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]))

(deftest remove-statement-parser-test
  (testing-parser!
    "with no argument" :remove-statement
    "remove")

  (testing-parser
    "with an identifier" :remove-statement
    "remove a" [:filter-statement [:expression [:not-operation [:expression :a]]]])

  (testing-parser
    "with a boolean expression" :remove-statement
    "remove x==y" [:filter-statement [:expression [:not-operation [:expression [:equals-expression :x :y]]]]]))
