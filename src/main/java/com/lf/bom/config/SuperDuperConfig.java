package com.lf.bom.config;


import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan("com.lf.bom")
@Import({BackupProduceConfig.class, GoogleDriveConfig.class})
@PropertySource(value={"classpath:common.properties"})
@EnableScheduling
public class SuperDuperConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
