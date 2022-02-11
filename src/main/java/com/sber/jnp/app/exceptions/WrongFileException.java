package com.sber.jnp.app.exceptions;

/**
 * Exception occurs when input file is read, but have not equal to json etc.
 */
public class WrongFileException extends RuntimeException {
	public WrongFileException(String message) {
		super("Wrong input file: " + message);
	}

	public WrongFileException(Exception exception) {
		super("Wrong input file: " + exception.getMessage());
	}
}
