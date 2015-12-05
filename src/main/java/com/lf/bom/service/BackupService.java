package com.lf.bom.service;


import com.lf.bom.dao.CommitDao;
import com.lf.bom.dao.ProduceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BackupService {

    @Autowired
    @Qualifier("mysqlProduceDao")
    private ProduceDao produceDao;

    @Autowired
    @Qualifier("googleDriveCommitDao")
    private CommitDao commitDao;

    public void run() {
        List<String> backedUpFilenames = produceDao.produce();
        commitDao.commit(backedUpFilenames);
    }

}
