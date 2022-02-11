package com.sber.jnp.app;

import java.util.ArrayList;

/**
 * Class represents one Node in json tree.
 */
public class Node {
	private String  		name;
	private Color   		color;
	private int     		value;
	private ArrayList<Node> children;

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
