package com.sber.jnp.app.exceptions;

public class InvalidInternalJsonPathException extends RuntimeException {
	public InvalidInternalJsonPathException(String path) {
		super("can't find block by path: " + path);
	}
}
