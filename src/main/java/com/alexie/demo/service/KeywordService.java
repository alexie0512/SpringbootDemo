package com.alexie.demo.service;

import com.alexie.demo.entity.SearchKeyword;

import java.util.List;

/**
 * @author Alexie on 2021/7/5
 * @ClassName KeywordService
 * @Description TODO
 * @Version 1.0
 */
public interface KeywordService {

    /**
     * 根据用户id 和 名字 模糊查询
     * @param searchStr
     * @return
     */
    public List<String> searchbyNameorId(String searchStr);
}
