package com.demo.rabbitmq.Topics_6;

import java.io.IOException;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class App {
	
	private final static String EXCHANGE_NAME = "test-topics-exchange";
	private final static String QUEUE_NAME_1 = "test-topics-queue-1";
	private final static String QUEUE_NAME_2 = "test-topics-queue-2";
	
	
	/*
	 * Configuration:
	 * ~~~~~~~~~~~~
	 * Topics exchange (test-topics-exchange) configured using RabbitMQ admin console
	 * test-topics-queue-1 binded to exchange for messages with routing key : *.1
	 * test-topics-queue-2 binded to exchange for messages with routing key : *.2.* & #.3
	 * So when 6 messages send to exchange with following routing key:
	 * Severity.1 -> send to queue 1
	 * Severity.2.Error & Severity.Log.Details.3 -> Send to queue 2
	 * Severity.4, 3.Log.Severity, Data.Severity.1 - messages dropped
	 *  
	 * Output:
	 * ~~~~~~~
	 *  [Consumer 1] Waiting for messages at queue: [test-topics-queue-1]. To exit press CTRL+C
	 * [Consumer 2] Waiting for messages at queue: [test-topics-queue-2]. To exit press CTRL+C
 	 * [Producer] Sent: 'Sev 1 - Database down'
 	 * [Producer] Sent: 'Sev 1 - Server not responding'
 	 * [Producer] Sent: 'Sev 2 - Create customer reports on overdraft'
 	 * [Producer] Sent: 'Sev 3 - Page not loading on IE'
 	 * [Producer] Sent: 'Sev 3 - Page not aligned on IE'
 	 * [Producer] Sent: 'Sev 4 - Change background to Red'
 	 * [Consumer 2] Received message from [test-topics-queue-2] :'Sev 2 - Create customer reports on overdraft'
 	 * [Consumer 1] Received message from [test-topics-queue-1] :'Sev 1 - Database down'
 	 * [Consumer 2] Received message from [test-topics-queue-2] :'Sev 3 - Page not loading on IE'
	 */
	public static void main(String[] args) throws InterruptedException {
		
		//Create Thread 1 as Producer and send messages to 'Topic Exchange'
		Thread t1 = new Thread(new Runnable() {

			public void run() {						
				producer();			
			}			
		});
		
		
		
		
		//Create  2 thread's as Consumer who are constantly listening to the queue
		
		Thread t2 = new Thread(new Runnable() {

			public void run() {
				consumer("Consumer 1",QUEUE_NAME_1);
			}
			
		});
		
		Thread t3 = new Thread(new Runnable() {

			public void run() {
				consumer("Consumer 2",QUEUE_NAME_2);
			}
			
		});
		
		// Start both threads
		t1.start();		
		t2.start();
		t3.start();
		
		//Wait for completion of the thread
		
		 t1.join();
	     t2.join();
	     t3.join();
	     
	    
	}
	
	/*
	 * Producer method to send messages to topic exchange
	 * Producer sends message with routing key and exchange routes the messages to corresponding queues
	 * Routing key | Queue
	 * Severity.1 -> send to queue 1
	 * Severity.2.Error & Severity.Log.Details.3 -> Send to queue 2
	 * Severity.4, 3.Log.Severity, Data.Severity.1 - messages dropped
	 */
	public static void producer() {
		
		Connection connection=null;
    	Channel channel=null;
    	
    	try {
				// Step 1. Create connection to the server
		    	
		    	ConnectionFactory factory = new ConnectionFactory();
		    	factory.setHost("localhost");
		    	connection = factory.newConnection();
		    	channel = connection.createChannel();
		    	
		    	
		    	// Step 2. Send a message to queue
		    	
		    	//Declare exchange type
		    	channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		    		    	
		    	String message = "Sev 1 - Database down";		    		
		    	channel.basicPublish(EXCHANGE_NAME, "Severity.1", null, message.getBytes());
		    	System.out.println(" [Producer] Sent: '" + message + "'");
		    	
		    	message = "Sev 1 - Server not responding" ;
		    	channel.basicPublish(EXCHANGE_NAME, "Data.Severity.1", null, message.getBytes());
		    	System.out.println(" [Producer] Sent: '" + message + "'");
		    	
		    		
		    	message = "Sev 2 - Create customer reports on overdraft" ;
		    	channel.basicPublish(EXCHANGE_NAME, "Severity.2.Error", null, message.getBytes());
		    	System.out.println(" [Producer] Sent: '" + message + "'");
		    		
		    	message = "Sev 3 - Page not loading on IE" ;
		    	channel.basicPublish(EXCHANGE_NAME, "Severity.Log.Details.3", null, message.getBytes());
		    	System.out.println(" [Producer] Sent: '" + message + "'");
		    		
		    	message = "Sev 3 - Page not aligned on IE" ;
		    	channel.basicPublish(EXCHANGE_NAME, "3.Log.Severity", null, message.getBytes());
		    	System.out.println(" [Producer] Sent: '" + message + "'");
		    	
		    	message = "Sev 4 - Change background to Red" ;
		    	channel.basicPublish(EXCHANGE_NAME, "Severity.4", null, message.getBytes());
		    	System.out.println(" [Producer] Sent: '" + message + "'");
		    	
		  
		    	}catch(Exception e) {
		    		System.out.println("Exception Error:"+ e.getMessage());
		    	}finally {
		    		try {
		    	    	// Step 3.  Finally close connections
		    	    		channel.close();
		    	    		connection.close();
		    	    	}catch(Exception e) {
		    	    		System.out.println("Exception Error:"+ e.getMessage());
		    	    	}
		    	}
		}
	
	/*
	 * Consumer method to listen to queue and consume messages
	 * The consumer is made to sleep using Thread.sleep(1000) to create a scenario as if the thread is processing message
	 */
	
	public static void consumer(final String thread_name, final String queue_name) {
		
		try {
			// Step 1. Create connection to the server
	    	
	    	ConnectionFactory factory = new ConnectionFactory();
	    	factory.setHost("localhost");
	    	Connection connection = factory.newConnection();
	    	Channel channel = connection.createChannel();
	    	
	    	// Step 2: Listen for messages in queue
	    	
	    	
	    	channel.queueDeclare(queue_name, true, false, false, null);
	    	System.out.println(" ["+thread_name+"] Waiting for messages at queue: ["+queue_name+"]. To exit press CTRL+C");
	    	
	    	//Step 3: Create a default Consumer and receive messages asynchronously 
	    	
	    	Consumer consumer = new DefaultConsumer(channel) {
	    	      @Override
	    	      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
	    	          throws IOException {
	    	        String message = new String(body, "UTF-8");
	    	        System.out.println(" ["+thread_name+"] Received message from ["+queue_name+"] :'" + message + "'");
	    	      }
	    	    };
	    	   
	         channel.basicConsume(queue_name, true, consumer);
	    	    
			}catch(Exception e) {
	    		System.out.println("Exception Error:"+ e.getMessage());
	    	}
	}

}
