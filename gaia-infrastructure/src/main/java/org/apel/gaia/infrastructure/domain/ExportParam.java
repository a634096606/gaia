package org.apel.gaia.infrastructure.domain;

import java.util.List;
import java.util.Map;

import org.apel.gaia.commons.pager.Condition;
import org.apel.gaia.commons.pager.Order;
import org.apel.gaia.commons.pager.PageBean;
import org.apel.gaia.infrastructure.consist.ExportConsist;
import org.springframework.util.StringUtils;

/**
 * 
 * 导出参数类，用于封装jqgrid对excel的导出参数封装
 * 
 * @author lijian
 *
 */
public class ExportParam {

	// 导出文件名称
	private String exportFileName;

	// 导出的语句模板
	private String hqlTemplate;

	// 导出的查询条件
	private List<Condition> conditions;

	// 导出的排序条件
	private List<Order> orders;

	// 导出的列表头
	private String colNameParam;

	// 用于sql查询的字段信息
	private String fieldsParam;
	
	// 用于计算的字段信息
	private String computefieldsParam;

	// 导出的数据id集合
	private String ids;
	
	//字段前缀
	private String prefix;
	
	//需特殊处理字段
	private Map<String, ExportType> specialFields;

	public String getExportFileName() {
		return exportFileName;
	}

	public void setExportFileName(String exportFileName) {
		this.exportFileName = exportFileName;
	}

	public String getHqlTemplate() {
		return hqlTemplate;
	}

	public void setHqlTemplate(String hqlTemplate) {
		this.hqlTemplate = hqlTemplate;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public String getColNameParam() {
		return colNameParam;
	}

	public void setColNameParam(String colNameParam) {
		this.colNameParam = colNameParam;
	}

	public String getFieldsParam() {
		return fieldsParam;
	}

	public void setFieldsParam(String fieldsParam) {
		this.fieldsParam = fieldsParam;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getComputefieldsParam() {
		return computefieldsParam;
	}

	public void setComputefieldsParam(String computefieldsParam) {
		this.computefieldsParam = computefieldsParam;
	}

	public static ExportParam buildExportParam(String sqlTemplate, String fileName, String ids, String colNameParam, 
			String fieldsParam, PageBean pageBean, String prefix){
		if(StringUtils.isEmpty(sqlTemplate)){
			throw new RuntimeException("没有导出语句，无法生成excel文件");
		}
		if(StringUtils.isEmpty(fileName)){
			fileName = "无名称.xls";
		}
		if(!sqlTemplate.toLowerCase().contains("where")){
			sqlTemplate = sqlTemplate + " where 1=1";
		}
		ExportParam exportParam = new ExportParam();
		exportParam.setColNameParam(colNameParam);
		exportParam.setFieldsParam(dealSqlFields(fieldsParam));
		exportParam.setComputefieldsParam(dealComputeFields(fieldsParam));
		exportParam.setConditions(pageBean.getConditions());
		exportParam.setOrders(pageBean.getOrders());
		exportParam.setHqlTemplate("select " + ExportConsist.FIELDS_TEMPLATE + " " + sqlTemplate);
		exportParam.setIds(ids);
		exportParam.setExportFileName(fileName);
		exportParam.setPrefix(prefix);
		return exportParam;
	}
	
	//对字段前缀名进行处理，变成sql可以查询的字段信息
	private static String dealSqlFields(String fields){
		if(!StringUtils.isEmpty(fields)){
			StringBuffer sb = new StringBuffer();
			String[] fieldsStrArray = fields.split(",");
			for (String singleFieldStr : fieldsStrArray) {
				String[] fieldPair = singleFieldStr.split("=");
				if(fieldPair[0].contains(".")){
					String newSingleFieldStr = fieldPair[0] + " as " + fieldPair[1];
					sb.append(newSingleFieldStr + ",");
				}else{
					sb.append(fieldPair[0] + ",");
				}
			}
			fields = sb.substring(0, sb.length() - 1);
		}
		return fields;
	}
	
	//对字段前缀名进行处理，变成可以计算的字段信息
	private static String dealComputeFields(String fields){
		if(!StringUtils.isEmpty(fields)){
			StringBuffer sb = new StringBuffer();
			String[] fieldsStrArray = fields.split(",");
			for (String singleFieldStr : fieldsStrArray) {
				String[] singleFieldStrPair = singleFieldStr.split("=");
				if(singleFieldStrPair[0].contains(".")){
					String newSingleFieldStr = singleFieldStrPair[0].substring(singleFieldStrPair[0].lastIndexOf(".") + 1);
					sb.append(newSingleFieldStr + ",");
				}else{
					sb.append(singleFieldStrPair[0] + ",");
				}
			}
			fields = sb.substring(0, sb.length() - 1);
		}
		return fields;
	}

	public Map<String, ExportType> getSpecialFields() {
		return specialFields;
	}

	public void setSpecialFields(Map<String, ExportType> specialFields) {
		this.specialFields = specialFields;
	}

}
