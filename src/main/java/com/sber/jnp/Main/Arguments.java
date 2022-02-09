package com.sber.jnp.Main;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = " =")
public class Arguments {
	@Parameter(names = "--jsonFile", required = true, arity = 1)
	private String	jsonFilePath;

	public void 	parse(String[] args) {
		JCommander.newBuilder().addObject(this).build().parse(args);
	}

	public String	getJsonFilePath() {
		return jsonFilePath;
	}
}
