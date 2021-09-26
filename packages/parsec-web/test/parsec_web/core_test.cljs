(ns parsec-web.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [parsec-web.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
