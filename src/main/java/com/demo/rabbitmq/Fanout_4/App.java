package com.demo.rabbitmq.Fanout_4;

import java.io.IOException;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class App {
	
	private final static String EXCHANGE_NAME = "test-fanout-exchange";
	private final static String QUEUE_NAME_1 = "test-fanout-queue-1";
	private final static String QUEUE_NAME_2 = "test-fanout-queue-2";
	
	public static void main(String[] args) throws InterruptedException {
		
		//Create Thread 1 as Producer and send messages to 'Exchange'
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
	 * Producer method to send messages to queue 
	 * The producer is made to sleep using Thread.sleep(100) to create a scenario as if the thread is processing message
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
		    	
		    	channel.queueDeclare(EXCHANGE_NAME, true, false, false, null);
		    		    	
		    		String message = "Broadcasting Message - Hello World";
		    		channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
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
