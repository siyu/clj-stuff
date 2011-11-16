(ns clj-stuff.sicp.chapter1)

(defn max2 [a b c]
  (let [coll [a b c]
        sq (fn [x] (* x x))]
    (- (apply + (map sq coll)) (sq (apply min coll)))))

(max2 2 3 4)
;;25
(max2 2 4 3)
;;25
(max2 3 2 4)
;;25
(max2 3 4 2)
;;25
(max2 4 3 2)
;;25
(max2 4 2 3)
;;25
(max2 1 1 1)
;;2
(max2 -1 -2 -3)
;;-3

