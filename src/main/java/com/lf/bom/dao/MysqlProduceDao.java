package com.lf.bom.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component("mysqlProduceDao")
public class MysqlProduceDao implements ProduceDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MysqlProduceDao.class);

    @Value("#{'${mysql.databases}'.split(',')}")
    private List<String> databases;

    @Value("${mysql.path.mysqldump}")
    private String mysqlDumpPath;

    @Value("${backup.file.destination.folder}")
    private String destinationLocation;

    @Value("${mysql.username}")
    private String mysqlUsername;
    @Value("${mysql.password}")
    private String mysqlPassword;

    @Autowired
    private Runtime runtime;

    @Resource(name = "now")
    private String now;

    @Override
    public List<String> produce() {

        List<String> backedUpFilenames = new ArrayList<>();

        for (String database : databases) {

            String backedFile = dumpData(database);
            if (backedFile != null) {
                backedUpFilenames.add(backedFile);
                LOGGER.info("Backed up file: {}", backedFile);
            }
        }

        return backedUpFilenames;
    }

    private String dumpData(String database) {

        try {

            String backedUpFilename = destinationLocation + database + "." + now + ".sql";

            String command = mysqlDumpPath + " -u" +
                    mysqlUsername + " -p" +
                    mysqlPassword + " " +  database + " > " + backedUpFilename;

            LOGGER.debug("Mysql dump command: " + command);

            String[] commands = new String[] {"cmd.exe", "/c", command};

            Process exec = runtime.exec(commands);

            if (exec.waitFor() == 0) {
                LOGGER.info("Mysql dump success with");
                return backedUpFilename;
            } else {
                InputStream errorStream = exec.getErrorStream();
                byte[] buffer = new byte[errorStream.available()];
                int a = errorStream.read(buffer);
                LOGGER.error("Mysql dump console results: length: [{}] : ", a);
                LOGGER.error(new String(buffer));
                return null;
            }
        } catch (InterruptedException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }
}
