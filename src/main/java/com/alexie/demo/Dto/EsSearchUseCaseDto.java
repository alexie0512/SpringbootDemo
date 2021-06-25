package com.alexie.demo.Dto;

import lombok.Data;

import java.util.List;

/**
 * 搜索测试用例
 *
 * @author Alexie on 2021/6/10
 * @ClassName EsSearchUseCaseDto
 * @Description TODO
 * @Version 1.0
 */

@Data
public class EsSearchUseCaseDto {
    private List<ESSearchDto> esSearchDtoList;
}
