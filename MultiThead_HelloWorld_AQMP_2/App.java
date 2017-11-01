package com.demo.rabbitmq.MultiThead_HelloWorld_AQMP_2;

import java.io.IOException;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class App {
	
	private final static String QUEUE_NAME = "test-queue";
	
	public static void main(String[] args) throws InterruptedException {
		
		//Create Thread 1 as Producer
		Thread t1 = new Thread(new Runnable() {

			public void run() {
				
				Connection connection=null;
		    	Channel channel=null;
		    	
		    	
		    	try {
				// Step 1. Create connection to the server
		    	
		    	ConnectionFactory factory = new ConnectionFactory();
		    	factory.setHost("localhost");
		    	connection = factory.newConnection();
		    	channel = connection.createChannel();
		    	
		    	// Step 2. Send a message to queue
		    	
		    	channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		    	String message = "[Java Client]: Hello World !";
		    	channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		    	System.out.println(" [Thread 1] Sent: '" + message + "'");
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
		});
		
		
		//Create Thread 2 as Consumer who is constantly listening on the queue
		
		Thread t2 = new Thread(new Runnable() {

			public void run() {
				try {
				// Step 1. Create connection to the server
		    	
		    	ConnectionFactory factory = new ConnectionFactory();
		    	factory.setHost("localhost");
		    	Connection connection = factory.newConnection();
		    	Channel channel = connection.createChannel();
		    	
		    	// Step 2: Listen for messages in queue
		    	
		    	
		    	channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		    	System.out.println(" [Thread 2:] Waiting for messages. To exit press CTRL+C");
		    	
		    	//Step 3: Create a default Consumer and receive messages asynchronously 
		    	
		    	Consumer consumer = new DefaultConsumer(channel) {
		    	      @Override
		    	      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
		    	          throws IOException {
		    	        String message = new String(body, "UTF-8");
		    	        System.out.println(" [Thread 2:] Received '" + message + "'");
		    	      }
		    	    };
		    	    channel.basicConsume(QUEUE_NAME, true, consumer);
				}catch(Exception e) {
		    		System.out.println("Exception Error:"+ e.getMessage());
		    	}
			}
			
		});
		
		// Start both threads
		t1.start();		
		t2.start();
		
		 t1.join();
	     t2.join();
	}

}
