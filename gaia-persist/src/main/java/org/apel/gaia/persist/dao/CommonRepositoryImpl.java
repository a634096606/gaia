package org.apel.gaia.persist.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Query;

import org.apel.gaia.commons.pager.Condition;
import org.apel.gaia.commons.pager.Operation;
import org.apel.gaia.commons.pager.Order;
import org.apel.gaia.commons.pager.OrderType;
import org.apel.gaia.commons.pager.PageBean;
import org.apel.gaia.commons.pager.RelateType;
import org.apel.gaia.util.CglibBeanUtil;
import org.apel.gaia.util.UUIDUtil;
import org.hibernate.annotations.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.StringUtils;

/**
 * @author lijian
 * 自定义repository的方法接口实现类,该类主要提供自定义的公用方法(doPagger等)
 */
public class CommonRepositoryImpl<T, ID extends Serializable> 
	extends SimpleJpaRepository<T, Serializable> implements CommonRepository<T, Serializable>{
	
	private static final Logger LOG = LoggerFactory.getLogger(CommonRepositoryImpl.class);
	
	private final EntityManager entityManager;
	
	public CommonRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
			EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	



	@Override
	public void store(Object... item) {
		if (null != item) {
			for (Object entity : item) {
				innerSave(entity);
			}
		}
	}

	@Override
	public void update(Object... item) {
		if (null != item) {
			for (Object entity : item) {
				entityManager.merge(entity);
			}
		}
	}

	@Override
	public int executeUpdate(String qlString, Object... values) {
		Query query = entityManager.createQuery(qlString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i + 1, values[i]);
			}
		}
		return query.executeUpdate();
	}

	@Override
	public int executeUpdate(String qlString, Map<String, Object> params) {
		Query query = entityManager.createQuery(qlString);
		for (String name : params.keySet()) {
			query.setParameter(name, params.get(name));
		}
		return query.executeUpdate();
	}

	@Override
	public int executeUpdate(String qlString, List<Object> values) {
		Query query = entityManager.createQuery(qlString);
		for (int i = 0; i < values.size(); i++) {
			query.setParameter(i + 1, values.get(i));
		}
		return query.executeUpdate();
	}

	@Override
	public void doPager(PageBean pageBean, String qlString) {
		doPager(pageBean, qlString, new ArrayList<Object>());
	}

	@Override
	public void doPager(PageBean pageBean, String qlString, boolean cacheable) {
		doPager(pageBean, qlString, new ArrayList<Object>(),cacheable);
	}

	@Override
	public void doPager(PageBean pageBean, String qlString,
			Map<String, Object> params) {
		List<Object> values = new ArrayList<Object>();// 来自params的values,值的顺序按照qlString的参数顺序存放
		qlString = preQLAndParam(qlString, params, values);// 2.将name形式的参数解析成？号形式的参数，并返回参数值集合
		doPager(pageBean, qlString, values);
	}

	@Override
	public void doPager(PageBean pageBean, String qlString, List<Object> values){
		doPager(pageBean, qlString, values, false);
	}

	@Override
	public void doPager(PageBean pageBean, String qlString, Object... values){
		List<Object> list = new ArrayList<Object>();
		if(values!=null){
			for (Object value : values) {
				list.add(value);
			}
		}
		doPager(pageBean, qlString, list);
	}
	
	private void doPager(PageBean pageBean, String qlString, List<Object> values,boolean cacheable){
		if (values == null) {
			values = new ArrayList<Object>();
		}
		qlString = convertQL(qlString);// 1.转换ql为指定格式的语句
		List<Object> conValues = new ArrayList<Object>();// 条件参数值集合,来自pageBean.conditions的values
		List<Object> list = preConditionJPQL(qlString,
				pageBean.getConditions(), conValues);// 解析条件语句,获取条件参数集合
		int conBeginIndex = (Integer) list.get(0);// 返回条件集合在hql起始位置
		String condition_jpql = (String) list.get(1);// 获取条件ql语句
		String order_jpql = preOrderJPQL(pageBean.getOrders());// 获取排序ql语句
		String list_ql = preQL(qlString, condition_jpql, order_jpql);// 获取完整的list ql语句
		String count_ql = preCountJPQL(qlString, condition_jpql);// 获取完整的count ql语句
		LOG.info("count_ql = {" + count_ql + "}");
		// 合并数据
		for (int i = conValues.size() - 1; i >= 0; i--) {
			values.add(conBeginIndex, conValues.get(i));
		}
		executeCount(pageBean, count_ql, values, conBeginIndex,cacheable);// 执行count语句，将填充pageBean中的totalRows
		executeList(pageBean, list_ql, values, cacheable, false, true);// 执行list语句，将填充pageBean中的items
	}
	
	public List<?> doList(String qlString, List<Condition> conditions, List<Order> orders, boolean sqlable){
		return doList(qlString, new ArrayList<Object>(), conditions, orders, sqlable);
	}
	
	public List<?> doList(String qlString, List<Object> values, List<Condition> conditions, List<Order> orders, boolean sqlable){
		if (values == null) {
			values = new ArrayList<Object>();
		}
		qlString = convertQL(qlString);// 1.转换ql为指定格式的语句
		List<Object> conValues = new ArrayList<Object>();// 条件参数值集合,来自pageBean.conditions的values
		List<Object> list = preConditionJPQL(qlString,
				conditions, conValues);// 解析条件语句,获取条件参数集合
		int conBeginIndex = (Integer) list.get(0);// 返回条件集合在hql起始位置
		String condition_jpql = (String) list.get(1);// 获取条件ql语句
		String order_jpql = preOrderJPQL(orders);// 获取排序ql语句
		String list_ql = preQL(qlString, condition_jpql, order_jpql);// 获取完整的list ql语句
		// 合并数据
		for (int i = conValues.size() - 1; i >= 0; i--) {
			values.add(conBeginIndex, conValues.get(i));
		}
		PageBean pageBean = new PageBean();
		executeList(pageBean, list_ql, values, false, sqlable, false);
		return pageBean.getItems();// 执行list语句，将填充pageBean中的items
	}

	@Override
	public int batchDeleteByQl(Class<?> entityClass, Object... primaryKeyValues) {
		if(primaryKeyValues==null || primaryKeyValues.length<1)return 0;
		StringBuilder sb = new StringBuilder("delete from " + entityClass.getSimpleName()+ " where " + getIdName(entityClass) + " in (");
		for (int i = 0; i < primaryKeyValues.length; i++) {
			sb.append("?,");
		}
		sb.replace(sb.length()-1, sb.length(), "");
		sb.append(")");
		Query query = entityManager.createQuery(sb.toString());
		for (int i = 1; i <= primaryKeyValues.length; i++) {
			query.setParameter(i, primaryKeyValues[i-1]);
		}
		int result = query.executeUpdate();
		entityManager.clear();
		return result;
	}

	@Override
	public int batchDeleteByQl(Class<?> entityClass, String... pKeyVals) {
		Object[]pvals = pKeyVals;
		return batchDeleteByQl(entityClass, pvals);
	}
	
	private Serializable innerSave(Object item) {
		try {
			if(item==null)return null;
			Class<?> clazz = item.getClass();
			Field idField = getIdField(clazz);
			Method getMethod = null;
			if(idField!=null){
				Class<?> type = idField.getType();
				Object val = idField.get(item);
				if(type == String.class && (val==null || "".equals(val))){
					idField.set(item, UUIDUtil.uuid());
				}
			}else{
				Method[] methods = clazz.getDeclaredMethods();
				for (Method method : methods) {
					Id id = method.getAnnotation(Id.class);
					if (id != null) {
						Object val = method.invoke(item);
						if(val==null || "".equals(val)){
							String methodName = "s" + method.getName().substring(1);
							Method setMethod = clazz.getDeclaredMethod(methodName, method.getReturnType());
							if(setMethod!=null){
								setMethod.invoke(item, UUIDUtil.uuid());
							}
						}
						getMethod = method;
						break;
					}
				}
			}
			entityManager.persist(item);
			entityManager.flush();
			if(idField!=null){
				return (Serializable) idField.get(item);	
			}
			if(getMethod!=null){
				return (Serializable)getMethod.invoke(item);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}
	
	private String getIdName(Class<?> clazz) {
		Field idField = getIdField(clazz);
		if (idField != null) {
			return idField.getName();
		}
		return null;
	}
	
	private Field getIdField(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		Field item = null;
		for (Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				field.setAccessible(true);
				item = field;
				break;
			}
		}
		if(item==null){
			Class<?> superclass = clazz.getSuperclass();
			if(superclass!=null){
				item = getIdField(superclass);
			}
		}
		return item;
	}
	
	private void setParameter(Query query, int position, Object value){
		try {
			query.setParameter(position, value);
		} catch (IllegalArgumentException e) {
			LOG.info("WARN : " + e.getMessage());
			Pattern p = Pattern.compile("(\\w+\\.\\w+\\.\\w+)");
			Matcher matcher = p.matcher(e.getMessage());
			while(matcher.find()){
				String clazz = matcher.group(1);
				if (Integer.class.getName().equals(clazz)) {
					value = Integer.parseInt(value.toString());
				}else if(Long.class.getName().equals(clazz)){
					value = Long.parseLong(value.toString());
				}else if(Double.class.getName().equals(clazz)){
					value = Double.parseDouble(value.toString());
				}else if(Boolean.class.getName().equals(clazz)){
					if("1".equals(value.toString())
							|| "true".equals(value.toString().toLowerCase())){
						value = true;
					}else if("0".equals(value.toString())
							|| "false".equals(value.toString().toLowerCase())){
						value = false;
					}else{
						value = null;
					}
				}else if(Date.class.getName().equals(clazz)){
					String temp = value.toString().replaceAll("%", "");
					String pattern = null;
					if(temp.matches("^\\d{4}-\\d{2}-\\d{2}$")){
						pattern = "yyyy-MM-dd";
					}else if(temp.matches("^\\d{8}$")){
						pattern = "yyyyMMdd";
					}else if(temp.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")){
						pattern = "yyyy-MM-dd HH:mm:ss";
					}
					if(pattern!=null){
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						try {
							value = sdf.parse(temp);
						} catch (ParseException e1) {
							throw new RuntimeException(e1);
						}
					}else{
						throw new RuntimeException("传递的日期值"+temp+"不能够被识别");
					}
				}
				query.setParameter(position, value);
			}
		}
	}
	
	/**
	 * 执行集合语句
	 */
	protected void executeList(PageBean pageBean, String list_ql,
			List<Object> values, boolean cacheable, boolean sqlable, boolean isPage){
		Query query = null;
		if(sqlable){
			query = entityManager.createNativeQuery(list_ql);
			query.setHint(QueryHints.CACHEABLE, cacheable);
		}else{
			query = entityManager.createQuery(list_ql);
		}
		for (int i = 0; i < values.size(); i++) {
			setParameter(query, i + 1, values.get(i));
		}
		if(!sqlable && isPage){
			int firstResult = (pageBean.getCurrentPage() - 1)
					* pageBean.getRowsPerPage();
			int maxResults = pageBean.getRowsPerPage();
			query.setFirstResult(firstResult)
			.setMaxResults(maxResults);
		}
		List<?> list = query.getResultList();
		if(list.size()>0){
			Object item = list.get(0);
			if(item != null){
				if(item.getClass().isArray()){
					String[] fieldArray = preFieldInfo(list_ql);
					Map<String, Class<?>> propertyMap = preProp(fieldArray,(Object[]) list.get(0));
					List<Object> items = new ArrayList<Object>();
					for (Object object : list) {
						Object[] entity = (Object[]) object;
						CglibBeanUtil bean = new CglibBeanUtil(propertyMap);
						for (int i = 0; i < fieldArray.length; i++) {
							bean.setValue(fieldArray[i], entity[i]);
						}
						items.add(bean.getObject());
					}
					list = items;
				}
			}
		}
		pageBean.setItems(list);
	}

	
	public Map<String, Class<?>> preProp(String[] fieldArray, Object[] fieldValArray) {
		Map<String, Class<?>> propertyMap = new HashMap<String, Class<?>>();
		Class<?> clazz = null;
		for (int i = 0; i < fieldArray.length; i++) {
			clazz = fieldValArray[i]!=null?fieldValArray[i].getClass():Object.class;
			propertyMap.put(fieldArray[i],clazz);
		}
		return propertyMap;
	}

	public String[] preFieldInfo(String list_ql) {
		String[] fieldArray;
		int firstFormIndex = list_ql.indexOf("FROM");
		LOG.info("firstFromIndex={" + firstFormIndex + "}");
		String prefixFrom = list_ql.substring(0, firstFormIndex);
		LOG.info("prefixFrom={" + prefixFrom + "}");
		fieldArray = prefixFrom.replace("SELECT", "").trim().split(",");
		for (int i = 0; i < fieldArray.length; i++) {
			String field = fieldArray[i];
			String[] s = field.split(" as | AS | ");
			if(s.length==2){
				fieldArray[i] = s[1];
			}
			String[]tempArray = fieldArray[i].split("\\.");
			fieldArray[i] = tempArray[tempArray.length-1];
		}
		return fieldArray;
	}

	/**
	 * 解析ql语句
	 */
	protected String preQL(String qlString, String condition_jpql,
			String order_jpql) {
		if (qlString.endsWith("ASC") || qlString.endsWith("DESC")) {
			order_jpql = order_jpql.replace("ORDER BY", ",");
		}
		qlString = qlString.replaceAll("WHERE 1=1", " WHERE 1=1 "
				+ condition_jpql)
				+ order_jpql;// 排序位置有待修改
		LOG.info("list_ql = {" + qlString + "}");
		return qlString;
	}

	/**
	 * 解析统计ql语句
	 * 
	 * @param qlString
	 * @return
	 * @throws Exception
	 */
	protected String preCountJPQL(String qlString, String condition_jpql){
		String countField = "*";
		String distinctField = findDistinctField(qlString);
		if(distinctField!=null){
			countField = "DISTINCT " + distinctField;
		}
		if (qlString.matches("^FROM.+")) {
			qlString = "SELECT count("+countField+") " + qlString;
		} else {
			int beginIndex = qlString.indexOf("FROM");
			qlString = "SELECT count("+countField+") " + qlString.substring(beginIndex);
		}
		qlString = qlString.replaceAll("WHERE 1=1", " WHERE 1=1 "
				+ condition_jpql);
		return qlString;
	}

	private String findDistinctField(String qlString) {
		int index = qlString.indexOf("DISTINCT");
		if(index!=-1){
			String subql = qlString.substring(index+9);
			int end = -1;
			for (int i = 0; i < subql.length(); i++) {
				if(subql.substring(i,i+1).matches(" |,")){
					end = i;
					break;
				}
			}
			return subql.substring(0, end);
		}
		return null;
	}

	/**
	 * 解析排序语句
	 * @param orders
	 * @return
	 */
	protected String preOrderJPQL(List<Order> orders) {
		if (orders.size() == 0) {
			return "";
		}
		StringBuffer c = new StringBuffer(" ORDER BY ");
		for (Order order : orders) {
			String propertyName = order.getPropertyName();
			OrderType orderType = order.getOrderType();
			c.append(propertyName + " " + orderType + ",");
		}
		if (orders.size() > 0) {
			c.replace(c.length() - 1, c.length(), "");
		}
		LOG.info("order = {" + c.toString() + "}");
		return c.toString();
	}

	/**
	 * 格式化ql
	 * @param qlString
	 * @return
	 */
	protected String convertQL(String qlString) {
		String result = qlString.replaceAll("from", "FROM")
				.replaceAll("distinct", "DISTINCT")
				.replaceAll("select", "SELECT").replaceAll("where", "WHERE")
				.replaceAll("order by", "ORDER BY").replaceAll("asc", "ASC")
				.replaceAll("desc", "DESC").trim();
		LOG.info("qlString = {" + result + "}");
		return result;
	}

	/**
	 * 解析ql语句和参数
	 * 
	 * @param qlString
	 * @param params
	 * @param values
	 * @return
	 */
	protected String preQLAndParam(String qlString, Map<String, Object> params,
			List<Object> values) {
		LOG.info("开始解析qlString：{" + qlString + "}");
		Map<Integer, Object> map_values = new HashMap<Integer, Object>();
		Map<Integer, String> map_names = new HashMap<Integer, String>();
		List<Integer> list = new ArrayList<Integer>();
		String preQL = qlString;
		if (null != params) {
			for (String key : params.keySet()) {
				int index = qlString.indexOf(":" + key);
				Object value = params.get(key);
				preQL = preQL.replaceAll(":" + key + " ", "? ");
				list.add(index);
				map_values.put(index, value);
				map_names.put(index, key);
			}
			LOG.info("解析完成qlString:{" + preQL + "}");
			Collections.sort(list);
			LOG.info("最终参数值顺序(参数名->参数位置->参数值)：");
			for (Integer position : list) {
				if (LOG.isDebugEnabled()) {
					System.out.println(map_names.get(position) + "->"
							+ position + "->" + map_values.get(position));
				}
				values.add(map_values.get(position));
			}
		}
		return preQL;
	}

	private String preConditionJPQL(List<Condition> conditions,
			List<Object> values){
 		StringBuffer c = new StringBuffer();
		if (conditions != null && conditions.size() > 0) {
			c.append(RelateType.AND.toString() + " ( ");
			for (int i = 0; i < conditions.size(); i++) {
				Condition condition = conditions.get(i);
				String groupPrefixBrackets = condition.getGroupPrefixBrackets();
				String propertyName = condition.getPropertyName();
				Object value = condition.getPropertyValue();
				boolean isPrefixBrackets = condition.isPrefixBrackets();
				boolean isSuffixBrackets = condition.isSuffixBrackets();
				Operation operation = condition.getOperation();
				RelateType relateType = condition.getRelateType();
				String related = "";
				if(i!=0){
					if(relateType==null){
						relateType = RelateType.AND;
					}
					related = relateType  + (isPrefixBrackets ? 
							StringUtils.isEmpty(condition.getPreffixBracketsValue()) ? " ( " : " " + 
								condition.getPreffixBracketsValue() + " " 
							: " ");
				}else{
					related = "" + (isPrefixBrackets ? 
							StringUtils.isEmpty(condition.getPreffixBracketsValue()) ? " ( " : " " + 
							condition.getPreffixBracketsValue() + " " 
						: " ");
				}
				c.append(groupPrefixBrackets);
				switch (operation) {
				case NC:
				case CN:
					String[] list = value.toString().split("[, ]");
					if(list.length>1){
						c.append(related + " ( " + propertyName + operation + "?");
						values.add("%" + list[0] + "%");
						for (int j = 1; j < list.length; j++) {
							c.append(RelateType.OR + propertyName + operation + "?");
							values.add("%" + list[j] + "%");
						}
						c.append(" ) "); 
					}else{
						c.append(related + propertyName + operation + "?");
						values.add("%" + value + "%");
					}
					break;
				case BN:
				case BW:
					c.append(related + propertyName + operation + "?");
					values.add(value + "%");
					break;
				case EN:
				case EW:
					c.append(related + propertyName + operation + "?");
					values.add("%" + value);
					break;
				case BETWEEN:
					c.append(related + propertyName + operation + "?" + " AND "
							+ "?");
					Object[] params = new Object[2];
					if (value instanceof String) {
						String[] array = value.toString().split("#|,");
						params[0] = array[0];
						params[1] = array[1];
					} else {
						params = (Object[]) value;
					}
					values.add(params[0]);
					values.add(params[1]);
					break;
				case NI:
				case IN:
					c.append(related + propertyName + operation + "(");
					if(value!=null){
						Class<?> clazz = value.getClass();
						if (clazz.isArray()) {
							Object[] array = (Object[])value;
							for (Object object : array) {
								c.append("?,");
								values.add(object);
							}
							if(array.length>0){
								c.replace(c.length() - 1, c.length(), "");
							}
						} else if (value instanceof Collection<?>) {
							Collection<?> coll = (Collection<?>) value;
							for (Object object : coll) {
								c.append("?,");
								values.add(object);
							}
							if(coll.size()>0){
								c.replace(c.length() - 1, c.length(), "");
							}
						}else if(value instanceof String){
							if(StringUtils.isEmpty((String)value)){
								c.append("NULL");
							}else{
								String[]array = ((String) value).split(",");
								for (String val : array) {
									c.append("?,");
									values.add(val);
								}
								if(array.length>0){
									c.replace(c.length() - 1, c.length(), "");
								}
							}
						}
					}else{
						c.append("NULL");
					}
					c.append(")");
					break;
				case EQ:
				case GE:
				case GT:
				case LE:
				case LT:
				case NE:
					c.append(related + propertyName + operation + "?");
					values.add(value);
					break;
				case NN:
				case NU:
					c.append(related + propertyName + operation);
					break;
				default:
					break;
				}
				c.append(isSuffixBrackets ? 
						StringUtils.isEmpty(condition.getSuffixBracketsValue()) ? " ) " : " " + condition.getSuffixBracketsValue() + " " 
								: " ");
			}
			c.append(" ) ");
		}
		LOG.info("condition = {" + c.toString() + "}");
		return c.toString();
	}
	
	/**
	 * 解析条件语句
	 * 
	 * @param qlString
	 * @param conditions
	 * @param conValues
	 * @throws Exception
	 */
	protected List<Object> preConditionJPQL(String qlString,
			List<Condition> conditions, List<Object> conValues)
			{
		List<Object> list = new ArrayList<Object>();
		int conBeginIndex = getConBeginIndex(qlString);
		list.add(conBeginIndex);
		list.add(preConditionJPQL(conditions, conValues).toString());
		return list;
	}
	
	protected int getConBeginIndex(String qlString) {
		int conIndex = qlString.indexOf("WHERE 1=1");
		if (conIndex == -1) {
			throw new RuntimeException("ql中没有WHERE 1=1");
		}
		LOG.info("conIndex = {" + conIndex + "}");
		String conBefore = qlString.substring(0, conIndex);
		String conAfter = qlString.substring(conIndex);
		int conBeforeCount = counter(conBefore, '?');
		int conAfterCount = counter(conAfter, '?');
		LOG.info("条件前的?个数：{" + conBeforeCount + "}");
		LOG.info("条件后的?个数：{" + conAfterCount + "}");
		LOG.info("条件的起始位置：{" + conBeforeCount + "}");
		return conBeforeCount;
	}

	protected int counter(String s, char c) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == c) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * 执行统计语句
	 */
	protected void executeCount(PageBean pageBean, String count_ql,
			List<Object> values, int conBeginIndex,boolean cacheable){
		Query query = entityManager.createQuery(count_ql);
		query.setHint(QueryHints.CACHEABLE, cacheable);
		
		for (int i = 0; i + conBeginIndex < values.size(); i++) {
			setParameter(query, i + 1, values.get(i + conBeginIndex));
		}
		List<?> list = null;
		list = query.getResultList();
		if (list.size() == 1) {
			int totalRows = Integer.parseInt(list.get(0).toString());
			LOG.info("executeCount totalRows = {" + totalRows + "}");
			pageBean.setTotalRows(totalRows);
		} else {
			pageBean.setTotalRows(list.size());
		}
	}
	
}
