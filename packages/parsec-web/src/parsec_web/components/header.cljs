(ns parsec-web.components.header
  (:require
   [parsec-web.components.base :as c]
   [parsec-web.theme :as theme]
   [parsec-web.components.icons :as icons]
   [parsec-web.events :as events]
   [re-frame.core :as re-frame]
   ["@chakra-ui/react" :as chakra]))

;; site-wide header

(defn header []
  [c/flex
   {:p "0.5rem"
    :mx "2rem"
    :align "center"
    :justify "space-between"}
   [c/flex
    {:align "center"
     :cursor "pointer"
     :on-click #(re-frame/dispatch [::events/navigate :home])}
    [c/image {:src "/assets/img/parsec-logo.svg"
              :box-size theme/header-text-size
              :mr "0.5rem"}]
    [c/flex {:align "baseline"}
     [c/heading
      {:font-size theme/header-text-size :font-family theme/parsec-font-family :font-weight "400"}
      "parsec"]
     [c/heading
      {:font-size theme/header-subtext-size :font-family theme/parsec-font-family :font-weight "400"}
      "beta"]]]

   [c/flex
    {:align "center"}
    (map #(into [c/router-link {:router-target (keyword %)
                                :font-family theme/parsec-font-family
                                :font-size "2rem"
                                :mx "0.5rem"
                                :_hover {:background "white"
                                         :color "teal.500"}} %])
         ["home" "editor"])

    [:> chakra/Menu
     [:> chakra/MenuButton
      {:font-family theme/parsec-font-family
       :font-size "2rem"
       :mx "0.5rem"
       :_hover {:background "white"
                :color "teal.500"}}
      [c/text "learn"]]
     [:> chakra/MenuList
      [:> chakra/MenuItem {:font-family theme/parsec-font-family
                           :font-size "2rem"
                           :on-click #(re-frame/dispatch [::events/navigate :quickstart])} "quick start"]
      [:> chakra/MenuItem  {:font-family theme/parsec-font-family
                            :font-size "2rem"
                            :on-click #(re-frame/dispatch [::events/navigate :reference])} "reference"]]]

    [c/link
     {:href "https://github.com/ExpediaGroup/parsec"
      :height "2rem"}
     [c/icon {:as icons/github
              :mx "0.5rem"
              :boxSize "2rem"}]]]])