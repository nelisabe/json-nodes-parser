package com.sber.jnp.app.exceptions;

/**
 * Exception occurs when some IO error appeared while writing json file.
 */
public class IOErrorWritingJsonFileException extends RuntimeException {
	public IOErrorWritingJsonFileException(Exception exception) {
		super("I/O error writing JSON file: " + exception.getMessage());
	}
}
