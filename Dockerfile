#
# Node.js Build Stage
#
FROM node:10-jessie as web
RUN npm set progress=false && npm set loglevel warn
COPY . /usr/src/parsec
WORKDIR /usr/src/parsec/web
RUN npm install && npm run build

#
# Clojure Build Stage
#
FROM clojure:openjdk-13-lein-2.9.1-buster as build
COPY --from=web /usr/src/parsec /usr/src/parsec
RUN mkdir -p /opt/parsec/bin
RUN mkdir -p /opt/parsec/lib

WORKDIR /usr/src/parsec
RUN wget https://repo1.maven.org/maven2/org/bouncycastle/bcprov-jdk15on/1.54/bcprov-jdk15on-1.54.jar -O /opt/parsec/lib/bcprov-jdk15on-1.54.jar

# Download dependencies
RUN lein deps

# Create Uberjar
COPY . /usr/src/parsec

RUN mv "$(lein uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" /opt/parsec/bin/parsec.jar

COPY ./bin/parsec2.sh /opt/parsec/bin/parsec2.sh
COPY ./bin/config.edn /etc/parsec/config.edn

#
# Production Stage
#
FROM clojure:openjdk-13-lein-2.9.1
WORKDIR /opt/parsec
COPY --from=build /opt/parsec .

EXPOSE 8101
CMD ["/bin/sh", "/opt/parsec/bin/parsec2.sh"]
