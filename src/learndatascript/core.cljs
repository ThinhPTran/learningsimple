(ns learndatascript.core
    (:require [reagent.core :as reagent]
              [datascript.core :as d]))

;; -------------------------
;; Datascript

;; Create a DataScript "connection" (an atom with the current DB value)
(defonce conn (d/create-conn))

;; Define datoms to transact
(defonce datoms [{:db/id -1 :name "Bob" :age 30}
                 {:db/id -2 :name "Sally" :age 15}])

;; Add the datoms via transaction
(d/transact! conn datoms)

(def q-young '[:find ?n
               :in $ ?min-age
               :where
               [?e :name ?n]
               [?e :age ?a]
               [(< ?a ?min-age)]])

;;; execute query: q-young, passing 18 as min-age
(defn queryfunc [inval]
  (d/q q-young @conn inval))

;;; Query Result
;;; => [["Sally"]]

;; -------------------------
;; App-state

(defonce app-state (reagent/atom {:foo {:bar "Hello, world!"
                                        :baz {:quux "Woot"}
                                        :count 0}
                                  :data nil
                                  :database nil}))

(def foo-cursor (reagent/cursor app-state [:foo]))
(def foobar-cursor (reagent/cursor app-state [:foo :bar]))
(def foobaz-cursor (reagent/cursor app-state [:foo :baz]))
(def foobazquux-cursor (reagent/cursor app-state [:foo :baz :quux]))
(def foocount-cursor (reagent/cursor app-state [:foo :count]))
(def data-cursor (reagent/cursor app-state [:data]))
(def database-cursor (reagent/cursor app-state [:database]))

;; -------------------------
;; Handler
;(defn get-datoms [conn]
;  (->> @conn
;       (.-eavt)))
;       ;(-seq)
;       ;(.-keys)
;       ;(js->clj)
;       ;(mapv #({:e (.-e %2)
;       ;         :a (.-name (.-a %2))
;       ;         :v (.-v %2)
;       ;         :tx (.-tx %2)
;       ;         :add (.-added %2)}))))


(defn get-datoms [conn]
  (mapv #(zipmap [:e :a :v :t :add] %) (:eavt @conn)))

(let [result (vec (queryfunc 40))]
  (swap! app-state assoc :data result)
  (swap! app-state assoc :database (get-datoms conn)))

(defn handleaddperson []
  (let [datom [{:db/id -1 :name (str "Thinh" (rand-int 1000)) :age (rand-int 50)}]
        tmp (d/transact! conn datom)
        result (vec (queryfunc 40))]
    (swap! app-state assoc :data result)
    (swap! app-state assoc :database (get-datoms conn)))
  (.log js/console "handle adding a new person!!!")
  (.log js/console "db: " (str @database-cursor)))

;; -------------------------
;; Views
(defn inside-app-state []
  (.log js/console "inside-app-state")
  [:div (str "Inside app-state: " @app-state)])

(defn inside-foo-cursor []
  (.log js/console "inside-foo-cursor")
  [:div (str "Inside foo-cursor: " @foo-cursor)])

(defn inside-foobar-cursor []
  (.log js/console "inside-foobar-cursor")
  [:div (str "Inside foobar-cursor: " @foobar-cursor)])

(defn inside-foobaz-cursor []
  (.log js/console "inside-foobaz-cursor")
  [:div (str "Inside foobaz-cursor: " @foobaz-cursor)])

(defn inside-foobazquux-cursor []
  (.log js/console "inside-foobazquux-cursor")
  [:div (str "Inside foobazquux-cursor: " @foobazquux-cursor)])

(defn simple-component []
  (.log js/console "simple-component")
  [:div
   [:p "I am a component!"]
   [:p.someclass
    "I have " [:strong "bold"]
    [:span {:style {:color "red"}} " and red "] "text."]])

(defn inside-foocount []
  (.log js/console "inside-foocount")
  [:div (str "count value: " @foocount-cursor)
   [:input {:type "button" :value "Click me!"
            :on-click #(swap! foocount-cursor inc)}]])

(defn inside-data []
  (.log js/console "inside-data")
  [:div (str "data: " @data-cursor)
   [:input {:type "button" :value "Create a new person!"
            :on-click #(handleaddperson)}]])

(defn inside-database []
  (.log js/console "inside-database")
  [:div (str "inside database: " @database-cursor)])

(defn home-page []
  [:div
   [:h2 "Welcome to my Datascript experiment"]
   [inside-app-state]
   [inside-foo-cursor]
   [inside-foobar-cursor]
   [inside-foobaz-cursor]
   [inside-foobazquux-cursor]
   [simple-component]
   [inside-foocount]
   [inside-data]
   [inside-database]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
