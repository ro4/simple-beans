package me.ro4.beans;

import java.util.List;

public interface BeanDefinition {

    String getClassName();

    void setClassName(String className);

    ScopeEnum getScope();

    void setScope(ScopeEnum scope);

    AutowiredModelEnum getAutowiredModel();

    void setAutowiredModel(AutowiredModelEnum autowiredModel);

    boolean isAutowired();

    boolean isSingleton();

    boolean isPrototype();

    public void setAbstract(boolean abstractFlag);

    public boolean isAbstract();

    void setPrimary(boolean primary);

    boolean isPrimary();

    void addPropertyValue(String name, Object value);

    void removePropertyValue(String name);

    void clearPropertyValue();

    List<PropertyValue> getPropertyValues();

    void setFactoryBeanName(String factoryBeanName);

    String getFactoryBeanName();

    void setFactoryMethodName(String factoryMethodName);

    String getFactoryMethodName();

    enum ScopeEnum {
        SINGLETON,
        PROTOTYPE
    }

    enum AutowiredModelEnum {
        NO,
        BY_TYPE,
        BY_NAME,
        BY_CONSTRUCTOR
    }
}
