(ns parsec.functions.functional-functions-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.functions :refer :all]
            [parsec.functions.functionalfunctions :refer :all]
            [clj-time.core :as time]))

(deftest apply-test
  (let [apply' (partial function-transform :apply)]
    (testing "with no args"
      (is (= "result" ((apply' (fn [_ _] (->ParsecUdf [] (fn [_ _] "result")))) {} nil))))

    (testing "with one arg"
      (is (= 80 ((apply' (fn [_ _]
                           (->ParsecUdf
                             ["x"]
                             (fn [args _]
                               (* 2 (first args)))))
                         40) {} nil))))

    (testing "with two args"
      (is (= 80 ((apply' (fn [_ _]
                           (->ParsecUdf
                             ["x" "y"]
                             (fn [args _]
                               (apply + args))))
                         35 45) {} nil))))

    (testing "with unsupported types"
      (is (thrown? Exception ((apply' nil) {} nil)))
      (is (thrown? Exception ((apply' 3.14) {} nil)))
      (is (thrown? Exception ((apply' true) {} nil)))
      (is (thrown? Exception ((apply' "hello") {} nil)))
      (is (thrown? Exception ((apply' time/now) {} nil))))))

(deftest map-test
  (let [map' (partial function-transform :map)
        inc-function (fn [_ _] (->ParsecUdf ["x"] (fn [args _] (+ 1 (first args)))))]
    (testing "with no args or nil"
      (is (nil? ((map' (fn [_ _] (->ParsecUdf [] (fn [_ _] "result")))) {} nil)))
      (is (nil? ((map' inc-function nil) {} nil))))

    (testing "with empty list"
      (is (= [] ((map' inc-function []) {} nil))))

    (testing "with list of one"
      (is (= [2] ((map' inc-function [1]) {} nil))))

    (testing "with list of many"
      (is (= [1 2 3 11 21 99] ((map' inc-function [0 1 2 10 20 98]) {} nil))))

    (testing "with unsupported types instead of a function"
      (is (thrown? Exception ((map' nil) {} nil)))
      (is (thrown? Exception ((map' 3.14) {} nil)))
      (is (thrown? Exception ((map' true) {} nil)))
      (is (thrown? Exception ((map' "hello") {} nil)))
      (is (thrown? Exception ((map' (time/now)) {} nil))))

    (testing "with unsupported types instead of a list"
      (is (nil? ((map' inc-function 3.14) {} nil)))
      (is (nil? ((map' inc-function true) {} nil)))
      (is (nil? ((map' inc-function "hello") {} nil)))
      (is (nil? ((map' inc-function (time/now)) {} nil)))
      (is (nil? ((map' inc-function {:x 1 :y 2}) {} nil))))))

(deftest mapcat-test
  (let [mapcat' (partial function-transform :mapcat)
        inc-function (fn [_ _] (->ParsecUdf ["x"] (fn [args _] (+ 1 (first args)))))
        inc2-function (fn [_ _] (->ParsecUdf ["x"] (fn [args _] (let [n (first args)]
                                                                  (vector n (+ 1 n))))))]
    (testing "with no args or nil"
      (is (empty? ((mapcat' (fn [_ _] (->ParsecUdf [] (fn [_ _] "result")))) {} nil)))
      (is (empty? ((mapcat' inc-function nil) {} nil))))

    (testing "with empty list"
      (is (= [] ((mapcat' inc-function []) {} nil))))

    (testing "with list of one"
      (is (= [2] ((mapcat' inc-function [1]) {} nil)))
      (is (= [1 2] ((mapcat' inc2-function [1]) {} nil))))

    (testing "with list of many"
      (is (= [1 2 3 11 21 99] ((mapcat' inc-function [0 1 2 10 20 98]) {} nil)))
      (is (= [0 1 1 2 2 3 10 11 20 21 98 99] ((mapcat' inc2-function [0 1 2 10 20 98]) {} nil))))

    (testing "with unsupported types instead of a function"
      (is (thrown? Exception ((mapcat' nil) {} nil)))
      (is (thrown? Exception ((mapcat' 3.14) {} nil)))
      (is (thrown? Exception ((mapcat' true) {} nil)))
      (is (thrown? Exception ((mapcat' "hello") {} nil)))
      (is (thrown? Exception ((mapcat' (time/now)) {} nil))))

    (testing "with unsupported types instead of a list"
      (is (nil? ((mapcat' inc2-function 3.14) {} nil)))
      (is (nil? ((mapcat' inc2-function true) {} nil)))
      (is (nil? ((mapcat' inc2-function "hello") {} nil)))
      (is (nil? ((mapcat' inc2-function (time/now)) {} nil)))
      (is (nil? ((mapcat' inc2-function {:x 1 :y 2}) {} nil))))))

(deftest mapvalues-test
  (let [mapvalues' (partial function-transform :mapvalues)
        inc-function (fn [_ _] (->ParsecUdf ["x"] (fn [args _] (+ 1 (first args)))))]
    (testing "with no args or nil"
      (is (nil? ((mapvalues' (fn [_ _] (->ParsecUdf [] (fn [_ _] "result")))) {} nil)))
      (is (nil? ((mapvalues' inc-function nil) {} nil))))

    (testing "with empty map"
      (is (= {} ((mapvalues' inc-function {}) {} nil))))

    (testing "with map of one"
      (is (= {:abc 26} ((mapvalues' inc-function {:abc 25}) {} nil))))

    (testing "with map of many"
      (is (= {:x 2 :y 3 :z 100} ((mapvalues' inc-function {:x 1 :y 2 :z 99}) {} nil))))

    (testing "with unsupported types instead of a function"
      (is (thrown? Exception ((mapvalues' nil {:x 1 :y 2}) {} nil)))
      (is (thrown? Exception ((mapvalues' 3.14 {:x 1 :y 2}) {} nil)))
      (is (thrown? Exception ((mapvalues' true {:x 1 :y 2}) {} nil)))
      (is (thrown? Exception ((mapvalues' "hello" {:x 1 :y 2}) {} nil)))
      (is (thrown? Exception ((mapvalues' (time/now) {:x 1 :y 2}) {} nil))))

    (testing "with unsupported types instead of a map"
      (is (nil? ((mapvalues' inc-function 3.14) {} nil)))
      (is (nil? ((mapvalues' inc-function true) {} nil)))
      (is (nil? ((mapvalues' inc-function "hello") {} nil)))
      (is (nil? ((mapvalues' inc-function (time/now)) {} nil)))
      (is (nil? ((mapvalues' inc-function [1 2 3]) {} nil))))))

(deftest filter-test
  (let [filter' (partial function-transform :filter)
        odd-function (fn [_ _] (->ParsecUdf ["x"] (fn [args _] (odd? (first args)))))]
    (testing "with no args or nil"
      (is (nil? ((filter' (fn [_ _] (->ParsecUdf [] (fn [_ _] "result")))) {} nil)))
      (is (nil? ((filter' odd-function nil) {} nil))))

    (testing "with empty list"
      (is (= [] ((filter' odd-function []) {} nil))))

    (testing "with list of one"
      (is (= [1] ((filter' odd-function [1]) {} nil))))

    (testing "with list of many"
      (is (= [1 3 5 7] ((filter' odd-function [0 1 2 3 4 5 6 7]) {} nil))))

    (testing "with unsupported types instead of a function"
      (is (thrown? Exception ((filter' nil) {} nil)))
      (is (thrown? Exception ((filter' 3.14) {} nil)))
      (is (thrown? Exception ((filter' true) {} nil)))
      (is (thrown? Exception ((filter' "hello") {} nil)))
      (is (thrown? Exception ((filter' (time/now)) {} nil))))

    (testing "with unsupported types instead of a list"
      (is (nil? ((filter' odd-function 3.14) {} nil)))
      (is (nil? ((filter' odd-function true) {} nil)))
      (is (nil? ((filter' odd-function "hello") {} nil)))
      (is (nil? ((filter' odd-function (time/now)) {} nil)))
      (is (nil? ((filter' odd-function {:x 1 :y 2}) {} nil))))))
