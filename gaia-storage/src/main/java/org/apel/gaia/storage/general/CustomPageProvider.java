package org.apel.gaia.storage.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apel.gaia.storage.annotation.Sql;

import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class CustomPageProvider  extends MapperTemplate{

	public CustomPageProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	
	public String pageByCondition(MappedStatement ms) {
		Class<?> entityClass = getEntityClass(ms);
		Sql sqlType = entityClass.getAnnotation(Sql.class);
		MetaObject metaObject = SystemMetaObject.forObject(ms);
		
		StringBuilder entitySql = new StringBuilder();
		entitySql.append(SqlHelper.selectAllColumns(entityClass));
		entitySql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
		entitySql.append(" where 1=1");
		String originalSql = null;
		//如果配置了实体主语句则使用实体主语句作为查询主干，如果没有配置，则使用entityClass的属性自动拼接形成sql查询主干
		if (sqlType != null){
			String resultMapId = sqlType.resultMap();
			//如果配置了返回类型，则设置当前查询的返回类型
			if (StringUtils.isNotEmpty(resultMapId)){
				List<ResultMap> resultMaps = new ArrayList<ResultMap>();
		        resultMaps.add(ms.getConfiguration().getResultMap(resultMapId));
		        metaObject.setValue("resultMaps", Collections.unmodifiableList(resultMaps));
			}else{
				setResultType(ms, entityClass);
			}
			originalSql = StringUtils.isNotEmpty(sqlType.value()) ? sqlType.value() : entitySql.toString();
		}else{
			setResultType(ms, entityClass);
			originalSql = entitySql.toString();
		}
		
		StringBuffer sql = new StringBuffer(originalSql + " ");
		sql.append(" <if test=\"conditions != null\">");
		sql.append("${conditions}");
		sql.append("</if>");
		sql.append(" <if test=\"orders != null\">");
		sql.append("${orders}");
		sql.append("</if>");
		return sql.toString();
	}
	
}
