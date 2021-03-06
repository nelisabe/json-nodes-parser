package com.sber.jnp.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sber.jnp.app.exceptions.*;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.BinaryOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Json tree handler class.
 * @version 0.1
 */
public class JSONHandlerImpl implements JSONHandler {
	private Node node;
	private final Gson gson;
	private final BinaryOperator<Node> defaultOperator;

	private static final Logger logger = LoggerFactory.getLogger(JSONHandlerImpl.class);

	private final int MIN_JSON_INT_VALUE = 0;
	private final int MAX_JSON_INT_VALUE = 100;

	public JSONHandlerImpl() {
		GsonBuilder gsonBuilder = new GsonBuilder();

		gsonBuilder.setPrettyPrinting();
		this.gson = gsonBuilder.create();
		this.defaultOperator = (x, y) -> x.getValue() < y.getValue() ? x : y;
		logger.info("JSONHandler object created");
	}

	public void 	create(Node firstNode) {
		this.node = firstNode;
	}

	public void 	create(String name, Color color, int value) {
		this.node = new Node(name, color, value);
	}

	public void 	read(String jsonFilePath) {
		try {
			readImpl(jsonFilePath);
		} catch (Exception exception) {
			logException(exception);
			throw exception;
		}
	}

	private void  	readImpl(String jsonFilePath) {
		String jsonString;

		jsonString = readJsonFile(jsonFilePath);
		if (isFileEmpty(jsonString)) {
			throw new WrongFileException("No content in file");
		}

		try {
			this.node = this.gson.fromJson(jsonString, Node.class);
		} catch (Exception exception) {
			throw new WrongFileException(exception);
		}
		logger.debug("Json file converted to object");
		checkFieldsValues();
		logger.info("Json object created");
	}

	private String	readJsonFile(String jsonFilePath) {
		String json;

		checkFileExtension(jsonFilePath);
		try {
			json = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
		} catch (IOException exception) {
			throw new IOErrorReadingJsonException(exception);
		}
		logger.debug("Input file {} read", jsonFilePath);
		return json;
	}

	private void	checkFileExtension(String jsonFilePath) {
		String message = "invalid file extension";

		if (!jsonFilePath.endsWith(".json")) {
			throw new WrongFileException(message);
		}

		logger.debug("Input file extension validated");
	}

	private boolean isFileEmpty(String fileContent) {
		return fileContent
				.replaceAll("[ \t]", "")
				.replaceAll(System.lineSeparator(), "")
				.equals("");
	}

	private void	checkFieldsValues() {
		JSONIterator it = new JSONIterator(node, defaultOperator);
		Node current;
		boolean error = false;

		while (it.hasNext()) {
			current = it.next();
			if (current.getValue() < 0 || current.getValue() > 100) {
				logger.error("Invalid value in node: {}. Allowed values: {} - {}.",
						it.getCurrentNodePath(), MIN_JSON_INT_VALUE, MAX_JSON_INT_VALUE);
				error = true;
			}

			if (current.getColor() == null) {
				logger.error("Invalid value in node: {}. Allowed colors: {}.",
						it.getCurrentNodePath(), Arrays.toString(Color.values()));
				error = true;
			}
		}
		if (error) {
			throw new InvalidValueInJsonException("Errors occurs in json file. Check logs!");
		}

		logger.debug("Json content validated");
	}

	private void	logException(Exception exception) {
		logger.error("Exception: {} \n\tStack trace: {}",
				exception.getMessage(),
				exception.getStackTrace());
	}

	public void 	save(String jsonFilePath) {
		try	{
			saveImpl(jsonFilePath);
		} catch (Exception exception) {
			logException(exception);
			throw exception;
		}
	}

	private void 	saveImpl(String jsonFilePath) {
		if (checkJsonWasNotRead()) {
			throw new NoJsonObjectReadException();
		}

		try {
			saveJsonToFile(jsonFilePath);
		} catch (IOException exception) {
			throw new IOErrorWritingJsonFileException(exception);
		}
		logger.info("Json object saved into file: {}", jsonFilePath);
	}

	private boolean checkJsonWasNotRead() {
		return node == null;
	}

	private void	saveJsonToFile(String jsonFilePath) throws IOException {
		try {
			Files.createFile(Paths.get(jsonFilePath));
			logger.debug("{} outfile created", jsonFilePath);
		} catch (FileAlreadyExistsException exception) {
			throw new FileAlreadyExistsException("file already exists: " + jsonFilePath);
		}
		PrintWriter out = new PrintWriter(jsonFilePath);
		this.gson.toJson(this.node, out);
		out.close();
		logger.debug("Json object wrote to {}", jsonFilePath);
	}

	public void 	add(Node newNode, String path) {
		try {
			addImpl(newNode, path);
		} catch (Exception exception) {
			logException(exception);
			throw exception;
		}
	}

	public void 	add(String name, Color color, int value, String path) {
		try {
			Node newNode = new Node(name, color, value);
			logger.debug("New node {} created", newNode);

			addImpl(newNode, path);
		} catch (Exception exception) {
			logException(exception);
			throw exception;
		}
	}

	private void	addImpl(Node newNode, String path) {
		Node store;

		if (checkJsonWasNotRead()) {
			throw new NoJsonObjectReadException();
		}

		store = getNodeImpl(path);
		store.getChildren().add(newNode);
		logger.info("New node {} added to node {}", newNode, store);
	}

	public void		deleteWithChildren(String pathToNode) {
		try {
			deleteImpl(pathToNode, true);
		} catch (Exception exception) {
			logException(exception);
			throw exception;
		}
	}

	public void 	deleteWithoutChildren(String pathToNode) {
		try {
			deleteImpl(pathToNode, false);
		} catch (Exception exception) {
			logException(exception);
			throw exception;
		}
	}

	private void	deleteImpl(String path, boolean deleteChildren) {
		Node toDelete;
		Node previous;

		if (checkJsonWasNotRead()) {
			throw new NoJsonObjectReadException();
		}

		if (path.equals(this.node.getName()) ||
				path.equals(this.node.getName() + '/')) {
			throw new FirstNodeDeleteException(this.node.getName());
		}

		toDelete = getNodeImpl(path);
		previous = getPenultimateNode(path);
		previous.getChildren().remove(toDelete);
		if (!deleteChildren)
			previous.getChildren().addAll(toDelete.getChildren());
	}

	private Node	getPenultimateNode(String path) {
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}

		return getNodeImpl(path.substring(0, path.lastIndexOf('/')));
	}

	public Node		getNode(String path) {
		try {
			return getNodeImpl(path);
		} catch (Exception exception) {
			logException(exception);
			throw exception;
		}
	}

	private Node 	getNodeImpl(String path) {
		StringBuilder currentPath = new StringBuilder();
		Node startNode;

		if (checkJsonWasNotRead()) {
			throw new NoJsonObjectReadException();
		}

		if (!path.endsWith("/")) {
			path += '/';
		}

		startNode = findSpecifiedPath(currentPath, path);
		if (!currentPath.toString().equals(path)) {
			throw new InvalidInternalJsonPathException(path);
		}

		logger.info("{} node found by getNode(path)", path);
		return startNode;
	}

	private Node	findSpecifiedPath(StringBuilder currentPath, String path) {
		Node startNode;
		Node child;
		int	 length;

		startNode = this.node;
		appendPath(currentPath, this.node.getName());
		length = startNode.getChildren().size();
		for (int i = 0; i < length && !currentPath.toString().equals(path); ++i) {
			child = startNode.getChildren().get(i);
			if (path.startsWith(currentPath + child.getName())) {
				startNode = child;
				length = startNode.getChildren().size();
				i = -1;
				appendPath(currentPath, child.getName());
			}
		}
		return startNode;
	}

	private void	appendPath(StringBuilder currentPath, String append) {
		currentPath.append(append);
		currentPath.append("/");
	}

	public Iterator<Node>	iterator() {
		if (checkJsonWasNotRead()) {
			NoJsonObjectReadException exception = new NoJsonObjectReadException();
			logException(exception);
			throw exception;
		}

		logger.info("Iterator created (default)");
		return new JSONIterator(node, defaultOperator);
	}

	public Iterator<Node>	iterator(BinaryOperator<Node> operator) {
		if (checkJsonWasNotRead()) {
			NoJsonObjectReadException exception = new NoJsonObjectReadException();
			logException(exception);
			throw exception;
		}

		logger.info("Iterator created (custom)");
		return new JSONIterator(node, operator);
	}
}
