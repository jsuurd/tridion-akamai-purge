package org.suurd.tridion.deployer.module.facade;

public class ClientFacadeProcessingException extends RuntimeException {

	private static final long serialVersionUID = 505057004261648575L;

	public ClientFacadeProcessingException(String message) {
		super(message);
	}

	public ClientFacadeProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

}
