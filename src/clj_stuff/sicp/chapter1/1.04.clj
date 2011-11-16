(ns clj-stuff.sicp.chapter1)

(defn a-plus-abs-b [a b]
  ((if (> b 0) + -)) a b)

