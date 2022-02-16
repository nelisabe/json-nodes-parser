package com.sber.jnp.app;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.function.BinaryOperator;

class JSONIterator implements Iterator<Node> {
	private final Stack<Node> nodesQueue;
	private final HashSet<Node> passedNodes;
	private final BinaryOperator<Node> operator;
	private String currentNodePath;

	public JSONIterator(Node firstNode, BinaryOperator<Node> operator) {
		this.operator = operator;
		this.nodesQueue = new Stack<>();
		this.passedNodes = new HashSet<>();
		this.nodesQueue.push(firstNode);
	}

	public boolean	hasNext() {
		return !nodesQueue.empty();
	}

	public Node		next() {
		Node current;

		if (this.nodesQueue.empty()) {
			throw new NoSuchElementException();
		}

		current = this.nodesQueue.peek();
		if (!this.passedNodes.contains(current)) {
			setCurrentNodePath();
		}

		if (wasAllChildrenPassed(current)) {
			this.nodesQueue.pop();
		} else {
			this.nodesQueue.push(selectChild(current));
		}

		if (!this.passedNodes.add(current)) {
			current = next();
			while (!this.nodesQueue.empty() &&
					wasAllChildrenPassed(this.nodesQueue.peek()))
				this.nodesQueue.pop();
		}
		return current;
	}

	private void	setCurrentNodePath() {
		StringBuilder stringBuilder = new StringBuilder();

		this.nodesQueue.forEach(node -> {
			stringBuilder.append(node.getName());
			stringBuilder.append("/");
		});
		this.currentNodePath = stringBuilder.toString();
	}

	private boolean wasAllChildrenPassed(Node node) {
		for (Node child : node.getChildren()) {
			if (!this.passedNodes.contains(child)) {
				return false;
			}
		}
		return true;
	}

	private Node	selectChild(Node parent) {
		Node result;

		result = null;
		for (Node child : parent.getChildren()) {
			if (!this.passedNodes.contains(child)) {
				result = result == null ? child : this.operator.apply(result, child);
			}
		}
		return result;
	}

	public String	getCurrentNodePath() {
		return currentNodePath;
	}
}
