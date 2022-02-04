package com.sber.jnp.app.exceptions;

public class NoJsonObjectReadException extends RuntimeException {
	public NoJsonObjectReadException() {
		super("No json object. Read some json object first!");
	}
}
