(ns clj-stuff.sandbox.rabbitmq-sender
  (:use clj-stuff.sandbox.rabbitmq))

(println "Sending...")
(with-rabbit ["localhost" "guest" "guest"]
  (send-message "rabbit-test" "rabbit-test-method"))
(println "Done sending!")
