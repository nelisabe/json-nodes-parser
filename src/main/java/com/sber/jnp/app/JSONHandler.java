package com.sber.jnp.app;

import java.util.Iterator;
import java.util.function.BinaryOperator;

public interface JSONHandler {
	void	read(String jsonFilePath);
	void	save(String jsonFilePath);
	void 	add(Node newNode, String path);
	void 	add(String name, Color color, int value, String path);
	Node	getNode(String path);
	Iterator<Node>	iterator();
	Iterator<Node>	iterator(BinaryOperator<Node> operator);
}
