# RabbitMQ


## Notes
* Message broker that implements AMQP (Advanced Message Queue Protocol)
* AMQP standardizes messaging using Producers, Broker and Consumers
* Producer -> Exchange -> Queue(s) -> Consumer(s)
* **Advantage:** Loose coupling and Scalability 
* Exchange types: 
  1. **Direct** - Exchange sends message to queues where routing key = binding key
  1. **Fanout** - Exchange broadcasts the message to all the queues it knows
  1. **topic** - Partial match of keys red.* will be send on red.green queue
  1. **headers** - message header used instead of routing key
* Default(nameless) exchange - Routing key is the queue name ( RabbitMQ specific) 
* Message Queue Vs Web Service: https://stackoverflow.com/questions/2383912/message-queue-vs-web-services


