package me.ro4.beans;

import me.ro4.beans.annotation.Autowired;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        // constructor injection
        BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
        if (null == bd) {
            return bean;
        }
        try {
            Class<?> clazz = Class.forName(bd.getClassName());
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                Parameter[] parameters = constructor.getParameters();
                if (!constructor.isAnnotationPresent(Autowired.class) || parameters.length < 1) {
                    continue;
                }
                Object[] params = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    params[i] = beanFactory.getBean(parameters[i].getType());
                }
                constructor.setAccessible(true);
                return constructor.newInstance(params);
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                 InvocationTargetException ignore) {

        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        // field autowired injection
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Autowired.class)) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object injectVal;
                if (Map.class == field.getType()) {
                    // map autowired
                    Type[] type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                    if (type.length != 2) {
                        return null;
                    }
                    injectVal = beanFactory.getBeans((Class<?>) type[1]);
                } else if (List.class == field.getType()) {
                    Type[] type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                    injectVal = new ArrayList<>(beanFactory.getBeans((Class<?>) type[0]).values());
                } else {
                    injectVal = beanFactory.getBean(field.getType());
                }
                field.set(bean, injectVal);
            } catch (IllegalArgumentException | IllegalAccessException ignore) {
            }
        }

        // method autowired injection
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Autowired.class)) {
                continue;
            }
            method.setAccessible(true);
            Parameter[] parameters = method.getParameters();
            Object[] params = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                params[i] = beanFactory.getBean(parameters[i].getType());
            }
            try {
                method.invoke(bean, params);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ignore) {
            }
        }
        return bean;
    }
}
