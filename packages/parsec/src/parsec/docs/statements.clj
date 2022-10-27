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

(ns parsec.docs.statements)

(def tokens
  '({:name "input"
     :type "statement"
     :description ["The input statement loads a dataset from various possible sources. Different input sources are available, like mock, jdbc, and http. Each source has various options for configuring it. If a dataset is already in the pipeline, it will be replaced."]
     :examples [{:description "Loading mock data from the built-in mock input"
                 :q "input mock"}
                {:description "Several named datasets are included in the mock input"
                 :q "input mock name=\"chick-weight\""}]
     :related ["input:mock", "input:datastore", "input:docs", "input:graphite", "input:http",  "input:influxdb", "input:jdbc", "input:mongodb", "input:smb", "input:s3"]}
    {:name "output"
     :type "statement"
     :syntax ["output name=\":name\" :options"
              "output ignore"]
     :description ["Outputs the current dataset. This is useful when executing multiple queries at once, in order to return multiple named datasets in the result. Any query which does not end in either an output or temp statement will have an implicit output added.  To prevent this, use \"output ignore\"."
                   "If the option \"temporary=true\" is added, the dataset will be stored in the datastore but not returned with the final query results. This is useful for intermediate datasets."
                   "If no name is provided, an automatic name will be generated using the index of the dataset in the data store."
                   "By default, the dataset is output to the datastore, in the execution context.  There is a \"type\" option to change the default destination."
                   "Available output types: \"datastore\" (default), \"ignore\"."]
     :examples [{:description "Returning two named datasets"
                 :q "input mock | output name=\"ds1\";\ninput mock | output name=\"ds2\""}
                {:description "Creating a temporary dataset"
                 :q "input mock | output name=\"ds1\" temporary=true;\ninput mock | output name=\"ds2\""}
                {:description "Ignoring the current dataset"
                 :q "input mock | output ignore"}
                {:description "Explicit options"
                 :q "input mock | output name=\"results\" temporary=false type=\"datastore\" auto=false"}]
     :related ["statement:temp"]}
    {:name "temp"
     :type "statement"
     :syntax ["temp :name"]
     :description ["The temp statement stores the current dataset in the datastore as a temporary dataset. This means the dataset will be accessible to the rest of the queries, but will not be output as a result dataset."
                   "The temp statement is a shortcut for \"output name='name' temporary=true\"."]
     :examples [{:q "input mock | temp ds1; input datastore name=\"ds1\""}]
     :related ["statement:output"]}
    {:name "head"
     :type "statement"
     :syntax ["head"
              "head :number"]
     :description ["Filters the current dataset to the first row or first :number of rows."]
     :examples [{:description "Just the first row"
                 :q "input mock | head"}
                {:description "First twenty rows"
                 :q "input mock name=\"thurstone\" | head 20"}]
     :related ["statement:tail"]}
    {:name "tail"
     :type "statement"
     :syntax ["tail"
              "tail :number"]
     :description ["Filters the current dataset to the last row or last :number of rows."]
     :examples [{:description "Just the tail row"
                 :q "input mock | tail"}
                {:description "Last twenty rows"
                 :q "input mock name=\"thurstone\" | tail 20"}]
     :related ["statement:head"]}
    {:name "select"
     :type "statement"
     :syntax ["select :identifier [, :identifier, ...]"]
     :description ["Selects the specified columns in each row and removes all other columns."]
     :examples [{:description "Creating and selecting a new column"
                 :q "input mock | sum = col1 + col2 | select sum"}
                {:description "Selecting several columns"
                 :q "input mock name=\"survey\" | select age, income, male"}]
     :related ["statement:unselect"]}
    {:name "unselect"
     :type "statement"
     :syntax ["unselect :identifier [, :identifier, ...]"]
     :description ["Removes the specified columns in each row, leaving the remaining columns."]
     :examples [{:description "Unselecting a specific column"
                 :q "input mock | unselect col3"}
                {:description "Unselecting several columns"
                 :q "input mock name=\"survey\" | unselect n, intercept, outmig, inmig | head 20"}]
     :related ["statement:select"]}
    {:name "filter"
     :type "statement"
     :syntax ["filter :expression"]
     :description ["Filters rows in the current dataset by a predicate. Rows for which the predicate evaluates to true will be kept in the dataset."
                   "This is the inverse of the remove statement."]
     :examples [{:description "Simple predicate"
                 :q "input mock name=\"chick-weight\" | filter Chick == 1"}
                {:description "Predicate with multiple conditions"
                 :q "input mock name=\"chick-weight\" | filter Chick == 1 and Time == 0"}]
     :related ["statement:remove"]}
    {:name "remove"
     :type "statement"
     :syntax ["remove :expression"]
     :description ["Removes rows in the current dataset by a predicate. Rows for which the predicate evaluates to true will be removed from the dataset."
                   "This is the inverse of the filter statement."]
     :examples [{:description "Simple predicate"
                 :q "input mock name=\"chick-weight\" | remove Chick == 1"}
                {:description "Predicate with multiple conditions"
                 :q "input mock name=\"chick-weight\" | remove Chick > 1 or Time > 0"}]
     :related ["statement:filter"]}
    {:name "reverse"
     :type "statement"
     :syntax ["reverse"]
     :description ["Reverses the order of the current dataset. That is, sorts the rows by the current index, descending."]
     :examples [{:q "input mock name=\"iran-election\" | reverse"}]}
    {:name "union"
     :type "statement"
     :syntax ["union :query"
              "union (all|distinct) :query"
              "union (all|distinct) (:query)"]
     :description ["Combines the rows of the current dataset with those from another sub-query.  If the sub-query is longer than a single input statement, it should be wrapped in parentheses to differentiate from the parent query."
                   "The argument ALL or DISTINCT determines which rows are included in the dataset.  DISTINCT returns only unique rows, whereas ALL includes all rows from both datasets, even if they are identical.  The default behavior is DISTINCT."
                   "Rows from the first dataset will preceed those from the sub-query."]
     :examples [{:description "Performs a distinct union by default"
                 :q "input mock | union input mock"}
                {:description "Optionally unions all rows"
                 :q "input mock | union all input mock"}
                {:description "Wrap multi-statement sub-queries in parentheses"
                 :q "input mock | union (input mock | col1 = col2 + col3)"}]}
    {:name "distinct"
     :type "statement"
     :syntax ["distinct"]
     :description ["Filters the current dataset to the set of unique rows.  A row is unique if there is no other row in the dataset with the same columns and values."
                   "Rows in the resulting dataset will remain in the same order as in the original dataset."]
     :examples [{:q "input mock | select col3 | distinct"}
                {:q "input mock | union all input mock | distinct"}]}
    {:name "rownumber"
     :type "statement"
     :syntax ["rownumber"
              "rownumber :identifier"]
     :description ["Assigns the current row number in the dataset to a column. If a column identifier is not provided, the default value of " _index " will be used."
                   "The rank() function implements similar functionality as a function, but has greater overhead and is only preferable when being used in an expression."]
     :examples [{:q "input mock | rownumber"}
                {:q "input mock | rownumber rowNumber"}
                {:q "input mock name=\"chick-weight\" | rownumber rank1 | rank2 = rank() | equal = rank1 == rank2 | stats count = count(*) by equal"}]
     :related ["function:rank"]}
    {:name "sort"
     :type "statement"
     :syntax ["sort :identifier"
              "sort :identifier (asc|desc)"
              "sort :identifier (asc|desc) [, :identifier (asc|desc), ...]"]
     :description ["Sorts the current dataset by one or more columns.  Each column can be sorted either in ascending (default) or descending order"]
     :examples [{:q "input mock | sort col1"}
                {:q "input mock | sort col1 asc"}
                {:q "input mock | sort col1 asc, col2 desc"}]}
    {:name "def"
     :type "statement"
     :syntax ["def :function-name = (:arg, :arg, ..) -> :expression, ..."]
     :description ["Defines one or more user-defined functions and saves into the current query context. Functions can take any number of arguments and return a single value."
                   "Def can be used at any point in the query, even before input. user-defined functions must be defined before they are invoked, but can be used recursively or by other functions."
                   "It is not possible to override built-in Parsec functions, but user-defined functions can be redefined multiple times."]
     :examples [{:description "Calculate the distance between two points"
                 :q "def distance = (p1, p2) -> \n    sqrt((index(p2, 0) - index(p1, 0))^2 + (index(p2, 1) - index(p1, 1))^2)\n| set @d = distance([0,0],[1,1])"}
                {:description "Calculate the range of a list"
                 :q "def range2 = (list) -> lmax(list) - lmin(list)\n| set @range = range2([8, 11, 5, 14, 25])"}
                {:description "Calculate the factorial of a number using recursion"
                 :q "def factorial = (n) -> if(n == 1, 1, n*factorial(n-1))\n| set @f9 = factorial(9)"}
                {:description "Calculate the number of permutations of n things, taken r at a time"
                 :q "def factorial = (n) -> if(n == 1, 1, n*factorial(n-1)), permutations = (n, r) -> factorial(n) / factorial(n - r)\n| set @p = permutations(5, 2)"}]
     :related ["symbol:->:function"]}
    {:name "set"
     :type "statement"
     :syntax ["set @var1 = :expression, [@var2 = :expression, ...]"
              "set @var1 = :expression, [@var2 = :expression, ...] where :predicate"]
     :description ["Sets one or more variables in the context. As variables are not row-based, the expression used cannot reference individual rows in the data set. However, it can use aggregate functions over the data set. An optional predicate argument can be used to filter rows before the aggregation."
                   "Set can be used at any point in the query, even before input"
                   "Variables are assigned from left to right, so later variables can reference previously assigned variables."
                   "Existing variables will be overridden."]
     :examples [{:description "Set a constant variable"
                 :q "set @plank = 6.626070040e-34"}
                {:description "Set several dependent variables"
                 :q "set @inches = 66, @centimeters_per_inch = 2.54, @centimeters = @inches * @centimeters_per_inch"}
                {:description "Calculate an average and use later in the query"
                 :q "input mock | set @avg = avg(col1) | filter col1 > @avg"}]
     :related ["literal:\"variable\""]}
    {:name "rename"
     :type "statement"
     :syntax ["rename :column=:oldcolumn, ..."
              "rename :column=:oldcolumn, ... where :predicate"]
     :description ["Renames one or more columns in the dataset."
                   "If a predicate is specified, the rename will apply only to rows satisfying the predicate."]
     :examples [{:q "input mock | rename day = col1, sales = col2, bookings = col3, numberOfNights = col4"}
                {:description "With predicate"
                 :q "input mock | rename col5 = col1 where col1 < 2"}]}
    {:name "assignment"
     :type "statement"
     :syntax [":column=:expr, ..."
              ":column=:expr, ... where :predicate"]
     :description ["Assigns one or more columns to each row in the dataset, by evaluating an expression for each row. Assignments are made from left to right, so it is possible to reference previously assigned columns in the same assignemnt statement."
                   "If a predicate is specified, the assignments will apply only to rows satisfying the predicate."]
     :examples [{:q "input mock | ratio = col1 / col2"}
                {:description "With predicate"
                 :q "input mock | col1 = col1 + col2 where col1 < 3"}
                {:description "With multiple assignments"
                 :q "input mock | ratio = col1 / col2, col5 = col3 * ratio"}]}
    {:name "stats"
     :type "statement"
     :syntax ["stats :expression, ... "
              "stats :expression, ... by :expression, ..."]
     :description ["Calculates one or more aggregate expressions across the dataset, optionally grouped by one or more expressions.  The resulting dataset will have a single row for each unique value(s) of the grouping expressions."
                   "Grouping expressions will be evaluated for each row, and the values used to divide the datasets into subsets.  Each grouping expression will become a column in the resulting dataset, containing the values for that subset.  If no grouping expressions are provided, the aggregations will run across the entire dataset."]
     :examples [{:q "input mock | stats count=count(*) by col2"}
                {:q "input mock name=\"chick-weight\" | stats avgWeight = avg(weight) by Chick"}
                {:description "Expressions can be used to group by"
                 :q "input mock name=\"chick-weight\" | stats avgWeight = avg(weight) by Chick, bucket(Time, 4)"}
                {:q "input mock name=\"chick-weight\" | stats avgWeight = avg(weight) by Chick, Time = bucket(Time, 4)"}]}
    {:name "sleep"
     :type "statement"
     :syntax ["sleep"
              "sleep :expression"]
     :description ["Delays execution of the query for a given number of milliseconds.  Defaults to 1000 milliseconds if no argument is given."]
     :examples [{:q "input mock | sleep 5000"}]}
    {:name "sample"
     :type "statement"
     :syntax ["sample :expr"]
     :description ["Returns a sample of a given size from the current dataset.  The sample size can be either smaller or larger than the current dataset."]
     :examples [{:description "Selecting a sample"
                 :q "input mock n=100 | sample 10"}
                {:description "Generating a larger sample from a smaller dataset"
                 :q "input mock n=10 | sample 100"}]}
    {:name "benchmark"
     :type "statement"
     :syntax ["benchmark :expr [:options]"
              "bench :expr [:options]"]
     :description ["Benchmarks a query and returns a single row of the results.  The query will be run multiple times and includes a warm-up period for the JIT compiler and forced GC before and after testing."
                   "Various options are available to customize the benchmark, but defaults will be used if not provided.  The values used will be returned as a map in the \"options\" column."]
     :examples [{:description "Benchmarking a query with default options"
                 :q "benchmark \"input mock\""}
                {:description "Providing a sample size"
                 :q "bench \"input mock | head 1\" samples=20"}
                {:description "Projecting the performance results for each sample"
                 :q "benchmark \"input mock\" | project first(performance)"}
                {:description "Projecting the text summary of the benchmark"
                 :q "benchmark \"set @x = random()\" | project split(first(summary), \"\\n\")"}]}
    {:name "pivot"
     :type "statement"
     :description ["Increases the number of columns in the current dataset by \"pivoting\" values of one or more columns into additional columns. For example, unique values in a column \"type\" can be converted into separate columns, with the values attributable to each type as their contents."]
     :syntax ["pivot :value [, :value ...] per :expression [, :expression ...] by :group-by [, :group-by ...]"
              "pivot :identifier=:value [, identifier=:value ...] per :expression [, :expression ...] by :group-by [, :group-by ...]"]
     :related ["statement:unpivot"]}
    {:name "unpivot"
     :type "statement"
     :syntax ["unpivot :value per :column by :group-by [, :group-by ...]"]
     :description ["Reduces the number of columns in the current dataset by \"unpivoting\" them into rows of a single column.  This is the inverse operation of the pivot statement."]
     :examples [{:q "input http uri=\"http://pastebin.com/raw.php?i=YGN7gWWG\" parser=\"json\" | unpivot CrimesCommitted per Type by Year, Population"}]
     :related ["statement:pivot"]}
    {:name "project"
     :type "statement"
     :syntax ["project :expression"]
     :description ["Replaces the current dataset with the result of an expression. The expression must be either a list or a single map which will be wrapped in a list automatically."
                   "Projecting a list of maps replaces the entire dataset verbatim. If the list does not contain maps, each value will be stored in a new column called \"value\"."
                   "Can be useful when parsing data from a string."]
     :examples [{:description "With a list of maps"
                 :q "input mock | project [{x:1, y:1},{x:2, y:2}]"}
                {:description "With a list of numbers"
                 :q "input mock | project [4, 8, 15, 16, 23, 42] | stats avg = avg(value)"}
                {:description "From a variable"
                 :q "input mock | x = [{x:1, y:1},{x:2, y:2}] | project first(x)"}
                {:description "With a single map"
                 :q "input mock | project {a: 1, b: 2, c: 3}"}
                {:description "With a JSON string"
                 :q "input mock | x = '[{\"x\":1, \"y\":1},{\"x\":2, \"y\":2}]' | project parsejson(first(x))"}
                {:description "With a CSV string"
                 :q "input http uri=\"http://pastebin.com/raw.php?i=s6xmiNzx\" | project parsecsv(first(body), { strict: true, delimiter: \",\" })"}]}
    {:name "join"
     :type "statement"
     :syntax ["[inner] join [:left-alias], :dataset-or-query on :expression [, :expression, ...]"
              "left [outer] join [:left-alias], :dataset-or-query on :expression [, :expression, ...]"
              "right [outer] join [:left-alias], :dataset-or-query on :expression [, :expression, ...]"
              "full [outer] join [:left-alias], :dataset-or-query on :expression [, :expression, ...]"
              "cross join :dataset-or-query"]
     :description ["Joins the current dataset with another dataset. The join statement takes the current dataset as the left dataset, and either accepts the name of a dataset in the context or an inline query in parentheses."
                   "Join types available: Inner, Left Outer (Left), Right Outer (Right), Full Outer (Full), and Cross Join.  If no join type is specified, inner join will be assumed."
                   "The joined dataset is the output of this statement.  Columns from joined rows will be merged, with preference given to the value in the Left dataset."]
     :examples [{:description "Inner join"
                 :q "project [{ id: 1, type: \"a\" }, { id: 2, type: \"b\" }] | temp types; project [{ name: \"x\", typeId: 1 }, { name: \"y\", typeId: 2 }] | inner join types on typeId == types.id"}
                {:description "Left outer join"
                 :q "project [{ id: 1, type: \"a\" }, { id: 2, type: \"b\" }] | temp types; project [{ name: \"x\", typeId: 1 }, { name: \"y\", typeId: 2 }, { name: \"z\", typeId: 3 }] | left outer join types on typeId == types.id"}
                {:description "Right outer join"
                 :q "project [{ id: 1, type: \"a\" }, { id: 2, type: \"b\" }, { id: 3, type: \"c\" }] | temp types; project [{ name: \"x\", typeId: 1 }, { name: \"y\", typeId: 2 }, { name: \"z\", typeId: 1 }] | right outer join types on typeId == types.id"}
                {:description "Full outer join"
                 :q "project [{ id: 1, type: \"a\" }, { id: 2, type: \"b\" }, { id: 3, type: \"c\" }] | temp types; project [{ name: \"x\", typeId: 1 }, { name: \"y\", typeId: 2 }, { name: \"z\", typeId: 4 }] | full outer join types on typeId == types.id"}
                {:description "Cross join"
                 :q "project [{ id: 1, type: \"a\" }, { id: 2, type: \"b\" }] | temp types; project [{ name: \"x\" }, { name: \"y\" }] | cross join types"}]
     :related ["statement:inner join" "statement:left outer join" "statement:right outer join" "statement:full outer join" "statement:cross join"]}
    {:name "inner join"
     :type "statement"
     :syntax ["[inner] join [:left-alias], :dataset-or-query on :expression [, :expression, ...]"]
     :description ["Inner join is a type of join which requires each row in the joined datasets to satisfy a join predicate."]
     :examples [{:description "Inner join"
                 :q "project [{ id: 1, type: \"a\" }, { id: 2, type: \"b\" }] | temp types; project [{ name: \"x\", typeId: 1 }, { name: \"y\", typeId: 2 }] | inner join types on typeId == types.id"}]
     :related ["statement:join" "statement:left outer join" "statement:right outer join" "statement:full outer join" "statement:cross join"]}
    {:name "left outer join"
     :type "statement"
     :syntax ["left [outer] join [:left-alias], :dataset-or-query on :expression [, :expression, ...]"]
     :description ["Left Outer join is a type of join which always includes all rows of the left-hand dataset, even if they do not satisfy the join predicate."]
     :examples [{:description "Left outer join"
                 :q "project [{ id: 1, type: \"a\" }, { id: 2, type: \"b\" }] | temp types; project [{ name: \"x\", typeId: 1 }, { name: \"y\", typeId: 2 }, { name: \"z\", typeId: 3 }] | left outer join types on typeId == types.id"}]
     :related ["statement:join" "statement:inner join" "statement:right outer join" "statement:full outer join" "statement:cross join"]}
    {:name "right outer join"
     :type "statement"
     :syntax ["right [outer] join [:left-alias], :dataset-or-query on :expression [, :expression, ...]"]
     :description ["Right Outer join is a type of join which always includes all rows of the right-hand dataset, even if they do not satisfy the join predicate."]
     :examples [{:description "Right outer join"
                 :q "project [{ id: 1, type: \"a\" }, { id: 2, type: \"b\" }, { id: 3, type: \"c\" }] | temp types; project [{ name: \"x\", typeId: 1 }, { name: \"y\", typeId: 2 }, { name: \"z\", typeId: 1 }] | right outer join types on typeId == types.id"}]
     :related ["statement:join" "statement:inner join" "statement:left outer join" "statement:full outer join" "statement:cross join"]}
    {:name "full outer join"
     :type "statement"
     :syntax ["full [outer] join [:left-alias], :dataset-or-query on :expression [, :expression, ...]"]
     :description ["Full Outer join is a type of join which combines the effects of both left and right outer join.  All rows from both left and right datasets will be included in the result."]
     :examples [{:description "Full outer join"
                 :q "project [{ id: 1, type: \"a\" }, { id: 2, type: \"b\" }, { id: 3, type: \"c\" }] | temp types; project [{ name: \"x\", typeId: 1 }, { name: \"y\", typeId: 2 }, { name: \"z\", typeId: 4 }] | full outer join types on typeId == types.id"}]
     :related ["statement:join" "statement:inner join" "statement:left outer join" "statement:right outer join" "statement:cross join"]}
    {:name "cross join"
     :type "statement"
     :syntax ["cross join [:left-alias], :dataset-or-query"]
     :description ["Cross join is a type of join which returns the cartesian product of rows from both datasets."]
     :examples [{:description "Cross join"
                 :q "project [{ id: 1, type: \"a\" }, { id: 2, type: \"b\" }] | temp types; project [{ name: \"x\" }, { name: \"y\" }] | cross join types"}]
     :related ["statement:join" "statement:inner join" "statement:left outer join" "statement:right outer join" "statement:full outer join"]}))