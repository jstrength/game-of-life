(ns game-of-life.core-test
  (:require [game-of-life.core :refer :all]
            [midje.sweet :refer :all]))

(facts "live cell"
       (live-cell? 0) => falsey
       (live-cell? 1) => truthy)

(facts "neighbors"
       (fact "north"
             (north 3 1 [0 0 0
                         0 1 0]) => 0
             (north 3 4 [0 0 0
                         0 1 0]) => 0
             (north 3 4 [0 1 0
                         0 1 0]) => 1
             (north 3 3 [1 0 0
                         0 0 0]) => 1
             (north 3 3 []) => 0)
       (fact "south"
             (south 3 1 [0 0 0
                         0 1 0]) => 1
             (south 3 0 [0 0 0
                         0 0 0]) => 0
             (south 3 4 [0 0 0
                         0 1 0]) => 0
             (south 3 3 []) => 0)
       (fact "west"
             (west 3 0 [0 0 0
                        0 1 0]) => 0
             (west 3 1 [0 0 0
                        0 0 0]) => 0
             (west 3 1 [1 0 0
                        0 1 0]) => 1
             (west 3 3 [1 0 1
                        0 1 0]) => 0
             (west 3 3 []) => 0)
       (fact "east"
             (east 3 0 [0 0 0
                        0 1 0]) => 0
             (east 3 3 [1 0 0
                        0 1 0]) => 1
             (east 3 2 [0 0 0
                        1 0 0]) => 0
             (east 3 5 [1 0 1
                        0 1 0]) => 0
             (east 3 3 []) => 0)
       (fact "northeast"
             (northeast 3 4 [0 0 0
                             0 1 0]) => 0
             (northeast 3 4 [0 0 1
                             0 1 0]) => 1
             (northeast 3 0 [0 0 1
                             0 1 0]) => 0
             (northeast 3 8 [1 1 1
                             1 1 1
                             1 1 1]) => 0)
       (fact "northwest"
             (northwest 3 4 [0 0 0
                             0 1 0]) => 0
             (northwest 3 4 [1 0 0
                             0 1 0]) => 1
             (northwest 3 0 [0 0 1
                             0 1 0]) => 0
             (northwest 3 6 [1 1 1
                             1 1 1
                             1 1 1]) => 0)
       (fact "southeast"
             (southeast 3 0 [0 0 0
                             0 0 0]) => 0
             (southeast 3 0 [0 0 0
                             0 1 0]) => 1
             (southeast 3 3 [0 0 0
                             0 1 0]) => 0
             (southeast 3 2 [1 1 1
                             1 1 1
                             1 1 1]) => 0)
       (fact "southwest"
             (southwest 3 1 [0 0 0
                             0 0 0]) => 0
             (southwest 3 1 [0 0 0
                             1 0 0]) => 1
             (southwest 3 3 [1 1 1
                             1 1 1]) => 0
             (southwest 3 5 [1 1 1
                             1 1 1]) => 0)

       (fact "get live neighbors count"
             (count-live-neighbors 3 0 [0 1 0
                                        1 0 1]) => 2
             (count-live-neighbors 3 4 [0 1 0
                                        1 0 1
                                        1 1 1]) => 6
             (count-live-neighbors 3 0 [0 0 0
                                        1 0 1]) => 1))

(facts "rules"
       (fact "rule-one: under-populated?"
             (let [width 3]
               (under-populated? width 1 [0 1 0
                                          0 0 0]) => truthy
               (under-populated? width 1 [1 1 1
                                          1 1 1]) => falsey
               (under-populated? width 0 [1 1 0
                                          1 0 0]) => falsey
               (under-populated? width 0 [1 0 0
                                          1 0 0]) => truthy
               (under-populated? width 0 [0 0 0
                                          1 0 0]) => falsey
               (under-populated? width 0 [0 1 1
                                          1 1 1]) => falsey
               (under-populated? width 4 [0 0 0
                                          0 1 0]) => truthy
               (under-populated? width 1 []) => (throws IndexOutOfBoundsException)))
       (fact "rule-two: lives-on?"
             (let [width 3]
               (lives-on? width 1 [0 1 0
                                   0 0 0]) => falsey
               (lives-on? width 1 [1 1 1
                                   0 0 0]) => truthy
               (lives-on? width 1 [1 1 1
                                   1 0 0]) => truthy
               (lives-on? width 1 [1 1 1
                                   0 1 1]) => falsey
               (lives-on? width 1 [1 0 1
                                   0 0 0]) => falsey
               (under-populated? width 1 []) => (throws IndexOutOfBoundsException)))
       (fact "rule-three: over-crowded?"
             (let [width 3]
               (over-crowded? width 1 [0 1 0
                                       0 0 0]) => falsey
               (over-crowded? width 1 [0 1 0
                                       1 1 1]) => falsey
               (over-crowded? width 1 [1 1 0
                                       1 1 1]) => truthy
               (over-crowded? width 1 [1 0 0
                                       1 1 1]) => falsey
               (under-populated? width 1 []) => (throws IndexOutOfBoundsException)))
       (fact "rule-four: reproduced?"
             (let [width 3]
               (reproduced? width 1 [0 1 0
                                     0 0 0]) => falsey
               (reproduced? width 1 [0 0 0
                                     1 1 1]) => truthy
               (reproduced? width 1 [0 0 0
                                     0 1 1]) => falsey
               (reproduced? width 1 [1 0 0
                                     1 1 1]) => falsey
               (reproduced? width 1 [0 1 0
                                     1 1 1]) => falsey
               (under-populated? width 1 []) => (throws IndexOutOfBoundsException))))

(facts "new generation tick"
       (facts "still lifes"
              (fact "block"
                    (tick 4 [0 0 0 0
                             0 1 1 0
                             0 1 1 0
                             0 0 0 0]) =>
                    [0 0 0 0
                     0 1 1 0
                     0 1 1 0
                     0 0 0 0])
              (fact "beehive"
                    (tick 6 [0 0 0 0 0 0
                             0 0 1 1 0 0
                             0 1 0 0 1 0
                             0 0 1 1 0 0
                             0 0 0 0 0 0]) =>
                    [0 0 0 0 0 0
                     0 0 1 1 0 0
                     0 1 0 0 1 0
                     0 0 1 1 0 0
                     0 0 0 0 0 0])
              (fact "loaf"
                    (tick 6 [0 0 0 0 0 0
                             0 0 1 1 0 0
                             0 1 0 0 1 0
                             0 0 1 0 1 0
                             0 0 0 1 0 0]) =>
                    [0 0 0 0 0 0
                     0 0 1 1 0 0
                     0 1 0 0 1 0
                     0 0 1 0 1 0
                     0 0 0 1 0 0])
              (fact "boat"
                    (tick 5 [0 0 0 0 0
                             0 1 1 0 0
                             0 1 0 1 0
                             0 0 1 0 0
                             0 0 0 0 0]) =>
                    [0 0 0 0 0
                     0 1 1 0 0
                     0 1 0 1 0
                     0 0 1 0 0
                     0 0 0 0 0]))
       (facts "oscillators"
              (fact "blinker"
                    (tick 5 [0 0 0 0 0
                             0 0 1 0 0
                             0 0 1 0 0
                             0 0 1 0 0
                             0 0 0 0 0]) =>
                    [0 0 0 0 0
                     0 0 0 0 0
                     0 1 1 1 0
                     0 0 0 0 0
                     0 0 0 0 0])
              (fact "toad"
                    (tick 6 [0 0 0 0 0 0
                             0 0 0 1 0 0
                             0 1 0 0 1 0
                             0 1 0 0 1 0
                             0 0 1 0 0 0]) =>
                    [0 0 0 0 0 0
                     0 0 0 0 0 0
                     0 0 1 1 1 0
                     0 1 1 1 0 0
                     0 0 0 0 0 0])
              (fact "beacon"
                    (tick 6 [0 0 0 0 0 0
                             0 1 1 0 0 0
                             0 1 0 0 0 0
                             0 0 0 0 1 0
                             0 0 0 1 1 0]) =>
                    [0 0 0 0 0 0
                     0 1 1 0 0 0
                     0 1 1 0 0 0
                     0 0 0 1 1 0
                     0 0 0 1 1 0])))
(comment
  (ticks 20
         [
          0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
          0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
          0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
          0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
          0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
          0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
          0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
          0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
          0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
          0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
          0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
          0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
          0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
          0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
          ] 50))