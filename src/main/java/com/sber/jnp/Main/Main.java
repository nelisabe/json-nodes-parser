package com.sber.jnp.app;

import java.util.Iterator;

public class Main {
	private static String	parseArguments(String[] args) {
		Arguments	arguments;
		String		jsonFilePath;

		arguments = new Arguments();
		arguments.parse(args);
		jsonFilePath = arguments.getJsonFilePath();
		return jsonFilePath;
	}

	public static void	main(String[] args) {
		String	jsonFilePath;
		JSONHandler jsonHandler = new JSONHandler();
		Iterator<Node> iterator;

		jsonFilePath = parseArguments(args);
		jsonHandler.read(jsonFilePath);
		iterator = jsonHandler.iterator();
		while (iterator.hasNext()) {
			System.out.print(iterator.next().getName());
		}
		System.out.println();
		jsonHandler.save("test.json");
	}
}
