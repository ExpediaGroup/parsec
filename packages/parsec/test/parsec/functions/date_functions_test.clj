(ns parsec.functions.date_functions-test
  (:require [clojure.test :refer :all]
            [parsec.functions :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.functions.datefunctions :refer :all]
            [clj-time.core :as time :refer [days minutes hours]]
            [clj-time.coerce :refer [to-long to-date-time]]
            [clj-time.coerce :as timec])

  (:import (org.joda.time DateTime Period)))

;; Passing nil as the context argument for most functions, since they don't use it

(defn approx
  "Returns true if the two datetimes are within seconds of each other."
  [x y]
  (let [d1 (to-date-time x)
        d2 (to-date-time y)]
    (> 100 (time/in-millis
             (time/interval
               (time/earliest d1 d2)
               (time/latest d1 d2))))))

(deftest now-test
  (let [now (function-transform :now)]
    (testing "with no args"
      (let [now' (now {} nil)]
        (is (approx (time/now) now'))
        (is (= (time/default-time-zone) (.getZone now')))
        (is (= DateTime (type now')))))))

(deftest nowutc-test
  (let [nowutc (function-transform :nowutc)]
    (testing "with no args"
      (let [nowutc' (nowutc {} nil)]
        (is (approx (time/now) nowutc'))
        (is (= time/utc (.getZone nowutc')))
        (is (= DateTime (type nowutc')))))))

(deftest today-test
  (let [today (function-transform :today)]
    (testing "with no args"
      (let [today' (today {} nil)]
        (is (approx (time/today-at-midnight (time/default-time-zone)) today'))
        (is (= (time/default-time-zone) (.getZone today')))
        (is (= DateTime (type today')))))))

(deftest todayutc-test
  (let [todayutc (function-transform :todayutc)]
    (testing "with no args"
      (let [todayutc' (todayutc {} nil)]
        (is (approx (time/today-at-midnight) todayutc'))
        (is (= time/utc (.getZone todayutc')))
        (is (= DateTime (type todayutc')))))))

(deftest yesterday-test
  (let [yesterday (function-transform :yesterday)]
    (testing "with no args"
      (let [yesterday' (yesterday {} nil)]
        (is (approx (time/minus (time/today-at-midnight (time/default-time-zone)) (time/days 1)) yesterday'))
        (is (= (time/default-time-zone) (.getZone yesterday')))
        (is (= DateTime (type yesterday')))))))

(deftest yesterdayutc-test
  (let [yesterdayutc (function-transform :yesterdayutc)]
    (testing "with no args"
      (let [yesterdayutc' (yesterdayutc {} nil)]
        (is (approx (time/minus (time/today-at-midnight) (time/days 1)) yesterdayutc'))
        (is (= time/utc (.getZone yesterdayutc')))
        (is (= DateTime (type yesterdayutc')))))))

(deftest tomorrow-test
  (let [tomorrow (function-transform :tomorrow)]
    (testing "with no args"
      (let [tomorrow' (tomorrow {} nil)]
        (is (approx (time/plus (time/today-at-midnight (time/default-time-zone)) (time/days 1)) tomorrow'))
        (is (= (time/default-time-zone) (.getZone tomorrow')))
        (is (= DateTime (type tomorrow')))))))

(deftest tomorrowutc-test
  (let [tomorrowutc (function-transform :tomorrowutc)]
    (testing "with no args"
      (let [tomorrowutc' (tomorrowutc {} nil)]
        (is (approx (time/plus (time/today-at-midnight) (time/days 1)) tomorrowutc'))
        (is (= time/utc (.getZone tomorrowutc')))
        (is (= DateTime (type tomorrowutc')))))))

(deftest tolocaltime-test
  (let [tolocaltime (partial function-transform :tolocaltime)
        t1 (time/from-time-zone (time/date-time 2015 04 04 22 45) (time/default-time-zone))
        t1-utc (time/from-time-zone (time/date-time 2015 04 05 05 45) time/utc)
        todaylocal (timec/to-date-time (time/today-at-midnight (time/default-time-zone)))
        nowutc (time/now)
        nowlocal (time/to-time-zone nowutc (time/default-time-zone))
        nownairobi (time/to-time-zone nowutc (time/time-zone-for-id "Africa/Nairobi"))]

    (testing "with utc datetimes"
      (let [t1' ((tolocaltime t1-utc) {} nil)]
        ; TODO: Test is dependent on being run in PST TZ..need to fix
        ;(is (= t1 t1'))
        (is (= (time/default-time-zone) (.getZone t1'))))

      (let [nowlocal' ((tolocaltime nowutc) {} nil)]
        (is (= nowlocal nowlocal'))
        (is (= (time/default-time-zone) (.getZone nowlocal')))))

    (testing "with local datetimes"
      (let [todaylocal' ((tolocaltime todaylocal) {} nil)]
        (is (= todaylocal todaylocal'))
        (is (= (time/default-time-zone) (.getZone todaylocal')))))

    (testing "with other datetimes"
      (let [nownairobi' ((tolocaltime nownairobi) {} nil)]
        (is (= (time/default-time-zone) (.getZone nownairobi')))))

    (testing "with nil"
      (is (nil? ((tolocaltime nil) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((tolocaltime "a") {} nil)))
      (is (thrown? Exception ((tolocaltime true) {} nil))))))

(deftest toutctime-test
  (let [toutctime (partial function-transform :toutctime)
        t1 (time/from-time-zone (time/date-time 2015 04 04 22 45) (time/default-time-zone))
        t1-utc (time/from-time-zone (time/date-time 2015 04 05 05 45) time/utc)
        todayutc (timec/to-date-time (time/today-at-midnight time/utc))
        nowutc (time/now)
        nowlocal (time/to-time-zone (time/now) (time/default-time-zone))
        nairobi-tz (time/time-zone-for-id "Africa/Nairobi")
        nownairobi (time/to-time-zone (time/now) nairobi-tz)]

    (testing "with utc datetimes"
      (let [todayutc' ((toutctime todayutc) {} nil)]
        (is (= todayutc todayutc'))
        (is (= time/utc (.getZone todayutc'))))

      (let [nowutc' ((toutctime nowutc) {} nil)]
        ; TODO: Test is dependent on being run in PST TZ..need to fix
        ;(is (= nowutc nowutc'))
        (is (= time/utc (.getZone nowutc')))))

    (testing "with local datetimes"
      (let [t1' ((toutctime t1) {} nil)]
        ; TODO: Test is dependent on being run in PST TZ..need to fix
        ;(is (= t1-utc t1'))
        (is (= time/utc (.getZone t1'))))
      (let [nowutc' ((toutctime nowlocal) {} nil)]
        (is (= nowutc nowutc'))
        (is (= time/utc (.getZone nowutc')))))

    (testing "with other datetimes"
      (let [nownairobi' ((toutctime nownairobi) {} nil)]
        (is (= nowutc nownairobi'))
        (is (= time/utc (.getZone nownairobi')))))

    (testing "with nil"
      (is (nil? ((toutctime nil) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((toutctime "a") {} nil)))
      (is (thrown? Exception ((toutctime true) {} nil))))))

(deftest totimezone-test
  (let [totimezone (partial function-transform :totimezone)
        pacific-tz (time/time-zone-for-id "America/Los_Angeles")
        indiana-tz (time/time-zone-for-id "America/Indiana/Indianapolis")
        nairobi-tz (time/time-zone-for-id "Africa/Nairobi")
        t1-utc (time/from-time-zone (time/date-time 2015 04 05 05 45) time/utc)
        t1-pacific (time/from-time-zone (time/date-time 2015 04 04 22 45) pacific-tz)
        t1-indiana (time/from-time-zone (time/date-time 2015 04 05 01 45) indiana-tz)
        t1-nairobi (time/from-time-zone (time/date-time 2015 04 05 8 45) nairobi-tz)]

    (testing "with utc to utc"
      (let [t1' ((totimezone t1-utc "UTC") {} nil)]
        (is (= t1-utc t1'))
        (is (= (.getOffset time/utc t1') (.getOffset (.getZone t1') t1')))))

    (testing "with pacific to utc"
      (let [t1' ((totimezone t1-pacific "UTC") {} nil)]
        (is (= t1-utc t1'))
        (is (= (.getOffset time/utc t1') (.getOffset (.getZone t1') t1')))))

    (testing "with pacific to indiana"
      (let [t1' ((totimezone t1-pacific "America/Indiana/Indianapolis") {} nil)]
        (is (= t1-indiana t1'))
        (is (= (.getOffset indiana-tz t1') (.getOffset (.getZone t1') t1')))))

    (testing "with nairobi to pacific"
      (let [t1' ((totimezone t1-nairobi "America/Los_Angeles") {} nil)]
        (is (= t1-pacific t1'))
        (is (= (.getOffset pacific-tz t1') (.getOffset (.getZone t1') t1')))))

    (testing "with nil"
      (is (nil? ((totimezone nil "UTC") {} nil)))
      (is (nil? ((totimezone t1-utc nil) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((totimezone "a" "Zulu") {} nil)))
      (is (thrown? Exception ((totimezone true "Zulu") {} nil))))))

(deftest toepoch-test
  (let [toepoch (partial function-transform :toepoch)
        t1 (time/date-time 2009 2 13 23 31 30 000)
        t2 (time/date-time 2001 9 9 1 46 40 000)
        t3 (time/date-time 2014 6 29 11 13 24 000)]

    (testing "with happy path"
      (is (= 1234567890 ((toepoch t1) {} nil)))
      (is (= 1000000000 ((toepoch t2) {} nil)))
      (is (= 1404040404 ((toepoch t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? IllegalArgumentException ((toepoch "a") {} nil)))
      (is (thrown? IllegalArgumentException ((toepoch true) {} nil))))

    (testing "with nil"
      (is (nil? ((toepoch nil) {} nil))))))

(deftest toepochmillis-test
  (let [toepochmillis (partial function-transform :toepochmillis)
        t1 (time/date-time 2009 2 13 23 31 30 123)
        t2 (time/date-time 2001 9 9 1 46 40 000)
        t3 (time/date-time 2014 6 29 11 13 24 40)]

    (testing "with happy path"
      (is (= 1234567890123 ((toepochmillis t1) {} nil)))
      (is (= 1000000000000 ((toepochmillis t2) {} nil)))
      (is (= 1404040404040 ((toepochmillis t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? IllegalArgumentException ((toepochmillis "a") {} nil)))
      (is (thrown? IllegalArgumentException ((toepochmillis true) {} nil))))

    (testing "with nil"
      (is (nil? ((toepochmillis nil) {} nil))))))

(deftest millisecond-test
  (let [millisecond (partial function-transform :millisecond)
        t1 (time/date-time 2015 01 01 1 15 00 554)
        t2 (time/date-time 2015 02 03 4 00 45 001)
        t3 (time/date-time 2014 10 25 18 02 01 141)]

    (testing "with happy path"
      (is (= 554 ((millisecond t1) {} nil)))
      (is (= 1 ((millisecond t2) {} nil)))
      (is (= 141 ((millisecond t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((millisecond "a") {} nil)))
      (is (thrown? Exception ((millisecond 10) {} nil)))
      (is (thrown? Exception ((millisecond true) {} nil))))

    (testing "with nil"
      (is (nil? ((millisecond nil) {} nil))))))

(deftest millisecondofday-test
  (let [millisecondofday (partial function-transform :millisecondofday)
        t1 (time/date-time 2015 01 01 0 15 00 554)
        t2 (time/date-time 2015 02 01 1 07 45 001)
        t3 (time/date-time 2015 03 01 18 00 01 141)]

    (testing "with happy path"
      (is (= 900554 ((millisecondofday t1) {} nil)))
      (is (= (+ 1 (* 45 1000) (* 67 60000)) ((millisecondofday t2) {} nil)))
      (is (= (+ 1141 (* 18 60 60 1000)) ((millisecondofday t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((millisecondofday "a") {} nil)))
      (is (thrown? Exception ((millisecondofday 10) {} nil)))
      (is (thrown? Exception ((millisecondofday true) {} nil))))

    (testing "with nil"
      (is (nil? ((millisecondofday nil) {} nil))))))

(deftest second-test
  (let [second (partial function-transform :second)
        t1 (time/date-time 2015 01 01 1 15 00 554)
        t2 (time/date-time 2015 02 03 4 00 45 001)
        t3 (time/date-time 2014 10 25 18 02 01 141)]

    (testing "with happy path"
      (is (= 0 ((second t1) {} nil)))
      (is (= 45 ((second t2) {} nil)))
      (is (= 1 ((second t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((second "a") {} nil)))
      (is (thrown? Exception ((second 10) {} nil)))
      (is (thrown? Exception ((second true) {} nil))))

    (testing "with nil"
      (is (nil? ((second nil) {} nil))))))

(deftest secondofday-test
  (let [secondofday (partial function-transform :secondofday)
        t1 (time/date-time 2015 01 01 1 15 00 554)
        t2 (time/date-time 2015 02 03 4 00 45 001)
        t3 (time/date-time 2014 10 25 18 02 01 141)]

    (testing "with happy path"
      (is (= (* 75 60) ((secondofday t1) {} nil)))
      (is (= 14445 ((secondofday t2) {} nil)))
      (is (= 64921 ((secondofday t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((secondofday "a") {} nil)))
      (is (thrown? Exception ((secondofday 10) {} nil)))
      (is (thrown? Exception ((secondofday true) {} nil))))

    (testing "with nil"
      (is (nil? ((secondofday nil) {} nil))))))

(deftest minute-test
  (let [minute (partial function-transform :minute)
        t1 (time/date-time 2015 01 01 1 15 00 554)
        t2 (time/date-time 2015 02 03 4 00 45 001)
        t3 (time/date-time 2014 10 25 18 02 01 141)]

    (testing "with happy path"
      (is (= 15 ((minute t1) {} nil)))
      (is (= 0 ((minute t2) {} nil)))
      (is (= 2 ((minute t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((minute "a") {} nil)))
      (is (thrown? Exception ((minute 10) {} nil)))
      (is (thrown? Exception ((minute true) {} nil))))

    (testing "with nil"
      (is (nil? ((minute nil) {} nil))))))

(deftest minuteofday-test
  (let [minuteofday (partial function-transform :minuteofday)
        t1 (time/date-time 2015 01 01 1 15 00 554)
        t2 (time/date-time 2015 02 03 4 00 45 001)
        t3 (time/date-time 2014 10 25 18 02 01 141)]

    (testing "with happy path"
      (is (= 75 ((minuteofday t1) {} nil)))
      (is (= 240 ((minuteofday t2) {} nil)))
      (is (= 1082 ((minuteofday t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((minuteofday "a") {} nil)))
      (is (thrown? Exception ((minuteofday 10) {} nil)))
      (is (thrown? Exception ((minuteofday true) {} nil))))

    (testing "with nil"
      (is (nil? ((minuteofday nil) {} nil))))))

(deftest hour-test
  (let [hour (partial function-transform :hour)
        t1 (time/date-time 2015 01 01 0 15 00 554)
        t2 (time/date-time 2015 02 03 1 00 45 001)
        t3 (time/date-time 2014 10 25 18 02 01 141)]

    (testing "with happy path"
      (is (= 0 ((hour t1) {} nil)))
      (is (= 1 ((hour t2) {} nil)))
      (is (= 18 ((hour t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((hour "a") {} nil)))
      (is (thrown? Exception ((hour 10) {} nil)))
      (is (thrown? Exception ((hour true) {} nil))))

    (testing "with nil"
      (is (nil? ((hour nil) {} nil))))))

(deftest day-test
  (let [day (partial function-transform :day)
        t1 (time/date-time 2015 01 01 1 15 00 554)
        t2 (time/date-time 2015 02 03 4 00 45 001)
        t3 (time/date-time 2014 10 25 18 02 01 141)]

    (testing "with happy path"
      (is (= 1 ((day t1) {} nil)))
      (is (= 3 ((day t2) {} nil)))
      (is (= 25 ((day t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((day "a") {} nil)))
      (is (thrown? Exception ((day 10) {} nil)))
      (is (thrown? Exception ((day true) {} nil))))

    (testing "with nil"
      (is (nil? ((day nil) {} nil))))))

(deftest dayofweek-test
  (let [dayofweek (partial function-transform :dayofweek)
        t1 (time/date-time 2015 03 28 1 15 00 554)
        t2 (time/date-time 2015 03 29 4 00 45 001)
        t3 (time/date-time 2015 03 30 18 02 01 141)]

    (testing "with happy path"
      (is (= 6 ((dayofweek t1) {} nil)))
      (is (= 7 ((dayofweek t2) {} nil)))
      (is (= 1 ((dayofweek t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((dayofweek "a") {} nil)))
      (is (thrown? Exception ((dayofweek 10) {} nil)))
      (is (thrown? Exception ((dayofweek true) {} nil))))

    (testing "with nil"
      (is (nil? ((dayofweek nil) {} nil))))))

(deftest dayofyear-test
  (let [dayofyear (partial function-transform :dayofyear)
        t1 (time/date-time 2015 01 01 1 15 00 554)
        t2 (time/date-time 2015 03 28 4 00 45 001)
        t3 (time/date-time 2015 12 31 18 02 01 141)]

    (testing "with happy path"
      (is (= 1 ((dayofyear t1) {} nil)))
      (is (= 87 ((dayofyear t2) {} nil)))
      (is (= 365 ((dayofyear t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((dayofyear "a") {} nil)))
      (is (thrown? Exception ((dayofyear 10) {} nil)))
      (is (thrown? Exception ((dayofyear true) {} nil))))

    (testing "with nil"
      (is (nil? ((dayofyear nil) {} nil))))))

(deftest month-test
  (let [month (partial function-transform :month)
        t1 (time/date-time 2015 01 01 1 15 00 554)
        t2 (time/date-time 2015 02 03 4 00 45 001)
        t3 (time/date-time 2014 10 25 18 02 01 141)]

    (testing "with happy path"
      (is (= 1 ((month t1) {} nil)))
      (is (= 2 ((month t2) {} nil)))
      (is (= 10 ((month t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((month "a") {} nil)))
      (is (thrown? Exception ((month 10) {} nil)))
      (is (thrown? Exception ((month true) {} nil))))

    (testing "with nil"
      (is (nil? ((month nil) {} nil))))))

(deftest year-test
  (let [year (partial function-transform :year)
        t1 (time/date-time 1990 01 01 1 15 00 554)
        t2 (time/date-time 2001 02 03 4 00 45 001)
        t3 (time/date-time 2015 10 25 18 02 01 141)]

    (testing "with happy path"
      (is (= 1990 ((year t1) {} nil)))
      (is (= 2001 ((year t2) {} nil)))
      (is (= 2015 ((year t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((year "a") {} nil)))
      (is (thrown? Exception ((year 10) {} nil)))
      (is (thrown? Exception ((year true) {} nil))))

    (testing "with nil"
      (is (nil? ((year nil) {} nil))))))

(deftest adddays-test
  (let [adddays (partial function-transform :adddays)
        now (time/now)]

    (testing "with happy path"
      (is (= (time/plus now (days 1)) ((adddays now 1) {} nil)))
      (is (= now ((adddays now 0.5) {} nil)))               ;; Only whole days are supported
      (is (= (time/minus now (days 1)) ((adddays now -1) {} nil))))

    (testing "with nil"
      (is (nil? ((adddays now nil) {} nil)))
      (is (nil? ((adddays nil 1) {} nil))))))

(deftest minusdays-test
  (let [minusdays (partial function-transform :minusdays)
        now (time/now)]

    (testing "with happy path"
      (is (= (time/minus now (days 1)) ((minusdays now 1) {} nil)))
      (is (= (time/plus now (days 1)) ((minusdays now -1) {} nil))))

    (testing "with nil"
      (is (nil? ((minusdays now nil) {} nil)))
      (is (nil? ((minusdays nil 1) {} nil))))))

(deftest addminutes-test
  (let [addminutes (partial function-transform :addminutes)
        now (time/now)]

    (testing "with happy path"
      (is (= (time/plus now (minutes 1)) ((addminutes now 1) {} nil)))
      (is (= (time/minus now (minutes 1)) ((addminutes now -1) {} nil))))

    (testing "with nil"
      (is (nil? ((addminutes now nil) {} nil)))
      (is (nil? ((addminutes nil 1) {} nil))))))

(deftest minusminutes-test
  (let [minusminutes (partial function-transform :minusminutes)
        now (time/now)]

    (testing "with happy path"
      (is (= (time/minus now (minutes 1)) ((minusminutes now 1) {} nil)))
      (is (= (time/plus now (minutes 1)) ((minusminutes now -1) {} nil))))

    (testing "with nil"
      (is (nil? ((minusminutes now nil) {} nil)))
      (is (nil? ((minusminutes nil 1) {} nil))))))

(deftest earliest-test
  (let [earliest (partial function-transform :earliest)
        now (time/now)
        then (time/plus now (minutes 1))]

    (testing "with happy path"
      (is (= now ((earliest now then) {} nil)))
      (is (= now ((earliest then now) {} nil))))

    (testing "with incompatible types"
      (is (nil? ((earliest now 20) {} nil)))
      (is (nil? ((earliest 3.14 now) {} nil)))
      (is (nil? ((earliest now "hello") {} nil)))
      (is (nil? ((earliest "hello" now) {} nil))))

    (testing "with nil"
      (is (nil? ((earliest now nil) {} nil)))
      (is (nil? ((earliest nil 1) {} nil))))))

(deftest latest-test
  (let [latest (partial function-transform :latest)
        now (time/now)
        then (time/plus now (minutes 1))]

    (testing "with happy path"
      (is (= then ((latest now then) {} nil)))
      (is (= then ((latest then now) {} nil))))

    (testing "with incompatible types"
      (is (nil? ((latest now 20) {} nil)))
      (is (nil? ((latest 3.14 now) {} nil)))
      (is (nil? ((latest now "hello") {} nil)))
      (is (nil? ((latest "hello" now) {} nil))))

    (testing "with nil"
      (is (nil? ((latest now nil) {} nil)))
      (is (nil? ((latest nil 1) {} nil))))))

(deftest interval-test
  (let [intervalfn (partial function-transform :interval)
        now (time/now)
        interval0 (time/interval now now)
        interval1 (time/interval (time/today-at-midnight) now)]
    (testing "with happy path"
      (is (= interval0 ((intervalfn now now) {} nil)))
      (is (= interval1 ((intervalfn (time/today-at-midnight) now) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((intervalfn 1 0) {} nil)))
      (is (thrown? Exception ((intervalfn "a" "b") {} nil)))
      (is (thrown? Exception ((intervalfn true false) {} nil)))
      (is (thrown? Exception ((intervalfn 2 (time/now)) {} nil))))

    (testing "with nil"
      (is (nil? ((intervalfn nil (time/now)) {} nil)))
      (is (nil? ((intervalfn (time/now) nil) {} nil))))))

(deftest inmillis-test
  (let [inmillis (partial function-transform :inmillis)
        now (time/now)
        interval0 (time/interval now now)
        interval1 (time/interval now (time/plus now (time/millis 101)))]
    (testing "with happy path"
      (is (zero? ((inmillis interval0) {} nil)))
      (is (= 101 ((inmillis interval1) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((inmillis 20) {} nil)))
      (is (thrown? Exception ((inmillis "string") {} nil)))
      (is (thrown? Exception ((inmillis true) {} nil)))
      (is (thrown? Exception ((inmillis now) {} nil))))

    (testing "with nil"
      (is (nil? ((inmillis nil) {} nil))))))

(deftest inseconds-test
  (let [inseconds (partial function-transform :inseconds)
        now (time/now)
        interval (time/interval now (time/plus now (time/seconds 101)))]
    (testing "with happy path"
      (is (= 101 ((inseconds interval) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((inseconds 20) {} nil)))
      (is (thrown? Exception ((inseconds "string") {} nil)))
      (is (thrown? Exception ((inseconds true) {} nil)))
      (is (thrown? Exception ((inseconds now) {} nil))))

    (testing "with nil"
      (is (nil? ((inseconds nil) {} nil))))))

(deftest inminutes-test
  (let [inminutes (partial function-transform :inminutes)
        now (time/now)
        interval (time/interval now (time/plus (time/now) (time/minutes 101)))]
    (testing "with happy path"
      (is (= 101 ((inminutes interval) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((inminutes 20) {} nil)))
      (is (thrown? Exception ((inminutes "string") {} nil)))
      (is (thrown? Exception ((inminutes true) {} nil)))
      (is (thrown? Exception ((inminutes (time/now)) {} nil))))

    (testing "with nil"
      (is (nil? ((inminutes nil) {} nil))))))

(deftest inhours-test
  (let [inhours (partial function-transform :inhours)
        now (time/now)
        interval0 (time/interval now now)
        interval1 (time/interval now (time/plus now (time/hours 101)))
        interval2 (time/interval now
                                 (time/plus
                                   (time/plus now (time/hours 30))
                                   (time/minutes 10)))]
    (testing "with happy path"
      (is (zero? ((inhours interval0) {} nil)))
      (is (= 101 ((inhours interval1) {} nil)))
      (is (= 30 ((inhours interval2) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((inhours 20) {} nil)))
      (is (thrown? Exception ((inhours "string") {} nil)))
      (is (thrown? Exception ((inhours true) {} nil)))
      (is (thrown? Exception ((inhours now) {} nil))))

    (testing "with nil"
      (is (nil? ((inhours nil) {} nil))))))

(deftest indays-test
  (let [indays (partial function-transform :indays)
        now (time/now)
        interval (time/interval now (time/plus now (time/days 101)))]
    (testing "with happy path"
      (is (= 101 ((indays interval) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((indays 20) {} nil)))
      (is (thrown? Exception ((indays "string") {} nil)))
      (is (thrown? Exception ((indays true) {} nil)))
      (is (thrown? Exception ((indays now) {} nil))))

    (testing "with nil"
      (is (nil? ((indays nil) {} nil))))))

(deftest inweeks-test
  (let [inweeks (partial function-transform :inweeks)
        now (time/now)
        interval0 (time/interval now now)
        interval1 (time/interval now (time/plus now (time/weeks 12)))]
    (testing "with happy path"
      (is (zero? ((inweeks interval0) {} nil)))
      (is (= 12 ((inweeks interval1) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((inweeks 20) {} nil)))
      (is (thrown? Exception ((inweeks "string") {} nil)))
      (is (thrown? Exception ((inweeks true) {} nil)))
      (is (thrown? Exception ((inweeks now) {} nil))))

    (testing "with nil"
      (is (nil? ((inweeks nil) {} nil))))))

(deftest inmonths-test
  (let [inmonths (partial function-transform :inmonths)
        now (time/now)
        interval0 (time/interval now now)
        interval1 (time/interval now (time/plus now (time/months 9)))
        interval2 (time/interval now (time/plus now (time/days 35)))]
    (testing "with happy path"
      (is (zero? ((inmonths interval0) {} nil)))
      (is (= 9 ((inmonths interval1) {} nil)))
      (is (= 1 ((inmonths interval2) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((inmonths 20) {} nil)))
      (is (thrown? Exception ((inmonths "string") {} nil)))
      (is (thrown? Exception ((inmonths true) {} nil)))
      (is (thrown? Exception ((inmonths now) {} nil))))

    (testing "with nil"
      (is (nil? ((inmonths nil) {} nil))))))

(deftest inyears-test
  (let [inyears (partial function-transform :inyears)
        now (time/now)
        interval0 (time/interval now now)
        interval1 (time/interval now (time/plus now (time/years 19)))
        interval2 (time/interval now (time/plus now (time/days 366)))]
    (testing "with happy path"
      (is (zero? ((inyears interval0) {} nil)))
      (is (= 19 ((inyears interval1) {} nil)))
      (is (= 1 ((inyears interval2) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((inyears 20) {} nil)))
      (is (thrown? Exception ((inyears "string") {} nil)))
      (is (thrown? Exception ((inyears true) {} nil)))
      (is (thrown? Exception ((inyears now) {} nil))))

    (testing "with nil"
      (is (nil? ((inyears nil) {} nil))))))

(deftest isbetween-test
  (let [isbetween (partial function-transform :isbetween)
        now (time/now)
        t1 (time/plus now (minutes 1))
        t2 (time/minus now (minutes 1))
        t3 (time/minus now (minutes 60))
        t4 (time/minus now (hours 10))]

    (testing "with happy path"
      (is (true? ((isbetween now t2 t1) {} nil)))
      (is (false? ((isbetween now t4 t3) {} nil)))
      (is (true? ((isbetween t3 t4 t2) {} nil)))
      (is (false? ((isbetween t4 now t1) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((isbetween 10 20 30) {} nil)))
      (is (thrown? Exception ((isbetween "x" "y" "z") {} nil)))
      (is (thrown? Exception ((isbetween true false false) {} nil))))

    (testing "with nil"
      (is (false? ((isbetween nil t1 t2) {} nil))))))

(deftest startofminute-test
  (let [startofminute (partial function-transform :startofminute)
        t1 (time/date-time 2015 01 01 1 15 00 554)
        t2 (time/date-time 2015 02 03 4 00 45 001)
        t3 (time/date-time 2014 10 25 18 02 01 141)]

    (testing "with happy path"
      (is (= (time/date-time 2015 01 01 1 15 00 000) ((startofminute t1) {} nil)))
      (is (= (time/date-time 2015 02 03 4 00 00 000) ((startofminute t2) {} nil)))
      (is (= (time/date-time 2014 10 25 18 02 00 00) ((startofminute t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((startofminute "a") {} nil)))
      (is (thrown? Exception ((startofminute 10) {} nil)))
      (is (thrown? Exception ((startofminute true) {} nil))))

    (testing "with nil"
      (is (nil? ((startofminute nil 1) {} nil))))))

(deftest startofhour-test
  (let [startofhour (partial function-transform :startofhour)
        t1 (time/date-time 2015 01 01 1 15 00 554)
        t2 (time/date-time 2015 02 03 4 00 45 001)
        t3 (time/date-time 2014 10 25 18 02 01 141)]

    (testing "with happy path"
      (is (= (time/date-time 2015 01 01 1 00 00 000) ((startofhour t1) {} nil)))
      (is (= (time/date-time 2015 02 03 4 00 00 000) ((startofhour t2) {} nil)))
      (is (= (time/date-time 2014 10 25 18 00 00 000) ((startofhour t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((startofhour "a") {} nil)))
      (is (thrown? Exception ((startofhour 10) {} nil)))
      (is (thrown? Exception ((startofhour true) {} nil))))

    (testing "with nil"
      (is (nil? ((startofhour nil 1) {} nil))))))

(deftest startofday-test
  (let [startofday (partial function-transform :startofday)
        t1 (time/date-time 2015 01 01 1 15 00 554)
        t2 (time/date-time 2015 02 03 4 00 45 001)
        t3 (time/date-time 2014 10 25 18 02 01 141)]

    (testing "with happy path"
      (is (= (time/date-time 2015 01 01 0 00 00 000) ((startofday t1) {} nil)))
      (is (= (time/date-time 2015 02 03 0 00 00 000) ((startofday t2) {} nil)))
      (is (= (time/date-time 2014 10 25 0 00 00 000) ((startofday t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((startofday "a") {} nil)))
      (is (thrown? Exception ((startofday 10) {} nil)))
      (is (thrown? Exception ((startofday true) {} nil))))

    (testing "with nil"
      (is (nil? ((startofday nil 1) {} nil))))))

(deftest startofweek-test
  (let [startofweek (partial function-transform :startofweek)
        t1 (time/date-time 2015 01 01 1 15 00 554)
        t2 (time/date-time 2015 02 03 4 00 45 001)
        t3 (time/date-time 2014 10 26 18 02 01 141)
        t4 (time/date-time 2014 10 20 0 02 01 141)]

    (testing "with happy path"
      (is (= (time/date-time 2014 12 29 0 00 00 000) ((startofweek t1) {} nil)))
      (is (= (time/date-time 2015 02 02 0 00 00 000) ((startofweek t2) {} nil)))
      (is (= (time/date-time 2014 10 20 0 00 00 000) ((startofweek t3) {} nil)))
      (is (= (time/date-time 2014 10 20 0 00 00 000) ((startofweek t4) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((startofweek "a") {} nil)))
      (is (thrown? Exception ((startofweek 10) {} nil)))
      (is (thrown? Exception ((startofweek true) {} nil))))

    (testing "with nil"
      (is (nil? ((startofweek nil 1) {} nil))))))

(deftest startofmonth-test
  (let [startofmonth (partial function-transform :startofmonth)
        t1 (time/date-time 2015 01 01 1 15 00 554)
        t2 (time/date-time 2015 02 03 4 00 45 001)
        t3 (time/date-time 2014 10 25 18 02 01 141)]

    (testing "with happy path"
      (is (= (time/date-time 2015 01 01 0 00 00 000) ((startofmonth t1) {} nil)))
      (is (= (time/date-time 2015 02 01 0 00 00 000) ((startofmonth t2) {} nil)))
      (is (= (time/date-time 2014 10 01 0 00 00 000) ((startofmonth t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((startofmonth "a") {} nil)))
      (is (thrown? Exception ((startofmonth 10) {} nil)))
      (is (thrown? Exception ((startofmonth true) {} nil))))

    (testing "with nil"
      (is (nil? ((startofmonth nil 1) {} nil))))))

(deftest startofyear-test
  (let [startofyear (partial function-transform :startofyear)
        t1 (time/date-time 1985 12 21 23 15 59 554)
        t2 (time/date-time 2015 02 03 14 00 45 001)
        t3 (time/date-time 2014 10 25 18 02 01 141)]

    (testing "with happy path"
      (is (= (time/date-time 1985 01 01 0 00 00 000) ((startofyear t1) {} nil)))
      (is (= (time/date-time 2015 01 01 0 00 00 000) ((startofyear t2) {} nil)))
      (is (= (time/date-time 2014 01 01 0 00 00 000) ((startofyear t3) {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((startofyear "a") {} nil)))
      (is (thrown? Exception ((startofyear 10) {} nil)))
      (is (thrown? Exception ((startofyear true) {} nil))))

    (testing "with nil"
      (is (nil? ((startofyear nil 1) {} nil))))))

(deftest period-test
  (let [periodfn (partial function-transform :period)]
    (testing "with happy path"
      (is (instance? Period ((periodfn 1 "milliseconds") {} nil)))
      (is (instance? Period ((periodfn 2 "millisecond") {} nil)))
      (is (instance? Period ((periodfn 3 "millis") {} nil)))
      (is (time/seconds? ((periodfn 1 "seconds") {} nil)))
      (is (time/seconds? ((periodfn 2 "second") {} nil)))
      (is (time/seconds? ((periodfn 3 "sec") {} nil)))
      (is (time/minutes? ((periodfn 1 "minutes") {} nil)))
      (is (time/minutes? ((periodfn 2 "minute") {} nil)))
      (is (time/minutes? ((periodfn 3 "min") {} nil)))
      (is (time/hours? ((periodfn 1 "hours") {} nil)))
      (is (time/hours? ((periodfn 2 "hour") {} nil)))
      (is (time/hours? ((periodfn 3 "hr") {} nil)))
      (is (time/days? ((periodfn 1 "days") {} nil)))
      (is (time/days? ((periodfn 2 "day") {} nil)))
      (is (time/weeks? ((periodfn 1 "weeks") {} nil)))
      (is (time/weeks? ((periodfn 2 "week") {} nil)))
      (is (time/months? ((periodfn 1 "months") {} nil)))
      (is (time/months? ((periodfn 2 "month") {} nil)))
      (is (time/years? ((periodfn 1 "years") {} nil)))
      (is (time/years? ((periodfn 2 "year") {} nil)))
      (is (= (time/minutes 30) ((periodfn 30 "minutes") {} nil))))

    (testing "with incompatible types"
      (is (thrown? Exception ((periodfn 1 0) {} nil)))
      (is (thrown? Exception ((periodfn 2 "b") {} nil)))
      (is (thrown? Exception ((periodfn 3 false) {} nil)))
      (is (thrown? Exception ((periodfn 4 (time/now)) {} nil))))

    (testing "with nil"
      (is (thrown? Exception ((periodfn nil "seconds") {} nil)))
      (is (thrown? Exception ((periodfn 5 nil) {} nil))))))
