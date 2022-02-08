package com.sber.jnp.app;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.function.BinaryOperator;

class JSONIterator implements Iterator<Node> {
	private final Node					firstNode;
	private final Stack<Node> 			nodesQueue;
	private final HashSet<Node> 		passedNodes;
	private final BinaryOperator<Node> 	operator;

	public JSONIterator(Node firstNode) {
		this.firstNode = firstNode;
		operator = (x, y) -> x.getValue() < y.getValue() ? x : y;

		nodesQueue = new Stack<>();
		passedNodes = new HashSet<>();
		init();
	}

	public JSONIterator(Node firstNode, BinaryOperator<Node> operator) {
		this.firstNode = firstNode;
		this.operator = operator;

		nodesQueue = new Stack<>();
		passedNodes = new HashSet<>();
		init();
	}

	private void	init() {
		nodesQueue.push(firstNode);
	}

	public boolean	hasNext() {
		if (!nodesQueue.empty()) {
			if (nodesQueue.peek().equals(firstNode))
				return !wasAllChildrenPassed(firstNode);
			return true;
		}
		return false;
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
			if (!nodesQueue.empty() && wasAllChildrenPassed(nodesQueue.peek()))
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
