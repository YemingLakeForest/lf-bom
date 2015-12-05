package com.lf.bom.dao;

import java.util.List;

public interface CommitDao {

    void commit(List<String> filenames);

}
