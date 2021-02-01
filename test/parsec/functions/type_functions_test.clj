(ns parsec.functions.type-functions-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.functions :refer :all]
            [clj-time.core :as time]
            [clj-time.coerce :refer [to-long to-date-time]]
            [clj-time.format :as timef]
            [parsec.functions.typefunctions :refer :all]))

(deftest type-test
  (let [type (partial function-transform :type)]
    (testing "with nil"
      (is (nil? ((type nil) {} nil)))
      (is (nil? ((type :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (= "java.lang.Long" ((type (long 0)) {} nil)))
      (is (= "java.lang.Integer" ((type (int 1)) {} nil)))
      (is (= "java.lang.Double" ((type 1.0) {} nil)))
      (is (= "clojure.lang.Ratio" ((type 1/2) {} nil))))
    (testing "with string"
      (is (= "java.lang.String" ((type "hello world") {} nil))))
    (testing "with boolean"
      (is (= "java.lang.Boolean" ((type false) {} nil)))
      (is (= "java.lang.Boolean" ((type true) {} nil))))
    (testing "with date"
      (is (= "org.joda.time.DateMidnight" ((type (time/today-at-midnight)) {} nil)))
      (is (= "org.joda.time.DateTime" ((type (time/now)) {} nil))))
    (testing "with list"
      (is (= "clojure.lang.PersistentList$EmptyList" ((type '()) {} nil)))
      (is (= "clojure.lang.PersistentList" ((type '(1 2)) {} nil)))
      (is (= "clojure.lang.PersistentList" ((type '(nil true 1 "x")) {} nil))))
    ))

(deftest toboolean-test
  (let [toboolean (partial function-transform :toboolean)]
    (testing "with string"
      (is (nil? ((toboolean "abc") {} nil)))
      (is (nil? ((toboolean "") {} nil))))

    (testing "with number"
      (is (false? ((toboolean 0) {} nil)))
      (is (false? ((toboolean 0.0) {} nil)))
      (is (true? ((toboolean 50) {} nil)))
      (is (true? ((toboolean 3/4) {} nil)))
      (is (true? ((toboolean 3.14) {} nil)))
      (is (true? ((toboolean Long/MAX_VALUE) {} nil))))

    (testing "with boolean"
      (is (true? ((toboolean true) {} nil)))
      (is (false? ((toboolean false) {} nil))))

    (testing "with nil"
      (is (nil? ((toboolean nil) {} nil))))

    (testing "with object"
      (is (nil? ((toboolean { }) {} nil)))
      (is (nil? ((toboolean { :a 1 :b 2 }) {} nil))))

    (testing "with list"
      (is (nil? ((toboolean []) {} nil)))
      (is (nil? ((toboolean [1 2 3]) {} nil)))
      (is (nil? ((toboolean [true -4 "x"]) {} nil))))

    (testing "with keyword"
      (is (true? ((toboolean :col1) {:col1 "TRUE"} nil))))))

(deftest tostring-test
  (let [tostring (partial function-transform :tostring)
        hongkong-tz (time/time-zone-for-id "Asia/Hong_Kong")
        utc-time (time/date-time 2015 1 1 12 23 34 999)
        hk-time (time/to-time-zone utc-time hongkong-tz)]
    (testing "with string"
      (is (= "abc" ((tostring "abc") {} nil)))
      (is (= "" ((tostring "") {} nil))))

    (testing "with number"
      (is (= "50" ((tostring 50) {} nil)))
      (is (= "0.75" ((tostring 3/4) {} nil)))
      (is (= "3.14" ((tostring 3.14) {} nil)))
      (is (= (str Long/MAX_VALUE) ((tostring Long/MAX_VALUE) {} nil))))

    (testing "with boolean"
      (is (= "true" ((tostring true) {} nil)))
      (is (= "false" ((tostring false) {} nil))))

    (testing "with nil"
      (is (nil? ((tostring nil) {} nil))))

    (testing "with map"
      (is (= "{}" ((tostring { }) {} nil)))
      (is (= "{\"a\":1,\"b\":2}" ((tostring { :a 1 :b 2 }) {} nil))))

    (testing "with list"
      (is (= "[]" ((tostring []) {} nil)))
      (is (= "[1,2,3]" ((tostring [1 2 3]) {} nil)))
      (is (= "[true,-4,\"x\"]" ((tostring [true -4 "x"]) {} nil))))

    (testing "with keyword"
      (is (= "5" ((tostring :col1) {:col1 5} nil))))

    (testing "with UTC datetime"
      (is (= "2015-01-01T12:23:34.999Z" ((tostring utc-time) {} nil)))
      (is (= "2015-01-01" ((tostring utc-time "yyyy-MM-dd") {} nil)))
      (is (= "12:23:34.999" ((tostring utc-time "HH:mm:ss.SSS") {} nil))))

    (testing "with local datetime"
      (is (= "2015-01-01T20:23:34.999+08:00" ((tostring hk-time) {} nil)))
      (is (= "2015-01-01" ((tostring hk-time "yyyy-MM-dd") {} nil)))
      (is (= "20:23:34.999" ((tostring hk-time "HH:mm:ss.SSS") {} nil))))))

(deftest tonumber-test
  (let [tonumber (partial function-transform :tonumber)]
    (testing "with string"
      (is (= 0 ((tonumber "0") {} nil)))
      (is (= 1 ((tonumber "1") {} nil)))
      (is (= -1 ((tonumber "-1") {} nil)))
      (is (= 1/2 ((tonumber ".5") {} nil)))                 ; missing 0
      (is (= 10 ((tonumber "010") {} nil)))                 ; avoid octal
      (is (= 20 ((tonumber "0x000014") {} nil)))            ; hexadecimal
      (is (= 157/50 ((tonumber "3.14") {} nil)))
      (is (= 3/4 ((tonumber "3/4") {} nil)))
      (is (nil? ((tonumber "abc") {} nil)))
      (is (nil? ((tonumber "4.5L") {} nil))))

    (testing "with empty string"
      (is (nil? ((tonumber "") {} nil))))

    (testing "with number"
      (is (= 0 ((tonumber 0) {} nil)))
      (is (= 50 ((tonumber 50) {} nil)))
      (is (= 3.14 ((tonumber 3.14) {} nil))))

    (testing "with boolean"
      (is (nil? ((tonumber true) {} nil)))
      (is (nil? ((tonumber false) {} nil))))

    (testing "with datetime"
      (is (= 1427501340000 ((tonumber (time/from-time-zone (time/date-time 2015 3 27 17 9 0 0) (time/time-zone-for-offset -7))) {} nil)))
      (is (= 1420115014999 ((tonumber (time/date-time 2015 1 1 12 23 34 999)) {} nil))))

    (testing "with nil"
      (is (nil? ((tonumber nil) {} nil))))

    (testing "with keyword"
      (is (= 400 ((tonumber :col1) {:col1 "400"} nil))))))

(deftest tointeger-test
  (let [tointeger (partial function-transform :tointeger)]
    (testing "with string"
      (is (= 0 ((tointeger "0") {} nil)))
      (is (= 1 ((tointeger "1") {} nil)))
      (is (= 1 ((tointeger "1.0") {} nil)))
      (is (= 2 ((tointeger "2.1") {} nil)))
      (is (= -1 ((tointeger "-1.0001") {} nil)))
      (is (= 0 ((tointeger "0.99") {} nil)))                 ; truncate decimal
      (is (= 0 ((tointeger "0.5") {} nil)))                  ; truncate decimal
      (is (= 0 ((tointeger "0.45") {} nil)))                 ; truncate decimal
      (is (= -10 ((tointeger "-10.5") {} nil)))                   ; missing 0
      (is (= 10 ((tointeger "010") {} nil)))                 ; avoid octal
      (is (= 20 ((tointeger "0x000014") {} nil)))            ; hexadecimal
      (is (= 3 ((tointeger "3.1415926535897932384") {} nil)))
      (is (= 0 ((tointeger "9/10") {} nil)))                 ; truncate decimal / round down
      (is (nil? ((tointeger "abc") {} nil)))
      (is (nil? ((tointeger "4.5L") {} nil))))

    (testing "with empty string"
      (is (nil? ((tointeger "") {} nil))))

    (testing "with number"
      (is (= 0 ((tointeger 0) {} nil)))
      (is (= 50 ((tointeger 50.1) {} nil)))
      (is (= 3 ((tointeger 3.14) {} nil))))

    (testing "with boolean"
      (is (nil? ((tointeger true) {} nil)))
      (is (nil? ((tointeger false) {} nil))))

    (testing "with datetime"
      (is (= 1427501340000 ((tointeger (time/from-time-zone (time/date-time 2015 3 27 17 9 0 0) (time/time-zone-for-offset -7))) {} nil)))
      (is (= 1420115014999 ((tointeger (time/date-time 2015 1 1 12 23 34 999)) {} nil))))

    (testing "with nil"
      (is (nil? ((tointeger nil) {} nil))))

    (testing "with keyword"
      (is (= 400 ((tointeger :col1) {:col1 "400.3"} nil))))))

(deftest todouble-test
  (let [todouble (partial function-transform :todouble)]
    (testing "with string"
      (is (= 0.0 ((todouble "0") {} nil)))
      (is (= 1.0 ((todouble "1") {} nil)))
      (is (= -1.0 ((todouble "-1") {} nil)))
      (is (= 0.5 ((todouble ".5") {} nil)))                 ; missing 0
      (is (= 10.0 ((todouble "010") {} nil)))               ; avoid octal
      (is (= 20.0 ((todouble "0x000014") {} nil)))          ; hexadecimal
      (is (= 3.141592653589793 ((todouble "3.1415926535897932384") {} nil)))
      (is (= 0.75 ((todouble "3/4") {} nil)))
      (is (nil? ((todouble "abc") {} nil)))
      (is (nil? ((todouble "4.5L") {} nil))))

    (testing "with empty string"
      (is (nil? ((todouble "") {} nil))))

    (testing "with number"
      (is (= 0.0 ((todouble 0) {} nil)))
      (is (= 50.0 ((todouble 50) {} nil)))
      (is (= 3.14 ((todouble 3.14) {} nil))))

    (testing "with boolean"
      (is (nil? ((todouble true) {} nil)))
      (is (nil? ((todouble false) {} nil))))

    (testing "with datetime"
      (is (= 1427501340000.0 ((todouble (time/from-time-zone (time/date-time 2015 3 27 17 9 0 0) (time/time-zone-for-offset -7))) {} nil)))
      (is (= 1420115014999.0 ((todouble (time/date-time 2015 1 1 12 23 34 999)) {} nil))))

    (testing "with nil"
      (is (nil? ((todouble nil) {} nil))))

    (testing "with keyword"
      (is (= 400.0 ((todouble :col1) {:col1 "400"} nil))))))

(deftest todate-test
  (let [todate (partial function-transform :todate)
        today (time/today-at-midnight)
        todayepoch (to-long today)
        todaybigint (bigint todayepoch)]

    (testing "with string and format"
      (is (nil? ((todate "abc" "yyyy-MM-dd") {} nil)))
      (is (nil? ((todate "" "yyyy-MM-dd") {} nil)))
      (is (= (timef/parse "2015-03-25") ((todate "2015-03-25" "yyyy-MM-dd") {} nil)))
      (is (= (timef/parse (timef/formatter "dd-MM-yyyy") "02-04-2014") ((todate "02-04-2014" "dd-MM-yyyy") {} nil))))

    (testing "with string no format"
      (is (= (timef/parse "2015-03-24") ((todate "2015-03-24") {} nil)))
      (is (= (timef/parse "2015-03-24T02:02:02.222Z") ((todate "2015-03-24T02:02:02.222Z") {} nil)))
      (is (= (timef/parse "2015-04-04 4:00:00") ((todate "2015-04-04 4:00:00") {} nil))))

    (testing "with number"
      (is (= today ((todate todayepoch "yyyy-MM-dd") {} nil)))
      (is (= today ((todate todaybigint "yyyy-MM-dd") {} nil))))

    (testing "with boolean"
      (is (nil? ((todate true "yyyy-MM-dd") {} nil)))
      (is (nil? ((todate false "yyyy-MM-dd") {} nil))))

    (testing "with nil"
      (is (nil? ((todate nil "yyyy-MM-dd") {} nil))))))

(deftest tolist-test
  (let [tolist (partial function-transform :tolist)]
    (testing "with one arg"
      (is (= '(nil) ((tolist nil) {} nil)))
      (is (= '(1) ((tolist 1) {} nil)))
      (is (= '("a") ((tolist "a") {} nil))))

    (testing "with multiple args"
      (is (= '("a" "b") ((tolist "a" "b") {} nil)))
      (is (= '("a" "b" "c" "d") ((tolist "a" "b" "c" "d") {} nil)))
      (is (= '(1 true nil "x") ((tolist 1 true nil "x") {} nil))))

    (testing "with keywords args"
      (is (= '(1 2) ((tolist :x :y) {:x 1 :y 2} nil)))
      (is (= '(false "A" false) ((tolist :y :x :y) {:x "A" :y false} nil))))))

(deftest tomap-test
  (let [tomap (partial function-transform :tomap)]

    (testing "with one key/value pair"
      (is (= {:a "b"} ((tomap "a" "b") {} nil)))
      (is (= {:a 1} ((tomap "a" 1) {} nil)))
      (is (= {:hello true} ((tomap "hello" true) {} nil))))

    (testing "with multiple key/value pairs"
      (is (= {:a "b" :c "d"} ((tomap "a" "b" "c" "d") {} nil)))
      (is (= {:a1 true :b2 false} ((tomap "a1" always-predicate "b2" false) {} nil))))))
