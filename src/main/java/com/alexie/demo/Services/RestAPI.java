package com.alexie.demo.Services;

import com.alexie.demo.utils.config.Config;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * RestAssured 封装GET/POST 方法
 *
 * @author Alexie on 2021/5/19
 * @ClassName RestAPI
 * @Description TODO
 * @Version 1.0
 */


@Component
public class RestAPI {

    //Logger
    protected static Logger logger = LoggerFactory.getLogger(RestAPI.class);

    @Autowired
    protected Config config;
    private static RestAPI restAPI;


    @PostConstruct
    public void init(){
        restAPI = this;
        restAPI.config = this.config;
    }


    //Variables
//    public static  final String baseURL= "https://vms-service.tezign.com";
//    public static  final String ESbaseURL= "https://es-cn-mp91mb9ff000j5bde.kibana.elasticsearch.aliyuncs.com:5601";
//
//    protected EnumMap<Request,Object> reqBody = new EnumMap<Request, Object>(Request.class);
//    protected EnumMap<Request, Map<String,Object>> reqMap = new EnumMap<Request, Map<String, Object>>(Request.class);




    public enum Request{
        COOKIE,
        HEADER,
        QUERY,
        PATH,
        FORM,
        BODY
    }


    //Post API
    public Response RestPostWithFormParams(Header header, String contentType, String endPoint, Map<String, Object> reqFormParams){
        Response res = given()
                .header(header)
                .contentType(contentType)
                .formParams(reqFormParams)
                .log().all()
                .post(restAPI.config.getServiceURL()+endPoint)
                .then()
                .log().ifError()
                .extract()
                .response();
        return res;
    }


    public Response RestPostwithBody(Map<String,Object> headers, String contentType, String endPoint, String body){
        Response res = given()
                .headers(headers)
                .contentType(contentType)
                .body(body)
                .log().all()
                .post(restAPI.config.getServiceURL()+endPoint)
                .then()
                .log().ifError()
                .extract()
                .response();

        return res;
    }


    public Response RestPostwithBody_ES(Map<String,Object> headers, String contentType, Map<String,Object> queryParams, String endPoint, Object body){
        Response res = given()
                .headers(headers)
                .contentType(contentType)
                .queryParams(queryParams) //(Map<String, ?>) queryParams.getOrDefault(Request.QUERY,Collections.emptyMap())
                .body(body)
                .log().all()
                .post(restAPI.config.getKibana_BaseURL()+endPoint)
                .then()
                .log().ifError()
                .extract()
                .response();

        return res;
    }




    public Response RestPostwithBody_ES1(Map<String,Object> headers, String contentType, String endPoint, Object body){
        Response res = given()
                .headers(headers)
                .contentType(contentType)
                .body(body)
                .log().all()
                .post(restAPI.config.getKibana_BaseURL()+endPoint)
                .then()
                .log().ifError()
                .extract()
                .response();

        return res;
    }


    //Get API
    public static Response RestGet(Header header, String contentType, String URL){
        Response res = given()
                .header(header)
                .contentType(contentType)
                .log().all()
                .get(URL)
                .then()
                .log().ifError()
                .extract()
                .response();
        return res;
    }
}
