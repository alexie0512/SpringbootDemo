package com.alexie.demo.ESSearchTests;

import com.alexie.demo.Dto.ESSearchDto;
import com.alexie.demo.Services.RestAPI;
import com.alexie.demo.utils.config.CustomizedHeader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import com.alexie.demo.utils.FileUtils;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 中台搜索
 *
 * @author Alexie on 2021/5/18
 * @ClassName ESSearchTest
 * @Description TODO
 * @Version 1.0
 */


@Slf4j
//@ContextConfiguration()
@Component
@DisplayName("关键词IK分词器分词策略相关测试用例")
@SpringBootTest
public class ESSearchTest implements CustomizedHeader {

    private static final Logger logger= LoggerFactory.getLogger(ESSearchTest.class);
    private static String cookie;
    private static List<String> list = new ArrayList<>();


    /***********************************************
     ********** 查看Kibana分词结果   *****************
     ********** 分词方式：     ***********************
     ********** - ik smart   ***********************
     ***********************************************
     */

    @Disabled
    @Description("获取Kibana_cookie")
    @Step("登录Kibana")
    @BeforeAll
    @DisplayName("登录KIBANA用户")
    static void getKibanaCookie() throws InterruptedException {

        Map<String,Object>  headers = new HashMap<>();
        headers.put("kbn-version", CustomizedHeader.KIBANA_VERSION);

        String payLoad = "{\"username\":\"elastic\",\"password\":\"CwWkTuF1AY14HA3OTo66\"}";
        //String payLoad= FileReader.readToString(new File(System.getProperty("security_v1_login")),"UTF-8");
        Response res = new RestAPI().RestPostwithBody_ES1(headers,ContentType.JSON.withCharset("UTF-8"),"/api/security/v1/login",payLoad);

        cookie = res.getCookie("sid");
        logger.info("cookie值为：" + cookie);

    }


    /***********************************************
     ********** 批量分词结果   ***********************
     ********** 分词方式：     ***********************
     ********** - ik smart   ***********************
     ***********************************************
     */

    @Disabled
    @ParameterizedTest(name="T13素材分词用例[{index}]")
    @MethodSource
    @DisplayName("参数化测试_调用KIBANA批量分词_analyze")
    void getIKESResults(ESSearchDto esSearchDto) throws Exception {
        Map<String,Object> queryParams = new HashMap<>();
        queryParams.put("path","_analyze");
        queryParams.put("method","POST");

        Map<String,Object>  headers = new HashMap<>();
        headers.put("kbn-version", CustomizedHeader.KIBANA_VERSION);
        headers.put("Cookie","sid="+ cookie);

        Response res =  new RestAPI().RestPostwithBody_ES(headers,ContentType.JSON.withCharset("UTF-8"),queryParams,"/api/console/proxy",esSearchDto);

        //list.add("关键字---- "+esSearchDto.getText()+"----的分词结果为："+res.path("tokens.token") +"\n");
        //System.out.println(list);
        FileUtils.writeStrtoFile("- "+ esSearchDto.getText()+": "+ res.path("tokens.token") +"\n","src/test/output",CustomizedHeader.TENANT_T13+"素材库标题ik_smart分词策略结果.yml");

    }

    static List<ESSearchDto> getIKESResults() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        List<ESSearchDto> esSearchDtoList = mapper.readValue(ESSearchTest.class.getResourceAsStream("/searchfiles/t13素材搜索关键字.yml"),new TypeReference<List<ESSearchDto>>() {
        });
        return esSearchDtoList;
    }



}