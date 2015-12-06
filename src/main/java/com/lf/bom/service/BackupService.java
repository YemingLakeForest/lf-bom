package com.lf.bom.service;


import com.lf.bom.dao.CommitDao;
import com.lf.bom.dao.ProduceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BackupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackupService.class);

    @Autowired
    @Qualifier("mysqlProduceDao")
    private ProduceDao produceDao;

    @Autowired
    @Qualifier("googleDriveCommitDao")
    private CommitDao commitDao;

    public void run() {

        LOGGER.info("Producing backup files...");

        List<String> backedUpFilenames = produceDao.produce();

        LOGGER.info("Produced {} backup files.", backedUpFilenames.size());

        commitDao.commit(backedUpFilenames);

        LOGGER.info("All done and checking out...");
    }

}
