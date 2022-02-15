package com.sber.jnp.app.tests;

import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {
	private Utils() { }

	public static String	createRandomJsonName() {
		return ((int)(Math.random() * 1000000)) + ".json";
	}

	public static String	createJsonFile(String content) {
		String fileName;

		fileName = createRandomJsonName();
		try {
			Files.createFile(Paths.get(fileName));
		} catch (FileAlreadyExistsException ignore) {
			return createJsonFile(content);
		} catch (Exception exception) {
			throw new InternalTestErrorException(exception);
		}
		writeFile(fileName, content);
		return fileName;
	}

	public static void 		writeFile(String fileName, String content) {
		PrintWriter out;

		try {
			out = new PrintWriter(fileName);
		} catch (Exception exception) {
			throw new InternalTestErrorException(exception);
		}
		out.println(content);
		out.close();
	}

	public static byte[]	readFile(String fileName) {
		try {
			return Files.readAllBytes(Paths.get(fileName));
		} catch (Exception exception) {
			throw new InternalTestErrorException(exception);
		}
	}

	public static void		deleteFile(String fileName) {
		try {
			Files.deleteIfExists(Paths.get(fileName));
		} catch (Exception exception) {
			throw new InternalTestErrorException(exception);
		}
	}
}
