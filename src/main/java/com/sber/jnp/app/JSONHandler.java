package com.sber.jnp.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sber.jnp.app.exceptions.*;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.function.BinaryOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONHandler {
	private Node    					node;
	private final Gson					gson;
	private JSONIterator				iterator;
	private final BinaryOperator<Node>	defaultOperator;

	private static final Logger logger = LoggerFactory.getLogger(JSONHandler.class);

	public JSONHandler() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		gson = gsonBuilder.create();
		defaultOperator = (x, y) -> x.getValue() < y.getValue() ? x : y;
		logger.info("JSONHandler object created");
	}

	public void  	read(String jsonFilePath) {
		String  jsonString;

		jsonString = readJsonFile(jsonFilePath);
		logger.info("Input file read");
		if (isFileEmpty(jsonString))
			throw new WrongFileException("No content in file");
		try {
			node = gson.fromJson(jsonString, Node.class);
		} catch (Exception exception) {
			throw new WrongFileException(exception);
		}
		logger.info("Json file converted to object");
		checkFieldsValues();
		logger.info("Json object validated");
	}

	private String	readJsonFile(String jsonFilePath) {
		String	json;

		checkFileExtension(jsonFilePath);
		try {
			json = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
		} catch (IOException exception) {
			throw new IOErrorReadingJsonException(exception);
		}
		return json;
	}

	private void	checkFileExtension(String jsonFilePath) {
		String 	message = "invalid file extension";

		if (!jsonFilePath.endsWith(".json"))
			throw new WrongFileException(message);
	}

	private boolean isFileEmpty(String fileContent) {
		String	noNewLinesContent;

		noNewLinesContent = fileContent.replaceAll("\n", "");
		return noNewLinesContent.equals("");
	}

	private void	checkFieldsValues() {
		JSONIterator 	it = new JSONIterator(node, defaultOperator);
		Node			current;
		boolean			error = false;

		while (it.hasNext()) {
			current = it.next();
			if ((current.getValue() < 0 || current.getValue() > 100) && (error = true))
				logger.error("Invalid value in node: " +
						it.getCurrentNodePath() +
						". Allowed values: 0 - 100.");
			if (current.getColor() == null && (error = true))
				logger.error("Invalid value in node: " +
						it.getCurrentNodePath() +
						"Allowed colors: Red, Green, Blue.");
		}
		if (error)
			throw new InvalidValueInJsonException("Errors occurs in json file. Check logs!");
	}

	public void 	save(String jsonFilePath) {
		checkJsonWasRead();
		try {
			saveJsonToFile(jsonFilePath);
		} catch (IOException exception) {
			throw new IOErrorWritingJsonFileException(exception);
		}
	}

	private void	checkJsonWasRead() {
		if (node == null)
			throw new NoJsonObjectReadException();
	}

	private void	saveJsonToFile(String jsonFilePath) throws IOException {
		try {
			Files.createFile(Paths.get(jsonFilePath));
		} catch (FileAlreadyExistsException exception) {
			throw new FileAlreadyExistsException("file already exists: " + jsonFilePath);
		}
		PrintWriter out = new PrintWriter(jsonFilePath);
		gson.toJson(node, out);
		out.close();
	}

	public Iterator<Node>	iterator() {
		checkJsonWasRead();
		iterator = new JSONIterator(node, defaultOperator);
		return iterator;
	}

	public Iterator<Node>	iterator(BinaryOperator<Node> operator) {
		checkJsonWasRead();
		iterator = new JSONIterator(node, operator);
		return iterator;
	}

	public Node 			getNode(String path) {
		StringBuilder	currentPath = new StringBuilder();
		Node			startNode;

		checkJsonWasRead();
		startNode = findSpecifiedPath(currentPath, path);
		if (!currentPath.toString().equals(path))
			throw new InvalidInternalJsonPathException(path);
		return startNode;
	}

	private Node	findSpecifiedPath(StringBuilder currentPath, String path) {
		Node 			startNode;
		Node			child;
		int				length;

		startNode = node;
		appendPath(currentPath, node.getName());
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
}
