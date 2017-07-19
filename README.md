`The irresponsible clojure guild presents...

# cryptic

Cryptography made palatable

## Usage

```clojure
(require '[irresponsible.cryptic :as c])

(c/check-password "foo" (c/hash-password "foo" {:algo :argon2id})) ;; => true
(c/check-password "bar" (c/hash-password "foo" {:algo :argon2id})) ;; => false
```

## Recommendations

These are our current recommendations. As cryptography is an active research area, these recommendations may be updated from time to time and you are encouraged to provide for algorithm rollover.

| Usage            | Preferred Algorithms (best first)   | Pure Java? | Extra dependencies required |
|------------------|-------------------------------------|------------|-----------------------------|
| Password hashing | `:argon2id`, `:argon2i`, `:argon2d` | No, JNA    | `[de.mkammerer/argon2-jvm "2.2"]` or  `[de.mkammerer/argon2-jvm-nolibs "2.2"]` (see notes section |
<!-- | Password hashing | `:bcrypt`                           | Yes        | `[org.mindrot/jbcrypt "0.4"]` | -->

cryptic was designed from the ground up to be pluggable. The clojure code which handles algorithms is fairly small, but the java libraries upon which they depend are a little bigger so we don't force a dependency on any of them. Pleae include the dependencies for the algorithms you need, bearing in mind that some of them use JNA (see also the notes section below)

## Notes

Argon2 support relies on [argon2-jvm](https://github.com/phxql/argon2-jvm) which uses JNA. Binaries are included in the `argon2-jvm` artifact for [these platforms](https://github.com/phxql/argon2-jvm#usage). You may depend on the (smaller) `argon2-jvm-nolibs` release which does not include binaries but this will require you to have `libargon2` installed on the target system. *if* you do this, beware that `:argon2id` is a newer algorithm and "stable" distributions may not ship a new enough libargon2 to support it.

Register functions catch any loading errors and return a vector of algorithms successfully loaded in preference order (my preference, what i consider sane defaults for general purpose use) so that you can easily choose the best of the available algorithms.

## Providing for Algorithm Rollover

In the event that weaknesses are discovered in one or more of the algorithms you are using, there are two scenarios with different mitigation strategies:

1. A total break; change algorithm and force reset all passwords
2. A security reduction; re-encrypt the user's password with a stronger algorithm when they next log in with code like this:

```clojure
(require '[irresponsible.cryptic :as c])

(declare update-user) ;; placeholder fn; go update the database or something

(defn rollover [username password hash]
  (let [algo (c/identify-algo hash)]
    (when (not= :argon2id algo)
	  (->> (c/hash-password password {:algo :argon2id})
	       (update-user username)))))
```

You should follow up with users who do not change their password over an appropriate period (say, a month) to encourage them to do so.

## Cryptographic settings

### Password Hashing

Argon2 family (`:argon2id`, `:argon2i`, `:argon2d`):

| Parameter      | Range | Default |
|----------------|-------|---------|
| `:time`        | ?     | 3       |
| `:memory`      | ?     | 12      |
| `:parallelism` | ?     | 1       |

<!-- BCrypt: -->

<!-- | Parameter      | Range | Default | -->
<!-- |----------------|-------|---------| -->
<!-- | `:work`        | 10-30 | 24      | -->

## Copyright and License

cryptic is copyright (c) 2017 James Laver

MIT LICENSE

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

