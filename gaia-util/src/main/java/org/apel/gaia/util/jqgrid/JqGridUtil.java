package org.apel.gaia.util.jqgrid;

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
					
					for (int j = 0; j < subRuleItems.size(); j++) {
						RuleItem subRuleItem = subRuleItems.get(j);
						Condition condition = new Condition(RelateType.valueOf(suGroupOp), subRuleItem
								.getField(), subRuleItem.getData(), map
								.get(subRuleItem.getOp().toLowerCase()));
						if((groups.size()==1  || i==groups.size()-1) && j==0){
							condition.setRelateType(null);
						}
						if(j == 0){
							condition.setPrefixBrackets(true);
						}
						if(j == subRuleItems.size()-1){
							condition.setSuffixBrackets(true);
						}
						pageBean.addCondition(condition);
					}
				}
				List<RuleItem> rules = filters.getRules();
				for (int j = 0; j < rules.size(); j++) {
					RuleItem ruleItem = rules.get(j);
					Condition condition = new Condition(rootRelateType, ruleItem
							.getField(), ruleItem.getData(), map
							.get(ruleItem.getOp().toLowerCase()));
					if(j==rules.size()-1){
						condition.setSuffixBrackets(true);
					}
					if(j == 0){
						condition.setPrefixBrackets(true);
					}
					if(groups.size()==0 && j==0){
						condition.setRelateType(null);
					}
					pageBean.addCondition(condition);
				}
			}else{
				List<RuleItem> rules = filters.getRules();
				for (int i = 0; i < rules.size(); i++) {
					RuleItem ruleItem = rules.get(i);
					Condition condition = new Condition(rootRelateType, ruleItem
							.getField(), ruleItem.getData(), map
							.get(ruleItem.getOp().toLowerCase()));
					if(i==0){
						condition.setPrefixBrackets(true);
					}
					if(i==rules.size()-1){
						condition.setSuffixBrackets(true);
					}
					pageBean.addCondition(condition);
				}
			}
		}
		return pageBean;
	}

	
}
