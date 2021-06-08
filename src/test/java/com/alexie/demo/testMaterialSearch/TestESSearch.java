package com.alexie.demo.testMaterialSearch;

import com.alexie.demo.Dto.ESSearchDto;
import com.alexie.demo.Dto.SearchDto;
import com.alexie.demo.Services.RestAPI;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.qameta.allure.Description;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 中台搜索
 *
 * @author Alexie on 2021/5/18
 * @ClassName TestESSearch
 * @Description TODO
 * @Version 1.0
 */
public class TestESSearch {
    private static final Logger logger= LoggerFactory.getLogger(TestESSearch.class);
    private static String cookie;


    /**
     * 查看Kibana分词结果
     * 分词方式：
     * - ik smart
     * - max word
     *
     */

    @BeforeAll
    static void getKibanaCookie(){

//        Map<String, Object> payLoad = new HashMap<>();
//        payLoad.put("username", "elastic");
//        payLoad.put("password", "CwWkTuF1AY14HA3OTo66");

        Map<String,Object>  headers = new HashMap<>();
        headers.put("kbn-version", "5.6.16");
        String payLoad = "{\"username\":\"elastic\",\"password\":\"CwWkTuF1AY14HA3OTo66\"}";


        // Response res = RestAPI.RestPostwithBody_ES(headers,ContentType.JSON.withCharset("UTF-8"),,"/api/security/v1/login",);
        Response res = given()
//                .config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
//                .config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("x-www-form-urlencoded", ContentType.URLENC)))
                .header("kbn-version", "5.6.16")
                .contentType("application/json;charset=UTF-8")
                .body(payLoad)
                .log().all()
                .post("https://es-cn-mp91mb9ff000j5bde.kibana.elasticsearch.aliyuncs.com:5601/api/security/v1/login")
                .then()
                .log().all()
                .extract()
                .response();

        cookie = res.getCookie("sid");
        logger.info("cookie值为：" + cookie);

    }

    @ParameterizedTest
    @MethodSource
    void getIKESResults(ESSearchDto esSearchDto){
        Map<String,Object> queryParams = new HashMap<>();
        queryParams.put("path","_analyze");
        queryParams.put("method","POST");

        Map<String,Object>  headers = new HashMap<>();
        headers.put("kbn-version", "5.6.16");
        headers.put("Cookie","sid="+ cookie);

        Response res = RestAPI.RestPostwithBody_ES(headers,ContentType.JSON.withCharset("UTF-8"),queryParams,"/api/console/proxy",esSearchDto);

    }

    static List<ESSearchDto> getIKESResults() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        List<ESSearchDto> esSearchDtoList = mapper.readValue(TestESSearch.class.getResourceAsStream("/searchfiles/ESsearch.yaml"),new TypeReference<List<ESSearchDto>>() {
        });
                //readValue(
        //                TestESSearch.class.getResourceAsStream("/ESsearch.yaml"),typeReference);
        return esSearchDtoList;
    }



}