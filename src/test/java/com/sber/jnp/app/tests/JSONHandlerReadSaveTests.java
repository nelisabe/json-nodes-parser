package com.sber.jnp.app.tests;

import com.sber.jnp.app.*;
import com.sber.jnp.app.exceptions.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class JSONHandlerReadSaveTests {
	@Test
	public void WrongFile() {
		assertThrows(IOErrorReadingJsonException.class, () -> {
			JSONHandler jsonHandler = new JSONHandlerImpl();
			jsonHandler.read(Utils.createRandomJsonName());
		});
		assertThrows(WrongFileException.class, () -> {
			JSONHandler jsonHandler = new JSONHandlerImpl();
			jsonHandler.read("json.wr");
		});
	}

	@Test
	public void	WrongFileContent() {
		final String jsonFile = Utils.createJsonFile(
				"""
						{
						  "name" "Name 1-1-2",
						  "color": "Green",
						  "value": 33,
						  "children": []
						}""");
		assertThrows(WrongFileException.class, () -> {
			JSONHandler jsonHandler = new JSONHandlerImpl();
			jsonHandler.read(jsonFile);
		});
		Utils.deleteFile(jsonFile);
	}

	@Test
	public void	EmptyFile() {
		final String jsonFile = Utils.createJsonFile("");
		assertThrows(WrongFileException.class, () -> {
			JSONHandler jsonHandler = new JSONHandlerImpl();
			jsonHandler.read(jsonFile);
		});
		Utils.deleteFile(jsonFile);

		final String jsonFile2 = Utils.createJsonFile("\n \n\t");
		assertThrows(WrongFileException.class, () -> {
			JSONHandler jsonHandler = new JSONHandlerImpl();
			jsonHandler.read(jsonFile2);
		});
		Utils.deleteFile(jsonFile2);
	}

	@Test
	public void	Create() {
		JSONHandler jsonHandler = new JSONHandlerImpl();
		String fileName = Utils.createRandomJsonName();

		jsonHandler.create(new Node("A", Color.Red, 1));
		jsonHandler.save(fileName);
		assertEquals("""
				{
				  "name": "A",
				  "color": "Red",
				  "value": 1,
				  "children": []
				}""", new String(Utils.readFile(fileName)));
		Utils.deleteFile(fileName);

		jsonHandler.add("B", Color.Green, 10, "A/");
		jsonHandler.save(fileName);
		assertEquals("""
				{
				  "name": "A",
				  "color": "Red",
				  "value": 1,
				  "children": [
				    {
				      "name": "B",
				      "color": "Green",
				      "value": 10,
				      "children": []
				    }
				  ]
				}""", new String(Utils.readFile(fileName)));
		Utils.deleteFile(fileName);

		jsonHandler.create("C", Color.Blue, 30);
		jsonHandler.save(fileName);
		assertEquals("""
				{
				  "name": "C",
				  "color": "Blue",
				  "value": 30,
				  "children": []
				}""", new String(Utils.readFile(fileName)));
		Utils.deleteFile(fileName);
	}

	@Test
	public void	SaveBeforeRead() {
		assertThrows(NoJsonObjectReadException.class, () -> {
			JSONHandler jsonHandler = new JSONHandlerImpl();
			jsonHandler.save("some.json");
		});
	}

	@Test
	public void	ReadAndSaveObjectAsJson() {
		JSONHandler	jsonHandler = new JSONHandlerImpl();
		String jsonContent =
				"""
						{
						  "name": "Name 1-1-2",
						  "color": "Green",
						  "value": 33,
						  "children": []
						}""";
		String jsonFile = Utils.createJsonFile(jsonContent);
		String resultJson = Utils.createRandomJsonName();

		jsonHandler.read(jsonFile);
		jsonHandler.save(resultJson);
		assertTrue(Files.exists(Paths.get(resultJson)));
		assertEquals(jsonContent, new String(Utils.readFile(resultJson)));
		assertThrows(IOErrorWritingJsonFileException.class, () ->
				jsonHandler.save(resultJson));
		Utils.deleteFile(resultJson);
		Utils.deleteFile(jsonFile);
	}

	@Test
	public void	GetNodeByPath() {
		JSONHandler jsonHandler = new JSONHandlerImpl();
		Node node;

		jsonHandler.read("ComplexTree.json");
		node = jsonHandler.getNode("A/F/O/");
		assertEquals("O", node.getName());
		assertEquals(44, node.getValue());
		node = jsonHandler.getNode("A/W/F/C/D/W/B/");
		assertEquals("B", node.getName());
		assertEquals(2, node.getValue());
		node = jsonHandler.getNode("A/M/O");
		assertEquals("O", node.getName());
		assertEquals(25, node.getValue());
		node = jsonHandler.getNode("A");
		assertEquals("A", node.getName());
		assertEquals(10, node.getValue());
	}

	@Test
	public void	WrongNodePath() {
		JSONHandler jsonHandler = new JSONHandlerImpl();

		jsonHandler.read("ComplexTree.json");
		assertThrows(InvalidInternalJsonPathException.class, () ->
				jsonHandler.getNode(""));
		assertThrows(InvalidInternalJsonPathException.class, () ->
			jsonHandler.getNode("A/D/P/"));
		assertThrows(InvalidInternalJsonPathException.class, () ->
			jsonHandler.getNode("O/A/A/"));
		assertThrows(InvalidInternalJsonPathException.class, () ->
			jsonHandler.getNode("/A/D/P"));
		assertThrows(InvalidInternalJsonPathException.class, () ->
			jsonHandler.getNode("/A/W/F/C/D/O/"));
	}

	@Test
	public void	GetIteratorBeforeRead() {
		assertThrows(NoJsonObjectReadException.class, () -> {
			JSONHandler jsonHandler = new JSONHandlerImpl();
			jsonHandler.iterator();
		});
		assertThrows(NoJsonObjectReadException.class, () -> {
			JSONHandler jsonHandler = new JSONHandlerImpl();
			jsonHandler.iterator((x, y) ->
					x.getValue() < y.getValue() ? x : y);
		});
	}

	@Test
	public void	GetNodeBeforeRead() {
		assertThrows(NoJsonObjectReadException.class, () -> {
			JSONHandler jsonHandler = new JSONHandlerImpl();
			jsonHandler.getNode("A/");
		});
	}

	@Test
	public void	ExcessFields() {
		String jsonContent = """
				{
				  "name": "Some Name",
				  "wrong": "should not be there",
				  "color": "Blue",
				  "value": 22,
				  "excess": 123123,
				  "children": [],
				  "excess_2": []
				}""";
		String jsonContentAfterHandle = """
				{
				  "name": "Some Name",
				  "color": "Blue",
				  "value": 22,
				  "children": []
				}""";
		String jsonFile = Utils.createJsonFile(jsonContent);
		String resultJson = Utils.createRandomJsonName();
		JSONHandler	jsonHandler = new JSONHandlerImpl();

		jsonHandler.read(jsonFile);
		jsonHandler.save(resultJson);
		assertTrue(Files.exists(Paths.get(resultJson)));
		assertEquals(jsonContentAfterHandle,
				new String(Utils.readFile(resultJson)));
		Utils.deleteFile(jsonFile);
		Utils.deleteFile(resultJson);
	}

	@Test
	public void	InvalidValuesInJson() {
		assertThrows(InvalidValueInJsonException.class, () -> {
			JSONHandler jsonHandler = new JSONHandlerImpl();
			jsonHandler.read("ComplexTreeErrors.json");
		});
	}

	@Test
	public void	NodeCreation() {
		Node node;
		Node node2;

		node = new Node("Name", Color.Red, 50);
		assertEquals("Name", node.getName());
		assertEquals(Color.Red, node.getColor());
		assertEquals(50, node.getValue());
		assertEquals(0, node.getChildren().size());

		ArrayList<Node>	children = new ArrayList<>();
		children.add(node);
		node2 = new Node("Name2", Color.Green, 60, children);
		assertEquals(node, node2.getChildren().get(0));

		assertThrows(InvalidNodeValueException.class, () ->
				new Node("Name3", Color.Red, -12));
		assertThrows(InvalidNodeValueException.class, () ->
				new Node("Name3", Color.Red, 120));

		assertEquals("name: Name, value: 50, color: Red",
				node.toString());
	}

	@Test
	public void	AddNewNode() {
		JSONHandler jsonHandler = new JSONHandlerImpl();
		Node node;

		jsonHandler.read("BigTree.json");
		jsonHandler.add("C", Color.Blue, 43, "A/H/Z/");
		assertEquals("C", jsonHandler.getNode("A/H/Z/C/").getName());

		node = new Node("V", Color.Blue, 43);
		jsonHandler.add(node, "A/H/Z/C/");
		assertEquals("V", jsonHandler.getNode("A/H/Z/C/V/").getName());
	}

	@Test
	public void	AddNewNodeExceptions() {
		JSONHandler jsonHandler = new JSONHandlerImpl();
		Node node = new Node("V", Color.Blue, 43);

		assertThrows(NoJsonObjectReadException.class, () ->
				jsonHandler.add("Name", Color.Red, 50, "A/"));
		assertThrows(NoJsonObjectReadException.class, () ->
				jsonHandler.add(node, "A/"));
		jsonHandler.read("BigTree.json");
		assertThrows(InvalidNodeValueException.class, () ->
				jsonHandler.add("Name", Color.Red, -10, "A/H/Z/C/"));
		assertThrows(InvalidInternalJsonPathException.class, () ->
				jsonHandler.add(node, "A/H/R/C/"));
	}

	@Test
	public void	DeleteNodeWithChildren() {
		JSONHandler jsonHandler = new JSONHandlerImpl();
		String fileName = Utils.createRandomJsonName();

		assertThrows(NoJsonObjectReadException.class, () ->
				jsonHandler.deleteWithChildren("A/H/"));

		jsonHandler.read("BigTree.json");
		assertThrows(InvalidInternalJsonPathException.class, () ->
				jsonHandler.deleteWithChildren("A/P/"));

		jsonHandler.deleteWithChildren("A/H/");
		jsonHandler.deleteWithChildren("A/B/C/F");
		jsonHandler.save(fileName);
		assertEquals("""
				{
				  "name": "A",
				  "color": "Blue",
				  "value": 22,
				  "children": [
				    {
				      "name": "B",
				      "color": "Red",
				      "value": 22,
				      "children": [
				        {
				          "name": "C",
				          "color": "Green",
				          "value": 10,
				          "children": [
				            {
				              "name": "D",
				              "color": "Green",
				              "value": 67,
				              "children": []
				            },
				            {
				              "name": "E",
				              "color": "Green",
				              "value": 33,
				              "children": []
				            },
				            {
				              "name": "G",
				              "color": "Green",
				              "value": 50,
				              "children": []
				            }
				          ]
				        }
				      ]
				    }
				  ]
				}""", new String(Utils.readFile(fileName)));
		Utils.deleteFile(fileName);
	}
	@Test
	public void	DeleteNodeWithoutChildren() {
		JSONHandler jsonHandler = new JSONHandlerImpl();
		String fileName = Utils.createRandomJsonName();

		assertThrows(NoJsonObjectReadException.class, () ->
				jsonHandler.deleteWithoutChildren("A/H/"));

		jsonHandler.read("BigTree.json");
		assertThrows(InvalidInternalJsonPathException.class, () ->
				jsonHandler.deleteWithoutChildren("A/P/"));

		jsonHandler.deleteWithoutChildren("A/H/");
		jsonHandler.deleteWithoutChildren("A/B/C/F");
		jsonHandler.save(fileName);
		assertEquals("""
				{
				  "name": "A",
				  "color": "Blue",
				  "value": 22,
				  "children": [
				    {
				      "name": "B",
				      "color": "Red",
				      "value": 22,
				      "children": [
				        {
				          "name": "C",
				          "color": "Green",
				          "value": 10,
				          "children": [
				            {
				              "name": "D",
				              "color": "Green",
				              "value": 67,
				              "children": []
				            },
				            {
				              "name": "E",
				              "color": "Green",
				              "value": 33,
				              "children": []
				            },
				            {
				              "name": "G",
				              "color": "Green",
				              "value": 50,
				              "children": []
				            }
				          ]
				        }
				      ]
				    },
				    {
				      "name": "X",
				      "color": "Green",
				      "value": 10,
				      "children": [
				        {
				          "name": "W",
				          "color": "Green",
				          "value": 99,
				          "children": [
				            {
				              "name": "O",
				              "color": "Green",
				              "value": 99,
				              "children": []
				            },
				            {
				              "name": "M",
				              "color": "Green",
				              "value": 86,
				              "children": []
				            }
				          ]
				        }
				      ]
				    },
				    {
				      "name": "Z",
				      "color": "Green",
				      "value": 45,
				      "children": []
				    },
				    {
				      "name": "Y",
				      "color": "Green",
				      "value": 1,
				      "children": []
				    }
				  ]
				}""", new String(Utils.readFile(fileName)));
		Utils.deleteFile(fileName);
	}
}
