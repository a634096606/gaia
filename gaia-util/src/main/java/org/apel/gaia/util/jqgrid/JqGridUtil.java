package org.apel.gaia.util.jqgrid;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apel.gaia.commons.jqgrid.Filters;
import org.apel.gaia.commons.jqgrid.GroupItem;
import org.apel.gaia.commons.jqgrid.QueryParams;
import org.apel.gaia.commons.jqgrid.RuleItem;
import org.apel.gaia.commons.pager.Condition;
import org.apel.gaia.commons.pager.Operation;
import org.apel.gaia.commons.pager.Order;
import org.apel.gaia.commons.pager.OrderType;
import org.apel.gaia.commons.pager.PageBean;
import org.apel.gaia.commons.pager.RelateType;
import org.apel.gaia.util.DateUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JqGridUtil {
	public static ObjectMapper objectMapper = new ObjectMapper();
	public static Map<String, Operation> map = new HashMap<String, Operation>();
	static {
		map.put("bw", Operation.BW);
		map.put("eq", Operation.EQ);
		map.put("ne", Operation.NE);
		map.put("bn", Operation.BN);
		map.put("ew", Operation.EW);
		map.put("en", Operation.EN);
		map.put("cn", Operation.CN);
		map.put("nc", Operation.NC);
		map.put("nu", Operation.NU);
		map.put("nn", Operation.NN);
		map.put("in", Operation.IN);
		map.put("ni", Operation.NI);
		map.put("le", Operation.LE);
		map.put("lt", Operation.LT);
		map.put("ge", Operation.GE);
		map.put("gt", Operation.GT);
		map.put("bt", Operation.BETWEEN);
	}

	public static PageBean getPageBean(QueryParams queryParams){
		PageBean pageBean = new PageBean();
		pageBean.setCurrentPage(queryParams.getPage()).setRowsPerPage(queryParams.getRows());
		String searchField = queryParams.getSearchField();
		String SearchString = queryParams.getSearchString();
		Operation operation = map.get(queryParams.getSearchOper());
		String sidx = queryParams.getSidx();
		String sord = queryParams.getSord();
		if (!StringUtils.isEmpty(searchField)) {
			pageBean.addCondition(new Condition(searchField, SearchString,
					operation));
		}
		if (!StringUtils.isEmpty(sidx)) {
			pageBean.addOrder(new Order(sidx, OrderType.valueOf(sord
					.toUpperCase())));
		}
		String content = queryParams.getFilters();
		if (!StringUtils.isEmpty(content)) {
			Filters filters;
			try {
				filters = objectMapper.readValue(content, Filters.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} 
			convertPropertyDataWithDate(filters);
			//解析最外层条件
			String groupOp = filters.getGroupOp();
			RelateType rootRelateType = null;
			if(!StringUtils.isEmpty(groupOp)){
				rootRelateType =  RelateType
						.valueOf(groupOp.toUpperCase());
			}
			
			//解析分组查询条件
			List<GroupItem> groups = filters.getGroups();
			if(groups!=null && groups.size()>0){
				for (int i = 0; i < groups.size(); i++) {
					GroupItem groupItem = groups.get(i);
					String suGroupOp = groupItem.getGroupOp().toUpperCase();
					List<RuleItem> subRuleItems = groupItem.getRules();
					if(!CollectionUtils.isEmpty(subRuleItems)){
						for (int j = 0; j < subRuleItems.size(); j++) {
							RuleItem subRuleItem = subRuleItems.get(j);
							Condition condition = new Condition(RelateType.valueOf(suGroupOp), subRuleItem
									.getField(), subRuleItem.getData(), map
									.get(subRuleItem.getOp().toLowerCase()));
							if((groups.size()==1  || i==groups.size()-1) && j==0){
								condition.setRelateType(null);
							}else if(!StringUtils.isEmpty(subRuleItem.getGroupOp())){
								condition.setRelateType(RelateType.valueOf(subRuleItem.getGroupOp()));
							}
							if(j == 0){
								condition.setPrefixBrackets(true);
								condition.setPreffixBracketsValue(condition.getPreffixBracketsValue() + "(");
							}
							if(subRuleItem.isPrefixBrackets()){
								condition.setPrefixBrackets(true);
								condition.setPreffixBracketsValue(condition.getPreffixBracketsValue() + "(");
							}
							if(j == subRuleItems.size()-1){
								condition.setSuffixBrackets(true);
								condition.setSuffixBracketsValue(condition.getSuffixBracketsValue() + ")");
							}
							if(subRuleItem.isSuffixBrackets()){
								condition.setSuffixBrackets(true);
								condition.setSuffixBracketsValue(condition.getSuffixBracketsValue() + ")");
							}
							pageBean.addCondition(condition);
						}
					}
				}
				List<RuleItem> rules = filters.getRules();
				if(!CollectionUtils.isEmpty(rules)){
					for (int j = 0; j < rules.size(); j++) {
						RuleItem ruleItem = rules.get(j);
						Condition condition = new Condition(rootRelateType, ruleItem
								.getField(), ruleItem.getData(), map
								.get(ruleItem.getOp().toLowerCase()));
						if((j==rules.size()-1)){
							condition.setSuffixBrackets(true);
							condition.setSuffixBracketsValue(condition.getSuffixBracketsValue() + ")");
						}
						if(ruleItem.isSuffixBrackets()){
							condition.setSuffixBrackets(true);
							condition.setSuffixBracketsValue(condition.getSuffixBracketsValue() + ")");
						}
						if(j == 0){
							condition.setPrefixBrackets(true);
							condition.setPreffixBracketsValue(condition.getPreffixBracketsValue() + "(");
						}
						if(ruleItem.isPrefixBrackets()){
							condition.setPrefixBrackets(true);
							condition.setPreffixBracketsValue(condition.getPreffixBracketsValue() + "(");
						}
						if(groups.size()==0 && j==0){
							condition.setRelateType(null);
						}else if(!StringUtils.isEmpty(ruleItem.getGroupOp())){
							condition.setRelateType(RelateType.valueOf(ruleItem.getGroupOp()));
						}
						pageBean.addCondition(condition);
					}
				}
			}else{
				List<RuleItem> rules = filters.getRules();
				if(!CollectionUtils.isEmpty(rules)){
					for (int i = 0; i < rules.size(); i++) {
						RuleItem ruleItem = rules.get(i);
						Condition condition = new Condition(rootRelateType, ruleItem
								.getField(), ruleItem.getData(), map
								.get(ruleItem.getOp().toLowerCase()));
						if(i==0){
							condition.setPrefixBrackets(true);
							condition.setPreffixBracketsValue(condition.getPreffixBracketsValue() + "(");
						}
						if(ruleItem.isPrefixBrackets()){
							condition.setPrefixBrackets(true);
							condition.setPreffixBracketsValue(condition.getPreffixBracketsValue() + "(");
						}
						if((i==rules.size()-1)){
							condition.setSuffixBrackets(true);
							condition.setSuffixBracketsValue(condition.getSuffixBracketsValue() + ")");
						}
						if(ruleItem.isSuffixBrackets()){
							condition.setSuffixBrackets(true);
							condition.setSuffixBracketsValue(condition.getSuffixBracketsValue() + ")");
						}
						if(!StringUtils.isEmpty(ruleItem.getGroupOp())){
							condition.setRelateType(RelateType.valueOf(ruleItem.getGroupOp()));
						}
						pageBean.addCondition(condition);
					}
				}
			}
		}
		return pageBean;
	}
	
	private static void convertPropertyDataWithDate(Filters filters){
		if(!CollectionUtils.isEmpty(filters.getRules())){
			convertRuleDataWithDate(filters.getRules());
		}
		if(!CollectionUtils.isEmpty(filters.getGroups())){
			for (GroupItem groupItem : filters.getGroups()) {
				convertRuleDataWithDate(groupItem.getRules());
			}
		}
	}
	
	private static void convertRuleDataWithDate(List<RuleItem> subRuleItems){
		List<RuleItem> newRuleItems = new ArrayList<>();
		for (int i = 0; i < subRuleItems.size(); i++) {
			RuleItem subRuleItem = subRuleItems.get(i);
			if("date".equals(subRuleItem.getCusType())){
				Object data = subRuleItem.getData();
				if(StringUtils.isEmpty(data)){
					newRuleItems.add(subRuleItem);
				}else{
					if(subRuleItem.getOp().equals("eq")){
						Date originalDateData = null;
						try {
							originalDateData = DateUtil.strToDate(data.toString(), "yyyy-MM-dd");
						} catch (ParseException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
						Calendar c = Calendar.getInstance();
						c.setTime(originalDateData);
						c.add(Calendar.DAY_OF_MONTH, 1);
						Date nextDay = c.getTime();
						subRuleItem.setData(originalDateData);
						subRuleItem.setCusType("date");
						subRuleItem.setOp("ge");
						subRuleItem.setPrefixBrackets(true);
						newRuleItems.add(subRuleItem);
						//新增下一天
						RuleItem nextDayRule = new RuleItem();
						nextDayRule.setOp("lt");
						nextDayRule.setGroupOp("AND");
						nextDayRule.setData(nextDay);
						nextDayRule.setField(subRuleItem.getField());
						nextDayRule.setCusType("date");
						nextDayRule.setSuffixBrackets(true);
						newRuleItems.add(nextDayRule);
					}else if(subRuleItem.getOp().equals("ne")){
						Date originalDateData = null;
						try {
							originalDateData = DateUtil.strToDate(data.toString(), "yyyy-MM-dd");
						} catch (ParseException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
						Calendar c = Calendar.getInstance();
						c.setTime(originalDateData);
						c.add(Calendar.DAY_OF_MONTH, 1);
						Date nextDay = c.getTime();
						subRuleItem.setData(originalDateData);
						subRuleItem.setCusType("date");
						subRuleItem.setOp("lt");
						subRuleItem.setPrefixBrackets(true);
						newRuleItems.add(subRuleItem);
						//新增下一天
						RuleItem nextDayRule = new RuleItem();
						nextDayRule.setOp("ge");
						nextDayRule.setGroupOp("OR");
						nextDayRule.setData(nextDay);
						nextDayRule.setField(subRuleItem.getField());
						nextDayRule.setCusType("date");
						nextDayRule.setSuffixBrackets(true);
						newRuleItems.add(nextDayRule);
					}else{
						//把所有日期字段的值进行转换
						Date originalDateData = null;
						try {
							originalDateData = DateUtil.strToDate(data.toString(), "yyyy-MM-dd");
						} catch (ParseException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
						subRuleItem.setData(originalDateData);
						newRuleItems.add(subRuleItem);
					}
				}
			}else{
				newRuleItems.add(subRuleItem);
			}
		}
		subRuleItems.clear();
		subRuleItems.addAll(newRuleItems);
	}	
	
}
