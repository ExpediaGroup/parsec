(ns parsec.statements.rename-statement-test
  (:require [clojure.test :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [rename-statement]])
  (:use [clojure.set :only (rename-keys)]))

(deftest rename-statement-parser-test
  (testing-parser!
    "with incomplete rename" :rename-statement
    "RENAME x =")

  (testing-parser
    "with one rename" :rename-statement
    "RENAME x = y" [:rename-statement {:y :x}])

  (testing-parser
    "with two renames" :rename-statement
    "RENAME y = x, z = y" [:rename-statement {:x :y, :y :z}])

  (testing-parser
    "with two renames and where" :rename-statement
    "RENAME y = x, z = y WHERE x >= 10" [:rename-statement
                                         {:x :y, :y :z}
                                         [:expression [:greater-or-equals-expression :x 10]]]))


(deftest rename-statement-test
  (testing-statement
    "with one rename"
    (rename-statement {:col1 :x})
    test-dataset1
    (map #(rename-keys % {:col1 :x}) test-dataset1))

  (testing-statement
    "with one rename of test-dataset2"
    (rename-statement {:col1 :x})
    test-dataset2
    (map #(rename-keys % {:col1 :x}) test-dataset2))

  (testing-statement
    "with one rename of non-existing column"
    (rename-statement {:bogus-column :x})
    test-dataset1
    test-dataset1)

  (testing-statement
    "with two renames"
    (rename-statement {:col1 :x, :col2 :y})
    test-dataset1
    (map #(rename-keys % {:col1 :x, :col2 :y}) test-dataset1))

  (testing-statement
    "with chained renames -- not supported"
    (rename-statement {:col1 :x, :x :y})
    test-dataset1
    (map #(rename-keys % {:col1 :x}) test-dataset1))

  (testing-statement
    "with one rename and where"
    (rename-statement {:col1 :x} (fn [row dataset] (if (>= (:col1 row) 3) true false)))
    test-dataset1
    '({:col1 1, :col2 2, :col3 3, :col4 4}
       {:col1 2, :col2 2, :col3 3, :col4 3}
       {:x 3, :col2 2, :col3 5, :col4 2}
       {:x 4, :col2 5, :col3 5, :col4 1}
       {:x 5, :col2 5, :col3 3, :col4 0}))

  (testing-statement
    "with two renames and where"
    (rename-statement {:col1 :x, :col4 :y} (fn [row dataset] (if (>= (:col1 row) 3) true false)))
    test-dataset1
    '({:col1 1, :col2 2, :col3 3, :col4 4}
       {:col1 2, :col2 2, :col3 3, :col4 3}
       {:x 3, :col2 2, :col3 5, :y 2}
       {:x 4, :col2 5, :col3 5, :y 1}
       {:x 5, :col2 5, :col3 3, :y 0})))
