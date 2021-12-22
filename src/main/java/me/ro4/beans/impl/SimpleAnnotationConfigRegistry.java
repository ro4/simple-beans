package me.ro4.beans.impl;

import me.ro4.beans.AnnotationConfigRegistry;
import me.ro4.beans.BeanFactory;
import me.ro4.beans.BeanFactoryAware;
import me.ro4.beans.util.Assert;

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
            SimpleBeanDefinition sbd = new SimpleBeanDefinition();
//            beanFactory.registerBeanDefinition();
        }
    }
}
