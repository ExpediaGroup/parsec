(ns parsec-web.components.header
  (:require
   [parsec-web.components.chakra-ui :as chakra]))

;; about

(def header-text-size "2.5rem")

(defn header []
  [chakra/flex
   [chakra/image {:src "/assets/img/parsec-logo.svg" :box-size header-text-size}]
   [chakra/heading {:font-size header-text-size} "Parsec"]])
