package org.apel.gaia.infrastructure.service;

import java.io.Serializable;
import java.util.List;

import org.apel.gaia.commons.pager.PageBean;

/**
 * 
 * 公用业务接口
 * @author lijian
 *
 * @param <T> 实体对象
 * @param <PK> 实体对象id
 */
public interface BizCommonService<T,PK extends Serializable> {
	
	/**
     * 根据实体属性作为条件进行删除，查询条件使用等号
     *
     * @param record
     * @return
     */
	int delete(T record);
	
	/**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     *
     * @param key
     * @return
     */
	int deleteByPrimaryKey(PK key);
	
	/**
     * 根据Example条件删除数据
     *
     * @param example
     * @return
     */
	int deleteByExample(Object example);
	
	/**
     * 保存一个实体，null的属性也会保存，不会使用数据库默认值
     *
     * @param record
     * @return
     */
	int add(T record);
	
	/**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值
     *
     * @param record
     * @return
     */
	int addSelective(T record);
	
	/**
     * 根据Example条件更新实体`record`包含的全部属性，null值会被更新
     *
     * @param record
     * @param example
     * @return
     */
	int updateByExample(T record, Object example);
	
	 /**
     * 根据Example条件更新实体`record`包含的不是null的属性值
     *
     * @param record
     * @param example
     * @return
     */
	int updateByExampleSelective(T record, Object example);
	
	/**
     * 根据主键更新实体全部字段，null值会被更新
     *
     * @param record
     * @return
     */
	int updateByPrimaryKey(T record);
	
	/**
     * 根据主键更新属性不为null的值
     *
     * @param record
     * @return
     */
	int updateByPrimaryKeySelective(T record);
	
	/**
     * 根据主键字段查询总数，方法参数必须包含完整的主键属性，查询条件使用等号
     *
     * @param key
     * @return
     */
	boolean existsWithPrimaryKey(PK key);
	
	/**
     * 根据实体中的属性值进行查询，查询条件使用等号
     *
     * @param record
     * @return
     */
	List<T> find(T record);
	
	/**
     * 查询全部结果
     *
     * @return
     */
	List<T> findAll();
	
	/**
     * 根据Example条件进行查询
     *
     * @param example
     * @return
     */
	List<T> findByExample(Object example);
	
	/**
     * 根据example条件和pageBean进行分页查询(不会统计查询总数)，此方法当中不会利用pageBean的Condition条件
     *
     * @param example
     * @param rowBounds
     * @return
     */
	void pageFindByExample(Object example, PageBean pageBean);
	
	/**
     * 根据example条件和pageBean进行分页查询(会统计查询总数)，此方法当中不会利用pageBean的Condition条件
     *
     * @param example
     * @param rowBounds
     * @return
     */
	void pageFindByExampleWithCount(Object example, PageBean pageBean);
	
	/**
     * 根据实体属性和pageBean进行分页查询(不会统计查询总数)，此方法当中不会利用pageBean的Condition条件
     *
     * @param record
     * @param rowBounds
     * @return
     */
	void pageFindByEntity(T record, PageBean pageBean);
	
	void pageFindByCondition(PageBean pageBean);
	
	void pageFindByConditionWithCount(PageBean pageBean);
	
	/**
     * 根据实体属性和pageBean进行分页查询(会统计查询总数)，此方法当中不会利用pageBean的Condition条件
     *
     * @param record
     * @param rowBounds
     * @return
     */
	void pageFindByEntityWithCount(T record, PageBean pageBean);
	
	/**
     * 根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
     *
     * @param key
     * @return
     */
	T findByPrimaryKey(PK key);
	
	/**
     * 根据实体中的属性查询总数，查询条件使用等号
     *
     * @param record
     * @return
     */
	int count(T record);
	
	/**
     * 根据Example条件进行查询总数
     *
     * @param example
     * @return
     */
	int countByExample(Object example);
	
	/**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     *
     * @param record
     * @return
     */
	T selectOne(T record);
	
	
}
