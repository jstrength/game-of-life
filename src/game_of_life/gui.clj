(ns game-of-life.gui
  (:require
    [game-of-life.core :refer :all]
    [game-of-life.field-generator :as gen]
    [quil.core :as q]
    [quil.middleware :as m])
  (:import (java.awt Toolkit))
  (:gen-class :main true))

(def screen-size (.getScreenSize (Toolkit/getDefaultToolkit)))
(def ^:dynamic width (* 0.5 (.getWidth screen-size)))
(def ^:dynamic height (* 0.5 (.getHeight screen-size)))
(def cell-size 5)
(def x-row-count (/ width cell-size))
(def y-row-count (/ height cell-size))
(def random-field (gen/random-field x-row-count y-row-count))

(defn setup
  []
  (q/no-stroke)
  (q/frame-rate 30)
  {:field random-field})

(defn update
  [old-state]
  (update-in old-state [:field] (partial tick x-row-count)))

(defn draw
  [state]
  (doseq [curr (range (* x-row-count y-row-count))]
    (let [x (* cell-size (mod curr x-row-count))
          y (* cell-size (quot curr x-row-count))]
      (if (live-cell? ((:field state) curr))
        (q/fill 0)
        (q/fill 255))
      (q/rect x y cell-size cell-size))))

(defn start
  []
  (q/defsketch game-of-life-gui
               :title "Game of Life"
               :setup setup
               :draw draw
               :update update
               :size [width height]
               :middleware [m/fun-mode]))

(defn -main
  ([]
    (start))
  ([w h]
   (binding
     [width (read-string w)
      height (read-string h)]
     (start))))
