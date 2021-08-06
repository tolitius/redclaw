(ns redclaw.data
  (:refer-clojure :exclude [into conj])
  (:import [org.redisson RedissonSet
                         RedissonMap]))

(defprotocol Seq
  (conj [xs v])
  (into [xs oxs]))

(extend-protocol Seq
  RedissonMap
  (conj [rs v]
    (.put rs v))

  (into [rm xs]
    (->> xs
         (clojure.core/into {})
         (.putAll rm))))

(extend-protocol Seq
  RedissonSet
  (conj [rs v]
    (.add rs v))

  (into [rs xs]
    (->> xs
         (clojure.core/into #{})
         (.addAll rs))))


;; -----------------------------------------------------------------
;; these are composable wrappers for one to one function <=> method
;; -----------------------------------------------------------------

(defn add [rs v]
  (.add rs v))

(defn add-all [rs xs]
  (.addAll rs xs))

(defn read-all [rs]
  (.readAll rs))

(defn put [rm k v]
  (.put rm k v))

(defn put-all [rm m]
  (.putAll rm m))

(defn get-all [rm]
  (.getAll rm))
