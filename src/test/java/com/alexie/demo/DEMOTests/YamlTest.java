package com.alexie.demo.DEMOTests;

import com.alexie.demo.Dto.ESSearchDto;
import com.alexie.demo.Dto.EsSearchUseCaseDto;
import com.alexie.demo.Dto.UserDto;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.util.List;


/**
 * 测试Yaml文件的序列化/反序列化
 *
 * @author Alexie on 2021/6/10
 * @ClassName YamlTest
 * @Description TODO
 * @Version 1.0
 */

public class YamlTest {

    public void testread(){
        Yaml yaml = new Yaml(new Constructor(EsSearchUseCaseDto.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("searchfiles/ESSearch2.yaml");
//        UserDto userDto = yaml.load(inputStream);
//        System.out.println(userDto.getFirstName());
//        System.out.println(userDto.getLastName());
//        System.out.println(userDto.getAge());

        EsSearchUseCaseDto esSearchUseCaseDto = yaml.load(inputStream);
        System.out.println(esSearchUseCaseDto.getEsSearchDtoList());

    }

    public void testread2() throws FileNotFoundException {
        InputStream input = new FileInputStream(new File("src/test/output/t13素材搜索关键字.yml"));
        Yaml yaml = new Yaml();
        for(Object data: yaml.loadAll(input)) System.out.println(data);

    }


    public void testwrite(){
        EsSearchUseCaseDto esSearchUseCaseDto = new EsSearchUseCaseDto();
        List<ESSearchDto> esSearchDtoList;
        Yaml yaml = new Yaml();
        StringWriter writer = new StringWriter();
        yaml.dump(esSearchUseCaseDto, writer);

    }


}
