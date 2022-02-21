(ns parsec-web.views.home
  (:require
   [re-frame.core :as re-frame]
   [parsec-web.components.base :as c]
   [parsec-web.events :as events]
   [parsec-web.subs :as subs]
   [parsec-web.styles :as styles]
   [parsec-web.theme :as theme]
   ["@chakra-ui/react" :as chakra]))


;; home
(defn home-panel []
  (let [name (re-frame/subscribe [::subs/name])]

    ;;[:div {:class "stars"}]
    ;;[:div {:class "twinkle"}]

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

    [c/flex {:flexDirection "column"
             :flexGrow 2
             :position "relative"
             :overflow "hidden"
             :background "black"
             :color "white"
             :_before {:content "\" \""
                       :background "transparent url(/assets/img/stars.png) repeat center center"
                       :background-size "contain"
                       :position "absolute"
                       :top 0
                       :left 0
                       :w "100%"
                       :h "100%"
                       :transform-origin "50% 50%"
                       :animation (str (styles/spin) " 420s linear infinite")}
             :_after {:content "\" \""
                      :background "transparent url(/assets/img/twinkle.png) repeat top center"
                      :position "absolute"
                      :top 0
                      :left 0
                      :w "100%"
                      :h "100%"
                      :animation (str (styles/twinkling) " 30s linear infinite")}}

     [c/vstack {:align "center"
                :z-index 2}

      ;; [:div
      ;;  [:a {:on-click #(re-frame/dispatch [::events/navigate :about])}
      ;;   "go to About Page"]]

      [c/vstack {:align "center"
                 :py "8rem"}

       [c/image {:src "/assets/img/parsec-logo.svg"
                 :box-size "12rem"
                 :mr "0.5rem"}]
       [c/heading
        {:font-size "8rem" :font-family "heading" :font-weight "400"
         :py "6rem"}
        "parsec"]
       [c/text {:font-size "lg"} "Parsec is a data processing and calculation engine for running analytic queries against various data sources."]

       ;[c/box {:w "15rem" :h "15rem" :bg "#004044"}]
       ]]]))
