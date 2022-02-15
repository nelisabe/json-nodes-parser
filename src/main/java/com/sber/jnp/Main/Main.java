package com.sber.jnp.Main;

import com.sber.jnp.app.JSONHandler;
import com.sber.jnp.app.JSONHandlerImpl;
import com.sber.jnp.app.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(JSONHandlerImpl.class);

	private static String	parseArguments(String[] args) {
		Arguments	arguments = new Arguments();

		arguments.parse(args);
		return arguments.getJsonFilePath();
	}

	public static void	main(String[] args) {
		String	jsonFilePath;
		JSONHandler jsonHandler = new JSONHandlerImpl();
		Iterator<Node> iterator;
		StringBuilder	stringBuilder = new StringBuilder();

		jsonFilePath = parseArguments(args);
		jsonHandler.read(jsonFilePath);
		iterator = jsonHandler.iterator();
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().getName());
		}
		logger.info("Result of json {} read: {}", jsonFilePath, stringBuilder);
		jsonHandler.save("test.json");
	}
}
