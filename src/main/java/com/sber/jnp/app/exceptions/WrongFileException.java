package com.sber.jnp.app.exceptions;

public class WrongFileException extends RuntimeException {
	public WrongFileException(String message) {
		super("Wrong input file: " + message);
	}

	public WrongFileException(Exception exception) {
		super("Wrong input file: " + exception.getMessage());
	}
}
