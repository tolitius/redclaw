# red claw

> _"... Red Claw managed to pull Batman's Cowl from his face"_

[![<! release](https://img.shields.io/badge/dynamic/json.svg?label=release&url=https%3A%2F%2Fclojars.org%2Fcom.tolitius%2Fredclaw%2Flatest-version.json&query=version&colorB=blue)](https://github.com/tolitius/redclaw/releases)
[![<! clojars>](https://img.shields.io/clojars/v/com.tolitius/redclaw.svg)](https://clojars.org/com.tolitius/redclaw)

redis clojure client based on [redisson](https://github.com/redisson/redisson).

- [spilling the beans](#spilling-the-beans)
- [license](#license)

## spilling the beans

```clojure
$ make repl

=> (require '[redclaw.core :as rc]  ;; core functionality
            '[redclaw.data :as rd]) ;; working with data/structures

=> (def redis (rc/connect))  ;; by default will connect to a locally running redis on 6379 port
#'user/redis
```

```clojure
;; a map
=> (def solar-system (rc/rmap redis "solar-system"))
#'user/solar-system

=> (rd/into solar-system {:saturn {:age 4503000000} :jupiter {:age 4603000000}})

=> solar-system
{:jupiter {:age 4603000000}, :saturn {:age 4503000000}}

=> (:jupiter solar-system)
{:age 4603000000}
```

clojure seq functions, such as `into`, for maps, sets, lists, etc.. would work the same way they do on clojure seqs taking vectors and all:

```clojure
;; a set
=> (def planets (rc/rset redis "planets"))
#'user/planets

=> (rd/into planets [:mercury :venus :earth :mars :jupiter :saturn :uranus :neptune])

=> planets
#{:uranus :jupiter :saturn :mercury :neptune :venus :mars :earth}

user=> (rd/conj planets :pluto)
true

;; now with pluto
user=> planets
#{:uranus :jupiter :saturn :mercury :neptune :pluto :venus :mars :earth}

;; "some" clojure fns
user=> (some #{:proxima-centauri-b} planets)
nil

user=> (some #{:proxima-centauri-b :saturn} planets)
:saturn
```

atomic long

```clojure
user=> (def stars (rc/rlong redis "number-of-stars"))

user=> stars
#object[org.redisson.RedissonAtomicLong 0x426913c4 "0"]

user=> (repeatedly 42 #(rd/incr counter))

user=> (rd/get stars)
42
```

> _`redclaw.data` also has one to one redisson mapped functions: `add`, `add-all`, `get-all`, etc.._

looking inside the source (redis server):

```bash
127.0.0.1:6379> keys *
1) "planets"
2) "solar-system"
3) "number-of-stars"

redis 127.0.0.1:6379> smembers "planets"
1) "\x04>\x05earth"
2) "\x04>\x05pluto"
3) "\x04>\ajupiter"
4) "\x04>\x06saturn"
5) "\x04>\x06uranus"
6) "\x04>\aneptune"
7) "\x04>\amercury"
8) "\x04>\x04mars"
9) "\x04>\x05venus"

redis 127.0.0.1:6379> get "number-of-stars"
"42"
```

## license

Copyright © 2021 tolitius

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
