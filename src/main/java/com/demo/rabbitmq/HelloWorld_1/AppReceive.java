package com.demo.rabbitmq.HelloWorld_1;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;



/**
 * Receive Message from queue : test-queue
 *
 */
public class AppReceive 
{
	
	private final static String QUEUE_NAME = "test-queue";

	
    public static void main( String[] args ) throws Exception
    {
       		
    	// Step 1. Create connection to the server
    	
    	ConnectionFactory factory = new ConnectionFactory();
    	factory.setHost("localhost");
    	Connection connection = factory.newConnection();
    	Channel channel = connection.createChannel();
    	
    	// Step 2: Listen for messages in queue
    	
    	
    	channel.queueDeclare(QUEUE_NAME, true, false, false, null);
    	System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    	
    	//Step 3: Create a default Consumer and receive messages asynchronously 
    	
    	Consumer consumer = new DefaultConsumer(channel) {
    	      @Override
    	      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
    	          throws IOException {
    	        String message = new String(body, "UTF-8");
    	        System.out.println(" [x] Received '" + message + "'");
    	      }
    	    };
    	    channel.basicConsume(QUEUE_NAME, true, consumer);
    	
    }
 }

