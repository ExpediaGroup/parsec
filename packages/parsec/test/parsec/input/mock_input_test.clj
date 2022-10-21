(ns parsec.input.mock-input-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.input.mock :refer [input-transform mock-dataset]]))

(deftest mock-input-transform-test
  (testing "mock input with no option"
    (let [result (:current-dataset ((input-transform {}) nil))]
      (is (= 5 (count result)))
      (is (= mock-dataset result))))

  (testing "mock N input with 1"
    (let [result (:current-dataset ((input-transform {:n 1}) nil))]
      (is (= 1 (count result)))))

  (testing "mock N input with 20"
    (let [result (:current-dataset ((input-transform {:n 20}) nil))]
      (is (= 20 (count result)))))

  (testing "mock N input with 20"
    (let [result (:current-dataset ((input-transform {:n "20"}) nil))]
      (is (= 20 (count result))))))
