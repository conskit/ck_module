(ns {{name}}-test
  (:require
    [puppetlabs.trapperkeeper.app :as app]
    [puppetlabs.trapperkeeper.core :refer [defservice]]
    [puppetlabs.trapperkeeper.services :refer [service-context]]
    [puppetlabs.trapperkeeper.testutils.bootstrap :refer [with-app-with-cli-data]]
    [conskit.core :as ck]
    [conskit.macros :refer :all])
  (:use midje.sweet))


(defcontroller
  my-controller
  []
  (action
    my-action
    [req]
    {:hello "world" :req req}))

(defprotocol ResultService
  (get-result [this]))

(defservice
  test-service ResultService
  [[:ActionRegistry register-controllers!]]
  (init [this context]
        (register-controllers! [my-controller])
        context)
  (start [this context]
         {:result :fix-me-i-fail})
  (get-result [this]
              (:result (service-context this))))

(with-app-with-cli-data
  app
  [ck/registry test-service]
  {:config "./dev-resources/test-config.conf"}
  (let [serv (app/get-service app :ResultService)
        res (get-result serv)]
    (fact res => :i-really-do-fail)))