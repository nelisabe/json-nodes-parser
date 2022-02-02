package com.sber.jnp.app.exceptions;

public class IOErrorReadingJsonException extends RuntimeException {
	public IOErrorReadingJsonException() {
		super("I/O error while reading JSON file.");
	}
}
