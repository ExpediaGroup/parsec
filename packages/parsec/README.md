


## Source Jar

The most common Clojure build creates a jar file containing Clojure source code.  This is appropriate for use within other Clojure projects.

```
clojure -T:build clean
clojure -T:build jar
```

## Uberjar

Build a standalone uberjar with all dependencies included:

```
clojure -T:build clean
clojure -T:build uber
```
