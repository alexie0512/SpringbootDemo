package com.alexie.demo.dto;

import lombok.Data;

/**
 * 素材库搜索字段Dto
 *
 * @author Alexie on 2021/6/23
 * @ClassName MaterialSearchDto
 * @Description TODO
 * @Version 1.0
 */

@Data
public class MaterialSearchDto {
    private String searchStr;
    private Integer expected_ID;
    private String expected_NAME;
    private String expected_DESC;
    private String expected_TAG;
    private Integer total_Count;
}
