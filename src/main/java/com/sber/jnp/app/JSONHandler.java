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

	public void 	read(String jsonFilePath) {
		try {
			readImpl(jsonFilePath);
		} catch (Exception exception) {
			logException(exception);
			throw exception;
		}
	}

	private void  	readImpl(String jsonFilePath) {
		String  jsonString;

		jsonString = readJsonFile(jsonFilePath);
		if (isFileEmpty(jsonString))
			throw new WrongFileException("No content in file");
		try {
			node = gson.fromJson(jsonString, Node.class);
		} catch (Exception exception) {
			throw new WrongFileException(exception);
		}
		logger.debug("Json file converted to object");
		checkFieldsValues();
		logger.info("Json object created");
	}

	private String	readJsonFile(String jsonFilePath) {
		String	json;

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
		String 	message = "invalid file extension";

		if (!jsonFilePath.endsWith(".json"))
			throw new WrongFileException(message);
		logger.debug("Input file extension validated");
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
				logger.error("Invalid value in node: {}. Allowed values: 0 - 100.",
						it.getCurrentNodePath());
			if (current.getColor() == null && (error = true))
				logger.error("Invalid value in node: {}. Allowed colors: Red, Green, Blue.",
						it.getCurrentNodePath());
		}
		if (error)
			throw new InvalidValueInJsonException("Errors occurs in json file. Check logs!");
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
		checkJsonWasRead();
		try {
			saveJsonToFile(jsonFilePath);
		} catch (IOException exception) {
			throw new IOErrorWritingJsonFileException(exception);
		}
		logger.info("Json object saved into file: {}", jsonFilePath);
	}

	private void	checkJsonWasRead() {
		if (node == null)
			throw new NoJsonObjectReadException();
	}

	private void	saveJsonToFile(String jsonFilePath) throws IOException {
		try {
			Files.createFile(Paths.get(jsonFilePath));
			logger.debug("{} outfile created", jsonFilePath);
		} catch (FileAlreadyExistsException exception) {
			throw new FileAlreadyExistsException("file already exists: " + jsonFilePath);
		}
		PrintWriter out = new PrintWriter(jsonFilePath);
		gson.toJson(node, out);
		out.close();
		logger.debug("Json object wrote to {}", jsonFilePath);
	}

	public Iterator<Node>	iterator() {
		try {
			checkJsonWasRead();
		} catch (NoJsonObjectReadException exception) {
			logException(exception);
			throw exception;
		}
		iterator = new JSONIterator(node, defaultOperator);
		logger.info("Iterator created (default)");
		return iterator;
	}

	public Iterator<Node>	iterator(BinaryOperator<Node> operator) {
		try {
			checkJsonWasRead();
		} catch (NoJsonObjectReadException exception) {
			logException(exception);
			throw exception;
		}
		iterator = new JSONIterator(node, operator);
		logger.info("Iterator created (custom)");
		return iterator;
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
		StringBuilder	currentPath = new StringBuilder();
		Node			startNode;

		checkJsonWasRead();
		startNode = findSpecifiedPath(currentPath, path);
		if (!currentPath.toString().equals(path))
			throw new InvalidInternalJsonPathException(path);
		logger.info("{} node found by getNode(path)", path);
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
