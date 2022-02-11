package com.sber.jnp.app.exceptions;

/**
 * Exception occurs when wrong path specified in {@link com.sber.jnp.app.JSONHandler}
 * method <b>getNode(String path)</b>.
 */
public class InvalidInternalJsonPathException extends RuntimeException {
	public InvalidInternalJsonPathException(String path) {
		super("can't find block by path: " + path);
	}
}
