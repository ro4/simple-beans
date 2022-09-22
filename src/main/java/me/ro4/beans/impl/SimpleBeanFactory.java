package me.ro4.beans.impl;

import me.ro4.beans.*;
import me.ro4.beans.io.ResourcePatternResolver;
import me.ro4.beans.io.SimpleResourcePatternResolver;
import me.ro4.beans.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanFactory implements BeanFactory {
    private final ResourcePatternResolver resourcePatternResolver;

    private final InstantiationStrategy instantiationStrategy;

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(64);

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    private final ThreadLocal<Set<String>> currentInCreation = ThreadLocal.withInitial(HashSet::new);

    public SimpleBeanFactory() {
        this.instantiationStrategy = new SimpleInstantiationStrategy();
        this.resourcePatternResolver = new SimpleResourcePatternResolver();
    }

    public SimpleBeanFactory(InstantiationStrategy instantiationStrategy, ResourcePatternResolver resourcePatternResolver) {
        this.instantiationStrategy = instantiationStrategy;
        this.resourcePatternResolver = resourcePatternResolver;
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
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                bean = beanPostProcessor.postProcessBeforeInitialization(bean, name);
            }
            if (null == bean) {
                bean = createBean(beanDefinition);
            }
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
        List<String> matches = findCandidates(requiredType);
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
    public <T> Map<String, T> getBeans(Class<T> requiredType) {
        List<String> matches = findCandidates(requiredType);
        if (matches.isEmpty()) {
            return null;
        }
        Map<String, T> map = new HashMap<>(matches.size());
        for (String match: matches) {
            map.put(match, getBean(match, requiredType));
        }
        return map;
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
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessor.setBeanFactory(this);
        beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor) {
        beanFactoryPostProcessors.add(beanFactoryPostProcessor);
    }

    protected List<String> findCandidates(Class<?> requiredType) {
        List<String> matches = new ArrayList<>(2);
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String name = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            try {
                if (requiredType.isAssignableFrom(Class.forName(beanDefinition.getClassName()))) {
                    matches.add(name);
                }
            } catch (ClassNotFoundException ignore) {
            }
        }
        return matches;
    }

    protected Object createBean(BeanDefinition beanDefinition) {
        try {
            if (beanDefinition.isAbstract()) {
                Class<?> factoryBeanClass = Class.forName(beanDefinition.getFactoryBeanName());
                Object factoryBean = getBean(factoryBeanClass);
                if (Objects.isNull(factoryBean)) {
                    return null;
                }
                Method factoryMethod = factoryBeanClass.getDeclaredMethod(beanDefinition.getFactoryMethodName());
                factoryMethod.setAccessible(true);
                return factoryMethod.invoke(factoryBean);
            }
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException ignore) {

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return this.instantiationStrategy.instantiate(beanDefinition);
    }
}
