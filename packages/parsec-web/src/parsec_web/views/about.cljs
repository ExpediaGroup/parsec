(ns parsec-web.views.about
  (:require
   [re-frame.core :as re-frame]
   [parsec-web.events :as events]
   [parsec-web.components.chakra-ui :as chakra]))

;; about

(defn about-panel []
  [:div
   [chakra/heading "This is the About Page."]

   [:div
    [:a {:on-click #(re-frame/dispatch [::events/navigate :home])}
     "go to Home Page"]]])
