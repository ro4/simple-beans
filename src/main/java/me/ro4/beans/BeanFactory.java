package me.ro4.beans;

import java.util.Map;

public interface BeanFactory {
    Object getBean(String name);

    <T> T getBean(Class<T> requiredType);

    <T> Map<String, T> getBeans(Class<T> requiredType);

    <T> T getBean(String name, Class<T> requiredType);

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
    BeanDefinition getBeanDefinition(String beanName);

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor);
}
