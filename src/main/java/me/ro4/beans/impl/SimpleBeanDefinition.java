package me.ro4.beans.impl;

import me.ro4.beans.BeanDefinition;
import me.ro4.beans.PropertyValue;
import me.ro4.beans.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanDefinition implements BeanDefinition {
    private String className;

    private ScopeEnum scope = ScopeEnum.SINGLETON;

    private AutowiredModelEnum autowiredModel = AutowiredModelEnum.BY_TYPE;

    private final Map<String, PropertyValue> propertyValues = new ConcurrentHashMap<>(256);

    private boolean primary = false;

    @Override
    public String getClassName() {
        return this.className;
    }

    @Override
    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public ScopeEnum getScope() {
        return this.scope;
    }

    @Override
    public void setScope(ScopeEnum scope) {
        this.scope = scope;
    }

    @Override
    public AutowiredModelEnum getAutowiredModel() {
        return autowiredModel;
    }

    @Override
    public void setAutowiredModel(AutowiredModelEnum autowiredModel) {
        this.autowiredModel = autowiredModel;
    }

    @Override
    public boolean isAutowired() {
        return this.autowiredModel != AutowiredModelEnum.NO;
    }

    @Override
    public boolean isSingleton() {
        return ScopeEnum.SINGLETON.equals(scope);
    }

    @Override
    public boolean isPrototype() {
        return ScopeEnum.PROTOTYPE.equals(scope);
    }

    @Override
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    @Override
    public boolean isPrimary() {
        return primary;
    }

    @Override
    public void addPropertyValue(String name, Object value) {
        Assert.isNotNull(name, "property name can not be null");
        this.propertyValues.put(name, new PropertyValue(name, value));
    }

    @Override
    public void removePropertyValue(String name) {
        Assert.isNotNull(name, "property name can not be null");
        this.propertyValues.remove(name);
    }

    @Override
    public void clearPropertyValue() {
        this.propertyValues.clear();
    }

    @Override
    public List<PropertyValue> getPropertyValues() {
        return new ArrayList<>(this.propertyValues.values());
    }
}
