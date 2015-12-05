package com.lf.bom.service;

import com.lf.bom.dao.CommitDao;
import com.lf.bom.dao.ProduceDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BackupServiceTest {

    @InjectMocks
    private BackupService backupService = new BackupService();

    @Mock
    private CommitDao mockCommitDao;

    @Mock
    private ProduceDao mockProduceDao;

    @Test
    public void backupServiceFlowTest() {
        File file = new File("");
        when(mockProduceDao.produceBackup()).thenReturn(file);
        backupService.run();
        verify(mockCommitDao).commit(file);
    }

}