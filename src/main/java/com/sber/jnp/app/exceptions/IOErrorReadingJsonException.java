package com.sber.jnp.app.exceptions;

/**
 * Exception occurs when some IO error appeared while reading json file.
 */
public class IOErrorReadingJsonException extends RuntimeException {
	public IOErrorReadingJsonException(Exception exception) {
		super("I/O error while reading JSON file: " + exception.getMessage());
	}
}
