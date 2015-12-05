package com.lf.bom.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MysqlProduceDaoTest {

    @InjectMocks
    private MysqlProduceDao mysqlProduceDao = new MysqlProduceDao();

    @Mock
    private Runtime mockRuntime;

    @Before
    public void setup() throws Exception {
        List<String> databases = new ArrayList<>();
        databases.add("sonar");
        String mysqlDumpPath = "c:\\mysqldump";
        String destinationLocation = "c:\\backup\\";
        String mysqlUsername = "root";
        String mysqlPassword = "root";
        String now = "20151205233434";

        Whitebox.setInternalState(mysqlProduceDao, "databases", databases);
        Whitebox.setInternalState(mysqlProduceDao, "mysqlDumpPath", mysqlDumpPath);
        Whitebox.setInternalState(mysqlProduceDao, "destinationLocation", destinationLocation);
        Whitebox.setInternalState(mysqlProduceDao, "mysqlUsername", mysqlUsername);
        Whitebox.setInternalState(mysqlProduceDao, "mysqlPassword", mysqlPassword);
        Whitebox.setInternalState(mysqlProduceDao, "now", now);
    }

    @Test
    public void produceBackupSuccess() throws Exception {
        String command = "c:\\mysqldump -uroot -proot sonar > c:\\backup\\sonar.20151205233434.sql";
        String[] commands = new String[] {"cmd.exe", "/c", command};

        Process mockProcess = mock(Process.class);

        when(mockProcess.waitFor()).thenReturn(0);
        when(mockRuntime.exec(commands)).thenReturn(mockProcess);

        Whitebox.setInternalState(mysqlProduceDao, "runtime", mockRuntime);

        assertEquals("c:\\backup\\sonar.20151205233434.sql", mysqlProduceDao.produce().get(0));
    }

    @Test
    public void produceBackupFailed() throws Exception {

        String command = "c:\\mysqldump -uroot -proot sonar > c:\\backup\\sonar.20151205233434.sql";
        String[] commands = new String[] {"cmd.exe", "/c", command};

        Process mockProcess = mock(Process.class);

        when(mockProcess.waitFor()).thenReturn(-1);
        when(mockRuntime.exec(commands)).thenReturn(mockProcess);
        InputStream mockInputStream = mock(InputStream.class);
        when(mockProcess.getErrorStream()).thenReturn(mockInputStream);
        when(mockInputStream.available()).thenReturn(3);
        when(mockInputStream.read(any(byte[].class))).thenReturn(2);

        Whitebox.setInternalState(mysqlProduceDao, "runtime", mockRuntime);

        assertEquals(0, mysqlProduceDao.produce().size());
    }
}