(ns parsec.parser-test
  (:require [clojure.test :refer :all]
            [parsec.parser :refer :all]
            [parsec.test-helpers :refer :all]
            [instaparse.core :as insta]))

(defn test-parsec-parse [string]
  (is (false? (insta/failure? (parsec-parser string)))))

(deftest whitespace-test
  (testing "with all spaces"
    (is (false? (insta/failure? (whitespace " ")))))
  (testing "without newline and tab"
    (is (false? (insta/failure? (whitespace " \n \t ")))))
  (testing "with characters"
    (is (true? (insta/failure? (whitespace " g "))))))

(deftest whitespace-or-comments-test
  (testing "with line comment"
    (is (false? (insta/failure? (whitespace-or-comments " /* comment */")))))
  (testing "with block comment"
    (is (false? (insta/failure? (whitespace-or-comments " // comment\n")))))
  (testing "with block comment at end of string"
    (is (false? (insta/failure? (whitespace-or-comments " // comment")))))
  (testing "with two comments"
    (is (false? (insta/failure? (whitespace-or-comments "/*comment1*//*comment2*/\n")))))
  (testing "with multiple comments"
    (is (false? (insta/failure? (whitespace-or-comments "\n/* first comment */\n/* second comment */\n")))))
  (testing "with characters"
    (is (true? (insta/failure? (whitespace-or-comments " g /* help */"))))))

(deftest query-parse-test
  (testing "various queries for parse errors (does not verify accuracy of parsing)"
    (test-parsec-parse "input mock")
    (test-parsec-parse "input mock;")
    (test-parsec-parse "input\n\tmock\t /* comment */")
    (test-parsec-parse "/* comment */ INPUT  mock\t /* comment */ | xx=yy")
    (test-parsec-parse "// line comment\n\ninput mock // input\n|x=y // assignment\n")
    (test-parsec-parse "input mock|x=y")
    (test-parsec-parse "input mock | x = y")
    (test-parsec-parse "input mock | x=y")
    (test-parsec-parse "input mock; input jdbc")
    (test-parsec-parse "input mock; input jdbc;")
    (test-parsec-parse "input jdbc | filter col1==1")
    (test-parsec-parse "input jdbc | a = x + 4")
    (test-parsec-parse "input jdbc | a = -x")
    (test-parsec-parse "input jdbc | a = x--5")
    (test-parsec-parse "input jdbc | a = -1")
    (test-parsec-parse "input jdbc | a = 0")
    (test-parsec-parse "input jdbc | a = -3.4e-3")
    (test-parsec-parse "input jdbc | col_2 = col-2")
    (test-parsec-parse "input jdbc x=''")
    (test-parsec-parse "input jdbc x='''test'''")
    (test-parsec-parse "input jdbc x='y'")
    (test-parsec-parse "input jdbc x='a \\n b'")
    (test-parsec-parse "input jdbc x='y' z=0")
    (test-parsec-parse "input jdbc x='y', z=0")
    (test-parsec-parse "input jdbc x='000', z=0, y=true")
    (test-parsec-parse "input mock name='thurstone' | stats x = count(*) by x")
    (test-parsec-parse "input mock | x = 4^y; input net | y= z ;input mock")
    (test-parsec-parse "set @size = tonumber(replace('100gb', '', ''))")
    (test-parsec-parse "set @size = tonumber(replace(\"100gb\", \"\", \"\"))")
    (test-parsec-parse "set @x = '', @y = ''")
    (test-parsec-parse "set @x = \"\", @y = \"\"")
    (test-parsec-parse "set @x = '''''', @y = ''''''")
    (test-parsec-parse (str "set @x = \"" (apply str (range 0 900)) ""\"))
    (test-parsec-parse "set @tau = -> pi()*2")
    (test-parsec-parse "set @tau = () -> pi()*2")
    (test-parsec-parse "set @cube = (n) -> n*n*n")
    (test-parsec-parse "set @cube = n -> n*n*n")
    (test-parsec-parse "set @isOdd = (n) -> gcd(n,2) != 2, @isThirteenOdd = apply(@isOdd, 13)")
    (test-parsec-parse "set @sqr = (n) -> n*n, @cube = (n) -> n*n*n")))

(deftest query-test
  (testing-parser
    "with a single query" :Q
    "input mock" '([:query [:input-statement :mock [:function :tolowercasemap]]]))

  (testing-parser
    "with a single query, capitalized" :Q
    "INPUT mock" '([:query [:input-statement :mock [:function :tolowercasemap]]]))

  (testing-parser
    "with a single query and spaces" :Q
    "  INPUT mock  " '([:query [:input-statement :mock [:function :tolowercasemap]]]))

  (testing-parser
    "with two queries" :Q
    "input mock; input mock" '([:query [:input-statement :mock [:function :tolowercasemap]]]
                                [:query [:input-statement :mock [:function :tolowercasemap]]]))

  (testing-parser
    "with two queries and spaces" :Q
    "input mock ; input mock" '([:query [:input-statement :mock [:function :tolowercasemap]]]
                                 [:query [:input-statement :mock [:function :tolowercasemap]]]))
  (testing-parser
    "with two queries and trailing semicolonr" :Q
    "input mock ; input mock;" '([:query [:input-statement :mock [:function :tolowercasemap]]]
                                 [:query [:input-statement :mock [:function :tolowercasemap]]]))

  (testing-parser
    "with multiple statements" :Q
    "input mock| head 1" '([:query
                            [:input-statement :mock [:function :tolowercasemap]]
                            [:head-statement 1]]))

  (testing-parser
    "with multiple statements and spaces" :Q
    "input mock | col1=   5" '([:query
                                [:input-statement :mock [:function :tolowercasemap]]
                                [:assignment-statement [:assignment-terms [:col1 [:expression 5]]]]]))

  (testing-parser
    "with newlines as whitespace" :Q
    "INPUT\nmock" '([:query [:input-statement :mock [:function :tolowercasemap]]]))

  (testing-parser
    "with tabs as whitespace" :Q
    "INPUT\tmock\n" '([:query [:input-statement :mock [:function :tolowercasemap]]]))

  (testing-parser
    "with block comment at the end" :Q
    "INPUT mock /* block comment */" '([:query [:input-statement :mock [:function :tolowercasemap]]]))

  (testing-parser
    "with block comment at the beginning" :Q
    "/* block comment */INPUT mock" '([:query [:input-statement :mock [:function :tolowercasemap]]]))

  (testing-parser
    "with block comment in the middle" :Q
    "INPUT /* block comment */ mock" '([:query [:input-statement :mock [:function :tolowercasemap]]]))

  (testing-parser
    "with line comment at the end" :Q
    "INPUT mock // line comment" '([:query [:input-statement :mock [:function :tolowercasemap]]]))

  (testing-parser
    "with line comment at the beginning" :Q
    "// line comment \nINPUT mock" '([:query [:input-statement :mock [:function :tolowercasemap]]]))

  (testing-parser
    "with line comment in the middle" :Q
    "INPUT // line // comment \n mock" '([:query [:input-statement :mock [:function :tolowercasemap]]]))

  (let [expected '([:query
                    [:input-statement :mock [:function :tolowercasemap]]
                    [:filter-statement
                     [:expression
                      [:and-operation
                       [:equals-expression :col1 4]
                       [:equals-expression
                        :col2
                        [:addition-operation 5 :col3]]]]]
                    [:assignment-statement
                     [:assignment-terms
                      [:col6 [:expression [:addition-operation :col1 :col5]]]]]])]
    (testing-parser "with input, filter, and assignment" :Q "input mock|filter col1==4 and col2==5+col3|col6=col1+col5" expected)
    (testing-parser "with input, filter, and assignment" :Q "input mock | filter col1 == 4 and col2 == 5 + col3 | col6 = col1 + col5" expected)
    (testing-parser "with input, filter, and assignment" :Q "input mock\n| filter col1 == 4 and col2 == 5 + col3\n| col6 = col1 + col5\n" expected)
    (testing-parser "with input, filter, and assignment" :Q "input mock\n\t | filter col1 == 4 and col2 == 5 + col3\n\t | col6 = col1 + col5\n" expected)
    (testing-parser "with input, filter, and assignment" :Q "INPUT  mock | FILTER col1 == 4 \n\t\t AND col2 == 5 + col3 | col6 = col1 + col5  \n " expected)
    (testing-parser "with input, filter, and assignment" :Q "\t\t\t\t\t\tINPUT\t\t\t\t\t\t\t\t\t\tmock|filter col1==4\t\t\t\tand\t\t\t\t\t\tcol2==5+col3|col6=col1+col5\t\t\t" expected)
    (testing-parser "with input, filter, and assignment" :Q "\n\n/* COMMENT \n \t COMMENT */\n\n\n\n\n input mock | filter col1 == 4 and col2 == 5 + col3 | col6 = col1 + col5" expected)
    (testing-parser "with input, filter, and assignment" :Q "/* \n\tCOMMENT input jdbc \n */ input  mock /* ?? */\n \t| filter col1 == 4 and/* not */col2 == 5 + col3 \n \t| col6/*!*/=col1 + col5" expected)
    (testing-parser "with input, filter, and assignment" :Q "input mock|// line comment\nfilter col1==4 and col2==5+col3|col6=col1+col5" expected)))
