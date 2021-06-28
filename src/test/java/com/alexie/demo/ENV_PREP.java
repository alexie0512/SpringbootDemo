package com.alexie.demo;
import com.alexie.demo.utils.config.Config;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;

/**
 * 环境准备
 *
 * @author Alexie on 2021/6/8
 * @ClassName ENV_PREP
 * @Description TODO
 * @Version 1.0
 */



@Component
public abstract class ENV_PREP {

    protected static Logger logger = LoggerFactory.getLogger(ENV_PREP.class);
    protected static String BaseURI;

    @Autowired
    protected Config config;

//    @Autowired
//    ENVConfig envConfig;

    /********************************************************
     ********  环境准备：BEFORE ALL/AFTER ALL  ****************
     * ******************************************************
     */
    @BeforeAll
    void setUp() throws InterruptedException {

//        System.out.println(config.toString());
        logger.info(config.toString());


        /***********************************
         **** 清理output folder 下生成的文件 ** 待优化
         ***********************************/
        File txt1 = new File("src/test/output/IKSmartResult/搜索词ik_smart分词策略结果.yml");
        if (txt1.isFile() && txt1.exists()) {
            logger.info("测试数据清理：文件ik_smart分词策略结果.txt已存在！进行删除操作！");
            txt1.delete();
        }

        Thread.sleep(5000);

    }



    @AfterAll
    void afterAll() throws IOException {

        logger.info("测试结束！！！！！！");

        File ENV_FILE = new File("src/test/resources/config/environment.properties");

        FileUtils.copyFile(new File("src/test/resources/config/environment.properties"),new File("target/allure-results/environment.properties"));
        logger.info("环境文件拷贝成功");

    }
}


