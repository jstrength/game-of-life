(defproject game-of-life "0.1.0-SNAPSHOT"
  :description "Game of life simulator, written with TDD and quil"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [midje "1.6.3"]
                 [quil "2.2.5"]]
  :plugins [[lein-midje "3.1.3"]]
  :main ^:skip-aot game-of-life.gui
  :uberjar-name "game-of-life.jar"
  :profiles {:uberjar {:aot :all}})
