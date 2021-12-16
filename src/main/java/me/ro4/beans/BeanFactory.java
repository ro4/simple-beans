package me.ro4.beans;

public interface BeanFactory {
    Object getBean(String name);

    <T> T getBean(Class<T> requiredType);

    <T> T getBean(String name, Class<T> requiredType);

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
