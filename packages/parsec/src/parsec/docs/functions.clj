;; Copyright 2022 Expedia, Inc.
;;
;; Licensed under the Apache License, Version 2.0 (the \\"License\\");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;     https://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an \\"AS IS\\" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

(ns parsec.docs.functions)

(def tokens
  '({:name "avg"
     :type "function"
     :subtype "aggregation"
     :description ["avg() is an alias for mean()."]
     :aliases ["function:mean"]}
    {:name "count"
     :type "function"
     :subtype "aggregation"
     :syntax ["count(*)"
              "count(:expression)"
              "count(:expression, :predicate)"]
     :returns "number"
     :description ["Counts the number of rows where :expression is non-null. An optional predicate argument can be used to filter rows before the aggregation."
                   "count(*) is equivalent to count(1); it counts every row."]
     :examples [{:q "input mock | stats count = count(*)"}
                {:q "input mock | col1 = null where col2 == 2 | stats count = count(col1)"}
                {:q "input mock | stats count = count(*, col2 == 2)"}]
     :related ["function:distinctcount"]}
    {:name "cumulativeavg"
     :type "function"
     :subtype "aggregation"
     :description ["cumulativeavg() is an alias for cumulativemean()."]
     :aliases ["function:cumulativemean"]}
    {:name "cumulativemean"
     :type "function"
     :subtype "aggregation"

     :syntax ["cumulativemean(:expression)"
              "cumulativemean(:expression, :predicate)"]
     :returns "list"
     :description ["Returns a list of cumulative means for the current dataset.  For example, the first value in the list is the value of the first row; the second value is the mean of the first two rows, the third is the mean of the first three rows, etc."
                   "An optional predicate argument can be used to filter rows before the aggregation."]
     :examples [{:q "input mock | stats c1 = cumulativemean(col1), c2 = cumulativemean(col2)"}]
     :related ["function:mean", "function:geometricmean"]
     :aliases ["function:cumulativeavg"]}
    {:name "distinctcount"
     :type "function"
     :subtype "aggregation"
     :syntax ["distinctcount(:expression)"
              "distinctcount(:expression, :predicate)"]
     :returns "number"
     :description ["Counts the number of rows for which :expression is distinct and non-null. An optional predicate argument can be used to filter rows before the aggregation."]
     :examples [{:q "input mock | stats col1DistinctCount = distinctcount(col1), col2DistinctCount = distinctcount(col2)"}
                {:q "input mock | stats count = count(*, col2 == 2)"}]
     :related ["function:count"]}
    {:name "geometricmean"
     :type "function"
     :subtype "aggregation"
     :syntax ["geometricmean(:expression)"
              "geometricmean(:expression, :predicate)"]
     :returns "number"
     :description ["Returns the geometric mean of :expression, calculated across all rows. An optional predicate argument can be used to filter rows before the aggregation."]
     :examples [{:q "input mock | stats geometricmean = geometricmean(col1)"}
                {:q "input mock | stats geometricmean = geometricmean(col1 + col2, col3 == 3)"}]
     :related ["function:mean", "function:cumulativemean"]}
    {:name "max"
     :type "function"
     :subtype "aggregation"
     :syntax ["max(:expression)"
              "max(:expression, :predicate)"]
     :returns "number"
     :description ["Returns the maximum value of :expression, calculated across all rows. An optional predicate argument can be used to filter rows before the aggregation."]
     :examples [{:q "input mock | stats max = max(col1)"}
                {:q "input mock name=\"chick-weight\" | stats maxWeight = max(weight, Chick == 1)"}]
     :related ["function:min"]}
    {:name "mean"
     :type "function"
     :subtype "aggregation"

     :syntax ["mean(:expression)"
              "mean(:expression, :predicate)"]
     :returns "number"
     :description ["Returns the arithmetic mean of :expression, calculated across all rows. An optional predicate argument can be used to filter rows before the aggregation."]
     :examples [{:q "input mock | stats mean = mean(col1)"}
                {:q "input mock | stats mean = mean(col1 + col2, col3 == 3)"}]
     :related ["function:geometricmean", "function:cumulativemean"]
     :aliases ["function:avg"]}
    {:name "median"
     :type "function"
     :subtype "aggregation"
     :syntax ["median(:expression)"
              "median(:expression, :predicate)"]
     :returns "number"
     :description ["Returns the median value of :expression, calculated across all rows. If there is no middle value, the median is the mean of the two middle values."
                   "An optional predicate argument can be used to filter rows before the aggregation."]
     :examples [{:q "input mock | stats median = median(col1)"}
                {:q "input mock name=\"chick-weight\" | stats medianWeight = median(weight, Chick == 1)"}
                {:q "input mock name=\"airline-passengers\" | stats mean = mean(passengers), median = median(passengers)"}]}
    {:name "mode"
     :type "function"
     :subtype "aggregation"
     :syntax ["mode(:expression)"
              "mode(:expression, :predicate)"]
     :returns "number"
     :description ["Returns the mode of :expression, calculated across all rows. If there are multiple modes, one of them will be returned."
                   "An optional predicate argument can be used to filter rows before the aggregation."]
     :examples [{:q "input mock | stats mode = mode(col2)"}
                {:q "input mock name=\"hair-eye-color\" | stats mode = mode(hair) by gender"}
                {:q "input mock name=\"survey\" | stats medianAge = median(age), modeAge = mode(age)"}]}
    {:name "min"
     :type "function"
     :subtype "aggregation"
     :syntax ["min(:expression)"
              "min(:expression, :predicate)"]
     :returns "number"
     :description ["Returns the minimum value of :expression, calculated across all rows. An optional predicate argument can be used to filter rows before the aggregation."]
     :examples [{:q "input mock | stats min = min(col1)"}
                {:q "input mock name=\"chick-weight\" | stats minWeight = min(weight, Chick == 1)"}]
     :related ["function:max"]}
    {:name "pluck"
     :type "function"
     :subtype "aggregation"
     :syntax ["pluck(expression)"]
     :returns "list"
     :description ["Evaluates an expression against each row in the data set, and returns a list composed of the resulting values. The list maintains the same order as the rows in the data set."]
     :examples [{:description "Plucking a column"
                 :q "input mock | stats values = pluck(col1)"}
                {:description "Plucking a calculated expression"
                 :q "input mock | stats values = pluck(col1 + col2)"}]}
    {:name "stddev_pop"
     :type "function"
     :subtype "aggregation"
     :description ["stddev_pop() is an alias for stddevp()."]
     :aliases ["function:stddevp"]}
    {:name "stddev_samp"
     :type "function"
     :subtype "aggregation"
     :description ["stddev_samp() is an alias for stddev()."]
     :aliases ["function:stddev"]}
    {:name "stddevp"
     :type "function"
     :subtype "aggregation"
     :syntax ["stddevp(:expression)"
              "stddevp(:expression, :predicate)"]
     :returns "number"
     :description ["Returns the population standard deviation of :expression, calculated across all rows in the data set (excluding nulls). An optional predicate argument can be used to filter rows before the aggregation."
                   "If the data set is not the complete population, use stddev() instead to calculate the sample standard deviation."]
     :examples [{:description "Standard deviation of a column"
                 :q "input mock | stats sigma = stddevp(col1)"}
                {:description "Standard deviation of a calculated value"
                 :q "input mock | stats sigma = stddevp(col1 + col2)"}]
     :related ["function:stddev"]
     :aliases ["function:stddev_pop"]}
    {:name "stddev"
     :type "function"
     :subtype "aggregation"
     :syntax ["stddev(:expression)"
              "stddev(:expression, :predicate)"]
     :returns "number"
     :description ["Returns the sample standard deviation of :expression, calculated across all rows in the data set (excluding nulls). An optional predicate argument can be used to filter rows before the aggregation."
                   "If the data set is the complete population, use stddevp() instead to calculate the population standard deviation."]
     :examples [{:description "Standard deviation of a column"
                 :q "input mock | stats sigma = stddev(col1)"}
                {:description "Standard deviation of a calculated value"
                 :q "input mock | stats sigma = stddev(col1 + col2)"}]
     :related ["function:stddevp"]
     :aliases ["function:stddev_samp"]}
    {:name "sum"
     :type "function"
     :subtype "aggregation"
     :syntax ["sum(:expression)"
              "sum(:expression, :predicate)"]
     :returns "number"
     :description ["Returns the sum of :expression, calculated across all rows. An optional predicate argument can be used to filter rows before the aggregation."]
     :examples [{:q "input mock | stats total = sum(col1)"}
                {:q "input mock name=\"airline-passengers\" | stats pTotal = sum(passengers), p1960 = sum(passengers, year == 1960)"}]}
    {:name "every"
     :type "function"
     :subtype "aggregation"
     :syntax ["every(:expression)"
              "every(:expression, :predicate)"]
     :returns "boolean"
     :description ["Returns true if :expression is true for all rows in the dataset. An optional predicate argument can be used to filter rows before the aggregation."]
     :examples [{:q "input mock | stats test = every(col1 > 0)"}
                {:q "input mock name=\"chick-weight\" | stats hasWeight = every(weight > 0, Chick == 1)"}]
     :related ["function:some"]}
    {:name "first"
     :type "function"
     :subtype "aggregation"
     :syntax ["first(:expression)"
              "first(:expression, :predicate)"]
     :returns "number"
     :description ["Returns the first non-null value of :expression in the dataset, closest to the top of the dataset. An optional predicate argument can be used to filter rows before the aggregation."]
     :examples [{:q "input mock | stats first = first(col1)"}
                {:q "input mock name=\"chick-weight\" | stats firstWeight = min(weight, Chick == 1)"}]
     :related ["function:last"]}
    {:name "last"
     :type "function"
     :subtype "aggregation"
     :syntax ["last(:expression)"
              "last(:expression, :predicate)"]
     :returns "number"
     :description ["Returns the last non-null value of :expression in the dataset, closest to the bottom of the dataset. An optional predicate argument can be used to filter rows before the aggregation."]
     :examples [{:q "input mock | stats last = last(col1)"}
                {:q "input mock name=\"chick-weight\" | stats lastWeight = last(weight, Chick == 1)"}]
     :related ["function:first"]}
    {:name "percentile"
     :type "function"
     :subtype "aggregation"
     :syntax ["percentile(:expression, :percentage)"
              "percentile(:expression, :percentage, :predicate)"]
     :returns "number"
     :description ["Calculates the percentile of an :expression and a given :percentage. Specifically, it returns the value below which :percentage of values in the dataset are less than the value. The percentage can be a number or calculated value."
                   "Uses the linear interpolation method, so the returned value may not be in the original dataset. Excludes nils from the calculation and returns nil if there are no non-nil values."]
     :examples [{:q "input mock name=\"chick-weight\" | stats p95 = percentile(weight, 95) by Chick"}]}
    {:name "some"
     :type "function"
     :subtype "aggregation"
     :syntax ["some(:expression)"
              "some(:expression, :predicate)"]
     :returns "boolean"
     :description ["Returns true if :expression is true for some rows in the dataset. An optional predicate argument can be used to filter rows before the aggregation."]
     :examples [{:q "input mock | stats test = some(col1 > 0)"}
                {:q "input mock name=\"chick-weight\" | stats hasWeight = some(weight > 0, Chick == 1)"}]
     :related ["function:some"]}
    {:name "isexist"
     :type "function"
     :subtype "is-functions"
     :syntax ["isexist(:column)"]
     :returns "boolean"
     :description ["Returns true if its argument exists as a column the current row, else false. It does not check the value of the column, just its existance. As a result, isexist() on a column with a null value will return true."]
     :examples [{:q "input mock | col6 = 1 where col2 == 2 | exists = isexist(col6)"}]}
    {:name "isnull"
     :type "function"
     :subtype "is-functions"
     :syntax ["isnull(:expression)"]
     :returns "boolean"
     :description ["Returns true if its argument is null, else false."]
     :examples [{:q "set @test = isnull(null)"}]}
    {:name "isnan"
     :type "function"
     :subtype "is-functions"
     :syntax ["isnan(:expression)"]
     :returns "boolean"
     :description ["Returns true if its argument is NaN, else false."]
     :examples [{:q "set @test = isnan(0 * infinity())"}]
     :related ["function:nan"]}
    {:name "isinfinite"
     :type "function"
     :subtype "is-functions"
     :syntax ["isinfinite(:expression)"]
     :returns "boolean"
     :description ["Returns true if its argument is infinite, else false."]
     :related ["function:isfinite", "function:isinfinite", "function:neginfinity"]}
    {:name "isfinite"
     :type "function"
     :subtype "is-functions"
     :syntax ["isfinite(:expression)"]
     :returns "boolean"
     :description ["Returns true if its argument is finite, else false."]
     :related ["function:isfinite", "function:isinfinite", "function:neginfinity"]}
    {:name "isempty"
     :type "function"
     :subtype "is-functions"
     :syntax ["isempty(:expression)"]
     :returns "boolean"
     :description ["Returns true if its argument is \"empty\", else false."
                   "Lists are empty if they have no elements; maps are empty if they have no keys; strings are empty if they have zero length or contain only whitespace. Zero and null are both considered empty."]
     :examples [{:q "set @nonempty = isempty(\"hello\"), @empty = isempty(\"  \n\")"}
                {:q "set @nonempty = isempty({a: 1}), @empty = isempty(tomap())"}
                {:q "set @nonempty = isempty([1, 2, 3]), @empty = isempty([])"}]}
    {:name "isdate"
     :type "function"
     :subtype "type"
     :syntax ["isdate(:expression)"]
     :returns "boolean"
     :description ["Returns true if its argument is a DateTime, else false."
                   "isdate(null) returns false."]
     :examples [{:q "set @test = isdate(now())"}
                {:description "Numbers are not DateTimes"
                 :q "set @test = isdate(101)"}]}
    {:name "isboolean"
     :type "function"
     :subtype "type"
     :syntax ["isboolean(:expression)"]
     :returns "boolean"
     :description ["Returns true if its argument is a boolean."
                   "isboolean(null) returns false."]
     :examples [{:q "set @test = isboolean(true)"}
                {:description "Numbers are not booleans"
                 :q "set @test = isboolean(false)"}
                {:description "Equality operators always return booleans"
                 :q "set @test = isboolean(100 > 0)"}]}
    {:name "isnumber"
     :type "function"
     :subtype "type"
     :syntax ["isnumber(:expression)"]
     :returns "boolean"
     :description ["Returns true if its argument is a number. This includes integers, doubles, ratios, etc."
                   "isnumber(null) returns false."]
     :examples [{:q "set @test = isnumber(0)"}
                {:description "Booleans are not numbers"
                 :q "set @test = isnumber(false)"}
                {:description "Numerical strings are not numbers"
                 :q "set @test = isnumber(\"3.14\")"}]}
    {:name "isinteger"
     :type "function"
     :subtype "type"
     :syntax ["isinteger(:expression)"]
     :returns "boolean"
     :description ["Returns true if its argument is an integer."
                   "isinteger(null) returns false."]
     :examples [{:q "set @test = isinteger(100)"}
                {:description "Floating-point numbers are not integers"
                 :q "set @test = isinteger(1.5)"}
                {:description "Since these numbers can be represented accurately as ratios, their addition yields an integer."
                 :q "set @test = isinteger(1.5 + 0.5)"}]}
    {:name "isratio"
     :type "function"
     :subtype "type"
     :syntax ["isratio(:expression)"]
     :returns "boolean"
     :description ["Returns true if its argument is a ratio. Parsec will automatically store the result of computations in ratio form if possible, e.g. if two integers are divided."
                   "isratio(null) returns false."]
     :examples [{:q "set @test = isratio(1/2)"}
                {:description "Booleans are not ratios"
                 :q "set @test = isratio(false)"}
                {:description "Numerical strings are not ratios"
                 :q "set @test = isratio(\"1/3\")"}]}
    {:name "isdouble"
     :type "function"
     :subtype "type"
     :syntax ["isdouble(:expression)"]
     :returns "boolean"
     :description ["Returns true if its argument is a double."
                   "isdouble(null) returns false."]
     :examples [{:q "set @test = isdouble(pi())"}
                {:description "Forcing a conversion to double"
                 :q "input mock | x = todouble(col1), @y = isdouble(x)"}
                {:description "Parsec stores exact ratios when possible, which are not considered doubles"
                 :q "set @test = isdouble(1 / 2)"}]}
    {:name "isstring"
     :type "function"
     :subtype "type"
     :syntax ["isstring(:expression)"]
     :returns "boolean"
     :description ["Returns true if its argument is a string."
                   "isstring(null) returns false."]
     :examples [{:q "set @test = isstring(\"hello world\")"}]}
    {:name "islist"
     :type "function"
     :subtype "type"
     :syntax ["islist(:expression)"]
     :returns "boolean"
     :description ["Returns true if its argument is a list."
                   "islist(null) returns false."]
     :examples [{:q "set @test = islist([1, 2, 3])"}]}
    {:name "ismap"
     :type "function"
     :subtype "type"
     :syntax ["ismap(:expression)"]
     :returns "boolean"
     :description ["Returns true if its argument is a map."
                   "ismap(null) returns false."]
     :examples [{:q "set @test = ismap({ a: 1, y: 2, z: 3})"}]}
    {:name "type"
     :type "function"
     :subtype "type"
     :syntax ["istype(:expression)"]
     :returns "string"
     :description ["Evaluates :expression and returns its data type name as a string."]
     :examples [{:q "input mock | x = type(col1)"}
                {:q "input mock | x = tostring(col1), y = type(x)"}
                {:q "input mock | stats x = pluck(col1) | y = type(x)"}
                {:q "input mock | x=type(null)"}]}
    {:name "toboolean"
     :type "function"
     :subtype "conversion"
     :syntax ["toboolean(:expression)"]
     :returns "boolean"
     :description ["Attempts to convert :expression to a boolean value, returning null if the conversion is not possible."
                   "If :expression is a boolean, it is returned as-is."
                   "If :expression is a number, yields true for any non-zero number, else false."
                   "If :expression is a string, a case-insensitive \"true\" or \"false\" will be converted to true and false respectively, with all other values returning null."
                   "All other data types (including null) return null."]
     :examples [{:description "Converting a number"
                 :q "set @true = toboolean(1),\n    @false = toboolean(0)"}
                {:description "Converting a string"
                 :q "set @test = toboolean(\"TRUE\")"}]
     :related ["function:isboolean"]}
    {:name "tostring"
     :type "function"
     :subtype "conversion"
     :syntax ["tostring(:expression)"
              "tostring(:expression, :format)"]
     :returns "string"
     :description ["Attempts to convert :expression to a string type, returning null if the conversion is not possible."
                   "If :expression is a string, it is returned as-is."
                   "If :expression is a boolean, \"true\" or \"false\" is returned."
                   "If :expression is a number, yields a string containing the numeric representation of the number."
                   "If :expression is a map or list, yields a string containing the JSON representation of :expression."
                   "If :expression is a date, yields an ISO 8601-format string representing the date, unless a format code is provided as the second argument. Joda-Time formatting code are accepted, see here: http://www.joda.org/joda-time/key_format.html"
                   "All other data types (including null) return null."]
     :examples [{:description "Converting a number"
                 :q "set @test = tostring(1) // returns \"1\""}
                {:description "Converting a boolean"
                 :q "set @test = tostring(true) // returns \"true\""}
                {:description "Converting a map"
                 :q "set @test = tostring({ \"id\": 123, \"name\": \"Vin Diesel\" }) // returns {\"id\":123,\"name\":\"Vin Diesel\"}"}
                {:description "Converting a list"
                 :q "set @test = tostring([1, 2, 3]) // returns \"[1,2,3]\""}
                {:description "Converting a date into ISO 8601 format"
                 :q "set @year = tostring(now())"}
                {:description "Converting a date with a format"
                 :q "set @year = tostring(now(), \"YYYY\")"}]
     :related ["function:isboolean"]}
    {:name "tonumber"
     :type "function"
     :subtype "conversion"
     :syntax ["tonumber(:expression)"]
     :returns "number"
     :description ["Attempts to convert :expression to a numerical type, returning null if the conversion is not possible.  This function can return integers, doubles, ratios, etc."
                   "If :expression is a number, it is returned as-is."
                   "If :expression is a date, yields the number of milliseconds since the epoch (1970-01-01)."
                   "If :expression is a string, attempts to parse it as an integer, double, or ratio.  If the parsed value can be represented as a ratio, it will be stored internally as such."
                   "All other data types (including null) return null."]
     :examples [{:description "Converting an integer string"
                 :q "set @test = tonumber(\"1\") // returns 1"}
                {:description "Converting a double string"
                 :q "set @test = tonumber(\"3.14\") // returns 3.14"}
                {:description "Converting a ratio"
                 :q "set @test = tonumber(\"1/5\") // returns 0.2"}
                {:description "Converting a date"
                 :q "set @test = tonumber(todate(\"2016-04-25T00:02:05.000Z\")) // returns 1461542525000"}]
     :related ["function:isnumber", "function:tointeger", "function:todouble"]}
    {:name "tointeger"
     :type "function"
     :subtype "conversion"
     :syntax ["tointeger(:expression)"]
     :returns "number"
     :description ["Attempts to convert :expression to a long integer, returning null if the conversion is not possible."
                   "If :expression is an integer, it is returned as-is."
                   "If :expression is a non-integer number, it will be rounded down to the nearest integer."
                   "If :expression is a date, yields the number of milliseconds since the epoch (1970-01-01)."
                   "If :expression is a string, attempts to parse it as an integer."
                   "All other data types (including null) return null."]
     :examples [{:description "Converting a double"
                 :q "set @test = tointeger(3.14) // returns 3"}
                {:description "Converting an integer string"
                 :q "set @test = tointeger(\"1\") // returns 1"}
                {:description "Converting a ratio"
                 :q "set @test = tointeger(\"22/3\") // returns 7"}
                {:description "Converting a date"
                 :q "set @test = tointeger(todate(\"2016-04-25T00:02:05.000Z\")) // returns 1461542525000"}]
     :related ["function:isinteger", "function:tonumber", "function:todouble"]}
    {:name "todouble"
     :type "function"
     :subtype "conversion"
     :syntax ["todouble(:expression)"]
     :returns "number"
     :description ["Attempts to convert :expression to a double, returning null if the conversion is not possible."
                   "If :expression is an double, it is returned as-is."
                   "If :expression is an number, it will be coerced into a double type internally."
                   "If :expression is a date, yields the number of milliseconds since the epoch (1970-01-01)."
                   "If :expression is a string, attempts to parse it as an double."
                   "All other data types (including null) return null."]
     :examples [{:description "Converting an integer"
                 :q "set @test = todouble(1) // returns 1"}
                {:description "Converting a double string"
                 :q "set @test = todouble(\"3.14\") // returns 3.14"}
                {:description "Converting a ratio string"
                 :q "set @test = todouble(\"22/3\") // returns 7.333333333333333"}
                {:description "Converting a date"
                 :q "set @test = todouble(todate(\"2016-04-25T00:02:05.000Z\")) // returns 1461542525000"}]
     :related ["function:isdouble", "function:tonumber", "function:tointeger"]}
    {:name "todate"
     :type "function"
     :subtype "conversion"
     :syntax ["todate(:expression)"
              "todate(:expression, :format)"]
     :returns "number"
     :description ["Attempts to convert :expression to a date, returning null if the conversion is not possible."
                   "If :expression is an date, it is returned as-is."
                   "If :expression is a number, it will be coerced to a long and interpreted as the number of milliseconds since the epoch (1970-01-01)."
                   "If :expression is a string, attempts to parse using various standard formats.  A format argument can optionally be provided to specify the correct format."
                   "All other data types (including null) return null."]
     :examples [{:description "Converting epoch milliseconds"
                 :q "set @test = todate(1461543510000)"}
                {:description "Converting an ISO-8601 date"
                 :q "set @test = todate(\"2016-04-25T00:02:05.000Z\")"}
                {:description "Converting a date with a format code"
                 :q "set @test = todate(\"1985-10-31\", \"yyyy-MM-dd\")"}]
     :related ["function:isdate"]}
    {:name "tolist"
     :type "function"
     :subtype "conversion"
     :syntax ["tolist(:expression [, :expression ...])"]
     :returns "map"
     :description ["Creates a list from a series of arguments."]
     :examples [{:q "input mock | x = tolist(1, 2, 3, 4)"}
                {:q "input mock | x = tolist(\"red\", \"yellow\", \"green\")"}
                {:description "Creating a list from expressions"
                 :q "input mock | x = tolist(meanmean(col1), meanmean(col2), meanmean(col3))"}]
     :related ["literal:\"list\"", "function:islist"]}
    {:name "tomap"
     :type "function"
     :subtype "conversion"
     :syntax ["tomap(:key, :expression [, :key, :expression ...])"]
     :returns "map"
     :description ["Creates a map from a series of key/value pair arguments: tomap(key1, expr1, key2, expr2, ...)."]
     :examples [{:q "input mock | x = tomap(\"x\", 1, \"y\", 2)"}
                {:description "Creating a map from expressions"
                 :q "input mock | x = tomap(\"meanmean\", mean(col1), \"max\", max(col1))"}]
     :related ["literal:\"map\"", "function:ismap"]}
    {:name "e"
     :type "function"
     :subtype "constant"
     :syntax ["e()"]
     :returns "number"
     :description ["Returns the constant Euler\"s number."]
     :examples [{:q "input mock | pi = e()"}]
     :related ["function:ln"]}
    {:name "pi"
     :type "function"
     :subtype "constant"
     :syntax ["pi()"]
     :returns "number"
     :description ["Returns the constant Pi."]
     :examples [{:q "input mock | pi = pi()"}]}
    {:name "nan"
     :type "function"
     :subtype "constant"
     :syntax ["nan()"]
     :returns "number"
     :description ["Returns the not-a-number (NaN) numerical value."]
     :examples [{:q "input mock | x = nan(), isnotanumber = isnan(x)"}]
     :related ["function:isnan"]}
    {:name "infinity"
     :type "function"
     :subtype "constant"
     :syntax ["infinity()"]
     :returns "number"
     :description ["Returns the positive infinite numerical value."]
     :examples [{:q "input mock | x = infinity(), isInf = isinfinite(x)"}]
     :related ["function:isfinite", "function:isinfinite", "function:neginfinity"]}
    {:name "neginfinity"
     :type "function"
     :subtype "constant"
     :syntax ["neginfinity()"]
     :description ["Returns the negative infinite numerical value."]
     :examples [{:q "input mock | x = neginfinity(), isInf = isinfinite(x)"}]
     :related ["function:isfinite", "function:isinfinite", "function:infinity"]}
    {:name "floor"
     :type "function"
     :subtype "math"
     :syntax ["floor(:number)"]
     :description ["Returns the greatest integer less than or equal to :number."]
     :examples [{:description "Floor of an double"
                 :q "input mock name=\"thurstone\" | z = y / x, floor = floor(z)"}
                {:description "Floor of an integer"
                 :q "set @floor = floor(42)"}]
     :related ["function:ceil", "function:round"]}
    {:name "ceil"
     :type "function"
     :subtype "math"
     :syntax ["ceil(:number)"]
     :description ["Returns the least integer greater than or equal to :number."]
     :examples [{:description "Ceiling of an double"
                 :q "input mock name=\"thurstone\" | z = y / x, ceil = ceil(z)"}
                {:description "Ceiling of an integer"
                 :q "set @ceil = ceil(42)"}]
     :related ["function:floor", "function:round"]}
    {:name "abs"
     :type "function"
     :subtype "math"
     :syntax ["abs(:number)"]
     :description ["Returns the absolute value of :number."]
     :examples [{:description "Absolute value of a positive number"
                 :q "set @abs = abs(10)"}
                {:description "Absolute value of a negative number"
                 :q "set @abs = abs(-99)"}]}
    {:name "sign"
     :type "function"
     :subtype "math"
     :syntax ["sign(:number)"]
     :description ["Returns 1 if :number is greater than zero, -1 if :number is less than zero, and 0 if :number is zero."
                   "Returns NaN if :number is NaN."]
     :examples [{:description "Sign of a positive number"
                 :q "set @sign = sign(10)"}
                {:description "Sign of a negative number"
                 :q "set @sign = sign(-99)"}
                {:description "Sign of zero"
                 :q "set @sign = sign(0)"}]}
    {:name "sqrt"
     :type "function"
     :subtype "math"
     :syntax ["sqrt(:number)"]
     :description ["Returns the square root of :number."]
     :examples [{:q "input mock name=\"thurstone\" | sqrt = sqrt(x + y)"}
                {:description "Square root of a perfect square"
                 :q "set @square = sqrt(64)"}]
     :related ["function:pow"]}
    {:name "pow"
     :type "function"
     :subtype "math"
     :syntax ["pow(:base, :power)"]
     :description ["Returns :base raised to the power of :power."]
     :examples [{:q "input mock name=\"thurstone\" | cube = pow(x + y, 3)"}
                {:description "Square root of a square"
                 :q "set @square = sqrt(pow(8, 2))"}]
     :related ["function:sqrt"]}
    {:name "round"
     :type "function"
     :subtype "math"
     :syntax ["round(:number)", "round(:number, :places)"]
     :description ["Rounds a number to a given number of decimal places."
                   "If the 2nd argument is omitted, the number is rounded to an integer (0 decimal places)."]
     :examples [{:description "Rounding to an integer"
                 :q "input mock name=\"thurstone\" | stats p90 = percentile(x, 90), r = round(p90)"}
                {:description "Rounding to five decimal places"
                 :q "input mock name=\"thurstone\" | stats z = mean(x / y), r = round(z, 5)"}]
     :related ["function:ceil", "function:floor"]}
    {:name "within"
     :type "function"
     :subtype "math"
     :syntax ["within(:number, :test, :tolerance)"]
     :description ["Returns true if the difference between :number and :test is less than or equal to :tolerance.  That is, it evaluates whether the value of :number is in the range of :test +/- :tolerance (inclusive)."]
     :examples [{:q "input mock | x = within(col1, col3, 1)"}
                {:description "Comparing maximum Chick weights to the average"
                 :q "input mock name=\"chick-weight\" | stats mean=mean(weight), max=max(weight), stdev = stddev(weight) by Chick | withinOneSigma = within(mean, max, stdev)"}]
     :related ["function:within%"]}
    {:name "within%"
     :type "function"
     :subtype "math"
     :syntax ["within%(:number, :test, :percent)"]
     :description ["Similar to within(), but uses :percent to calculate the tolerance from :test.  Returns true if the difference between :number and :test is less than or equal to (:percent * :test)."
                   "The :percent argument is expected as a decimal or percentage literal: \"0.1\" or \"10%\".  A value of \"10\" is interpreted as 1000%, not 10%."]
     :examples [{:q "input mock | select col1, col4 | x = within%(col1, col4, 50%)"}
                {:description "Comparing maximum Chick weights to the average"
                 :q "input mock name=\"chick-weight\" | stats avg=avg(weight), max=max(weight) by Chick | test = within%(max, avg, 50%)"}]
     :related ["function:within"]}
    {:name "greatest"
     :type "function"
     :subtype "math"
     :syntax ["greatest(:number, :number, ...)"]
     :description ["Returns the argument with the greatest numerical value, regardless of order."
                   "greatest(null) returns null, and non-numerical arguments will cause an exception. If a single argument is provided, it will be returned regardless of its type."]
     :examples [{:q "input mock | greatest = greatest(col1, col2, col3)"}]
     :related ["function:least"]}
    {:name "least"
     :type "function"
     :subtype "math"
     :syntax ["least(:number, :number, ...)"]
     :description ["Returns the argument with the smallest numerical value, regardless of order."
                   "least(null) returns null, and non-numerical arguments will cause an exception. If a single argument is provided, it will be returned regardless of its type."]
     :examples [{:q "input mock | least = least(col1, col2, col3)"}]
     :related ["function:greatest"]}
    {:name "gcd"
     :type "function"
     :subtype "math"
     :syntax ["gcd(:integer, :integer)"]
     :description ["Returns the greatest common divisor of two integers."
                   "Returns null if either :integer is null, or if non-integer arguments are passed."]
     :examples [{:q "set @gcd = gcd(12, 18)"}]
     :related ["function:lcm"]}
    {:name "lcm"
     :type "function"
     :subtype "math"
     :syntax ["lcm(:integer, :integer)"]
     :description ["Returns the least common multiple of two integers."
                   "Returns null if either :integer is null, or if non-integer arguments are passed."]
     :examples [{:q "set @lcm = lcm(12, 18)"}]
     :related ["function:gcd"]}
    {:name "ln"
     :type "function"
     :subtype "math"
     :syntax ["ln(:number)"]
     :description ["Returns the natural logarithm (base e) for :number"
                   "Returns null for non-numerical arguments.  If :number is NaN or less than zero, returns NaN. If :number is positive infinity, then returns positive infinity. If :number is positive zero or negative zero, then returns negative infinity."]
     :examples [{:q "set @ln = ln(5)"}]
     :related ["function:e", "function:log"]}
    {:name "log"
     :type "function"
     :subtype "math"
     :syntax ["log(:number)"]
     :description ["Returns the base 10 logarithm for :number"
                   "Returns null for non-numerical arguments.  If :number is NaN or less than zero, returns NaN. If :number is positive infinity, then returns positive infinity. If :number is positive zero or negative zero, then returns negative infinity."]
     :examples [{:q "set @log = log(5)"}]
     :related ["function:ln"]}
    {:name "degrees"
     :type "function"
     :subtype "trigonometric"
     :syntax ["degrees(:angle)"]
     :description ["Converts an :angle in radians to degrees."]
     :examples [{:q "set @degrees = degrees(pi() / 2)"}]
     :related ["function:radians"]}
    {:name "radians"
     :type "function"
     :subtype "trigonometric"
     :syntax ["radians(:angle)"]
     :description ["Converts an :angle in degrees to radians."]
     :examples [{:q "set @radians = radians(45)"}]
     :related ["function:degrees"]}
    {:name "sin"
     :type "function"
     :subtype "trigonometric"
     :syntax ["sin(:number)"]
     :description ["Returns the trigonometric sine of an angle in radians."
                   "If :number is NaN or infinity, returns NaN.  If :number is zero, then the result is zero."]
     :examples [{:q "set @sin = sin(3.1415)"}]
     :related ["function:cos", "function:tan", "function:asin", "function:sinh"]}
    {:name "cos"
     :type "function"
     :subtype "trigonometric"
     :syntax ["cos(:number)"]
     :description ["Returns the trigonometric cosine of an angle in radians."
                   "If :number is NaN or infinity, returns NaN."]
     :examples [{:q "set @cos = cos(3.1415)"}]
     :related ["function:sin", "function:tan", "function:acos", "function:cosh"]}
    {:name "tan"
     :type "function"
     :subtype "trigonometric"
     :syntax ["tan(:number)"]
     :description ["Returns the trigonometric tangent of an angle in radians."
                   "If :number is NaN or infinity, returns NaN.  If :number is zero, then the result is a zero with the same sign as :number."]
     :examples [{:q "set @tan = tan(3.1415)"}]
     :related ["function:sin", "function:cos", "function:atan", "function:atan2", "function:tanh"]}
    {:name "sinh"
     :type "function"
     :subtype "trigonometric"
     :syntax ["sinh(:number)"]
     :description ["Returns the hyperbolic sine of an angle in radians."
                   "If :number is NaN, then the result is NaN. If :number is infinite, then the result is an infinity with the same sign as :number. If :number is zero, then the result is zero."]
     :examples [{:q "set @sinh = sinh(3.1415)"}]
     :related ["function:sin", "function:cosh", "function:tanh", "function:asin"]}
    {:name "cosh"
     :type "function"
     :subtype "trigonometric"
     :syntax ["cosh(:number)"]
     :description ["Returns the hyperbolic cosine of an angle in radians."
                   "If :number is NaN, then the result is NaN. If :number is infinite, then the result then the result is positive infinity. If :number is zero, then the result is 1."]
     :examples [{:q "set @cosh = cosh(3.1415)"}]
     :related ["function:cos", "function:sinh", "function:tanh", "function:acos"]}
    {:name "tanh"
     :type "function"
     :subtype "trigonometric"
     :syntax ["tanh(:number)"]
     :description ["Returns the hyperbolic tangent of an angle in radians."
                   "If :number is NaN, then the result is NaN. If :number is zero, then the result is zero. If :number is positive infinity, then the result is 1. If :number is negative infinity, then the result is -1.0."]
     :examples [{:q "set @tanh = tanh(3.1415)"}]
     :related ["function:sinh", "function:cosh", "function:tan", "function:atan"]}
    {:name "asin"
     :type "function"
     :subtype "trigonometric"
     :syntax ["asin(:number)"]
     :description ["Returns the arc sine of an angle in radians."
                   "If :number is NaN or its absolute value is greater than 1, then the result is NaN. If :number is zero, then the result is zero."]
     :examples [{:q "set @asin = asin(0.5)"}]
     :related ["function:sin", "function:sinh", "function:acos", "function:atan"]}
    {:name "acos"
     :type "function"
     :subtype "trigonometric"
     :syntax ["acos(:number)"]
     :description ["Returns the arc cosine of an angle in radians."
                   "If :number is NaN or its absolute value is greater than 1, then the result is NaN."]
     :examples [{:q "set @acos = acos(0.5)"}]
     :related ["function:cos", "function:cosh", "function:asin", "function:atan"]}
    {:name "atan"
     :type "function"
     :subtype "trigonometric"
     :syntax ["atan(:number)"]
     :description ["Returns the arc tangent of an angle in radians."
                   "If :number is NaN, then the result is NaN. If :number is zero, then the result is zero."]
     :examples [{:q "set @atan = atan(0.5)"}]
     :related ["function:tan", "function:tanh", "function:asin", "function:acos"]}
    {:name "atan2"
     :type "function"
     :subtype "trigonometric"
     :syntax ["atan2(:y, :x)"]
     :description ["Returns the angle theta from the conversion of rectangular coordinates (x, y) to polar coordinates (r, theta)."]
     :examples [{:q "set @atan2 = atan2(1, 1)"}]
     :related ["function:tan", "function:tanh", "function:tan", "function:atan"]}
    {:name "pdfuniform"
     :type "function"
     :subtype "probability"
     :syntax ["pdfuniform(:number)"
              "pdfuniform(:number, :min, :max)"
              "pdfuniform(:list)"
              "pdfuniform(:list, :min, :max)"]
     :description ["Returns the continuous uniform distribution function of the given :number or :list, with default values of min=0, max=1."
                   "If a :list is provided, the result will be a list with pdfuniform() applied to each element."]
     :examples [{:q "set @pdf = pdfuniform(0.5), @pdf2 = pdfuniform(0.5, 0, 5)"}
                {:description "Applying pdfuniform() to a list"
                 :q "project range(0, 10) | P = pdfnormal(value, 0, 10)"}]
     :related ["function:cdfuniform", "function:pdfnormal", "function:pdfpoisson"]}
    {:name "pdfnormal"
     :type "function"
     :subtype "probability"
     :syntax ["pdfnormal(:number)"
              "pdfnormal(:number, :mean, :sd)"
              "pdfnormal(:list)"
              "pdfnormal(:list, :mean, :sd)"]
     :description ["Returns the continuous Normal cumulative distribution function of the given :number or :list, with default values of mean=0, sd=1."
                   "If a :list is provided, the result will be a list with pdfnormal() applied to each element."]
     :examples [{:q "set @pdf = pdfnormal(0.5), @pdf2 = pdfnormal(0.5, 0, 5)"}
                {:description "Applying pdfnormal() to a list"
                 :q "project range(0, 10) | P = pdfnormal(value, 0, 10)"}]
     :related ["function:cdfnormal", "function:pdfuniform", "function:pdfpoisson"]}
    {:name "pdfpoisson"
     :type "function"
     :subtype "probability"
     :syntax ["pdfpoisson(:number)"
              "pdfpoisson(:number, :lambda)"
              "pdfpoisson(:list)"
              "pdfpoisson(:list, :lambda)"]
     :description ["Returns the continuous Poisson cumulative distribution function of the given :number or :list, with default value of lambda=1."
                   "If a :list is provided, the result will be a list with pdfpoisson() applied to each element."]
     :examples [{:q "set @pdf = pdfpoisson(0.5), @pdf2 = pdfpoisson(0.5, 5)"}
                {:description "Applying pdfpoisson() to a list"
                 :q "project range(0, 10) | P = pdfpoisson(value, 2)"}]
     :related ["function:cdfpoisson", "function:pdfuniform", "function:pdfnormal"]}
    {:name "cdfuniform"
     :type "function"
     :subtype "probability"
     :syntax ["cdfuniform(:number)"
              "cdfuniform(:number, :min, :max)"
              "cdfuniform(:list)"
              "cdfuniform(:list, :min, :max)"]
     :description ["Returns the Uniform cumulative distribution function of the given :number or :list, with default values of min=0, max=1."
                   "If a :list is provided, the result will be a list with cdfuniform() applied to each element."]
     :examples [{:q "set @cdf = cdfuniform(0.5), @cdf2 = cdfuniform(0.5, 0, 5)"}
                {:description "If plant weights are equally probable, calculate the probability for each plant that another plant weighs less than or equal to it."
                 :q "input mock name=\"plant-growth\" | P = cdfuniform(weight, min(weight), max(weight))"}
                {:description "Same as above, but with lists"
                 :q "input mock name=\"plant-growth\" \n| set @weights = pluck(weight)\n| set @P = cdfuniform(@weights, listmin(@weights), listmax(@weights));"}]
     :related ["function:pdfuniform", "function:cdfnormal", "function:cdfpoisson"]}
    {:name "cdfnormal"
     :type "function"
     :subtype "probability"
     :syntax ["cdfnormal(:number)"
              "cdfnormal(:number, :mean, :sd)"
              "cdfnormal(:list)"
              "cdfnormal(:list, :mean, :sd)"]
     :description ["Returns the Normal cumulative distribution function of the given :number or :list, with default values of mean=0, sd=1."
                   "If a :list is provided, the result will be a list with cdfnormal() applied to each element."]
     :examples [{:q "set @cdf = cdfnormal(0.5), @cdf2 = cdfnormal(0.5, 0, 5)"}
                {:description "If plant weights have a normal distribution, calculate the probability for each plant that another plant weighs less than or equal to it."
                 :q "input mock name=\"plant-growth\" | P = cdfnormal(weight, mean(weight), stddev(weight))"}
                {:description "Same as above, but with lists"
                 :q "input mock name=\"plant-growth\" \n| set @weights = pluck(weight)\n| set @P = cdfnormal(@weights, listmean(@weights), liststddev(@weights));"}]
     :related ["function:pdfnormal", "function:cdfuniform", "function:cdfpoisson"]}
    {:name "cdfpoisson"
     :type "function"
     :subtype "probability"
     :syntax ["cdfpoisson(:number)"
              "cdfpoisson(:number, :lambda)"
              "cdfpoisson(:list)"
              "cdfpoisson(:list, :lambda)"]
     :description ["Returns the Poisson cumulative distribution function of the given :number or :list, with default value of lambda=1."
                   "If a :list is provided, the result will be a list with cdfpoisson() applied to each element."]
     :examples [{:q "set @cdf = cdfpoisson(3), @cdf2 = cdfpoisson(3, 4)"}
                {:description "If plant weights have a normal distribution, calculate the probability for each plant that another plant weighs less than or equal to it."
                 :q "input mock name=\"plant-growth\" | P = cdfpoisson(weight, mean(weight), stddev(weight))"}
                {:description "Applying cdfpoisson() to a list"
                 :q "set @l = cdfpoisson([1,2,3,4,5,6,7], 2)"}]
     :related ["function:pdfpoisson", "function:cdfuniform", "function:cdfnormal"]}
    {:name "len"
     :type "function"
     :subtype "string"
     :description ["len() is an alias for length()."]
     :aliases ["function:length"]}
    {:name "substring"
     :type "function"
     :subtype "string"
     :syntax ["substring(:string, :start)"
              "substring(:string, :start, :end)"]
     :description ["Returns a substring of the given :string, beginning with the character at the :start index, and extending either to the end of the string, or to the character before the :end index."
                   "The :start and :end indices are inclusive and exclusive, respectively. If the :end index is beyond the end of :string, the substring will extend to the end of the :string."
                   "Returns null if :string is either null or not a string."
                   "Do not confuse this function with substr(), which takes a :start index and a number of characters."]
     :examples [{:description "With start but no end"
                 :q "set @sub = substring(\"Parsec is cool!\", 10)"}
                {:description "With a start and end"
                 :q "set @sub = substring(\"Parsec is cool!\", 10, 14)"}
                {:description "Using indexof()"
                 :q "set @s = \"Hello World\", @sub = substring(@s, indexof(@s, \"o\"), indexof(@s, \"r\"))"}]
     :related ["function:substr"]}
    {:name "substr"
     :type "function"
     :subtype "string"
     :syntax ["substr(:string, :start)"
              "substr(:string, :start, :length)"]
     :description ["Returns a substring of the given :string, beginning with the character at the :start index, and extending either to the end of the string, or to the next :length number of characters."
                   "The :start index is inclusive.  If the :length extends beyond the end of :string, the substring will extend to the end of the :string."
                   "Returns null if :string is either null or not a string."
                   "Do not confuse this function with substring(), which takes a :start index and an :end index."]
     :examples [{:description "With start but no length"
                 :q "set @sub = substr(\"Parsec is cool!\", 10)"}
                {:description "With a start and length"
                 :q "set @sub = substr(\"Parsec is cool!\", 10, 4)"}
                {:description "Using indexof()"
                 :q "set @s = \"Hello World\", @sub = substr(@s, indexof(@s, \"W\"))"}]
     :related ["function:substring", "function:indexof"]}
    {:name "indexof"
     :type "function"
     :subtype "string"
     :syntax ["indexof(:string, :value)"]
     :description ["Returns the first index of :value in :string, if it exists.  If :value is not found, it will return null."
                   "If :value is not a string, it will be converted to a string automatically.  This allows indexof() to find numbers in a string, for example."
                   "Returns null if :string is either null or not a string."]
     :examples [{:q "set @index = indexof(\"Parsec\", \"s\")"}
                {:q "set @s = \"Hello World\", @sub = substr(@s, indexof(@s, \"W\"))"}]
     :related ["function:lastindexof"]}
    {:name "lastindexof"
     :type "function"
     :subtype "string"
     :syntax ["lastindexof(:string, :value)"]
     :description ["Returns the last index of :value in :string, if it exists.  If :value is not found, it will return null."
                   "If :value is not a string, it will be converted to a string automatically.  This allows lastindexof() to find numbers in a string, for example."
                   "Returns null if :string is either null or not a string."]
     :examples [{:q "set @index = lastindexof(\"123,445,660.0\", \",\")"}
                {:q "set @s = \"Hello World\", @sub = substr(@s, lastindexof(@s, \"o\"))"}]
     :related ["function:indexof"]}
    {:name "trim"
     :type "function"
     :subtype "string"
     :syntax ["trim(:string)"]
     :returns "string"
     :description ["The trim() function removes whitespace from both ends of a string."
                   "This function follows the Java whitespace definition: https://docs.oracle.com/javase/8/docs/api/java/lang/Character.html#isWhitespace-char-"
                   "Returns null if :string is either null or not a string."]
     :examples [{:q "set @x = trim(\"  parsec\t \")"}]
     :related ["function:ltrim", "function:rtrim"]}
    {:name "ltrim"
     :type "function"
     :subtype "string"
     :syntax ["ltrim(:string)"]
     :returns "string"
     :description ["The ltrim() function removes whitespace from the left end of a string."
                   "This function follows the Java whitespace definition: https://docs.oracle.com/javase/8/docs/api/java/lang/Character.html#isWhitespace-char-"
                   "Returns null if :string is either null or not a string."]
     :examples [{:q "set @x = ltrim(\"  parsec\")"}]
     :related ["function:trim", "function:rtrim"]}
    {:name "rtrim"
     :type "function"
     :subtype "string"
     :syntax ["rtrim(:string)"]
     :returns "string"
     :description ["The rtrim() function removes whitespace from the right end of a string."
                   "This function follows the Java whitespace definition: https://docs.oracle.com/javase/8/docs/api/java/lang/Character.html#isWhitespace-char-"
                   "Returns null if :string is either null or not a string."]
     :examples [{:q "set @x = rtrim(\"parsec  \")"}]
     :related ["function:trim", "function:ltrim"]}
    {:name "uppercase"
     :type "function"
     :subtype "string"
     :syntax ["uppercase(:string)"]
     :returns "string"
     :description ["The uppercase() function returns its argument converted to uppercase."
                   "Returns null if :string is either null or not a string."]
     :examples [{:q "set @x = uppercase(\"parsec\")"}]
     :related ["function:lowercase"]}
    {:name "lowercase"
     :type "function"
     :subtype "string"
     :syntax ["lowercase(:string)"]
     :returns "string"
     :description ["The lowercase() function returns its argument converted to lowercase."
                   "Returns null if :string is either null or not a string."]
     :examples [{:q "set @x = lowercase(\"parsec\")"}]
     :related ["function:uppercase"]}
    {:name "replace"
     :type "function"
     :subtype "string"
     :syntax ["replace(:string, :search, :replacement)"]
     :returns "string"
     :description ["Replaces the first instance of :search in :string with :replacement."
                   "If either :search or :replacement are not strings, they will be converted to strings automatically."
                   "Returns null if :string is either null or not a string."]
     :examples [{:q "set @string = replace(\"aaabbbbccc\", \"a\", \"x\")"}]
     :related ["function:replaceall"]}
    {:name "replaceall"
     :type "function"
     :subtype "string"
     :syntax ["replaceall(:string, :search, :replacement)"]
     :returns "string"
     :description ["Replaces all instance of :search in :string with :replacement."
                   "If either :search or :replacement are not strings, they will be converted to strings automatically."
                   "Returns null if :string is either null or not a string."]
     :examples [{:q "set @string = replaceall(\"aaabbbbccc\", \"a\", \"x\")"}]
     :related ["function:replace"]}
    {:name "startswith"
     :type "function"
     :subtype "string"
     :syntax ["startswith(:string, :search)"]
     :returns "string"
     :description ["Returns true if :string begins with :search, else false."
                   "If :search is not a string, it will be converted to a string automatically."
                   "Returns null if :string is either null or not a string."]
     :examples [{:q "set @test = startswith(\"parsec\", \"p\")"}]
     :related ["function:endswith"]}
    {:name "endswith"
     :type "function"
     :subtype "string"
     :syntax ["endswith(:string, :search)"]
     :returns "string"
     :description ["Returns true if :string ends with :search, else false."
                   "If :search is not a string, it will be converted to a string automatically."
                   "Returns null if :string is either null or not a string."]
     :examples [{:q "set @test = endswith(\"parsec\", \"c\")"}]
     :related ["function:startswith"]}
    {:name "join"
     :type "function"
     :subtype "string"
     :syntax ["join(:list)"
              "join(:list, :separator)"]
     :returns "string"
     :description ["Returns a string of all elements in :list, concatenated together with an optional :separator between them."
                   "Returns null if :list is either null or not a list."]
     :examples [{:q "set @string = join([\"a\", \"b\", \"c\"], \", \")"}
                {:q "input mock | set @col1 = join(pluck(col1), \"-\")"}]}
    {:name "split"
     :type "function"
     :subtype "string"
     :syntax ["split(:string, :delimiter)"]
     :returns "list"
     :description ["Splits a string into tokens using a delimiter and returns a list of the tokens.  A regex can be given as :delimiter."]
     :examples [{:q "input mock | x = split(\"1,2,3\", \",\")"}
                {:description "Using regex to match delimiters"
                 :q "set @phone = pop(split(\"(425)555-1234\", \"[^\\d]\"))"}]}
    {:name "urlencode"
     :type "function"
     :subtype "string"
     :syntax ["urlencode(:string)"]
     :returns "string"
     :description ["Translates a string into application/x-www-form-urlencoded format."
                   "Returns null if :string is either null or not a string."]
     :examples [{:q "input mock | x = urlencode(\"x=1&y=2\")"}]
     :related ["function:urldecode"]}
    {:name "urldecode"
     :type "function"
     :subtype "string"
     :syntax ["urldecode(:string)"]
     :returns "string"
     :description ["Decodes a string encoded in the application/x-www-form-urlencoded format."
                   "Returns null if :string is either null or not a string."]
     :examples [{:q "input mock | x = urldecode(\"x%3D1%26y%3D2\")"}]
     :related ["function:urlencode"]}
    {:name "base64encode"
     :type "function"
     :subtype "string"
     :syntax ["base64encode(:string)"]
     :returns "string"
     :description ["Encodes a string into a base64 string representation."
                   "Returns null if :string is either null or not a string."]
     :examples [{:q "input mock | x = base64encode(\"hello world\"), y = base64decode(x)"}]
     :related ["function:base64decode"]}
    {:name "base64decode"
     :type "function"
     :subtype "string"
     :syntax ["base64decode(:string)"]
     :returns "string"
     :description ["Decodes a string from a base64-encoded string."
                   "Returns null if :string is either null or not a string."]
     :examples [{:q "input mock | x = base64encode(\"hello world\"), y = base64decode(x)"}]
     :related ["function:base64encode"]}
    {:name "parsejson"
     :type "function"
     :subtype "parsing"
     :syntax ["parsejson(:string)"]
     :description ["Parses a JSON string and returns the result.  Any valid JSON data type may be returned, including lists, maps, strings, numbers, and booleans."]
     :examples [{:description "Parsing a string"
                 :q "input mock | x = parsejson(\"\"my-json\"\")"}
                {:description "Parsing a list"
                 :q "input mock | list = parsejson(\"[\" + col1 + \",\" + col2 + \",\" + col3 + \"]\")"}
                {:description "Projecting a JSON list of maps as a new data set"
                 :q "project parsejson(\"[{ \"x\": 1, \"y\": 2 }, {\"x\": 3, \"y\": 4 }]\")"}]
     :related ["function:jsonpath", "function:parsecsv", "function:parsexml"]}
    {:name "parsecsv"
     :type "function"
     :subtype "parsing"
     :syntax ["parsecsv(:string)"
              "parsecsv(:string, { delimiter: string, quote: string, eol: string })"]
     :description ["Parses a CSV string and returns the result (list of maps)."]
     :examples [{:description "Parsing CSV with default options"
                 :q "set @text=\"a,b,c\n1,2,3\n4,5,6\n7,8,9\", @parsed = parsecsv(@text)"}
                {:description "Using a custom end-of-line and delimiter"
                 :q "set @text=\"col1~col2~col3|1~2~3|4~5~6|7~8~9\", @parsed = parsecsv(@text, { eol: \"|\", delimiter: \"~\" })"}
                {:description "Parsing data without a header row"
                 :q "set @text=\"1,2,3\n4,5,6\n7,8,9\", @parsed = parsecsv(@text, { headers: false })"}
                {:description "Parsing a TSV string (tab-separated values)"
                 :q "set @text=\"col1\tcol2\tcol3|1\t2\t3|4\t5\t6|7\t8\t9\", @parsed = parsecsv(@text, { eol: \"|\", delimiter: \"\t\" })"}]
     :related ["function:parsejson", "function:parsexml"]}
    {:name "parsexml"
     :type "function"
     :subtype "parsing"
     :syntax ["parsexml(:string)"
              "parsexml(:string, :xpath)"
              "parsexml(:string, { xpath: string, raw: boolean, flatten: boolean })"]
     :description ["Parses an XML string and returns the result (list of maps)."]
     :examples [{:description "Without an XPath expression, parsing starts at the root element"
                 :q "input mock | xml = \"<root>\" + col1 + \"</root>\", parsed = parsexml(xml)"}
                {:description "Attributes are parsed into columns"
                 :q "input mock | xml = \"<root q=\"xyz\" s=\"abc\">\" + col1 + \"</root>\", parsed = parsexml(xml)"}
                {:description "By default, child elements are flattened."
                 :q "input mock | xml = \"<root><col1>\" + col1 + \"</col1><col2>\" + col2 + \"</col2></root>\", parsed = parsexml(xml, \"/root\")"}
                {:description "Repeated elements are parsed into multiple rows"
                 :q "input mock | xml = \"<root><val>\" + col1 + \"</val><val>\" + col2 + \"</val></root>\", parsed = parsexml(xml, \"/root/val\")"}
                {:description "Hacker News"
                 :q "input http uri=\"http://news.ycombinator.com/rss\" | project parsexml(first(body), \"/rss/channel/item\")"}]
     :related ["function:parsecsv", "function:parsejson"]}
    {:name "jsonpath"
     :type "function"
     :subtype "parsing"
     :syntax ["jsonpath(:map-or-list, :string)"]
     :description ["Applies a JsonPath expression to a map or list and returns the result.  Any valid JSON data type may be returned, including lists, maps, strings, numbers, and booleans."
                   "This function cannot be used on JSON strings. Use parsejson() beforehand to convert the strings into objects."
                   "The http input type has a built-in option for using jsonpath()."
                   "JsonPath reference: http://goessner.net/articles/JsonPath/.  For specific implementation reference, see: https://github.com/gga/json-path"]
     :examples [{:description "Select a field"
                 :q "set @results = jsonpath({ numbers: [1, 2, 3] }, \"$.numbers\")"}
                {:description "Select a field of all children"
                 :q "set @obj = { messages: [{from: \"me@parsec.com\"}, {from: \"you@parsec.com\"}, {from: \"me@parsec.com\"}] },\n    @results = jsonpath(@obj, \"$.messages[*].from\")"}]
     :related ["input:http", "function:parsejson"]}
    {:name "if"
     :type "function"
     :subtype "conditional"
     :syntax ["if(:predicate, :when-true)"
              "if(:predicate, :when-true, :when-false)"]
     :description ["Evaluates :predicate and, if true, returns :when-true. Else returns :when-false."
                   "If the :when-false clause is omitted, null will be returned"]
     :examples [{:q "input mock | x = if(col1 < 3, \"less than three\", \"greater than three\")"}]}
    {:name "case"
     :type "function"
     :subtype "conditional"
     :syntax ["case(:predicate1, :result1 [, :predicate2, :result2 ...])"]
     :description ["Evalutes a series of test expression & result pairs from left to right. The first test expression that evaluates to true will cause its corresponding result expression to be returned. If no test expression evaluates to true, the function will return null."
                   "If an odd number of arguments are provided, the last will be treated as an \"else\" clause, and returned in absense of any other successful test"]
     :examples [{:description "With two test expressions"
                 :q "input mock | x = case(col3 == 3, \"apples\", col3 == 5, \"oranges\")"}
                {:description "With an \"else\" clause"
                 :q "input mock | x = case(col1 == 1, \"one\", col1 == 2, \"two\", \"else\")"}
                {:description "With a single test expression"
                 :q "input mock | greaterThanThree = case(col1 >= 3, true)"}]}
    {:name "coalesce"
     :type "function"
     :subtype "conditional"
     :syntax ["coalesce(:expression1 [, :expression2, ...])"]
     :description ["Returns the first non-null expression in the argument list."
                   "If all expressions are null, coalesce() returns null."]
     :examples [{:q "input mock | x = coalesce(null, null, 1)"}]}
    {:name "reverse"
     :type "function"
     :subtype "list"
     :syntax ["reverse(:string)"
              "reverse(:list)"]
     :returns "string | list"
     :description ["Returns the characters in a string, or items in a list.  Returns null for all other arguments."]
     :examples [{:description "Reversing a string"
                 :q "set @reversed = reverse(\"Parsec\")"}
                {:description "Reversing a list"
                 :q "set @reversed = reverse([1, 2, 3, 4])"}]}
    {:name "listmean"
     :type "function"
     :subtype "list"
     :syntax ["listmean(:list)"]
     :returns "number"
     :description ["Returns the arithmetic mean of the list elements in :list."
                   "Throws an error if any of the list elements are non-numerical."]
     :examples [{:q "set @listmean = listmean([1, 2, 3, 4])"}]
     :aliases ["function:lmean"]}
    {:name "listmax"
     :type "function"
     :subtype "list"
     :syntax ["listmax(:list)"]
     :returns "number"
     :description ["Returns the element in :list with the maximal value of all elements."
                   "Throws an error if any of the list elements are non-numerical, unless :list has only a single element, in which case that element is returned."]
     :examples [{:q "set @listmax = listmax([2, 10, 4, 8, 5])"}]
     :aliases ["function:lmax"]}
    {:name "listmin"
     :type "function"
     :subtype "list"
     :syntax ["listmin(:list)"]
     :returns "number"
     :description ["Returns the element in :list with the minimal value of all elements."
                   "Throws an error if any of the list elements are non-numerical, unless :list has only a single element, in which case that element is returned."]
     :examples [{:q "set @listmin = listmin([2, 10, 4, 8, 5])"}]
     :aliases ["function:lmin"]}
    {:name "lmean"
     :type "function"
     :subtype "list"
     :description ["lmean() is an alias for listmean()."]
     :aliases ["function:listmean"]}
    {:name "lmax"
     :type "function"
     :subtype "list"
     :description ["lmax() is an alias for listmax()."]
     :aliases ["function:listmax"]}
    {:name "lmin"
     :type "function"
     :subtype "list"
     :description ["lmin() is an alias for listmin()."]
     :aliases ["function:listmin"]}
    {:name "liststddev"
     :type "function"
     :subtype "list"
     :syntax ["liststddev(:list)"]
     :returns "number"
     :description ["Returns the sample standard deviation of :list."
                   "If the list is the complete population, use liststddevp() instead to calculate the population standard deviation."]
     :examples [{:q "set @liststddev = liststddev([2, 10, 4, 8, 5])"}]
     :related ["function:liststddevp"]}
    {:name "liststddevp"
     :type "function"
     :subtype "list"
     :syntax ["liststddevp(:list)"]
     :returns "number"
     :description ["Returns the population standard deviation of :list."
                   "If the list is not the complete population, use liststddev() instead to calculate the sample standard deviation."]
     :examples [{:q "set @liststddevp = liststddevp([2, 10, 4, 8, 5])"}]
     :related ["function:liststddev"]}
    {:name "length"
     :type "function"
     :subtype "list"
     :syntax ["length(:string)"
              "length(:list)"]
     :returns "number"
     :description ["Returns the number of characters in a string, or items in a list.  Returns null for all other arguments."]
     :examples [{:description "Length of a string"
                 :q "set @length = length(\"Parsec\")"}
                {:description "Length of a list"
                 :q "set @length = length([1, 2, 3, 4])"}]
     :aliases ["function:len"]}
    {:name "peek"
     :type "function"
     :subtype "list"
     :returns "list"
     :syntax ["peek(:list)"]
     :description ["Returns the first item in a list. To retrieve the last item of the list, use peeklast()."
                   "peek() on an empty list returns null."]
     :examples [{:q "set @first = peek([1,2,3])"}]
     :related ["function:peeklast", "function:pop", "function:push"]}
    {:name "peeklast"
     :type "function"
     :subtype "list"
     :returns "list"
     :syntax ["peeklast(:list)"]
     :description ["Returns the last item in a list. To retrieve the first item of the list, use peek()."
                   "peeklast() on an empty list returns null."]
     :examples [{:q "set @last = peeklast([1,2,3])"}]
     :related ["function:peek", "function:pop", "function:push"]}
    {:name "pop"
     :type "function"
     :subtype "list"
     :returns "list"
     :syntax ["pop(:list)"]
     :description ["Returns a list without the first item. To retrieve the first value of the list, use peek()."
                   "pop() on an empty list throws an error."]
     :examples [{:q "set @list = pop([1,2,3])"}]
     :related ["function:peek", "function:push"]}
    {:name "push"
     :type "function"
     :subtype "list"
     :returns "list"
     :syntax ["push(:list, :value)"
              "push(:list, :value, :value, ...)"]
     :description ["Appends one or more values to the end of a list and returns the updated list."]
     :examples [{:description "One value"
                 :q "set @list = push([1, 2, 3], 4)"}
                {:description "Two values"
                 :q "set @list = push([1, 2, 3], 4, 5)"}]
     :related ["function:pop", "function:concat"]}
    {:name "concat"
     :type "function"
     :subtype "list"
     :returns "list"
     :syntax ["concat(:list, :list)"
              "concat(:list, :list, :list, ...)"
              "concat(:list, :value)"]
     :description ["Concatenates one or more lists or values together, from left to right."
                   "Non-list arguments will be converted into lists and concatenated."]
     :examples [{:description "Two lists"
                 :q "set @list = concat([1, 2, 3], [4, 5, 6])"}
                {:description "Three lists"
                 :q "set @list = concat([1, 2, 3], [4, 5, 6], [7, 8, 9])"}
                {:description "Lists and non-lists"
                 :q "set @list = concat([1, 2, 3], 4, 5, [6, 7])"}]
     :related ["function:push"]}
    {:name "contains"
     :type "function"
     :subtype "list"
     :returns "boolean"
     :syntax ["contains(:list, :expr)", "contains(:string, :expr)"]
     :description ["Returns true if :expr is contained as a member of :list, else false."
                   "If :string is given instead of :list, it returns true if :expr is a substring of :string, else false.  If necessary, :expr will be automatically converted to a string to perform the comparison."]
     :examples [{:q "input mock | set @list = [1, 2, 5] | test = contains(@list, col1)"}
                {:q "input mock | str = \"hello world\", hasWorld = contains(str, \"world\")"}]}
    {:name "distinct"
     :type "function"
     :subtype "list"
     :returns "list"
     :syntax ["distinct(:list)"]
     :description ["Returns a list of the elements of :list with duplicates removed."
                   "distinct(null) returns null; same for any non-list argument."]
     :examples [{:q "input mock | set @list = [1, 1, 2, 3, 2, 3] | test = distinct(@list)"}]}
    {:name "flatten"
     :type "function"
     :subtype "list"
     :returns "list"
     :syntax ["flatten(:list)"]
     :description ["Flattens a list of lists one level deep."]
     :examples [{:description "Two lists"
                 :q "set @list = flatten([[1, 2, 3], [4, 5, 6]])"}
                {:description "Three lists"
                 :q "set @list = flatten([[1, 2, 3], [4, 5, 6], [7, 8, 9]])"}
                {:description "Lists and non-lists"
                 :q "set @list = flatten([0, [1, 2, 3], 4, 5, [6, 7]])"}
                {:description "Deeply-nested data"
                 :q "set @list = flatten([[[\"red\", \"blue\"], [\"apple\", \"orange\"]], [[\"WA\", \"CA\"], [\"Seattle\", \"San Franscisco\"]]])"}]
     :related ["function:flattendeep"]}
    {:name "flattendeep"
     :type "function"
     :subtype "list"
     :returns "list"
     :syntax ["flattendeep(:list)"]
     :description ["Flattens a list of lists recursively."]
     :examples [{:description "Two lists"
                 :q "set @list = flattendeep([[1, 2, 3], [4, 5, 6]])"}
                {:description "Lists and non-lists"
                 :q "set @list = flattendeep([0, [1, 2, 3], 4, 5, [6, 7]])"}
                {:description "Deeply-nested data"
                 :q "set @list = flattendeep([[[\"WA\", \"OR\", \"AK\"], [\"CA\", \"AZ\"]], [[\"IN\", \"IL\"], [\"GA\", \"TX\"]]])"}]
     :related ["function:flatten"]}
    {:name "index"
     :type "function"
     :subtype "list"
     :returns "any"
     :syntax ["index(:list, :index)"]
     :description ["Returns the element of :list at the given 0-based :index."]
     :examples [{:q "input mock | map = tomap(\"foo\", \"bar\", \"fooz\", \"baz\"), values = values(map), second = index(values, 1)"}]}
    {:name "range"
     :type "function"
     :subtype "list"
     :syntax ["range(start,end,step)"]
     :returns "list"
     :description ["Returns a list of numbers from start to end, by step.  If only one argument is provided (end), start defaults to 0.  If two arguments are provided (start,end), step defaults to 1.  If step=0 it will remain the default of 1."]
     :examples [{:q "input mock | range = range(10)"}
                {:q "input mock | range = range(-10, 10)"}
                {:q "input mock | range = range(-10, 10, 2)"}]}
    {:name "get"
     :type "function"
     :subtype "map"
     :syntax ["get(:map, :key)"
              "get(:map, :key, :default)"
              "get(:map, [:key, ...])"
              "get(:map, [:key, ...], :default)"]
     :returns "any"
     :description ["Retrieves the value for :key in :map.  If a list of :keys is given, it will traverse nested maps by applying get() in a recursive fashion."
                   "Returns null if :key is not found, or optionally :default if provided."]
     :examples [{:q "set @headers = {Accepts: \"application/json\"}, @accepts = get(@headers, \"Accepts\")"}
                {:description "With a default value"
                 :q "set @headers = {Accepts: \"application/json\"}, @accepts = get(@headers, \"Accepts\")"}]
     :related ["function:set", "function:delete", "function:merge"]}
    {:name "set"
     :type "function"
     :subtype "map"
     :syntax ["set(:map, :key, :value)"
              "set(:map, [:key, ...], :value)"]
     :returns "map"
     :description ["Sets the :value for :key in :map, and returns the modified map."
                   "If a list of :keys is given, it will traverse nested maps for each :key, and set the value of the last :key to :value. Any missing keys will be initialized with empty maps. If a key exists but is not a map, an exception will be thrown."]
     :examples [{:q "set @headers = {},\n    @headers = set(@headers, \"Accept\", \"text/plain\")\n    @headers = set(@headers, \"Content-Type\", \"application/xml\")"}
                {:description "Setting nested keys"
                 :q "set @req = {},\n    @req = set(@req, [\"headers\", \"Accept\"], \"text/plain\")\n    @req = set(@req, [\"headers\", \"Content-Type\"], \"application/xml\")"}]
     :related ["function:get", "function:delete", "function:merge"]}
    {:name "delete"
     :type "function"
     :subtype "map"
     :syntax ["delete(:map, :key)"
              "delete(:map, [:key, ...])"]
     :returns "map"
     :description ["Deletes the value for :key in :map, and returns the modified map."
                   "If a list of :keys is given, it will traverse nested maps and for each :key, and delete the  last :key.  Any maps which were made empty will be removed."]
     :examples [{:q "set @headers = {\"Accept\": \"application/json\", \"Content-Type\": \"application/json\"},\n    @updated = delete(@headers, \"Accept\")"}
                {:description "Deleting a nested key"
                 :q "set @req = {headers: {\"Accept\": \"application/json\", \"Content-Type\": \"application/json\"}},\n    @updated = delete(@req, [\"headers\", \"Accept\"])"}]
     :related ["function:get", "function:set", "function:merge"]}
    {:name "merge"
     :type "function"
     :subtype "map"
     :syntax ["merge(:map, ...)"]
     :returns "map"
     :description ["Returns a map created by merging the keys of each argument :map together, with subsequent maps overwriting keys of previous maps."
                   "Any number of maps can be merged together. Any non-map arguments will be ignored."]
     :examples [{:q "set @headers = merge({\"Content-Type\": \"application/json\", \"Accept-Language\": \"en-US,en;q=0.8\"}, {\"Content-Length\": 100})"}]
     :related ["function:get", "function:set", "function:delete", "function:merge"]}
    {:name "keys"
     :type "function"
     :subtype "map"
     :syntax ["keys(:map)"]
     :returns "list"
     :description ["Returns a list of the keys in :map."
                   "keys(null) returns null."]
     :examples [{:q "input mock | map = tomap(\"foo\", \"bar\", \"fooz\", \"baz\"), keys = keys(map)"}]
     :related ["function:values"]}
    {:name "values"
     :type "function"
     :subtype "map"
     :syntax ["values(:map)"]
     :returns "list"
     :description ["Returns a list of the values in :map."
                   "values(null) returns null."]
     :examples [{:q "input mock | map = tomap(\"foo\", \"bar\", \"fooz\", \"baz\"), values = values(map)"}]
     :related ["function:keys"]}
    {:name "adler32"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["adler32(:string)"]
     :description ["Computes the Adler-32 checksum of :string"]
     :examples [{:q "set @digest = adler32(\"hello world\")"}]
     :related ["function:sha256", "function:sha1", "function:sha512"]}
    {:name "crc32"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["crc32(:string)"]
     :description ["Computes the CRC-32 cyclic redundancy check of :string"]
     :examples [{:q "set @digest = crc32(\"hello world\")"}]
     :related ["function:adler32", "function:md5"]}
    {:name "gost"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["gost(:string)"]
     :description ["Computes the GOST R 34.11-94 hash of :string"]
     :examples [{:q "set @digest = gost(\"hello world\")"}]
     :related ["function:hmac_gost", "function:ripemd128", "function:sha1"]}
    {:name "hmac_gost"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["hmac_gost(:string, :secret)"]
     :description ["Computes the HMAC of :string using the GOST R 34.11-94 hash and :secret"]
     :examples [{:q "set @digest = hmac_gost(\"hello world\", \"secret\")"}]
     :related ["function:gost", "function:ripemd128"]}
    {:name "md5"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["md5(:string)"]
     :description ["Computes the MD5 hash of :string"]
     :examples [{:q "set @digest = md5(\"hello world\")"}]
     :related ["function:hmac_md5", "function:sha256", "function:sha1", "function:sha512"]}
    {:name "hmac_md5"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["hmac_md5(:string, :secret)"]
     :description ["Computes the HMAC of :string using the MD5 hash and :secret"]
     :examples [{:q "set @digest = hmac_md5(\"hello world\", \"secret\")"}]
     :related ["function:md5", "function:sha256", "function:sha1", "function:sha512"]}
    {:name "ripemd128"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["ripemd128(:string)"]
     :description ["Computes the RIPEMD-128 hash of :string"]
     :examples [{:q "set @digest = ripemd128(\"hello world\")"}]
     :related ["function:hmac_ripemd128", "function:gost", "function:ripemd256", "function:ripemd320", "function:sha1"]}
    {:name "hmac_ripemd128"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["hmac_ripemd128(:string, :secret)"]
     :description ["Computes the HMAC of :string using the RIPEMD-128 hash and :secret"]
     :examples [{:q "set @digest = hmac_ripemd128(\"hello world\", \"secret\")"}]
     :related ["function:ripemd128", "function:hmac_gost", "function:hmac_ripemd128"]}
    {:name "ripemd256"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["ripemd256(:string)"]
     :description ["Computes the RIPEMD-256 hash of :string"]
     :examples [{:q "set @digest = ripemd256(\"hello world\")"}]
     :related ["function:hmac_ripemd256", "function:gost", "function:ripemd128", "function:ripemd320", "function:sha256"]}
    {:name "hmac_ripemd256"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["hmac_ripemd256(:string, :secret)"]
     :description ["Computes the HMAC of :string using the RIPEMD-256 hash and :secret"]
     :examples [{:q "set @digest = hmac_ripemd256(\"hello world\", \"secret\")"}]
     :related ["function:ripemd256", "function:hmac_gost", "function:hmac_ripemd128"]}
    {:name "ripemd320"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["ripemd320(:string)"]
     :description ["Computes the RIPEMD-320 hash of :string"]
     :examples [{:q "set @digest = ripemd320(\"hello world\")"}]
     :related ["function:hmac_ripemd320", "function:gost", "function:ripemd128", "function:ripemd256", "function:sha384"]}
    {:name "hmac_ripemd320"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["hmac_ripemd320(:string, :secret)"]
     :description ["Computes the HMAC of :string using the RIPEMD-320 hash and :secret"]
     :examples [{:q "set @digest = hmac_ripemd320(\"hello world\", \"secret\")"}]
     :related ["function:ripemd256", "function:hmac_gost", "function:hmac_ripemd128"]}
    {:name "sha1"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["sha1(:string)"]
     :description ["Computes the SHA-1 hash of :string"]
     :examples [{:q "set @digest = sha1(\"hello world\")"}]
     :related ["function:hmac_sha1", "function:sha1", "function:sha512", "function:md5"]}
    {:name "hmac_sha1"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["hmac_sha1(:string, :secret)"]
     :description ["Computes the HMAC of :string using the SHA-1 hash and :secret"]
     :examples [{:q "set @digest = hmac_sha1(\"hello world\", \"secret\")"}]
     :related ["function:sha1", "function:sha512", "function:md5"]}
    {:name "sha3_224"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["sha3_224(:string)"]
     :description ["Computes the SHA-3-224 hash of :string"]
     :examples [{:q "set @digest = sha3_224(\"hello world\")"}]
     :related ["function:hmac_sha3_224", "function:sha1", "function:sha256", "function:md5"]}
    {:name "hmac_sha3_224"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["hmac_sha3_224(:string, :secret)"]
     :description ["Computes the HMAC of :string using the SHA-3-224 hash and :secret"]
     :examples [{:q "set @digest = hmac_sha3_224(\"hello world\", \"secret\")"}]
     :related ["function:sha3_224", "function:sha512", "function:sha1", "function:hmac_sha1", "function:md5"]}
    {:name "sha3_256"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["sha3_256(:string)"]
     :description ["Computes the SHA-3-256 hash of :string"]
     :examples [{:q "set @digest = sha3_256(\"hello world\")"}]
     :related ["function:hmac_sha3_256", "function:sha1", "function:sha256", "function:md5"]}
    {:name "hmac_sha3_256"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["hmac_sha3_256(:string, :secret)"]
     :description ["Computes the HMAC of :string using the SHA-3-256 hash and :secret"]
     :examples [{:q "set @digest = hmac_sha3_256(\"hello world\", \"secret\")"}]
     :related ["function:sha3_256", "function:sha512", "function:sha1", "function:hmac_sha1", "function:md5"]}
    {:name "sha3_384"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["sha3_384(:string)"]
     :description ["Computes the SHA-3-384 hash of :string"]
     :examples [{:q "set @digest = sha3_384(\"hello world\")"}]
     :related ["function:hmac_sha3_384", "function:sha1", "function:sha384", "function:md5"]}
    {:name "hmac_sha3_384"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["hmac_sha3_384(:string, :secret)"]
     :description ["Computes the HMAC of :string using the SHA-3-384 hash and :secret"]
     :examples [{:q "set @digest = hmac_sha3_384(\"hello world\", \"secret\")"}]
     :related ["function:sha3_384", "function:sha512", "function:sha1", "function:hmac_sha1", "function:md5"]}
    {:name "sha3_512"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["sha3_512(:string)"]
     :description ["Computes the SHA-3-224 hash of :string"]
     :examples [{:q "set @digest = sha3_512(\"hello world\")"}]
     :related ["function:hmac_sha3_512", "function:sha1", "function:sha256", "function:md5"]}
    {:name "hmac_sha3_512"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["hmac_sha3_512(:string, :secret)"]
     :description ["Computes the HMAC of :string using the SHA-3-512 hash and :secret"]
     :examples [{:q "set @digest = hmac_sha3_512(\"hello world\", \"secret\")"}]
     :related ["function:sha3_512", "function:sha512", "function:sha1", "function:hmac_sha1", "function:md5"]}
    {:name "sha256"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["sha256(:string)"]
     :description ["Computes the SHA-256 hash of :string"]
     :examples [{:q "set @digest = sha256(\"hello world\")"}]
     :related ["function:hmac_sha256", "function:sha1", "function:sha512", "function:md5"]}
    {:name "hmac_sha256"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["hmac_sha256(:string, :secret)"]
     :description ["Computes the HMAC of :string using the SHA-256 hash and :secret"]
     :examples [{:q "set @digest = hmac_sha256(\"hello world\", \"secret\")"}]
     :related ["function:sha256", "function:sha1", "function:sha512", "function:md5"]}
    {:name "sha512"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["sha512(:string)"]
     :description ["Computes the SHA-512 hash of :string"]
     :examples [{:q "set @digest = sha512(\"hello world\")"}]
     :related ["function:hmac_sha512", "function:sha1", "function:sha256", "function:md5"]}
    {:name "hmac_sha512"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["hmac_sha512(:string, :secret)"]
     :description ["Computes the HMAC of :string using the SHA-512 hash and :secret"]
     :examples [{:q "set @digest = hmac_sha512(\"hello world\", \"secret\")"}]
     :related ["function:sha512", "function:sha1", "function:hmac_sha1", "function:md5"]}
    {:name "siphash"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["siphash(:string, :secret)"]
     :description ["Computes the HMAC of :string using the SipHash-2-4 hash and a 128-bit :secret."
                   ":secret must be 128-bits, e.g. a 16-character string."]
     :examples [{:q "set @digest = siphash(\"hello world\", \"abcdefghijklmnop\")"}]
     :related ["function:siphash48", "function:sha1", "function:hmac_sha1", "function:md5"]}
    {:name "siphash48"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["siphash48(:string, :secret)"]
     :description ["Computes the HMAC of :string using the SipHash-4-8 hash and a 128-bit :secret."
                   ":secret must be 128-bits, e.g. a 16-character string."]
     :examples [{:q "set @digest = siphash48(\"hello world\", \"abcdefghijklmnop\")"}]
     :related ["function:siphash", "function:sha1", "function:hmac_sha1", "function:md5"]}
    {:name "tiger"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["tiger(:string)"]
     :description ["Computes the Tiger-192 hash of :string"]
     :examples [{:q "set @digest = tiger(\"hello world\")"}]
     :related ["function:hmac_tiger", "function:sha1", "function:sha256", "function:md5"]}
    {:name "hmac_tiger"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["hmac_tiger(:string, :secret)"]
     :description ["Computes the HMAC of :string using the Tiger-192 hash and :secret"]
     :examples [{:q "set @digest = hmac_tiger(\"hello world\", \"secret\")"}]
     :related ["function:tiger", "function:sha1", "function:hmac_sha1", "function:md5"]}
    {:name "whirlpool"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["whirlpool(:string)"]
     :description ["Computes the Whirlpool hash of :string"]
     :examples [{:q "set @digest = whirlpool(\"hello world\")"}]
     :related ["function:hmac_whirlpool", "function:tiger", "function:sha256", "function:md5"]}
    {:name "hmac_whirlpool"
     :type "function"
     :subtype "digest"
     :returns "string"
     :syntax ["hmac_whirlpool(:string, :secret)"]
     :description ["Computes the HMAC of :string using the Whirlpool hash and :secret"]
     :examples [{:q "set @digest = hmac_whirlpool(\"hello world\", \"secret\")"}]
     :related ["function:whirlpool", "function:hmac_tiger", "function:hmac_sha1", "function:md5"]}
    {:name "random"
     :type "function"
     :subtype "math"
     :syntax ["random()"
              "random(:upperBound)"
              "random(:lowerBound, :upperBound)"]
     :returns "double"
     :description ["Returns a random number between 0 inclusive and 1 exclusive, when no arguments are provided."
                   "When called with a single argument, returns a random number between 0 inclusive and :upperBound, exclusive."
                   "When called with two arguments, returns a random number between :lowerBound inclusive and :upperBound, exclusive."]
     :examples [{:description "Random number between 0 and 1 (exclusive)"
                 :q "set @rand = random()"}
                {:description "Random number between 0 and 10 (exclusive)"
                 :q "set @rand = floor(random(10))"}
                {:description "Random number between 1 and 10 (exclusive)"
                 :q "set @rand = floor(random(1,10))"}]}
    {:name "rank"
     :type "function"
     :syntax ["rank()"]
     :description ["Returns the current row number in the dataset. The rownumber statement is faster for the general case, but as a function this may be more versatile."
                   "Can be used in the assignment statement, stats statement, and pivot statement. Cannot be used in a group-by expression."]
     :examples [{:q "input mock | x = rank()"}
                {:q "input mock | name = \"row \" + (rank() + 1)"}
                {:q "input mock name=\"chick-weight\" | stats avgWeight = mean(weight), rank=rank() by Chick"}]
     :related ["statement:rownumber"]}
    {:name "hostname"
     :type "function"
     :syntax ["hostname()"]
     :description ["Returns the current hostname of the Parsec server."]
     :examples [{:q "set @hostname = hostname()"}]
     :related ["function:ip"]}
    {:name "ip"
     :type "function"
     :syntax ["ip()"]
     :description ["Returns the current IP address of the Parsec server."]
     :examples [{:q "set @ip = ip()"}]
     :related ["function:hostname"]}
    {:name "runtime"
     :type "function"
     :syntax ["runtime()"]
     :description ["Returns information about Parsec\"s JVM runtime."]
     :examples [{:q "set @runtime = runtime()"}
                {:q "project [runtime()]"}]
     :related ["function:os"]}
    {:name "os"
     :type "function"
     :syntax ["os()"]
     :description ["Returns operating-system level information."]
     :examples [{:q "set @os = os()"}
                {:q "project [os()]"}]
     :related ["function:runtime"]}
    {:name "now"
     :type "function"
     :subtype "date and time"
     :syntax ["now()"]
     :description ["Returns the current date/time in the time zone of the server."]
     :examples [{:q "set @now = now()"}]
     :related ["function:nowutc", "function:today", "function:yesterday", "function:tomorrow"]}
    {:name "nowutc"
     :type "function"
     :subtype "date and time"
     :syntax ["nowutc()"]
     :description ["Returns the current date/time in the UTC time zone."]
     :examples [{:q "set @nowutc = nowutc()"}]
     :related ["function:nowutc", "function:todayutc", "function:yesterdayutc", "function:tomorrowutc"]}
    {:name "today"
     :type "function"
     :subtype "date and time"
     :syntax ["today()"]
     :description ["Returns the date/time of midnight of the current day, in the time zone of the server."]
     :examples [{:q "set @today = today()"}]
     :related ["function:todayutc", "function:now", "function:yesterday", "function:tomorrow"]}
    {:name "todayutc"
     :type "function"
     :subtype "date and time"
     :syntax ["todayutc()"]
     :description ["Returns the date/time of midnight of the current day, in the UTC time zone."]
     :examples [{:q "set @todayutc = todayutc()"}]
     :related ["function:today", "function:nowutc", "function:yesterdayutc", "function:tomorrowutc"]}
    {:name "yesterday"
     :type "function"
     :subtype "date and time"
     :syntax ["yesterday()"]
     :description ["Returns the date/time of midnight yesterday, in the time zone of the server."]
     :examples [{:q "set @yesterday = yesterday()"}]
     :related ["function:yesterdayutc", "function:now", "function:today", "function:tomorrow"]}
    {:name "yesterdayutc"
     :type "function"
     :subtype "date and time"
     :syntax ["yesterdayutc()"]
     :description ["Returns the date/time of midnight yesterday, in the UTC time zone."]
     :examples [{:q "set @yesterdayutc = yesterdayutc()"}]
     :related ["function:yesterday", "function:todayutc", "function:nowutc", "function:tomorrowutc"]}
    {:name "tomorrow"
     :type "function"
     :subtype "date and time"
     :syntax ["tomorrow()"]
     :description ["Returns the date/time of midnight tomorrow, in the time zone of the server."]
     :examples [{:q "set @tomorrow = tomorrow()"}]
     :related ["function:todayutc", "function:now", "function:yesterday", "function:tomorrow"]}
    {:name "tomorrowutc"
     :type "function"
     :subtype "date and time"
     :syntax ["tomorrowutc()"]
     :description ["Returns the date/time of midnight tomorrow, in the UTC time zone."]
     :examples [{:q "set @tomorrowutc = tomorrowutc()"}]
     :related ["function:tomorrow", "function:nowutc", "function:yesterdayutc", "function:todayutc"]}
    {:name "tolocaltime"
     :type "function"
     :subtype "date and time"
     :syntax ["tolocaltime(:datetime)"]
     :description ["Converts a date/time to the local time zone of the server."]
     :examples [{:q "set @nowutc = nowutc(), @nowlocal = tolocaltime(@nowutc)"}]
     :related ["function:toutctime", "function:totimezone"]}
    {:name "toutctime"
     :type "function"
     :subtype "date and time"
     :syntax ["toutctime(:datetime)"]
     :description ["Converts a date/time to the UTC time zone."]
     :examples [{:q "set @now = now(), @nowutc = toutctime(@now)"}]
     :related ["function:tolocaltime", "function:totimezone"]}
    {:name "totimezone"
     :type "function"
     :subtype "date and time"
     :syntax ["totimezone(:datetime, :timezone)"]
     :description ["Converts a date/time to another time zone. :timezone must be a long-form time zone, e.g. \"America/Matamoros\"."
                   "A list of all valid time zones can be retrieved using timezones()."]
     :examples [{:q "set @southpole = totimezone(now(), \"Antarctica/South_Pole\"), @str = tostring(@southpole)"}]
     :related ["function:tolocaltime", "function:totimezone", "function:timezones"]}
    {:name "timezones"
     :type "function"
     :subtype "date and time"
     :syntax ["timezones()"]
     :description ["Returns a list of all available time zones, for use with totimezone()"]
     :examples [{:q "set @tz = timezones()"}
                {:description "Project as dataset"
                 :q "project timezones()"}]
     :related ["function:totimezone"]}
    {:name "toepoch"
     :type "function"
     :subtype "date and time"
     :syntax ["toepoch(:datetime)"]
     :returns "number"
     :description ["Converts :datetime into the number of seconds after the Unix epoch."]
     :examples [:q "set @timestamp = toepoch(now())"]
     :related ["function:toepochmillis"]}
    {:name "toepochmillis"
     :type "function"
     :subtype "date and time"
     :syntax ["toepochmillis(:datetime)"]
     :returns "number"
     :description ["Converts :datetime into the number of milliseconds after the Unix epoch."]
     :examples [:q "set @timestamp = toepochmillis(now())"]
     :related ["function:toepoch"]}
    {:name "startofminute"
     :type "function"
     :subtype "date and time"
     :syntax ["startofminute(:datetime)"]
     :returns "datetime"
     :description ["Returns :datetime with the number of seconds and milliseconds zeroed."]
     :examples [:q "set @time = startofminute(now())"]
     :related ["function:startofhour", "function:startofday", "function:startofweek", "function:startofmonth", "function:startofyear"]}
    {:name "startofhour"
     :type "function"
     :subtype "date and time"
     :syntax ["startofhour(:datetime)"]
     :returns "datetime"
     :description ["Returns :datetime with the number of minutes, seconds, and milliseconds zeroed."]
     :examples [:q "set @time = startofhour(now())"]
     :related ["function:startofminute", "function:startofday", "function:startofweek", "function:startofmonth", "function:startofyear"]}
    {:name "startofday"
     :type "function"
     :subtype "date and time"
     :syntax ["startofday(:datetime)"]
     :returns "datetime"
     :description ["Returns midnight on the same day as :datetime."]
     :examples [:q "set @time = startofday(now())"]
     :related ["function:startofminute", "function:startofhour", "function:startofweek", "function:startofmonth", "function:startofyear"]}
    {:name "startofweek"
     :type "function"
     :subtype "date and time"
     :syntax ["startofweek(:datetime)"]
     :returns "datetime"
     :description ["Returns midnight of the first day of the week, in the same week as :datetime."]
     :examples [:q "set @time = startofweek(now())"]
     :related ["function:startofminute", "function:startofhour", "function:startofday", "function:startofmonth", "function:startofyear"]}
    {:name "startofmonth"
     :type "function"
     :subtype "date and time"
     :syntax ["startofmonth(:datetime)"]
     :returns "datetime"
     :description ["Returns midnight of the first day of the month, in the same month as :datetime."]
     :examples [:q "set @time = startofmonth(now())"]
     :related ["function:startofminute", "function:startofhour", "function:startofday", "function:startofweek", "function:startofyear"]}
    {:name "startofyear"
     :type "function"
     :subtype "date and time"
     :syntax ["startofyear(:datetime)"]
     :returns "datetime"
     :description ["Returns midnight of the first day of the year, in the same year as :datetime."]
     :examples [:q "set @time = startofyear(now())"]
     :related ["function:startofminute", "function:startofhour", "function:startofday", "function:startofweek", "function:startofmonth"]}
    {:name "millisecond"
     :type "function"
     :subtype "date and time"
     :syntax ["millisecond(:datetime)"]
     :returns "number"
     :description ["Returns the millisecond of second component of the given :datetime."]
     :examples [:q "set @millis = millisecond(now())"]
     :related ["function:millisecondofday", "function:second", "function:secondofday", "function:minute", "function:minuteofday", "function:hour", "function:day", "function:dayofweek", "function:dayofyear", "function:weekyear", "function:weekofweekyear", "function:month", "function:year"]}
    {:name "millisecondofday"
     :type "function"
     :subtype "date and time"
     :syntax ["millisecondofday(:datetime)"]
     :returns "number"
     :description ["Returns the number of milliseconds elapsed since midnight in :datetime."]
     :examples [:q "set @millis = millisecondofday(now())"]
     :related ["function:millisecond", "function:second", "function:secondofday", "function:minute", "function:minuteofday", "function:hour", "function:day", "function:dayofweek", "function:dayofyear", "function:weekyear", "function:weekofweekyear", "function:month", "function:year"]}
    {:name "second"
     :type "function"
     :subtype "date and time"
     :syntax ["second(:datetime)"]
     :returns "number"
     :description ["Returns the second of minute component of the given :datetime."]
     :examples [:q "set @seconds = second(now())"]
     :related ["function:millisecond", "function:millisecondofday", "function:secondofday", "function:minute", "function:minuteofday", "function:hour", "function:day", "function:dayofweek", "function:dayofyear", "function:weekyear", "function:weekofweekyear", "function:month", "function:year"]}
    {:name "secondofday"
     :type "function"
     :subtype "date and time"
     :syntax ["secondofday(:datetime)"]
     :returns "number"
     :description ["Returns the number of seconds elapsed since midnight in :datetime."]
     :examples [:q "set @seconds = secondofday(now())"]
     :related ["function:millisecond", "function:millisecondofday", "function:second", "function:minute", "function:minuteofday", "function:hour", "function:day", "function:dayofweek", "function:dayofyear", "function:weekyear", "function:weekofweekyear", "function:month", "function:year"]}
    {:name "minute"
     :type "function"
     :subtype "date and time"
     :syntax ["minute(:datetime)"]
     :returns "number"
     :description ["Returns the minute of hour component of the given :datetime."]
     :examples [:q "set @minutes = minute(now())"]
     :related ["function:millisecond", "function:millisecondofday", "function:second", "function:secondofday", "function:minuteofday", "function:hour", "function:day", "function:dayofweek", "function:dayofyear", "function:weekyear", "function:weekofweekyear", "function:month", "function:year"]}
    {:name "minuteofday"
     :type "function"
     :subtype "date and time"
     :syntax ["minuteofday(:datetime)"]
     :returns "number"
     :description ["Returns the number of minutes elapsed since midnight in :datetime."]
     :examples [:q "set @minutes = minuteofday(now())"]
     :related ["function:millisecond", "function:millisecondofday", "function:second", "function:secondofday", "function:minute", "function:hour", "function:day", "function:dayofweek", "function:dayofyear", "function:weekyear", "function:weekofweekyear", "function:month", "function:year"]}
    {:name "hour"
     :type "function"
     :subtype "date and time"
     :syntax ["hour(:datetime)"]
     :returns "number"
     :description ["Returns the hour of day component of the given :datetime."]
     :examples [:q "set @hours = hour(now())"]
     :related ["function:millisecond", "function:millisecondofday", "function:second", "function:secondofday", "function:minute", "function:minuteofday", "function:day", "function:dayofweek", "function:dayofyear", "function:weekyear", "function:weekofweekyear", "function:month", "function:year"]}
    {:name "day"
     :type "function"
     :subtype "date and time"
     :syntax ["day(:datetime)"]
     :returns "number"
     :description ["Returns the day of month component of the given :datetime."]
     :examples [:q "set @day = day(now())"]
     :related ["function:millisecond", "function:millisecondofday", "function:second", "function:secondofday", "function:minute", "function:minuteofday", "function:hour", "function:dayofweek", "function:dayofyear", "function:weekyear", "function:weekofweekyear", "function:month", "function:year"]}
    {:name "dayofweek"
     :type "function"
     :subtype "date and time"
     :syntax ["dayofweek(:datetime)"]
     :returns "number"
     :description ["Returns the day of week component of the given date/time, where Monday is 1 and Sunday is 7."]
     :examples [:q "set @day = dayofweek(now())"]
     :related ["function:millisecond", "function:millisecondofday", "function:second", "function:secondofday", "function:minute", "function:minuteofday", "function:hour", "function:day", "function:dayofyear", "function:weekyear", "function:weekofweekyear", "function:month", "function:year"]}
    {:name "dayofyear"
     :type "function"
     :subtype "date and time"
     :syntax ["dayofyear(:datetime)"]
     :returns "number"
     :description ["Returns the number of days elapsed since the beginning of the year, including the current day of :datetime."]
     :examples [:q "set @days = dayofyear(now())"]
     :related ["function:millisecond", "function:millisecondofday", "function:second", "function:secondofday", "function:minute", "function:minuteofday", "function:hour", "function:day", "function:dayofweek", "function:weekyear", "function:weekofweekyear", "function:month", "function:year"]}
    {:name "weekyear"
     :type "function"
     :subtype "date and time"
     :syntax ["weekyear(:datetime)"]
     :returns "number"
     :description ["Returns the week year for a given date. In the standard ISO8601 week algorithm, the first week of the year is that in which at least 4 days are in the year. As a result of this definition, day 1 of the first week may be in the previous year. The weekyear allows you to query the effective year for that day."
                   "The weekyear is used by the function weekofweekyear()."]
     :examples [:q "set @weekyear = weekyear(now())"]
     :related ["function:millisecond", "function:millisecondofday", "function:second", "function:secondofday", "function:minute", "function:minuteofday", "function:hour", "function:day", "function:dayofweek", "function:dayofyear", "function:weekofweekyear", "function:month", "function:year"]}
    {:name "weekofweekyear"
     :type "function"
     :subtype "date and time"
     :syntax ["weekofweekyear(:datetime)"]
     :returns "number"
     :description ["Given a date, returns the number of weeks elapsed since the beginning of the weekyear. In the standard ISO8601 week algorithm, the first week of the year is that in which at least 4 days are in the year. As a result of this definition, day 1 of the first week may be in the previous year."
                   "The function weekyear() gives the current weekyear for a given date."]
     :examples [:q "set @weeks = weekofweekyear(now())"]
     :related ["function:millisecond", "function:millisecondofday", "function:second", "function:secondofday", "function:minute", "function:minuteofday", "function:hour", "function:day", "function:dayofweek", "function:dayofyear", "function:weekyear", "function:month", "function:year"]}
    {:name "month"
     :type "function"
     :subtype "date and time"
     :syntax ["month(:datetime)"]
     :returns "number"
     :description ["Returns the month component of the given :datetime."]
     :examples [:q "set @months = month(now())"]
     :related ["function:millisecond", "function:millisecondofday", "function:second", "function:secondofday", "function:minute", "function:minuteofday", "function:hour", "function:day", "function:dayofweek", "function:dayofyear", "function:weekyear", "function:weekofweekyear", "function:year"]}
    {:name "year"
     :type "function"
     :subtype "date and time"
     :syntax ["year(:datetime)"]
     :returns "number"
     :description ["Returns the year of the given :datetime."]
     :examples [:q "set @years = year(now())"]
     :related ["function:millisecond", "function:millisecondofday", "function:second", "function:secondofday", "function:minute", "function:minuteofday", "function:hour", "function:day", "function:dayofweek", "function:dayofyear", "function:weekyear", "function:weekofweekyear", "function:month"]}
    {:name "adddays"
     :type "function"
     :subtype "date and time"}
    {:name "addhours"
     :type "function"
     :subtype "date and time"}
    {:name "addmilliseconds"
     :type "function"
     :subtype "date and time"}
    {:name "addminutes"
     :type "function"
     :subtype "date and time"}
    {:name "addmonths"
     :type "function"
     :subtype "date and time"}
    {:name "addseconds"
     :type "function"
     :subtype "date and time"}
    {:name "addweeks"
     :type "function"
     :subtype "date and time"}
    {:name "addyears"
     :type "function"
     :subtype "date and time"}
    {:name "minusdays"
     :type "function"
     :subtype "date and time"}
    {:name "minushours"
     :type "function"
     :subtype "date and time"}
    {:name "minusmilliseconds"
     :type "function"
     :subtype "date and time"}
    {:name "minusminutes"
     :type "function"
     :subtype "date and time"}
    {:name "minusmonths"
     :type "function"
     :subtype "date and time"}
    {:name "minusseconds"
     :type "function"
     :subtype "date and time"}
    {:name "minusweeks"
     :type "function"
     :subtype "date and time"}
    {:name "minusyears"
     :type "function"
     :subtype "date and time"}
    {:name "interval"
     :type "function"
     :subtype "date and time"}
    {:name "inmillis"
     :type "function"
     :subtype "date and time"}
    {:name "inseconds"
     :type "function"
     :subtype "date and time"}
    {:name "inminutes"
     :type "function"
     :subtype "date and time"}
    {:name "inhours"
     :type "function"
     :subtype "date and time"}
    {:name "indays"
     :type "function"
     :subtype "date and time"}
    {:name "inweeks"
     :type "function"
     :subtype "date and time"}
    {:name "inmonths"
     :type "function"
     :subtype "date and time"}
    {:name "inyears"
     :type "function"
     :subtype "date and time"}
    {:name "period"
     :type "function"
     :subtype "date and time"
     :syntax ["period(:value, :name)"]
     :description ["Creates a time period representing some number (:value) of a particular time unit (:name)."
                   "The following units are supported: milliseconds, seconds, minutes, hours, days, weeks, months, years. Both singular/plural forms, as well as some short forms (min, sec) are accepted."
                   "The bucket() function uses periods to group data."]
     :examples [{:q "set @fivemin = period(5, \"minutes\")"}
                {:q "input mock | mins = period(col1, \"minutes\")"}
                {:q "input mock | onehour = period(1, \"hours\")"}],
     :related ["function:bucket"]}
    {:name "earliest"
     :type "function"
     :subtype "date and time"
     :syntax ["earliest(:datetime, :datetime)"]
     :description ["Given two dates, returns the date which occurs first chronologically."]
     :examples [{:q "set @firstOccurrence = earliest(todate(\"2015-04-20\"), todate(\"2016-06-01\"))"}
                {:q "set @earliest = earliest(1462009645000, 1461999645000)"}]
     :related ["function:latest"]}
    {:name "latest"
     :type "function"
     :subtype "date and time"
     :syntax ["latest(:datetime, :datetime)"]
     :description ["Given two dates, returns the date which occurs last chronologically."]
     :examples [{:q "set @lastOccurrence = latest(todate(\"2015-04-20\"), todate(\"2016-06-01\"))"}
                {:q "set @latest = latest(1462009645000, 1461999645000)"}]
     :related ["function:earliest"]}
    {:name "isbetween"
     :type "function"
     :subtype "date and time"
     :syntax ["isbetween(:datetime, :start, :end)"]
     :returns "boolean"
     :description ["Returns true if :datetime occurs at or after :start and before or at :end. In other words, the function returns true if :datetime is within the inclusive interval between :start and :end."]
     :examples [{:q "set @test = isbetween(now(), minusminutes(now(), 1), addminutes(now(), 1))"}]}
    {:name "istoday"
     :type "function"
     :subtype "date and time"
     :syntax ["istoday(:datetime)"]
     :returns "boolean"
     :description ["Returns true if :datetime occurs at or after the start of the current day, and before the end."
                   "The result is relative to the timezone of :datetime. Use tolocaltime() or totimezone() to adjust if needed."]
     :examples [{:q "set @test = istoday(now())"}]
     :related ["function:istomorrow", "function:isyesterday", "function:isthisweek", "function:islastweek", "function:isnextweek", "function:isthismonth", "function:islastmonth", "function:isnextmonth"]}
    {:name "istomorrow"
     :type "function"
     :subtype "date and time"
     :syntax ["istomorrow(:datetime)"]
     :returns "boolean"
     :description ["Returns true if :datetime occurs at or after the start of tomorrow, and before the end."
                   "The result is relative to the timezone of :datetime. Use tolocaltime() or totimezone() to adjust if needed."]
     :examples [{:q "set @test = istomorrow(now())"}]
     :related ["function:istoday", "function:isyesterday", "function:isthisweek", "function:islastweek", "function:isnextweek", "function:isthismonth", "function:islastmonth", "function:isnextmonth"]}
    {:name "isyesterday"
     :type "function"
     :subtype "date and time"
     :syntax ["isyesterday(:datetime)"]
     :returns "boolean"
     :description ["Returns true if :datetime occurs at or after the start of yesterday, and before the end."
                   "The result is relative to the timezone of :datetime. Use tolocaltime() or totimezone() to adjust if needed."]
     :examples [{:q "set @test = isyesterday(now())"}]
     :related ["function:istoday", "function:istomorrow", "function:isthisweek", "function:islastweek", "function:isnextweek", "function:isthismonth", "function:islastmonth", "function:isnextmonth"]}
    {:name "isthisweek"
     :type "function"
     :subtype "date and time"
     :syntax ["isthisweek(:datetime)"]
     :returns "boolean"
     :description ["Returns true if :datetime occurs at or after the start of the current week, and before the end."
                   "The result is relative to the timezone of :datetime. Use tolocaltime() or totimezone() to adjust if needed."]
     :examples [{:q "set @test = isthisweek(now())"}]
     :related ["function:istoday", "function:istomorrow", "function:isyesterday", "function:islastweek", "function:isnextweek", "function:isthismonth", "function:islastmonth", "function:isnextmonth"]}
    {:name "isnextweek"
     :type "function"
     :subtype "date and time"
     :syntax ["isnextweek(:datetime)"]
     :returns "boolean"
     :description ["Returns true if :datetime occurs at or after the start of the week after the current week, and before the end."
                   "The result is relative to the timezone of :datetime. Use tolocaltime() or totimezone() to adjust if needed."]
     :examples [{:q "set @test = isnextweek(now())"}]
     :related ["function:istoday", "function:istomorrow", "function:isyesterday", "function:isthisweek", "function:islastweek", "function:isthismonth", "function:islastmonth", "function:isnextmonth"]}
    {:name "islastweek"
     :type "function"
     :subtype "date and time"
     :syntax ["islastweek(:datetime)"]
     :returns "boolean"
     :description ["Returns true if :datetime occurs at or after the start of the week prior to the current week, and before the end."
                   "The result is relative to the timezone of :datetime. Use tolocaltime() or totimezone() to adjust if needed."]
     :examples [{:q "set @test = islastweek(now())"}]
     :related ["function:istoday", "function:istomorrow", "function:isyesterday", "function:isthisweek", "function:isnextweek", "function:isthismonth", "function:islastmonth", "function:isnextmonth"]}
    {:name "isthismonth"
     :type "function"
     :subtype "date and time"
     :syntax ["isthismonth(:datetime)"]
     :returns "boolean"
     :description ["Returns true if :datetime occurs at or after the start of the current month, and before the end."
                   "The result is relative to the timezone of :datetime. Use tolocaltime() or totimezone() to adjust if needed."]
     :examples [{:q "set @test = isthismonth(now())"}]
     :related ["function:istoday", "function:istomorrow", "function:isyesterday", "function:isthisweek", "function:islastweek", "function:isnextweek", "function:islastmonth", "function:isnextmonth"]}
    {:name "isnextmonth"
     :type "function"
     :subtype "date and time"
     :syntax ["isnextmonth(:datetime)"]
     :returns "boolean"
     :description ["Returns true if :datetime occurs at or after the start of the month after the current month, and before the end."
                   "The result is relative to the timezone of :datetime. Use tolocaltime() or totimezone() to adjust if needed."]
     :examples [{:q "set @test = isnextmonth(now())"}]
     :related ["function:istoday", "function:istomorrow", "function:isyesterday", "function:isthisweek", "function:islastweek", "function:isnextweek", "function:isthismonth", "function:islastmonth"]}
    {:name "islastmonth"
     :type "function"
     :subtype "date and time"
     :syntax ["islastmonth(:datetime)"]
     :returns "boolean"
     :description ["Returns true if :datetime occurs at or after the start of the month prior to the current month, and before the end."
                   "The result is relative to the timezone of :datetime. Use tolocaltime() or totimezone() to adjust if needed."]
     :examples [{:q "set @test = islastmonth(now())"}]
     :related ["function:istoday", "function:istomorrow", "function:isyesterday", "function:isthisweek", "function:islastweek", "function:isnextweek", "function:isthismonth", "function:isnextmonth"]}
    {:name "bucket"
     :type "function"
     :syntax ["bucket(:expr, :buckets)"]
     :description ["Assigns buckets to :expr based on the 2nd argument, and returns the bucket for each value. Buckets can be numerical or time-based.  The start of the bucket will be returned."
                   "Numerical buckets are determined by dividing the set of real numbers into equivalent ranges of a given width.  Numbers are then located in the buckets according to their value.  For example, bucketing by 5 will yield buckets starting at 0, 5, 10, 15, etc.  Numerical buckets can be fractions or decimals."
                   "Time-based buckets are determined by dividing the time line into equivalent ranges of a given interval.  The :buckets argument must be given as a Period, created by the period() function.  For example, bucketing by a period of 5 minutes will yield buckets starting at 12:00, 12:05, 12:10, etc.  Buckets are always aligned on the hour."]
     :examples [{:q "input mock | buckets = bucket(col1, 2)"}
                {:description "Bucket by every 4th number and calculate an average per bucket"
                 :q "input mock name=\"chick-weight\"\n| timebucket = bucket(Time, 4)\n| stats avgWeight = avg(weight) by timebucket\n| sort timebucket"}
                {:description "Bucket by 5 minute periods"
                 :q "input mock n=20\n| t = minusMinutes(now(), col1)\n| tbuckets = bucket(t, period(5, \"minutes\"))"}],
     :related ["function:period"]}
    {:name "apply"
     :type "function"
     :subtype "functional"
     :syntax ["apply(:function, :args, ...)"]
     :description ["Invokes a first-order :function with any number of arguments. Commonly used to execute functions stored in variables."
                   "Using the def statement will avoid the need to call apply(), as the function will be mapped to a user-defined function name."
                   "Throws an exception if the first argument is not a function."]
     :examples [{:q "set @cube = (n) -> n*n*n, @cube9 = apply(@cube, 9)"}
                {:q "input mock name=\"thurstone\"\n| set @differenceFromAvg = (n, avg) -> abs(n - avg)\n| set @avgX = avg(x), @avgY = avg(y)\n| diffX = apply(@differenceFromAvg, x, @avgX)\n| diffY = apply(@differenceFromAvg, y, @avgY)"}]
     :related ["symbol:->:function"]}
    {:name "map"
     :type "function"
     :subtype "functional"
     :syntax ["map(:function, :list)"]
     :returns "list"
     :description ["Applies a first-order :function to each item in :list, and returns a list of the return values."
                   "Throws an exception if the first argument is not a function."]
     :examples [{:q "set @x = map(n -> n / 100, [10, 20, 30, 40, 50])"}
                {:description "Using a function in a variable"
                 :q "set @percent = n -> n / 100,\n@x = map(@percent, [10, 20, 30, 40, 50])"}
                {:description "Generating a dataset using map() and range()"
                 :q "project map((x) -> {`x`: x, `square`: x^2, `cube`: x^3, `fourth-power`: x^4}, range(1,10))"}]
     :related ["symbol:->:function", "function:mapcat", "function:mapvalues", "function:filter"]}
    {:name "mapcat"
     :type "function"
     :subtype "functional"
     :syntax ["mapcat(:function, :list)"]
     :returns "list"
     :description ["Applies a first-order :function to each item in :list, and applies concat() over the return values."
                   "Typically :function should return a list, otherwise it works identically to map()."
                   "Throws an exception if the first argument is not a function."]
     :examples [{:q "set @x = mapcat(n -> [n, n^2], [10, 20, 30, 40, 50])"}
                {:description "Using a function in a variable"
                 :q "set @percent = n -> n / 100,\n@x = mapcat(@percent, [10, 20, 30, 40, 50])"}
                {:description "Generating a set of ranges using mapcat() and range()"
                 :q "project mapcat((x) -> range(x, x + 5), [0, 10, 20, 30])"}]
     :related ["symbol:->:function", "function:map"]}
    {:name "mapvalues"
     :type "function"
     :subtype "functional"
     :syntax ["mapvalues(:function, :map)"]
     :returns "map"
     :description ["Applies a first-order :function to the value of each key in :map, and returns a new map containing the keys and their return values."
                   "Throws an exception if the first argument is not a function."]
     :examples [{:description "Increment each value in the map"
                 :q "set @map = mapvalues(n -> n+1, { x: 1, y: 2, z: 3 })"}
                {:description "Using a function in a variable"
                 :q "set @inc = n -> n+1,\n@map = mapvalues(@inc, { x: 1, y: 2, z: 3 })"}]
     :related ["symbol:->:function", "function:map"]}
    {:name "filter"
     :type "function"
     :subtype "functional"
     :syntax ["filter(:function, :list)"]
     :returns "list"
     :description ["Returns a list of items in :list for which the first-order :function returns true."
                   "Throws an exception if the first argument is not a function."]
     :examples [{:description "Filtering for even numbers"
                 :q "set @x = filter(n -> n mod 2 == 0, range(1,20))"}
                {:description "Using a function in a variable"
                 :q "set @isEven = n -> n mod 2 == 0,\n    @x = filter(@isEven, range(1,20))"}]
     :related ["symbol:->:function", "function:map"]}
    {:name "exec"
     :type "function"
     :subtype "execution"
     :syntax ["exec(:query)"
              "exec(:query, { dataset: \":dataset-name\" })"
              "exec(:query, { variable: \":variable-name\" })"
              "exec(:query, { context: true })"]
     :returns "any"
     :description ["Executes a Parsec query contained in :query, and returns the resulting context object. Any parsing, execution, or type errors will return a context with errors, rather than throwing an exception."
                   "A map of :options can be provided to return a specific part of the result, rather than the entire context. Either a single dataset or a single variable can be named in the :options, causing that dataset or variable value to be returned. If the dataset or variable is not found, null is returned.  Caution: if an error occurs, the dataset or variable may not exist (depending on when the error occurs), so null may also be returned."
                   "By default, uses a new isolated query context. However if the context option is set to true, the current parent context will be used to execute :query, allowing access to variables and datasets. Note this is read-only access; the parent context cannot be modified from within exec()."
                   "Returns null if :query is null."]
     :examples [{:q "set @x = exec(\"input mock\")"}
                {:description "Using options to select only the default dataset"
                 :q "set @dataset = exec(\"input mock\", { dataset: \"0\" })"}
                {:description "Using options to select a specific named dataset"
                 :q "set @dataset = exec(\"input mock | output name=\"mock\"; input mock name=\"chick-weight\"\", { dataset: \"mock\" })"}
                {:description "Using options to return only the value of a specific variable"
                 :q "set @avg = exec(\"input mock | set @avg=avg(col1+col2)\", { variable: \"@avg\" })"}
                {:description "Looking at the performance of a query ran with exec()"
                 :q "set @performance = get(exec(\"input mock\"), \"performance\")"}
                {:description "Using a shared context to access variables"
                 :q "set @price = 49.95,\n    @tax = 9.2%,\n    @total = exec(\"set @total = round(@price * (1 + @tax), 2)\", { variable: \"@total\", context: true })"}]}))