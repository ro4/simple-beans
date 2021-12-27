package me.ro4.beans.impl;

import me.ro4.beans.AnnotationConfigRegistry;
import me.ro4.beans.BeanFactory;
import me.ro4.beans.BeanFactoryAware;
import me.ro4.beans.annotation.Bean;
import me.ro4.beans.util.Assert;

import java.lang.reflect.Method;
import java.util.Objects;

public class SimpleAnnotationConfigRegistry implements AnnotationConfigRegistry, BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void register(Class<?>... componentClasses) {
        Assert.isNotEmpty(componentClasses, "component classes is not allow null");
        for (Class<?> componentClass : componentClasses) {
            SimpleBeanDefinition componentBD = new SimpleBeanDefinition();
            componentBD.setClassName(componentClass.getName());
            beanFactory.registerBeanDefinition("component" + componentClass.getSimpleName(), componentBD);
            Method[] methods = componentClass.getDeclaredMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(Bean.class)) {
                    continue;
                }
                SimpleBeanDefinition sbd = new SimpleBeanDefinition();
                sbd.setClassName(method.getReturnType().getName());
                sbd.setAbstract(true);
                sbd.setFactoryBeanName(componentClass.getName());
                sbd.setFactoryMethodName(method.getName());
                beanFactory.registerBeanDefinition(method.getName(), sbd);
            }
        }
    }
}
