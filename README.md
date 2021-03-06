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

## Code Samples
1. **Hello World** - Created a queue (test-queue) using RabbitMQ Admin Interface and used AQMP helper classes to send and receive message from the queue.<br/>
Solution for error: Exception Error:channel is already closed due to channel error; protocol method: #method<channel.close>(reply-code=406, reply-text=PRECONDITION_FAILED - inequivalent arg 'durable' for queue 'test-queue' in vhost '/': received 'false' but current is 'true', class-id=50, method-id=10)<br/> 
https://stackoverflow.com/questions/31762563/issue-in-establishing-connection-with-rabbit-mq 
1. **Multi Thread AQMP - Hello World** - Created two threads t1 & t2 , where t1 is producer and t2 is consumer.<br/>
Output:<br/>
 [Thread 2:] Waiting for messages. To exit press CTRL+C<br/>
 [Thread 1] Sent: '[Java Client]: Hello World !'<br/>
 [Thread 2:] Received '[Java Client]: Hello World !'<br/>
1. **Work Queues** - Created one thread as producer to iterate and send multiple messages to queue & 2 consumer threads to listen to same queue.<br/>
Output:<br/>
[Consumer 2] Waiting for messages. To exit press CTRL+C<br/>
 [Consumer 1] Waiting for messages. To exit press CTRL+C<br/>
 [Producer] Sent: 'Message Id : [1]'<br/>
 [Consumer 2] Received 'Message Id : [1]'<br/>
 [Producer] Sent: 'Message Id : [2]'<br/>
 [Consumer 1] Received 'Message Id : [2]'<br/>
 [Producer] Sent: 'Message Id : [3]'<br/>
 [Producer] Sent: 'Message Id : [4]'<br/>
 [Producer] Sent: 'Message Id : [5]'<br/>
 [Consumer 2] Received 'Message Id : [3]'<br/>
 [Consumer 1] Received 'Message Id : [4]'<br/>
 [Consumer 2] Received 'Message Id : [5]'<br/>
 1. **Fanout** - Created an exchange **(test-fanout-exchange)** and binded two queues using RabbitMQ Admin interface <br/>
 To replicate Publish/Subscribe, created a producer thread which send message to 'Exchange' and two consumer thread listening to their own corresponding separate queues. <br/>
 Output: <br/>
 [Consumer 2] Waiting for messages at queue: [test-fanout-queue-2]. To exit press CTRL+C<br/>
 [Consumer 1] Waiting for messages at queue: [test-fanout-queue-1]. To exit press CTRL+C<br/>
 [Producer] Sent: 'Broadcasting Message - Hello World'<br/>
 [Consumer 2] Received message from [test-fanout-queue-2] :'Broadcasting Message - Hello World'<br/>
 [Consumer 1] Received message from [test-fanout-queue-1] :'Broadcasting Message - Hello World'<br/>
 1. **Direct Exchange**- Direct exchange **(test-direct-exchange)** configured using RabbitMQ admin console<br/>
	  test-direct-queue-1 binded to exchange for messages with routing key : **Severity_1**<br/>
	  test-direct-queue-2 binded to exchange for messages with routing key : **Severity_2 & Severity_3**<br/>
	  So when 4 messages send to exchange <br/>
	  Severity 1 -> send to queue 1<br/>
	  Severity 2 & Severity 3 -> Send to queue 2<br/>
	  Severity 4 message dropped<br/>
1. **Topics Exchange** - Topics exchange **(test-topics-exchange)** configured using RabbitMQ admin console<br/>
	  test-topics-queue-1 binded to exchange for messages with routing key :  *.1 <br/> 
	  test-topics-queue-2 binded to exchange for messages with routing key :  *.2.* and #.3  <br/>
	  So when 6 messages send to exchange with following routing key:<br/>
	  Severity.1 -> send to queue 1<br/>
	  Severity.2.Error & Severity.Log.Details.3 -> Send to queue 2<br/>
	  Severity.4, 3.Log.Severity, Data.Severity.1 - messages dropped<br/>
