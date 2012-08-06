(ns clj-stuff.util.err)

(defmacro with-try-catch
  "Runs body in a try/catch returning result of body
   and nil in a vector.  On exception, returns
   err-result and the exception in a vector."
  [err-result & body]
  `(try
     [(do ~@body) nil]
     (catch Exception e# [~err-result e#])))

(defmacro with-dflt
  "Runs body in a try/catch returning result of body
   if no exception otherwise default"
  [default & body]
  `(try
     (do ~@body)
     (catch Exception _# ~default)))

(defmacro ignore-exception
  "Returns the result of evaluating body
   or nil if exception."
  [& body]
  `(with-dflt nil ~@body))
