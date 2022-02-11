package com.sber.jnp.app.exceptions;

/**
 * Exception occurs when user tries to save json file before reading some, i.e
 * there is no json equivalent object to save.
 */
public class NoJsonObjectReadException extends RuntimeException {
	public NoJsonObjectReadException() {
		super("No json object. Read some json object first!");
	}
}
