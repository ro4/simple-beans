package me.ro4.beans.annotationconfig;

import me.ro4.beans.BeanFactory;
import me.ro4.beans.annotation.Bean;
import me.ro4.beans.annotation.Configuration;
import me.ro4.beans.beanexample.SimpleBean;
import me.ro4.beans.impl.SimpleAnnotationConfigRegistry;
import me.ro4.beans.impl.SimpleBeanFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unused")
public class AnnotationConfigRegistryTest {

    BeanFactory beanFactory;

    @Before
    public void setUp() {
        this.beanFactory = new SimpleBeanFactory();
    }

    @Test
    public void registryTest() {
        SimpleAnnotationConfigRegistry registry = new SimpleAnnotationConfigRegistry();
        registry.setBeanFactory(beanFactory);
        registry.register(Config.class);
        SimpleBean sb = beanFactory.getBean("simpleBean", SimpleBean.class);
        Assert.assertNotNull(sb);
        Assert.assertEquals("@Bean", sb.getName());
    }

    @Configuration
    public static class Config {
        @Bean
        SimpleBean simpleBean() {
            SimpleBean sb = new SimpleBean();
            sb.setName("@Bean");
            return sb;
        }
    }
}
