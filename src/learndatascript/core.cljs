(ns learndatascript.core
    (:require [reagent.core :as reagent]))

(defonce app-state (reagent/atom {:foo {:bar "Hello, world!"
                                        :baz {:quux "Woot"}}}))
(def foo-cursor (reagent/cursor app-state [:foo]))
(def foobar-cursor (reagent/cursor app-state [:foo :bar]))
(def foobaz-cursor (reagent/cursor app-state [:foo :baz]))
(def foobazquux-cursor (reagent/cursor app-state [:foo :baz :quux]))

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


(defn home-page []
  [:div
   [:h2 "Welcome to Reagent"]
   [inside-app-state]
   [inside-foo-cursor]
   [inside-foobar-cursor]
   [inside-foobaz-cursor]
   [inside-foobazquux-cursor]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
