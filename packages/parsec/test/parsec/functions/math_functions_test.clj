(ns parsec.functions.math-functions-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.functions :refer :all]
            [parsec.functions.mathfunctions :refer :all]))

(deftest e-test
  (let [e (partial function-transform :e)]
    (testing "with no arguments"
      (is (= Math/E ((e) {} nil))))))

(deftest pi-test
  (let [pi (partial function-transform :pi)]
    (testing "with no arguments"
      (is (= Math/PI ((pi) {} nil))))))

(deftest nan-test
  (let [nan (partial function-transform :nan)]
    (testing "with no arguments"
      (is (Double/isNaN ((nan) {} nil))))))

(deftest infinity-test
  (let [infinity (partial function-transform :infinity)]
    (testing "with no arguments"
      (is (Double/isInfinite ((infinity) {} nil))))))

(deftest neginfinity-test
  (let [neginfinity (partial function-transform :neginfinity)]
    (testing "with no arguments"
      (is (Double/isInfinite ((neginfinity) {} nil)))
      (is (> 0 ((neginfinity) {} nil))))))

(deftest round-test
  (let [round (partial function-transform :round)]
    (testing "with 0 precision"
      (is (= 0 ((round 0 0) {} nil)))
      (is (= 2 ((round 2.1 0) {} nil))))

    (testing "with precision"
      (is (= 2 ((round 2.129 0) {} nil)))
      (is (= 2.5 ((round 2.545 1) {} nil)))
      (is (= 2.55 ((round 2.549 2) {} nil))))

    (testing "with more precision than available"
      (is (= 3.14 ((round 3.14 5) {} nil))))

    (testing "with negative precision"
      (is (= 0.0 ((round 2.25 -2) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((round Double/NaN 2) {} nil)))
      (is (Double/isNaN ((round 2 Double/NaN) {} nil)))
      (is (= 2.45562 ((round 2.45562 Double/POSITIVE_INFINITY) {} nil)))
      (is (= 0.0 ((round 2 Double/NEGATIVE_INFINITY) {} nil)))
      (is (Double/isInfinite ((round Double/POSITIVE_INFINITY 2) {} nil)))
      (is (Double/isInfinite ((round Double/NEGATIVE_INFINITY 2) {} nil))))

    (testing "with keyword"
      (is (= 3 ((round :col1 0) {:col1 3.4} nil))))

    (testing "with nil"
      (is (nil? ((round nil 0) {} nil)))
      (is (nil? ((round 0 nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((round true 0) {} nil)))
      (is (thrown? Exception ((round "hello" 0) {} nil))))))

(deftest floor-test
  (let [floor (partial function-transform :floor)]
    (testing "with number"
      (is (= 0 ((floor 0) {} nil)))
      (is (= 2.0 ((floor 2.1) {} nil)))
      (is (= 2.0 ((floor 2.5) {} nil)))
      (is (= 2.0 ((floor 2.9) {} nil)))
      (is (= 1N ((floor 9/5) {} nil))))

    (testing "with keyword"
      (is (= 3.0 ((floor :col1) {:col1 3.99} nil))))

    (testing "with nil"
      (is (nil? ((floor nil) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((floor Double/NaN) {} nil)))
      (is (Double/isInfinite ((floor Double/POSITIVE_INFINITY) {} nil)))
      (is (Double/isInfinite ((floor Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((floor true) {} nil)))
      (is (thrown? Exception ((floor "hello") {} nil))))))

(deftest ceil-test
  (let [ceil (partial function-transform :ceil)]
    (testing "with number"
      (is (= 0 ((ceil 0) {} nil)))
      (is (= 3.0 ((ceil 2.1) {} nil)))
      (is (= 3.0 ((ceil 2.5) {} nil)))
      (is (= 3.0 ((ceil 2.9) {} nil)))
      (is (= 2N ((ceil 9/5) {} nil))))

    (testing "with keyword"
      (is (= 4.0 ((ceil :col1) {:col1 3.01} nil))))

    (testing "with nil"
      (is (nil? ((ceil nil) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((ceil Double/NaN) {} nil)))
      (is (Double/isInfinite ((ceil Double/POSITIVE_INFINITY) {} nil)))
      (is (Double/isInfinite ((ceil Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((ceil true) {} nil)))
      (is (thrown? Exception ((ceil "hello") {} nil))))))

(deftest abs-test
  (let [abs (partial function-transform :abs)]
    (testing "with number"
      (is (= 0 ((abs 0) {} nil)))
      (is (= 1 ((abs -1) {} nil)))
      (is (= 2.0 ((abs -2.0) {} nil)))
      (is (= 5 ((abs 5) {} nil)))
      (is (= 9/5 ((abs -9/5) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((abs Double/NaN) {} nil)))
      (is (Double/isInfinite ((abs Double/POSITIVE_INFINITY) {} nil)))
      (is (Double/isInfinite ((abs Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with keyword"
      (is (= 10.0 ((abs :col1) {:col1 -10.0} nil))))

    (testing "with nil"
      (is (nil? ((abs nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((abs true) {} nil)))
      (is (thrown? Exception ((abs "hello") {} nil))))))

(deftest sign-test
  (let [sign (partial function-transform :sign)]
    (testing "with number"
      (is (= 0 ((sign 0) {} nil)))
      (is (= 1 ((sign 2) {} nil)))
      (is (= -1 ((sign -2.0) {} nil)))
      (is (= 1 ((sign 5) {} nil)))
      (is (= -1 ((sign -9/5) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((sign Double/NaN) {} nil)))
      (is (= 1 ((sign Double/POSITIVE_INFINITY) {} nil)))
      (is (= -1 ((sign Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with keyword"
      (is (= -1 ((sign :col1) {:col1 -10.0} nil))))

    (testing "with nil"
      (is (nil? ((sign nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((sign true) {} nil)))
      (is (thrown? Exception ((sign "hello") {} nil))))))

(deftest sqrt-test
  (let [sqrt (partial function-transform :sqrt)]
    (testing "with number"
      (is (= 0 ((sqrt 0) {} nil)))
      (is (= 1 ((sqrt 1) {} nil)))
      (is (Double/isNaN ((sqrt -1) {} nil)))
      (is (= 2 ((sqrt 4) {} nil)))
      (is (= 12.24744871391589 ((sqrt 150) {} nil))))

    (testing "with nil"
      (is (nil? ((sqrt nil) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((sqrt Double/NaN) {} nil)))
      (is (Double/isInfinite ((sqrt Double/POSITIVE_INFINITY) {} nil)))
      (is (Double/isNaN ((sqrt Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with keyword"
      (is (= 10.0 ((sqrt :col1) {:col1 100.0} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((sqrt true) {} nil)))
      (is (thrown? Exception ((sqrt "hello") {} nil))))))

(deftest pow-test
  (let [pow (partial function-transform :pow)]
    (testing "with zero base"
      (is (= 1 ((pow 0 0) {} nil)))
      (is (= 0 ((pow 0 1) {} nil)))
      (is (= 0 ((pow 0 2) {} nil))))

    (testing "with number"
      (is (= 1 ((pow 1 2) {} nil)))
      (is (= 4 ((pow 2 2) {} nil)))
      (is (= 4 ((pow -2 2) {} nil)))
      (is (= 8 ((pow 2 3) {} nil)))
      (is (= 3.0 ((pow 9 1/2) {} nil)))
      (is (= 150.0 ((pow 12.24744871391589 2) {} nil))))

    (testing "with negative exponents"
      (is (= 1 ((pow 1 -1) {} nil)))
      (is (= 1/4 ((pow 2 -2) {} nil))))

    (testing "with nil"
      (is (nil? ((pow nil 2) {} nil)))
      (is (nil? ((pow 2 nil) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((pow Double/NaN 2) {} nil)))
      (is (Double/isNaN ((pow 2 Double/NaN) {} nil)))
      (is (Double/isInfinite ((pow Double/POSITIVE_INFINITY 2) {} nil)))
      (is (Double/isInfinite ((pow 2 Double/POSITIVE_INFINITY) {} nil)))
      (is (Double/isInfinite ((pow Double/NEGATIVE_INFINITY 2) {} nil)))
      (is (= 0.0 ((pow 2 Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with keyword"
      (is (= 100.0 ((pow :col1 :col2) {:col2 2.0 :col1 10.0} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((pow true) {} nil)))
      (is (thrown? Exception ((pow "hello") {} nil))))))

(deftest gcd-test
  (let [gcd (partial function-transform :gcd)]
    (testing "with number"
      (is (= 0 ((gcd 0 0) {} nil)))
      (is (= 1 ((gcd 1 1) {} nil)))
      (is (= 1 ((gcd 1 200) {} nil)))
      (is (= 8 ((gcd 24 200) {} nil)))
      (is (= 8 ((gcd 200 24) {} nil)))
      (is (= 2434 ((gcd 2434 654746) {} nil)))
      (is (= 2434 ((gcd 654746 2434) {} nil))))

    (testing "with keyword"
      (is (= 5 ((gcd :col1 :col2) {:col1 15 :col2 20} nil))))

    (testing "with nil"
      (is (nil? ((gcd nil 45) {} nil))))

    (testing "with unsupported types"
      (is (nil? ((gcd true 40) {} nil)))
      (is (nil? ((gcd 1 1/2) {} nil)))
      (is (nil? ((gcd "the" "greatest") {} nil)))
      (is (nil? ((gcd Double/NaN 10) {} nil)))
      (is (nil? ((gcd 10 Double/POSITIVE_INFINITY) {} nil)))
      (is (nil? ((gcd 10 Double/NEGATIVE_INFINITY) {} nil))))))

(deftest lcm-test
  (let [lcm (partial function-transform :lcm)]
    (testing "with number"
      (is (= 0 ((lcm 0 0) {} nil)))
      (is (= 1 ((lcm 1 1) {} nil)))
      (is (= 45 ((lcm 5 9) {} nil)))
      (is (= 45 ((lcm 9 5) {} nil)))
      (is (= 132 ((lcm 11 12) {} nil)))
      (is (= 132 ((lcm 12 11) {} nil)))
      (is (= 3416325 ((lcm 1025 3333) {} nil))))

    (testing "with keyword"
      (is (= 285 ((lcm :col1 :col2) {:col1 15 :col2 19} nil))))

    (testing "with nil"
      (is (nil? ((lcm nil 45) {} nil))))

    (testing "with unsupported types"
      (is (nil? ((lcm true 40) {} nil)))
      (is (nil? ((lcm 1 1/2) {} nil)))
      (is (nil? ((lcm "the" "greatest") {} nil)))
      (is (nil? ((lcm Double/NaN 10) {} nil)))
      (is (nil? ((lcm 10 Double/POSITIVE_INFINITY) {} nil)))
      (is (nil? ((lcm 10 Double/NEGATIVE_INFINITY) {} nil))))))

(deftest ln-test
  (let [ln (partial function-transform :ln)]
    (testing "with number"
      (is (Double/isInfinite ((ln 0) {} nil)))
      (is (= 1.0 ((ln 2.718281828459045) {} nil)))
      (is (= -0.6931471805599453 ((ln 0.5) {} nil))))

    (testing "with keyword"
      (is (= 1.0 ((ln :col1) {:col1 2.718281828459045} nil))))

    (testing "with nil"
      (is (nil? ((ln nil 45) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((ln Double/NaN) {} nil)))
      (is (Double/isInfinite ((ln Double/POSITIVE_INFINITY) {} nil)))
      (is (Double/isNaN ((ln Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with unsupported types"
      (is (nil? ((ln true 40) {} nil)))
      (is (nil? ((ln "the" "greatest") {} nil))))))

(deftest log-test
  (let [log (partial function-transform :log)]
    (testing "with number"
      (is (Double/isInfinite ((log 0) {} nil)))
      (is (= 1.0 ((log 10) {} nil)))
      (is (= -0.3010299956639812 ((log 0.5) {} nil))))

    (testing "with keyword"
      (is (= 1.0 ((log :col1) {:col1 10} nil))))

    (testing "with nil"
      (is (nil? ((log nil 45) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((log Double/NaN) {} nil)))
      (is (Double/isInfinite ((log Double/POSITIVE_INFINITY) {} nil)))
      (is (Double/isNaN ((log Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with unsupported types"
      (is (nil? ((log true 40) {} nil)))
      (is (nil? ((log "the" "greatest") {} nil))))))

(deftest degrees-test
  (let [degrees (partial function-transform :degrees)]
    (testing "with numbers"
      (is (= 0.0 ((degrees 0) {} nil)))
      (is (= 90.0 ((degrees 1.5707963267948966) {} nil)))
      (is (= 180.0 ((degrees 3.141592653589793) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((degrees Double/NaN) {} nil)))
      (is (Double/isInfinite ((degrees Double/POSITIVE_INFINITY) {} nil)))
      (is (Double/isInfinite ((degrees Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with nil"
      (is (nil? ((degrees nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((degrees true) {} nil)))
      (is (thrown? Exception ((degrees "hello") {} nil))))))

(deftest radians-test
  (let [radians (partial function-transform :radians)]
    (testing "with numbers"
      (is (= 0.0 ((radians 0) {} nil)))
      (is (= 1.5707963267948966 ((radians 90) {} nil)))
      (is (= 3.141592653589793 ((radians 180) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((radians Double/NaN) {} nil)))
      (is (Double/isInfinite ((radians Double/POSITIVE_INFINITY) {} nil)))
      (is (Double/isInfinite ((radians Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with nil"
      (is (nil? ((radians nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((radians true) {} nil)))
      (is (thrown? Exception ((radians "hello") {} nil))))))

(deftest sin-test
  (let [sin (partial function-transform :sin)]
    (testing "with numbers"
      (is (= 0.0 ((sin 0) {} nil)))
      (is (= 9.265358966049026E-5 ((sin 3.1415) {} nil)))
      (is (= 0.9738476308781951 ((sin 9/5) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((sin Double/NaN) {} nil)))
      (is (Double/isNaN ((sin Double/POSITIVE_INFINITY) {} nil)))
      (is (Double/isNaN ((sin Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with nil"
      (is (nil? ((sin nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((sin true) {} nil)))
      (is (thrown? Exception ((sin "hello") {} nil))))))

(deftest cos-test
  (let [cos (partial function-transform :cos)]
    (testing "with numbers"
      (is (= 1.0 ((cos 0) {} nil)))
      (is (= -0.9999999957076562 ((cos 3.1415) {} nil)))
      (is (= -0.2272020946930871 ((cos 9/5) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((cos Double/NaN) {} nil)))
      (is (Double/isNaN ((cos Double/POSITIVE_INFINITY) {} nil)))
      (is (Double/isNaN ((cos Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with nil"
      (is (nil? ((cos nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((cos true) {} nil)))
      (is (thrown? Exception ((cos "hello") {} nil))))))

(deftest tan-test
  (let [tan (partial function-transform :tan)]
    (testing "with numbers"
      (is (= 0.0 ((tan 0) {} nil)))
      (is (= -9.265359005819132E-5 ((tan 3.1415) {} nil)))
      (is (= -4.286261674628062 ((tan 9/5) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((tan Double/NaN) {} nil)))
      (is (Double/isNaN ((tan Double/POSITIVE_INFINITY) {} nil)))
      (is (Double/isNaN ((tan Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with nil"
      (is (nil? ((tan nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((tan true) {} nil)))
      (is (thrown? Exception ((tan "hello") {} nil))))))

(deftest sinh-test
  (let [sinh (partial function-transform :sinh)]
    (testing "with numbers"
      (is (= 0.0 ((sinh 0) {} nil)))
      (is (= 11.547665370743681 ((sinh 3.1415) {} nil)))
      (is (= 2.94217428809568 ((sinh 9/5) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((sinh Double/NaN) {} nil)))
      (is (Double/isInfinite ((sinh Double/POSITIVE_INFINITY) {} nil)))
      (is (Double/isInfinite ((sinh Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with nil"
      (is (nil? ((sinh nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((sinh true) {} nil)))
      (is (thrown? Exception ((sinh "hello") {} nil))))))

(deftest cosh-test
  (let [cosh (partial function-transform :cosh)]
    (testing "with numbers"
      (is (= 1.0 ((cosh 0) {} nil)))
      (is (= 11.590883293117605 ((cosh 3.1415) {} nil)))
      (is (= 3.1074731763172667 ((cosh 9/5) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((cosh Double/NaN) {} nil)))
      (is (Double/isInfinite ((cosh Double/POSITIVE_INFINITY) {} nil)))
      (is (Double/isInfinite ((cosh Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with nil"
      (is (nil? ((cosh nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((cosh true) {} nil)))
      (is (thrown? Exception ((cosh "hello") {} nil))))))

(deftest tanh-test
  (let [tanh (partial function-transform :tanh)]
    (testing "with numbers"
      (is (= 0.0 ((tanh 0) {} nil)))
      (is (= 0.9962713866337016 ((tanh 3.1415) {} nil)))
      (is (= 0.9468060128462683 ((tanh 9/5) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((tanh Double/NaN) {} nil)))
      (is (= 1.0 ((tanh Double/POSITIVE_INFINITY) {} nil)))
      (is (= -1.0 ((tanh Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with nil"
      (is (nil? ((tanh nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((tanh true) {} nil)))
      (is (thrown? Exception ((tanh "hello") {} nil))))))

(deftest asin-test
  (let [asin (partial function-transform :asin)]
    (testing "with numbers"
      (is (= 0.0 ((asin 0) {} nil)))
      (is (Double/isNaN ((asin 3.1415) {} nil)))
      (is (= 0.5235987755982989 ((asin 1/2) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((asin Double/NaN) {} nil)))
      (is (Double/isNaN ((asin Double/POSITIVE_INFINITY) {} nil)))
      (is (Double/isNaN ((asin Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with nil"
      (is (nil? ((asin nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((asin true) {} nil)))
      (is (thrown? Exception ((asin "hello") {} nil))))))

(deftest acos-test
  (let [acos (partial function-transform :acos)]
    (testing "with numbers"
      (is (= 1.5707963267948966 ((acos 0) {} nil)))
      (is (Double/isNaN ((acos 3.1415) {} nil)))
      (is (= 1.0471975511965979 ((acos 0.5) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((acos Double/NaN) {} nil)))
      (is (Double/isNaN ((acos Double/POSITIVE_INFINITY) {} nil)))
      (is (Double/isNaN ((acos Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with nil"
      (is (nil? ((acos nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((acos true) {} nil)))
      (is (thrown? Exception ((acos "hello") {} nil))))))

(deftest atan-test
  (let [atan (partial function-transform :atan)]
    (testing "with numbers"
      (is (= 0.0 ((atan 0) {} nil)))
      (is (= 1.2626187313511044 ((atan 3.1415) {} nil)))
      (is (= 0.4636476090008061 ((atan 0.5) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((atan Double/NaN) {} nil)))
      (is (= 1.5707963267948966 ((atan Double/POSITIVE_INFINITY) {} nil)))
      (is (= -1.5707963267948966 ((atan Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with nil"
      (is (nil? ((atan nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((atan true) {} nil)))
      (is (thrown? Exception ((atan "hello") {} nil))))))

(deftest atan2-test
  (let [atan2 (partial function-transform :atan2)]
    (testing "with numbers"
      (is (= 0.0 ((atan2 0 0) {} nil)))
      (is (= 1.5707963267948966 ((atan2 3.1415 0) {} nil)))
      (is (= -1.2626187313511044 ((atan2 -3.1415 1) {} nil)))
      (is (= 1.5707963267948966 ((atan2 0.5 0) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((atan2 0 Double/NaN) {} nil)))
      (is (Double/isNaN ((atan2 Double/NaN 0) {} nil)))
      (is (= 1.5707963267948966 ((atan2 Double/POSITIVE_INFINITY 0) {} nil)))
      (is (= -1.5707963267948966 ((atan2 Double/NEGATIVE_INFINITY 0) {} nil))))

    (testing "with nil"
      (is (nil? ((atan2 nil 0) {} nil)))
      (is (nil? ((atan2 0 nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((atan2 true 0) {} nil)))
      (is (thrown? Exception ((atan2 "hello" 0) {} nil))))))

(deftest within-test
  (let [within (partial function-transform :within)]
    (testing "with numbers"
      (is (true? ((within 0 0 0) {} nil)))
      (is (true? ((within 0 0 0.1) {} nil)))
      (is (true? ((within 9 10 1) {} nil)))
      (is (false? ((within 9 10 0.5) {} nil)))
      (is (false? ((within 10 20 9) {} nil))))

    (testing "with negative tolerance"
      (is (false? ((within 1 2 -1) {} nil)))
      (is (false? ((within 2.001 2 -0.002) {} nil))))

    (testing "with NaN/Infinity"
      (is (false? ((within 100 Double/NaN 2) {} nil)))
      (is (false? ((within Double/NaN 0 10) {} nil)))
      (is (false? ((within 1 0 Double/NaN) {} nil)))
      (is (false? ((within Double/POSITIVE_INFINITY Double/POSITIVE_INFINITY 10) {} nil))))

    (testing "with nil"
      (is (nil? ((within nil 0 0.1) {} nil)))
      (is (nil? ((within 0 nil 0.1) {} nil)))
      (is (nil? ((within 0 0 nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((within true 0 1) {} nil)))
      (is (thrown? Exception ((within "hello" 0 4) {} nil))))))

(deftest within%-test
  (let [within% (partial function-transform :within%)]
    (testing "with numbers"
      (is (true? ((within% 0 0 0) {} nil)))
      (is (true? ((within% 0 0 0.1) {} nil)))
      (is (true? ((within% 9 10 0.1) {} nil)))
      (is (false? ((within% 9 10 1/100) {} nil)))
      (is (true? ((within% 10 20 0.5) {} nil)))
      (is (false? ((within% 10 21 0.5) {} nil)))
      (is (true? ((within% 0 21 1.0) {} nil)))
      (is (false? ((within% -0.001 21 1.0) {} nil)))
      (is (true? ((within% 17 20 1/5) {} nil))))

    (testing "with negative tolerance"
      (is (false? ((within% 1 2 -0.1) {} nil)))
      (is (false? ((within% 2.001 2 -0.002) {} nil))))

    (testing "with NaN/Infinity"
      (is (false? ((within% 100 Double/NaN 0.2) {} nil)))
      (is (false? ((within% Double/NaN 0 0.10) {} nil)))
      (is (false? ((within% 1 0 Double/NaN) {} nil)))
      (is (false? ((within% Double/POSITIVE_INFINITY Double/POSITIVE_INFINITY 0.10) {} nil))))

    (testing "with nil"
      (is (nil? ((within% nil 0 0.1) {} nil)))
      (is (nil? ((within% 0 nil 0.1) {} nil)))
      (is (nil? ((within% 0 0 nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((within% true 0 1) {} nil)))
      (is (thrown? Exception ((within% "hello" 0 4) {} nil))))))

(deftest greatest-test
  (let [greatest (partial function-transform :greatest)]
    (testing "with numbers"
      (is (zero? ((greatest 0) {} nil)))
      (is (zero? ((greatest 0 0) {} nil)))
      (is (= 1 ((greatest 1 0) {} nil)))
      (is (= 1 ((greatest 0 1) {} nil)))
      (is (= 1 ((greatest 1 1 1 0) {} nil)))
      (is (= 3 ((greatest 1 2 3) {} nil)))
      (is (= 3 ((greatest 2 1 3 2 1 0) {} nil)))
      (is (= 2 ((greatest -1 0 1 2 -5) {} nil)))
      (is (= -5 ((greatest -5 -10 -20) {} nil)))
      (is (= 0.5 ((greatest 0 0.5 0.2 0.3) {} nil)))
      (is (= 400 ((greatest 40 -400 -0.94 0.49 10.4 400) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((greatest Double/NaN 1 2) {} nil)))
      (is (= Double/POSITIVE_INFINITY ((greatest 0 2 4 Double/POSITIVE_INFINITY) {} nil)))
      (is (zero? ((greatest Double/NEGATIVE_INFINITY 0) {} nil))))

    (testing "with nil"
      (is (nil? ((greatest nil) {} nil)))
      (is (nil? ((greatest 1 nil 2) {} nil))))

    (testing "with unsupported types"
      (is (true? ((greatest true) {} nil)))
      (is (= "hello" ((greatest "hello") {} nil)))
      (is (thrown? Exception ((greatest 0 false) {} nil)))
      (is (thrown? Exception ((greatest "hello" "world") {} nil))))))

(deftest least-test
  (let [least (partial function-transform :least)]
    (testing "with numbers"
      (is (zero? ((least 0) {} nil)))
      (is (zero? ((least 0 0) {} nil)))
      (is (zero? ((least 1 0) {} nil)))
      (is (= 1 ((least 2 1) {} nil)))
      (is (= 1 ((least 1 1 1 2) {} nil)))
      (is (= 1 ((least 1 2 3) {} nil)))
      (is (= 3 ((least 4 6 3 4 3 7) {} nil)))
      (is (= -5 ((least -1 0 1 2 -5) {} nil)))
      (is (= -20 ((least -5 -10 -20) {} nil)))
      (is (zero? ((least 0 0.5 0.2 0.3) {} nil)))
      (is (= -400 ((least 40 -400 -0.94 0.49 10.4 400) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((least Double/NaN 1 2) {} nil)))
      (is (= 1 ((least 1 2 4 Double/POSITIVE_INFINITY) {} nil)))
      (is (= Double/NEGATIVE_INFINITY ((least Double/NEGATIVE_INFINITY 0) {} nil))))

    (testing "with nil"
      (is (nil? ((least nil) {} nil)))
      (is (nil? ((least 1 nil 2) {} nil))))

    (testing "with unsupported types"
      (is (true? ((least true) {} nil)))
      (is (= "hello" ((least "hello") {} nil)))
      (is (thrown? Exception ((least 0 false) {} nil)))
      (is (thrown? Exception ((least "hello" "world") {} nil))))))

(deftest pdfuniform-test
  (let [pdfuniform (partial function-transform :pdfuniform)]
    (testing "with number"
      (is (= 0.0 ((pdfuniform 0) {} nil)))
      (is (= 1.0 ((pdfuniform 0.5) {} nil)))
      (is (= 0.0 ((pdfuniform 1) {} nil))))

    (testing "with number and min/max"
      (is (= 0.0 ((pdfuniform 1 1 5) {} nil)))
      (is (= 0.25 ((pdfuniform 3 1 5) {} nil)))
      (is (= 0.25 ((pdfuniform 4 1 5) {} nil))))

    (testing "with list"
      (is (= [0.0 1.0 0.0] ((pdfuniform [0 0.5 1]) {} nil))))

    (testing "with list and min/max"
      (is (= [0.0 0.25 0.25] ((pdfuniform [1 3 4] 1 5) {} nil))))

    (testing "with NaN/Infinity"
      (is (= 1.0 ((pdfuniform Double/NaN) {} nil)))
      (is (= 0.0 ((pdfuniform Double/POSITIVE_INFINITY) {} nil)))
      (is (= 0.0 ((pdfuniform Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with nil"
      (is (nil? ((pdfuniform nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((pdfuniform true) {} nil)))
      (is (thrown? Exception ((pdfuniform "hello") {} nil))))))

(deftest pdfnormal-test
  (let [pdfnormal (partial function-transform :pdfnormal)]
    (testing "with number"
      (is (= 0.3989422804014327 ((pdfnormal 0) {} nil)))
      (is (= 0.3520653267642995 ((pdfnormal 0.5) {} nil)))
      (is (= 0.24197072451914337 ((pdfnormal 1) {} nil))))

    (testing "with number and mean/sd"
      (is (= 0.07978845608028654 ((pdfnormal 1 1 5) {} nil)))
      (is (= 0.07365402806066466 ((pdfnormal 3 1 5) {} nil)))
      (is (= 0.05793831055229655 ((pdfnormal 5 1 5) {} nil))))

    (testing "with list"
      (is (= [0.3989422804014327 0.3520653267642995 0.24197072451914337] ((pdfnormal [0 0.5 1]) {} nil))))

    (testing "with list and mean/sd"
      (is (= [0.07978845608028654 0.07365402806066466 0.05793831055229655] ((pdfnormal [1 3 5] 1 5) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((pdfnormal Double/NaN) {} nil)))
      (is (= 0.0 ((pdfnormal Double/POSITIVE_INFINITY) {} nil)))
      (is (= 0.0 ((pdfnormal Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with nil"
      (is (nil? ((pdfnormal nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((pdfnormal true) {} nil)))
      (is (thrown? Exception ((pdfnormal "hello") {} nil))))))

(deftest pdfpoisson-test
  (let [pdfpoisson (partial function-transform :pdfpoisson)]
    (testing "with number"
      (is (= 0.36787944117144233 ((pdfpoisson 0) {} nil)))
      (is (= 0.36787944117144233 ((pdfpoisson 1) {} nil)))
      (is (= 0.18393972058572114 ((pdfpoisson 2) {} nil))))

    (testing "with number and lambda"
      (is (= 0.2706705664732254 ((pdfpoisson 1 2) {} nil)))
      (is (= 0.18044704431548356 ((pdfpoisson 3 2) {} nil)))
      (is (= 0.03608940886309672 ((pdfpoisson 5 2) {} nil))))

    (testing "with list"
      (is (= [0.36787944117144233 0.36787944117144233	0.18393972058572114] ((pdfpoisson [0 1 2]) {} nil))))

    (testing "with list and lambda"
      (is (= [0.2706705664732254 0.18044704431548356	0.03608940886309672] ((pdfpoisson [1 3 5] 2) {} nil))))

    (testing "with NaN/Infinity"
      (is (= 0.36787944117144233 ((pdfpoisson Double/NaN) {} nil)))
      (is (thrown? Exception ((pdfpoisson Double/POSITIVE_INFINITY) {} nil)))
      (is (thrown? Exception ((pdfpoisson Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with nil"
      (is (nil? ((pdfpoisson nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((pdfpoisson true) {} nil)))
      (is (thrown? Exception ((pdfpoisson "hello") {} nil))))))

(deftest cdfuniform-test
  (let [cdfuniform (partial function-transform :cdfuniform)]
    (testing "with number"
      (is (= 0.0 ((cdfuniform 0) {} nil)))
      (is (= 0.5 ((cdfuniform 0.5) {} nil)))
      (is (= 1.0 ((cdfuniform 1) {} nil))))

    (testing "with number and min/max"
      (is (= 0.0 ((cdfuniform 1 1 5) {} nil)))
      (is (= 0.5 ((cdfuniform 3 1 5) {} nil)))
      (is (= 1.0 ((cdfuniform 5 1 5) {} nil))))

    (testing "with list"
      (is (= [0.0 0.5 1.0] ((cdfuniform [0 0.5 1]) {} nil))))

    (testing "with list and min/max"
      (is (= [0.0 0.5 1.0] ((cdfuniform [1 3 5] 1 5) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((cdfuniform Double/NaN) {} nil)))
      (is (= 1.0 ((cdfuniform Double/POSITIVE_INFINITY) {} nil)))
      (is (= 0.0 ((cdfuniform Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with nil"
      (is (nil? ((cdfuniform nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((cdfuniform true) {} nil)))
      (is (thrown? Exception ((cdfuniform "hello") {} nil))))))

(deftest cdfnormal-test
  (let [cdfnormal (partial function-transform :cdfnormal)]
    (testing "with number"
      (is (= 0.5 ((cdfnormal 0) {} nil)))
      (is (= 0.691462461274013 ((cdfnormal 0.5) {} nil)))
      (is (= 0.8413447460685429 ((cdfnormal 1) {} nil))))

    (testing "with number and mean/sd"
      (is (= 0.5 ((cdfnormal 1 1 5) {} nil)))
      (is (= 0.6554217416103242 ((cdfnormal 3 1 5) {} nil)))
      (is (= 0.7881446014166033 ((cdfnormal 5 1 5) {} nil))))

    (testing "with list"
      (is (= [0.5 0.691462461274013 0.8413447460685429] ((cdfnormal [0 0.5 1]) {} nil))))

    (testing "with list and mean/sd"
      (is (= [0.5 0.6554217416103242 0.7881446014166033] ((cdfnormal [1 3 5] 1 5) {} nil))))

    (testing "with NaN/Infinity"
      (is (Double/isNaN ((cdfnormal Double/NaN) {} nil)))
      (is (= 1.0 ((cdfnormal Double/POSITIVE_INFINITY) {} nil)))
      (is (= 0.0 ((cdfnormal Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with nil"
      (is (nil? ((cdfnormal nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((cdfnormal true) {} nil)))
      (is (thrown? Exception ((cdfnormal "hello") {} nil))))))

(deftest cdfpoisson-test
  (let [cdfpoisson (partial function-transform :cdfpoisson)]
    (testing "with number"
      (is (= 0.36787944117144233 ((cdfpoisson 0) {} nil)))
      (is (= 0.7357588823428847 ((cdfpoisson 1) {} nil)))
      (is (= 0.9196986029286058 ((cdfpoisson 2) {} nil))))

    (testing "with number and lambda"
      (is (= 0.406005849709838 ((cdfpoisson 1 2) {} nil)))
      (is (= 0.857123460498547 ((cdfpoisson 3 2) {} nil)))
      (is (= 0.9834363915193856 ((cdfpoisson 5 2) {} nil))))

    (testing "with list"
      (is (= [0.36787944117144233 0.7357588823428847 0.9196986029286058] ((cdfpoisson [0 1 2]) {} nil))))

    (testing "with list and lambda"
      (is (= [0.406005849709838 0.857123460498547	0.9834363915193856] ((cdfpoisson [1 3 5] 2) {} nil))))

    (testing "with NaN/Infinity"
      (is (= 0.36787944117144233 ((cdfpoisson Double/NaN) {} nil)))
      (is (thrown? Exception ((cdfpoisson Double/POSITIVE_INFINITY) {} nil)))
      (is (thrown? Exception ((cdfpoisson Double/NEGATIVE_INFINITY) {} nil))))

    (testing "with nil"
      (is (nil? ((cdfpoisson nil) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((cdfpoisson true) {} nil)))
      (is (thrown? Exception ((cdfpoisson "hello") {} nil))))))
