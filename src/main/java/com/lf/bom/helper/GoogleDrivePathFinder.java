package com.lf.bom.helper;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GoogleDrivePathFinder {

    @Autowired
    private Drive service;

    public String findPathId(String path) throws IOException {
        String[] sections = path.split("/");

        About about = service.about().get().execute();

        String curFolderId = findSectionId(about.getRootFolderId(), sections[0]);

        for (int i = 1; i < sections.length; i++) {
            curFolderId = findSectionId(curFolderId, sections[i]);
        }

        return curFolderId;
    }

    private String findSectionId(String sectionId, String title) throws IOException {
        ChildList children = service.children().list(sectionId).execute();

        for (ChildReference child : children.getItems()) {
            File file = service.files().get(child.getId()).execute();
            if (title.equals(file.getTitle())) {
                return file.getId();
            }
        }
        throw new IllegalStateException("Folder {} not found in google drive...");
    }

}
