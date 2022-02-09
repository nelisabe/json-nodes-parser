package com.sber.jnp.app.exceptions;

public class IOErrorWritingJsonFileException extends RuntimeException {
	public IOErrorWritingJsonFileException(Exception exception) {
		super("I/O error writing JSON file: " + exception.getMessage());
	}
}
