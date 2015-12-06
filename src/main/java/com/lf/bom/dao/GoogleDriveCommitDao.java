package com.lf.bom.dao;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component("googleDriveCommitDao")
public class GoogleDriveCommitDao implements CommitDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleDriveCommitDao.class);

    @Autowired
    private Drive service;

    @Override
    public void commit(List<String> filePaths) {
        for (String filePath : filePaths) {
            sendFile(filePath);
        }
    }

    private void sendFile(String filePath) {

        int index = filePath.lastIndexOf("\\");
        String fileName = filePath.substring(index + 1);

        File body = new File();
        body.setTitle(fileName);
        body.setDescription(fileName);
        body.setMimeType("text/x-sql");

        body.setParents(Arrays.asList(new ParentReference().setId("0Bx_DfLBtRih8QTI2M0dESTRyc1E")));

        java.io.File fileContent = new java.io.File(filePath);
        FileContent mediaContent = new FileContent("text/x-sql", fileContent);
        try {
            File file = service.files().insert(body, mediaContent).execute();

            LOGGER.info("Sent file: ", file.getId());

        } catch (IOException e) {
            LOGGER.error("An error occured: " + e, e);
        }
    }

}
