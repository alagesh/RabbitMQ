package com.demo.rabbitmq.HelloWorld_1;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import java.io.IOException;

import com.rabbitmq.client.Channel;

/**
 * Send Message -[Java Client]: Hello World! to test-queue
 *
 */
public class AppSend 
{
	
	private final static String QUEUE_NAME = "test-queue";

	
    public static void main( String[] args )
    {
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
    	System.out.println(" [x] Sent: '" + message + "'");
    	    	
    	}catch(IOException e) {    		
    		System.out.println("IOException Error:"+ e.getMessage());
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
 }

