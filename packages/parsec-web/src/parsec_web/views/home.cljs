(ns parsec-web.views.home
  (:require
   [re-frame.core :as re-frame]
   [parsec-web.events :as events]
   [parsec-web.subs :as subs]
   [parsec-web.components.base :as c]))


;; home
(defn hello-component []
  [c/button {:size "xs" :colorScheme "green"} "An amazing button"])


(defn home-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    
    ;; .hero
    ;;     .hero-background
    ;;     .hero-inner
    ;;         .hero-logo
    ;;             img(src='img/parsec-logo.svg', alt='Parsec')
    ;;             h1 parsec
    ;;             h2 A Next-Gen Query Engine
    ;;         .hero-copy
    ;;         .hero-attribution
    ;;           span Image by:
    ;;             a(href="https://unsplash.com/@pawel_czerwinski")  Paweł Czerwiński

    [:div
     [c/heading (str "Hello from " @name ". This is the Home Page.")]

     (hello-component)

     [:div
      [:a {:on-click #(re-frame/dispatch [::events/navigate :about])}
       "go to About Page"]]
     
     ;; hero
     [c/vstack {:align "center"} 
      [c/image {:src "/assets/img/parsec-logo.svg"
                    :box-size "4rem"
                    :mr "0.5rem"}]
      [:div "there"]]]))
