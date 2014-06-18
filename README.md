rest-jms-sender
===============

Small JavaEE 6 Webapp which allows to add text messages to a JMS queue.

This one runs fine on WebSphere 8. It is not (yet) tested on other application servers like JBoss / Wildfly, Glassfish etc.

The motivation for this project is to explore transactional JMS behaviour within an EJB. I use WebSphere's internal SIB for Messaging, and the result is still confusing for me: Messages make it into the queue only after a surrounding transaction is commited. That means that other applications cannot access the JMS message from the queue as long as the EJB's transaction has ended.

For installation, you need:
- a ConnectionFactory with JNDI name "jms/JmsSenderConnectionFactory" 
- a JMS queue with JNDI name "jms/JmsSenderQueue"

Use a REST tool to POST to a URL like this:
```
http://localhost:10039/contextPath/rest/send/ta
```
Add a POST body like "Test message" and a header property "sleepSeconds" to the request with a value like 30 to make the REST resource wait for 30 seconds until sending back a response (and commit its transaction). In the meanwhile, check the destination queue for new messages (I did so by refreshing the queue points within WebSphere's messaging engine).

