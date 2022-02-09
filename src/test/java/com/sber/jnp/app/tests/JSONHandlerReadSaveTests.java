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
import java.util.Iterator;

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
		final String jsonFile = createJsonFile("\n" +
				"{\n" +
				"  \"name\" \"Name 1-1-2\",\n" +
				"  \"color\": \"Green\",\n" +
				"  \"value\": 33,\n" +
				"  \"children\": []\n" +
				"}");
		assertThrows(WrongFileException.class, () -> {
			JSONHandler jsonHandler = new JSONHandler();
			jsonHandler.read(jsonFile);
		});
		Files.deleteIfExists(Paths.get(jsonFile));
	}

	@Test
	public void	EmptyFile() throws IOException {
		final String jsonFile = createJsonFile("");
		assertThrows(WrongFileException.class, () -> {
			JSONHandler jsonHandler = new JSONHandler();
			jsonHandler.read(jsonFile);
		});
		Files.deleteIfExists(Paths.get(jsonFile));

		final String jsonFile2 = createJsonFile("\n\n");
		assertThrows(WrongFileException.class, () -> {
			JSONHandler jsonHandler = new JSONHandler();
			jsonHandler.read(jsonFile2);
		});
		Files.deleteIfExists(Paths.get(jsonFile2));
	}

	@Test
	public void	SaveBeforeRead() {
		assertThrows(NoJsonObjectReadException.class, () -> {
			JSONHandler jsonHandler = new JSONHandler();
			jsonHandler.save("some.json");
		});
	}

	@Test
	public void	IteratorSmallTree() throws IOException {
		StringBuilder	stringBuilder = new StringBuilder();
		JSONHandler		jsonHandler = new JSONHandler();
		Iterator<Node>	iterator;
		String			jsonFile = createJsonFile("{\n" +
				"  \"name\": \"A\",\n" +
				"  \"color\": \"Blue\",\n" +
				"  \"value\": 22,\n" +
				"  \"children\": []\n" +
				"}");

		jsonHandler.read(jsonFile);
		iterator = jsonHandler.getIterator();
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().getName());
		}
		assertEquals("A", stringBuilder.toString());
		Files.deleteIfExists(Paths.get(jsonFile));

	}

	@Test
	public void	IteratorSimpleTree() throws IOException {
		StringBuilder	stringBuilder = new StringBuilder();
		JSONHandler		jsonHandler = new JSONHandler();
		Iterator<Node>	iterator;
		String			jsonFile = createJsonFile("{\n" +
				"  \"name\": \"A\",\n" +
				"  \"color\": \"Blue\",\n" +
				"  \"value\": 22,\n" +
				"  \"children\": [\n" +
				"    {\n" +
				"      \"name\": \"B\",\n" +
				"      \"color\": \"Blue\",\n" +
				"      \"value\": 23,\n" +
				"      \"children\": [\n" +
				"        {\n" +
				"          \"name\": \"E\",\n" +
				"          \"color\": \"Blue\",\n" +
				"          \"value\": 50,\n" +
				"          \"children\": []\n" +
				"        }\n" +
				"      ]\n" +
				"    },\n" +
				"    {\n" +
				"      \"name\": \"C\",\n" +
				"      \"color\": \"Green\",\n" +
				"      \"value\": 24,\n" +
				"      \"children\": []\n" +
				"    }\n" +
				"  ]\n" +
				"}");

		jsonHandler.read(jsonFile);
		iterator = jsonHandler.getIterator();
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().getName());
		}
		assertEquals("ABEC", stringBuilder.toString());
		Files.deleteIfExists(Paths.get(jsonFile));
	}

	@Test
	public void	IteratorDoubleTree() throws IOException {
		StringBuilder	stringBuilder = new StringBuilder();
		JSONHandler		jsonHandler = new JSONHandler();
		Iterator<Node>	iterator;
		String			jsonFile = createJsonFile("{\n" +
				"  \"name\": \"A\",\n" +
				"  \"color\": \"Blue\",\n" +
				"  \"value\": 10,\n" +
				"  \"children\": [\n" +
				"    {\n" +
				"      \"name\": \"F\",\n" +
				"      \"color\": \"Blue\",\n" +
				"      \"value\": 99,\n" +
				"      \"children\": [\n" +
				"        {\n" +
				"          \"name\": \"O\",\n" +
				"          \"color\": \"Blue\",\n" +
				"          \"value\": 44,\n" +
				"          \"children\": [\n" +
				"            {\n" +
				"              \"name\": \"Z\",\n" +
				"              \"color\": \"Blue\",\n" +
				"              \"value\": 12,\n" +
				"              \"children\": [\n" +
				"                {\n" +
				"                  \"name\": \"R\",\n" +
				"                  \"color\": \"Blue\",\n" +
				"                  \"value\": 6,\n" +
				"                  \"children\": []\n" +
				"                },\n" +
				"                {\n" +
				"                  \"name\": \"I\",\n" +
				"                  \"color\": \"Blue\",\n" +
				"                  \"value\": 4,\n" +
				"                  \"children\": []\n" +
				"                }\n" +
				"              ]\n" +
				"            }\n" +
				"          ]\n" +
				"        }\n" +
				"      ]\n" +
				"    }\n" +
				"  ]\n" +
				"}");

		jsonHandler.read(jsonFile);
		iterator = jsonHandler.getIterator();
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().getName());
		}
		assertEquals("AFOZIR", stringBuilder.toString());
		Files.deleteIfExists(Paths.get(jsonFile));
	}

	@Test
	public void	IteratorBigTree() {
		StringBuilder	stringBuilder = new StringBuilder();
		JSONHandler		jsonHandler = new JSONHandler();
		Iterator<Node>	iterator;

		jsonHandler.read("BigTree.json");
		iterator = jsonHandler.getIterator();
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().getName());
		}
		assertEquals("ABCEGDFHYXWMOZ", stringBuilder.toString());
	}

	@Test
	public void	IteratorBigTreeDifferentOperator() {
		StringBuilder	stringBuilder = new StringBuilder();
		JSONHandler		jsonHandler = new JSONHandler();
		Iterator<Node>	iterator;

		jsonHandler.read("BigTree.json");
		iterator = jsonHandler.getIterator((x, y) -> {
			if (y.getName().compareTo(x.getName()) < 0)
				return y;
			return x;
		});
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().getName());
		}
		assertEquals("ABCDEFGHXWMOYZ", stringBuilder.toString());
	}

	@Test
	public void	IteratorComplexTree() {
		StringBuilder	stringBuilder = new StringBuilder();
		JSONHandler		jsonHandler = new JSONHandler();
		Iterator<Node>	iterator;

		jsonHandler.read("ComplexTree.json");
		iterator = jsonHandler.getIterator();
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().getName());
		}
		assertEquals("ABCFGEIDKMOWKFCADIYWBMONKJSRISWJCAKWZGXFYOMLQFBQXKFWEZAJOZIR",
				stringBuilder.toString());
	}

	@Test
	public void	IteratorComplexTreeDifferentOperator() {
		StringBuilder	stringBuilder = new StringBuilder();
		JSONHandler		jsonHandler = new JSONHandler();
		Iterator<Node>	iterator;

		jsonHandler.read("ComplexTree.json");
		iterator = jsonHandler.getIterator((x, y) ->
				x.getValue() > y.getValue() ? x : y);
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().getName());
		}
		assertEquals("AFOZRIAJXFZEWKBQRISQJAKLMGXOFYZWCWWFCSDJWKBNOMYIAKMOBCDKEIFG",
				stringBuilder.toString());
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
}
