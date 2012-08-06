(ns clj-stuff.sandbox.rabbitmq-receiver-multi-1
  (:use clj-stuff.sandbox.rabbitmq))

(defn print-multi-msg []
  (loop [msg (next-message-from "rabbit-test")]
    (println "Message: " msg)
    (recur (next-message-from "rabbit-test"))))

(with-rabbit ["localhost" "guest" "guest"]
  (println "Waiting for messages...")
  (print-multi-msg))
