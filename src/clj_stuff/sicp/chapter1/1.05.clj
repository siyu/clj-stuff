(ns clj-stuff.sicp.chapter1)

(defn p [] (p))

(defn testx [x y]
  (if (= x 0)
    0
    y))

(test 0 (p))

;; normal-order Evaluation would return 0
;; applicative-order Evaluation would be stuck on expanding (p) infinitely
