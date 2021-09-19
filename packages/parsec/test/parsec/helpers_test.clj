(ns parsec.helpers-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]))

(deftest always-predicate-test
  (testing "with nil"
    (is (true? (always-predicate nil nil)))))

(deftest any-pred?-test
  (testing "with nils"
    (is (false? (any-pred? true? nil))))
  (testing "with vector of booleans"
    (is (true? (any-pred? true? [false true false])))
    (is (false? (any-pred? true? [false false false])))
    (is (true? (any-pred? false? [false true false])))
    (is (false? (any-pred? false? [true true true])))))

(deftest in?-test
  (testing "with empty list"
    (is (false? (in? '() 1)))
    (is (false? (in? '() nil))))
  (testing "with empty vector"
    (is (false? (in? [] 1)))
    (is (false? (in? [] nil))))
  (testing "with nil list"
    (is (false? (in? nil 1)))
    (is (false? (in? nil nil))))
  (testing "with lists"
    (is (true? (in? '(nil) nil)))
    (is (true? (in? '(nil nil) nil)))
    (is (true? (in? '(1 2 nil 3) nil)))
    (is (false? (in? '(1 2 3 4) nil)))
    (is (false? (in? '(1 2 3 4) 5)))
    (is (true? (in? '(1 2 3 4) 3)))
    (is (false? (in? '("x" "y" "z") "w")))
    (is (true? (in? '("x" "y" "z") "x"))))
  (testing "with vectors"
    (is (true? (in? [nil] nil)))
    (is (true? (in? [nil nil] nil)))
    (is (true? (in? [1 2 nil 3] nil)))
    (is (false? (in? [1 2 3 4] nil)))
    (is (false? (in? [1 2 3 4] 5)))
    (is (true? (in? [1 2 3 4] 3)))
    (is (false? (in? ["x" "y" "z"] "w")))
    (is (true? (in? ["x" "y" "z"] "x"))))
  (testing "with strings"
    (is (false? (in? "hello world" nil)))
    (is (true? (in? "hello world" "hello")))
    (is (false? (in? "hello world" "Hello")))
    (is (true? (in? "555-4445" 5)))
    (is (false? (in? "555-1445" 9)))))

(deftest mapmapv-test
  (testing "with nil map"
    (is (nil? (mapmapv identity nil))))
  (testing "with empty map"
    (is (= {} (mapmapv identity {}))))
  (testing "with identity"
    (is (= { :a 1 } (mapmapv identity { :a 1 }))))
  (testing "with fn"
    (is (= { :a 2 :b 2 } (mapmapv #(+ 1 %) { :a 1 :b 1 })))
    (is (= { :a 3 :b 5 } (mapmapv count { :a [1,2,3] :b [4,5,6,7,8] }))))
  (testing "with nil function"
    (is (thrown? NullPointerException (mapmapv nil { :a 1 }))))
  (testing "with vector"
    (is (thrown? UnsupportedOperationException (mapmapv identity [1 2 3])))))

(deftest mapply-test
  (testing "with nil map"
    (is (zero? (mapply + nil))))
  (testing "with empty map"
    (is (zero? (mapply + { }))))
  (testing "with nil map"
    (is (= ":x1:y2" (mapply #(str % %2 %3 %4) { :x 1 :y 2 }))))
  (testing "with empty map"
    (is (= 10 (mapply + { 1 2 3 4 })))))

(deftest mapcat2-test
  (testing "with nil map"
    (is (empty? (mapcat2 identity nil))))
  (testing "with empty list"
    (is (empty? (mapcat2 identity []))))
  (testing "with single element"
    (is (= [1] (mapcat2 identity [1]))))
  (testing "with multiple elements"
    (is (= [1 2 3] (mapcat2 identity [1 2 3]))))
  (testing "with lists returned"
    (is (= [1 2 2 4 3 6] (mapcat2 #(vector % (* 2 %)) [1 2 3])))))

(deftest xor-test
  (testing "with nil"
    (is (false? (xor nil nil))))
  (testing "with booleans"
    (is (false? (xor true true)))
    (is (false? (xor false false)))
    (is (true? (xor false true)))
    (is (true? (xor true false)))))

(deftest eval-expression-test
  (testing "nil"
    (is (nil? (eval-expression nil {} {}))))
  (testing "number"
    (is (= 444 (eval-expression 444 {} {}))))
  (testing "keyword"
    (is (= 155 (eval-expression :col1 { :col1 155 } {}))))
  (testing "variable"
    (is (= 42 (eval-expression [:variable "@V1"] {} { :variables { "@V1" 42 }}))))
  (testing "function"
    (is (= 1 (eval-expression (fn [_ _] 1) {} { :variables { }})))))

(deftest eval-expression-without-row-test
  (testing "nil"
    (is (nil? (eval-expression-without-row nil {}))))
  (testing "number"
    (is (= 444 (eval-expression-without-row 444 {}))))
  (testing "keyword"
    (is (thrown? Exception (eval-expression-without-row :col1 {}))))
  (testing "variable"
    (is (= 42 (eval-expression-without-row [:variable "@V1"] { :variables { "@V1" 42 }}))))
  (testing "function"
    (is (= 1 (eval-expression-without-row (fn [_ _] 1) { :variables { }})))))

(deftest expr-to-string-test
  (testing "with list"
    (is (= "[1,2,3]" (expr-to-string [1,2,3]))))

  (testing "with string"
    (is (= "hello" (expr-to-string "hello"))))

  (testing "with string expression"
    (is (= "hello" (expr-to-string [:expression "hello"]))))

  (testing "with wrapped string expression"
    (is (= "(hello)" (expr-to-string [:expression "hello"] true))))

  (testing "with numerical expression"
    (is (= "-1" (expr-to-string [:expression -1]))))

  (testing "with wrapped numerical expression"
    (is (= "(-1)" (expr-to-string [:expression -1] true))))

  (testing "with boolean expression"
    (is (= "true" (expr-to-string [:expression true]))))

  (testing "with function of one variable"
    (is (= "fn(1)" (expr-to-string [:function :fn [:expression 1]]))))

  (testing "with function of two variables"
    (is (= "fn(x,y)" (expr-to-string [:function :fn [:expression :x] [:expression :y]]))))

  (testing "with avg function"
    (is (= "avg(x)" (expr-to-string [:function :avg [:expression :x]]))))

  (testing "with stddev function"
    (is (= "stddev(x)" (expr-to-string [:function :stddev [:expression :x]]))))

  (testing "with percentile function"
    (is (= "percentile(x,99)" (expr-to-string [:function :percentile [:expression :x] [:expression 99]]))))

  (testing "with isexist function"
    (is (= "isexist(x)" (expr-to-string [:isexist-function [:expression :x]]))))

  (testing "with not expression"
    (is (= "!x" (expr-to-string [:expression [:not-operation :x]]))))

  (testing "with and expression"
    (is (= "x and y" (expr-to-string [:expression [:and-operation :x :y]]))))

  (testing "with or expression"
    (is (= "x or y" (expr-to-string [:expression [:or-operation :x :y]]))))

  (testing "with xor expression"
    (is (= "x xor y" (expr-to-string [:expression [:xor-operation :x :y]]))))

  (testing "with negation expression"
    (is (= "-x" (expr-to-string [:expression [:negation-operation :x]]))))

  (testing "with addition expression"
    (is (= "x+y" (expr-to-string [:expression [:addition-operation :x :y]]))))

  (testing "with subtraction expression"
    (is (= "x-y" (expr-to-string [:expression [:subtraction-operation :x :y]]))))

  (testing "with multiplication expression"
    (is (= "x*y" (expr-to-string [:expression [:multiplication-operation :x :y]]))))

  (testing "with division expression"
    (is (= "x/y" (expr-to-string [:expression [:division-operation :x :y]]))))

  (testing "with equals expression"
    (is (= "x==y" (expr-to-string [:expression [:equals-expression :x :y]]))))

  (testing "with not equals expression"
    (is (= "x!=y" (expr-to-string [:expression [:not-equals-expression :x :y]]))))

  (testing "with less than expression"
    (is (= "x<y" (expr-to-string [:expression [:less-than-expression :x :y]]))))

  (testing "with less or equals expression"
    (is (= "x<=y" (expr-to-string [:expression [:less-or-equals-expression :x :y]]))))

  (testing "with greater than expression"
    (is (= "x>y" (expr-to-string [:expression [:greater-than-expression :x :y]]))))

  (testing "with greater or equals expression"
    (is (= "x>=y" (expr-to-string [:expression [:greater-or-equals-expression :x :y]])))))

(deftest mask-password-in-string-test
  (testing "with no password"
    (is (= "" (mask-password-in-string "")))
    (is (= "input mock" (mask-password-in-string "input mock")))
    (is (= "input mock | select password | sort name asc" (mask-password-in-string "input mock | select password | sort name asc"))))
  (testing "with password in single quotes"
    (is (= "password=''" (mask-password-in-string "password=''")))
    (is (= "password='' query='..'" (mask-password-in-string "password='' query='..'")))
    (is (= "password='******'" (mask-password-in-string "password='secret'")))
    (is (= "password = '******'" (mask-password-in-string "password = 'secret'")))
    (is (= "password='******' | password='***'" (mask-password-in-string "password='secret' | password='abc'")))
    (is (= "user='joe' password='************'" (mask-password-in-string "user='joe' password='secret \\\"x\\\"'"))))
  (testing "with password in double quotes"
    (is (= "password=\"\"" (mask-password-in-string "password=\"\"")))
    (is (= "password=\"******\"" (mask-password-in-string "password=\"secret\"")))
    (is (= "password = \"******\"" (mask-password-in-string "password = \"secret\"")))
    (is (= "password=\"******\" | password=\"***\"" (mask-password-in-string "password=\"secret\" | password=\"abc\"")))
    (is (= "user=\"joe\" password=\"************\"" (mask-password-in-string "user=\"joe\" password=\"secret \\\"x\\\"\""))))
  (testing "with password in triple quotes"
    (is (= "password=''''''" (mask-password-in-string "password=''''''")))
    (is (= "password='''******'''" (mask-password-in-string "password='''secret'''")))
    (is (= "password = '''******'''" (mask-password-in-string "password = '''secret'''")))
    (is (= "password='''******''' | password='''***'''" (mask-password-in-string "password='''secret''' | password='''abc'''")))
    (is (= "user='''joe''' password='''************'''" (mask-password-in-string "user='''joe''' password='''secret \\\"x\\\"'''"))))
  (testing "with unquoted password"
    (is (= "\"password=****;\"" (mask-password-in-string "\"password=mask;\"")))
    (is (= "\"password=****\"" (mask-password-in-string "\"password=mask\"")))
    (is (= " uri=\"jdbc:...?user=bmx&password=****;\" | " (mask-password-in-string " uri=\"jdbc:...?user=bmx&password=mask;\" | ")))
    (is (= " uri=\"jdbc:...?user=bmx&password=****\" | " (mask-password-in-string " uri=\"jdbc:...?user=bmx&password=mask\" | ")))
    (is (= " uri='jdbc:...?user=bmx&password=****;' | " (mask-password-in-string " uri='jdbc:...?user=bmx&password=mask;' | ")))
    (is (= " uri='jdbc:...?user=bmx&password=****' | " (mask-password-in-string " uri='jdbc:...?user=bmx&password=mask' | "))))
  (testing "with password in map in single quotes"
    (is (= "password:''" (mask-password-in-string "password:''")))
    (is (= "password:'******'" (mask-password-in-string "password:'secret'")))
    (is (= "password : '******'" (mask-password-in-string "password : 'secret'")))
    (is (= "password: '******' | password: '***'" (mask-password-in-string "password: 'secret' | password: 'abc'")))
    (is (= "'password': '******' | 'password': '***'" (mask-password-in-string "'password': 'secret' | 'password': 'abc'"))))
  (testing "with password in map in double quotes"
    (is (= "password:\"\"" (mask-password-in-string "password:\"\"")))
    (is (= "password:\"******\"" (mask-password-in-string "password:\"secret\"")))
    (is (= "password : \"******\"" (mask-password-in-string "password : \"secret\"")))
    (is (= "password: \"******\" | password: \"***\"" (mask-password-in-string "password: \"secret\" | password: \"abc\"")))
    (is (= "\"password\": \"******\", \"password\": \"***\"" (mask-password-in-string "\"password\": \"secret\", \"password\": \"abc\""))))
  (testing "with password in map in triple quotes"
    (is (= "password:''''''" (mask-password-in-string "password:''''''")))
    (is (= "password:'''******'''" (mask-password-in-string "password:'''secret'''")))
    (is (= "password : '''******'''" (mask-password-in-string "password : '''secret'''")))
    (is (= "password: '''******''' | password: '''***'''" (mask-password-in-string "password: '''secret''' | password: '''abc'''")))
    (is (= "'''password''': '''******''' | '''password''': '''***'''" (mask-password-in-string "'''password''': '''secret''' | '''password''': '''abc'''"))))
  )

(deftest apply-default-http-options-test
  (testing "with nil"
    (is (nil? (apply-default-http-options nil nil))))
  (testing "with maps"
    (is (= { :x 1 :y 2 } (apply-default-http-options { } { :x 1 :y 2})))
    (is (= { :x 1 :y 2 } (apply-default-http-options { :x 1 :y 2} { })))
    (is (= { :x 0 :y 2 } (apply-default-http-options { :x 0 } { :x 1 :y 2})))
    (is (= { :x 1 :y 2 :z 3 } (apply-default-http-options { :z 3 } { :x 1 :y 2})))
    (is (= { :auth { :user "dave" :password "secret123" :preemptive true } }
           (apply-default-http-options { :user "dave" :password "secret123"} {})))
    (is (= { :auth { :user "dave" :password "secret123" :preemptive true } }
           (apply-default-http-options { :username "dave" :password "secret123"} {})))))

(deftest urldecode-test
  (testing "with string"
    (is (= "hello world" (urldecode "hello+world")))))

(deftest urlencode-test
  (testing "with string"
    (is (= "hello+world" (urlencode "hello world")))))
