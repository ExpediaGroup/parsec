(ns parsec.helpers.types-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [clj-time.core :as time]
            [clj-time.coerce :as timec]
            [clj-time.format :as timef])
  (:import (org.joda.time DateTime)))

(deftest boolean?-test
  (testing "with true"
    (is (true? (boolean? true))))
  (testing "with false"
    (is (true? (boolean? false))))
  (testing "with nil"
    (is (false? (boolean? nil))))
  (testing "with number"
    (is (false? (boolean? 10))))
  (testing "with floating-point"
    (is (false? (boolean? 3.14))))
  (testing "with string"
    (is (false? (boolean? "true"))))
  (testing "with list"
    (is (false? (boolean? [1 2 3]))))
  (testing "with map"
    (is (false? (boolean? { :x 1 :y 2 }))))
  (testing "with date"
    (is (false? (boolean? (time/now))))))

(deftest date?-test
  (testing "with true"
    (is (false? (date? true))))
  (testing "with false"
    (is (false? (date? false))))
  (testing "with nil"
    (is (false? (date? nil))))
  (testing "with number"
    (is (false? (date? 10))))
  (testing "with floating-point"
    (is (false? (date? 3.14))))
  (testing "with string"
    (is (false? (date? "true"))))
  (testing "with list"
    (is (false? (date? [1 2 3]))))
  (testing "with map"
    (is (false? (date? { :x 1 :y 2 }))))
  (testing "with date"
    (is (true? (date? (time/now)))))
  (testing "with date 2"
    (is (true? (date? (time/yesterday)))))
  (testing "with date 3"
    (is (true? (date? (.withTimeAtStartOfDay ^DateTime (time/now))))))
  (testing "with interval"
    (is (false? (date? (time/interval (time/yesterday) (time/now)))))))

(deftest double?-test
  (testing "with true"
    (is (false? (double? true))))
  (testing "with false"
    (is (false? (double? false))))
  (testing "with nil"
    (is (false? (double? nil))))
  (testing "with number"
    (is (false? (double? 10))))
  (testing "with floating-point"
    (is (true? (double? 3.14))))
  (testing "with negative floating-point"
    (is (true? (double? -3.14))))
  (testing "with zero floating-point"
    (is (true? (double? 0.0))))
  (testing "with string"
    (is (false? (double? "true"))))
  (testing "with list"
    (is (false? (double? [1 2 3]))))
  (testing "with map"
    (is (false? (double? { :x 1 :y 2 }))))
  (testing "with date"
    (is (false? (double? (time/now))))))

(deftest to-number-test
  (testing "with true"
    (is (nil? (to-number true))))
  (testing "with false"
    (is (nil? (to-number false))))
  (testing "with nil"
    (is (nil? (to-number nil))))
  (testing "with integer"
    (is (= 101 (to-number 101))))
  (testing "with ratio"
    (is (= 9/4 (to-number 9/4))))
  (testing "with floating-point"
    (is (= 3.14 (to-number 3.14))))
  (testing "with negative floating-point"
    (is (= -3.14 (to-number -3.14))))
  (testing "with zero floating-point"
    (is (= 0.0 (to-number 0.0))))
  (testing "with non-numeric string"
    (is (nil? (to-number "true"))))
  (testing "with non-numeric string"
    (is (nil? (to-number ":-)"))))
  (testing "with numeric string"
    (is (= 6/5 (to-number "1.2"))))
  (testing "with zero string"
    (is (= 0 (to-number "0"))))
  (testing "with positive string"
    (is (= 42 (to-number "+42"))))
  (testing "with scientific notation string"
    (is (= 14400N (to-number "1.44e4"))))
  (testing "with negative scientific notation string"
    (is (= -19200N (to-number "-1.92E4"))))
  (testing "with long string"
    (is (= 123456789001987 (to-number "123456789001987"))))
  (testing "with fractional string"
    (is (= 3/4 (to-number "3/4"))))
  (testing "with list"
    (is (nil? (to-number [1 2 3]))))
  (testing "with map"
    (is (nil? (to-number { :x 1 :y 2 }))))
  (testing "with date"
    (let [t1 (time/now)
          epoch (timec/to-long t1)]
      (is (= epoch (to-number t1))))))

(deftest to-integer-test
  (testing "with true"
    (is (nil? (to-integer true))))
  (testing "with false"
    (is (nil? (to-integer false))))
  (testing "with nil"
    (is (nil? (to-integer nil))))
  (testing "with integer"
    (is (= 101 (to-integer 101))))
  (testing "with ratio"
    (is (= 2 (to-integer 9/4))))
  (testing "with floating-point"
    (is (= 3 (to-integer 3.14))))
  (testing "with negative floating-point"
    (is (= -3 (to-integer -3.14))))
  (testing "with zero floating-point"
    (is (= 0 (to-integer 0.0))))
  (testing "with non-numeric string"
    (is (nil? (to-integer "true"))))
  (testing "with non-numeric string"
    (is (nil? (to-integer ":-)"))))
  (testing "with numeric string"
    (is (= 1 (to-integer "1.2"))))
  (testing "with zero string"
    (is (= 0 (to-integer "0"))))
  (testing "with positive string"
    (is (= 42 (to-integer "+42"))))
  (testing "with scientific notation string"
    (is (= 14400N (to-integer "1.44e4"))))
  (testing "with negative scientific notation string"
    (is (= -19200N (to-integer "-1.92E4"))))
  (testing "with long string"
    (is (= 123456789001987 (to-integer "123456789001987"))))
  (testing "with double string"
    (is (= 1234 (to-integer "1234.56789001987"))))
  (testing "with fractional string"
    (is (= 0 (to-integer "3/4"))))
  (testing "with list"
    (is (nil? (to-integer [1 2 3]))))
  (testing "with map"
    (is (nil? (to-integer { :x 1 :y 2 }))))
  (testing "with date"
    (let [t1 (time/now)
          epoch (timec/to-long t1)]
      (is (= epoch (to-integer t1))))))

(deftest to-double-test
  (testing "with true"
    (is (nil? (to-double true))))
  (testing "with false"
    (is (nil? (to-double false))))
  (testing "with nil"
    (is (nil? (to-double nil))))
  (testing "with integer"
    (is (= 101.0 (to-double 101))))
  (testing "with ratio"
    (is (= 2.25 (to-double 9/4))))
  (testing "with floating-point"
    (is (= 3.14 (to-double 3.14))))
  (testing "with negative floating-point"
    (is (= -3.14 (to-double -3.14))))
  (testing "with zero floating-point"
    (is (= 0.0 (to-double 0.0))))
  (testing "with non-numeric string"
    (is (nil? (to-double "true"))))
  (testing "with non-numeric string"
    (is (nil? (to-double ":-)"))))
  (testing "with numeric string"
    (is (= 1.2 (to-double "1.2"))))
  (testing "with zero string"
    (is (= 0.0 (to-double "0"))))
  (testing "with positive string"
    (is (= 42.0 (to-double "+42"))))
  (testing "with scientific notation string"
    (is (= 14400.0 (to-double "1.44e4"))))
  (testing "with negative scientific notation string"
    (is (= -19200.0 (to-double "-1.92E4"))))
  (testing "with long string"
    (is (= 123456789001987.0 (to-double "123456789001987"))))
  (testing "with double string"
    (is (= 1234.56789001987 (to-double "1234.56789001987"))))
  (testing "with fractional string"
    (is (= 0.75 (to-double "3/4"))))
  (testing "with list"
    (is (nil? (to-double [1 2 3]))))
  (testing "with map"
    (is (nil? (to-double { :x 1 :y 2 }))))
  (testing "with date"
    (let [t1 (time/now)
          epoch (timec/to-long t1)]
      (is (= (double epoch) (to-double t1))))))

(deftest to-string-test
  (testing "with true"
    (is (= "true" (to-string true))))
  (testing "with false"
    (is (= "false" (to-string false))))
  (testing "with nil"
    (is (nil? (to-string nil))))
  (testing "with integer"
    (is (= "101" (to-string 101))))
  (testing "with ratio"
    (is (= "2.25" (to-string 9/4))))
  (testing "with floating-point"
    (is (= "3.14" (to-string 3.14))))
  (testing "with negative floating-point"
    (is (= "-3.14" (to-string -3.14))))
  (testing "with zero floating-point"
    (is (= "0.0" (to-string 0.0))))
  (testing "with string"
    (is (= "true" (to-string "true"))))
  (testing "with non-numeric string"
    (is (= ":-)" (to-string ":-)"))))
  (testing "with zero string"
    (is (= "Hello World" (to-string "Hello World"))))
  (testing "with list of numbers"
    (is (= "[1,2,3]" (to-string [1 2 3]))))
  (testing "with list of strings"
    (is (= "[\"a\",\"b\",\"c\"]" (to-string ["a" "b" "c"]))))
  (testing "with map"
    (is (= "{\"x\":1,\"y\":2}" (to-string { :x 1 :y 2 }))))
  (testing "with date (now)"
    (let [t1 (time/now)]
      (is (= (str t1) (to-string t1)))))
  (testing "with date (hardcoded)"
    (is (= "2016-03-10T00:00:00.000Z" (to-string (timef/parse (timef/formatter "yyyy/MM/dd") "2016/03/10")))))
  (testing "with date and formatter"
    (is (= "03-10-2016" (to-string (timef/parse (timef/formatter "yyyy/MM/dd") "2016/03/10") "MM-dd-yyyy"))))
  (testing "with non-date and formatter"
    (is (thrown? Exception (to-string "foo" "MM-dd-yyyy")))))

(deftest to-boolean-test
  (testing "with true"
    (is (true? (to-boolean true))))
  (testing "with false"
    (is (false? (to-boolean false))))
  (testing "with nil"
    (is (nil? (to-boolean nil))))
  (testing "with non-zero integer"
    (is (true? (to-boolean 101))))
  (testing "with zero integer"
    (is (false? (to-boolean 0))))
  (testing "with ratio"
    (is (true? (to-boolean 9/4))))
  (testing "with ratio less than one"
    (is (true? (to-boolean 3/4))))
  (testing "with floating-point"
    (is (true? (to-boolean 3.14))))
  (testing "with negative floating-point"
    (is (true? (to-boolean -3.14))))
  (testing "with zero floating-point"
    (is (false? (to-boolean 0.0))))
  (testing "with true string"
    (is (true? (to-boolean "true"))))
  (testing "with false string"
    (is (false? (to-boolean "false"))))
  (testing "with non-boolean string"
    (is (nil? (to-boolean ":-)"))))
  (testing "with list of numbers"
    (is (nil? (to-boolean [1 2 3]))))
  (testing "with list of strings"
    (is (nil? (to-boolean ["a" "b" "c"]))))
  (testing "with map"
    (is (nil? (to-boolean { :x 1 :y 2 }))))
  (testing "with date"
      (is (nil? (to-boolean (time/now))))))

(deftest try-to-boolean-test
  (testing "with true"
    (is (true? (try-to-boolean true))))
  (testing "with false"
    (is (false? (try-to-boolean false))))
  (testing "with nil"
    (is (nil? (try-to-boolean nil))))
  (testing "with non-zero integer"
    (is (true? (try-to-boolean 101))))
  (testing "with zero integer"
    (is (false? (try-to-boolean 0))))
  (testing "with ratio"
    (is (true? (try-to-boolean 9/4))))
  (testing "with floating-point"
    (is (true? (try-to-boolean 3.14))))
  (testing "with negative floating-point"
    (is (true? (try-to-boolean -3.14))))
  (testing "with zero floating-point"
    (is (false? (try-to-boolean 0.0))))
  (testing "with string"
    (is (true? (try-to-boolean "true"))))
  (testing "with string"
    (is (false? (try-to-boolean "false"))))
  (testing "with non-boolean string"
    (is (= ":-)" (try-to-boolean ":-)"))))
  (testing "with list of numbers"
    (is (= [1 2 3] (try-to-boolean [1 2 3]))))
  (testing "with list of strings"
    (is (= ["a" "b" "c"] (try-to-boolean ["a" "b" "c"]))))
  (testing "with map"
    (is (= { :x 1 :y 2 } (try-to-boolean { :x 1 :y 2 }))))
  (testing "with date"
    (let [t1 (time/now)]
      (is (= t1 (try-to-boolean t1))))))

(deftest to-date-test
  (testing "with true"
    (is (nil? (to-date true))))
  (testing "with false"
    (is (nil? (to-date false))))
  (testing "with nil"
    (is (nil? (to-date nil))))
  (testing "with integer"
    (is (= (time/date-time 1970 01 01 0 0 0 101) (to-date 101))))
  (testing "with zero floating-point"
    (is (= (time/date-time 1970 01 01 0 0 0 0) (to-date 0.0))))
  (testing "with boolean string"
    (is (nil? (to-date "true"))))
  (testing "with non-numeric string"
    (is (nil? (to-date ":-)"))))
  (testing "with zero string"
    (is (nil? (to-date "Hello World"))))
  (testing "with ISO-8601 string"
    (is (= (time/date-time 2016 04 24 0 0 0) (to-date "2016-04-24T00:00:00.000Z"))))
  (testing "with ISO-8601 string and format code"
    (is (= (time/date-time 2016 04 24 0 0 0) (to-date "2016-04-24T00:00:00.000Z" "yyyy-MM-dd'T'HH:mm:ss.sssZ"))))
  (testing "with date string and format code"
    (is (= (time/date-time 2016 04 24 0 0 0) (to-date "2016-04-24" "yyyy-MM-dd"))))
  (testing "with list of numbers"
    (is (nil? (to-date [1 2 3]))))
  (testing "with list of strings"
    (is (nil? (to-date ["a" "b" "c"]))))
  (testing "with map"
    (is (nil? (to-date { :x 1 :y 2 }))))
  (testing "with epoch milliseconds"
    (let [t1 (time/now)
          epoch (timec/to-long t1)]
      (is (= t1 (to-date epoch)))))
  (testing "with date"
    (let [t1 (time/now)]
      (is (= t1 (to-date t1))))))

(deftest only-strings-test
  (testing "with true"
    (is (nil? (only-strings identity true))))
  (testing "with false"
    (is (nil? (only-strings identity false))))
  (testing "with nil"
    (is (nil? (only-strings #(123) nil))))
  (testing "with number"
    (is (nil? (only-strings identity 10))))
  (testing "with floating-point"
    (is (nil? (only-strings identity 3.14))))
  (testing "with negative floating-point"
    (is (nil? (only-strings identity -3.14))))
  (testing "with zero floating-point"
    (is (nil? (only-strings identity 0.0))))
  (testing "with string"
    (is (= "true" (only-strings identity "true"))))
  (testing "with strings"
    (is (= "hello world" (only-strings #(str % %2 %3) "hello" " " "world"))))
  (testing "with list"
    (is (nil? (only-strings identity [1 2 3]))))
  (testing "with map"
    (is (nil? (only-strings identity { :x 1 :y 2 }))))
  (testing "with date"
    (is (nil? (only-strings identity (time/now))))))

(deftest only-sequentials-test
  (testing "with true"
    (is (nil? (only-sequentials identity true))))
  (testing "with false"
    (is (nil? (only-sequentials identity false))))
  (testing "with nil"
    (is (nil? (only-sequentials #(123) nil))))
  (testing "with number"
    (is (nil? (only-sequentials identity 10))))
  (testing "with floating-point"
    (is (nil? (only-sequentials identity 3.14))))
  (testing "with string"
    (is (nil? (only-sequentials identity "true"))))
  (testing "with list"
    (is (= '(1 2 3) (only-sequentials identity '(1 2 3)))))
  (testing "with vector"
    (is (= [1 2 3] (only-sequentials identity [1 2 3]))))
  (testing "with map"
    (is (nil? (only-sequentials identity { :x 1 :y 2 }))))
  (testing "with date"
    (is (nil? (only-sequentials identity (time/now))))))

(deftest only-sequentials-as-lists-test
  (testing "with true"
    (is (nil? (only-sequentials identity true))))
  (testing "with false"
    (is (nil? (only-sequentials identity false))))
  (testing "with nil"
    (is (nil? (only-sequentials #(123) nil))))
  (testing "with number"
    (is (nil? (only-sequentials identity 10))))
  (testing "with floating-point"
    (is (nil? (only-sequentials identity 3.14))))
  (testing "with string"
    (is (nil? (only-sequentials identity "true"))))
  (testing "with list"
    (is (= '(1 2 3) (only-sequentials identity '(1 2 3)))))
  (testing "with vector"
    (is (= '(1 2 3) (only-sequentials identity [1 2 3]))))
  (testing "with map"
    (is (nil? (only-sequentials identity { :x 1 :y 2 }))))
  (testing "with set"
    (is (nil? (only-sequentials identity (set [1 2 3])))))
  (testing "with date"
    (is (nil? (only-sequentials identity (time/now))))))
