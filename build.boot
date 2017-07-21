(set-env!
  :resource-paths #{"src"}
  :source-paths #{"src"}
  :description "cryptography made palatable"
  :version "0.1.0"
  :dependencies '[[org.clojure/clojure     "1.9.0-alpha17"]
                  [org.clojure/test.check  "0.10.0-alpha2" :scope "test"]
                  [boot-codox              "0.10.3" :scope "test"]
                  [de.mkammerer/argon2-jvm "2.2"           :scope "test"]
;                  [org.mindrot/bcrypt "0.4.0" :scope "test"]
                  [adzerk/boot-test        "1.1.0"         :scope "test"]])

(require '[adzerk.boot-test :as t]
         '[codox.boot :refer [codox]])

(task-options!
  codox {:name "cryptic"
         :description (get-env :description)
         :version (get-env :version)}
  pom {:project 'irresponsible/locker-room
       :version (get-env :version)}
  target {:dir #{"target"}})

(deftask testing []
  (set-env! :source-paths   #(conj % "test")
            :resource-paths #(conj % "test"))
  identity)

(deftask test []
  (comp (speak)
        (testing)
        (t/test)))

(deftask build []
  (comp (speak)
        (pom)
        (jar)))

(deftask deps []
  identity)

(deftask travis []
  (comp (testing) (t/test)))
