package com.sber.jnp.app.exceptions;

/**
 * Exception occurs when invalid values was found in input json file.
 */
public class InvalidValueInJsonException extends RuntimeException {
	public InvalidValueInJsonException(String message) {
		super(message);
	}
}
