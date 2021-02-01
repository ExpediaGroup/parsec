(ns parsec.expressions.expression-parser-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.parser :refer :all]))

(deftest literal-expression-parser-test

  ;; Numbers
  (testing-parser "number with zero" :expression "0" [:expression 0])
  (testing-parser "number with one" :expression "1" [:expression 1])
  (testing-parser "number with negative one" :expression "-1" [:expression -1])
  (testing-parser "number with positive one" :expression "+1" [:expression +1])
  (testing-parser "number with decimal" :expression "1.45" [:expression 29/20])
  (testing-parser "number with leading-zero decimal" :expression "0.45" [:expression 9/20])
  (testing-parser "number with leading-period decimal" :expression ".45" [:expression 9/20])
  (testing-parser "number with negative leading-period decimal" :expression "-.45" [:expression -9/20])
  (testing-parser "number with negative leading zero decimal" :expression "-0.99" [:expression -99/100])
  (testing-parser "number with positive leading zero decimal" :expression "+0.99" [:expression +99/100])
  (testing-parser "number with positive exponent and lowercase E" :expression "1.234e+5" [:expression 123400N])
  (testing-parser "number with positive exponent and uppercase E" :expression "1.234E+5" [:expression 123400N])
  (testing-parser "number with negative exponent and lowercase E" :expression "1.234e-5" [:expression 617/50000000])
  (testing-parser "number with negative exponent and uppercase E" :expression "1.234E-5" [:expression 617/50000000])
  (testing-parser "number with one percent" :expression "1%" [:expression 1/100])
  (testing-parser "number with 10 percent" :expression "10%" [:expression 1/10])
  (testing-parser "number with 100 percent" :expression "100%" [:expression 1])
  (testing-parser "number with negative percent" :expression "-34.5%" [:expression -69/200])

  ;; Booleans
  (testing-parser "boolean true" :expression "true" [:expression true])
  (testing-parser "boolean false" :expression "false" [:expression false])

  ;; Identifiers
  (testing-parser "identifier with one letter" :expression "x" [:expression :x])
  (testing-parser "identifier with multiple letters" :expression "xyz" [:expression :xyz])
  (testing-parser "identifier with underscore" :expression "x_z" [:expression :x_z])
  (testing-parser "identifier with multiple underscores" :expression "x__z" [:expression :x__z])
  (testing-parser "identifier with leading underscore" :expression "_time" [:expression :_time])
  (testing-parser "identifier with leading and trailing underscores" :expression "__index__" [:expression :__index__])

  ;; Backtick identifiers
  (testing-parser "backtick identifier with one letter" :expression "`x`" [:expression :x])
  (testing-parser "backtick identifier with multiple letters" :expression "`xyz`" [:expression :xyz])
  (testing-parser "backtick identifier with numbers" :expression "`42`" [:expression :42])
  (testing-parser "backtick identifier with underscore" :expression "`x_z`" [:expression :x_z])
  (testing-parser "backtick identifier with multiple underscores" :expression "`x__z`" [:expression :x__z])
  (testing-parser "backtick identifier with dash" :expression "`x-z`" [:expression :x-z])
  (testing-parser "backtick identifier with space" :expression "`x z`" [:expression (keyword "x z")])
  (testing-parser "backtick identifier with exclamation" :expression "`x!`" [:expression (keyword "x!")])
  (testing-parser "backtick identifier with special chararcters" :expression "`%#ok`" [:expression (keyword "%#ok")])
  (testing-parser "backtick identifier with multiple words" :expression "`travel id`" [:expression (keyword "travel id")])

  ;; Variables
  (testing-parser "variable with one letter" :expression "@x" [:expression [:variable "@x"]])
  (testing-parser "variable with multiple letters" :expression "@xyz" [:expression [:variable "@xyz"]])
  (testing-parser "variable with underscore" :expression "@x_z" [:expression [:variable "@x_z"]])
  (testing-parser "variable with multiple underscores" :expression "@x__z" [:expression [:variable "@x__z"]])
  (testing-parser "variable with leading underscore" :expression "@_time" [:expression [:variable "@_time"]])
  (testing-parser "variable with leading and trailing underscores" :expression "@__index__" [:expression [:variable "@__index__"]])
  (testing-parser "variable with trailing prime" :expression "@x'" [:expression [:variable "@x'"]])

  ;; Strings
  (testing-parser "single-quoted string, empty" :expression "''" [:expression ""])
  (testing-parser "single-quoted string with one letter" :expression "'x'" [:expression "x"])
  (testing-parser "single-quoted string with numbers" :expression "'101'" [:expression "101"])
  (testing-parser "single-quoted string with multiple letters" :expression "'xyz'" [:expression "xyz"])
  (testing-parser "single-quoted string with multiple words" :expression "'hello world'" [:expression "hello world"])
  (testing-parser "single-quoted string with double-quote" :expression "'x\"y'" [:expression "x\"y"])
  (testing-parser "single-quoted string with chinese" :expression "'深圳'" [:expression "深圳"])
  (testing-parser "single-quoted string with newline" :expression "'line1\nline2'" [:expression "line1\nline2"])
  (testing-parser "single-quoted string with escaped newline" :expression "'line1\\nline2'" [:expression "line1\nline2"])
  (testing-parser "single-quoted string with escaped tab" :expression "'line1\\tline2'" [:expression "line1\tline2"])
  (testing-parser "single-quoted string with single-quote" :expression "'don\\'t'" [:expression "don't"])

  (testing-parser "double-quoted string, empty" :expression "\"\"" [:expression ""])
  (testing-parser "double-quoted string with one letter" :expression "\"x\"" [:expression "x"])
  (testing-parser "double-quoted string with numbers" :expression "\"101\"" [:expression "101"])
  (testing-parser "double-quoted string with multiple letters" :expression "\"xyz\"" [:expression "xyz"])
  (testing-parser "double-quoted string with multiple words" :expression "\"hello world\"" [:expression "hello world"])
  (testing-parser "double-quoted string with double-quote" :expression "\"x\\\"y\"" [:expression "x\"y"])
  (testing-parser "double-quoted string with chinese" :expression "\"深圳\"" [:expression "深圳"])
  (testing-parser "double-quoted string with newline" :expression "\"line1\nline2\"" [:expression "line1\nline2"])
  (testing-parser "double-quoted string with escaped newline" :expression "\"line1\\nline2\"" [:expression "line1\nline2"])
  (testing-parser "double-quoted string with escaped tab" :expression "\"line1\\tline2\"" [:expression "line1\tline2"])
  (testing-parser "double-quoted string with single-quote" :expression "\"don't\"" [:expression "don't"])

  (testing-parser "triple-quoted string, empty" :expression "''''''" [:expression ""])
  (testing-parser "triple-quoted string with one letter" :expression "'''x'''" [:expression "x"])
  (testing-parser "triple-quoted string with numbers" :expression "'''101'''" [:expression "101"])
  (testing-parser "triple-quoted string with multiple letters" :expression "'''xyz'''" [:expression "xyz"])
  (testing-parser "triple-quoted string with multiple words" :expression "'''hello world'''" [:expression "hello world"])
  (testing-parser "triple-quoted string with chinese" :expression "'''深圳'''" [:expression "深圳"])
  (testing-parser "triple-quoted string with newline" :expression "'''line1\nline2'''" [:expression "line1\nline2"])
  (testing-parser "triple-quoted string with escaped newline" :expression "'''line1\\nline2'''" [:expression "line1\nline2"])
  (testing-parser "triple-quoted string with escaped tab" :expression "'''line1\\tline2'''" [:expression "line1\tline2"])
  (testing-parser "triple-quoted string with single-quote" :expression "'''don't'''" [:expression "don't"])
  (testing-parser "triple-quoted string with double-quote" :expression "'''x\"y'''" [:expression "x\"y"])
  (testing-parser "triple-quoted string with double-quotes and single-quotes" :expression "'''ya'll don't \"care\"'''" [:expression "ya'll don't \"care\""])
  (testing-parser "triple-quoted string with escaped triple-quote" :expression "'''one \\''' two'''" [:expression "one ''' two"])

  ;; Nulls
  (testing-parser "null" :expression "null" [:expression nil])
  (testing-parser "null in parentheses" :expression "(null)" [:expression [:expression nil]])

  ;; Star
  (testing-parser "star" :expression "*" [:expression [:star]])

  ;; List
  (testing-parser "empty list" :expression "[]" [:expression [:function :tolist]])
  (testing-parser "list with nil" :expression "[null]" [:expression [:function :tolist [:expression nil]]])
  (testing-parser "list with keyword" :expression "[x]" [:expression [:function :tolist [:expression :x]]])
  (testing-parser "list with number" :expression "[101]" [:expression [:function :tolist [:expression 101]]])
  (testing-parser "list with two numbers" :expression "[1,2]" [:expression [:function :tolist [:expression 1] [:expression 2]]])
  (testing-parser "list with multiple numbers" :expression "[1, 2, 3]" [:expression [:function :tolist [:expression 1] [:expression 2] [:expression 3]]])
  (testing-parser "list with multiple numbers and percents" :expression "[10%, 0%, 99%]" [:expression [:function :tolist [:expression 1/10] [:expression 0] [:expression 99/100]]])
  (testing-parser "list with string" :expression "['xyz']" [:expression [:function :tolist [:expression "xyz"]]])
  (testing-parser "list with multiple strings" :expression "['xyz','zy']" [:expression [:function :tolist [:expression "xyz"] [:expression "zy"]]])
  (testing-parser "list with boolean" :expression "[true, false]" [:expression [:function :tolist [:expression true] [:expression false]]])
  (testing-parser "list with mixed types" :expression "[1,'a',true,x]" [:expression [:function :tolist [:expression 1] [:expression "a"] [:expression true] [:expression :x]]])

  ;; Map
  (testing-parser "empty map" :expression "{}" [:expression [:function :tomap]])
  (testing-parser "map with one key" :expression "{ \"x\": 1}" [:expression [:function :tomap "x" [:expression 1]]])
  (testing-parser "map with two keys" :expression "{ \"x\": 1, \"y\": 2 }" [:expression [:function :tomap "x" [:expression 1] "y" [:expression 2]]])
  (testing-parser "map with nested map" :expression "{ \"x\": { \"y\": 2 } }" [:expression [:function :tomap "x" [:expression [:function :tomap "y" [:expression 2]]]]]))

(deftest function-expression-parser-test

  (testing "with normal functions"

    (testing-parser
      "with no arguments" :expression
      "e()" [:expression [:function :e]])

    (testing-parser
      "with no arguments" :expression
      "pi()" [:expression [:function :pi]])

    (testing-parser
      "with single argument" :expression
      "sin(x)" [:expression
                [:function :sin [:expression :x]]])

    (testing-parser
      "with number in name and null argument" :expression
      " a4(null)" [:expression
                   [:function :a4 [:expression nil]]])

    (testing-parser
      "with underscore in name and boolean argument" :expression
      "a_b(true)" [:expression
                   [:function :a_b [:expression true]]])

    (testing-parser
      "with leading underscore and numerical argument" :expression
      "_x(20)" [:expression
                [:function :_x [:expression 20]]])

    (testing-parser
      "with string argument" :expression
      "fn(\"test\")" [:expression
                      [:function :fn [:expression "test"]]])

    (testing-parser
      "with two identifier arguments" :expression
      "function(x,y)" [:expression
                       [:function :function
                        [:expression :x]
                        [:expression :y]]])

    (testing-parser
      "with two identifier arguements and a space between" :expression
      "pow(x, y)" [:expression
                   [:function :pow
                    [:expression :x]
                    [:expression :y]]])

    (testing-parser
      "with multiple arguments and comment inside" :expression
      "function(x, /* twice is nice */ 2*y, z)" [:expression
                                                 [:function :function
                                                  [:expression :x]
                                                  [:expression [:multiplication-operation 2 :y]]
                                                  [:expression :z]]])
    (testing-parser
      "with preceding negation" :expression
      "-sin(x)" [:expression
                 [:negation-operation
                  [:function :sin [:expression :x]]]])

    (testing-parser
      "with negated argument" :expression
      "sin(-pi)" [:expression
                  [:function :sin [:expression [:negation-operation :pi]]]])

    (testing-parser
      "with exponent on function" :expression
      "cos(x)^2 / y" [:expression
                      [:division-operation
                       [:exponent-operation
                        [:function :cos [:expression :x]]
                        2]
                       :y]])

    (testing-parser
      "" :expression
      "sin(x + y)" [:expression
                    [:function :sin
                     [:expression [:addition-operation :x :y]]]])

    (testing-parser
      "with nested functions" :expression
      "sin(cos(y))" [:expression
                     [:function :sin
                      [:expression [:function :cos
                                    [:expression :y]]]]])

    (testing-parser
      "with more nested functions" :expression
      "sin(x * cos(y))" [:expression
                         [:function :sin
                          [:expression
                           [:multiplication-operation
                            :x
                            [:function :cos [:expression :y]]]]]]))

  (testing "with special functions"
    (testing-parser
      "with identifier" :expression
      "isexist(col1)" [:expression [:isexist-function :col1]])))


(deftest arithmetic-expression-parser-test
  ;; Negation
  (testing-parser
    "with negation of identifier" :expression
    "-x" [:expression [:negation-operation :x]])
  (testing-parser
    "with double-negation of identifier" :expression
    "--x" [:expression [:negation-operation [:negation-operation :x]]])
  (testing-parser
    "with negation of negative number" :expression
    "--10" [:expression [:negation-operation -10]])

  ;; Addition
  (testing-parser
    "with addition of identifier" :expression
    "x + y" [:expression [:addition-operation :x :y]])
  (testing-parser
    "With addition inside parentheses" :expression
    "(x + y)" [:expression [:expression [:addition-operation :x :y]]])
  (testing-parser
    "with addition of identifier and number" :expression
    "x + 99.0" [:expression [:addition-operation :x 99N]])
  (testing-parser
    "with addition of strings" :expression
    "'hello' + 'world'" [:expression [:addition-operation "hello" "world"]])
  (testing-parser
    "with multiple additions" :expression
    "x+y +z" [:expression
              [:addition-operation
               [:addition-operation :x :y]
               :z]])

  ;; Subtraction
  (testing-parser
    "with subtraction of identifiers" :expression
    "x - y" [:expression [:subtraction-operation :x :y]])
  (testing-parser
    "with subtraction of one negated identifier" :expression
    "x--y" [:expression [:subtraction-operation :x [:negation-operation :y]]])
  (testing-parser
    "with subtraction of two negated identifiers" :expression
    "-x - -y" [:expression [:subtraction-operation [:negation-operation :x] [:negation-operation :y]]])
  (testing-parser
    "with subtraction of two negative numbers" :expression
    "-1--2" [:expression [:subtraction-operation -1 -2]])

  ;; Multiplication
  (testing-parser
    "with multiplication of identifiers" :expression
    "x * y" [:expression [:multiplication-operation :x :y]])

  ;; Division
  (testing-parser
    "with division of number and identifier" :expression
    "4.0 / col2" [:expression [:division-operation 4N :col2]])

  ;; Exponent
  (testing-parser
    "with identifier raised to the power of a number" :expression
    "col2^2.0" [:expression [:exponent-operation :col2 2N]])

  ;; Modulus
  (testing-parser
    "with identifier mod number" :expression
    "col2 mod 3" [:expression [:modulus-operation :col2 3]])

  ;; Combinations
  (testing-parser
    "with subtraction and addition" :expression
    "x - y +z" [:expression
                [:addition-operation
                 [:subtraction-operation :x :y]
                 :z]])
  (testing-parser
    "with addition and subtraction" :expression
    "x +y -z" [:expression
               [:subtraction-operation
                [:addition-operation :x :y]
                :z]])

  (testing-parser
    "with addition and multiplication" :expression
    "x+y*z" [:expression
             [:addition-operation
              :x
              [:multiplication-operation :y :z]
              ]])
  (testing-parser
    "with multiplication and addition" :expression
    " x * y + 45" [:expression
                   [:addition-operation
                    [:multiplication-operation :x :y]
                    45
                    ]])

  (testing-parser
    "with addition, multiplication, and subtraction" :expression
    "x + y * z - 3" [:expression
                     [:subtraction-operation
                      [:addition-operation
                       :x
                       [:multiplication-operation :y :z]]
                      3]])
  )

(deftest parenthetical-expression-parser-test

  (testing-parser
    "with number inside parentheses" :expression
    "(1.4)" [:expression [:expression 7/5]])
  (testing-parser
    "with number inside multiple parentheses" :expression
    "((1.4))" [:expression [:expression [:expression 7/5]]])
  (testing-parser
    "with string inside parentheses" :expression
    "('hello')" [:expression [:expression "hello"]])
  (testing-parser
    "with identifier inside parentheses" :expression
    "(x)" [:expression [:expression :x]])

  (testing-parser
    "with negation of parentheses" :expression
    "-(x)"
    [:expression
     [:negation-operation [:expression :x]]])

  (testing-parser
    "with grouped addition and multiplication" :expression
    "(x + y) * z"
    [:expression
     [:multiplication-operation
      [:expression [:addition-operation :x :y]]
      :z]])

  (testing-parser
    "with grouped addition, subtraction, and division" :expression
    "(4 + (x - y) / z) - 1"
    [:expression
     [:subtraction-operation
      [:expression
       [:addition-operation
        4
        [:division-operation
         [:expression
          [:subtraction-operation :x :y]]
         :z]]]
      1]]))

(deftest boolean-expression-parser-test

  (testing-parser
    "with equality of identifiers" :expression
    "x==y" [:expression [:equals-expression :x :y]])
  (testing-parser
    "with equality of booleans" :expression
    "true==false" [:expression [:equals-expression true false]])
  (testing-parser
    "with equality of identifier and number" :expression
    "x==4.0" [:expression [:equals-expression :x 4N]])
  (testing-parser
    "with equality of numbers" :expression
    "1 == 0" [:expression [:equals-expression 1N 0N]])

  (testing-parser
    "with not-equality of identifiers" :expression
    "x!=y" [:expression [:not-equals-expression :x :y]])
  (testing-parser
    "with not-equality of identifier and number" :expression
    "x!=4.0" [:expression [:not-equals-expression :x 4N]])

  (testing-parser
    "with less than inequality of identifiers" :expression
    "x < y" [:expression [:less-than-expression :x :y]])

  (testing-parser
    "with greater than inequality of identifiers" :expression
    "x > y" [:expression [:greater-than-expression :x :y]])

  (testing-parser
    "with less than or equal inequality of identifiers" :expression
    "x <= y" [:expression [:less-or-equals-expression :x :y]])

  (testing-parser
    "with greater than or equal inequality of identifiers" :expression
    "x >= y" [:expression [:greater-or-equals-expression :x :y]])

  (testing-parser
    "with and of identifiers" :expression
    "x and y" [:expression [:and-operation :x :y]])
  (testing-parser
    "with alternate and" :expression
    "x && y" [:expression [:and-operation :x :y]])
  (testing-parser
    "with or of identifiers" :expression
    "x or y" [:expression [:or-operation :x :y]])
  (testing-parser
    "with alternate or" :expression
    "x || y" [:expression [:or-operation :x :y]])
  (testing-parser
    "with exclusive or of identifiers" :expression
    "x xor y" [:expression [:xor-operation :x :y]])
  (testing-parser
    "with alternate xor" :expression
    "x ^^ y" [:expression [:xor-operation :x :y]])
  (testing-parser
    "with not identifier" :expression
    "not x" [:expression [:not-operation :x]])
  (testing-parser
    "with alternate not" :expression
    "!x" [:expression [:not-operation :x]])
  (testing-parser
    "with double-not of identifier" :expression
    "!!x" [:expression [:not-operation [:not-operation :x]]])

  (testing-parser
    "with and / or" :expression
    "x and y or z" [:expression [:or-operation [:and-operation :x :y] :z]])
  (testing-parser
    "with or / and" :expression
    "x or y and z" [:expression [:or-operation :x [:and-operation :y :z]]])
  (testing-parser
    "with multiple or" :expression
    "x or y or z" [:expression [:or-operation [:or-operation :x :y] :z]])
  (testing-parser
    "with or / not" :expression
    "x or not y" [:expression [:or-operation :x [:not-operation :y]]])
  (testing-parser
    "with not / and not" :expression
    "not x and not y" [:expression [:and-operation [:not-operation :x] [:not-operation :y]]])
  (testing-parser
    "with and in parentheses" :expression
    "(x and y)" [:expression [:expression [:and-operation :x :y]]])
  (testing-parser
    "with and of identifiers wrapped in parentheses" :expression
    "((x) and (y))" [:expression [:expression [:and-operation [:expression :x] [:expression :y]]]])
  (testing-parser
    "with not of and wrapped in parentheses" :expression
    "not (x and y)" [:expression [:not-operation [:expression [:and-operation :x :y]]]])
  (testing-parser
    "with and of two not-identifiers in parentheses" :expression
    "(not x) and (not y)" [:expression [:and-operation [:expression [:not-operation :x]] [:expression [:not-operation :y]]]])
  (testing-parser
    "with and and one identifier in parentheses" :expression
    "x and (y)" [:expression [:and-operation :x [:expression :y]]])
  (testing-parser
    "with or / and in parentheses" :expression
    "x or (y and z)" [:expression [:or-operation :x [:expression [:and-operation :y :z]]]])
  (testing-parser
    "with and / and" :expression
    "(x and y) and z" [:expression [:and-operation [:expression [:and-operation :x :y]] :z]])
  (testing-parser
    "with and / or / and" :expression
    "(x and y) or (z and w)" [:expression [:or-operation [:expression [:and-operation :x :y]] [:expression [:and-operation :z :w]]]])

  (testing-parser
    "with xor of booleans" :expression
    "true xor false" [:expression [:xor-operation true false]])

  (testing "with equality precedence"
    (testing-parser
      "with and / equality" :expression
      "z == x and y"
      [:expression
       [:and-operation
        [:equals-expression :z :x]
        :y]])

    (testing-parser
      "with and / equality and not-equality" :expression
      "4.0 == x and y != 0"                                 ; (4.0 == x) and (y != 0)
      [:expression
       [:and-operation
        [:equals-expression 4N :x]
        [:not-equals-expression :y 0N]]])

    (testing-parser
      "with addition, equality, and" :expression
      "2 + x == y && x==5"                                  ; ((2 + x) == y) && (x == 5)
      [:expression
       [:and-operation
        [:equals-expression
         [:addition-operation 2N :x]
         :y]
        [:equals-expression :x 5N]]])

    (testing-parser
      "with not, not-equality, or, and addition" :expression
      "not x != y or z + 4"                                 ; ((not x) != y) or (z + 4)
      [:expression
       [:or-operation
        [:not-equals-expression
         [:not-operation :x]
         :y]
        [:addition-operation
         :z
         4N]]])

    (testing-parser
      "with inequality and equality" :expression
      "x < y == y > z"
      [:expression
       [:equals-expression
        [:less-than-expression :x :y]
        [:greater-than-expression :y :z]]])

    (testing-parser
      "with and / and" :expression
      "x and y and z"
      [:expression
       [:and-operation
        [:and-operation :x :y]
        :z]])

    (testing-parser
      "with and / or / and" :expression
      "x and y or y and z"
      [:expression
       [:or-operation
        [:and-operation :x :y]
        [:and-operation :y :z]]])

    (testing-parser
      "with xor / or" :expression
      "x xor y or z"
      [:expression
       [:or-operation
        [:xor-operation :x :y]
        :z]])

    (testing-parser
      "with or / xor" :expression
      "x or y xor z"
      [:expression
       [:xor-operation
        [:or-operation :x :y]
        :z]])

    (testing-parser
      "with or / and / or" :expression
      "x or y and y or z"
      [:expression
       [:or-operation
        [:or-operation :x
         [:and-operation :y :y]]
        :z]])

    (testing-parser
      "with not / or" :expression
      "not x or y"
      [:expression
       [:or-operation [:not-operation :x]
        :y]])

    (testing-parser
      "with not / and" :expression
      "not x and y"
      [:expression
       [:and-operation [:not-operation :x]
        :y]])

    (testing-parser
      "with or / not" :expression
      "x or not y"
      [:expression
       [:or-operation :x
        [:not-operation :y]]])))
