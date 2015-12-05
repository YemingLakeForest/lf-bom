package com.lf.bom.launcher;

import com.lf.bom.config.SuperDuperConfig;
import com.lf.bom.service.BackupService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SuperDuperConfig.class);
        BackupService service = context.getBean(BackupService.class);
        service.run();
    }

}
