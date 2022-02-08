package com.sber.jnp.app;

import java.util.ArrayList;

public class Node {
	private String  		name;
	private Color   		color; // red, green, blue
	private int     		value; // 0 - 100
	private ArrayList<Node> children;

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

	public Color getColor() {
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
