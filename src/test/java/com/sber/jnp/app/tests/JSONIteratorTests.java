package com.sber.jnp.app.tests;

import com.sber.jnp.app.JSONHandler;
import com.sber.jnp.app.JSONHandlerImpl;
import com.sber.jnp.app.Node;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JSONIteratorTests {
	@Test
	public void	IteratorSmallTree() {
		StringBuilder stringBuilder = new StringBuilder();
		JSONHandler jsonHandler = new JSONHandlerImpl();
		Iterator<Node> iterator;
		String jsonFile = Utils.createJsonFile("""
				{
				  "name": "A",
				  "color": "Blue",
				  "value": 22,
				  "children": []
				}""");

		jsonHandler.read(jsonFile);
		iterator = jsonHandler.iterator();
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().getName());
		}
		assertEquals("A", stringBuilder.toString());
		Utils.deleteFile(jsonFile);
	}

	@Test
	public void	IteratorSimpleTree() {
		StringBuilder stringBuilder = new StringBuilder();
		JSONHandler jsonHandler = new JSONHandlerImpl();
		Iterator<Node> iterator;
		String jsonFile = Utils.createJsonFile("""
				{
				  "name": "A",
				  "color": "Blue",
				  "value": 22,
				  "children": [
				    {
				      "name": "B",
				      "color": "Blue",
				      "value": 23,
				      "children": [
				        {
				          "name": "E",
				          "color": "Blue",
				          "value": 50,
				          "children": []
				        }
				      ]
				    },
				    {
				      "name": "C",
				      "color": "Green",
				      "value": 24,
				      "children": []
				    }
				  ]
				}""");

		jsonHandler.read(jsonFile);
		iterator = jsonHandler.iterator();
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().getName());
		}
		assertEquals("ABEC", stringBuilder.toString());
		Utils.deleteFile(jsonFile);
	}

	@Test
	public void	IteratorDoubleTree() {
		StringBuilder stringBuilder = new StringBuilder();
		JSONHandler jsonHandler = new JSONHandlerImpl();
		Iterator<Node> iterator;
		String jsonFile = Utils.createJsonFile("""
				{
				  "name": "A",
				  "color": "Blue",
				  "value": 10,
				  "children": [
				    {
				      "name": "F",
				      "color": "Blue",
				      "value": 99,
				      "children": [
				        {
				          "name": "O",
				          "color": "Blue",
				          "value": 44,
				          "children": [
				            {
				              "name": "Z",
				              "color": "Blue",
				              "value": 12,
				              "children": [
				                {
				                  "name": "R",
				                  "color": "Blue",
				                  "value": 6,
				                  "children": []
				                },
				                {
				                  "name": "I",
				                  "color": "Blue",
				                  "value": 4,
				                  "children": []
				                }
				              ]
				            }
				          ]
				        }
				      ]
				    }
				  ]
				}""");

		jsonHandler.read(jsonFile);
		iterator = jsonHandler.iterator();
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().getName());
		}
		assertEquals("AFOZIR", stringBuilder.toString());
		Utils.deleteFile(jsonFile);
	}

	@Test
	public void	IteratorBigTree() {
		StringBuilder stringBuilder = new StringBuilder();
		JSONHandler	jsonHandler = new JSONHandlerImpl();
		Iterator<Node> iterator;

		jsonHandler.read(Utils.getResourceFilePath(
				"BigTree.json", this));
		iterator = jsonHandler.iterator();
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().getName());
		}
		assertEquals("ABCEGDFHYXWMOZ", stringBuilder.toString());
	}

	@Test
	public void	IteratorBigTreeDifferentOperator() {
		StringBuilder stringBuilder = new StringBuilder();
		JSONHandler jsonHandler = new JSONHandlerImpl();
		Iterator<Node> iterator;

		jsonHandler.read(Utils.getResourceFilePath(
				"BigTree.json", this));
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
		StringBuilder stringBuilder = new StringBuilder();
		JSONHandler jsonHandler = new JSONHandlerImpl();
		Iterator<Node> iterator;

		jsonHandler.read(Utils.getResourceFilePath(
				"ComplexTree.json", this));
		iterator = jsonHandler.iterator();
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().getName());
		}
		assertEquals("ABCFGEIDKMOWKFCADIYWBMONKJSRISWJCAKWZGXFYOMLQFBQXKFWEZAJOZIR",
				stringBuilder.toString());
	}

	@Test
	public void	IteratorComplexTreeDifferentOperator() {
		StringBuilder stringBuilder = new StringBuilder();
		JSONHandler jsonHandler = new JSONHandlerImpl();
		Iterator<Node> iterator;

		jsonHandler.read(Utils.getResourceFilePath(
				"ComplexTree.json", this));
		iterator = jsonHandler.iterator((x, y) ->
				x.getValue() > y.getValue() ? x : y);
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().getName());
		}
		assertEquals("AFOZRIAJXFZEWKBQRISQJAKLMGXOFYZWCWWFCSDJWKBNOMYIAKMOBCDKEIFG",
				stringBuilder.toString());
	}

	@Test
	public void	EndOfTree() {
		JSONHandler	jsonHandler = new JSONHandlerImpl();
		Iterator<Node> iterator;
		String jsonFile = Utils.createJsonFile("""
				{
				  "name": "A",
				  "color": "Blue",
				  "value": 22,
				  "children": []
				}""");

		jsonHandler.read(jsonFile);
		iterator = jsonHandler.iterator();
		iterator.next();
		assertThrows(NoSuchElementException.class, iterator::next);
		Utils.deleteFile(jsonFile);
	}

	@Test
	public void	SameNodes() {
		JSONHandler jsonHandler = new JSONHandlerImpl();
		Iterator<Node> iterator;
		StringBuilder stringBuilder = new StringBuilder();
		String jsonFile = Utils.createJsonFile("""
				{
				  "name": "A",
				  "color": "Blue",
				  "value": 12,
				  "children": [
				    {
				      "name": "A",
				      "color": "Blue",
				      "value": 12,
				      "children": [
				        {
				          "name": "A",
				          "color": "Blue",
				          "value": 12,
				          "children": []
				        }
				      ]
				    },
				    {
				      "name": "A",
				      "color": "Blue",
				      "value": 12,
				      "children": []
				    }
				  ]
				}
				""");

		jsonHandler.read(jsonFile);
		iterator = jsonHandler.iterator();
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().getName());
		}
		assertEquals("AAAA", stringBuilder.toString());
		Utils.deleteFile(jsonFile);
	}
}
