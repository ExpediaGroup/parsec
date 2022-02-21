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
   {:p "0 2rem"
    :align "stretch"
    :justify "space-between"
    :color :white
    ;:bg "#3d444e"
    :bg "synthwave.500"}

   [c/flex
    {:align "center"
     :cursor "pointer"
     :my "0.5rem"
     :on-click #(re-frame/dispatch [::events/navigate :home])}
    [c/image {:src "/assets/img/parsec-logo.svg"
              :box-size theme/header-text-size
              :mr "1rem"}]
    [c/flex {:align "baseline"}
     [c/heading
      {:font-size theme/header-text-size :font-weight "400"}
      "parsec"]
     [c/heading
      {:font-size theme/header-subtext-size :font-weight "400"}
      "beta"]]]

   [c/flex
    {:align "stretch"}
    (map #(into [c/router-link {:key %
                                :router-target (keyword %)
                                :font-family "heading"
                                :font-size "2rem"
                                :font-weight "600"
                                :px "1rem"
                                :display "flex"
                                :position "relative"
                                :justify-content "center"
                                :flex-direction "column"
                                :border-top "4px solid transparent"
                                :border-bottom "4px solid transparent"
                                :_hover {:border-bottom "4px solid"
                                         :color "parsec.blue"}} %])
         ["home" "editor"])

    [:> chakra/Menu
     [:> chakra/MenuButton
      {:font-family "heading"
       :font-size "2rem"
       :font-weight "600"
       :px "1rem"
       :display "flex"
       :position "relative"
       :justify-content "center"
       :flex-direction "column"
       :border-top "4px solid transparent"
       :border-bottom "4px solid transparent"
       :_hover {:border-bottom "4px solid"
                :color "parsec.blue"}
       :sx {"> span" {:flex "unset"}}}
      "learn"]
     [:> chakra/MenuList {:z-index 20
                          :color :black}
      [:> chakra/MenuItem {:font-family "heading"
                           :font-size "2rem"
                           :on-click #(re-frame/dispatch [::events/navigate :quickstart])} "quick start"]
      [:> chakra/MenuItem  {:font-family "heading"
                            :font-size "2rem"
                            :on-click #(re-frame/dispatch [::events/navigate :reference])} "reference"]]]

    [c/flex {:flex-direction "column"
             :justify-content "center"}
     (let [height "2.25rem"]
       [c/link
        {:href "https://github.com/ExpediaGroup/parsec"
         :height height}
        [c/icon {:as icons/github
                 :mx "1rem"

                 :boxSize height
                 :_hover {:color "parsec.blue"}}]])]]])