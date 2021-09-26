(ns parsec-web.views.main
  (:require
   [re-frame.core :as re-frame]
   [parsec-web.routes :as routes]
   [parsec-web.views.about :as about]
   [parsec-web.views.home :as home]
   [parsec-web.subs :as subs]
   [parsec-web.components.chakra-ui :as chakra]
   [parsec-web.components.header :as header]))

;; main

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [chakra/chakraProvider
     [:div
      (header/header)
      (routes/panels @active-panel)]]))


;; register sub-panels

(defmethod routes/panels :about-panel [] [about/about-panel])
(defmethod routes/panels :home-panel [] [home/home-panel])
