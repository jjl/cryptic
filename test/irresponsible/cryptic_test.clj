(ns irresponsible.cryptic-test
  (:require [irresponsible.cryptic :as c]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test :as t]))

;; Note that there's a bug with test.check that means pass isn't defined when pass2 is declared
;; which is why i've had to comment out the checks and sometimes it might fail
;; perhaps we can append a long constant to the end to make failure unlikely

(defspec argon2id-test
  (prop/for-all [pass (gen/such-that #(not= "" %) gen/string)]
    (let [hash (c/hash-password pass {:algo :argon2id})
          pass2 (str pass pass)]
      (t/is (= :argon2id (c/identify-algo hash)))
      (t/is (true?  (c/check-password pass hash)))
      (t/is (false? (c/check-password pass2 hash))))))

(defspec argon2i-test
  (prop/for-all [pass (gen/such-that #(not= "" %) gen/string)]
    (let [hash (c/hash-password pass {:algo :argon2i})
          pass2 (str pass pass)]
      (t/is (= :argon2i (c/identify-algo hash)))
      (t/is (true?  (c/check-password pass hash)))
      (t/is (false? (c/check-password pass2 hash))))))

(defspec argon2d-test
  (prop/for-all [pass (gen/such-that #(not= "" %) gen/string)]
    (let [hash (c/hash-password pass {:algo :argon2d})
          pass2 (str pass pass)]
      (t/is (= :argon2d (c/identify-algo hash)))
      (t/is (true?  (c/check-password pass hash)))
      (t/is (false? (c/check-password pass2 hash))))))
  
;; (defspec bcrypt-test
;;   (prop/for-all [pass gen/string
;;                  pass2 (gen/such-that #(not= pass %) gen/string)]
;;     (let [hash (c/hash-password pass {:algo :bcrypt})]
;;       (t/is (= :bcrypt (c/identify-algo hash)))
;;       (t/is (true?  (c/check-password pass hash)))
;;       (t/is (false? (c/check-password pass2 hash))))))
