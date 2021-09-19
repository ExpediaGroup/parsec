(ns parsec.core-test
  (:require [clojure.test :refer :all]
            [parsec.core :refer :all]
            [parsec.functions :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.test-helpers :refer :all]))

(deftest test-execute-query
  (testing "with one query"
    (let [final-context (execute-query "input mock")
         datastore (:datastore final-context)
         ds1 (:0 datastore)]
      (is (= test-dataset1 (:data ds1)))))
  (testing "with two queries"
    (let [final-context (execute-query "input mock; input mock n=11")
          datastore (:datastore final-context)
          ds1 (:0 datastore)
          ds2 (:1 datastore)]
      (and
        (is (= test-dataset1 (:data ds1)))
        (is (= 11 (count (:data ds2)))))))
  (testing "with two queries but one non-dataset query"
    (let [final-context (execute-query "input mock; set @x = 10")
          datastore (:datastore final-context)
          ds1 (:0 datastore)
          dataset-count (count datastore)]
      (and
        (is (= test-dataset1 (:data ds1)))
        (is (= 1 dataset-count)))))
  (testing "with two queries but and two non-dataset query"
    (let [final-context (execute-query "input mock | output ignore; set @x = 10")
          datastore (:datastore final-context)
          variables (:variables final-context)]
      (and
        (is (= 1 (count variables)))
        (is (= 0 (count datastore)))))))
