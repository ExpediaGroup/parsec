(ns parsec.input.s3-input-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.test-datasets :refer :all]
            [parsec.parser :refer :all]
            [parsec.input.s3 :refer :all]))

(deftest parse-s3-uri-test
  (testing "with nil"
    (is (nil? (parse-s3-uri nil))))
  (testing "with empty string"
    (is (nil? (parse-s3-uri ""))))
  (testing "with bucket only"
    (is (= ["my-bucket" nil nil] (parse-s3-uri "s3://my-bucket"))))
  (testing "with bucket and object"
    (is (= ["my-bucket" nil "object1"] (parse-s3-uri "s3://my-bucket/object1"))))
  (testing "with bucket and prefix"
    (is (= ["my-bucket" "2016/06/08/" nil] (parse-s3-uri "s3://my-bucket/2016/06/08/"))))
  (testing "with bucket and prefix and object"
    (is (= ["my-bucket" "2016/06/08/" "foo.xml"] (parse-s3-uri "s3://my-bucket/2016/06/08/foo.xml"))))
  )
