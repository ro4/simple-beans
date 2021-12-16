package me.ro4.beans;

import me.ro4.beans.beanexample.FatherBean;
import me.ro4.beans.beanexample.PersonBean;
import me.ro4.beans.beanexample.SimpleBean;
import me.ro4.beans.beanexample.SonBean;
import me.ro4.beans.impl.SimpleBeanDefinition;
import me.ro4.beans.impl.SimpleBeanFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BeanFactoryAutowiredTest {
    BeanFactory beanFactory;

    @Before
    public void setUp() {
        this.beanFactory = new SimpleBeanFactory();
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    @Test
    public void autowiredFieldTest() {
        BeanDefinition beanDefinition = new SimpleBeanDefinition();
        beanDefinition.setClassName(SimpleBean.class.getName());
        beanFactory.registerBeanDefinition("sb", beanDefinition);

        BeanDefinition personBD = new SimpleBeanDefinition();
        personBD.setClassName(PersonBean.class.getName());
        beanFactory.registerBeanDefinition("person", personBD);

        PersonBean pb = beanFactory.getBean("person", PersonBean.class);
        Assert.assertNotNull(pb);
        Assert.assertNotNull(pb.getName());
        Assert.assertNotNull(pb.getAge());
        Assert.assertNotNull(pb.getFoot());
    }

    @Test
    public void singletonCircularReferenceTest() {
        BeanDefinition fatherDefinition = new SimpleBeanDefinition();
        fatherDefinition.setClassName(FatherBean.class.getName());
        beanFactory.registerBeanDefinition("father", fatherDefinition);

        BeanDefinition sonDefinition = new SimpleBeanDefinition();
        sonDefinition.setClassName(SonBean.class.getName());
        beanFactory.registerBeanDefinition("son", sonDefinition);

        FatherBean father = beanFactory.getBean("father", FatherBean.class);
        SonBean son = beanFactory.getBean("son", SonBean.class);

        Assert.assertNotNull(father);
        Assert.assertNotNull(son);
    }

    @Test
    public void prototypeCircularReferenceTest() {
        BeanDefinition fatherDefinition = new SimpleBeanDefinition();
        fatherDefinition.setClassName(FatherBean.class.getName());
        fatherDefinition.setScope(BeanDefinition.ScopeEnum.PROTOTYPE);
        beanFactory.registerBeanDefinition("father", fatherDefinition);

        BeanDefinition sonDefinition = new SimpleBeanDefinition();
        sonDefinition.setClassName(SonBean.class.getName());
        sonDefinition.setScope(BeanDefinition.ScopeEnum.PROTOTYPE);
        beanFactory.registerBeanDefinition("son", sonDefinition);
        try {
            FatherBean father = beanFactory.getBean("father", FatherBean.class);
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), UnsupportedOperationException.class);
        }
    }
}
