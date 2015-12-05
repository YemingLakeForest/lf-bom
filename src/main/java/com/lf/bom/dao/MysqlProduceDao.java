package com.lf.bom.dao;

import org.springframework.stereotype.Component;

import java.util.List;

@Component("mysqlProduceDao")
public class MysqlProduceDao implements ProduceDao {

    @Override
    public List<String> produce() {
        return null;
    }
}
