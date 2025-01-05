package io.github.mmirzakhani93.model;

import io.github.mmirzakhani93.HashableField;

import java.util.List;

public class ParentObjectWithList {

    @HashableField(order = 1)
    private final String name;

    @HashableField(order = 2)
    private final Integer age;

    @HashableField(order = 3)
    private final List<TestObject> children;

    public ParentObjectWithList(String name, Integer age, List<TestObject> children) {
        this.name = name;
        this.age = age;
        this.children = children;
    }
}
