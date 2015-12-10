package com.lf.bom.launcher;

import com.lf.bom.config.SuperDuperConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    private static AnnotationConfigApplicationContext context;

    public static void start(String[] args) {
        LOGGER.info("Starting context.");
        context = new AnnotationConfigApplicationContext(SuperDuperConfig.class);
    }

    public static void stop(String[] args) {
        LOGGER.info("Shutting down context.");
        context.destroy();
    }

    public static void main(String[] args) {
        start(args);
    }

}
