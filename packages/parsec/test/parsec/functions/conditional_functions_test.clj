(ns parsec.functions.conditional-functions-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.functions :refer :all]
            [parsec.functions.conditionalfunctions :refer :all]))

(deftest if-test
  (let [iff (partial function-transform :if)]
    (testing "with string"
      (is (= "wasTrue" ((iff "red" "wasTrue" "wasFalse") {} nil)))
      (is (= "wasTrue" ((iff "" "wasTrue" "wasFalse") {} nil)))
      (is (= "" ((iff "" "" ".") {} nil))))

    (testing "with number"
      (is (= 20 ((iff 0 10 20) {} nil)))
      (is (= 10 ((iff 1 10 20) {} nil)))
      (is (= 10 ((iff 3/4 10 20) {} nil)))
      (is (= 10 ((iff 3.14 10 20) {} nil)))
      (is (= 0 ((iff Long/MAX_VALUE 0 1) {} nil))))

    (testing "with boolean"
      (is (= false ((iff true false true) {} nil)))
      (is (= true ((iff false false true) {} nil))))

    (testing "with nil"
      (is (= "y" ((iff nil "x" "y") {} nil)))
      (is (= nil ((iff false "x" nil) {} nil))))

    (testing "with keyword"
      (is (= 1 ((iff :col1 :col2 :col3) {:col1 true :col2 1 :col3 2} nil)))
      (is (= 2 ((iff :col1 :col2 :col3) {:col1 false :col2 1 :col3 2} nil))))

    (testing "without whenFalse"
      (is (= "x" ((iff true "x") {} nil)))
      (is (= nil ((iff false "x") {} nil))))))

(deftest case-test
  (let [casefn (partial function-transform :case)]
    (testing "with string"
      (is (= "wasTrue" ((casefn "red" "wasTrue" "blue" "wasFalse") {} nil)))
      (is (= "wasFalse" ((casefn false "wasTrue" "blue" "wasFalse") {} nil)))
      (is (= "wasTrue" ((casefn "" "wasTrue" "" "wasFalse") {} nil)))
      (is (= "" ((casefn "" "" "." ".") {} nil))))

    (testing "with number"
      (is (= 20 ((casefn 0 10 1 20) {} nil)))
      (is (= 10 ((casefn 1 10 0 20) {} nil)))
      (is (= 10 ((casefn 3/4 10 -3/4 20) {} nil)))
      (is (= 10 ((casefn 3.14 10 false 20) {} nil)))
      (is (= 0 ((casefn Long/MAX_VALUE 0 4 1) {} nil)))
      (is (= nil ((casefn 0 1 0 1 0 1) {} nil))))

    (testing "with boolean"
      (is (= true ((casefn true true) {} nil)))
      (is (= nil ((casefn false 1) {} nil)))
      (is (= 1 ((casefn true 1 false 2) {} nil)))
      (is (= 4 ((casefn false 3 true 4) {} nil)))
      (is (= nil ((casefn false 3 false 4) {} nil)))
      (is (= 6 ((casefn false 1 false 2 false 3 false 4 false 5 true 6 false 7) {} nil))))

    (testing "with nil"
      (is (= "y" ((casefn nil "x" true "y") {} nil)))
      (is (= nil ((casefn false "x" true nil) {} nil))))

    (testing "with keyword"
      (is (= 1 ((casefn :col1 :col2 true :col3) {:col1 true :col2 1 :col3 2} nil)))
      (is (= 2 ((casefn :col1 :col2 true :col3) {:col1 false :col2 1 :col3 2} nil))))

    (testing "with mismatched args"
      (is (thrown? Exception ((casefn true) {} nil)))
      (is (thrown? Exception ((casefn true true true) {} nil)))
      (is (thrown? Exception ((casefn false false true true false) {} nil))))))
