package com.lf.bom.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Override
    public List<String> produce() {

        List<String> backedupFilenames = new ArrayList<>();

        for (String database : databases) {

            String backedFile = dumpData(database);
            if (backedFile != null) {
                backedupFilenames.add(backedFile);
                LOGGER.info("Backed up file: {}", backedFile);
            }
        }

        return backedupFilenames;
    }

    private String dumpData(String database) {

        try {

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String formattedNow = now.format(formatter);

            String command = mysqlDumpPath + " -u" +
                    mysqlUsername + " -p" +
                    mysqlPassword + " " +  database + " > " +
                    destinationLocation + database + "." + formattedNow + ".sql";

            LOGGER.debug("Mysql dump command: " + command);

            String[] commands = new String[] {"cmd.exe", "/c", command};


            Process exec = Runtime.getRuntime().exec(commands);

            if (exec.waitFor() == 0) {
                InputStream inputStream = exec.getInputStream();
                byte[] buffer = new byte[inputStream.available()];
                int a = inputStream.read(buffer);
                LOGGER.info("Mysql dump success with [{}]", a);
                return destinationLocation + database + ".sql";
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
