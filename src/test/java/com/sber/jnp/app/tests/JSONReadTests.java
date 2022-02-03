package com.sber.jnp.app.tests;

import com.sber.jnp.app.*;
import com.sber.jnp.app.exceptions.IOErrorReadingJsonException;
import com.sber.jnp.app.exceptions.InvalidArgumentsException;

import com.sber.jnp.app.exceptions.WrongFileException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class JSONReadTests {
	@Test
	public void	WrongProgramArguments() {
		assertThrows(
				InvalidArgumentsException.class, () -> {
					Main.main(new String[]{});
				});
		assertThrows(
				InvalidArgumentsException.class, () -> {
						Main.main(new String[]{""});
				});
		assertThrows(
				InvalidArgumentsException.class, () -> {
					Main.main(new String[]{"--jsonFile"});
				});
		assertThrows(
				InvalidArgumentsException.class, () -> {
					Main.main(new String[]{"--File"});
				});
		assertThrows(
				InvalidArgumentsException.class, () -> {
					Main.main(new String[]{"--File File File"});
				});
	}
	@Test
	public void WrongFile() {
		assertThrows(IOErrorReadingJsonException.class, () -> {
			Main.main(new String[]{"--jsonFile NoSuchFile.json"});
		});
		assertThrows(WrongFileException.class, () -> {
			Main.main(new String[]{"--jsonFile Wrong.json"});
		});
		assertThrows(WrongFileException.class, () -> {
			Main.main(new String[]{"--jsonFile json.wr"});
		});
	}
}
