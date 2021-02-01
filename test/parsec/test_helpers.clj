(ns parsec.test-helpers
  (:require [clojure.test :refer :all]
            [parsec.core :refer :all]
            [parsec.parser :refer :all]
            [parsec.executor :as executor])

  (:use [clojure.pprint :only (pprint)]))


(defn testing-statement
  "Tests a statement against an input dataset and compares it to an expected output dataset.
  Basically abstracts away the context/current-dataset."
  [description compiled-statement input-dataset expected-dataset]
   (testing description
     (let [context {:current-dataset input-dataset}
           final-context (compiled-statement context)
           output-dataset (:current-dataset final-context)]
       (is (= expected-dataset output-dataset)))))

(defn testing-statement-context
  "Tests a statement against an input dataset and evaluates a function with the resulting context."
  ([description compiled-statement input-dataset context-tester]
   (testing description
     (let [context {:current-dataset input-dataset}
           final-context (compiled-statement context)]
       (is (true? (context-tester final-context)))))))

(defn testing-statement!
  "Tests a statement against an input dataset and expects an exception."
  [description compiled-statement input-dataset]
  (testing description
    (let [context {:current-dataset input-dataset}]
      (is (thrown? Exception (compiled-statement context))))))


(defn testing-parser
  "Tests the parser by comparing the parser output to an expected tree."
  [description element query expected]
  (testing description
    (let [tree (parse query element)]
      (is (= expected tree)))))

(defn testing-parser!
  "Tests the parser by parsing a query and expecting an exception."
  [description element query]
  (testing description
    (is (thrown? Exception (parse query element)))))

(defn testing-query-execution
  "Tests a query by parsing and evaluating with a given context. Compares the output to an expected output dataset."
  ([description query context expected-dataset]
   (testing description
     (let [parsed-tree (parse query)
           compiled-tree (executor/compile-query parsed-tree)
           final-context (executor/execute compiled-tree context)
           output-dataset (:current-dataset final-context)]
       (is (= expected-dataset output-dataset))))))

(defn testing-query-context
  "Tests a query by parsing and evaluating with a given context. Evaluates a function with the resulting context."
  ([description query context context-tester]
   (testing description
     (let [parsed-tree (parse query)
           compiled-tree (executor/compile-query parsed-tree)
           final-context (executor/execute compiled-tree context)]
       (is (true? (context-tester final-context)))))))
