package io.github.mmirzakhani93.model;

import io.github.mmirzakhani93.HashableField;

public class ParentObject {

    @HashableField(order = 1)
    private final String name;

    @HashableField(order = 2)
    private final Integer age;

    @HashableField(order = 3)
    private final TestObject child;

    public ParentObject(String name, Integer age, TestObject child) {
        this.name = name;
        this.age = age;
        this.child = child;
    }
}
