package org.apel.gaia.storage.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apel.gaia.commons.pager.Condition;
import org.apel.gaia.commons.pager.Operation;
import org.apel.gaia.commons.pager.Order;
import org.apel.gaia.commons.pager.OrderType;
import org.apel.gaia.commons.pager.RelateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class SQLUtil {

	private static final Logger LOG = LoggerFactory.getLogger(SQLUtil.class);
	
	/**
	 * 解析Condition为sql语句
	 * @param conditions
	 * @param values
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String parseCondition(List<Condition> conditions,
			Map<String, Object> values){
 		StringBuffer c = new StringBuffer();
		if (conditions != null && conditions.size() > 0) {
			c.append(RelateType.AND.toString() + " ( ");
			int count = 0;
			for (int i = 0; i < conditions.size(); i++) {
				count++;
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
					
					c.append(related + propertyName + operation + "concat('%',#{" + propertyName + "_" + count + "},'%')");
					values.put(propertyName + "_" + count, value);
					break;
				case BN:
				case BW:
					c.append(related + propertyName + operation + "concat('',#{" + propertyName + "_" + count + "},'%')");
					values.put(propertyName + "_" + count, value);
					break;
				case EN:
				case EW:
					c.append(related + propertyName + operation + "concat('',#{" + propertyName + "_" + count + "},'')");
					values.put(propertyName + "_" + count, value);
					break;
				case BETWEEN:
					Object[] params = new Object[2];
					if (value instanceof String) {
						String[] array = value.toString().split("#|,");
						params[0] = array[0];
						params[1] = array[1];
					} else {
						params = (Object[]) value;
					}
					c.append(related + propertyName + operation + "#{" + propertyName + "0_" + count + "}" + " AND "
							+ "#{" + propertyName + "1}");
					values.put(propertyName + "0" + "_" + count, params[0]);
					values.put(propertyName + "1" + "_" + count, params[1]);
					break;
				case NI:
				case IN:
					Collection<Object> inListValue = new ArrayList<>();
					if ( value != null){
						Class<?> clazz = value.getClass();
						if (value instanceof String){
							String[] split = ((String) value).split(",");
							for (String sv : split) {
								inListValue.add(sv);
							}
							inListValue.addAll(Arrays.asList(split));
						}else if (clazz.isArray()){
							Object[] array = (Object[])value;
							for (Object ov : array) {
								inListValue.add(ov);
							}
						}else if (value instanceof Collection){
							inListValue = (Collection)value;
						}
					}else{
						inListValue.add("NULL");
					}
					String inClause = "<foreach item=\"item" + count + "\" collection=\"" + propertyName + "_" + count + "\" separator=\",\" open=\"(\" close=\")\">"
						+ "#{item" + count + "}</foreach>";
					c.append(related + propertyName + operation + inClause);
					values.put(propertyName + "_" + count, inListValue);
					break;
				case EQ:
				case GE:
				case GT:
				case LE:
				case LT:
				case NE:
					c.append(related + propertyName + operation + "#{" + propertyName + "_" + count + "}");
					values.put(propertyName + "_" + count, value);
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
	
	public static String parseOrder(List<Order> orders) {
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
	
}
