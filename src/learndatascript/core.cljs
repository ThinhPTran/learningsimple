(ns learndatascript.core
    (:require [reagent.core :as reagent]))

(defonce app-state (reagent/atom {:foo {:bar "Hello, world!"
                                        :baz {:quux "Woot"}
                                        :count 0}}))
(def foo-cursor (reagent/cursor app-state [:foo]))
(def foobar-cursor (reagent/cursor app-state [:foo :bar]))
(def foobaz-cursor (reagent/cursor app-state [:foo :baz]))
(def foobazquux-cursor (reagent/cursor app-state [:foo :baz :quux]))
(def foocount-cursor (reagent/cursor app-state [:foo :count]))

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

(defn home-page []
  [:div
   [:h2 "Welcome to Reagent"]
   [inside-app-state]
   [inside-foo-cursor]
   [inside-foobar-cursor]
   [inside-foobaz-cursor]
   [inside-foobazquux-cursor]
   [simple-component]
   [inside-foocount]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
