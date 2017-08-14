package org.apel.gaia.storage.general;

import org.apache.ibatis.mapping.MappedStatement;
import org.apel.gaia.storage.annotation.Sql;

import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;

public class CustomPageProvider  extends MapperTemplate{

	public CustomPageProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	public void test1(MappedStatement ms){
		Class<?> entityClass = getEntityClass(ms);
		Sql[] annotationsByType = entityClass.getAnnotationsByType(Sql.class);
		for (Sql sql : annotationsByType) {
			String value = sql.value();
			System.out.println(value);
		}
		System.out.println(annotationsByType);
	}
	
}
