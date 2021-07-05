package com.alexie.demo;
import com.alexie.demo.service.RestAPI;
import com.alexie.demo.utils.config.Config;
import com.alexie.demo.utils.config.CustomizedHeader;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    public static String token;
    public static String cookie;
    public static Map<String, Object> headers = new HashMap<>();
    public static Map<String,Object>  K_headers = new HashMap<>();
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


    /**********************
     * 登录租户T13,获取Token *
     **********************/
    @BeforeAll
    void getTENANTToken_KibanaCookie(){

        /********************
         * 登录租户，获取token *
         *********************/
        logger.info("BeforeALl");
        logger.info("登录租户，获取token");
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("username", CustomizedHeader.USER_NAME_T13);
        reqParams.put("password", CustomizedHeader.PWD_T13);
        reqParams.put("expires", CustomizedHeader.EXPIRES);

        Header T_header = new Header("x-tenant-id", CustomizedHeader.TENANT_T13);
        Response res= new RestAPI().RestPostWithFormParams(T_header, ContentType.URLENC.withCharset("UTF-8"), "/user/login", reqParams);
        token = res.path("result.token");
        logger.info("租户 ---------- token值为：" + token);

        /************************
         * 登录Kibana，获取Cookie *
         ************************/
        logger.info("登录Kibana，获取Cookie");
        Map<String,Object>  K_header = new HashMap<>();
        K_header.put("kbn-version", CustomizedHeader.KIBANA_VERSION);

        String payLoad = "{\"username\":\"elastic\",\"password\":\"CwWkTuF1AY14HA3OTo66\"}";
        Response response = new RestAPI()
                .RestPostwithBody_ES1(K_header,ContentType.JSON.withCharset("UTF-8"),"/api/security/v1/login",payLoad);

        cookie = response.getCookie("sid");
        logger.info("Kibana ---------- cookie值为：" + cookie);
    }


    @BeforeEach
    public void setHeader(){
        /**
         * 组装租户素材搜索通用Header
         */
        logger.info("设置Header");
        headers.put("x-tenant-id", CustomizedHeader.TENANT_T13);
        headers.put("x-token", token);
        headers.put("x-user-id", CustomizedHeader.USER_ID);


        /**
         * 组装Kibana 请求Header
         */
        K_headers.put("kbn-version", CustomizedHeader.KIBANA_VERSION);
        K_headers.put("Cookie","sid="+ cookie);
    }




    @AfterAll
    void afterAll() throws IOException {

        logger.info("测试结束！！！！！！");

        File ENV_FILE = new File("src/test/resources/config/environment.properties");

        FileUtils.copyFile(new File("src/test/resources/config/environment.properties"),new File("target/allure-results/environment.properties"));
        logger.info("环境文件拷贝成功");

    }
}


