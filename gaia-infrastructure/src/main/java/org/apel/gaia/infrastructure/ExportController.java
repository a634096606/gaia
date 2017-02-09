package org.apel.gaia.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apel.gaia.commons.jqgrid.QueryParams;
import org.apel.gaia.commons.pager.Condition;
import org.apel.gaia.commons.pager.Operation;
import org.apel.gaia.commons.pager.PageBean;
import org.apel.gaia.infrastructure.domain.ExportParam;
import org.apel.gaia.infrastructure.domain.ExportType;
import org.apel.gaia.util.jqgrid.JqGridUtil;
import org.apel.poseidon.api.domain.OrganizationInfo;
import org.apel.poseidon.security.commons.util.UserDetailsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/export")
public class ExportController {

	@Autowired
	private ExportService exportService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/jqgrid", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<byte[]> 
		exportExcel(QueryParams queryParams, String fileName, String sqlTemplate, 
				String colNameParam, String fieldsParam, String ids, String prefix,
				String authorityField,String exportTypes) throws Exception{
		sqlTemplate = sqlTemplate.replaceAll("-", "=");//替换破折号为等于号，自定义http参数转义
		prefix = prefix == null ? "" : prefix + ".";
		PageBean pageBean = JqGridUtil.getPageBean(queryParams);
		
		//导出数据权限
		if(StringUtils.isNotBlank(authorityField)){
			List<OrganizationInfo> organizationInfo = UserDetailsUtil.getCurrentUser().getOrgs();
			Set<String> orgIds = new HashSet<String>();
			for (OrganizationInfo org : organizationInfo) {
				orgIds.add(org.getId());
			}
			if(orgIds.size() > 0){
				pageBean.addCondition(new Condition(authorityField,orgIds,Operation.IN));	
			}
		}
		
		//导出类型处理
		Map<String,ExportType> specialFields = new HashMap<String,ExportType>();
		if(StringUtils.isNotBlank(exportTypes)){
			ObjectMapper objectMapper = new ObjectMapper();
			List<Map<String, Object>>  list = objectMapper.readValue(exportTypes, ArrayList.class); 
			for (Map<String, Object> obj : list) {
				ExportType exportType = new ExportType();
				exportType.setField(obj.get("field").toString());
				exportType.setType(obj.get("type").toString());
				exportType.setValue(obj.get("value"));
				
				String field = exportType.getField();
				if(exportType.getField().contains(".")){
					field = field.substring(field.lastIndexOf(".") + 1);
				}
				specialFields.put(field, exportType);
			}
		}
		
		pageBean.setPropPrefix(prefix);
		ExportParam exportParam = 
				ExportParam.buildExportParam(sqlTemplate, fileName, ids, 
						colNameParam, fieldsParam, pageBean, prefix);
		exportParam.setSpecialFields(specialFields);
		return exportService.exportListExcel(exportParam);
	}
}
