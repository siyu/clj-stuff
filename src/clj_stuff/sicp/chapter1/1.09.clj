(ns clj-stuff.sicp.chapter1)

;; recursive process
(defn my+ [a b]
  (if (= a 0)
    b
    (inc (my+ (dec a) b))))

(my+ 4 5)

;; iterative process
(defn my+ [a b]
  (if (= a 0)
    b
    (recur (dec a) (inc b))))

(my+ 4 5)


