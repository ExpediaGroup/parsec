(ns parsec.statements.sort-statement-test
  (:require [clojure.test :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [sort-statement]]))

(deftest sort-statement-parser-test
  (testing-parser!
    "with no args" :sort-statement
    "sort")

  (testing-parser
    "without direction" :sort-statement
    "sort col1" [:sort-statement [:col1 :order-ascending]])

  (testing-parser
    "with ascending" :sort-statement
    "sort col1 asc" [:sort-statement [:col1 :order-ascending]])
  (testing-parser
    "with descending" :sort-statement
    "sort col1 desc" [:sort-statement [:col1 :order-descending]])

  (testing-parser
    "with multiple columns no direction" :sort-statement
    "sort col1, col2" [:sort-statement
                       [:col1 :order-ascending]
                       [:col2 :order-ascending]])
  (testing-parser
    "with multiple columns no direction" :sort-statement
    "sort col1 desc, col2 asc" [:sort-statement
                                [:col1 :order-descending]
                                [:col2 :order-ascending]]))

  (deftest sort-statement-test
    (testing-statement
      "with one column ascending"
      (sort-statement [:col4 :order-ascending])
      test-dataset1
      '({:col1 5, :col2 5, :col3 3, :col4 0}
         {:col1 4, :col2 5, :col3 5, :col4 1}
         {:col1 3, :col2 2, :col3 5, :col4 2}
         {:col1 2, :col2 2, :col3 3, :col4 3}
         {:col1 1, :col2 2, :col3 3, :col4 4}))

    (testing-statement
      "with one column descending"
      (sort-statement [:col1 :order-descending])
      test-dataset1
      '({:col1 5, :col2 5, :col3 3, :col4 0}
         {:col1 4, :col2 5, :col3 5, :col4 1}
         {:col1 3, :col2 2, :col3 5, :col4 2}
         {:col1 2, :col2 2, :col3 3, :col4 3}
         {:col1 1, :col2 2, :col3 3, :col4 4}))

    (testing-statement
      "with nil values"
      (sort-statement [:col1 :order-ascending])
      test-dataset2
      '({:col1 nil, :col2 2, :col3 5}
         {:col1 nil, :col2 nil, :col3 5, :col4 1}
         {:col1 1, :col2 2, :col4 4}
         {:col1 2, :col2 nil, :col4 3}
         {:col1 5, :col2 5, :col3 3}))

    (testing-statement
      "with nil values descending"
      (sort-statement [:col1 :order-descending])
      test-dataset2
      '({:col1 5, :col2 5, :col3 3}
         {:col1 2, :col2 nil, :col4 3}
         {:col1 1, :col2 2, :col4 4}
         {:col1 nil, :col2 2, :col3 5}
         {:col1 nil, :col2 nil, :col3 5, :col4 1}))

    (testing-statement
      "with missing values"
      (sort-statement [:col4 :order-ascending])
      test-dataset2
      '({:col1 nil, :col2 2, :col3 5}
         {:col1 5, :col2 5, :col3 3}
         {:col1 nil, :col2 nil, :col3 5, :col4 1}
         {:col1 2, :col2 nil, :col4 3}
         {:col1 1, :col2 2, :col4 4}))

    (testing-statement
      "with missing values descending"
      (sort-statement [:col4 :order-descending])
      test-dataset2
      '({:col1 1, :col2 2, :col4 4}
         {:col1 2, :col2 nil, :col4 3}
         {:col1 nil, :col2 nil, :col3 5, :col4 1}
         {:col1 nil, :col2 2, :col3 5}
         {:col1 5, :col2 5, :col3 3}))

    (testing-statement
      "with two columns, ascending"
      (sort-statement [:col3 :order-ascending] [:col2 :order-ascending])
      test-dataset1
      '({:col1 1, :col2 2, :col3 3, :col4 4}
         {:col1 2, :col2 2, :col3 3, :col4 3}
         {:col1 5, :col2 5, :col3 3, :col4 0}
         {:col1 3, :col2 2, :col3 5, :col4 2}
         {:col1 4, :col2 5, :col3 5, :col4 1}))

    (testing-statement
      "with two columns, descending"
      (sort-statement [:col3 :order-descending] [:col2 :order-descending])
      test-dataset1
      '({:col1 4, :col2 5, :col3 5, :col4 1}
         {:col1 3, :col2 2, :col3 5, :col4 2}
         {:col1 5, :col2 5, :col3 3, :col4 0}
         {:col1 1, :col2 2, :col3 3, :col4 4}
         {:col1 2, :col2 2, :col3 3, :col4 3}))

    (testing-statement
      "with two columns, ascending and descending"
      (sort-statement [:col3 :order-ascending] [:col2 :order-descending])
      test-dataset1
      '({:col1 5, :col2 5, :col3 3, :col4 0}
         {:col1 1, :col2 2, :col3 3, :col4 4}
         {:col1 2, :col2 2, :col3 3, :col4 3}
         {:col1 4, :col2 5, :col3 5, :col4 1}
         {:col1 3, :col2 2, :col3 5, :col4 2}))

    (testing-statement
      "with three columns, ascending and descending"
      (sort-statement [:col3 :order-ascending] [:col2 :order-descending] [:col1 :order-descending])
      test-dataset1
      '({:col1 5, :col2 5, :col3 3, :col4 0}
         {:col1 2, :col2 2, :col3 3, :col4 3}
         {:col1 1, :col2 2, :col3 3, :col4 4}
         {:col1 4, :col2 5, :col3 5, :col4 1}
         {:col1 3, :col2 2, :col3 5, :col4 2})))
