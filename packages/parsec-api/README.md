# Parsec API

This package contains the API service for Parsec. It provides a RESTful API for
submitting queries and retrieving results from the Parsec query engine.

## Development

Launch a development server using [ring-dev](https://github.com/mtkp/ring-dev):

```
clojure -M:server
```

Then open http://localhost:8101 in a browser.

## Uberjar

Build a standalone uberjar with all dependencies included:

```
clojure -T:build clean
clojure -T:build uber
```

The Parsec API Service can be launched from the uberjar like this:

```
java -cp "target/parsec-api-VERSION-standalone.jar" parsec.api.service
```
