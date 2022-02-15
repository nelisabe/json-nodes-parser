package com.sber.jnp.app.tests;

public class InternalTestErrorException extends RuntimeException {
	public InternalTestErrorException(Exception exception) {
		super(exception);
	}
}
