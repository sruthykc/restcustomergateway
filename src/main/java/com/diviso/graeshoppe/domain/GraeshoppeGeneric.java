package com.diviso.graeshoppe.domain;

public class GraeshoppeGeneric<T> {
	T obj;

	void add(T obj) {
		this.obj = obj;
	}

	T get() {
		return obj;
	}
}
