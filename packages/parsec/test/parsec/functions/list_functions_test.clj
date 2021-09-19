(ns parsec.functions.list-functions-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.functions :refer :all]
            [clj-time.core :as time]
            [parsec.functions.listfunctions :refer :all]))

(deftest reverse-test
  (let [reverse (partial function-transform :reverse)]
    (testing "with nil"
      (is (nil? ((reverse nil) {} nil)))
      (is (nil? ((reverse :col1) {:col1 nil} nil))))
    (testing "unsupported data types"
      (is (nil? ((reverse 1) {} nil)))
      (is (nil? ((reverse true) {} nil)))
      (is (nil? ((reverse (time/now)) {} nil))))
    (testing "with string"
      (is (= "" ((reverse "") {} nil)))
      (is (= "x" ((reverse "x") {} nil)))
      (is (= "cba" ((reverse "abc") {} nil)))
      (is (= "hello world" ((reverse "dlrow olleh") {} nil))))
    (testing "with list"
      (is (= '() ((reverse '()) {} nil)))
      (is (= '("x") ((reverse '("x")) {} nil)))
      (is (= '(3 2 1) ((reverse '(1 2 3)) {} nil))))
    (testing "with vector"
      (is (= [] ((reverse []) {} nil)))
      (is (= ["x"] ((reverse ["x"]) {} nil)))
      (is (= [3 2 1] ((reverse [1 2 3]) {} nil))))))

(deftest length-test
  (let [length (partial function-transform :length)]
    (testing "with nil"
      (is (nil? ((length nil) {} nil)))
      (is (nil? ((length :col1) {:col1 nil} nil))))
    (testing "unsupported data types"
      (is (nil? ((length 1) {} nil)))
      (is (nil? ((length true) {} nil)))
      (is (nil? ((length (time/now)) {} nil))))
    (testing "with string"
      (is (= 0 ((length "") {} nil)))
      (is (= 1 ((length "a") {} nil)))
      (is (= 5 ((length "abcde") {} nil))))
    (testing "with list"
      (is (zero? ((length '()) {} nil)))
      (is (= 1 ((length '("x")) {} nil)))
      (is (= 3 ((length '(1 2 3)) {} nil))))
    (testing "with vector"
      (is (zero? ((length []) {} nil)))
      (is (= 1 ((length ["x"]) {} nil)))
      (is (= 3 ((length [1 2 3]) {} nil))))))

(deftest listmean-test
  (let [listmean (partial function-transform :listmean)]
    (testing "with nil"
      (is (nil? ((listmean nil) {} nil)))
      (is (nil? ((listmean :col1) {:col1 nil} nil))))
    (testing "unsupported data types"
      (is (nil? ((listmean 1) {} nil)))
      (is (nil? ((listmean true) {} nil)))
      (is (nil? ((listmean (time/now)) {} nil))))
    (testing "unsupported list data types"
      (is (thrown? Exception ((listmean ["a"]) {} nil)))
      (is (thrown? Exception ((listmean [true false]) {} nil)))
      (is (thrown? Exception ((listmean [time/now time/yesterday]) {} nil))))
    (testing "with list"
      (is (Double/isNaN ((listmean '()) {} nil)))
      (is (= 1.0 ((listmean '(1 1 1)) {} nil)))
      (is (= 2.0 ((listmean '(1 2 3)) {} nil)))
      (is (= 10.0 ((listmean '(0 10 20)) {} nil))))
    (testing "with vector"
      (is (Double/isNaN ((listmean []) {} nil)))
      (is (= 1.0 ((listmean [1 1 1]) {} nil)))
      (is (= 2.0 ((listmean [1 2 3]) {} nil)))
      (is (= 10.0 ((listmean [0 10 20]) {} nil))))))

(deftest listmin-test
  (let [listmin (partial function-transform :listmin)]
    (testing "with nil"
      (is (nil? ((listmin nil) {} nil)))
      (is (nil? ((listmin :col1) {:col1 nil} nil))))
    (testing "unsupported data types"
      (is (nil? ((listmin 1) {} nil)))
      (is (nil? ((listmin true) {} nil)))
      (is (nil? ((listmin (time/now)) {} nil))))
    (testing "unsupported list data types"
      (is (thrown? Exception ((listmin ["a" "b"]) {} nil)))
      (is (thrown? Exception ((listmin [true false]) {} nil)))
      (is (thrown? Exception ((listmin [time/now time/yesterday]) {} nil))))
    (testing "with list"
      (is (nil? ((listmin '()) {} nil)))
      (is (= 1 ((listmin '(1 1 1)) {} nil)))
      (is (= 2 ((listmin '(5 2 3)) {} nil)))
      (is (= -10 ((listmin '(0 -10 9)) {} nil))))
    (testing "with vector"
      (is (nil? ((listmin []) {} nil)))
      (is (= 1 ((listmin [1 1 1]) {} nil)))
      (is (= 2 ((listmin [5 2 3]) {} nil)))
      (is (= -10 ((listmin [0 -10 19]) {} nil))))))

(deftest peek-test
  (let [peek (partial function-transform :peek)]
    (testing "with nil"
      (is (nil? ((peek nil) {} nil)))
      (is (nil? ((peek :col1) {:col1 nil} nil))))
    (testing "unsupported data types"
      (is (nil? ((peek "x") {} nil)))
      (is (nil? ((peek 1) {} nil)))
      (is (nil? ((peek true) {} nil)))
      (is (nil? ((peek (time/now)) {} nil))))
    (testing "with list"
      (is (nil? ((peek '()) {} nil)))
      (is (= "x" ((peek '("x")) {} nil)))
      (is (= 1 ((peek '(1 2 3)) {} nil))))
    (testing "with vector"
      (is (nil? ((peek []) {} nil)))
      (is (= "x" ((peek ["x"]) {} nil)))
      (is (= 1 ((peek [1 2 3]) {} nil))))))

(deftest peeklast-test
  (let [peeklast (partial function-transform :peeklast)]
    (testing "with nil"
      (is (nil? ((peeklast nil) {} nil)))
      (is (nil? ((peeklast :col1) {:col1 nil} nil))))
    (testing "unsupported data types"
      (is (nil? ((peeklast "x") {} nil)))
      (is (nil? ((peeklast 1) {} nil)))
      (is (nil? ((peeklast true) {} nil)))
      (is (nil? ((peeklast (time/now)) {} nil))))
    (testing "with list"
      (is (nil? ((peeklast '()) {} nil)))
      (is (= "x" ((peeklast '("x")) {} nil)))
      (is (= 3 ((peeklast '(1 2 3)) {} nil))))
    (testing "with vector"
      (is (nil? ((peeklast []) {} nil)))
      (is (= "x" ((peeklast ["x"]) {} nil)))
      (is (= 3 ((peeklast [1 2 3]) {} nil))))))

(deftest pop-test
  (let [pop (partial function-transform :pop)]
    (testing "with nil"
      (is (nil? ((pop nil) {} nil)))
      (is (nil? ((pop :col1) {:col1 nil} nil))))
    (testing "unsupported data types"
      (is (nil? ((pop "x") {} nil)))
      (is (nil? ((pop 1) {} nil)))
      (is (nil? ((pop true) {} nil)))
      (is (nil? ((pop (time/now)) {} nil))))
    (testing "with list"
      (is (thrown? Exception ((pop '()) {} nil)))
      (is (= '() ((pop '("x")) {} nil)))
      (is (= '(2 3) ((pop '(1 2 3)) {} nil)))
      (is (= '("x" true false) ((pop '(1 "x" true false)) {} nil))))
    (testing "with vector"
      (is (thrown? Exception ((pop []) {} nil)))
      (is (= [] ((pop ["x"]) {} nil)))
      (is (= [2 3] ((pop [1 2 3]) {} nil)))
      (is (= ["x" true false] ((pop [1 "x" true false]) {} nil))))))

(deftest push-test
  (let [push (partial function-transform :push)]
    (testing "with nil"
      (is (nil? ((push nil) {} nil)))
      (is (nil? ((push nil "x") {} nil)))
      (is (nil? ((push :col1) {:col1 nil} nil))))
    (testing "unsupported data types"
      (is (nil? ((push "x") {} nil)))
      (is (nil? ((push "x" "y") {} nil)))
      (is (nil? ((push 1) {} nil)))
      (is (nil? ((push 1 2) {} nil)))
      (is (nil? ((push true) {} nil)))
      (is (nil? ((push (time/now)) {} nil))))
    (testing "with list"
      (is (= '(nil) ((push '() nil) {} nil)))
      (is (= '(1) ((push '() 1) {} nil)))
      (is (= '("x" "y") ((push '("x"), "y") {} nil)))
      (is (= '(1 2 3) ((push '(1 2 ) 3) {} nil)))
      (is (= '(true false false) ((push '(true false) false) {} nil))))
    (testing "with vector"
      (is (= [nil] ((push [] nil) {} nil)))
      (is (= [1] ((push [] 1) {} nil)))
      (is (= ["x" "y"] ((push ["x"], "y") {} nil)))
      (is (= [1 2 3] ((push [1 2] 3) {} nil)))
      (is (= [true false false] ((push [true false] false) {} nil))))))

(deftest concat-test
  (let [concat (partial function-transform :concat)]
    (testing "with non-lists"
      (is (= '(nil) ((concat nil) {} nil)))
      (is (= '(nil "x") ((concat nil "x") {} nil)))
      (is (= '("x") ((concat '() :col1) {:col1 "x"} nil))))
    (testing "with list"
      (is (= '(nil) ((concat '() nil) {} nil)))
      (is (= '() ((concat '() '()) {} nil)))
      (is (= '("x" "y") ((concat '("x"), "y") {} nil)))
      (is (= '("x" "y") ((concat '("x"), '("y")) {} nil)))
      (is (= '(1 2 3 4) ((concat '(1 2 ) '(3 4)) {} nil)))
      (is (= '(1 true false "x") ((concat '(1) '(true false) '("x")) {} nil)))
      (is (= '(1 2 3 4 5 6) ((concat '(1 2 ) '(3 4) '(5 6)) {} nil))))
    (testing "with vectors"
      (is (= [nil] ((concat [] nil) {} nil)))
      (is (= [] ((concat [] []) {} nil)))
      (is (= ["x" "y"] ((concat ["x"], "y") {} nil)))
      (is (= ["x" "y"] ((concat ["x"], ["y"]) {} nil)))
      (is (= [1 2 3 4] ((concat [1 2] [3 4]) {} nil)))
      (is (= [1 true false "x"] ((concat [1] [true false] ["x"]) {} nil)))
      (is (= [1 2 3 4 5 6] ((concat [1 2] [3 4] [5 6]) {} nil))))
    (testing "with mixed types"
      (is (= [] ((concat [] '()) {} nil)))
      (is (= [1 2] ((concat [1] '(2)) {} nil)))
      (is (= '(1 2) ((concat '(1) [2]) {} nil))))))

(deftest contains-test
  (let [contains (partial function-transform :contains)]
    (testing "with non-lists"
      (is (thrown? Exception ((contains nil) {} nil)))
      (is (false? ((contains nil "x") {} nil)))
      (is (false? ((contains true "x") {} nil)))
      (is (false? ((contains 40 30) {} nil))))

    (testing "with lists"
      (is (false? ((contains '() nil) {} nil)))
      (is (false? ((contains '() '()) {} nil)))
      (is (false? ((contains '("x"), "y") {} nil)))
      (is (true? ((contains '("x"), "x") {} nil)))
      (is (false? ((contains '(1 2 3) 0) {} nil)))
      (is (true? ((contains '(1 2 3) 3) {} nil))))

    (testing "with vectors"
      (is (false? ((contains [] nil) {} nil)))
      (is (false? ((contains [] 1) {} nil)))
      (is (true? ((contains ["x"], "x") {} nil)))
      (is (false? ((contains ["x"], "y") {} nil)))
      (is (true? ((contains ["x" "y" "z"], "z") {} nil)))
      (is (false? ((contains [1 2 3] 4) {} nil)))
      (is (true? ((contains [1 2 3 4] 4) {} nil))))

    (testing "with strings"
      (is (false? ((contains "x" nil) {} nil)))
      (is (true? ((contains "x" "x") {} nil)))
      (is (true? ((contains "x" "") {} nil)))
      (is (true? ((contains "xyz" "x") {} nil)))
      (is (true? ((contains "hello world" " ") {} nil)))
      (is (false? ((contains "abcd" "x") {} nil)))
      (is (true? ((contains "abcd" "abc") {} nil)))
      (is (true? ((contains "abcd" "bcd") {} nil)))
      (is (true? ((contains "hello world" "world") {} nil)))
      (is (false? ((contains "hello world" "world?") {} nil)))
      (is (true? ((contains "[false]" false) {} nil)))
      (is (false? ((contains "99 red balloons" 1) {} nil)))
      (is (true? ((contains "99 red balloons" 99) {} nil))))))

(deftest distinct-test
  (let [distinct (partial function-transform :distinct)]
    (testing "with nil"
      (is (nil? ((distinct nil) {} nil)))
      (is (nil? ((distinct :col1) {:col1 nil} nil))))
    (testing "unsupported data types"
      (is (nil? ((distinct "x") {} nil)))
      (is (nil? ((distinct 1) {} nil)))
      (is (nil? ((distinct true) {} nil)))
      (is (nil? ((distinct (time/now)) {} nil))))
    (testing "with list"
      (is (= '() ((distinct '()) {} nil)))
      (is (= '("x") ((distinct '("x")) {} nil)))
      (is (= '(1 2 3) ((distinct '(1 2 3)) {} nil)))
      (is (= '(1 2) ((distinct '(1 2 2 1)) {} nil)))
      (is (= '("x") ((distinct '("x" "x" "x")) {} nil))))
    (testing "with vector"
      (is (= [] ((distinct []) {} nil)))
      (is (= ["x"] ((distinct ["x"]) {} nil)))
      (is (= [1 2 3] ((distinct [1 2 3]) {} nil)))
      (is (= [1 2] ((distinct [1 1 2 2]) {} nil)))
      (is (= ["x" "y"] ((distinct ["x" "y" "x" "y"]) {} nil))))))

(deftest flatten-test
  (let [flatten (partial function-transform :flatten)]
    (testing "with nil"
      (is (nil? ((flatten nil) {} nil))))
    (testing "with list"
      (is (= '() ((flatten '()) {} nil)))
      (is (= '() ((flatten '([])) {} nil)))
      (is (= '("x" "y") ((flatten (list '("x") '("y"))) {} nil)))
      (is (= '("x" "y" "z") ((flatten (list '("x") '("y") '("z"))) {} nil)))
      (is (= '("a" "x" "y" "z") ((flatten (list "a" '("x") '("y") "z")) {} nil))))
    (testing "with vectors"
      (is (= [] ((flatten []) {} nil)))
      (is (= [] ((flatten [[]]) {} nil)))
      (is (= ["x" "y"] ((flatten [["x"], "y"]) {} nil)))
      (is (= ["x" "y"] ((flatten [["x"], ["y"]]) {} nil)))
      (is (= ["a" "x" "y" "z"] ((flatten ["a" ["x"], ["y"] "z"]) {} nil))))
    (testing "with mixed types"
      (is (= [] ((flatten [[] '()]) {} nil)))
      (is (= [1 2] ((flatten [[1] '(2)]) {} nil)))
      (is (= '(1 2) ((flatten (list '(1) [2])) {} nil))))))

(deftest flattendeep-test
  (let [flattendeep (partial function-transform :flattendeep)]
    (testing "with nil"
      (is (nil? ((flattendeep nil) {} nil))))
    (testing "with list"
      (is (= '() ((flattendeep '()) {} nil)))
      (is (= '() ((flattendeep '([])) {} nil)))
      (is (= '("x" "y") ((flattendeep (list '("x") '("y"))) {} nil)))
      (is (= '("x" "y" "z") ((flattendeep (list '("x") '("y") '("z"))) {} nil)))
      (is (= '("a" "x" "y" "z") ((flattendeep (list "a" '("x") '("y") "z")) {} nil)))
      (is (= '("a" "x" "y" "z") ((flattendeep (list "a" (list (list "x") (list "y") "z"))) {} nil)))
      (is (= '("a" "x" "y" "z") ((flattendeep (list (list "a" (list (list "x") (list "y") "z")))) {} nil))))
    (testing "with vectors"
      (is (= [] ((flattendeep []) {} nil)))
      (is (= [] ((flattendeep [[]]) {} nil)))
      (is (= ["x" "y"] ((flattendeep [["x"], "y"]) {} nil)))
      (is (= ["x" "y"] ((flattendeep [["x"], ["y"]]) {} nil)))
      (is (= ["a" "x" "y" "z"] ((flattendeep ["a" ["x"], ["y"] "z"]) {} nil)))
      (is (= ["a" "x" "y" "z"] ((flattendeep ["a" [["x"], ["y"] "z"]]) {} nil)))
      (is (= ["a" "x" "y" "z"] ((flattendeep [["a" ["x"], ["y"] "z"]]) {} nil))))
    (testing "with mixed types"
      (is (= [] ((flattendeep [[] '()]) {} nil)))
      (is (= [1 2] ((flattendeep [[1] '(2)]) {} nil)))
      (is (= '(1 2 3) ((flattendeep (list '(1) [[2] 3])) {} nil))))))
