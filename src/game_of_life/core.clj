(ns game-of-life.core)

;1. Any live cell with fewer than two live neighbours dies, as if caused by under-population.
;2. Any live cell with two or three live neighbours lives on to the next generation.
;3. Any live cell with more than three live neighbours dies, as if by overcrowding.
;4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

(defn live-cell? [cell] (not (zero? cell)))

(defn north
  [w idx field]
  (nth field (- idx w) 0))
(defn south
  [w idx field]
  (nth field (+ idx w) 0))
(defn west
  [w idx field]
  (if (zero? (mod idx w))
    0
    (nth field (dec idx) 0)))
(defn east
  [w idx field]
  (if (zero? (mod (inc idx) w))
    0
    (nth field (inc idx) 0)))
(defn northeast
  [w idx field]
  (if (zero? (mod (inc idx) w))
    0
    (nth field (- idx (dec w)) 0)))
(defn northwest
  [w idx field]
  (if (zero? (mod idx w))
    0
    (nth field (- idx (inc w)) 0)))
(defn southeast
  [w idx field]
  (if (zero? (mod (inc idx) w))
    0
    (nth field (+ idx (inc w)) 0)))
(defn southwest
  [w idx field]
  (if (zero? (mod idx w))
    0
    (nth field (+ idx (dec w)) 0)))

(defn count-live-neighbors
  [w idx field]
  (let [neighbors [(north w idx field)
                   (west w idx field)
                   (south w idx field)
                   (east w idx field)
                   (northwest w idx field)
                   (southwest w idx field)
                   (southeast w idx field)
                   (northeast w idx field)]]
    (count (filter live-cell? neighbors))))

;TODO: Better way of return false?
(defn under-populated?
  [width pos field]
  (if (live-cell? (field pos))
    (> 2 (count-live-neighbors width pos field))
    false))
(defn lives-on?
  [width pos field]
  (if (live-cell? (field pos))
    (let [live-neighbors (count-live-neighbors width pos field)]
      (or (= 2 live-neighbors) (= 3 live-neighbors)))
    false))
(defn over-crowded?
  [width pos field]
  (if (live-cell? (field pos))
    (< 3 (count-live-neighbors width pos field))
    false))
(defn reproduced?
  [width pos field]
  (if (not (live-cell? (field pos)))
    (= 3 (count-live-neighbors width pos field))
    false))

(defn tick
  [w field]
  (->>
    (map-indexed (fn [idx cell]
                   (if (under-populated? w idx field)
                     0
                     (if (lives-on? w idx field)
                       1
                       (if (over-crowded? w idx field)
                         0
                         (if (reproduced? w idx field)
                           1
                           cell)))))
                 field)
    (into [])))


(defn ticks
  [width field iterations]
  (Thread/sleep 100)
  (doall (map println (partition width field)))
  (flush)
  (if (zero? iterations)
    "done"
    (recur width (tick width field) (dec iterations))))
