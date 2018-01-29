(ns hello
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]))

(defn ok [body]
  {:status 200 :body body})

(defn not-found []
  {:status 404 :body "Not found\n"})

(def unmentionables #{"YHWH" "Voldemort" "Mxyzptlk" "Rumplestiltskin" "曹操"})

(defn greeting-for [nm]
  (cond
    (unmentionables nm) nil
    (empty? nm) "Hello world!\n"
    :else (str "Hello " nm "\n")))

(defn respond-hello [request]
  (let [nm (get-in request [:query-params :name])
        resp (greeting-for nm)]
    (if resp
      (ok resp)
      (not-found))))

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
