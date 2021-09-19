(ns parsec.functions.string-functions-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.functions :refer :all]
            [parsec.functions.stringfunctions :refer :all]))

(deftest trim-test
  (let [trim (partial function-transform :trim)]
    (testing "with string"
      (is (= "abc" ((trim " abc ") {} nil)))
      (is (= "abc" ((trim "\n \t abc\n   \t\t\r") {} nil)))
      (is (= "" ((trim "") {} nil)))
      (is (= "" ((trim " ") {} nil))))

    (testing "with keyword"
      (is (= "x" ((trim :col1) {:col1 " x "} nil))))

    (testing "with non-string types"
      (is (nil? ((trim nil) {} nil)))
      (is (nil? ((trim true) {} nil)))
      (is (nil? ((trim 50) {} nil)))
      (is (nil? ((trim [1 2 3]) {} nil))))))

(deftest ltrim-test
  (let [ltrim (partial function-transform :ltrim)]
    (testing "with string"
      (is (= "abc " ((ltrim " abc ") {} nil)))
      (is (= "abc" ((ltrim "\n \t abc") {} nil))))

    (testing "with non-string types"
      (is (nil? ((ltrim nil) {} nil)))
      (is (nil? ((ltrim true) {} nil)))
      (is (nil? ((ltrim 50) {} nil)))
      (is (nil? ((ltrim [1 2 3]) {} nil))))))

(deftest rtrim-test
  (let [rtrim (partial function-transform :rtrim)]
    (testing "with string"
      (is (= " abc" ((rtrim " abc ") {} nil)))
      (is (= "\n \t abc" ((rtrim "\n \t abc \n \t") {} nil))))

    (testing "with non-string types"
      (is (nil? ((rtrim nil) {} nil)))
      (is (nil? ((rtrim true) {} nil)))
      (is (nil? ((rtrim 50) {} nil)))
      (is (nil? ((rtrim [1 2 3]) {} nil))))))

(deftest uppercase-test
  (let [uppercase (partial function-transform :uppercase)]
    (testing "with string"
      (is (= "ABC" ((uppercase "abc") {} nil)))
      (is (= "" ((uppercase "") {} nil))))

    (testing "with keyword"
      (is (= "GREAT EXPECTATIONS" ((uppercase :col1) {:col1 "Great Expectations"} nil))))

    (testing "with non-string types"
      (is (nil? ((uppercase nil) {} nil)))
      (is (nil? ((uppercase true) {} nil)))
      (is (nil? ((uppercase 50) {} nil)))
      (is (nil? ((uppercase [1 2 3]) {} nil))))))

(deftest lowercase-test
  (let [lowercase (partial function-transform :lowercase)]
    (testing "with string"
      (is (= "abc" ((lowercase "ABC") {} nil)))
      (is (= "" ((lowercase "") {} nil))))

    (testing "with keyword"
      (is (= "great expectations" ((lowercase :col1) {:col1 "Great Expectations"} nil))))

    (testing "with non-string types"
      (is (nil? ((lowercase nil) {} nil)))
      (is (nil? ((lowercase true) {} nil)))
      (is (nil? ((lowercase 50) {} nil)))
      (is (nil? ((lowercase [1 2 3]) {} nil))))))

(deftest split-test
  (let [split (partial function-transform :split)]
    (testing "with string"
      (is (nil? ((split nil "|") {} nil))))
      (is (= ["A" "B" "C"] ((split "ABC" "") {} nil)))
      (is (= ["A" "B" "C"] ((split "A,B,C" ",") {} nil))
      (is (= ["apple" "banana" "mango"] ((split "apple | banana | mango" "\\s*\\|\\s*") {} nil))))

    (testing "with non-string types"
      (is (thrown? Exception ((split true ",") {} nil)))
      (is (thrown? Exception ((split 50 ",") {} nil))))))

(deftest replace-test
  (let [replace (partial function-transform :replace)]
    (testing "with string"
      (is (= "Jane Doe" ((replace "John Doe" "John" "Jane") {} nil)))
      (is (= "x" ((replace "" "" "x") {} nil)))
      (is (= "two one two" ((replace "one one two" "one" "two") {} nil)))
      (is (= "" ((replace "" "xy" "zy") {} nil)))
      (is (= "|abcde" ((replace "abcde", "", "|") {} nil)))
      (is (= "abcde" ((replace "abcde", "!", "?") {} nil))))

    (testing "with keyword"
      (is (= "Xyz" ((replace :col1 :col2 :col3) {:col1 "xyz" :col2 "x" :col3 "X"} nil))))

    (testing "with non-string types"
      (is (nil? ((replace nil "?" "!") {} nil)))
      (is (nil? ((replace true "?" "!") {} nil)))
      (is (nil? ((replace 50 "?" "!") {} nil)))
      (is (nil? ((replace [1 2 3] "?" "!") {} nil))))))

(deftest replaceall-test
  (let [replaceall (partial function-transform :replaceall)]
    (testing "with string"
      (is (= "Jane Doe" ((replaceall "John Doe" "John" "Jane") {} nil)))
      (is (= "x" ((replaceall "" "" "x") {} nil)))
      (is (= "two two two" ((replaceall "one one two" "one" "two") {} nil)))
      (is (= "" ((replaceall "" "xy" "zy") {} nil)))
      (is (= "|a|b|c|d|e|" ((replaceall "abcde", "", "|") {} nil)))
      (is (= "abcde" ((replaceall "abcde", "!", "?") {} nil))))

    (testing "with keyword"
      (is (= "Xyz" ((replaceall :col1 :col2 :col3) {:col1 "xyz" :col2 "x" :col3 "X"} nil))))

    (testing "with non-string types"
      (is (nil? ((replaceall nil "?" "!") {} nil)))
      (is (nil? ((replaceall true "?" "!") {} nil)))
      (is (nil? ((replaceall 50 "?" "!") {} nil)))
      (is (nil? ((replaceall [1 2 3] "?" "!") {} nil))))))

(deftest urlencode-test
  (let [urlencode (partial function-transform :urlencode)]
    (testing "with string"
      (is (= "" ((urlencode "") {} nil)))
      (is (= "apples" ((urlencode "apples") {} nil)))
      (is (= "+" ((urlencode " ") {} nil)))
      (is (= "x+y" ((urlencode "x y") {} nil)))
      (is (= "%3Fx%3Dy" ((urlencode "?x=y") {} nil))))

    (testing "with nil"
      (is (nil? ((urlencode nil) {} nil))))

    (testing "with keyword"
      (is (= "x%3Dy" ((urlencode :col1) {:col1 "x=y"} nil))))

    (testing "with non-string types"
      (is (nil? ((urlencode true) {} nil)))
      (is (nil? ((urlencode 112) {} nil)))
      (is (nil? ((urlencode [1 2 3]) {} nil))))))

(deftest urldecode-test
  (let [urldecode (partial function-transform :urldecode)]
    (testing "with string"
      (is (= "" ((urldecode "") {} nil)))
      (is (= "apples" ((urldecode "apples") {} nil)))
      (is (= " " ((urldecode " ") {} nil)))
      (is (= " " ((urldecode "+") {} nil)))
      (is (= "x y z" ((urldecode "x+y+z") {} nil)))
      (is (= "?x=y" ((urldecode "%3Fx%3Dy") {} nil))))

    (testing "with nil"
      (is (nil? ((urldecode nil) {} nil))))

    (testing "with keyword"
      (is (= "x=y" ((urldecode :col1) {:col1 "x%3Dy"} nil))))

    (testing "with non-string types"
      (is (nil? ((urldecode true) {} nil)))
      (is (nil? ((urldecode 112) {} nil)))
      (is (nil? ((urldecode [1 2 3]) {} nil))))))

(deftest base64encode-test
  (let [base64encode (partial function-transform :base64encode)]
    (testing "with string"
      (is (= "" ((base64encode "") {} nil)))
      (is (= "YXBwbGVz" ((base64encode "apples") {} nil)))
      (is (= "IA==" ((base64encode " ") {} nil)))
      (is (= "aGVsbG8gd29ybGQ=" ((base64encode "hello world") {} nil))))

    (testing "with nil"
      (is (nil? ((base64encode nil) {} nil))))

    (testing "with keyword"
      (is (= "eD15" ((base64encode :col1) {:col1 "x=y"} nil))))

    (testing "with non-string types"
      (is (nil? ((base64encode true) {} nil)))
      (is (nil? ((base64encode 112) {} nil)))
      (is (nil? ((base64encode [1 2 3]) {} nil))))))

(deftest base64decode-test
  (let [base64decode (partial function-transform :base64decode)]
    (testing "with string"
      (is (= "" ((base64decode "") {} nil)))
      (is (= "apples" ((base64decode "YXBwbGVz") {} nil)))
      (is (= " " ((base64decode "IA==") {} nil)))
      (is (= "hello world" ((base64decode "aGVsbG8gd29ybGQ=") {} nil))))

    (testing "with nil"
      (is (nil? ((base64decode nil) {} nil))))

    (testing "with keyword"
      (is (= "x=y" ((base64decode :col1) {:col1 "eD15"} nil))))

    (testing "with non-string types"
      (is (nil? ((base64decode true) {} nil)))
      (is (nil? ((base64decode 112) {} nil)))
      (is (nil? ((base64decode [1 2 3]) {} nil))))))

(deftest substring-test
  (let [substring (partial function-transform :substring)]
    (testing "with string"
      (is (= "hello world" ((substring "hello world" 0) {} nil)))
      (is (= "world" ((substring "hello world" 6) {} nil)))
      (is (= "h" ((substring "hello world" 0 1) {} nil)))
      (is (= "ello" ((substring "hello world" 1 5) {} nil)))
      (is (= "hello world" ((substring "hello world" 0 100) {} nil)))
      (is (= " " ((substring "hello world" 5 6) {} nil)))

    (testing "with non-string types"
      (is (nil? ((substring nil 0 10) {} nil))))
      (is (nil? ((substring true 0 1) {} nil)))
      (is (nil? ((substring 100 0 1) {} nil)))
      (is (nil? ((substring [1 2 3] 0 10) {} nil))))))

(deftest substr-test
  (let [substr (partial function-transform :substr)]
    (testing "with string"
      (is (= "hello world" ((substr "hello world" 0) {} nil)))
      (is (= "world" ((substr "hello world" 6) {} nil)))
      (is (= "h" ((substr "hello world" 0 1) {} nil)))
      (is (= "el" ((substr "hello world" 1 2) {} nil)))
      (is (= "ello" ((substr "hello world" 1 4) {} nil)))
      (is (= "hello world" ((substr "hello world" 0 100) {} nil)))
      (is (= "wor" ((substr "hello world" 6 3) {} nil))))

    (testing "with non-string types"
      (is (nil? ((substr nil 0 10) {} nil))))
      (is (nil? ((substr true 0 1) {} nil)))
      (is (nil? ((substr 100 0 1) {} nil)))
      (is (nil? ((substr [1 2 3] 0 10) {} nil)))))

(deftest indexof-test
  (let [indexof (partial function-transform :indexof)]
    (testing "with string"
      (is (zero? ((indexof "hello world" "h") {} nil)))
      (is (= 1 ((indexof "hello world" "e") {} nil)))
      (is (= 6 ((indexof "hello world" "world") {} nil)))
      (is (nil? ((indexof "hello world" "!") {} nil)))
      (is (= 2 ((indexof "@@!!!!@@" "!") {} nil)))
      (is (= 1 ((indexof "(425)555-4456" 425) {} nil)))
      (is (= 8 ((indexof "true or false" false) {} nil))))

    (testing "with non-string types"
      (is (nil? ((indexof nil "a") {} nil)))
      (is (nil? ((indexof true 0) {} nil)))
      (is (nil? ((indexof 100 0) {} nil)))
      (is (nil? ((indexof [1 2 3] "a") {} nil))))))

(deftest lastindexof-test
  (let [lastindexof (partial function-transform :lastindexof)]
    (testing "with string"
      (is (zero? ((lastindexof "hello world" "h") {} nil)))
      (is (= 1 ((lastindexof "hello world" "e") {} nil)))
      (is (= 9 ((lastindexof "hello world" "l") {} nil)))
      (is (= 6 ((lastindexof "hello world" "world") {} nil)))
      (is (nil? ((lastindexof "hello world" "!") {} nil)))
      (is (= 5 ((lastindexof "@@!!!!@@" "!") {} nil)))
      (is (= 1 ((lastindexof "(425)555-4456" 425) {} nil)))
      (is (= 9 ((lastindexof "false or false" false) {} nil))))

    (testing "with non-string types"
      (is (nil? ((lastindexof nil "a") {} nil)))
      (is (nil? ((lastindexof true 0) {} nil)))
      (is (nil? ((lastindexof 100 0) {} nil)))
      (is (nil? ((lastindexof [1 2 3] "a") {} nil))))))

(deftest startswith-test
  (let [startswith (partial function-transform :startswith)]
    (testing "with string"
      (is (true?((startswith "hello world" "h") {} nil)))
      (is (false? ((startswith "hello world" "e") {} nil)))
      (is (false? ((startswith "hello world" "world") {} nil)))
      (is (true? ((startswith "hello world" "hello") {} nil)))
      (is (true? ((startswith "425-555-4456" 425) {} nil)))
      (is (true? ((startswith "true or false" true) {} nil))))

    (testing "with non-string types"
      (is (nil? ((startswith nil nil) {} nil)))
      (is (nil? ((startswith true 0) {} nil)))
      (is (nil? ((startswith 100 1) {} nil)))
      (is (nil? ((startswith [1 2 3] "a") {} nil))))))

(deftest endswith-test
  (let [endswith (partial function-transform :endswith)]
    (testing "with string"
      (is (true? ((endswith "hello world" "d") {} nil)))
      (is (false? ((endswith "hello world" "e") {} nil)))
      (is (false? ((endswith "hello world" "hello") {} nil)))
      (is (true? ((endswith "hello world" "world") {} nil)))
      (is (true? ((endswith "425-555-4456" 456) {} nil)))
      (is (true? ((endswith "true or false" false) {} nil))))

    (testing "with non-string types"
      (is (nil? ((endswith nil nil) {} nil)))
      (is (nil? ((endswith true 0) {} nil)))
      (is (nil? ((endswith 100 1) {} nil)))
      (is (nil? ((endswith [1 2 3] "a") {} nil))))))

(deftest join-test
  (let [join (partial function-transform :join)]
    (testing "with sequence and two arguments"
      (is (= "" ((join [] "") {} nil)))
      (is (= "" ((join '() "") {} nil)))
      (is (= "a" ((join ["a"] ",") {} nil)))
      (is (= "abc" ((join ["a" "b" "c"] "") {} nil)))
      (is (= "a,b,c" ((join ["a" "b" "c"] ",") {} nil)))
      (is (= "a, b, c" ((join ["a" "b" "c"] ", ") {} nil)))
      (is (= "425-555-2244" ((join '("425" "555" "2244") "-") {} nil)))
      (is (= "1-2-3" ((join '(1 2 3) "-") {} nil))))

    (testing "with non-sequential types"
      (is (nil? ((join nil nil) {} nil)))
      (is (nil? ((join true "a") {} nil)))
      (is (nil? ((join 100 "b") {} nil)))
      (is (nil? ((join "hello world" "c") {} nil))))))
