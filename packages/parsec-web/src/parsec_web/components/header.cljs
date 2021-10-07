(ns parsec-web.components.header
  (:require
   [parsec-web.components.base :as c]
   [parsec-web.theme :as theme]))

;; site-wide header

(defn header []
  [c/flex
   {:p "0.5rem"
    :align "center"
    :justify "space-between"}
   [c/flex
    {:align "center"}
    [c/image {:src "/assets/img/parsec-logo.svg"
                   :box-size theme/header-text-size
                   :mr "0.5rem"}]
    [c/heading
     {:font-size theme/header-text-size :font-family theme/parsec-font-family :font-weight "400"}
     "parsec"]
    
    (map #(into [c/router-link {:router-target (keyword %) 
                                :font-family theme/parsec-font-family 
                                :font-size "2rem" 
                                :mx "0.5rem"} %]) 
         ["home" "editor" "learn" "community"])
   ]])
