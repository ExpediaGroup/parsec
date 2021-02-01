(ns parsec.functions.map-functions-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.functions :refer :all]
            [parsec.functions.mapfunctions :refer :all]))

(deftest get-test
  (let [get (partial function-transform :get)]
    (testing "with nil"
      (is (nil? ((get nil nil) {} nil)))
      (is (nil? ((get :col1 nil) {:col1 nil} nil)))
      (is (nil? ((get "x" nil) {} nil)))
      (is (nil? ((get {:foo "bar"} nil) {} nil))))
    (testing "with non-map argument"
      (is (nil? ((get nil "x") {} nil)))
      (is (nil? ((get :col1 "x") {:col1 false} nil)))
      (is (nil? ((get "x" "y") {} nil)))
      (is (nil? ((get 99 "x") {} nil))))
    (testing "with different key types"
      (is (= "bar" ((get {:foo "bar"} "foo") {} nil)))
      (is (= 3 ((get {:foo 3} "foo") {} nil)))
      (is (= true ((get {:foo true :bar false} "foo") {} nil)))
      (is (= {:bar "baz"} ((get {:foo {:bar "baz"}} "foo") {} nil))))
    (testing "with default values"
      (is (= "default" ((get nil "foo" "default") {} nil)))
      (is (= "default" ((get {} "foo" "default") {} nil)))
      (is (= "default" ((get {:x 1} "foo" "default") {} nil)))
      (is (= "default" ((get {:x {:y 1}} ["x", "z"] "default") {} nil))))
    (testing "with nested gets"
      (is (= {:b "xx"} ((get {:a {:b "xx"}} ["a"]) {} nil)))
      (is (= "xx" ((get {:a {:b "xx"}} ["a" "b"]) {} nil)))
      (is (= "xx" ((get {:a {:b "xx"}} '("a" "b")) {} nil)))
      (is (= "xx" ((get {:a {:b {:c "xx"}}} ["a" "b" "c"]) {} nil)))
      (is (= "xx" ((get {:a {:b {:c "xx"}}} '("a" "b" "c")) {} nil))))))

(deftest set-function-test
  (let [set (partial function-transform :set)]
    (testing "null map with null"
      (is (= {nil nil} ((set nil nil nil) {} nil))))
    (testing "empty map with different types"
      (is (= {:foo nil} ((set {} "foo" nil) {} nil)))
      (is (= {:foo "bar"} ((set {} "foo" "bar") {} nil)))
      (is (= {:5 nil} ((set {} 5 nil) {} nil)))
      (is (= {:truth true} ((set {} "truth" true) {} nil))))
    (testing "null map with different key types"
      (is (= {:foo nil} ((set nil "foo" nil) {} nil)))
      (is (= {:5 nil} ((set nil 5 nil) {} nil))))
    (testing "null map with different value types"
      (is (= {:foo "bar"} ((set nil "foo" "bar") {} nil)))
      (is (= {:foo 5} ((set nil "foo" 5) {} nil)))
      (is (= {:foo 5.30293} ((set nil "foo" 5.30293) {} nil)))
      (is (= {:truth true} ((set nil "truth" true) {} nil)))
      (is (= {:foo {"bar" "baz"}} ((set nil "foo" {"bar" "baz"}) {} nil))))
    (testing "existing map with null"
      (is (= {:foo "bar", nil nil} ((set :col1 nil nil) {:col1 {:foo "bar"}} nil))))
    (testing "existing map with different non-existing key of different types"
      (is (= {:foo "bar", :baz "bar"} ((set :col1 "baz" "bar") {:col1 {:foo "bar"}} nil)))
      (is (= {:foo "bar", :5 "bar"} ((set :col1 5 "bar") {:col1 {:foo "bar"}} nil))))
    (testing "existing map with existing key"
      (is (= {:foo "baz"} ((set :col1 "foo" "baz") {:col1 {:foo "bar"}} nil))))
    (testing "with nested sets"
      (is (= {:a "xx"} ((set {} ["a"] "xx") {} nil)))
      (is (= {:a {:b "xx"}} ((set {} ["a" "b"] "xx") {} nil)))
      (is (= {:a {:b "xx"}} ((set {} '("a" "b") "xx") {} nil)))
      (is (thrown? ClassCastException ((set {:a "xx"} ["a" "b"] "xx") {} nil)))
      (is (= {:a {:b {:c "xx"}}} ((set {} ["a" "b" "c"] "xx") {} nil)))
      (is (= {:a {:b {:c "xx"}}} ((set {:a {:b {:c "yy"}}} '("a" "b" "c") "xx") {} nil)))
      (is (= {:a {:b {:c "xx" :d "yy"}} :z 1} ((set {:a {:b {:d "yy"}} :z 1} '("a" "b" "c") "xx") {} nil))))))

(deftest delete-test
  (let [delete (partial function-transform :delete)]
    (testing "with null args"
      (is (nil? ((delete nil nil) {} nil)))
      (is (= {:foo "bar"} ((delete {:foo "bar"} nil) {} nil))))
    (testing "existing map with different key types"
      (is (= {} ((delete :col1 "foo") {:col1 {:foo "bar"}} nil)))
      (is (= {:no true} ((delete {:yes false :no true}  "yes") {} nil)))
      (is (= {} ((delete :col1 5) {:col1 {:5 "bar"}} nil))))
    (testing "with non-existing keys"
      (is (= {} ((delete {} "a") {} nil)))
      (is (= {:a "x"} ((delete {:a "x"} "b") {} nil))))
    (testing "with nested sets"
      (is (= {} ((delete {:a {:b "xx"}} ["a"]) {} nil)))
      (is (= {:c 2} ((delete {:a {:b "xx"} :c 2} ["a"]) {} nil)))
      (is (= {:a {:d "yy"} :c 2} ((delete {:a {:b "xx" :d "yy"} :c 2} ["a" "b"]) {} nil))))))

(deftest merge-test
  (let [merge (partial function-transform :merge)]
    (testing "null map"
      (is (nil? ((merge nil) {} nil))))
    (testing "two null maps"
      (is (nil? ((merge nil nil) {} nil))))
    (testing "empty map"
      (is (= {} ((merge {}) {} nil))))
    (testing "two empty maps"
      (is (= {} ((merge {} {}) {} nil))))
    (testing "one map"
      (is (= {:a 1} ((merge {:a 1}) {} nil))))
    (testing "two identical maps"
      (is (= {:a 1} ((merge {:a 1} {:a 1}) {} nil))))
    (testing "two maps with duplicate keys"
      (is (= {:a 4 :b 2} ((merge {:a 1 :b 3} {:b 2 :a 4}) {} nil))))
    (testing "three maps"
      (is (= {:a 2 :b 3 :c 6} ((merge {:a 1} {:a 2 :b 3} {:c 6}) {} nil))))
    (testing "non-map types"
      (is (= {:a 1} ((merge {:a 1} "a") {} nil))))
    (testing "non-map types"
      (is (= {:a "x" :b "y"} ((merge nil {:a "x"} true {:b "y"} 100) {} nil))))))

(deftest keys-test
  (let [keys (partial function-transform :keys)]
    (testing "null map"
      (is (nil? ((keys nil) {} nil))))
    (testing "empty map"
      (is (= [] ((keys {}) {} nil))))
    (testing "existing map"
      (is (= '("baz" "foo") (sort ((keys :col1) {:col1 {:foo "bar", :baz "boom"}} nil)))))))

(deftest values-test
  (let [values (partial function-transform :values)]
    (testing "null map"
      (is (nil? ((values nil) {} nil))))
    (testing "empty map"
      (is (= [] ((values {}) {} nil))))
    (testing "existing map"
      (is (= '("bar" "boom" "farm") (sort ((values :col1) {:col1 {:foo "bar", :baz "boom", :boo "farm"}} nil)))))))

