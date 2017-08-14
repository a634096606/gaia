package org.apel.gaia.infrastructure.service;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.common.Mapper;

@Component
public class AbstractBizCommonService <T,PK extends Serializable> implements BizCommonService<T, PK>{

	@Autowired
	protected Mapper<T> mapper;
	
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
	public List<T> findByExampleAndRowBounds(Object example,
			RowBounds rowBounds) {
		return mapper.selectByExampleAndRowBounds(example, rowBounds);
	}

	@Override
	public T findByPrimaryKey(PK key) {
		return mapper.selectByPrimaryKey(key);
	}

	@Override
	public List<T> findByRowBounds(T record, RowBounds rowBounds) {
		return mapper.selectByRowBounds(record, rowBounds);
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

}
