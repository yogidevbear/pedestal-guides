(ns hello
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]))

(defn ok [body]
  {:status 200 :body body})

(defn greeting-for [nm]
  (if (empty? nm)
    "Hello world!\n"
    (str "Hello " nm "\n")))

(defn respond-hello [request]
  (let [nm (get-in request [:query-params :name])
        resp (greeting-for nm)]
    (ok resp)))

(def routes
  (route/expand-routes
    #{["/greet" :get respond-hello :route-name :greet]}))

(defn create-server []
  (http/create-server
    {::http/routes routes
     ::http/type   :jetty
     ::http/port   8890}))

(defn start []
  (http/start (create-server)))
