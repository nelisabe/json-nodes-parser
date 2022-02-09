package com.sber.jnp.app;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.function.BinaryOperator;

class JSONIterator implements Iterator<Node> {
	private final Stack<Node> 			nodesQueue;
	private final HashSet<Node> 		passedNodes;
	private final BinaryOperator<Node> 	operator;

	public JSONIterator(Node firstNode, BinaryOperator<Node> operator) {
		this.operator = operator;

		nodesQueue = new Stack<>();
		passedNodes = new HashSet<>();
		nodesQueue.push(firstNode);
	}

	public boolean	hasNext() {
		return !nodesQueue.empty();
	}

	public Node		next() {
		Node	current;

		if (nodesQueue.empty())
			throw new NoSuchElementException();
		current = nodesQueue.peek();
		if (wasAllChildrenPassed(current))
			nodesQueue.pop();
		else
			nodesQueue.push(selectChild(current));
		if (!passedNodes.add(current)) {
			current = next();
			while (!nodesQueue.empty() && wasAllChildrenPassed(nodesQueue.peek()))
				nodesQueue.pop();
		}
		return current;
	}

	private boolean wasAllChildrenPassed(Node node) {
		for (Node child : node.getChildren()) {
			if (!passedNodes.contains(child))
				return false;
		}
		return true;
	}

	private Node	selectChild(Node parent) {
		Node	result;

		result = null;
		for (Node child : parent.getChildren()) {
			if (!passedNodes.contains(child))
				result = result == null ? child : operator.apply(result, child);
		}
		return result;
	}
}
