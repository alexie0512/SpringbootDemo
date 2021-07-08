package com.alexie.demo.dao;

import com.alexie.demo.common.MySqlExtensionMapper;
import com.alexie.demo.entity.SearchKeyword;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SearchKeywordMapper extends MySqlExtensionMapper<SearchKeyword> {

//    List<SearchKeyword> searchbyIdorName(@Param("Id") Long Id, @Param("Keyword") String  Keyword);


}