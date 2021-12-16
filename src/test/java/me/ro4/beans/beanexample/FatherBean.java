package me.ro4.beans.beanexample;

import me.ro4.beans.annotation.Autowired;

public class FatherBean {
    @Autowired
    SonBean sonBean;

    @Autowired
    SonBean secondSon;

    SonBean thirdSon;

    @Autowired
    FatherBean(SonBean thirdSon) {
        this.thirdSon = thirdSon;
    }
}
