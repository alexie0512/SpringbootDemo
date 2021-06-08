package com.alexie.demo.Dto;

import lombok.Data;

/**
 * 搜索中台搜索Dto
 *
 * @author Alexie on 2021/6/4
 * @ClassName SearchDto
 * @Description TODO
 * @Version 1.0
 */


@Data
public class SearchDto {
    private String searchStr;
    private String expectedStr;
}

