(ns nplus1)

(def m [{:a "fn" :name "fannie" :c 1 :oas 100 :px 11}
        {:a "fn" :name "fannie" :c 1 :oas 200 :px 12}
        {:a "fn" :name "fannie" :c 2 :oas 100 :px 13}
        {:a "fn" :name "fannie" :c 2 :oas 200 :px 14}
        {:a "gn" :name "ginnie" :c 1 :oas 100 :px 15}
        {:a "gn" :name "ginnie" :c 1 :oas 200 :px 16}
        {:a "gn" :name "ginnie" :c 2 :oas 100 :px 17}
        {:a "gn" :name "ginnie" :c 2 :oas 200 :px 18}])

(defn group-by*
  "group-by that maintains order"
  [f coll]
  (reduce
    (fn [ret x]
      (let [k (f x)]
        (assoc ret k (conj (get ret k []) x))))
    (array-map) coll))

(defn group-by-keys [sm coll]
  "A nested version of group-by."
  (if sm
    (let [{:keys [pk ks children]} sm]
      (->>
       (group-by* (fn [m] (select-keys m ks)) coll)
       (reduce (fn [ret [k v]]
                 (assoc ret k (for [c children] (group-by-keys c v))))
               {})
       (assoc {} pk)))
    coll))

(defn- reduce-rows* [coll]
  (reduce (fn [c [k v]]
            (->>
             (for [[kk vv] v]
               (if (map? kk)
                 (apply merge kk (map reduce-rows* vv) )
                 kk))
             (assoc c k)))
          {}
          coll))

(defn reduce-rows [sm coll]
  (->>
   (group-by-keys sm coll)
   reduce-rows*))
