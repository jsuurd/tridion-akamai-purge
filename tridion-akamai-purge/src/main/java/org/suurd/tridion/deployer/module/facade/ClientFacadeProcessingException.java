package org.suurd.tridion.deployer.module.facade;

/**
 * Exception thrown when a error is returned by the client used by a facade.
 * 
 * @author jsuurd
 */
public class ClientFacadeProcessingException extends RuntimeException {

	private static final long serialVersionUID = 505057004261648575L;

	/**
	 * Constructs a new client facade processing exception with the specified
	 * message.
	 * 
	 * @param message the message
	 */
	public ClientFacadeProcessingException(String message) {
		super(message);
	}

	/**
	 * Constructs a new client facade processing exception with the specified
	 * message and cause.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public ClientFacadeProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

}
