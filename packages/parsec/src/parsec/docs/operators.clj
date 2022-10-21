;; Copyright 2022 Expedia, Inc.
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;     https://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

(ns parsec.docs.operators)

(def tokens
  '({:name "-"
     :altName "negation"
     :type "operator"
     :subtype "arithmetic"
     :syntax ["-:expression"]
     :returns "number"
     :description ["The negation operator toggles the sign of a number: making positive numbers negtive and negative numbers positive."
                   "Returns null if the expression is null"
                   "Since numbers can be parsed with a negative sign, this operator will only be used when preceding a non-numerical expression in the query, e.g. \"-x\" or \"-cos(y)\"."]
     :examples [{:description "Negating a column"
                 :q "input mock | x = -col1"}]}
    {:name "+"
     :altName "addition"
     :type "operator"
     :subtype "arithmetic"
     :syntax [":expression1 + :expression2"]
     :returns "number"
     :description ["The addition operator adds one number to another, or concatenates two strings. If one operand is a string, the other will be coerced into a string, and concatenated."
                   "Returns null if either expression is null."]
     :examples [{:description "Adding numbers"
                 :q "input mock | x = col1 + col2"}
                {:description "Concatenating strings"
                 :q "input mock | x = \"hello\" + \" \" + \"world\""}]}
    {:name "-"
     :altName "subtraction"
     :type "operator"
     :subtype "arithmetic"
     :syntax [":expression1 - :expression2"]
     :returns "number"
     :description ["The subtraction operator subtracts one number from another, or creates an interval between two DateTimes."
                   "Returns null if either expression is null."]
     :examples [{:description "Subtracting numbers"
                 :q "input mock | x = col1 - col2"}
                {:description "Subtracting DateTimes to create an interval"
                 :q "input mock | x = inminutes(now() - today())"}]}
    {:name "*"
     :altName "multiplication"
     :type "operator"
     :subtype "arithmetic"
     :syntax [":expression1 * :expression2"]
     :returns "number"
     :description ["The multiplication operator multiplies two numbers together."
                   "Returns null if either expression is null."
                   "Multiplying by any non-zero number by infinity gives infinity, while the result of zero times infinity is NaN."]
     :examples [{:description "Multiplying numbers"
                 :q "input mock | x = col1 * col2"}
                {:description "Multiplying by infinity"
                 :q "input mock | x = col1 * infinity()"}]}
    {:name "/"
     :altName "division"
     :type "operator"
     :subtype "arithmetic"
     :syntax [":expression1 / :expression2"]
     :returns "number"
     :description ["The division operator divides the value of one number by another. If the value is not an integer, it will be stored as a ratio or floating-point number."
                   "Returns null if either expression is null."
                   "Dividing a positive number by zero yields infinity, and dividing a negative number by zero yields -infinity. If both numeric expressions are zero, the result is NaN."]
     :examples [{:description "Dividing numbers"
                 :q "input mock | x = col1 / col2"}
                {:description "Dividing by zero gives infinity"
                 :q "input mock | x = col1 / 0, isInfinity = isinfinite(x)"}]}
    {:name "mod"
     :type "operator"
     :subtype "arithmetic"
     :syntax [":expression1 mod :expression2"]
     :returns "number"
     :description ["The modulus operator finds the amount by which a dividend exceeds the largest integer multiple of the divisor that is not greater than that number. For positive numbers, this is equivalent to the remainder after division of one number by another."
                   "Returns null if either expression is null."
                   "By definition, 0 mod N yields 0, and N mod 0 yields NaN."]
     :examples [{:q "input mock | x = col2 mod col1"}
                {:description "Modulus by zero is NaN"
                 :q "input mock | x = col1 mod 0, isNaN = isnan(x)"}]}
    {:name "^"
     :altName "exponent"
     :type "operator"
     :subtype "arithmetic"
     :syntax [":expression1 ^ :expression2"]
     :returns "number"
     :description ["The exponent operator raises a number to the power of another number."
                   "Returns null if either expression is null."
                   "By definition, N^0 yields 1."]
     :examples [{:q "input mock | x = col1 ^ col2"}]}
    {:name "and"
     :type "operator"
     :subtype "logical"
     :syntax [":expression1 and :expression2" ":expression1 && :expression2"]
     :returns "boolean"
     :description ["The logical and operator compares the value of two expressions and returns true if both values are true, else false."
                   "Returns null if either expression is null."]
     :examples [{:q "input mock | x = (true and true)"}
                {:q "input mock | x = (true && false)"}
                {:q "input mock | x = isnumber(col1) and col1 > 3"}
                {:q "input mock | x = col1 > 1 and null"}]}
    {:name "or"
     :type "operator"
     :subtype "logical"
     :syntax [":expression1 or :expression2" ":expression1 || :expression2"]
     :returns "boolean"
     :description ["The logical or operator compares the value of two expressions and returns true if at least one value is true, else false."
                   "Returns null if either expression is null."]
     :examples [{:q "input mock | x = (true or false)"}
                {:q "input mock | x = (false || false)"}
                {:q "input mock | x = col3 == 3 or col1 > 3"}
                {:q "input mock | x = col1 > 1 or null"}]}
    {:name "not"
     :type "operator"
     :subtype "logical"
     :syntax ["not :expression" "!:expression"]
     :returns "boolean"
     :description ["The logical negation operator returns true if the boolean value of :expression is false, else it returns false."
                   "Returns null if the expression is null."]
     :examples [{:q "input mock | x = not true"}
                {:q "input mock | x = !false"}
                {:q "input mock | x = !null"}]}
    {:name "xor"
     :type "operator"
     :subtype "logical"
     :syntax [":expression1 xor :expression2" ":expression1 ^^ :expression2"]
     :returns "boolean"
     :description ["The logical xor operator compares the value of two expressions and returns true if exactly one value is true, else false."
                   "Returns null if either expression is null."]
     :examples [{:q "input mock | x = (true xor false)"}
                {:q "input mock | x = (false ^^ false)"}
                {:q "input mock | x = true xor null"}]}
    {:name "=="
     :altName "equals"
     :type "operator"
     :subtype "equality"
     :syntax [":expression1 == :expression2"]
     :returns "boolean"
     :description ["The equality operator compares the value of two expressions and returns true if they are equal, else false."]
     :examples [{:q "input mock | x = (col1 == col2)"}
                {:q "input mock | filter col1 == 1 "}]}
    {:name "!="
     :altName "unequals"
     :type "operator"
     :subtype "equality"
     :syntax [":expression1 != :expression2"]
     :returns "boolean"
     :description ["The inequality operator compares the value of two expressions and returns true if they are unequal, else false."]
     :examples [{:q "input mock | x = (col1 != col2)"}
                {:q "input mock | filter col1 != 1 "}]}
    {:name ">"
     :altName "greater than"
     :type "operator"
     :subtype "equality"
     :syntax [":expression1 > :expression2"]
     :returns "boolean"
     :description ["The greater-than operator returns true if :expression1 is strictly greater than :expression2, else false."]
     :examples [{:q "input mock | x = (col1 > col2)"}
                {:q "input mock | filter col1 > 1 "}]}
    {:name "<"
     :altName "less than"
     :type "operator"
     :subtype "equality"
     :syntax [":expression1 < :expression2"]
     :returns "boolean"
     :description ["The less-than operator returns true if :expression1 is strictly less than :expression2, else false."]
     :examples [{:q "input mock | x = (col1 < col2)"}
                {:q "input mock | filter col1 < 1 "}]}
    {:name ">="
     :altName "greater or equal"
     :type "operator"
     :subtype "equality"
     :syntax [":expression1 >= :expression2"]
     :returns "boolean"
     :description ["The greater than or equal operator returns true if :expression1 is greater than or equal to :expression2, else false."]
     :examples [{:q "input mock | x = (col1 >= col2)"}
                {:q "input mock | filter col1 >= 1 "}]}
    {:name "<="
     :altName "less or equal"
     :type "operator"
     :subtype "equality"
     :syntax [":expression1 <= :expression2"]
     :returns "boolean"
     :description ["The less than or equal operator returns true if :expression1 is less than or equal to :expression2, else false."]
     :examples [{:q "input mock | x = (col1 <= col2)"}
                {:q "input mock | filter col1 <= 1 "}]}))