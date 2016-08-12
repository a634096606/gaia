package org.apel.gaia.persist.util;

import java.util.List;
import java.util.Map;

import org.apel.gaia.commons.pager.Condition;
import org.apel.gaia.commons.pager.Order;
import org.apel.gaia.commons.pager.PageBean;


public interface IGeneralDao {

	/**
	 * 保存对象<br/>
	 * 注意：如果对象id是字符串，并且没有赋值，该方法将自动设置为uuid值
	 * @param item
	 *            持久对象，或者对象集合
	 * @throws Exception
	 */
	public void store(Object... item);
	
	/**
	 * 更新对象数据
	 * 
	 * @param item
	 *            持久对象，或者对象集合
	 * @throws Exception
	 */
	public void update(Object... item) ;
	
	/**
	 * 执行ql语句
	 * @param qlString 基于jpa标准的ql语句
	 * @param values ql中的?参数值,单个参数值或者多个参数值
	 * @return 返回执行后受影响的数据个数
	 */
	public int executeUpdate(String qlString, Object... values);

	/**
	 * 执行ql语句
	 * @param qlString 基于jpa标准的ql语句
	 * @param params key表示ql中参数变量名，value表示该参数变量值
	 * @return 返回执行后受影响的数据个数
	 */
	public int executeUpdate(String qlString, Map<String, Object> params);
	
	/**
	 * 执行ql语句，可以是更新或者删除操作
	 * @param qlString 基于jpa标准的ql语句
	 * @param values ql中的?参数值
	 * @return 返回执行后受影响的数据个数
	 * @throws Exception
	 */
	public int executeUpdate(String qlString, List<Object> values);
	
	/**
	 * 结合提供的分页信息，获取指定条件下的数据对象
	 * @param pageBean 分页信息
	 * @param qlString 基于jpa标准的ql语句
	 */
	public void doPager(PageBean pageBean, String qlString);
	
	/**
	 * 结合提供的分页信息，获取指定条件下的数据对象
	 * @param pageBean 分页信息
	 * @param qlString 基于jpa标准的ql语句
	 * @param cacheable 是否启用缓存查询
	 */
	public void doPager(PageBean pageBean,String qlString,boolean cacheable);
	
	/**
	 * 结合提供的分页信息，获取指定条件下的数据对象
	 * @param pageBean 分页信息
	 * @param qlString 基于jpa标准的ql语句
	 * @param params key表示ql中参数变量名，value表示该参数变量值
	 */
	public void doPager(PageBean pageBean, String qlString,
			Map<String, Object> params);

	/**
	 * 结合提供的分页信息，获取指定条件下的数据对象
	 * @param pageBean 分页信息
	 * @param qlString 基于jpa标准的ql语句
	 * @param values ql中的?参数值
	 */
	public void doPager(PageBean pageBean, String qlString, List<Object> values)
			throws Exception;
	
	
	
	/**
	 * 结合提供的分页信息，获取指定条件下的数据对象
	 * @param pageBean 分页信息
	 * @param qlString 基于jpa标准的ql语句
	 * @param values ql中的?参数值
	 */
	public void doPager(PageBean pageBean, String qlString, Object... values);
	
	/**
	 * 批量删除数据对象
	 * @param entityClass
	 * @param primaryKeyValues
	 * @return
	 */
	public int batchDeleteByQl(Class<?> entityClass,Object...primaryKeyValues);
	
	/**
	 * 批量删除数据对象
	 * @param entityClass
	 * @param pKeyVals 主键是字符串形式的
	 * @return
	 * @throws Exception
	 */
	public int batchDeleteByQl(Class<?> entityClass,String...pKeyVals);
	
	/**
	 * @param qlString 查询hql语句
	 * @param values hql参数值
	 * @param conditions 查询条件
	 * @param orders 排序条件
	 * @return
	 */
	public List<?> doList(String qlString, List<Object> values, List<Condition> conditions, List<Order> orders, boolean sqlable);
	
	public List<?> doList(String qlString, List<Condition> conditions, List<Order> orders, boolean sqlable);
	
}
