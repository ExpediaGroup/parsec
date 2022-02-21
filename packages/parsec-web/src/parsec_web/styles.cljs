(ns parsec-web.styles
  (:require-macros
   [garden.def :refer [defcssfn]])
  (:require
   [parsec-web.theme :as theme]
   [spade.core   :refer [defglobal defclass defkeyframes]]
   [garden.units :refer [deg px]]
   [garden.color :refer [rgba]]))

(defcssfn linear-gradient
  ([c1 p1 c2 p2]
   [[c1 p1] [c2 p2]])
  ([dir c1 p1 c2 p2]
   [dir [c1 p1] [c2 p2]]))

(defglobal defaults
  [:body
   {:font-family "Lato"
    :color               :red
    :background-color    :black
    :background-image    [(linear-gradient :white (px 2) :transparent (px 2))
                          (linear-gradient (deg 90) :white (px 2) :transparent (px 2))
                          (linear-gradient (rgba 255 255 255 0.3) (px 1) :transparent (px 1))
                          (linear-gradient (deg 90) (rgba 255 255 255 0.3) (px 1) :transparent (px 1))]
    :background-size     [[(px 100) (px 100)] [(px 100) (px 100)] [(px 20) (px 20)] [(px 20) (px 20)]]
    :background-position [[(px -2) (px -2)] [(px -2) (px -2)] [(px -1) (px -1)] [(px -1) (px -1)]]}])

(defclass level1
  []
  {:color :green})

(defkeyframes spin []
  ["0%"
   {:transform "rotate(0deg)"}]
  ["100%"
   {:transform "rotate(360deg)"}])

(defkeyframes twinkling []
  ["0%"
   {:background-position "0 0"}]
  ["100%"
   {:background-position "-1000px 1000px"}])