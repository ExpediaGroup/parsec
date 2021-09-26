(ns parsec-web.components.chakra-ui
  (:require
   ["@chakra-ui/react" :as chakra]))

(defn chakraProvider [& args] (into [:> chakra/ChakraProvider args]))

(defn box [& args] (into [:> chakra/Box] args))

(defn button [& args] (into [:> chakra/Button] args))

(defn flex [& args] (into [:> chakra/Flex] args))

(defn heading [& args] (into [:> chakra/Heading] args))

(defn image [& args] (into [:> chakra/Image] args))