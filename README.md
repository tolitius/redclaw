# red claw

> _"... Red Claw managed to pull Batman's Cowl from his face"_

[![<! release](https://img.shields.io/badge/dynamic/json.svg?label=release&url=https%3A%2F%2Fclojars.org%2Ftolitius%2Fredclaw%2Flatest-version.json&query=version&colorB=blue)](https://github.com/tolitius/redclaw/releases)
[![<! clojars>](https://img.shields.io/clojars/v/tolitius/redclaw.svg)](https://clojars.org/tolitius/redclaw)

redis clojure client based on [redisson](https://github.com/redisson/redisson).

- [spilling the beans](#spilling-the-beans)
- [license](#license)

## spilling the beans

```clojure
$ make repl

=> (require '[redclaw.core :as rc])
```
```clojure
=> (def redis (rc/connect))  ;; by default will connect to a locally running redis on 6379 port
#'user/redis

=> (def s (rc/rset redis "solar-system"))
#'user/s

=> (rc/add-all s #{:mercury :venus :earth :mars :jupiter :saturn :uranus :neptune :pluto})
true
=> (count s)
9
=> (type s)
org.redisson.RedissonSet

=> (rc/read-all s)
#{:earth :mars :venus :jupiter :uranus :neptune :mercury :pluto :saturn}
```

looking inside the source (redis server):

```bash
redis 127.0.0.1:6379> smembers "solar-system"
1) "\x04>\x05earth"
2) "\x04>\x05pluto"
3) "\x04>\ajupiter"
4) "\x04>\x06saturn"
5) "\x04>\x06uranus"
6) "\x04>\aneptune"
7) "\x04>\amercury"
8) "\x04>\x04mars"
9) "\x04>\x05venus"
```

## license

Copyright © 2021 tolitius

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
