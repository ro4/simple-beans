package me.ro4.beans;

import me.ro4.beans.beanexample.SimpleBean;
import me.ro4.beans.impl.SimpleBeanDefinition;
import me.ro4.beans.impl.SimpleBeanFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BeanFactoryTest {

    BeanFactory beanFactory;

    @Before
    public void setUp() {
        this.beanFactory = new SimpleBeanFactory();
    }

    @Test
    public void getSingletonBeanTest() {
        BeanDefinition beanDefinition = new SimpleBeanDefinition();
        beanDefinition.setClassName(SimpleBean.class.getName());
        beanFactory.registerBeanDefinition("sb", beanDefinition);
        SimpleBean sb = (SimpleBean) beanFactory.getBean("sb");
        SimpleBean sb1 = (SimpleBean) beanFactory.getBean("sb");

        Assert.assertNotNull(sb);
        Assert.assertNotNull(sb1);
        Assert.assertTrue("singleton scope is default", beanDefinition.isSingleton());
        Assert.assertEquals(BeanDefinition.ScopeEnum.SINGLETON, beanDefinition.getScope());
        Assert.assertEquals(sb, sb1);

        BeanDefinition samDefinition = new SimpleBeanDefinition();
        samDefinition.setClassName(SimpleBean.class.getName());
        samDefinition.addPropertyValue("name", "Sam");
        beanFactory.registerBeanDefinition("sam", samDefinition);
        SimpleBean sam = (SimpleBean) beanFactory.getBean("sam");
        Assert.assertEquals(sam.getName(), "Sam");
        Assert.assertFalse(samDefinition.getPropertyValues().isEmpty());
        samDefinition.clearPropertyValue();
        Assert.assertTrue(samDefinition.getPropertyValues().isEmpty());
        samDefinition.addPropertyValue("property2", null);
        Assert.assertFalse(samDefinition.getPropertyValues().isEmpty());
        samDefinition.removePropertyValue("property2");
        Assert.assertTrue(samDefinition.getPropertyValues().isEmpty());

        samDefinition.addPropertyValue("name", "Zhang");
        beanFactory.registerBeanDefinition("sam", samDefinition);
        sam = (SimpleBean) beanFactory.getBean("sam");
        Assert.assertEquals("Zhang", sam.getName());

        BeanFactory beanFactory1 = new SimpleBeanFactory(bd -> null);
        beanFactory1.registerBeanDefinition("sam", samDefinition);
        Assert.assertNull(beanFactory1.getBean("sam"));
    }

    @Test
    public void getPrototypeBeanTest() {
        BeanDefinition beanDefinition = new SimpleBeanDefinition();
        beanDefinition.setClassName(SimpleBean.class.getName());
        beanDefinition.setScope(BeanDefinition.ScopeEnum.PROTOTYPE);
        beanFactory.registerBeanDefinition("sb", beanDefinition);
        SimpleBean sb = (SimpleBean) beanFactory.getBean("sb");
        SimpleBean sb1 = (SimpleBean) beanFactory.getBean("sb");
        SimpleBean sb2 = (SimpleBean) beanFactory.getBean("not exist");

        Assert.assertNotNull(sb);
        Assert.assertNotNull(sb1);
        Assert.assertNull(sb2);
        Assert.assertFalse(beanDefinition.isSingleton());
        Assert.assertTrue(beanDefinition.isPrototype());
        Assert.assertEquals(BeanDefinition.ScopeEnum.PROTOTYPE, beanDefinition.getScope());
        Assert.assertNotEquals(sb, sb1);
    }

    @Test
    public void getBeanByTypeTest() {
        BeanDefinition beanDefinition = new SimpleBeanDefinition();
        beanDefinition.setClassName(SimpleBean.class.getName());
        beanDefinition.setScope(BeanDefinition.ScopeEnum.PROTOTYPE);
        beanFactory.registerBeanDefinition("sb", beanDefinition);
        Assert.assertNotNull(beanFactory.getBean(SimpleBean.class));

        Exception e = null;
        beanFactory.registerBeanDefinition("sb2", beanDefinition);
        try {
            beanFactory.getBean(SimpleBean.class);
        } catch (IllegalArgumentException ex) {
            e = ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(e.getClass(), IllegalArgumentException.class);

        Assert.assertNull(beanFactory.getBean("no", SimpleBean.class));
    }
}
