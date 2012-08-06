(ns nplus1)

(def m [{:a "fn" :name "fannie" :c 1 :oas 100 :px 11}
        {:a "fn" :name "fannie" :c 1 :oas 200 :px 12}
        {:a "fn" :name "fannie" :c 2 :oas 100 :px 13}
        {:a "fn" :name "fannie" :c 2 :oas 200 :px 14}
        {:a "gn" :name "ginnie" :c 1 :oas 100 :px 15}
        {:a "gn" :name "ginnie" :c 1 :oas 200 :px 16}
        {:a "gn" :name "ginnie" :c 2 :oas 100 :px 17}
        {:a "gn" :name "ginnie" :c 2 :oas 200 :px 18}])


(defn group-by-keys [coll & kvs]
  "A nested version of group-by."
  (if-let [kv (first kvs)]
    (->>
     (group-by (fn [m] (select-keys m kv)) coll)
     (reduce (fn [ret [k v]]
               (assoc ret k (apply group-by-keys v (rest kvs))))
             {}))
      coll))

(defn- reduce-rows* [coll]
  (reduce (fn [c [k v]]
            (if (map? v)
              (conj c (merge {} k {:* (reduce-rows* v)}))
              (conj c k)))
          []
          coll))

(defn reduce-rows [coll & kvs]
  (->>
   (apply group-by-keys coll kvs)
   reduce-rows*
   (assoc {} :*)))
