package org.apel.gaia.infrastructure.service;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

/**
 * 
 * 公用业务接口
 * @author lijian
 *
 * @param <T> 实体对象
 * @param <PK> 实体对象id
 */
public interface BizCommonService<T,PK extends Serializable> {
	
	int delete(T record);
	
	int deleteByPrimaryKey(PK key);
	
	int deleteByExample(Object example);
	
	int add(T record);
	
	int addSelective(T record);
	
	int updateByExample(T record, Object example);
	
	int updateByExampleSelective(T record, Object example);
	
	int updateByPrimaryKey(T record);
	
	int updateByPrimaryKeySelective(T record);
	
	boolean existsWithPrimaryKey(PK key);
	
	List<T> find(T record);
	
	List<T> findAll();
	
	List<T> findByExample(Object example);
	
	List<T> findByExampleAndRowBounds(Object example, RowBounds rowBounds);
	
	T findByPrimaryKey(PK key);
	
	List<T> findByRowBounds(T record, RowBounds rowBounds);
	
	int count(T record);
	
	int countByExample(Object example);
	
	T selectOne(T record);
	
	
}
