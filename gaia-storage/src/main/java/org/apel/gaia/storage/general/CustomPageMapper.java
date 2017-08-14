package org.apel.gaia.storage.general;

import org.apache.ibatis.annotations.SelectProvider;
import org.apel.gaia.commons.pager.PageBean;

import tk.mybatis.mapper.common.Mapper;

public interface CustomPageMapper<T> extends Mapper<T>{

	@SelectProvider(type = CustomPageProvider.class, method = "dynamicSQL")
	void test1(PageBean pageBean);
	
}
