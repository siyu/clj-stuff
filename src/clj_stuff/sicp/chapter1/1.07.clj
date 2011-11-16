(ns clj-stuff.sicp.chapter1)

;; Newton's method of successive approximations:
;; guess y for the value of the square root of a number x,
;; we can perform a simple manipulation to get a better guess by averaging y with x/y
(defn avg [x y]
  (/ (+ x y) 2))

(defn square [x]
  (* x x))

(defn good-enough? [guess x]
  (< (Math/abs (- (square guess) x)) 0.001))

(defn improve [guess x]
  (avg guess (/ x guess)))

(defn sqrt-iter [guess x]
  (if (good-enough? guess x)
    guess
    (recur (improve guess x) x)))

(defn sqrt [x]
  (sqrt-iter 1.0 x))

(sqrt 9)
(sqrt (+ 100 37))
(sqrt (+ (sqrt 2) (sqrt 3)))
(square (sqrt 1000))