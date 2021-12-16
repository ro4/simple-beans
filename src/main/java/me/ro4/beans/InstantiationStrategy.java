package me.ro4.beans;

public interface InstantiationStrategy {
    Object instantiate(BeanDefinition beanDefinition);
}
