package com.sber.jnp.app;

import java.util.Iterator;
import java.util.function.BinaryOperator;

public interface JSONHandler {
	/**
	 * Initializes new representation of json tree. Previous nodes will be deleted.
	 * @param firstNode - {@link Node} object that will be first in new tree.
	 */
	void	create(Node firstNode);

	/**
	 * Initializes new representation of json tree. Previous nodes will be deleted.
	 * <b>name</b>, <b>color</b> and <b>value</b> are parameters of first {@link Node}
	 * in new tree;
	 */
	void	create(String name, Color color, int value);
	void	read(String jsonFilePath);
	void	save(String jsonFilePath);
	void 	add(Node newNode, String path);
	void 	add(String name, Color color, int value, String path);
	Node	getNode(String path);
	Iterator<Node>	iterator();
	Iterator<Node>	iterator(BinaryOperator<Node> operator);
}
