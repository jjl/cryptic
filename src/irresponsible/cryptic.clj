(ns irresponsible.cryptic
  (:require [clojure.string :as str]))

(def id-overrides
  {"2"  :bcrypt
   "2a" :bcrypt
   "2x" :bcrypt
   "2y" :bcrypt
   "2b" :bcrypt})

(defn identify-algo [hash]
  (when-let [v (some-> (str/split hash #"\$") second)]
    (or (id-overrides v) (keyword v))))

(defmulti hash-password
  "Hashes a password with the chosen algorithm and options
   args: [password opts]
     password: string
     opts: map with keys:
       :algo - keyword naming an algorithm"
  (fn [_ {:keys [algo]}]
    algo))

(defmulti check-password
  (fn [_ hash]
    (identify-algo hash))
  :default ::default)

(defmethod hash-password ::default
  [{:keys [algo]} _]
  (throw (ex-info (str "unknown algo: " algo) {})))

(defmethod check-password ::default
  [_ hash]
  (throw (ex-info (str "unknown algo: " (identify-algo hash)) {:got hash})))

(defn password-algos
  "Gets the list of algorithms which can be used to hash passwords
   args: []
   returns: set of algos"
  []
  (into #{} (comp (map first)
                  (filter #(not= ::default %)))
        (methods hash-password)))

;;; algorithms: argon2 family, requires argon2-jvm

(try
  (let [argon (de.mkammerer.argon2.Argon2Factory/create
               de.mkammerer.argon2.Argon2Factory$Argon2Types/ARGON2i)]
    (defmethod hash-password :argon2i [password
                                       {:keys [time memory parallelism]
                                        :or {time 3 memory 12 parallelism 1}}]
      (.hash argon time memory parallelism password))
    (defmethod check-password :argon2i  [password hash]
      (.verify argon hash password)))
  (catch Throwable e))

(try
  (let [argon (de.mkammerer.argon2.Argon2Factory/create
               de.mkammerer.argon2.Argon2Factory$Argon2Types/ARGON2d)]
    (defmethod hash-password :argon2d [password
                                       {:keys [time memory parallelism]
                                        :or {time 3 memory 12 parallelism 1}}]
      (.hash argon time memory parallelism password))
    (defmethod check-password :argon2d  [password hash]
      (.verify argon hash password)))
  (catch Throwable e))

(try
  (let [argon (de.mkammerer.argon2.Argon2Factory/create
               de.mkammerer.argon2.Argon2Factory$Argon2Types/ARGON2id)]
    (defmethod hash-password :argon2id [password
                                        {:keys [time memory parallelism]
                                         :or {time 3 memory 12 parallelism 1}}]
      (.hash argon time memory parallelism password))
    (defmethod check-password :argon2id [password hash]
      (.verify argon hash password)))
  (catch Throwable e))

;;; algorithm: bcrypt

;; (try
;;   (defmethod c/hash-password :bcrypt [password {:keys [work] :or {work 24}}]
;;     (->> (org.mindrot.jbcrypt.BCrypt/gensalt work)
;;          (org.mindrot.jbcrypt.BCrypt/hashpw password)))
;;   (defmethod c/check-password :bcrypt [password hash]
;;     (org.mindrot.jbcrypt.BCrypt/checkpw password hash))
;;   (catch Throwable e))
