(ns parsec.functions.is-functions-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.functions :refer :all]
            [clj-time.core :as time]
            [clojure.data.json :as json]
            [parsec.functions.isfunctions :refer :all]))

(deftest isexist-test
  (testing "with keyword"
    (is (true? ((isexist-transform :col1) {:col1 1} nil)))
    (is (false? ((isexist-transform :col2) {:col1 1} nil))))
  (testing "with non-keywords"
    (is (false? ((isexist-transform nil) {} nil)))
    (is (false? ((isexist-transform 40) {} nil)))
    (is (false? ((isexist-transform "") {} nil)))
    (is (false? ((isexist-transform "non-null") {} nil)))
    (is (false? ((isexist-transform false) {} nil)))
    (is (false? ((isexist-transform true) {} nil)))))

(deftest isnull-test
  (let [isnull (partial function-transform :isnull)]
    (testing "with nil"
      (is (true? ((isnull nil) {} nil))))
    (testing "with keyword"
      (is (true? ((isnull :col1) {:col1 nil} nil)))
      (is (false? ((isnull :col1) {:col1 "x"} nil))))
    (testing "with number"
      (is (false? ((isnull 40) {} nil))))
    (testing "with string"
      (is (false? ((isnull "") {} nil)))
      (is (false? ((isnull "non-null") {} nil))))
    (testing "with boolean"
      (is (false? ((isnull false) {} nil)))
      (is (false? ((isnull true) {} nil))))
    (testing "with list"
      (is (false? ((isnull '(1 2)) {} nil))))))

(deftest isnan-test
  (let [isnan (partial function-transform :isnan)]
    (testing "with nil"
      (is (false? ((isnan nil) {} nil)))
      (is (false? ((isnan :col1) {:col1 nil} nil))))
    (testing "with keyword"
      (is (true? ((isnan :col1) {:col1 Double/NaN} nil)))
      (is (false? ((isnan :col1) {:col1 0.0} nil))))
    (testing "with number"
      (is (false? ((isnan 40) {} nil))))
    (testing "with string"
      (is (false? ((isnan "") {} nil)))
      (is (false? ((isnan "non-null") {} nil))))
    (testing "with boolean"
      (is (false? ((isnan false) {} nil)))
      (is (false? ((isnan true) {} nil))))))

(deftest isinfinite-test
  (let [isinfinite (partial function-transform :isinfinite)]
    (testing "with nil"
      (is (false? ((isinfinite nil) {} nil)))
      (is (false? ((isinfinite :col1) {:col1 nil} nil))))
    (testing "with keyword"
      (is (true? ((isinfinite :col1) {:col1 Double/POSITIVE_INFINITY} nil)))
      (is (true? ((isinfinite :col1) {:col1 Double/NEGATIVE_INFINITY} nil)))
      (is (false? ((isinfinite :col1) {:col1 0.0} nil)))
      (is (false? ((isinfinite :col1) {:col1 1} nil)))
      (is (false? ((isinfinite :col1) {:col1 1e20} nil))))
    (testing "with number"
      (is (false? ((isinfinite 40) {} nil))))
    (testing "with string"
      (is (false? ((isinfinite "") {} nil)))
      (is (false? ((isinfinite "non-null") {} nil))))
    (testing "with boolean"
      (is (false? ((isinfinite false) {} nil)))
      (is (false? ((isinfinite true) {} nil))))))

(deftest isfinite-test
  (let [isfinite (partial function-transform :isfinite)]
    (testing "with nil"
      (is (false? ((isfinite nil) {} nil)))
      (is (false? ((isfinite :col1) {:col1 nil} nil))))
    (testing "with keyword"
      (is (false? ((isfinite :col1) {:col1 Double/POSITIVE_INFINITY} nil)))
      (is (false? ((isfinite :col1) {:col1 Double/NEGATIVE_INFINITY} nil)))
      (is (true? ((isfinite :col1) {:col1 0.0} nil)))
      (is (true? ((isfinite :col1) {:col1 1} nil)))
      (is (true? ((isfinite :col1) {:col1 1e20} nil))))
    (testing "with number"
      (is (true? ((isfinite 40) {} nil))))
    (testing "with string"
      (is (false? ((isfinite "") {} nil)))
      (is (false? ((isfinite "non-null") {} nil))))
    (testing "with boolean"
      (is (false? ((isfinite false) {} nil)))
      (is (false? ((isfinite true) {} nil))))))

(deftest isdate-test
  (let [isdate (partial function-transform :isdate)]
    (testing "with nil"
      (is (false? ((isdate nil) {} nil)))
      (is (false? ((isdate :col1) {:col1 nil} nil))))
    (testing "with keyword"
      (is (true? ((isdate :col1) {:col1 (time/today-at-midnight)} nil)))
      (is (true? ((isdate :col1) {:col1 (time/now)} nil)))
      (is (false? ((isdate :col1) {:col1 0.0} nil)))
      (is (false? ((isdate :col1) {:col1 false} nil)))
      (is (false? ((isdate :col1) {:col1 "datetime"} nil))))
    (testing "with number"
      (is (false? ((isdate 40) {} nil))))
    (testing "with string"
      (is (false? ((isdate "") {} nil)))
      (is (false? ((isdate "2015-02-05") {} nil))))
    (testing "with boolean"
      (is (false? ((isdate false) {} nil)))
      (is (false? ((isdate true) {} nil))))
    (testing "with date"
      (is (true? ((isdate (time/today-at-midnight)) {} nil)))
      (is (true? ((isdate (time/now)) {} nil)))
      (is (false? ((isdate (time/today)) {} nil))))))

(deftest isboolean-test
  (let [isboolean (partial function-transform :isboolean)]
    (testing "with nil"
      (is (false? ((isboolean nil) {} nil)))
      (is (false? ((isboolean :col1) {:col1 nil} nil))))
    (testing "with keyword"
      (is (true? ((isboolean :col1) {:col1 true} nil)))
      (is (true? ((isboolean :col1) {:col1 false} nil)))
      (is (false? ((isboolean :col1) {:col1 (time/now)} nil)))
      (is (false? ((isboolean :col1) {:col1 0.0} nil)))
      (is (false? ((isboolean :col1) {:col1 "true"} nil))))
    (testing "with number"
      (is (false? ((isboolean 40) {} nil))))
    (testing "with string"
      (is (false? ((isboolean "") {} nil)))
      (is (false? ((isboolean "2015-02-05") {} nil))))
    (testing "with boolean"
      (is (true? ((isboolean false) {} nil)))
      (is (true? ((isboolean true) {} nil))))
    (testing "with date"
      (is (false? ((isboolean (time/today-at-midnight)) {} nil)))
      (is (false? ((isboolean (time/now)) {} nil)))
      (is (false? ((isboolean (time/today)) {} nil))))))

(deftest isnumber-test
  (let [isnumber (partial function-transform :isnumber)]
    (testing "with nil"
      (is (false? ((isnumber nil) {} nil)))
      (is (false? ((isnumber :col1) {:col1 nil} nil))))
    (testing "with keyword"
      (is (true? ((isnumber :col1) {:col1 1.0} nil)))
      (is (false? ((isnumber :col1) {:col1 (time/today-at-midnight)} nil)))
      (is (false? ((isnumber :col1) {:col1 false} nil)))
      (is (false? ((isnumber :col1) {:col1 "datetime"} nil))))
    (testing "with number"
      (is (true? ((isnumber 0) {} nil)))
      (is (true? ((isnumber 1.0) {} nil)))
      (is (true? ((isnumber -1.0) {} nil)))
      (is (true? ((isnumber 1/2) {} nil))))
    (testing "with string"
      (is (false? ((isnumber "") {} nil)))
      (is (false? ((isnumber "100") {} nil))))
    (testing "with boolean"
      (is (false? ((isnumber false) {} nil)))
      (is (false? ((isnumber true) {} nil))))
    (testing "with date"
      (is (false? ((isnumber (time/today-at-midnight)) {} nil)))
      (is (false? ((isnumber (time/now)) {} nil))))))

(deftest isratio-test
  (let [isratio (partial function-transform :isratio)]
    (testing "with nil"
      (is (false? ((isratio nil) {} nil)))
      (is (false? ((isratio :col1) {:col1 nil} nil))))
    (testing "with keyword"
      (is (false? ((isratio :col1) {:col1 1.0} nil)))
      (is (false? ((isratio :col1) {:col1 (time/today-at-midnight)} nil)))
      (is (false? ((isratio :col1) {:col1 false} nil)))
      (is (false? ((isratio :col1) {:col1 "datetime"} nil))))
    (testing "with number"
      (is (false? ((isratio 0) {} nil)))
      (is (false? ((isratio 1.0) {} nil)))
      (is (false? ((isratio -1.0) {} nil)))
      (is (true? ((isratio 1/2) {} nil)))
      (is (true? ((isratio 22/7) {} nil))))
    (testing "with string"
      (is (false? ((isratio "") {} nil)))
      (is (false? ((isratio "100") {} nil))))
    (testing "with boolean"
      (is (false? ((isratio false) {} nil)))
      (is (false? ((isratio true) {} nil))))
    (testing "with date"
      (is (false? ((isratio (time/today-at-midnight)) {} nil)))
      (is (false? ((isratio (time/now)) {} nil))))))

(deftest isdouble-test
  (let [isdouble (partial function-transform :isdouble)]
    (testing "with nil"
      (is (false? ((isdouble nil) {} nil)))
      (is (false? ((isdouble :col1) {:col1 nil} nil))))
    (testing "with keyword"
      (is (true? ((isdouble :col1) {:col1 1.0} nil)))
      (is (false? ((isdouble :col1) {:col1 99} nil)))
      (is (false? ((isdouble :col1) {:col1 (time/today-at-midnight)} nil)))
      (is (false? ((isdouble :col1) {:col1 false} nil)))
      (is (false? ((isdouble :col1) {:col1 "datetime"} nil))))
    (testing "with number"
      (is (false? ((isdouble 0) {} nil)))
      (is (true? ((isdouble 1.0) {} nil)))
      (is (true? ((isdouble -1.0) {} nil)))
      (is (false? ((isdouble 1/2) {} nil))))
    (testing "with string"
      (is (false? ((isdouble "") {} nil)))
      (is (false? ((isdouble "100") {} nil))))
    (testing "with boolean"
      (is (false? ((isdouble false) {} nil)))
      (is (false? ((isdouble true) {} nil))))
    (testing "with date"
      (is (false? ((isdouble (time/today-at-midnight)) {} nil)))
      (is (false? ((isdouble (time/now)) {} nil))))))

(deftest isinteger-test
  (let [isinteger (partial function-transform :isinteger)]
    (testing "with nil"
      (is (false? ((isinteger nil) {} nil)))
      (is (false? ((isinteger :col1) {:col1 nil} nil))))
    (testing "with keyword"
      (is (false? ((isinteger :col1) {:col1 1.0} nil)))
      (is (true? ((isinteger :col1) {:col1 99} nil)))
      (is (false? ((isinteger :col1) {:col1 (time/today-at-midnight)} nil)))
      (is (false? ((isinteger :col1) {:col1 false} nil)))
      (is (false? ((isinteger :col1) {:col1 "datetime"} nil))))
    (testing "with number"
      (is (true? ((isinteger 0) {} nil)))
      (is (true? ((isinteger 1) {} nil)))
      (is (true? ((isinteger -99) {} nil)))
      (is (false? ((isinteger 1.0) {} nil)))
      (is (false? ((isinteger -1.0) {} nil)))
      (is (false? ((isinteger 1/2) {} nil))))
    (testing "with string"
      (is (false? ((isinteger "") {} nil)))
      (is (false? ((isinteger "100") {} nil))))
    (testing "with boolean"
      (is (false? ((isinteger false) {} nil)))
      (is (false? ((isinteger true) {} nil))))
    (testing "with date"
      (is (false? ((isinteger (time/today-at-midnight)) {} nil)))
      (is (false? ((isinteger (time/now)) {} nil))))
    (testing "with list"
      (is (false? ((isinteger '(1 2)) {} nil))))))

(deftest isstring-test
  (let [isstring (partial function-transform :isstring)]
    (testing "with nil"
      (is (false? ((isstring nil) {} nil)))
      (is (false? ((isstring :col1) {:col1 nil} nil))))
    (testing "with keyword"
      (is (true? ((isstring :col1) {:col1 "string"} nil)))
      (is (false? ((isstring :col1) {:col1 1.0} nil)))
      (is (false? ((isstring :col1) {:col1 (time/today-at-midnight)} nil)))
      (is (false? ((isstring :col1) {:col1 false} nil))))
    (testing "with number"
      (is (false? ((isstring 0) {} nil)))
      (is (false? ((isstring 1.0) {} nil)))
      (is (false? ((isstring -1.0) {} nil)))
      (is (false? ((isstring 1/2) {} nil))))
    (testing "with string"
      (is (true? ((isstring "") {} nil)))
      (is (true? ((isstring "hello world") {} nil)))
      (is (true? ((isstring "100") {} nil))))
    (testing "with boolean"
      (is (false? ((isstring false) {} nil)))
      (is (false? ((isstring true) {} nil))))
    (testing "with date"
      (is (false? ((isstring (time/today-at-midnight)) {} nil)))
      (is (false? ((isstring (time/now)) {} nil))))))

(deftest islist-test
  (let [islist (partial function-transform :islist)]
    (testing "with nil"
      (is (false? ((islist nil) {} nil)))
      (is (false? ((islist :col1) {:col1 nil} nil))))
    (testing "with keyword"
      (is (true? ((islist :col1) {:col1 '("a")} nil)))
      (is (false? ((islist :col1) {:col1 1.0} nil))))
    (testing "with number"
      (is (false? ((islist 0) {} nil)))
      (is (false? ((islist 1.0) {} nil))))
    (testing "with string"
      (is (false? ((islist "hello world") {} nil))))
    (testing "with boolean"
      (is (false? ((islist false) {} nil)))
      (is (false? ((islist true) {} nil))))
    (testing "with date"
      (is (false? ((islist (time/today-at-midnight)) {} nil)))
      (is (false? ((islist (time/now)) {} nil))))
    (testing "with sequential"
      (is (true? ((islist '()) {} nil)))
      (is (true? ((islist '(1 2)) {} nil)))
      (is (true? ((islist [1 2]) {} nil)))
      (is (true? ((islist '(nil true 1 "x")) {} nil))))))

(deftest ismap-test
  (let [ismap (partial function-transform :ismap)]
    (testing "with nil"
      (is (false? ((ismap nil) {} nil)))
      (is (false? ((ismap :col1) {:col1 nil} nil))))
    (testing "with keyword"
      (is (true? ((ismap :col1) {:col1 { :a 1 }} nil)))
      (is (false? ((ismap :col1) {:col1 1.0} nil))))
    (testing "with number"
      (is (false? ((ismap 0) {} nil)))
      (is (false? ((ismap 1.0) {} nil))))
    (testing "with string"
      (is (false? ((ismap "hello world") {} nil))))
    (testing "with boolean"
      (is (false? ((ismap false) {} nil)))
      (is (false? ((ismap true) {} nil))))
    (testing "with date"
      (is (false? ((ismap (time/today-at-midnight)) {} nil)))
      (is (false? ((ismap (time/now)) {} nil))))
    (testing "with list"
      (is (false? ((ismap '()) {} nil)))
      (is (false? ((ismap '(1 2)) {} nil))))
    (testing "with map"
      (is (true? ((ismap {}) {} nil)))
      (is (true? ((ismap { :a 1 :b 2 }) {} nil)))
      (is (true? ((ismap (json/read-str "{\"a\": 1, \"b\": 2}")) {} nil))))))

(deftest isempty-test
  (let [isempty (partial function-transform :isempty)]
    (testing "with nil"
      (is (true? ((isempty nil) {} nil)))
      (is (true? ((isempty :col1) {:col1 nil} nil))))
    (testing "with keyword"
      (is (true? ((isempty :col1) {:col1 {}} nil)))
      (is (false? ((isempty :col1) {:col1 1.0} nil))))
    (testing "with number"
      (is (true? ((isempty 0) {} nil)))
      (is (false? ((isempty 1) {} nil)))
      (is (false? ((isempty -123) {} nil)))
      (is (false? ((isempty 1.4) {} nil)))
      (is (true? ((isempty 0.0) {} nil))))
    (testing "with string"
      (is (true? ((isempty "") {} nil)))
      (is (true? ((isempty "   ") {} nil)))
      (is (true? ((isempty "\n\t  ") {} nil)))
      (is (false? ((isempty "hello world") {} nil))))
    (testing "with boolean"
      (is (false? ((isempty false) {} nil)))
      (is (false? ((isempty true) {} nil))))
    (testing "with date"
      (is (false? ((isempty (time/today-at-midnight)) {} nil)))
      (is (false? ((isempty (time/now)) {} nil))))
    (testing "with list"
      (is (true? ((isempty '()) {} nil)))
      (is (false? ((isempty '(1 2)) {} nil)))
      (is (true? ((isempty []) {} nil)))
      (is (false? ((isempty [1 2 3]) {} nil))))
    (testing "with map"
      (is (true? ((isempty {}) {} nil)))
      (is (false? ((isempty { :a 1 }) {} nil)))
      (is (false? ((isempty (json/read-str "{\"a\": 1, \"b\": 2}")) {} nil)))
      (is (true? ((isempty (json/read-str "{}")) {} nil))))))
