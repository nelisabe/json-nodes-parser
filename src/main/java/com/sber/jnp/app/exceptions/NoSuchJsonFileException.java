package com.sber.jnp.app.exceptions;

public class NoSuchJsonFileException extends RuntimeException {
	public NoSuchJsonFileException() {
		super("Can't find JSON file in specified path.");
	}

}
