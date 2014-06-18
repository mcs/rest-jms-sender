package de.haspa.hsf.jms.sender.control;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

@Stateless
public class JmsSender {

	private final Logger LOGGER = Logger.getLogger(JmsSender.class.getName());

	@Resource(lookup = "jms/JmsSenderConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(lookup = "jms/JmsSenderQueue")
	private Queue queue;

	/**
	 * Fuegt eine Nachricht zur definierten Queue hinzu und gibt die MessageID
	 * der Nachricht zurueck.
	 *
	 * @param message
	 *            die hinzuzufuegende Nachricht
	 * @return die MessageID der Nachricht, oder null falls das Hinzufuegen
	 *         fehlschlug
	 */
	public String addToQueue(String message) {
		try {
			Connection connection = connectionFactory.createConnection();
			Session session;
			session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(queue);

			TextMessage textMessage = session.createTextMessage();
			textMessage.setText(message);

			LOGGER.info("MessageProducer.send() wird gleich aufgerufen.");
			producer.send(textMessage);
			LOGGER.info("MessageProducer.send() wurde aufgerufen.");
			return textMessage.getJMSMessageID();
		} catch (JMSException e) {
			LOGGER.log(Level.SEVERE, "JMS-Queue konnte nicht befuellt werden.", e);
		}
		return null;
	}
}
