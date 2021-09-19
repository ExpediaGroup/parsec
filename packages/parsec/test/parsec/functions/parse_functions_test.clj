(ns parsec.functions.parse-functions-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.functions :refer :all]
            [parsec.functions.parsingfunctions :refer :all]))

(deftest parsejson-test
  (let [parsejson (partial function-transform :parsejson)]
    (testing "with nil"
      (is (nil? ((parsejson nil) {} nil)))
      (is (nil? ((parsejson :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (= 0 ((parsejson "0") {} nil)))
      (is (= 1.0 ((parsejson "1.0") {} nil))))
    (testing "with string"
      (is (= "hello world" ((parsejson "\"hello world\"") {} nil))))
    (testing "with boolean"
      (is (false? ((parsejson "false") {} nil)))
      (is (true? ((parsejson "true") {} nil))))
    (testing "with list"
      (is (= '() ((parsejson "[]") {} nil)))
      (is (= '(1 2) ((parsejson "[1, 2]") {} nil)))
      (is (sequential? ((parsejson "[1, 2]") {} nil))))
    (testing "with map"
      (is (map? ((parsejson "{}") {} nil)))
      (is (= { :a 1 :b 2 } ((parsejson "{\"a\": 1, \"b\": 2}") {} nil)))
      (is (= { :a [1 2] :b { :c true } } ((parsejson "{\"a\": [1, 2], \"b\": { \"c\": true }}") {} nil))))))

(deftest parsecsv-test
  (let [parsecsv (partial function-transform :parsecsv)]
    (testing "with nil"
      (is (nil? ((parsecsv nil) {} nil)))
      (is (nil? ((parsecsv :col1) {:col1 nil} nil))))
    (testing "with non-CSV data"
      (is (= [] ((parsecsv "0") {} nil)))
      (is (= [] ((parsecsv "\"hello world\"") {} nil)))
      (is (= [] ((parsecsv "false") {} nil)))
      (is (= [] ((parsecsv "true") {} nil)))
      (is (= [] ((parsecsv "[1, 2]") {} nil))))
    (testing "with CSV string"
      (is (= [{:a "1" :b "2" :c "3"}] ((parsecsv "a,b,c\n1,2,3") {} nil)))
      (is (= [{:a "1" :b "2" :c "3"} {:a "4" :b "5" :c "6"}] ((parsecsv "a,b,c\r\n1,2,3\r\n4,5,6") {} nil))))
    (testing "with options"
      (is (= [{:a "1" :b "2" :c "3"} {:a "4" :b "5" :c "6"}] ((parsecsv "a,b,c\t1,2,3\t4,5,6" { :eol "\t" }) {} nil)))
      (is (= [{:a "1" :b "2" :c "3"} {:a "4" :b "5" :c "6"}] ((parsecsv "a\tb\tc\n1\t2\t3\n4\t5\t6" { :delimiter "\t" }) {} nil)))
      (is (= [{:a "1" :b "2" :c "3"} {:a "4" :b "5" :c "6"}] ((parsecsv "`a`,`b`,`c`\n1,2,3\n4,5,6" { :quote "`" }) {} nil)))
      (is (= [{:a "hello world" :b "2" :c "3"} {:a "goodbye world" :b "5" :c "6"}] ((parsecsv "a-b-c\n`hello world`-2-3\n`goodbye world`-5-6" { :quote "`", :delimiter "-" }) {} nil))))
    (testing "with headers"
      (is (= [{:a "1" :b "2" :c "3"} {:a "4" :b "5" :c "6"}] ((parsecsv "a,b,c\n1,2,3\r\n4,5,6" { :headers true }) {} nil)))
      (is (= [{:a "1" :b "2" :c "3"} {:a "4" :b "5" :c "6"}] ((parsecsv "1,2,3\r\n4,5,6" { :headers ["a","b","c"] }) {} nil)))
      (is (= [{:col0 "1" :col1 "2" :col2 "3"} {:col0 "4" :col1 "5" :col2 "6"}] ((parsecsv "1,2,3\r\n4,5,6" { :headers false }) {} nil)))
      )))

(deftest parsexml-test
  (let [parsexml (partial function-transform :parsexml)]
    (testing "with nil"
      (is (nil? ((parsexml nil) {} nil)))
      (is (nil? ((parsexml :col1) {:col1 nil} nil))))
    (testing "with non-XML data"
      (is (thrown? Exception ((parsexml "0") {} nil)))
      (is (thrown? Exception ((parsexml "\"hello world\"") {} nil)))
      (is (thrown? Exception ((parsexml "false") {} nil)))
      (is (thrown? Exception ((parsexml "true") {} nil)))
      (is (thrown? Exception ((parsexml "[1, 2]") {} nil))))
    (testing "with XML string"
      (is (= [{:root "one"}] ((parsexml "<root>one</root>") {} nil)))
      (is (= [{:root "one" :v "1"}] ((parsexml "<root v=\"1\">one</root>") {} nil)))
      (is (= [{:root "one" :v "1" :w "ok"}] ((parsexml "<root v=\"1\" w=\"ok\">one</root>") {} nil)))
      (is (= [{:a "one" :b "two"}] ((parsexml "<root><a>one</a><b>two</b></root>") {} nil)))
      (is (= [{:a "two"}] ((parsexml "<root><a>one</a><a>two</a></root>") {} nil))))
    (testing "with raw mode"
      (is (= #clojure.data.xml.Element{ :tag :root :attrs {} :content ["one"] } ((parsexml "<root>one</root>" { :xpath "/root" :raw true }) {} nil))))
    (testing "with xpath"
      (is (= [{:a "one"} {:a "two"}] ((parsexml "<root><a>one</a><a>two</a></root>" { :xpath "/root/a" }) {} nil)))
      (is (= [{:a "one"} {:a "two"}] ((parsexml "<root><subroot><a>one</a><a>two</a></subroot></root>" { :xpath "/root/subroot/a" }) {} nil)))
      (is (= [{:a "one"} {:a "two"}] ((parsexml "<root><subroot><a>one</a><a>two</a></subroot></root>" { :xpath "//a" }) {} nil)))
      (is (= [{:a "one"} {:b "two"}] ((parsexml "<root><subroot><a>one</a><b>two</b></subroot></root>" { :xpath "/root/subroot/a|/root/subroot/b" }) {} nil))))
    (testing "with flatten"
      (is (= [{:a "one" :b "two"}] ((parsexml "<root><a>one</a><b>two</b></root>" { :xpath "/root" :flatten true}) {} nil)))
      (is (= [{ :children [{:a "one"}{:b "two"}] }] ((parsexml "<root><a>one</a><b>two</b></root>" { :xpath "/root" :flatten false}) {} nil)))
      (is (= [{ :root "root-text" :children [{:a "one"}{:b "two"}] }] ((parsexml "<root>root-text<a>one</a><b>two</b></root>" { :xpath "/root" :flatten false}) {} nil))))
    ))

(deftest jsonpath-test
  (let [jsonpath (partial function-transform :jsonpath)]
    (testing "with nil"
      (is (nil? ((jsonpath nil) {} nil)))
      (is (nil? ((jsonpath :col1) {:col1 nil} nil))))
    (testing "with unexpected types"
      (is (nil? ((jsonpath "0") {} nil)))
      (is (nil? ((jsonpath "1.0") {} nil))))
      (is (nil? ((jsonpath "\"hello world\"") {} nil)))
      (is (nil? ((jsonpath "false") {} nil)))
      (is (nil? ((jsonpath "true") {} nil)))
    (testing "with list"
      (is (= [1 2 3] ((jsonpath [1 2 3] "$") {} nil)))
      (is (= [1 2 3] ((jsonpath [1 2 3] "$.") {} nil)))
      (is (= ["Joe" "Bob"] ((jsonpath [{:name "Joe"} {:name "Bob"}] "$.name") {} nil))))
    (testing "with map"
      (is (= {} ((jsonpath {} "$") {} nil)))
      (is (= {} ((jsonpath {} "$.") {} nil)))
      (is (= [1 2 3] ((jsonpath {:messages [1 2 3]} "$.messages") {} nil)))
      (is (= [1 2 3] ((jsonpath {:messages [1 2 3]} "$.messages[*]") {} nil)))
      (is (= [1 2 3] ((jsonpath {:response {:messages [1 2 3]}} "$.response.messages[*]") {} nil)))
      (is (= [1 2 3 4] ((jsonpath {:in {:messages [1 2 ]} :out {:messages [3 4]}} "$..messages[*]") {} nil))))))
