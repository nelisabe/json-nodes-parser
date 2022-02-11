package com.sber.jnp.app;

import com.sber.jnp.app.exceptions.InvalidNodeValueException;

import java.util.ArrayList;

/**
 * Class represents one Node in json tree.
 */
public class Node {
	private final String  		name;
	private final Color   		color;
	private final int     		value;
	private final ArrayList<Node> children;

	public Node(String name, Color color, int value) {
		checkIntValueValid(value);
		this.name = name;
		this.color = color;
		this.value = value;
		this.children = new ArrayList<>();
	}

	public Node(String name, Color color, int value, ArrayList<Node> children) {
		checkIntValueValid(value);
		this.name = name;
		this.color = color;
		this.value = value;
		this.children = children;
	}

	private void	checkIntValueValid(int value) {
		if (value < 0 || value > 100)
			throw new InvalidNodeValueException("Invalid int value.");
	}

	public String	getName() {
		return name;
	}

	public int 		getValue() {
		return value;
	}

	public Color	getColor() {
		return color;
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	@Override
	public String	toString() {
		return "name: " + name + ", value: " + value + ", color: " + color.toString();
	}
}
