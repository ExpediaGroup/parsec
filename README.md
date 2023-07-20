# Parsec

## Overview

Parsec is a data processing engine for interpreted queries. It provides a custom domain-specific language (DSL) for loading, transforming, and analyzing data from various sources.

It provides an interpreted data pipeline for tabular data, starting with a
data source followed by any number of operations or transformations.

### Examples

Return a sample dataset:

    source = mock

Filter the dataset to rows matching the predicate:

    source = mock | filter col1 > 2

Sort the dataset:

    source = mock | filter col1 > 2 | sort col2 desc

Select only the first row:

    source = mock | filter col1 > 2 | sort col2 desc | head

Create a new column:

    source = mock | x = col * col2

Create two new columns:

    source = mock | x = col * col2, isXLarge = x > 10

Count rows:

    source = mock | stats count = count(1)

Count rows, grouped by another column:

    source = mock | stats count = count(1) by col2

Take an average of a column:

    source = mock | stats x = avg(col1)

Take an average of an expression:

    source = mock | stats x = avg(col1 + col2)

### Design

The engine of Parsec is split into three components:

#### Parser

The context-free grammar behind Parsec is defined in
[Extended Backus–Naur Form (EBNF)](https://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_Form).
This grammar is used to generate a parser which converts user-provided queries into an
expression tree. The expression tree has no logic or implementation details;
it is merely a representation of the query.

#### Optimizer

After the query is parsed into an expression tree, it can be inspected and,
if possible, rewritten into a more efficient form.

#### Executor

The optimized expression tree is traversed depth-first and transformed into a
series of nested functions. When executed, these functions will perform
the steps of the query as written, returning the final output.

## Using Parsec

### Requirements

Parsec is written in Clojure and runs in the JVM.

- Java 11 or greater
- [Clojure](https://clojure.org/guides/install_clojure) 1.10.3 or greater

### Building Parsec

This repository contains several packages, each of which can be built independently. For detailed
instructions, see the README.md file in each package.

The Clojure projects are built using [tools.build](https://clojure.org/guides/tools_build). The
following commands are available:

- `clojure -T:build clean` - Delete all build artifacts
- `clojure -T:build jar` - Build a source jar
- `clojure -T:build uber` - Build an uberjar
- `clojure -X:test` - Run tests

To build the Parsec API service, use the following commands:

```sh
cd packages/parsec-api
clojure -T:build uber
```

This creates a standalone uberjar in the `packages/parsec-api/target/uberjar` folder.

To build the Parsec web UI, use the following commands:

```sh
cd packages/parsec-web
nvm use
npm install
npm run build
```

### Running Parsec

The Parsec API service can be launched from the uberjar like this:

```sh
java -cp "packages/parsec-api/target/uberjar/parsec-api-VERSION-standalone.jar" parsec.api.service
```

This launches the service on port 8101.

The Parsec web UI can be launched from the `packages/parsec-web` folder like this:

```sh
npm start
```

This starts a local development server and automatically opens `http://localhost:8101/` in a browser.

### Docker

Build a Docker image, which compiles Parsec internally:

    docker build -t parsec .

And run:

    docker run -p 8101:8101 parsec

### Metrics

The web service provides a JSON dump of metrics at the following URL:

    http://localhost:8101/metrics

## Contributing

Pull requests are welcome. Please refer to our [CONTRIBUTING](./CONTRIBUTING.md) file.

## Legal

This project is available under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).

Copyright 2020-2023 Expedia, Inc.
