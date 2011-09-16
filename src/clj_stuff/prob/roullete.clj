(ns clj-stuff.prob.roullete)

(defn play-roullete
  [{:keys [stake prev-hand-won prev-bet-amt]}]
  (let [curr-bet-amt (if prev-hand-won 1 (* 2 prev-bet-amt))
        curr-hand-won (< (rand-int 38) 18)
        stake (if curr-hand-won (+ stake curr-bet-amt) (- stake curr-bet-amt))]
    {:stake stake :prev-hand-won curr-hand-won :prev-bet-amt curr-bet-amt}))

(defn play-roullete-limit-and-won []
  (->> (iterate play-roullete {:stake 0 :prev-hand-won true :prev-bet-amt 1})
       (take-while (fn [{stake :stake}]
                     (and (<= stake 5)
                          (>= stake -100))))
       last
       :stake
       (< 0 )))

(defn play-roullete-limit-win-prob
  [num-trials]
  (let [coll (take num-trials (repeatedly play-roullete-limit-and-won))
        num-won (count (filter true? coll))]
    (/ num-won num-trials)))

(play-roullete-limit-win-prob 10000)
