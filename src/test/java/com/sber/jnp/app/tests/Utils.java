package com.sber.jnp.app.tests;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {
	private Utils() { }

	public static String	createRandomJsonName() {
		return ((int)(Math.random() * 1000000)) + ".json";
	}

	public static String	createJsonFile(String content) throws IOException {
		String	fileName;

		fileName = createRandomJsonName();
		try {
			Files.createFile(Paths.get(fileName));
		} catch (FileAlreadyExistsException ignore) {
			return createJsonFile(content);
		}
		PrintWriter out = new PrintWriter(fileName);
		out.println(content);
		out.close();
		return fileName;
	}
}
