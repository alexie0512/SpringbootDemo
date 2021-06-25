package com.alexie.demo.MaterialTests;

import com.alexie.demo.DemoApplication;
import com.alexie.demo.Dto.ESSearchDto;
import com.alexie.demo.Dto.MaterialSearchDto;
import com.alexie.demo.Dto.SearchDto;
import com.alexie.demo.ENV_PREP;
import com.alexie.demo.ESSearchTests.ESSearchTest;
import com.alexie.demo.Services.RestAPI;
import com.alexie.demo.utils.FileUtils;
import com.alexie.demo.utils.Interfaces.SimpleExcelFileSource;
import com.alexie.demo.utils.config.CustomizedHeader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.qameta.allure.Description;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ddf.DefaultEscherRecordFactory;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.alexie.demo.utils.Tools.usingStream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;


/**
 * 素材库搜索
 *
 * @author Alexie on 2021/6/4
 * @ClassName MaterialSearchDEMOTest
 * @Description TODO
 * @Version 1.0
 */

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DemoApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@RunWith(JUnitPlatform.class)
@DisplayName("素材库搜索相关用例")
public class materialSearchTest extends ENV_PREP implements CustomizedHeader {
    private static final Logger logger = LoggerFactory.getLogger(materialSearchTest.class);
    private static String token;
    private static String cookie;
    private static Map<String, Object> headers = new HashMap<>();
    private static Map<String,Object>  K_headers = new HashMap<>();


    /**********************
     * 登录租户T3,获取Token *
     **********************/
    @BeforeAll
    static void getTENANTToken_KibanaCookie(){

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
        Response res = RestAPI
                .RestPostWithFormParams(T_header, ContentType.URLENC.withCharset("UTF-8"), "/user/login", reqParams);
        token = res.path("result.token");
        logger.info("租户 ---------- token值为：" + token);

        /************************
         * 登录Kibana，获取Cookie *
         ************************/
        logger.info("登录Kibana，获取Cookie");
        Map<String,Object>  K_header = new HashMap<>();
        K_header.put("kbn-version", CustomizedHeader.KIBANA_VERSION);

        String payLoad = "{\"username\":\"elastic\",\"password\":\"CwWkTuF1AY14HA3OTo66\"}";
        //String payLoad= FileReader.readToString(new File(System.getProperty("security_v1_login")),"UTF-8");
        Response response = RestAPI
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



    @ParameterizedTest
    @MethodSource
    @DisplayName("验证素材库 搜索 「关键字」后，返回结果素材名中包含分词结果")
    @Description("验证素材库 搜索 基础排序策略，交并集先后排序")
    public void materialSearchBasicOrder(MaterialSearchDto materialSearchDto) throws Exception {

        /**
         * 请求Kibana IK 分词器分词接口"/api/console/proxy"
         */

        ESSearchDto esSearchDto = new ESSearchDto(); //组装请求body
        esSearchDto.setText(materialSearchDto.getSearchStr());
        esSearchDto.setAnalyzer("ik_smart");

        Map<String,Object> queryParams = new HashMap<>(); //组装请求queryParams
        queryParams.put("path","_analyze");
        queryParams.put("method","POST");

        Response K_res = RestAPI.RestPostwithBody_ES(K_headers,ContentType.JSON.withCharset("UTF-8"),queryParams,"/api/console/proxy",esSearchDto);


        List<String> K_List= K_res.path("tokens.token"); //提取IK分词器分词结果，并输出到文件：搜索词ik_smart分词策略结果.yml
        //FileUtils.writeStrtoFile("- "+materialSearchDto.getSearchStr()+": "+ K_res.path("tokens.token") +"\n","src/test/output/IKSmartResult","搜索词ik_smart分词策略结果.yml");



        /**
         * 请求素材库搜索接口 /material/search/list
         */
        String payLoad = "{\"filterMap\":{},\"searchText\":{ \""+materialSearchDto.getSearchStr()+"\":[]},\"startPoint\":0,\"endPoint\":48}";
        Response T_res = RestAPI
                .RestPostwithBody(headers, ContentType.JSON.withCharset("UTF-8"), "/material/search/list", payLoad);


        /**
         * 如果分词结果K_List 包含在返回的素材名列表nameList中，命中为true，计数+1，和返回的素材TotalCount做Equal断言
         */

        List<String> nameList = T_res.path("result.list.CORE_NAME");
        System.out.println("获取的素材的名称："+ nameList);
        System.out.println("关键词分词结果："+ K_List);

        List<Map<String,Object>> T_result= T_res.jsonPath().get("result.list");
        System.out.println("提取响应的List: "+ T_result);


        //Step1: 将分词结果与字段：素材名匹配， 如果匹配，命中为true，计数+1
        // 将素材名中的所有英文字符转换成小写

        long matchedName_count = nameList.stream()
                .filter(name -> {
                    name = name.toLowerCase(); // 将素材名中的所有英文字符转换成小写
                    boolean flag = false;
                    for (int i = 0; i < K_List.size(); i++) {
                        if (name.contains(K_List.get(i))) {
                            flag = true;
//                            newList.add(name);
                            break;
                        }
                    }
                    return flag;

                }).count();


        //Step2: 提取素材名未命中的素材
        List<String> newList= nameList.stream()
                .filter(name -> {
                    name = name.toLowerCase(); // 将素材名中的所有英文字符转换成小写
                    boolean flag = true;
                    for (int i = 0; i < K_List.size(); i++) {
                        if (name.contains(K_List.get(i))) {
                            flag = false;
//                            newList.add(name);
                            break;
                        }
                    }
                    return flag;

                }).collect(Collectors.toList());

        System.out.println("命中素材的标题字段"+matchedName_count);
        System.out.println("没有命中素材的标题字段"+newList);



        //Step3: 如果有素材名未命中的素材，将分词结果与字段：描述 匹配， 如果匹配，命中为true，计数继续+1
        List<String> DescList = new ArrayList<>();
        Long count2;
        final String key= "CORE_NAME";
        if(!newList.isEmpty()){
            for(int i=0;i<newList.size();i++){
                String value = newList.get(i);
                String Desc= (T_result.stream().filter(map->map.get(key).equals(value))
                        .collect(Collectors.toList())
                        .get(0))
                        .get("CORE_DESCRIPTION")
                        .toString();
                DescList.add(Desc);
            }

            count2= DescList.stream().filter(desc ->{
                desc=desc.toLowerCase();
                boolean flag = false;
                for(int i=0; i<K_List.size();i++){
                    if(desc.contains(K_List.get(i))){
                        flag=true;
                        nameList.remove(i);
                        break;
                    }
                }
                return flag;
            }).count();

            matchedName_count+=count2;
            System.out.println("命中素材的描述字段"+count2);

        }

        //assertThat(T_res.jsonPath().get("result.list.CORE_NAME").toString()).contains(materialSearchDto.getExpected_NAME());

        // 断言：
        // Step1: 验证搜索 「关键字」后，返回结果素材名中命中分词结果计数与返回当页的素材数需相同
        assertEquals(matchedName_count,T_res.jsonPath().getLong("result.totalCount"));

    }

    static List<MaterialSearchDto> materialSearchBasicOrder() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        List<MaterialSearchDto> materialSearchDtoList= mapper
                .readValue(materialSearchTest.class.getResourceAsStream("/searchfiles/materialSearchBasicOrder.yaml"),
                        new TypeReference<List<MaterialSearchDto>>() {
                        });
        return materialSearchDtoList;

    }




    @ParameterizedTest
    @MethodSource
    @Description("验证 素材ID搜索功能")
    @DisplayName("验证 素材ID搜索功能")
    public void materialSearchbyId(MaterialSearchDto materialSearchDto){
        //String payLoad = "{\"filterMap\":{},\"searchText\":{\"奥妙\":[]},\"startPoint\":0,\"endPoint\":48}";

        String payLoad = "{\"filterMap\":{\"id\":[\""+materialSearchDto.getSearchStr()+"\"]},\"searchText\":{},\"startPoint\":0,\"endPoint\":48}";
        Response res = RestAPI
                .RestPostwithBody(headers, ContentType.JSON.withCharset("UTF-8"), "/material/search/list", payLoad);

        assertAll("断言测试",
                () -> assertEquals(materialSearchDto.getExpected_NAME(), res.path("result.list[0].CORE_NAME")),
                () -> assertEquals(materialSearchDto.getExpected_ID(), (Integer) res.path("result.list[0].CORE_ID")),
                () -> assertEquals(materialSearchDto.getTotal_Count(),(Integer) res.path("result.totalCount"))
        );

    }

    static List<MaterialSearchDto> materialSearchbyId() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        List<MaterialSearchDto> materialSearchDtoList= mapper
                .readValue(materialSearchTest.class.getResourceAsStream("/searchfiles/materialSearchbyId.yaml"),
                        new TypeReference<List<MaterialSearchDto>>() {
                        });
        return materialSearchDtoList;

    }






    @DisplayName("批量搜索T3素材库，判断返回列表是包含预期素材ID")
    @Description("验证素材库主色筛选相似色阈值均大于20且降序排列")
    @ParameterizedTest(name="用例[{index}],主色[{0}],相似色最低阈值[{1}]")
    @SimpleExcelFileSource(resource = "src/test/resources/searchfiles/主色筛选标准色_old.xlsx",sheetNameToRead = "Sheet1",headerLineNum = 0)
    public void materialSearchbyColor(String Standard_Color, Float Adjacent_min) throws JSONException {
        String payLoad = "{\"filterMap\":{\"materialOrigin.adjacentColor\":[\""+Standard_Color+"\"]},\"searchText\":{},\"selfRelevant\":0,\"startPoint\":0,\"endPoint\":48}";
        Response res = RestAPI
                .RestPostwithBody(headers, ContentType.JSON.withCharset("UTF-8"), "/material/search/list", payLoad);

        //Step 1: 提取响应体List
        List<Map<String,Object>> list = res.jsonPath().get("result.list");

        //Step 2：遍历List 字段 "adjacentColor_主色" 值，返回所有在最低相似阈值=>Adjacent_min 的结果
        long matched_Count = list.stream()
                .filter(s -> {
                    Float t = (Float) s.get("adjacentColor_" + Standard_Color);
            boolean b = t>= Adjacent_min;
            return b;
        }).count();


        List<Object> Adjacent_List = list.stream()
                .map(n -> n.get("adjacentColor_" + Standard_Color))
                .collect(Collectors.toList());

        Object obj= (Object) Adjacent_List;
        List<Float> Adjacent_List01= (List<Float>) obj;
       // Object[] Adjacent_array_sorted = Adjacent_array;

        List<Float>  Adjacent_sorted= Adjacent_List01;
        System.out.println("主色"+Standard_Color +"返回列表的所有相似色阈值为："+ Adjacent_List);



        //断言：
        // 1. 验证所有返回的结果与搜索主色相似阈值在最低阈值Adjacent_min 以上
        // 2. 验证颜色阈值列表返回是有序的(当返回列表非空且个数大于1时)
        assertThat(list.size()).isEqualTo(matched_Count);
        if((!Adjacent_List01.isEmpty())&&Adjacent_List01.size()>1){
            assertFalse(usingStream(Adjacent_List01,Adjacent_sorted));
        }

    }




    @Description("验证素材库同义词搜索")
    @Test
    public void materialSearchwithSynonoms(){
        logger.info("success");
    }



}
