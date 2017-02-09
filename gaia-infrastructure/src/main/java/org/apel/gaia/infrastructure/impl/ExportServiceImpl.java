package org.apel.gaia.infrastructure.impl;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apel.gaia.commons.pager.Condition;
import org.apel.gaia.commons.pager.Operation;
import org.apel.gaia.commons.pager.Order;
import org.apel.gaia.commons.pager.RelateType;
import org.apel.gaia.infrastructure.ExportService;
import org.apel.gaia.infrastructure.domain.ExportParam;
import org.apel.gaia.infrastructure.domain.ExportType;
import org.apel.gaia.persist.util.IGeneralDao;
import org.apel.gaia.util.DateUtil;
import org.apel.gaia.util.ExportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ExportServiceImpl implements ExportService {
	
	private static Logger LOG = LoggerFactory.getLogger(ExportServiceImpl.class);

	@Autowired
	private IGeneralDao generalDao;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<byte[]> exportListExcel(ExportParam exportParam) {
		String[] computeFields = new String[0];
		String[] cols = new String[0];
		if(!StringUtils.isEmpty(exportParam.getComputefieldsParam())){
			computeFields = exportParam.getComputefieldsParam().split(",");
		}
		if(!StringUtils.isEmpty(exportParam.getColNameParam())){
			cols = exportParam.getColNameParam().split(",");
		}
		String ids = exportParam.getIds();
		List<Condition> conditions = exportParam.getConditions();
		List<Order> orders = exportParam.getOrders();
		//如果jqgrid传递了数据id集合，则需要对条件进行过滤（应用场景通常为用户勾选了列表的checkbox后进行导出）
 		if(!StringUtils.isEmpty(ids)){
			String[] idsArray = ids.split(",");
			Condition condition = new Condition();
			condition.setOperation(Operation.IN);
			condition.setPropertyName(exportParam.getPrefix() + "id");
			condition.setRelateType(RelateType.AND);
			condition.setPropertyValue(idsArray);
			conditions.add(condition);
		}
		String sql = exportParam.getHqlTemplate().replaceAll("\\{fields\\}", exportParam.getFieldsParam());
		//根据条件从数据库查询要导出的数据
		List<?> resultList = generalDao.doList(sql, conditions, orders, true);
		byte[] bytes = null;
		try(Workbook wb = new HSSFWorkbook();ByteArrayOutputStream bos = new ByteArrayOutputStream();){
			Sheet sheet = wb.createSheet(exportParam.getExportFileName());
			//填充excel表头
			CellStyle colTopCellStyle = wb.createCellStyle();
			colTopCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			colTopCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			colTopCellStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			colTopCellStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
			colTopCellStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			colTopCellStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
			Row colRow = sheet.createRow(0);
			for (int i = 0; i < cols.length; i++) {
//				sheet.autoSizeColumn(i);
				sheet.setColumnWidth(i, 6000);
				String colName = cols[i];
				Cell cell = colRow.createCell(i);
				cell.setCellValue(colName);
				cell.setCellStyle(colTopCellStyle);
			}
			//填充excel数据
			Map<String, ExportType> specialFields = exportParam.getSpecialFields();
			for (int i = 0; i < resultList.size(); i++) {
				Object obj = resultList.get(i);
				Row row = sheet.createRow(i + 1);
				Class<?> clazz = obj.getClass();
				for (int j = 0; j < computeFields.length; j++) {
					sheet.setColumnWidth(j, 6000);
					String field = computeFields[j];
					String methodName = "get" + String.valueOf(field.charAt(0)).toUpperCase() + field.substring(1, field.length());
					Cell cell = row.createCell(j);
					Method classMehtod = clazz.getMethod(methodName);
					if(classMehtod != null){
						classMehtod.setAccessible(true);
						if(classMehtod.invoke(obj) instanceof Date){
							String DateType="yyyy-MM-dd";
							if(specialFields.containsKey(field)&&"date".equals(specialFields.get(field).getType())){
								DateType=specialFields.get(field).getValue().toString();
							}
							String dateValue = 
									DateUtil.dateToStr(((Date)classMehtod.invoke(obj)),DateType );
							cell.setCellValue(dateValue);
						}else{
							if(classMehtod.invoke(obj) != null){
								if(specialFields.containsKey(field)){
									Object special = specialFields.get(field).getValue();
									String specialType = specialFields.get(field).getType();
									if("boolean".equals(specialType)){
										Map<String,String> map = (Map<String,String>)special;
										String val = map.get(classMehtod.invoke(obj).toString());
										cell.setCellValue(val);
									}
								}else{
									cell.setCellValue(classMehtod.invoke(obj).toString());
								}
							}
						}
					}
				}
			}
			wb.write(bos);
			bytes = bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("填充excel数据异常");
			throw new RuntimeException(e);
		}
		return ExportUtil.getResponseEntityByFile(bytes, exportParam.getExportFileName());
	}
	

}
