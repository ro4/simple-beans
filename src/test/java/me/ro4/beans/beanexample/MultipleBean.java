package me.ro4.beans.beanexample;

import me.ro4.beans.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class MultipleBean {
    @Autowired
    Map<String, Human> humanMap;

    @Autowired
    List<Human> humanList;

    public Map<String, Human> getHumanMap() {
        return humanMap;
    }

    public List<Human> getHumanList() {
        return humanList;
    }
}
