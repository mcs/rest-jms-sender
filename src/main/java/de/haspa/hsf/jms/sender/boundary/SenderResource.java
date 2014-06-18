package de.haspa.hsf.jms.sender.boundary;

import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.haspa.hsf.jms.sender.control.JmsSender;

@Stateless
@Path("send")
public class SenderResource {

	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	@EJB
	private JmsSender jmsSender;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String info(@QueryParam("name") String name) {
		return "Add /ta or /nta to path for transactional and non-transactional JMS test.";
	}

	@POST
	@Path("ta")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response addToQueue(String message,
			@HeaderParam("sleepSeconds") Integer sleepSeconds) {
		if (message != null && !message.trim().isEmpty()) {
			LOGGER.info("Versuche, TextMessage zu queuen: " + message);
			String messageID = jmsSender.addToQueue(message);
			LOGGER.info("TextMessage " + messageID + " sollte in Queue gelandet sein.");
			if (sleepSeconds != null) {
				try {
					Thread.sleep(sleepSeconds * 1000);
				} catch (InterruptedException e) {
					// should never happen
					LOGGER.severe("Oops, Sleep wurde unterbrochen!");
				}
			}
			return Response.ok("Text in Queue eingestellt: " + message).build();
		}
		return Response.notModified("Kein Text hinzugefuegt, da keiner uebergeben wurde.").build();
	}

	@POST
	@Path("nta")
	@Consumes(MediaType.TEXT_PLAIN)
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Response nonTransactionalAddToQueue(String message,
			@HeaderParam("sleepSeconds") Integer sleepSeconds) {
		return addToQueue(message, sleepSeconds);
	}
}
