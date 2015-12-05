package com.lf.bom.dao;

import org.springframework.stereotype.Component;

import java.util.List;

@Component("googleDriveCommitDao")
public class GoogleDriveCommitDao implements CommitDao {
    @Override
    public void commit(List<String> filenames) {

    }
}
