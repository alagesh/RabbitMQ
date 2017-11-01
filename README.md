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

<br/><br/>
## Tutorials
1. **Hello World** - Created a queue (test-queue) using RabbitMQ Admin Interface and used AQMP helper classes to send and receive message from the queue.<br/>
Solution for error: Exception Error:channel is already closed due to channel error; protocol method: #method<channel.close>(reply-code=406, reply-text=PRECONDITION_FAILED - inequivalent arg 'durable' for queue 'test-queue' in vhost '/': received 'false' but current is 'true', class-id=50, method-id=10)
<br/> 
Change channel.queueDeclare(QUEUE_NAME, false, false, false, null) -> channel.queueDeclare(QUEUE_NAME, true, false, false, null);
<br/> 
https://stackoverflow.com/questions/31762563/issue-in-establishing-connection-with-rabbit-mq 

