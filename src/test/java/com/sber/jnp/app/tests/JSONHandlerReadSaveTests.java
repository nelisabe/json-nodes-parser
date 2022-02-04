package com.sber.jnp.app.tests;

import com.sber.jnp.app.*;
import com.sber.jnp.app.exceptions.IOErrorReadingJsonException;

import com.sber.jnp.app.exceptions.NoJsonObjectReadException;
import com.sber.jnp.app.exceptions.WrongFileException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class JSONHandlerReadSaveTests {
	@Test
	public void WrongFile() {
		assertThrows(IOErrorReadingJsonException.class, () -> {
			JSONHandler jsonHandler = new JSONHandler();
			jsonHandler.read(createRandomJsonName());
		});
		assertThrows(WrongFileException.class, () -> {
			JSONHandler jsonHandler = new JSONHandler();
			jsonHandler.read("json.wr");
		});
	}

	@Test
	public void	WrongFileContent() throws IOException {
		final String jsonFileName = createJsonFile("\n" +
				"{\n" +
				"  \"name\" \"Name 1-1-2\",\n" +
				"  \"color\": \"Green\",\n" +
				"  \"value\": 33,\n" +
				"  \"children\": []\n" +
				"}");
		assertThrows(WrongFileException.class, () -> {
			JSONHandler jsonHandler = new JSONHandler();
			jsonHandler.read(jsonFileName);
		});
		Files.deleteIfExists(Paths.get(jsonFileName));
	}

	@Test
	public void	EmptyFile() throws IOException {
		final String jsonFileName = createJsonFile("");
		assertThrows(WrongFileException.class, () -> {
			JSONHandler jsonHandler = new JSONHandler();
			jsonHandler.read(jsonFileName);
		});
		Files.deleteIfExists(Paths.get(jsonFileName));

		final String jsonFileName2 = createJsonFile("\n\n");
		assertThrows(WrongFileException.class, () -> {
			JSONHandler jsonHandler = new JSONHandler();
			jsonHandler.read(jsonFileName2);
		});
		Files.deleteIfExists(Paths.get(jsonFileName2));
	}

	private String	createRandomJsonName() {
		return ((int)(Math.random() * 1000000)) + ".json";
	}

	private String	createJsonFile(String content) throws IOException {
		String	fileName;

		fileName = createRandomJsonName();
		try {
			Files.createFile(Paths.get(fileName));
		} catch (FileAlreadyExistsException ignore) {
			return createJsonFile(content);
		}
		PrintWriter out = new PrintWriter(fileName);
		out.println(content);
		out.close();
		return fileName;
	}

	@Test
	public void	SaveBeforeRead() {
		assertThrows(NoJsonObjectReadException.class, () -> {
			JSONHandler jsonHandler = new JSONHandler();
			jsonHandler.save("some.json");
		});
	}
}
