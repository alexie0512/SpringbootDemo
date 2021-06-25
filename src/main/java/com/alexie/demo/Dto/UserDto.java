package com.alexie.demo.Dto;

import lombok.Data;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * ceshi
 *
 * @author Alexie on 2021/6/10
 * @ClassName UserDto
 * @Description TODO
 * @Version 1.0
 */

@Data
public class UserDto {

    private String firstName;
    private String lastName;
    private int age;

}
