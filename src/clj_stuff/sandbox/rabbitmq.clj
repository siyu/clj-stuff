(ns clj-stuff.sandbox.rabbitmq
  (:import (com.rabbitmq.client ConnectionFactory QueueingConsumer)))

(defn new-connection [host username password]
  (.newConnection
   (doto (ConnectionFactory.)
     (.setVirtualHost "/")
     (.setUsername username)
     (.setPassword password)
     (.setHost host))))

(def *rabbit-connection*)

(defmacro with-rabbit [[mq-host mq-username mq-password] & exprs]
  `(with-open [connection# (new-connection ~mq-host
                                           ~mq-username
                                           ~mq-password)]
     (binding [*rabbit-connection* connection#]
       (do ~@exprs))))

(defn send-message [routing-key message-object]
  (with-open [channel (.createChannel *rabbit-connection*)]
    (.basicPublish channel "" routing-key nil
                   (.getBytes (str message-object)))))

(defn delivery-from [channel consumer]
  (let [delivery (.nextDelivery consumer)]
    (.basicAck channel (.. delivery getEnvelope getDeliveryTag) false)
    (String. (.getBody delivery))))

(defn consumer-for [channel queue-name]
  (let [consumer (QueueingConsumer. channel)]
    (.queueDeclare channel queue-name false false false {})
    (.basicConsume channel queue-name consumer)
    consumer))

(defn next-message-from [queue-name]
  (with-open [channel (.createChannel *rabbit-connection*)]
    (let [consumer (consumer-for channel queue-name)]
      (delivery-from channel consumer))))
