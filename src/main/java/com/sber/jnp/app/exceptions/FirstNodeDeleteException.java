package com.sber.jnp.app.exceptions;

public class FirstNodeDeleteException extends RuntimeException {
	public FirstNodeDeleteException(String nodeName) {
		super("Cant delete first node " + nodeName);
	}
}
