package com.sber.jnp.app;

import com.sber.jnp.app.exceptions.InvalidArgumentsException;

public class Main {
	private static String	parseArguments(String[] args) {
		Arguments	arguments;
		String		jsonFilePath;

		try {
			arguments = new Arguments();
			arguments.parse(args);
			jsonFilePath = arguments.getJsonFilePath();
		} catch (Exception ex) {
			throw new InvalidArgumentsException(ex);
		}
		return jsonFilePath;
	}

	public static void	main(String[] args) {
		String	jsonFilePath;
		JSONHandler jsonHandler = new JSONHandler();

		jsonFilePath = parseArguments(args);
		jsonHandler.read(jsonFilePath);
	}
}
