(ns parsec-web.components.base
  (:require
   [re-frame.core :as re-frame]
   [parsec-web.events :as events]
   ["@chakra-ui/react" :as chakra]))

(defn chakraProvider [& args] (into [:> chakra/ChakraProvider args]))

(defn box [& args] (into [:> chakra/Box] args))

(defn button [& args] (into [:> chakra/Button] args))

(defn flex [& args] (into [:> chakra/Flex] args))

(defn heading [& args] (into [:> chakra/Heading] args))

(defn icon [& args] (into [:> chakra/Icon] args))

(defn image [& args] (into [:> chakra/Image] args))

(defn text [& args] (into [:> chakra/Text] args))

(defn router-link [{router-target :router-target :as attrs} & args] 
  (into [:> chakra/Link (merge-with merge attrs {:on-click #(re-frame/dispatch [::events/navigate router-target])})] args))

(defn link [& children] 
  (let [child1 (first children)
        [m children] (if (map? child1)
                       [child1 (rest children)]
                       [{} children])
        attrs (merge-with merge {:text-decoration "none"} m)]
    (into [:> chakra/Link attrs] children)))

(defn vstack [& args] (into [:> chakra/VStack] args))