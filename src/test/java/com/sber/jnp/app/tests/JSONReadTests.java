package com.sber.jnp.app.tests;

import com.sber.jnp.app.*;
import com.sber.jnp.app.exceptions.InvalidArgumentsException;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

public class JSONReadTests {
	@Test()
	public void	WrongProgramArguments() {
		Throwable exception = assertThrows(
				InvalidArgumentsException.class, () -> {
					Main.main(new String[]{"--fileFile Test"});
				});
	}

}
