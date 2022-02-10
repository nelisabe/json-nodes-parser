package com.sber.jnp.app.tests;

import com.sber.jnp.app.*;
import com.sber.jnp.app.exceptions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class JSONHandlerReadSaveTests {
	@Test
	public void WrongFile() {
		assertThrows(IOErrorReadingJsonException.class, () -> {
			JSONHandler jsonHandler = new JSONHandler();
			jsonHandler.read(Utils.createRandomJsonName());
		});
		assertThrows(WrongFileException.class, () -> {
			JSONHandler jsonHandler = new JSONHandler();
			jsonHandler.read("json.wr");
		});
	}

	@Test
	public void	WrongFileContent() throws IOException {
		final String jsonFile = Utils.createJsonFile(
				"""
						{
						  "name" "Name 1-1-2",
						  "color": "Green",
						  "value": 33,
						  "children": []
						}""");
		assertThrows(WrongFileException.class, () -> {
			JSONHandler jsonHandler = new JSONHandler();
			jsonHandler.read(jsonFile);
		});
		Files.deleteIfExists(Paths.get(jsonFile));
	}

	@Test
	public void	EmptyFile() throws IOException {
		final String jsonFile = Utils.createJsonFile("");
		assertThrows(WrongFileException.class, () -> {
			JSONHandler jsonHandler = new JSONHandler();
			jsonHandler.read(jsonFile);
		});
		Files.deleteIfExists(Paths.get(jsonFile));

		final String jsonFile2 = Utils.createJsonFile("\n\n");
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
	public void	ReadAndSaveObjectAsJson() throws IOException {
		JSONHandler	jsonHandler = new JSONHandler();
		String		jsonContent =
				"""
						{
						  "name": "Name 1-1-2",
						  "color": "Green",
						  "value": 33,
						  "children": []
						}""";
		String		jsonFile = Utils.createJsonFile(jsonContent);
		String		resultJson = Utils.createRandomJsonName();

		jsonHandler.read(jsonFile);
		jsonHandler.save(resultJson);
		assertTrue(Files.exists(Paths.get(resultJson)));
		assertEquals(jsonContent, new String(Files.readAllBytes(Paths.get(resultJson))));
		assertThrows(IOErrorWritingJsonFileException.class, () ->
				jsonHandler.save(resultJson));
		Files.deleteIfExists(Paths.get(resultJson));
		Files.deleteIfExists(Paths.get(jsonFile));
	}

	@Test
	public void	GetNodeByPath() {
		JSONHandler		jsonHandler = new JSONHandler();
		Node			node;

		jsonHandler.read("ComplexTree.json");
		node = jsonHandler.getNode("A/F/O/");
		assertEquals("O", node.getName());
		assertEquals(44, node.getValue());
		node = jsonHandler.getNode("A/W/F/C/D/W/B/");
		assertEquals("B", node.getName());
		assertEquals(2, node.getValue());
		node = jsonHandler.getNode("A/M/O/");
		assertEquals("O", node.getName());
		assertEquals(25, node.getValue());
		node = jsonHandler.getNode("A/");
		assertEquals("A", node.getName());
		assertEquals(10, node.getValue());
	}

	@Test
	public void	WrongNodePath() {
		JSONHandler		jsonHandler = new JSONHandler();

		jsonHandler.read("ComplexTree.json");
		assertThrows(InvalidInternalJsonPathException.class, () -> {
			jsonHandler.getNode("");
		});
		assertThrows(InvalidInternalJsonPathException.class, () -> {
			jsonHandler.getNode("A/D/P/");
		});
		assertThrows(InvalidInternalJsonPathException.class, () -> {
			jsonHandler.getNode("O/A/A/");
		});
		assertThrows(InvalidInternalJsonPathException.class, () -> {
			jsonHandler.getNode("/A/D/P");
		});
		assertThrows(InvalidInternalJsonPathException.class, () -> {
			jsonHandler.getNode("/A/W/F/C/D/O/");
		});
	}

	@Test
	public void	GetIteratorBeforeRead() {
		assertThrows(NoJsonObjectReadException.class, () -> {
			JSONHandler jsonHandler = new JSONHandler();
			jsonHandler.iterator();
			jsonHandler.iterator((x, y) ->
					x.getValue() < y.getValue() ? x : y);
		});
	}

	@Test
	public void	GetNodeBeforeRead() {
		assertThrows(NoJsonObjectReadException.class, () -> {
			JSONHandler jsonHandler = new JSONHandler();
			jsonHandler.getNode("A/");
		});
	}

	@Test
	public void	ExcessFields() throws IOException {
		String		jsonContent = """
				{
				  "name": "Some Name",
				  "wrong": "should not be there",
				  "color": "Blue",
				  "value": 22,
				  "excess": 123123,
				  "children": [],
				  "excess_2": []
				}""";
		String		jsonContentAfterHandle = """
				{
				  "name": "Some Name",
				  "color": "Blue",
				  "value": 22,
				  "children": []
				}""";
		String		jsonFile = Utils.createJsonFile(jsonContent);
		String		resultJson = Utils.createRandomJsonName();
		JSONHandler	jsonHandler = new JSONHandler();

		jsonHandler.read(jsonFile);
		jsonHandler.save(resultJson);
		assertTrue(Files.exists(Paths.get(resultJson)));
		assertEquals(jsonContentAfterHandle,
				new String(Files.readAllBytes(Paths.get(resultJson))));
		Files.deleteIfExists(Paths.get(jsonFile));
		Files.deleteIfExists(Paths.get(resultJson));
	}

	@Test
	public void	InvalidValuesInJson() {
		assertThrows(InvalidValueInJsonException.class, () -> {
			JSONHandler jsonHandler = new JSONHandler();
			jsonHandler.read("ComplexTreeErrors.json");
		});
	}
}
