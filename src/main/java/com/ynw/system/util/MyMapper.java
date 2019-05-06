package com.ynw.system.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author ChengZhi
 * @version 2018/11/24
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
