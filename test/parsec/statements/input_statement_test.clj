(ns parsec.statements.input-statement-test
  (:require [clojure.test :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [input-statement mock-dataset]]))

(deftest input-statement-parser-test
  (testing-parser!
    "with no type" :input-statement "input")

  (testing-parser!
    "with assignment" :input-statement "input=mock")

  (testing-parser!
    "with quoted source type" :input-statement "input 'mock'")

  (testing-parser!
    "with quoted source type" :input-statement "input [mock]")

  (testing-parser!
    "with quoted source type" :input-statement "input \"mock\"")

  (testing-parser
    "with no options" :input-statement
    "input mock" [:input-statement :mock [:function :tolowercasemap]])

  (testing-parser
    "with one option" :input-statement
    "input mocklarge n='20'" [:input-statement :mocklarge [:function :tolowercasemap "n" [:expression "20"]]])

  (testing-parser
    "with two options" :input-statement
    "input mock n='20' m=40" [:input-statement :mock [:function :tolowercasemap "n" [:expression "20"] "m" [:expression 40]]])

  (testing-parser
    "with three options" :input-statement
    "input test x=\"1\" y=2 z=true" [:input-statement :test [:function :tolowercasemap "x" [:expression "1"] "y" [:expression 2] "z" [:expression true]]])

  (testing-parser
    "with three options and commas" :input-statement
    "input test x=\"1\",y=2,z=true" [:input-statement :test [:function :tolowercasemap "x" [:expression "1"] "y" [:expression 2] "z" [:expression true]]]))

(deftest input-statement-test
  (testing-statement
    "mock input"
    (input-statement :mock {})
    nil
    mock-dataset)

  (testing "mock input with no option"
    (let [result (:current-dataset ((input-statement :mock {}) nil))]
      (is (= 5 (count result)))))

  (testing "mock N input with 1"
    (let [result (:current-dataset ((input-statement :mock { :n 1 }) nil))]
      (is (= 1 (count result)))))

  (testing "mock N input with 20"
    (let [result (:current-dataset ((input-statement :mock { :n 20 }) nil))]
      (is (= 20 (count result)))))

  (testing "mock N input with 20"
    (let [result (:current-dataset ((input-statement :mock { :n "20"}) nil))]
      (is (= 20 (count result)))))

  (testing "invalid input"
    (is (thrown? Exception (input-statement "bogus-input" {})))))
