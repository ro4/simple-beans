package me.ro4.beans.beanexample;

import me.ro4.beans.annotation.Autowired;

public class PersonBean {
    @Autowired
    SimpleBean name;

    SimpleBean age;

    SimpleBean foot;

    @Autowired
    PersonBean(SimpleBean foot) {
        this.foot = foot;
    }

    @Autowired
    public void setAge(SimpleBean age) {
        this.age = age;
    }

    public SimpleBean getAge() {
        return this.age;
    }

    public SimpleBean getName() {
        return name;
    }

    public SimpleBean getFoot() {
        return this.foot;
    }
}
