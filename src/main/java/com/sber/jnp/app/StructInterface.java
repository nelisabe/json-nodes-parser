package com.sber.jnp.app;

import java.util.Iterator;
import java.util.function.BinaryOperator;

public interface StructInterface {
	void        read(String jsonFile);
	void        save(String jsonFile);
	Iterator    getNode(String path);
	<T> Iterator    iterator(BinaryOperator<T> operator);
}
