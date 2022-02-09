package com.sber.jnp.app.tests;

import com.sber.jnp.app.JSONHandler;
import com.sber.jnp.app.Node;
import com.sber.jnp.app.exceptions.InvalidInternalJsonPathException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JSONIteratorTests {
	@Test
	public void	IteratorSmallTree() throws IOException {
		StringBuilder	stringBuilder = new StringBuilder();
		JSONHandler jsonHandler = new JSONHandler();
		Iterator<Node> iterator;
		String			jsonFile = Utils.createJsonFile("{\n" +
				"  \"name\": \"A\",\n" +
				"  \"color\": \"Blue\",\n" +
				"  \"value\": 22,\n" +
				"  \"children\": []\n" +
				"}");

		jsonHandler.read(jsonFile);
		iterator = jsonHandler.iterator();
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
		String			jsonFile = Utils.createJsonFile("{\n" +
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
		iterator = jsonHandler.iterator();
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
		String			jsonFile = Utils.createJsonFile("{\n" +
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
		iterator = jsonHandler.iterator();
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
		iterator = jsonHandler.iterator();
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
		iterator = jsonHandler.iterator((x, y) -> {
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
		iterator = jsonHandler.iterator();
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
		iterator = jsonHandler.iterator((x, y) ->
				x.getValue() > y.getValue() ? x : y);
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().getName());
		}
		assertEquals("AFOZRIAJXFZEWKBQRISQJAKLMGXOFYZWCWWFCSDJWKBNOMYIAKMOBCDKEIFG",
				stringBuilder.toString());
	}

	@Test
	public void	EndOfTree() throws IOException {
		JSONHandler jsonHandler = new JSONHandler();
		Iterator<Node> iterator;
		String			jsonFile = Utils.createJsonFile("{\n" +
				"  \"name\": \"A\",\n" +
				"  \"color\": \"Blue\",\n" +
				"  \"value\": 22,\n" +
				"  \"children\": []\n" +
				"}");

		jsonHandler.read(jsonFile);
		iterator = jsonHandler.iterator();
		iterator.next().getName();
		assertThrows(NoSuchElementException.class, () -> {
			iterator.next().getName();
		});
		Files.deleteIfExists(Paths.get(jsonFile));
	}

	@Test
	public void	GetIteratorByPath() {
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
	public void	WrongIteratorPath() {
		JSONHandler		jsonHandler = new JSONHandler();

		jsonHandler.read("ComplexTree.json");
		assertThrows(InvalidInternalJsonPathException.class, () -> {
			jsonHandler.getNode("");
		});
		assertThrows(InvalidInternalJsonPathException.class, () -> {
			jsonHandler.getNode("A/D/P");
		});
		assertThrows(InvalidInternalJsonPathException.class, () -> {
			jsonHandler.getNode("O/A/A");
		});
		assertThrows(InvalidInternalJsonPathException.class, () -> {
			jsonHandler.getNode("/A/D/P");
		});
	}
}
