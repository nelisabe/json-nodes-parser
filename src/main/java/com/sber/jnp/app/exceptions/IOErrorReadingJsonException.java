package com.sber.jnp.app.exceptions;

public class IOErrorReadingJsonException extends RuntimeException {
	public IOErrorReadingJsonException(Exception exception) {
		super("I/O error while reading JSON file: " + exception.getMessage());
	}
}
