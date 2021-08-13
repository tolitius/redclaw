(ns redclaw.data
  (:refer-clojure :exclude [into conj keys remove get set contains? empty?])
  (:require [clojure.set :as set]
            [clojure.tools.logging :as log])
  (:import [org.redisson RedissonSet
                         RedissonMap
                         RedissonList
                         RedissonAtomicLong]))

(defprotocol Seq
  (conj [xs v])
  (into [xs oxs])
  (get-all [rm] [rm xs])
  (contains? [rm k]))

(defprotocol AtomicLong
  (incr [k] [k v])
  (decr [k] [k v]))

(extend-protocol Seq
  RedissonMap

  (conj [rm k v]
    (.put rm k v))

  (into [rm xs]
    (->> xs
         (clojure.core/into {})
         (.putAll rm)))

  (get-all
    ([rm] (get-all rm (.keySet rm)))
    ([rm xs] (.getAll rm xs)))

  (contains? [rm v]
    (.containsKey rm v)))

(extend-protocol Seq
  RedissonSet

  (conj [rs v]
    (.add rs v))

  (into [rs xs]
    (->> xs
         (clojure.core/into #{})
         (.addAll rs)))

  (get-all [rs]
    (.readAll rs))

  (contains? [rs v]
    (.contains rs v)))

(extend-protocol Seq
  RedissonList

  (conj [rs v]
    (.add rs v))

  (into [rs xs]
    (->> xs
         (clojure.core/into #{})
         (.addAll rs)))

  (get-all [rs]
    (.readAll rs))

  (contains? [rs v]
    (.contains rs v)))

(extend-protocol AtomicLong
  RedissonAtomicLong

  (incr
    ([k] (.incrementAndGet k))
    ([k d] (.addAndGet k d)))

  (decr
    ([k] (.decrementAndGet k))
    ([k d] (.addAndGet k (* -1 d)))))


;; -----------------------------------------------------------------
;; these are composable wrappers for one to one function <=> method
;; -----------------------------------------------------------------

(defn set [r v]
  (.set r v))

(defn get [k]
  (.get k))

(defn add [rs v]
  (.add rs v))

(defn add-all [rs xs]
  (.addAll rs xs))

(defn remove [rs v]
  (.remove rs v))

(defn contains-all? [rs xs]
  (.containsAll rs xs))

(defn put [rm k v]
  (.put rm k v))

(defn put-all [rm m]
  (.putAll rm m))

(defn keys [rm]
  (.keySet rm))
