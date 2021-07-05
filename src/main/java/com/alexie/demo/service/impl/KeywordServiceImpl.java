package com.alexie.demo.service.impl;

import com.alexie.demo.dao.SearchKeywordMapper;
import com.alexie.demo.entity.SearchKeyword;
import com.alexie.demo.service.KeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Alexie on 2021/7/5
 * @ClassName KeywordServiceImpl
 * @Description TODO
 * @Version 1.0
 */


@Service
public class KeywordServiceImpl implements KeywordService {
    //Logger
    protected static Logger logger = LoggerFactory.getLogger(KeywordServiceImpl.class);



    @Autowired
    private SearchKeywordMapper searchKeywordMapper;

    @Override
    public List<String> searchbyNameorId(String searchStr) {

        /**
         * 通过搜索关键词，判断该关键词是否有同义词，有就获取，无就打印：该搜索关键词没有关联同义词！
         */

        SearchKeyword searchKeyword = new SearchKeyword();
        searchKeyword.setKeyword(searchStr);
        SearchKeyword keyword = searchKeywordMapper.selectOne(searchKeyword);
        List<String> Synonomlist = new ArrayList<>();

        if (Objects.isNull(keyword)) {
            logger.info("==============该搜索关键词不在词库内！！==================");
        } else {
            //获取搜索关键词的同义词字段
            String SynonomIds = keyword.getSynonym();
            try{
                String str[] = SynonomIds.split(",");
                List<String> SynonomList = Arrays.asList(str);
                for(String id:SynonomList){
                    List<SearchKeyword> searchKeywordList = searchKeywordMapper.selectByIds(id);
                    for(SearchKeyword s: searchKeywordList){
                        Synonomlist.add(s.getKeyword());
                    }
                }
            }catch (Exception e){
                logger.info("==============该搜索关键词没有同义词！！==================");
            }
        }

        return Synonomlist;
    }
}
