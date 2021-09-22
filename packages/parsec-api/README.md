

## Development

Launch a development server using [ring-dev](https://github.com/mtkp/ring-dev):

```
clojure -M:server
```

Then open http://localhost:8101 in a browser.

## Uberjar

Build a standalone uberjar with all dependencies included:

```
clojure -T:build uber
```

The Parsec API Service can be launched from the uberjar like this:

```
java -cp "target/parsec-api-*-standalone.jar" parsec.api.service
```