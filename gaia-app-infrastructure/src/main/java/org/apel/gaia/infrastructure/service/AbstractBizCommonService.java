package org.apel.gaia.infrastructure.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apel.gaia.commons.pager.Condition;
import org.apel.gaia.commons.pager.Order;
import org.apel.gaia.commons.pager.PageBean;
import org.apel.gaia.storage.general.CustomPageMapper;
import org.apel.gaia.storage.util.SQLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Component
public class AbstractBizCommonService <T,PK extends Serializable> implements BizCommonService<T, PK>{

	@Autowired
	protected CustomPageMapper<T> mapper;
	
	@Transactional
	@Override
	public int delete(T record) {
		return mapper.delete(record);
	}

	@Transactional
	@Override
	public int deleteByPrimaryKey(PK key) {
		return mapper.deleteByPrimaryKey(key);
	}

	@Transactional
	@Override
	public int deleteByExample(Object example) {
		return mapper.deleteByExample(example);
	}

	@Override
	public int add(T record) {
		return mapper.insert(record);
	}

	@Transactional
	@Override
	public int addSelective(T record) {
		return mapper.insertSelective(record);
	}

	@Transactional
	@Override
	public int updateByExample(T record, Object example) {
		return mapper.updateByExample(record, example);
	}

	@Transactional
	@Override
	public int updateByExampleSelective(T record, Object example) {
		return mapper.updateByExampleSelective(record, example);
	}

	@Transactional
	@Override
	public int updateByPrimaryKey(T record) {
		return mapper.updateByPrimaryKey(record);
	}

	@Transactional
	@Override
	public int updateByPrimaryKeySelective(T record) {
		return mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public boolean existsWithPrimaryKey(PK key) {
		return mapper.existsWithPrimaryKey(key);
	}

	@Override
	public List<T> find(T record) {
		return mapper.select(record);
	}

	@Override
	public List<T> findAll() {
		return mapper.selectAll();
	}

	@Override
	public List<T> findByExample(Object example) {
		return mapper.selectByExample(example);
	}

	@Override
	public void pageFindByExample(Object example, PageBean pageBean) {
		RowBounds rouBounds = getRowBounds(pageBean);
		List<T> items = mapper.selectByExampleAndRowBounds(example, rouBounds);
		pageBean.setItems(items);
	}
	
	@Override
	public void pageFindByExampleWithCount(Object example, PageBean pageBean) {
		RowBounds rouBounds = getRowBounds(pageBean);
		List<T> items = mapper.selectByExampleAndRowBounds(example, rouBounds);
		int count = mapper.selectCountByExample(example);
		pageBean.setItems(items);
		pageBean.setTotalRows(count);
	}

	@Override
	public T findByPrimaryKey(PK key) {
		return mapper.selectByPrimaryKey(key);
	}

	@Override
	public void pageFindByEntity(T record, PageBean pageBean) {
		RowBounds rouBounds = getRowBounds(pageBean);
		List<T> items = mapper.selectByRowBounds(record, rouBounds);
		pageBean.setItems(items);
	}
	
	@Override
	public void pageFindByEntityWithCount(T record, PageBean pageBean) {
		RowBounds rouBounds = getRowBounds(pageBean);
		List<T> items = mapper.selectByRowBounds(record, rouBounds);
		int count = mapper.selectCount(record);
		pageBean.setItems(items);
		pageBean.setTotalRows(count);
	}

	
	//根据pageBean得到mybatis的rowBounds
	private RowBounds getRowBounds(PageBean pageBean) {
		int currentPage = pageBean.getCurrentPage();
		int rowsPerPage = pageBean.getRowsPerPage();
		int offset = (currentPage - 1) * rowsPerPage;
		RowBounds rouBounds = new RowBounds(offset, rowsPerPage);
		return rouBounds;
	}

	@Override
	public int count(T record) {
		return mapper.selectCount(record);
	}

	@Override
	public int countByExample(Object example) {
		return mapper.selectCountByExample(example);
	}

	@Override
	public T selectOne(T record) {
		return mapper.selectOne(record);
	}

	@Override
	public void pageFindByCondition(PageBean pageBean) {
		List<Condition> conditions = pageBean.getConditions();
		List<Order> orders = pageBean.getOrders();
		Map<String, Object> values = convertConditions(conditions, orders);
		//根据pagbean中的分页条件调用分页插件进行分页
		PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getRowsPerPage());
		PageInfo<T> page = new PageInfo<>(mapper.pageByCondition(values));
		pageBean.setItems(page.getList());
		pageBean.setTotalRows(Integer.valueOf(String.valueOf(page.getTotal())));
	}
	
	//转换pagebean中设置的条件
	private Map<String, Object> convertConditions(List<Condition> conditions,
			List<Order> orders) {
		Map<String, Object> values = new HashMap<>();
		//解析pagebean where条件
		String conditionSql = SQLUtil.parseCondition(conditions, values);
		//解析pagebean order条件
		String orderSql = SQLUtil.parseOrder(orders);
		//添加到mybatis参数集合中
		if (StringUtils.isEmpty(conditionSql)){
			values.put("conditions", null);
		}else{
			values.put("conditions", conditionSql);
		}
		if (StringUtils.isEmpty(orderSql)){
			values.put("orders", null);
		}else{
			values.put("orders", orderSql);
		}
		return values;
	}


}
