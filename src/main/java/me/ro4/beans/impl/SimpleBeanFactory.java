package me.ro4.beans.impl;

import me.ro4.beans.BeanDefinition;
import me.ro4.beans.BeanFactory;
import me.ro4.beans.BeanPostProcessor;
import me.ro4.beans.InstantiationStrategy;
import me.ro4.beans.annotation.Autowired;
import me.ro4.beans.util.Assert;
import sun.reflect.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanFactory implements BeanFactory {
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(64);

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final InstantiationStrategy instantiationStrategy;

    private final ThreadLocal<Set<String>> currentInCreation = ThreadLocal.withInitial(HashSet::new);
//            Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    public SimpleBeanFactory() {
        this.instantiationStrategy = new SimpleInstantiationStrategy();
    }

    public SimpleBeanFactory(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    @Override
    public Object getBean(String name) {
        BeanDefinition beanDefinition = beanDefinitionMap.getOrDefault(name, null);
        if (null == beanDefinition) {
            return null;
        }
        Object bean = null;
        if (beanDefinition.isSingleton()) {
            bean = singletonObjects.getOrDefault(name, null);
        }
        if (null == bean) {
            bean = createBean(beanDefinition);
            if (beanDefinition.isSingleton() && bean != null) {
                singletonObjects.put(name, bean);
            }
        }
        if (currentInCreation.get().contains(name)) {
            if (beanDefinition.isPrototype()) {
                currentInCreation.get().remove(name);
                throw new UnsupportedOperationException("prototype bean do not support circular reference");
            }
            return bean;
        }
        currentInCreation.get().add(name);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            beanPostProcessor.postProcessAfterInitialization(bean, name);
        }
        currentInCreation.get().remove(name);
        return bean;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        List<String> matches = new ArrayList<>(2);
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String name = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if (beanDefinition.getClassName().equals(requiredType.getName())) {
                matches.add(name);
            }
        }
        int size = matches.size();
        if (size == 0) {
            return null;
        }

        if (size == 1) {
            return getBean(matches.get(0), requiredType);
        }
        throw new IllegalArgumentException("ambiguous required type for " + requiredType.getName()
                + ", found " + size + " bean names: " + String.join(", ", matches));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name, Class<T> requiredType) {
        Assert.isNotNull(requiredType, "required type can not be null");
        Object bean = getBean(name);
        if (null == bean) {
            return null;
        }
        if (!requiredType.isInstance(bean)) {
            throw new IllegalArgumentException("bean named " + name + " is not type " + requiredType.getName());
        }
        return (T) bean;
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        Assert.isNotNull(beanName, "bean definition name can not be mull");
        Assert.isNotNull(beanDefinition, "bean definition can not be null");
        if (beanDefinitionMap.containsKey(beanName) && beanDefinitionMap.get(beanName).isSingleton()) {
            singletonObjects.remove(beanName);
        }
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessor.setBeanFactory(this);
        beanPostProcessors.add(beanPostProcessor);
    }

    protected Object createBean(BeanDefinition beanDefinition) {
        try {
            if (beanDefinition.isAbstract()) {
                Class<?> factoryBeanClass = Class.forName(beanDefinition.getFactoryBeanName());
                Object factoryBean = getBean(factoryBeanClass);
                Method factoryMethod = factoryBeanClass.getDeclaredMethod(beanDefinition.getFactoryMethodName());
                factoryMethod.setAccessible(true);
                return factoryMethod.invoke(factoryBean);
            }
            // constructor injection
            Class<?> clazz = Class.forName(beanDefinition.getClassName());
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                Autowired[] annotations = constructor.getAnnotationsByType(Autowired.class);
                Parameter[] parameters = constructor.getParameters();
                if (annotations.length < 1 || parameters.length < 1) {
                    continue;
                }
                Object[] params = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    params[i] = this.getBean(parameters[i].getType());
                }
                constructor.setAccessible(true);
                return constructor.newInstance(params);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException ignore) {

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return this.instantiationStrategy.instantiate(beanDefinition);
    }
}
