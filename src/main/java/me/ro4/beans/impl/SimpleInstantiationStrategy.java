package me.ro4.beans.impl;

import me.ro4.beans.BeanDefinition;
import me.ro4.beans.InstantiationStrategy;
import me.ro4.beans.PropertyValue;
import me.ro4.beans.util.Assert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class SimpleInstantiationStrategy implements InstantiationStrategy {
    @Override
    public Object instantiate(BeanDefinition beanDefinition) {
        Assert.isNotNull(beanDefinition, "bean definition can not be null");
        Object bean = null;
        try {
            Class<?> beanClazz = Class.forName(beanDefinition.getClassName());
            Constructor<?> constructor = beanClazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            bean = constructor.newInstance();
            List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
            if (null != propertyValues && !propertyValues.isEmpty()) {
                for (PropertyValue propertyValue : propertyValues) {
                    try {
                        Field field = beanClazz.getDeclaredField(propertyValue.getName());
                        field.setAccessible(true);
                        field.set(bean, propertyValue.getValue());
                    } catch (NoSuchFieldException ignore) {
                    }
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return bean;
    }
}
