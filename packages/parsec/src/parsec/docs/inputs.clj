;; Copyright 2022 Expedia, Inc.
;;
;; Licensed under the Apache License, Version 2.0 (the \"License\");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;     https://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an \"AS IS\" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

(ns parsec.docs.inputs)

(def tokens
  '({:name "mock"
     :type "input"
     :syntax ["input mock"
              "input mock n=:number"
              "input mock name=:name"
              "input mock name=:name incanterHome=:path"]
     :description ["The mock input type is for testing and experimental purposes. By default, it returns a fixed dataset of 5 rows and 4 columns. If \"n\" is provided, the mock dataset will contain n-number of rows, and an additional column."
                   "There are also specific test datasets included, which can be loaded using the \"name\" option.  The available named datasets are: \"iris\", \"cars\", \"survey\", \"us-arrests\", \"flow-meter\", \"co2\", \"chick-weight\", \"plant-growth\", \"pontius\", \"filip\", \"longely\", \"chwirut\", \"thurstone\", \"austres\", \"hair-eye-color\", \"airline-passengers\", \"math-prog\", \"iran-election\"."
                   "By default, test datasets are loaded from GitHub. In order to load them from the local machine, download the Incanter source code (https://github.com/incanter/incanter), and provide the root directory as incanterHome."]
     :examples [{:description "Loading mock data"
                 :q "input mock"}
                {:description "Loading lots of mock data"
                 :q "input mock n=100"}
                {:description "Loading a named mock dataset"
                 :q "input mock name=\"chick-weight\""}
                {:description "Loading a named dataset from disk (requires additional download)"
                 :q "input mock name=\"chick-weight\" incanterHome=\"~/Downloads/incanter-master\""}]
     :related ["statement:input"]}
    {:name "datastore"
     :type "input"
     :syntax ["input datastore name=:name"]
     :description ["The datastore input type retrieves and loads datasets from the datastore in the current execution context. Datasets may have been written previously to the datastore using the temp or output statements."]
     :examples [{:description "Storing and loading a dataset"
                 :q "input mock | output name=\"ds1\"; input datastore name=\"ds1\""}
                {:description "Storing and loading a temporary dataset"
                 :q "input mock | temp ds2; input datastore name=\"ds2\""}]
     :related ["statement:input"]}
    {:name "jdbc"
     :type "input"
     :syntax ["input jdbc uri=\":uri\" query=\":query\""
              "input jdbc uri=\":uri\" [user | username]=\":username\" password=\":password\" query=\":query\""
              "input jdbc uri=\":uri\" [user | username]=\":username\" password=\":password\" query=\":query\" operation=\":operation\""]
     :description ["The jdbc input type connects to various databases using the JDBC api and executes a query. The results of the query will be returned as the current dataset."
                   "The default JDBC operation is \"query\", which is used for selecting data. The operation option can be used to provide another operation to run—currently, only \"execute\" is implemented.  INSERT/UPDATE/DELETE/EXECUTE queries can all be run through the \"execute\" operation."
                   "Caution: The implementation of the URI may vary across JDBC drivers, e.g. some require the username and password in the URI, whereas others as separate options. This is determined by the specific JDBC driver and its implementation, not Parsec. Please check the documentation for the JDBC driver you are using, or check the examples below. There may be multiple valid ways to configure some drivers."
                   "The following JDBC drivers are bundled with Parsec: AWS Redshift, DB2, Hive2, HSQLDB, MySQL, PostgreSQL, Presto, Qubole, SQL Server, SQL Server (jTDS), Teradata."]
     :examples [{:description "Querying MySQL"
                 :q "input jdbc uri=\"jdbc:mysql://my-mysql-server:3306/database?user=username&password=password\"\nquery=\"...\""}
                {:description "Inserting data into MySQL"
                 :q "input jdbc uri=\"jdbc:mysql://my-mysql-server:3306/database?user=username&password=password\" operation=\"execute\"\nquery=\"INSERT INTO ... VALUES (...)\""}
                {:description "Querying SQL Server"
                 :q "input jdbc uri=\"jdbc:sqlserver://my-sql-server:1433;databaseName=database;username=username;password=password\"\n  query=\"...\""}
                {:description "Querying Hive (Hiveserver2)"
                 :q "input jdbc uri=\"jdbc:hive2://my-hive-server:10000/default?mapred.job.queue.name=queuename\" username=\"username\" password=\"password\"\n  query=\"show tables\""}
                {:description "Querying Teradata"
                 :q "input jdbc uri=\"jdbc:teradata://my-teradata-server/database=database,user=user,password=password\"\n  query=\"...\""}
                {:description "Querying DB2"
                 :q "input jdbc uri=\"jdbc:db2://my-db2-server:50001/database:user=username;password=password;\"\n  query=\"...\""}
                {:description "Querying Oracle"
                 :q "input jdbc uri=\"jdbc:oracle:thin:username/password@//my-oracle-server:1566/database\"\n  query=\"...\""}
                {:description "Querying PostgreSQL"
                 :q "input jdbc uri=\"jdbc:postgresql://my-postgres-server/database?user=username&password=password\"\n  query=\"...\""}]
     :related ["statement:input"]}
    {:name "graphite"
     :type "input"
     :syntax ["input graphite uri=\":uri\" targets=\":target\""
              "input graphite uri=\":uri\" targets=[\":target\", \":target\"]"
              "input graphite uri=\":uri\" targets=[\":target\", \":target\"] from=\"-24h\" until=\"-10min\""]
     :description ["The graphite input type retrieves data from Graphite, a time-series database.  One or more target metrics can be retrieved; any valid Graphite targets can be used, including functions and wildcards."
                   "The :uri option should be set to the Graphite server UI or render API."]
     :examples [{:q "input graphite uri=\"http://my-graphite-server\" targets=\"carbon.agents.*.metricsReceived\" from=\"-4h\""}
                {:description "Unpivoting metrics into a row"
                 :q "input graphite uri=\"http://my-graphite-server\" targets=\"carbon.agents.*.*\" from=\"-2h\"\n| unpivot value per metric by _time\n| filter value != null"}]
     :related ["statement:input"]}
    {:name "http"
     :type "input"
     :syntax ["input http uri=\":uri\""
              "input http uri=\":uri\" user=\":user\" password=\"*****\""
              "input http uri=\":uri\" method=\":method\" body=\":body\""
              "input http uri=\":uri\" parser=\":parser\""
              "input http uri=\":uri\" parser=\":parser\" jsonpath=\":jsonpath\""]
     :description ["The http input type retrieves data from web servers using the HTTP/HTTPS networking protocol. It defaults to an HTTP GET without authentication, but can be changed to any HTTP method."
                   "Optional authentication is available using the user/password options. Preemptive authentication can also be enabled, which sends the authentication in the initial request instead of waiting for a 401 response. This may be required by some web services."
                   "Without the :parser option, a single row will be output, containing information about the request and response.   If a parser is specified, the body of the file will be parsed and projected as the new data set. The JSON parser has an option jsonpath option, allowing a subsection of the JSON document to be projected."
                   "Output columns are: \"body\", \"headers\", \"status\", \"msg\", \"protocol\", \"content-type\""
                   "Available options: user, password, method, body, parser, headers, query, timeout, connection-timeout, request-timeout, compression-enabled, and follow-redirects."]
     :examples [{:description "Loading a web page"
                 :q "input http uri=\"http://www.expedia.com\""}
                {:description "Authenticating with username and password"
                 :q "input http uri=\"http://my-web-server/path\"\n  user=\"readonly\" password=\"readonly\""}
                {:description "Preemptive authentication (sending credentials with initial request)"
                 :q "input http uri=\"http://my-web-server/path\"\n  auth={ user: \"readonly\", password: \"readonly\", preemptive: true }"}
                {:description "Posting data"
                 :q "input http uri=\"http://my-web-server/path\"\n  method=\"post\" body=\"{ \"key\": 123456 }\"\n  headers={ \"Content-Type\": \"application/json\" }"}
                {:description "Providing custom headers"
                 :q "input http uri=\"http://my-web-server/path\"\n  headers={ \"Accept\": \"application/json, text/plain, */*\",\n            \"Accept-Encoding\": \"en-US,en;q=0.5\" }"}
                {:description "Parsing an XML file"
                 :q "input http uri=\"http://news.ycombinator.com/rss\"\n| project parsexml(first(body), { xpath: \"/rss/channel/item\" })"}
                {:description "Parsing a JSON file with JsonPath"
                 :q "input http uri=\"http://pastebin.com/raw/wzmy7TMZ\" parser=\"json\" jsonpath=\"$.values[*]\""}]
     :related ["statement:input", "function:parsexml", "function:parsecsv", "function:parsejson", "function:jsonpath"]}
    {:name "influxdb"
     :type "input"
     :syntax ["input influxdb uri=\":uri\" db=\":db\" query=\":query\""
              "input influxdb uri=\":uri\" db=\":db\" query=\":query\" user=\":user\" password=\":password\""]
     :description ["The influxdb input type retrieves data from InfluxDB, a time-series database.  Supports InfluxDB 0.9 and higher."
                   "The :uri option should be set to the Query API endpoint on an InfluxDB server; by default is is on port 8086.  The URI of the Admin UI will not work."
                   "An error will be thrown if multiple queries are sent in one input statement."]
     :examples [{:q "input influxdb uri=\"http://my-influxdb-server:8086/query\"\n  db=\"NOAA_water_database\"\n  query=\"SELECT * FROM h2o_feet\""}]
     :related ["statement:input"]}
    {:name "mongodb"
     :type "input"
     :syntax ["input mongodb uri=\":uri\" query=\":query\""]
     :description ["The mongodb input type retrieves data from MongoDB, a NoSQL document database."
                   "The :uri option should follow the standard connection string format, documented here: https://docs.mongodb.com/manual/reference/connection-string/."
                   "The :query option accepts a limited subset of the Mongo Shell functionality."]
     :examples [{:q "input mongodb uri=\"mongodb://localhost:27017/testdb\" query=\"show collections\""}
                {:q "input mongodb uri=\"mongodb://localhost:27017/testdb\" query=\"db.testcollection.find({:type \"orders\"}.sort({:name 1})\""}]
     :related ["statement:input"]}
    {:name "s3"
     :type "input"
     :syntax ["input s3 accessKeyId=\":key-id\" secretAccessKey=\":secret\""
              "input s3 accessKeyId=\":key-id\" secretAccessKey=\":secret\" token=\":token\""
              "input s3 uri=\"s3://:uri\" accessKeyId=\":key-id\" secretAccessKey=\":secret\" token=\":token\" operation=\":operation\" maxKeys=:max-keys delimiter=:delimiter gzip=:gzip-enabled zip=:zip-enabled"
              "input s3 uri=\"s3://:uri\" accessKeyId=\":key-id\" secretAccessKey=\":secret\" token=\":token\" operation=\":operation\" maxKeys=:max-keys delimiter=:delimiter parser=\":parser\""]
     :description ["The s3 input type retrieves data from Amazon S3. It is designed to either retrieve information about objects stored in S3, or retrieve the contents of those objects."
                   "The following operations are supported: \"list-buckets\", \"list-objects\", \"list-objects-from\", \"get-objects\", \"get-objects-from\", \"get-object\". The operation can be specified manually, or auto-detected based on the arguments."
                   "Authentication requires AWS credentials of either accessKeyId/secretAccessKey, or accessKeyId/secretAccessKey/token."
                   "\"list-objects\" and \"get-objects\" have a default limit of 10 objects, which can be configured via the maxKeys option"
                   "\"list-objects-from\" and \"get-objects-from\" use a marker to retrieve objects only after the given prefix or object.  Partial object names are supported."
                   "Zip or gzip-compressed objects can be decompressed by setting gzip=true, or zip=true. If neither is set, the object is assumed to be uncompressed"]
     :examples [{:description "Listing all S3 buckets available for the given credentials"
                 :q "input s3 accessKeyId=\"***\" secretAccessKey=\"****\""}
                {:description "Listing all S3 objects in the given bucket"
                 :q "input s3 uri=\"s3://:bucket\" accessKeyId=\":key-id\" secretAccessKey=\":secret-key\""}
                {:description "Listing S3 objects in the given bucket with a given prefix (prefix must end in /)"
                 :q "input s3 uri=\"s3://:bucket/:prefix/\" accessKeyId=\":key-id\" secretAccessKey=\":secret-key\""}
                {:description "Getting the next level of the object hierarchy by using a delimiter."
                 :q "input s3 uri=\"s3://:bucket/:prefix/\" accessKeyId=\":key-id\" secretAccessKey=\":secret-key\" delimiter=\"/\""}
                {:description "Getting an S3 object"
                 :q "input s3 uri=\"s3://:bucket/:prefix/:object.json\"\naccessKeyId=\":key-id\" secretAccessKey=\":secret-key\""}
                {:description "Getting an S3 object and parsing its contents"
                 :q "input s3 uri=\"s3://:bucket/:prefix/:object.json\" parser=\"jsonlines\"\naccessKeyId=\":key-id\" secretAccessKey=\":secret-key\""}
                {:description "Getting multiple S3 objects and parsing their contents into a combined dataset"
                 :q "input s3 uri=\"s3://:bucket/:prefix/\" operation=\"get-objects\" parser=\"jsonlines\"\naccessKeyId=\":key-id\" secretAccessKey=\":secret-key\""}
                {:description "Limiting the number of objects returned"
                 :q "input s3 uri=\"s3://:bucket/:prefix/\" operation=\"list-objects\" maxKeys=100\naccessKeyId=\":key-id\" secretAccessKey=\":secret-key\""}
                {:description "Getting a single S3 object and parsing its contents"
                 :q "input s3 uri=\"s3://:bucket/:prefix/:object.json\" gzip=true parser=\"jsonlines\"\naccessKeyId=\":key-id\" secretAccessKey=\":secret-key\""}]
     :related ["statement:input"]}
    {:name "smb"
     :type "input"
     :syntax ["input smb uri=\"smb://:hostname/:path\""
              "input smb uri=\"smb://:hostname/:path\" user=\":user\" password=\"*****\""
              "input smb uri=\"smb://:user:******@:hostname/:path\""
              "input smb uri=\"smb://:hostname/:path\" parser=\":parser\""]
     :description ["The smb input type retrieves data using the SMB/CIFS networking protocol. It can either retrieve directory listings or file contents."
                   "Optional NTLM authentication is supported using the user/password options, or by embedding them in the :uri (slightly slower)."
                   "Without the :parser option, a single row will be output, containing metadata about the directory or file. If a parser is specified, the body of the file will be parsed and projected as the new data set (directories cannot be parsed)."
                   "Metadata columns are: \"name\", \"path\", \"isfile\", \"body\", \"attributes\", \"createdTime\", \"lastmodifiedTime\", \"length\", \"files\""]
     :examples [{:description "Retrieving a directory listing"
                 :q "input smb uri=\"smb://my-samba-server/my-share\"\n  user=\"readonly\" password=\"readonly\""}
                {:description "Retrieving file metadata"
                 :q "input smb uri=\"smb://my-samba-server/my-share/file.txt\"\n  user=\"readonly\" password=\"readonly\""}
                {:description "Parsing a JSON file"
                 :q "input smb uri=\"smb://my-samba-server/my-share/file.txt\"\n  user=\"readonly\" password=\"readonly\" parser=\"json\""}]
     :related ["statement:input"]}))