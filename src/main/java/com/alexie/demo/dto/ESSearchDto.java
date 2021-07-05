package com.alexie.demo.dto;

import lombok.Data;

/**
 * ES搜索Dto
 *
 * @author Alexie on 2021/6/4
 * @ClassName ESSearchDto
 * @Description TODO
 * @Version 1.0
 */

@Data
public class ESSearchDto {
    private String text;
    private String analyzer;
}
