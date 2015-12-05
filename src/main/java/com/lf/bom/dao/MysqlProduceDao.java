package com.lf.bom.dao;

import org.springframework.stereotype.Component;

import java.io.File;

@Component("mysqlProduceDao")
public class MysqlProduceDao implements ProduceDao {

    @Override
    public File produceBackup() {
        return null;
    }
}
