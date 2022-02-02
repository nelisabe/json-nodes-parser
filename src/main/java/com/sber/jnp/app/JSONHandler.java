package com.sber.jnp.app;

import com.google.gson.Gson;
import com.sber.jnp.app.exceptions.IOErrorReadingJsonException;
import com.sber.jnp.app.exceptions.InvalidArgumentsException;

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
		node = gson.fromJson(jsonString, Node.class);
		System.out.println(node.name);
		System.out.println(node.value);
		System.out.println(node.color);
	}

	private static String	readJsonFile(String jsonFilePath) {
		String	json;

		try {
			json = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
		} catch (IOException exception) {
			throw new IOErrorReadingJsonException();
		}
		return json;
	}
	public static void  save(String jsonFilePath) {

	}
}
