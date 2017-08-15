package org.apel.gaia.storage.general;

import java.util.Map;

import org.apache.ibatis.annotations.SelectProvider;

import tk.mybatis.mapper.common.Mapper;

import com.github.pagehelper.Page;

public interface CustomPageMapper<T> extends Mapper<T>{

	
	@SelectProvider(type = CustomPageProvider.class, method = "dynamicSQL")
	Page<T> pageByCondition(Map<String, Object> param);
	
}
