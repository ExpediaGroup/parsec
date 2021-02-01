(ns parsec.statements.output-statement-test
  (:require [clojure.test :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.statements :refer [output-statement]]))

(deftest output-statement-parser-test
  (testing-parser! "with unsupported assignment" :output-statement "output=x")

  (testing-parser
    "with happy path" :output-statement
    "output name='x'" [:output-statement :datastore [:function :tolowercasemap "name" [:expression "x"]]])

  (testing-parser
    "with happy path" :output-statement
    "output name='x100'" [:output-statement :datastore [:function :tolowercasemap "name" [:expression "x100"]]])

  (testing-parser
    "with output type and one option" :output-statement
    "output hdfs path='/tmp/parsec.out'" [:output-statement :hdfs [:function :tolowercasemap "path" [:expression "/tmp/parsec.out"]]])

  (testing-parser
    "with output type and two options" :output-statement
    "output type name=x1 format=csv" [:output-statement :type [:function :tolowercasemap "name" [:expression :x1] "format" [:expression :csv]]]))

(deftest output-statement-test
  (testing-statement
    "with passthrough of dataset"
    (output-statement :datastore {:name "x"})
    test-dataset1
    test-dataset1)

  (testing "with updated datastore"
    (let [context {:datastore {} :current-dataset test-dataset1}
          new-context ((output-statement :datastore {:name "x"}) context)
          new-datastore (:datastore new-context)]
      (is (= 1 (count new-datastore)))
      (is (= {:name      "x"
              :temporary false
              :count     (count test-dataset1)
              :columns   #{:col1 :col2 :col3 :col4}
              :data      test-dataset1} (:x new-datastore)))))

  (testing "with replacing existing dataset"
    (let [context {:datastore {:x test-dataset1} :current-dataset test-dataset2}
          new-context ((output-statement :datastore {:name "x"}) context)
          new-datastore (:datastore new-context)]
      (is (= 1 (count new-datastore)))
      (is (= {:name      "x"
              :temporary false
              :count     (count test-dataset2)
              :columns   #{:col1 :col2 :col3 :col4}
              :data      test-dataset2} (:x new-datastore)))))
  )
