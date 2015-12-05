package com.lf.bom.dao;

import org.springframework.stereotype.Component;

import java.io.File;

@Component("googleDriveCommitDao")
public class GoogleDriveCommitDao implements CommitDao {
    @Override
    public void commit(File file) {

    }
}
