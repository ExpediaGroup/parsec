# Config File
Parsec.Services.factory 'configService', ->
    configs =
        ui:
            maxRows: 1000
        parsec:
            serviceList: [
                {
                    name: 'SAME-DOMAIN'
                    url: '/api'
                }
                {
                    name: 'Localhost'
                    url: 'http://localhost:8101/api'
                }
            ]
            queryTemplates: [
                {
                    category: 'Mock'
                    name: 'Mock'
                    query: 'input mock'
                }
                {
                    category: 'Mock'
                    name: 'Chick Weight'
                    query: 'input mock name="chick-weight"'
                }
                {
                    category: 'JDBC'
                    name: 'MySQL'
                    query: 'input jdbc uri="jdbc:mysql://SERVERNAME:3306/DATABASE" user="USER" password="******"\r\n  query="SELECT 1"'
                }
                {
                    category: 'JDBC'
                    name: 'MySQL (Alternate)'
                    query: 'input jdbc uri="jdbc:mysql://SERVERNAME:3306/DATABASE?user=USER&password=******"\r\n  query="SELECT 1"'
                }
                {
                    category: 'JDBC'
                    name: 'MySQL with INSERT'
                    query: 'input jdbc uri="jdbc:mysql://SERVERNAME:3306/DATABASE" user="USER" password="******" operation="execute"\r\n  query="INSERT INTO tablename VALUES (...)"'
                }
                {
                    category: 'JDBC'
                    name: 'SQL Server'
                    query: 'input jdbc uri="jdbc:jtds:sqlserver://SERVERNAME:1433;databaseName=DATABASE;"\r\n  user="USER" password="******"\r\n  query="SELECT 1"'
                }
                {
                    category: 'JDBC'
                    name: 'SQL Server (alternate)'
                    query: 'input jdbc uri="jdbc:jtds:sqlserver://SERVERNAME:1433;databaseName=DATABASE;user=USER;password=******;"\r\n  query="SELECT 1"'
                }
                {
                    category: 'JDBC'
                    name: 'SQL Server with NTLMv2 Authentication'
                    query: 'input jdbc uri="jdbc:jtds:sqlserver://SERVERNAME:1433;databaseName=DATABASE;userNTLMv2=true;domain=DOMAIN;"\r\n  user="USER" password="******"\r\n  query="SELECT 1"'
                }
                {
                    category: 'JDBC'
                    name: 'Hive2'
                    query: 'input jdbc uri="jdbc:hive2://SERVERNAME:10001/default?mapred.job.queue.name=QUEUENAME" user="USER" password="******"\r\n  query="SHOW TABLES"'
                }
                {
                    category: 'JDBC'
                    name: 'Teradata'
                    query: 'input jdbc uri="jdbc:teradata://SERVERNAME/database=DATABASE"\r\n  user="USER" password="******"\r\n  query="SELECT 1"'
                }
                {
                    category: 'JDBC'
                    name: 'Teradata (alternate)'
                    query: 'input jdbc uri="jdbc:teradata://SERVERNAME/database=DATABASE,user=USER,password=******"\r\n  query="SELECT 1"'
                }
                {
                    category: 'JDBC'
                    name: 'DB2'
                    query: 'input jdbc uri="jdbc:db2://SERVERNAME:50001/DATABASE:user=USER;password=******;"\r\n  query="SELECT 1"'
                }
                {
                    category: 'JDBC'
                    name: 'Oracle'
                    query: 'input jdbc uri="jdbc:oracle:thin:USERNAME/PASSWORD@//SERVERNAME:1521/DATABASE"\r\n  query="SELECT 1"'
                }
                {
                    category: 'JDBC'
                    name: 'PostgreSQL'
                    query: 'input jdbc uri="jdbc:postgresql://SERVERNAME:5432/DATABASE?user=USER&password=******;"\r\n  query="SELECT 1"'
                }
                {
                    category: 'JDBC'
                    name: 'Presto (Hive)'
                    query: 'input jdbc uri="jdbc:presto://SERVERNAME:8080/hive"\r\n  user="USER" password="******"\r\n  query="SELECT 1"'
                }
                {
                    category: 'JDBC'
                    name: 'Amazon Redshift'
                    query: 'input jdbc uri="jdbc:redshift://SERVERNAME:5439/DATABASE"\r\n  query="SELECT 1"'
                }
                {
                    category: 'JDBC'
                    name: 'Qubole - Hive'
                    query: 'input jdbc uri="jdbc:qubole://hive/DATABASE" username="" password="API-KEY"\r\n  query="SHOW TABLES"'
                }
                {
                    category: 'HTTP'
                    name: 'Basic HTTP'
                    query: 'input http uri="URI"'
                }
                {
                    category: 'HTTP'
                    name: 'HTTP with JSON Parsing'
                    query: 'input http uri="URI" parser="json"'
                }
                {
                    category: 'HTTP'
                    name: 'HTTP with Authentication'
                    query: 'input http uri="URI" auth={ user: "USER", password: "******", preemptive: true }'
                }
                {
                    category: 'HTTP'
                    name: 'HTTP with POST Data'
                    query: 'input http uri="URI" method="post" headers={ "Content-Type": "application/json" }\r\n  body=\'{ "key": 123456 }\''
                }
                {
                    category: 'Graphite'
                    name: 'Query Data'
                    query: 'input graphite uri="http://SERVERNAME" targets="carbon.agents.*.metricsReceived" from="-1h"'
                }
                {
                    category: 'InfluxDB'
                    name: 'Show Measurements'
                    query: 'input influxdb uri="http://SERVERNAME:8086/query" db="DATABASE" query="SHOW MEASUREMENTS"'
                }
                {
                    category: 'InfluxDB'
                    name: 'Query Data'
                    query: 'input influxdb uri="http://SERVERNAME:8086/query" db="DATABASE" query="SELECT * FROM MEASUREMENT"'
                }
                {
                    category: 'S3'
                    name: 'List Objects'
                    query: 'input s3 uri="s3://BUCKET" accessKeyId="KEY-ID" secretAccessKey="SECRET-KEY"'
                }
                {
                    category: 'S3'
                    name: 'Get Object'
                    query: 'input s3 uri="s3://BUCKET/PREFIX/OBJECT" accessKeyId="KEY-ID" secretAccessKey="SECRET-KEY"'
                }
                {
                    category: 'SMB/CIFS'
                    name: 'Read File'
                    query: 'input smb uri="smb://SERVERNAME/DIRECTORY/FILENAME" user="USER" password="*******"'
                }
                {
                    category: 'SMB/CIFS'
                    name: 'Read Directory'
                    query: 'input smb uri="smb://SERVERNAME/DIRECTORY/ user="USER" password="*******"'
                }
            ]
        crunch:
            serviceList: [
            ]

    configs.selectedParsecService = configs.parsec.serviceList[0]

    return configs
