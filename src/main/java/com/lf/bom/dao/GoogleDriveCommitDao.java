package com.lf.bom.dao;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.lf.bom.helper.GoogleDrivePathFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component("googleDriveCommitDao")
public class GoogleDriveCommitDao implements CommitDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleDriveCommitDao.class);

    @Autowired
    private Drive service;

    @Value("${google.drive.backup.path}")
    private String path;

    @Value("${google.backup.mime.type}")
    private String mimeType;

    @Autowired
    private GoogleDrivePathFinder pathFinder;

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
        body.setMimeType(mimeType);

        try {

            String parentId = pathFinder.findPathId(path);

            body.setParents(Collections.singletonList(new ParentReference().setId(parentId)));

            java.io.File fileContent = new java.io.File(filePath);
            FileContent mediaContent = new FileContent(mimeType, fileContent);
            File file = service.files().insert(body, mediaContent).execute();

            LOGGER.info("Sent file: ", file.getTitle());

        } catch (IOException e) {
            LOGGER.error("An error occured: " + e, e);
        }
    }

}
