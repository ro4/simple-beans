package me.ro4.beans;

import me.ro4.beans.util.Assert;

public class PropertyValue {
    private final String name;

    private final Object value;

    public PropertyValue(String name, Object value) {
        Assert.isNotNull(name, "property name can not be null");
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
