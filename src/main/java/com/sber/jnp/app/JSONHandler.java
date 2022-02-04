package com.sber.jnp.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sber.jnp.app.exceptions.IOErrorReadingJsonException;
import com.sber.jnp.app.exceptions.IOErrorWritingJsonFileException;
import com.sber.jnp.app.exceptions.NoJsonObjectReadException;
import com.sber.jnp.app.exceptions.WrongFileException;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class JSONHandler {
	private Node    	node;
	private final Gson	gson;

	public JSONHandler() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		gson = gsonBuilder.create();
	}

	public void  	read(String jsonFilePath) {
		String  jsonString;

		jsonString = readJsonFile(jsonFilePath);

		if (isFileEmpty(jsonString))
			throw new WrongFileException("No content in file");
		try {
			node = gson.fromJson(jsonString, Node.class);
		} catch (Exception exception) {
			throw new WrongFileException(exception);
		}
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
		int		indexOfLastDot;
		String 	message = "invalid file extension";

		if ((indexOfLastDot = jsonFilePath.lastIndexOf('.')) == -1)
			throw new WrongFileException(message);
		if (!jsonFilePath.substring(indexOfLastDot + 1).equals("json"))
			throw new WrongFileException(message);
	}

	private boolean isFileEmpty(String fileContent) {
		String	noNewLinesContent;

		noNewLinesContent = fileContent.replaceAll("\n", "");
		return noNewLinesContent.equals("");
	}

	public void 	save(String jsonFilePath) {
		if (node == null)
			throw new NoJsonObjectReadException();
		try {
			saveJsonToFile(jsonFilePath);
		} catch (IOException exception) {
			throw new IOErrorWritingJsonFileException(exception);
		}
	}

	private void	saveJsonToFile(String jsonFilePath) throws IOException {
		try {
			Files.createFile(Paths.get(jsonFilePath));
		} catch (FileAlreadyExistsException exception) {
			throw new IOErrorWritingJsonFileException("file already exists: " + jsonFilePath);
		}
		PrintWriter out = new PrintWriter(jsonFilePath);
		gson.toJson(node, out);
		out.close();
	}
}
