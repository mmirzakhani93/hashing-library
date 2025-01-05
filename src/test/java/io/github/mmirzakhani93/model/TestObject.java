package io.github.mmirzakhani93.model;

import io.github.mmirzakhani93.HashableField;

public class TestObject {

    @HashableField(order = 1)
    private final String name;

    @HashableField(order = 2)
    private final Integer age;

    public TestObject(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
