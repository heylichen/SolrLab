package com.heylichen.amq.jmsbasic.p2p;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lichen2 on 2016/6/1.
 */
public class MySyncMessageConsumer implements Runnable {

  private static final Logger logger = LoggerFactory.getLogger(MySyncMessageConsumer.class);

  public void run() {
    Connection connection = null;
    Session session = null;
    MessageConsumer consumer = null;
    try {
      // Create a ConnectionFactory
      ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

      // Create a Connection
      connection = connectionFactory.createConnection();
      connection.start();

      // Create a Session
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      // Create the destination (Topic or Queue)
      Destination destination = session.createQueue("TEST.FOO");
      consumer = session.createConsumer(destination);
      TextMessage message = (TextMessage) consumer.receive(1000);

      // Tell the producer to send the message
      logger.info("received message: " + message.hashCode() + " : " + message.getText());
    } catch (Exception e) {
      System.out.println("Caught: " + e);
      e.printStackTrace();
    } finally {
      try {
        // Clean up
        if (consumer != null) {
          consumer.close();
        }
        if (session != null) {
          session.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
