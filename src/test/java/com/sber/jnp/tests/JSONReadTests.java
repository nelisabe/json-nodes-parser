package com.sber.jnp.tests;

import com.sber.jnp.app.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class JSONReadTests {
	@Test
	public void SimpleJSON() {
		JSONHandler.read("simple.json");
	}
}
