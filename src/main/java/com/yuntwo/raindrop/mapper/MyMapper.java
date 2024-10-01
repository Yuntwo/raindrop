package com.yuntwo.raindrop.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 通用的MyMapper接口
 * 也避免了org.apache.ibatis.annotations.Mapper和tk.mybatis.mapper.common.Mapper在一个文件中的重名问题
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
