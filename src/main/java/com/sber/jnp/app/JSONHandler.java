package com.sber.jnp.app;

import com.google.gson.Gson;
import com.sber.jnp.app.exceptions.IOErrorReadingJsonException;
import com.sber.jnp.app.exceptions.WrongFileException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class JSONHandler {
	private JSONHandler() {}

	public static void  read(String jsonFilePath) {
		Node    node;
		Gson	gson = new Gson();
		String  jsonString;

		jsonString = readJsonFile(jsonFilePath);
		try {
			node = gson.fromJson(jsonString, Node.class);
		} catch (Exception exception) {
			throw new WrongFileException(exception);
		}
	}

	private static String	readJsonFile(String jsonFilePath) {
		String	json;

		checkFileExtension(jsonFilePath);
		try {
			json = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
		} catch (IOException exception) {
			throw new IOErrorReadingJsonException(exception);
		}
		return json;
	}

	private static void		checkFileExtension(String jsonFilePath) {
		int		indexOfLastDot;
		String 	message = "invalid file extension";

		if ((indexOfLastDot = jsonFilePath.lastIndexOf('.')) == -1)
			throw new WrongFileException(message);
		if (!jsonFilePath.substring(indexOfLastDot + 1).equals("json"))
			throw new WrongFileException(message);
	}

	public static void  save(String jsonFilePath) {

	}
}
