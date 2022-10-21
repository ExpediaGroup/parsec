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

(ns parsec.docs.literals)

(def tokens
  '({:name "null"
     :type "literal"
     :syntax ["null"]
     :description ["The keyword \"null\" represents the absence of value.
                     
                     Generally, operators and functions applied to null values tend to return null."]
     :examples [{:q "set @x = null"}]}
    {:name "true"
     :type "literal"
     :syntax ["true"]
     :description ["The keyword \"true\" represents the boolean truth value. It can be used in equality/inequality expressions, or assigned to columns or variables.
                              
                    Non-zero numbers are considered true from a boolean perspective."]
     :examples [{:q "set @truth = true"}
                {:description "Non-zero numbers are considered true from a boolean perspective"
                 :q "set @truth = (1 == true)"}]
     :related ["literal:false"]}
    {:name "false"
     :type "literal"
     :syntax ["false"]
     :description "
                The keyword \"false\" represents the boolean falsehood value It can be used in equality/inequality expressions, or assigned to columns or variables.
                     
                Zero numbers are considered false from a boolean perspective."
     :examples [{:q "set @truth = false"}
                {:description "Zero numbers are considered false from a boolean perspective"
                 :q "set @truth = (0 == false)"}]
     :related ["literal:true"]}
    {:name "\"number\""
     :type "literal"
     :syntax ["Regex: /[-+]?(0(.\\d*)?|([1-9]\\d*.?\\d*)|(.\\d+))([Ee][+-]?\\d+)?/"
              "Regex: /[-+]?(0(.\\d*)?|([1-9]\\d*.?\\d*)|(.\\d+))([Ee][+-]?\\d+)?[%]/"]
     :returns "number"
     :description ["Internally, Parsec supports the full range of JVM primitive numbers, as well as BigInteger, BigDecimal, and Ratios.
                    
                    A number that ends with a percent sign indicates a percentage.  The numerical value of the number will be divided by 100."]
     :examples [{:description "Integer"
                 :q "set @x = 100"}
                {:description "Floating-point (double) number"
                 :q "set @y = 3.141592"}
                {:description "Scientific-notation"
                 :q "set @a = 1.234e50, @b = 1.234e-10"}
                {:description "Percents"
                 :q "set @a = 50%, @b = 12 * @a"}]}
    {:name "\"identifier\""
     :type "literal"
     :syntax ["Regex: /[a-zA-Z_][a-zA-Z0-9_]*/"
              "Regex: /`[^`]+`/"]
     :description ["Identifiers are alpha-numeric strings used to reference columns in the current dataset.
                     
                    There are two possible forms for identifiers: the primary form must start with a letter or underscore, and may only contain letters, numbers, or underscores. The alternate form uses two backtick (`) characters to enclose any string of characters, including spaces or special characters (except backticks).
                     
                    Identifiers are case-sensitive."]
     :examples [{:description "Primary form"
                 :q "input mock | col6 = col5"}
                {:description "Backtick form allows spaces and special characters"
                 :q "input mock | stats `avg $` = mean(col1 * col2)"}]}
    {:name "\"variable\""
     :type "literal"
     :syntax ["Regex: /@[a-zA-Z0-9_]+[']?/"]
     :description ["Variables are alpha-numeric strings prefixed with an at symbol (@). A trailing single-quote character is allowed, representing the prime symbol.
                    
                     Unlike identifiers, variables are not associated with rows in the current dataset. Variables are stored in the context and available throughout the evaluation of a query (once they have been set)."]
     :examples [{:description "Setting a variable"
                 :q "set @fruit = \" banana \""}
                {:description "Storing a calculated value in a variable"
                 :q "input mock | set @x=mean(col1) | y = col1 - @x"}
                {:description "Prime variables"
                 :q "input mock | set @x = mean(col1), @x' = @x^2 | stats x=@x, y=@x\"\""}]
     :related ["statement:set"]}
    {:name "\"string\""
     :type "literal"
     :syntax ["TBD"]
     :returns "string"
     :description ["Strings can be created by wrapping text in single or double-quote characters. The quote character can be escaped inside a string using \\\" or \\'.
                    
                     Strings can also be created using a triple single-quote, which has the benefit of allowing single and double quote characters to be used unescaped.  Naturally, a triple single quote can be escaped inside a string using \\'''.
                    
                     Standard escape characters can be used inside a string, e.g.: \\n, \\r, \\t, \\\\, etc."]
     :examples [{:description "Single-quoted string"
                 :q "set @msg = 'hello world'"}
                {:description "Double-quoted string"
                 :q "set @msg = \"hello world\""}
                {:description "Double-quoted string with escaped characters inside"
                 :q "set @msg = \" \\\"I can't imagine\\\", said the Princess. "}
                {:description "Triple-quoted string"
                 :q "set @query = '''Parsec query:\t 'set @msg = \" hello world \"'''"}]}
    {:name "\"list\""
     :type "literal"
     :syntax ["[expr1, expr2, ... ]"]
     :description ["Lists can be created using the tolist() function or with a list literal: [expr1, expr2, ...]."]
     :examples [{:description "Creating a list with hard-coded values"
                 :q "set @list = [1, 2, 3]"}
                {:description "Creating a list using expressions for each value"
                 :q "input mock | x = [col1, col2, col3 + col4]"}]
     :related ["function:tolist"]}
    {:name "\"map\""
     :type "literal"
     :syntax ["{ key1: expr1, key2: expr2, ... }"]
     :description ["Maps can be created using the tomap() function or with a map literal: {key1: expr1, key2: expr2, ...}."]
     :examples [{:description "Creating a map with hard-coded values"
                 :q "set @map = { a: 1, b: 2 }"}
                {:description "Creating a map with strings for keys"
                 :q "set @map = { " a ": 1, " b ": 2 }"}
                {:description "Creating a map using expressions for each value"
                 :q "input mock | x = { col1: col1, col2: col2 }"}
                {:description "Aggregating into a map"
                 :q "input mock | stats x = { min: min(col1), max: max(col1) }"}]
     :related ["function:tomap"]}))