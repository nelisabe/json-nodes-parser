package com.sber.jnp.app;

import java.util.Iterator;
import java.util.function.BinaryOperator;

/**
 * Json tree handler interface.
 * @version 0.1
 */
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

	/**
	 * Tries to read input <b>jsonFilePath</b> file and convert in to {@link Node} object.
	 * @param jsonFilePath - path to json file to handle.
	 */
	void	read(String jsonFilePath);

	/**
	 * Tries to create <b>jsonFilePath</b> outfile and save converted {@link Node} object into it.
	 * @param jsonFilePath - path to create json file.
	 */
	void	save(String jsonFilePath);

	/**
	 * Adds new {@link Node} object to children of specified node.
	 * @param newNode - new node.
	 * @param path - path to find node where new node will add.
	 */
	void 	add(Node newNode, String path);

	/**
	 * Adds new {@link Node} object to children of specified node.
	 * @param name - name of new node.
	 * @param color - color of new node.
	 * @param value - value of new node.
	 * @param path - path to find node where new node will add.
	 */
	void 	add(String name, Color color, int value, String path);

	void deleteWithChildren(String pathToNode);

	void deleteWithoutChildren(String pathToNode);

	/**
	 * Finds {@link Node} node in json tree that corresponds to specified path.
	 * @param path - NodeName1/NodeName2/NodeName3/ form path to find node.
	 * @return {@link Node} object.
	 */
	Node	getNode(String path);

	/**
	 * Creates an {@link Iterator<Node>} object, that  can iterate through json tree object.
	 * @return {@link Iterator<Node>} object.
	 * with default behavior.
	 */
	Iterator<Node>	iterator();

	/**
	 * Creates an {@link Iterator<Node>} object, that  can iterate through json tree object
	 * with custom behavior.
	 * @param operator - {@link BinaryOperator<Node>} describes special rule to iterate through tree.
	 * @return {@link Iterator<Node>} object.
	 */
	Iterator<Node>	iterator(BinaryOperator<Node> operator);
}
