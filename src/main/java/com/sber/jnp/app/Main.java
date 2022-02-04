package com.sber.jnp.app;

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

		jsonFilePath = parseArguments(args);
		jsonHandler.read(jsonFilePath);
		jsonHandler.save("test.json");
	}
}
