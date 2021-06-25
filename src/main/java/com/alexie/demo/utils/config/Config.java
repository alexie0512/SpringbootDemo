package com.alexie.demo.utils.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;



/**
 * 配置类
 *
 * @author Alexie on 2021/6/8
 * @ClassName Configuration
 * @Description TODO
 * @Version 1.0
 */




@Data
@Component
//@PropertySource(value={"classpath:config/config.properties"})
//@ConfigurationProperties(prefix = "config")
public class Config implements Serializable {

    //Logger
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    //Variables
    @Value("${config.project}")
    private String project;

    @Value("${config.environment}")
    private String environment;

    @Value("${config.environmentURL}")
    private String environmentalURL;

    @Override
    public String toString(){
        String border = StringUtils.repeat("-",62);
        String normalLine = "%20s : %-30s";
        List<String> configElements = new LinkedList<>();
        configElements.add(border);
        configElements.add(border);
        configElements.add(border);
        configElements.add(String.format(normalLine,"Project",getProject()));
        configElements.add(String.format(normalLine,"Environment",getEnvironment()));
        configElements.add(String.format(normalLine,"EnvironmentURL",getEnvironmentalURL()));
        configElements.add(border);
        configElements.add(border);

        return configElements.stream().collect(Collectors.joining("\n"));

    }

}
