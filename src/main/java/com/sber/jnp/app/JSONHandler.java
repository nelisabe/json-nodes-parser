package com.sber.jnp.app;

import com.google.gson.*;
import com.sber.jnp.app.exceptions.IOErrorReadingJsonException;
import com.sber.jnp.app.exceptions.NoSuchJsonFileException;

import java.io.*;


public class JSONHandler {
	private JSONHandler() {}

	public static void  main(String... args) {
		read("/Users/19809078/IdeaProjects/json-nodes-parser/src/main/java/com/sber/jnp/app/Tree.json");
	}

	public static void  read(String jsonFilePath) {
		Node    node;
		Gson    gson = new Gson();
		String  jsonString;

		jsonString = readJsonFile(jsonFilePath);
		node = gson.fromJson(jsonString, Node.class);
		System.out.println(node.name);
		System.out.println(node.value);
		System.out.println(node.color);
	}

	private static String   readJsonFile(String jsonFilePath) {
		FileReader      jsonFile;
		BufferedReader  bufferedReader;
		StringBuilder   jsonStringBuilder;
		char            buffer[];
		int             bytesRead;

		jsonFile = openJsonFile(jsonFilePath);
		bufferedReader = new BufferedReader(jsonFile);
		buffer = new char[512];
		jsonStringBuilder = new StringBuilder();
		try {
			while ((bytesRead = bufferedReader.read(buffer, 0, 512)) != -1) {
				jsonStringBuilder.append(buffer, 0, bytesRead);
			}
		} catch (IOException ex) {
			throw new IOErrorReadingJsonException();
		}
		return jsonStringBuilder.toString();
	}

	private static FileReader openJsonFile(String jsonFilePath) {
		FileReader fileReader;

		try {
			fileReader = new FileReader(jsonFilePath);
		} catch (FileNotFoundException ex) {
			throw new NoSuchJsonFileException();
		}
		return fileReader;
	}

	public static void  save(String jsonFilePath) {

	}
}
