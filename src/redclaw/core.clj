(ns redclaw.core
  (:require [clj-yaml.core :as yaml])
  (:import [org.redisson Redisson]
           [org.redisson.config Config]))

(defn edn->config
  "creates a redisson config from edn"
  [edn]
  (-> edn
      yaml/generate-string
      Config/fromYAML))

(defn single-server-config
  "creates a single servers config"
  ([]
   (single-server-config {}))
  ([{:keys [uri]
     :or {uri "redis://127.0.0.1:6379"}
     :as config}]
   (let [config (Config.)]
     (-> config
         .useSingleServer
         (.setAddress uri))
     config)))

(defn cluster-config
  "creates a cluster servers config"
  ([]
   (cluster-config {}))
  ([{:keys [uris]
     :or {uris ["redis://127.0.0.1:6379"]}
     :as config}]
   (let [config (Config.)]
     (-> config
         .useClusterServers
         (.setNodeAddresses uris))
     config)))

(defn replicated-config
  "creates a replicated servers config"
  ([]
   (cluster-config {}))
  ([{:keys [uris scan-interval]
     :or {uris ["redis://127.0.0.1:6379"]
          scan-interval 2000}
     :as config}]
   (let [config (Config.)
         rconf (.useReplicatedServers config)]
     (doto rconf
           (.setScanInterval scan-interval)
           (.setNodeAddresses uris))
     config)))

(defn connect
  ([]
   (Redisson/create))
  ([config]
  (Redisson/create config)))

;-------------------------------------------

(defn rset [redis sname]
  (.getSet redis sname))

(defn rmap [redis mname]
  (.getMap redis mname))
