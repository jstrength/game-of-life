(ns game-of-life.gui
  (:require
    [game-of-life.core :refer :all]
    [game-of-life.field-generator :as gen]
    [quil.core :as q])
  (:gen-class :main true))

(def ^:dynamic width (* 1.5 1080))
(def ^:dynamic height (* 1 920))
(def cell-size 5)
(def x-row-count (/ width cell-size))
(def y-row-count (/ height cell-size))
(def random-field (atom (gen/midddle-line-field x-row-count y-row-count)))

(defn setup
  []
  (q/frame-rate 20))

(defn draw
  []
  (doseq [curr (range (* x-row-count y-row-count))]
    (let [x (* cell-size (mod curr x-row-count))
          y (* cell-size (quot curr x-row-count))]
      (if (live-cell? (@random-field curr))
        (q/fill 0)
        (q/fill 255))
      (q/rect x y cell-size cell-size)))
  (reset! random-field (tick x-row-count @random-field)))

(defn start
  []
  (q/defsketch game-of-life-gui
               :title "Game of Life"
               :setup setup
               :draw draw
               :size [width height]))

(defn -main
  ([]
    (start))
  ([w h]
   (binding
     [width (read-string w)
      height (read-string h)]
     (start))))
