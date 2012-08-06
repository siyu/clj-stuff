(ns clj-stuff.sandbox.rabbitmq-receiver
  (:use clj-stuff.sandbox.rabbitmq))

(println "waiting..")
(with-rabbit ["localhost" "guest" "guest"]
  (println (next-message-from "rabbit-test")))
