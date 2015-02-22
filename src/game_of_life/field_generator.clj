(ns game-of-life.field-generator)

(defn random-field
  [width height]
  (into [] (repeatedly (* width height) #(int (rand 2)))))

(defn half-random-field
  [width height]
  (let [half-size (/ (* width height) 2)]
    (into [] (concat (repeat half-size 0) (repeatedly half-size #(int (rand 2)))))))

(defn one-fourth-random-field
  [width height]
  (let [one-fourth-size (/ (* width height) 4)]
    (into [] (concat (repeat one-fourth-size 0)
                     (repeatedly one-fourth-size #(int (rand 2)))
                     (repeat one-fourth-size 0)
                     (repeatedly one-fourth-size #(int (rand 2)))))))

(defn midddle-line-field
  [width height]
  (let [half-size (/ (* width height) 2)]
    (into [] (concat (repeat half-size 0) (repeat width 1) (repeat (- half-size 1) 0) ))))
