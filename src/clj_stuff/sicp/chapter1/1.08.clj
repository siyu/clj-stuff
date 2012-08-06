(ns clj-stuff.sicp.chapter1)

(defn square [x]
  (* x x))

(defn cube [x]
  (* x x x))

(defn good-enough? [guess x]
  (< (Math/abs (- (cube guess) x)) 0.001))

(defn improve [guess x]
  (/
   (+
    (/ x (square guess))
    (* 2 guess))
   3))

(defn cube-iter [guess x]
  (if (good-enough? guess x)
    guess
    (recur (improve guess x) x)))

(defn cube-root [x]
  (cube-iter 1.0 x))

(cube-root 5) ;; 1.7100597366002945
(cube (cube-root 5)) ;; 5.000735045685183
(cube (cube-root 79)) ;; 79.00000073112902
(cube (cube-root 0.5)) ;; 0.5000081682765313







