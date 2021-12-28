package me.ro4.beans;

public interface BeanFactoryAware extends Aware {
    void setBeanFactory(BeanFactory beanFactory);
}
