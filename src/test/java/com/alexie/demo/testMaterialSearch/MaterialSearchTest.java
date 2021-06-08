package com.alexie.demo.testMaterialSearch;

import com.alexie.demo.DemoApplication;
import com.alexie.demo.Dto.SearchDto;
import com.alexie.demo.Services.RestAPI;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.qameta.allure.Description;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 素材库搜索
 *
 * @author Alexie on 2021/6/4
 * @ClassName MaterialSearchTest
 * @Description TODO
 * @Version 1.0
 */

@Slf4j
@SpringBootTest
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ContextConfiguration()
@ExtendWith(SpringExtension.class)
public class MaterialSearchTest {

//
//    @Autowired
//    private RestAPI restAPI;

    private static final Logger logger= LoggerFactory.getLogger(MaterialSearchTest.class);
    private static String token;

    @Description("获取token")
    @BeforeAll
    static void getToken() {
        logger.info("BeforeALl");
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("username", "tzvirtual1@tezign.com");
        reqParams.put("password", 111111);
        reqParams.put("expires", "SHORT");

        Header header = new Header("x-tenant-id", "t2");
        Response res= RestAPI.RestPostwithFormParams(header, ContentType.URLENC.withCharset("UTF-8"), "/user/login",reqParams);
        token = res.path("result.token");
        logger.info("token值为：" + token);

    }


    /**
     * 搜索素材库，调试用
     */
    @Test
    public void searchMaterial1(){
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-tenant-id","t2");
        headers.put("x-token",token);
        headers.put("x-user-id",1);

        String payLoad= "{\"filterMap\":{},\"searchText\":{\"天猫\":[]},\"startPoint\":0,\"endPoint\":48}";
        Response res = RestAPI.RestPostwithBody(headers,ContentType.JSON.withCharset("UTF-8"),"/material/search/list",payLoad );

        assertAll("断言测试",
                ()->assertEquals("0-11_天猫",res.path("result.list[0].CORE_NAME")),
                ()->assertEquals("material-ec-10_抖音_开屏_静态全屏_1242*2208_天猫超市_商品图",res.path("result.list[1].CORE_NAME")));

    }


    /**
     * yaml 数据驱动 搜索素材库
     */
    @ParameterizedTest
    @MethodSource
    public void searchMaterial(SearchDto search) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-tenant-id", "t2");
        headers.put("x-token", token);
        headers.put("x-user-id", 1);

        String payLoad = "{\"filterMap\":{},\"searchText\":{ \"" + search.getSearchStr() + "\":[]},\"startPoint\":0,\"endPoint\":48}";
        Response res = RestAPI.RestPostwithBody(headers, ContentType.JSON.withCharset("UTF-8"), "/material/search/list", payLoad);

        logger.info("关键字「" + search.getSearchStr() + "」的搜索结果素材名称汇总为： " + res.path("result.list.CORE_NAME"));


        assertThat(res.path("result.list.CORE_NAME"), hasItem(search.getExpectedStr()));
    }

    static List<SearchDto> searchMaterial() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
//        TypeReference typeReference = new TypeReference<List<SearchDto>>() {
//        };
        List<SearchDto> searchDtoList = mapper.readValue(TestESSearch.class.getResourceAsStream("/searchfiles/materialSearch.yaml"), new TypeReference<List<SearchDto>>(){});
        return searchDtoList;

    }



}
