package com.sber.jnp.app.exceptions;

public class InvalidArgumentsException extends RuntimeException {
	public InvalidArgumentsException(Exception exception) {
		super("Invalid arguments: " + exception.getMessage());
	}
}
