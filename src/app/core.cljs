(ns app.core
  (:require
    [uix.core :as uix :refer [defui $]]
    [uix.dom]
    ["react-router-dom" :as router]
    ["antd" :as antd]))

(def context (uix.core/create-context {}))

(defui context-provider
  [{:keys [children]}]
  (let [[counter set-counter] (uix.core/use-state 0)]

    (uix.core/use-effect
     (fn []
       (set-counter (fn [x] (inc x))))
     [])
    ($ (.-Provider context) {:value {:title "Opa" :counter counter :set-counter set-counter}}
       children)))

(defui title
  []
  (let [context (uix.core/use-context context)]
    ($ :<>
       ($ (.-Title antd/Typography) (:title context))
       ($ router/Link {:to "/counter"} "Counter")
       ($ router/Link {:to "/button"} "Button"))))

(defui counter
  []
  (let [context (uix.core/use-context context)]
    ($ :<>
       ($ :h2 (:counter context))
       ($ router/Link {:to "/"} "Home")
       ($ router/Link {:to "/button"} "Button"))))

(defui button
  []
  (let [context (uix.core/use-context context)]
    ($ :<>
       ($ router/Link {:to "/"} "Home")
       ($ router/Link {:to "/counter"} "Counter")
       ($ :h2 (:counter context))
       ($ antd/Button {:on-click (fn [] ((:set-counter context) inc))}
          "Click me"))))

(defui router-provider
  []
  ($ (.-BrowserRouter router)
     ($ (.-Routes router)
        ($ (.-Route router) {:path "/" :exact true :element ($ title)})
        ($ (.-Route router) {:path "/counter" :element ($ counter)})
        ($ (.-Route router) {:path "/button" :element ($ button)}))))

(defui app
  []
  ($ context-provider
     ($ router-provider
        ($ :h1 "oi"))))

(defonce root
  (uix.dom/create-root (js/document.getElementById "root")))

(defn render []
  (uix.dom/render-root ($ app) root))

(defn ^:export init []
  (render))
