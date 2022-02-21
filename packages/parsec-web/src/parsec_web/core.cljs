(ns parsec-web.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [parsec-web.events :as events]
   [parsec-web.routes :as routes]
   [parsec-web.views.main :as views]
   [parsec-web.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "root")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (routes/start!)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
