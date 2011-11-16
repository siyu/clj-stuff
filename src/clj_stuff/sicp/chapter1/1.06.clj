(ns clj-stuff.sicp.chapter1)

(defn new-if [pred then else]
  (cond pred then
        :else else))

(new-if (= 2 3) 0 5)
;;5
(new-if (= 1 1) 0 5)
;;0

;; (def sqrt-iter [guess x]
;;   (new-if (good-enough? guess x)
;;           guess
;;           (sqrt-iter (improve guess x) x)))
;;
;; since new-if is a user-defined fn
;; it's arguements will be evaluated before passing into the body
;; so the third argument above would cause infinite recursion

