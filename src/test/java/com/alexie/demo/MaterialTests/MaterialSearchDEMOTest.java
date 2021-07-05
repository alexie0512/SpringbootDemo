package com.alexie.demo.MaterialTests;

import com.alexie.demo.DemoApplication;
import com.alexie.demo.Dto.SearchDto;
import com.alexie.demo.Services.RestAPI;
import com.alexie.demo.ESSearchTests.ESSearchTest;
import com.alexie.demo.utils.FileUtils;
import com.alexie.demo.utils.Interfaces.SimpleExcelFileSource;
import com.alexie.demo.utils.config.CustomizedHeader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;




/**
 * 素材库搜索
 *
 * @author Alexie on 2021/6/4
 * @ClassName MaterialSearchDEMOTest
 * @Description TODO
 * @Version 1.0
 */

//@Slf4j
////@ContextConfiguration()
//@ExtendWith(SpringExtension.class)
//@Component
//@SpringBootTest(classes = DemoApplication.class)
////@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DisplayName("素材库搜索相关用例")
public class MaterialSearchDEMOTest implements CustomizedHeader {


    private static Logger logger = LoggerFactory.getLogger(MaterialSearchDEMOTest.class);
    private static String token;


//    @BeforeAll
//    static void getToken(){
//
//        /**
//         * 登录租户T3,获取Token
//         */
//        logger.info("BeforeALl");
//        logger.info("登录租户，获取token");
//        Map<String, Object> reqParams = new HashMap<>();
//        reqParams.put("username", CustomizedHeader.USER_NAME_T13);
//        reqParams.put("password", CustomizedHeader.PWD_T13);
//        reqParams.put("expires", CustomizedHeader.EXPIRES);
//
//        Header header = new Header("x-tenant-id", CustomizedHeader.TENANT_T13);
//        Response res = RestAPI
//                .RestPostWithFormParams(header, ContentType.URLENC.withCharset("UTF-8"), "/user/login", reqParams);
//        token = res.path("result.token");
//    }

    /************************************
     ********  素材库搜索  ****************
     * **********************************
     */
//    @Disabled
//    @Test
//    @DisplayName("搜索素材库，匹配关键词：奥妙")
    public void searchMaterial1() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-tenant-id", CustomizedHeader.TENANT_T13);
        headers.put("x-token", token);
        headers.put("x-user-id",CustomizedHeader.USER_ID);

        String payLoad = "{\"filterMap\":{},\"searchText\":{\"奥妙\":[]},\"startPoint\":0,\"endPoint\":48}";
        Response res = new RestAPI()
                .RestPostwithBody(headers, ContentType.JSON.withCharset("UTF-8"), "/material/search/list", payLoad);

        assertAll("断言测试", () -> assertEquals("奥妙淡雅樱花2KG-800-800主图(1)", res.path("result.list[0].CORE_NAME")),
                () -> assertEquals("奥妙草本除菌内衣皂(1)",
                        res.path("result.list[1].CORE_NAME")));

    }




    /**
     * yaml 数据驱动 搜索素材库
     */
//    @Disabled
//    @ParameterizedTest
//    @MethodSource
//    @DisplayName("参数化测试_批量搜索匹配素材_读取YAML文件")
    public void searchMaterial(SearchDto search) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-tenant-id", CustomizedHeader.TENANT_T13);
        headers.put("x-token", token);
        headers.put("x-user-id", CustomizedHeader.USER_ID);

        String payLoad = "{\"filterMap\":{},\"searchText\":{ \"" + search.getSearchStr()
                + "\":[]},\"startPoint\":0,\"endPoint\":48}";
        Response res =  new RestAPI()
                .RestPostwithBody(headers, ContentType.JSON.withCharset("UTF-8"), "/material/search/list", payLoad);

        logger.info("关键字「" + search.getSearchStr() + "」的搜索结果素材名称汇总为： " + res.path("result.list.CORE_NAME"));

        //assertThat(res.path("result.list.CORE_NAME"), hasItem(search.getExpectedStr()));
    }

    static List<SearchDto> searchMaterial() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        List<SearchDto> searchDtoList = mapper
                .readValue(ESSearchTest.class.getResourceAsStream("/searchfiles/materialSearch.yaml"),
                        new TypeReference<List<SearchDto>>() {
                        });
        return searchDtoList;

    }

//    @Disabled
//    @DisplayName("获取租户的全量素材的素材名称并存入YAML文件")
//    @Test
    public void searchMaterial_All() throws Exception {
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-tenant-id", CustomizedHeader.TENANT_T13);
        headers.put("x-token", token);
        headers.put("x-user-id", CustomizedHeader.USER_ID);


        String payLoad = "{\"filterMap\":{},\"searchText\":{},\"startPoint\":0,\"endPoint\":1000}";
        Response res =  new RestAPI()
                .RestPostwithBody(headers, ContentType.JSON.withCharset("UTF-8"), "/material/search/list", payLoad);

        List<String> materialNameList = res.path("result.list.CORE_NAME");
        logger.info(CustomizedHeader.TENANT_T13+"所有的素材汇总为： \n" + materialNameList);


        StringBuilder sb= new StringBuilder();
        SearchDto searchDto= new SearchDto();
        Yaml yml =new Yaml();
        StringWriter writer = new StringWriter();

//        for(String s:materialNameList){
//            //sb.append(yml.dumpAs(searchDto, new Tag("test"), DumperOptions.FlowStyle.BLOCK)+"\n");
//            //sb.append("-  "+yml.dumpAsMap(searchDto)+"\n");
//            sb.append(yml.dumpAs(searchDto,new Tag("tag:yaml.org,2002:map"), DumperOptions.FlowStyle.BLOCK));
//            searchDto.setSearchStr(s);
//            searchDto.setExpectedStr(s);
//            //yml.dumpAs(searchDto, Tag.MAP,null);
//            //yml.dump(searchDto,writer);
//        }

        for(String s: materialNameList){
            sb.append("- text:  "+ s +"\n" +"  analyzer: ik_smart \n");
        }
        FileUtils.writeStrtoFile(sb.toString(),"src/test/resources/searchfiles",CustomizedHeader.TENANT_T13+"素材搜索关键字.yml");

    }



//    @Disabled
//    @DisplayName("批量搜索T3素材库，判断返回列表是包含预期素材ID")
//    @Description("参数化测试_读取搜索数据CSV文件实现模拟用户关键字搜索")
//    @Severity(SeverityLevel.CRITICAL)
//    @ParameterizedTest(name="用例[{index}],用户[{0}],搜索关键字[{1}],预期命中id[{2}]")
//    @CsvFileSource(files = "src/test/resources/searchfiles/租户_联合利华搜索数据 _4.9_4.15.csv", numLinesToSkip = 1)
    public void searchMaterialviaCSVfile(String keyword,String expected_assetId) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-tenant-id", CustomizedHeader.TENANT_T13);
        headers.put("x-token", token);
        headers.put("x-user-id", CustomizedHeader.USER_ID);

        String payLoad = "{\"filterMap\":{},\"searchText\":{\"" + keyword + "\":[]},\"startPoint\":0,\"endPoint\":2000}";

        //String payLoad = "{\"filterMap\":{},\"searchText\":{},\"startPoint\":0,\"endPoint\":1000}";
        Response res =  new RestAPI()
                .RestPostwithBody(headers, ContentType.JSON.withCharset("UTF-8"), "/material/search/list", payLoad);

        //List<String> materialNameList = res.path("result.list.CORE_NAME");
        //logger.info(CustomizedHeader.TENANT_T13+"所有的素材汇总为： \n" + materialNameList);

        List<String> matchedList = new ArrayList<>();
        matchedList = res.path("result.list");
        //logger.info(keyword + " 匹配的素材列表为：" + matchedList);

        assertThat(res.statusCode()).isEqualTo(200);
        assertThat(res.jsonPath().getString("result.list.id")).contains(expected_assetId);

    }

//
//    @Test
//    public void testwrite(){
////        ESSearchDto esSearchDto = new ESSearchDto();
////        EsSearchUseCaseDto esSearchUseCaseDto = new EsSearchUseCaseDto();
////        esSearchUseCaseDto.setEsSearchDtoList();
////        Yaml yaml = new Yaml();
////        StringWriter writer = new StringWriter();
////        yaml.dump(esSearchUseCaseDto, writer);
//        Constructor constructor = new Constructor(EsSearchUseCaseDto.class);//Car.class is root
//        TypeDescription carDescription = new TypeDescription(EsSearchUseCaseDto.class);
//        carDescription.putListPropertyType("esSearchDtoList", ESSearchDto.class);
//        constructor.addTypeDescription(carDescription);
//        Yaml yaml = new Yaml(constructor);
//        StringWriter writer = new StringWriter();
//
//    }




//
//    @Disabled
//    @DisplayName("批量搜索T3素材库，判断返回列表是包含预期素材ID")
//    @Description("参数化测试_读取EXCEL文件实现模拟用户关键字搜索")
//    @ParameterizedTest(name="用例[{index}],用户[{0}],搜索[{1}]")
//    @SimpleExcelFileSource(resource = "src/test/resources/searchfiles/租户搜索埋点数据_4.9_4.15_ceshi.xlsx",sheetNameToRead = "Sheet 1",headerLineNum = 0)
    public void searchMaterialviaExcelfile(String user_name, String keyword){
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-tenant-id", CustomizedHeader.TENANT_T13);
        headers.put("x-token", token);
        headers.put("x-user-id", CustomizedHeader.USER_ID);

        String payLoad = "{\"filterMap\":{},\"searchText\":{\"" + keyword + "\":[]},\"startPoint\":0,\"endPoint\":2000}";

        //String payLoad = "{\"filterMap\":{},\"searchText\":{},\"startPoint\":0,\"endPoint\":1000}";
        Response res =  new RestAPI()
                .RestPostwithBody(headers, ContentType.JSON.withCharset("UTF-8"), "/material/search/list", payLoad);

        //List<String> materialNameList = res.path("result.list.CORE_NAME");
        //logger.info(CustomizedHeader.TENANT_T13+"所有的素材汇总为： \n" + materialNameList);

        List<String> matchedList = new ArrayList<>();
        matchedList = res.path("result.list");
        //logger.info(keyword + " 匹配的素材列表为：" + matchedList);

        assertThat(res.statusCode()).isEqualTo(200);
        //assertThat(res.jsonPath().getString("result.list.id")).contains(expected_id);

    }



}
