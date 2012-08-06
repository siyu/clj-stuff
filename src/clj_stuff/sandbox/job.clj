(ns clj-stuff.sandbox.job
  (:require [clojure.pprint :as pp]
            [clj-stuff.util.err :as err]))

(defn- prep-ctx
  [run-ctx job-spec]
  (when (get-in run-ctx [:job-ctxs (:name job-spec)])
    (throw (Exception. (str "Duplicate job name: " (:name job-spec)))))
  (let [[run-ctx job-ctx] ((get job-spec :prep-ctx #(vector %1 %2)) run-ctx {:job-spec job-spec})
        run-ctx (assoc-in run-ctx [:job-ctxs (:name job-spec)] job-ctx)]
    (reduce prep-ctx run-ctx (:children job-spec))))

(defn- retry-job?
  [job-ctx]
  (not
   (or (job-ctx :complete?)
       (zero? (get-in job-ctx [:job-spec :retry-cnt])))))

(defn- run-children
  [run-ctx job-ctx]
  [run-ctx job-ctx])

(defn- run-job
  [run-ctx job-ctx]
  (println "run-job: " (get-in job-ctx [:job-spec :name]))
  (->> {:run-ctx run-ctx :job-ctx job-ctx}
       (iterate (fn [{:keys [run-ctx job-ctx]}]
                  (let [_ (println "here")
                        name (get-in job-ctx [:job-spec :name])
                        children (get-in job-ctx [:job-spec :children])
                        run-fn (if children
                                 (partial run-children)
                                 (get-in job-ctx [:job-spec :run]))
                        _ (println (when children "I have children"))
                        [[run-ctx job-ctx] e]
                        (err/with-try-catch [run-ctx job-ctx] (run-fn run-ctx job-ctx))]
                    (if e
                      (println "run-jobs: " name ": exception: " e)
                      (println "run-jobs: " name ": complete?: " (:complete? job-ctx)))
                    (when (retry-job? job-ctx)
                      (Thread/sleep (* (or (get-in job-ctx [:job-spec :retry-sleep-in-min] 1) 60 1000))))
                    {:run-ctx (assoc-in run-ctx [:job-spec name] job-ctx)
                     :job-ctx job-ctx})))
       (take-while (fn [{:keys [job-ctx]}] (retry-job? job-ctx)))
       last))

(defn- run-children [run-ctx job-ctx]
  (let [job-ctxs (map #((:job-ctxs run-ctx) (:name %)) (-> job-ctx :job-spec :children))
;        _ (pp/pprint j-ctxs)
        [run-ctx _] (reduce (fn [[run-ctx run? :as args] jj-ctx]
                              (if (and  run? (-> jj-ctx :complete? not))
                                (let [[run-ctx jj-ctx] (run-job run-ctx jj-ctx)]
                                  [run-ctx
                                   (or (:complete? jj-ctx) (-> job-ctx :job-spec :continue-on-incomplete-child))])
                                args))
                            [run-ctx true] job-ctxs)]
    [run-ctx (assoc job-ctx :complete? (every? #(-> % :name ((:job-ctxs run-ctx)) :complete?) (-> job-ctx :job-spec :children)))]))



(defn run
  [job-spec]
  (let [run-ctx (prep-ctx {} job-spec)
        [run-ctx job-ctx] (run-job run-ctx ((:job-ctxs run-ctx) (:name job-spec)))
        ]
    (pp/pprint run-ctx)))

(comment
  (run {:name :root :run (fn [run-ctx job-ctx] [run-ctx (assoc job-ctx :complete? true)])})
  (run {:name :root :run run-children})
  (run {:name :root :run run-children
        :children [{:name :c1 :run (fn [run-ctx j-ctx] (println "c1") [run-ctx (assoc j-ctx :complete? true)])}]})
  (run {:name :root :retry-cnt 1 :run run-children
        :children [{:name :c1 :run (fn [run-ctx j-ctx] (println "c1") (throw (Exception. "dreadful")))}]})
  (run {:name :root :run run-children
        :children [{:name :c1 :run (fn [run-ctx j-ctx] (println "c1") [run-ctx (assoc j-ctx :complete? true)])}
                   {:name :c2 :run (fn [run-ctx j-ctx] (println "c2") [run-ctx (assoc j-ctx :complete? true)])}]})
  (run {:name :root :retry-cnt 1 :retry-sleep-in-min 1/60 :continue-on-incomplete-child true :run run-children
        :children [{:name :c1 :run (fn [run-ctx j-ctx] (println "c1") (throw (Exception. "dreadful")))}
                   {:name :c2 :run (fn [run-ctx j-ctx] (println "c2") [run-ctx (assoc j-ctx :complete? true)])}]})
  )
