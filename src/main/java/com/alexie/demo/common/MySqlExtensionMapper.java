package com.alexie.demo.common;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * mybatis 的mapper 统一父类，用于简单sql语句的快速编码
 *
 * @author Alexie on 2021/7/5
 * @ClassName MySqlExtensionMapper
 * @Description TODO
 * @Version 1.0
 */
public interface MySqlExtensionMapper<T> extends Mapper<T>, MySqlMapper<T>, IdsMapper<T> {
}
