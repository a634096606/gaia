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

/**
 * jqgrid组件解析类，通过jqgrid前台传递的默认参数
 * <p>
 * <b>以下为jqgrid传递的查询参数样板</b>
 * <pre>
 * 	postData: {
 * 	  filters: {
 * 	    groupOp: "OR"
 * 		rules: [
 * 			{field: "name", op: "eq", data: "alex", groupOp: "AND"},
			{field: "name", op: "eq", data: "alex", groupOp: "AND"}
 * 		],
 * 		groups: [
 * 					{
 * 						groupOp: "AND", 
 * 						rules:[
 * 							{field: "birthday", op: "gt", data: "2015-01-11", cusType: "date", prefixBrackets:true, groupOp: "AND"},
 * 							{field: "birthday", op: "lt", data: "2015-01-21", cusType: "date", suffixBrackets: true, groupOp: "AND"},
 * 						]
 * 					},
 * 					{
 * 						groupOp: "OR", 
 * 						rules:[
 * 							{field: "age", op: "ge", data: 10,  prefixBrackets:true, groupOp: "AND"},
 * 							{field: "age", op: "le", data: 30,  suffixBrackets: true, groupOp: "AND"},
 * 						]
 * 					}
 * 			]
 *    }  	
 *  }
 * </pre>
 * 翻译之后为:  
 * <p>where name = alex and ((birthday > date'2015-01-11' and birthday < date'2015-01-21') or (age > 10 and age < 30) )
 * <p>
 * <b>规则</b>
 * <p>当内层rule任何一个都没有op时，外层op为and;当内部rule有op时，拥有自定义op的rule会覆盖外层的op，没有自定义op时，外层op作为rule的op</p>
 * @author lijian
 *
 */
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

	/**
	 * 解析jqgrid前台传递的参数，封装成Condition条件对象，然后放入到pageBean对象当中
	 * 
	 * @param queryParams jqgrid参数对象
	 */
	public static PageBean getPageBean(QueryParams queryParams){
		PageBean pageBean = new PageBean();
		//设置当前页
		pageBean.setCurrentPage(queryParams.getPage()).setRowsPerPage(queryParams.getRows());
		//最外层的查询条件解析
		String searchField = queryParams.getSearchField();
		String SearchString = queryParams.getSearchString();
		Operation operation = map.get(queryParams.getSearchOper());
		String sidx = queryParams.getSidx();
		String sord = queryParams.getSord();
		if (!StringUtils.isEmpty(searchField)) {
			//获取最外层的条件，创建Condition并放入到PageBean中
			pageBean.addCondition(new Condition(searchField, SearchString,
					operation));
		}
		//解析排序字段
		if (!StringUtils.isEmpty(sidx)) {
			pageBean.addOrder(new Order(sidx, OrderType.valueOf(sord
					.toUpperCase())));
		}
		//解析多条件DSL语句(详见模板)
		String content = queryParams.getFilters();
		if (!StringUtils.isEmpty(content)) {
			Filters filters;
			try {
				//将filtrers json字符串转化为filters对象
				filters = objectMapper.readValue(content, Filters.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} 
			//对filter当中的日期字段进行处理，如果发现是日期字段，需要把当前的rule条件中的data转换成日期格式
			//另外：当rule条件中为eq或ne的时候，将原有的条件拆分成两个条件，以达到eq和ne的作用，此任务的目的源于
			//当出现存入数据库字段为yyyy-MM-dd HH:mm:ss 但查询的时候是yyyy-MM-dd的时候进行隐式的条件变换
			convertPropertyDataWithDate(filters);
			
			//解析groups最外层条件
			String groupOp = filters.getGroupOp();
			RelateType rootRelateType = null;
			if(!StringUtils.isEmpty(groupOp)){
				rootRelateType =  RelateType
						.valueOf(groupOp.toUpperCase());
			}
			
			//解析分组查询条件
			List<GroupItem> groups = filters.getGroups();
			//需要为分组条件中每一个group组中的的首尾加上括号，并且也会根据rule本身是否加上括号进行二度添加括号操作
			if(groups != null && groups.size() > 0){//当前存在groups条件的时候：
				for (int i = 0; i < groups.size(); i++) {
					GroupItem groupItem = groups.get(i);
					//获取一个group中的统一op，当rule本身没有op时，会以这个统一op为准(前提是所有内部rule都没有op)
					String suGroupOp = groupItem.getGroupOp().toUpperCase();
					RelateType suGroupOpEnum = RelateType.valueOf(suGroupOp);
					List<RuleItem> subRuleItems = groupItem.getRules();
					buildCondition(pageBean, subRuleItems, suGroupOpEnum);
				}
			}
			//解析当前filters中的rules条件(与groups并行的条件)
			List<RuleItem> rules = filters.getRules();
			buildCondition(pageBean, rules, rootRelateType);
		}
		return pageBean;
	}
	
	//根据rule构造condition
	private static void buildCondition(PageBean pageBean, List<RuleItem> rules, RelateType outterRelateType){
		if(!CollectionUtils.isEmpty(rules)){
			//记录第一个条件
			boolean flag=false;
			Condition firstCondition=null;
			//循环所有rules，与groups中的rule操作一致
			for (int i = 0; i < rules.size(); i++) {
				RuleItem ruleItem = rules.get(i);
				//根据rule本身的数据创建condition对象
				Condition condition = new Condition(outterRelateType, ruleItem
						.getField(), ruleItem.getData(), map
						.get(ruleItem.getOp().toLowerCase()));
				if(i==0){//当为第一个rule时，需要添加前括号
					firstCondition=condition;
					condition.setPrefixBrackets(true);
					condition.setPreffixBracketsValue(condition.getPreffixBracketsValue() + "(");
				}
				if((i==rules.size()-1)){//当为最后一个rule时，需要添加后括号
					condition.setSuffixBrackets(true);
					condition.setSuffixBracketsValue(condition.getSuffixBracketsValue() + ")");
				}
				if(ruleItem.isPrefixBrackets()){//当此rule为明确指定添加前括号时，需要添加前括号
					condition.setPrefixBrackets(true);
					condition.setPreffixBracketsValue(condition.getPreffixBracketsValue() + "(");
				}
				if(ruleItem.isSuffixBrackets()){//当此rule为明确指定添加后括号时，需要添加后括号
					condition.setSuffixBrackets(true);
					condition.setSuffixBracketsValue(condition.getSuffixBracketsValue() + ")");
				}
				//当内部rule有op的时候，把当前的condition条件设置为内部op的值 
				if(!StringUtils.isEmpty(ruleItem.getGroupOp())){
					flag = true;
					condition.setRelateType(RelateType.valueOf(ruleItem.getGroupOp()));
				}
				pageBean.addCondition(condition);
			}
			if(!flag){
				if(firstCondition!=null){//当所有rule都没有自定义的op时，将最外层(第一个rule对应的)的condition op设置为and(null会被持久层组件解析为and)
					//当前所有rules没有op时，外层用默认and衔接
					firstCondition.setRelateType(null);
				}
			}
		}
	}
	
	
	//处理filters中的rules和groups 转换data成为日期类型
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
	
	//处理ruleItem中的关于日期类型的数据，需要配合前台的jqgrid自定义dateSearchabaleDeal.js插件进行使用
	//将插件注册到jqgrid的multableSearch中的OnSearch事件中(以此支持jqgrid原生的多条件检索)
	private static void convertRuleDataWithDate(List<RuleItem> subRuleItems){
		List<RuleItem> newRuleItems = new ArrayList<>();
		//循环rules，对所有date类型的字段进行处理
		for (int i = 0; i < subRuleItems.size(); i++) {
			RuleItem subRuleItem = subRuleItems.get(i);
			if("date".equals(subRuleItem.getCusType())){//如果是date字段，那将其转换成统一的yyyy-MM-dd格式的日期字段
				Object data = subRuleItem.getData();
				if(StringUtils.isEmpty(data)){
					newRuleItems.add(subRuleItem);
				}else{//如果是eq字段则将原有的一个日期条件拆解成两个日期条件，统一变为>=当天 and <下一天的格式
					if(subRuleItem.getOp().equals("eq")){
						Date originalDateData = null;
						try {
							originalDateData = DateUtil.strToDate(data.toString(), "yyyy-MM-dd");
						} catch (ParseException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
						//拆解等于条件开始
						//首先将等于变成大于当天时间的条件
						subRuleItem.setData(originalDateData);
						subRuleItem.setCusType("date");
						subRuleItem.setOp("ge");
						subRuleItem.setPrefixBrackets(true);
						newRuleItems.add(subRuleItem);
						
						//算出第二天的时间
						Date nextDay = calculateNextDay(originalDateData);
						
						//然后将再添加小于下一天的条件
						RuleItem nextDayRule = new RuleItem(subRuleItem.getField(), "lt", nextDay, "date", false, true, "AND");
						newRuleItems.add(nextDayRule);
					}else if(subRuleItem.getOp().equals("ne")){//如果是ne字段则将原有的一个日期条件拆解成两个日期条件，统一变为<当天  or >=下一天的格式
						Date originalDateData = null;
						try {
							originalDateData = DateUtil.strToDate(data.toString(), "yyyy-MM-dd");
						} catch (ParseException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
						//拆解等于条件开始
						//首先将等于变成小于当天时间的条件
						subRuleItem.setData(originalDateData);
						subRuleItem.setCusType("date");
						subRuleItem.setOp("lt");
						subRuleItem.setPrefixBrackets(true);
						newRuleItems.add(subRuleItem);
						
						Date nextDay = calculateNextDay(originalDateData);
						
						//然后将再添加大于等于下一天的条件
						RuleItem nextDayRule = new RuleItem(subRuleItem.getField(), "ge", nextDay, "date", false, true, "OR");
						newRuleItems.add(nextDayRule);
					}else{//如果仅为日期字段，则只进行日期 类型转换处理
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
			}else{//如果不为日期字段不进行处理
				newRuleItems.add(subRuleItem);
			}
		}
		//替换之前的集合，更换为转换后的集合
		subRuleItems.clear();
		subRuleItems.addAll(newRuleItems);
	}

	//算出下一天的时间
	private static Date calculateNextDay(Date originalDateData) {
		Calendar c = Calendar.getInstance();
		c.setTime(originalDateData);
		c.add(Calendar.DAY_OF_MONTH, 1);
		Date nextDay = c.getTime();
		return nextDay;
	}	
	
}
