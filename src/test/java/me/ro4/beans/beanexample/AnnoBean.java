package me.ro4.beans.beanexample;

import me.ro4.beans.annotation.Autowired;
import me.ro4.beans.annotation.Bean;
import me.ro4.beans.annotation.Component;

@Component
public class AnnoBean {

    @Autowired
    @Bean
    public void hello() {

    }
}
