package com.sber.jnp.app.exceptions;

/**
 * Exception occurs when user tries to create {@link com.sber.jnp.app.Node}
 * object with invalid values.
 */
public class InvalidNodeValueException extends RuntimeException {
	public InvalidNodeValueException(String message) {
		super(message);
	}
}
