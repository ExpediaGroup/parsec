(defproject parsec "1.0.0-SNAPSHOT"
  :description "Parsec: data processing engine"
  :url "https://github.com/expediagroup/parsec"
  :license {:name "Apache License"
            :url  "http://www.apache.org/licenses/LICENSE-2.0"}

  :dependencies [[org.clojure/clojure "1.10.0"]

                 ;; Core Dependencies
                 [org.clojure/core.incubator "0.1.4"]
                 [yogthos/config "1.1.1"]
                 [com.taoensso/timbre "4.10.0"]
                 [criterium "0.4.4"]
                 [org.clojars.frozenlock/commons-lang "3.3.0"]
                 [commons-codec/commons-codec "1.12"]
                 [instaparse "1.4.10"]
                 [metrics-clojure "2.10.0"]
                 [metrics-clojure-graphite "2.10.0"]
                 [org.apache.commons/commons-text "1.6"]

                 ;; Web Service Dependencies
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.4.0"]
                 [ring-cors "0.1.13"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [cheshire "5.8.1"]
                 [metrics-clojure-ring "2.6.1"]

                 ;; JDBC Dependencies
                 [org.clojure/java.jdbc "0.7.9"]
                 [net.sourceforge.jtds/jtds "1.3.1"]
                 [mysql/mysql-connector-java "5.1.38"]
                 [org.clojars.zentrope/ojdbc "11.2.0.3.0"]
                 [org.postgresql/postgresql "9.4-1200-jdbc41"]
                 [org.hsqldb/hsqldb "2.4.1"]
                 [com.amazon.redshift/redshift-jdbc42-no-awssdk "1.2.20.1043"]

                 ;; Data Dependencies
                 [http.async.client "1.3.0"]
                 [org.clojure/data.json "0.2.6"]
                 [json-path "0.3.0"]
                 [clojure-csv/clojure-csv "2.0.2"]
                 [semantic-csv "0.2.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [com.github.kyleburton/clj-xpath "1.4.11"]
                 [org.samba.jcifs/jcifs "1.2.19"]
                 ;; http://weavejester.github.io/clj-aws-s3/
                 [clj-aws-s3 "0.3.10"
                  :exclusions [joda-time org.apache.httpcomponents/httpclient]]
                 [monglorious "0.8.0"]
                 [com.google.cloud/google-cloud-bigquery "1.69.0"]

                 ;; Required after adding Qubole 4.1-3
                 ;; TODO: Remove and add to instructions for using Qubole
                 [org.apache.httpcomponents/httpclient "4.5.3"]

                 ;; Math Dependencies
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [incanter/incanter-core "1.9.3"]
                 [incanter/incanter-io "1.9.3"]
                 [org.clojure/algo.generic "0.1.2"]

                 ;; Misc Dependencies
                 [clojurewerkz/urly "1.0.0"]
                 [clj-time "0.15.1"]
                 [joda-time/joda-time "2.10.1"]
                 [pandect "0.6.1"]
                 [fipp "0.6.17"]

                 ;; Explicit Guava dependency to avoid pulling in older versions
                 [com.google.guava/guava "26.0-jre"]

                 ;; Optional JDBC Drivers
                 ;; Not bundled--must be externally vendored to use

                 ;;[com.microsoft/sqljdbc "4.0"]
                 ;;[com.ibm/db2-jdbc "9.7.1"]
                 ;;[com.teradata/terajdbc4 "14.10.00.01"]
                 ;;[com.teradata/tdgssconfig "14.10.00.01"]
                 ;;[com.teradata.presto/PrestoJDBC42 "1.0.14.1022"]
                 ;;[org.apache.hive/hive-jdbc "0.14.0"
                 ;; :exclusions [org.apache.httpcomponents/httpclient
                 ;;              org.apache.httpcomponents/httpcore
                 ;;              com.google.guava/guava]]
                 ;;[org.apache.hadoop/hadoop-common "2.6.0"
                 ;; :exclusions [org.apache.httpcomponents/httpclient
                 ;;              org.apache.httpcomponents/httpcore
                 ;;              com.google.guava/guava]]
                 ;;[com.qubole/jdbc "4.1-4"]
                 ]

  :exclusions [[junit]
               [com.google.code.findbugs/jsr305]]

  :plugins [[lein-ring "0.12.5"]
            [lein-cloverage "1.0.6"]
            [com.jakemccrary/lein-test-refresh "0.12.0"]]

  :repositories [["redshift" {:id  "redshift"
                              :url "https://s3.amazonaws.com/redshift-maven-repository/release"}]]

  :main ^:skip-aot parsec.service
  :target-path "target/%s"

  :profiles {:provided {:dependencies [;; Signed JAR; cannot be included in uberjar
                                       [org.bouncycastle/bcprov-jdk15on "1.54"]]}
             :uberjar  {:aot :all}}

  :ring {:handler       parsec.service/app
         :port          8101
         :auto-reload?  true
         :auto-refresh? true})
