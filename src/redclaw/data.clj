(ns redclaw.data
  (:refer-clojure :exclude [into conj])
  (:require [clojure.set :as set])
  (:import [org.redisson RedissonSet
                         RedissonMap
                         RedissonList
                         RedissonAtomicLong]))

(defprotocol Seq
  (conj [xs v])
  (into [xs oxs]))

(extend-protocol Seq
  RedissonMap
  (conj [rm k v]
    (.put rm k v))

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

;; -----------------------------------------------------------------
;; functions for list and set actions
;; -----------------------------------------------------------------
(defn add [rs v]
  (.add rs v))

(defn add-all [rs xs]
  (.addAll rs xs))

(defn read-all [rs]
  (.readAll rs))

(defn remove [rs v]
  (.remove rs v))

(defn contains? [rs s]
  (.contains rs s))

(defn contains-all? [rs xs]
  (.containsAll rs xs))

(defn contains-some? [rs xs]
  (->> (some xs (read-all rs))
       boolean))

;; -----------------------------------------------------------------
;; functions for map actions
;; -----------------------------------------------------------------

(defn put [rm k v]
  (.put rm k v))

(defn put-all [rm m]
  (.putAll rm m))

(defn get [rm k]
  (.get rm k))

(defn get-all
  ([rm] (get-all rm (.keySet rm)))
  ([rm xs] (.getAll rm xs)))

(defn get-keys [rm]
  (.keySet rm))

(defn has-key? [rm k]
  (.containsKey rm k))

;; -----------------------------------------------------------------
;; common functions for maps, sets and list
;; -----------------------------------------------------------------

(defn size [rm]
  (.size rm))

(defn empty? [rm]
  (.isEmpty rm))


;; -----------------------------------------------------------------
;; atomic long functions
;; -----------------------------------------------------------------
(defn get [k]
  (.get k))

(defn set [k v]
  (.set k v))

(defn incr
  ([k] (.incrementAndGet k))
  ([k d] (.addAndGet k d)))

(defn decr
  ([k] (.decrementAndGet k))
  ([k d] (.addAndGet k (* -1 d))))