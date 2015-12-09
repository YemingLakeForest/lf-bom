package com.lf.bom.launcher;

import com.lf.bom.config.SuperDuperConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SuperDuperConfig.class);
        LOGGER.info("Created Context: {}", context);
    }

}
