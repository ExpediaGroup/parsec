(ns parsec-web.views.home
  (:require
   [re-frame.core :as re-frame]
   [parsec-web.events :as events]
   [parsec-web.subs :as subs]
   [parsec-web.components.chakra-ui :as chakra]))


;; home
(defn hello-component []
  [chakra/button {:size "xs" :colorScheme "green"} "An amazing button"])


(defn home-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [chakra/heading (str "Hello from " @name ". This is the Home Page.")]

     (hello-component)

     [:div
      [:a {:on-click #(re-frame/dispatch [::events/navigate :about])}
       "go to About Page"]]]))
