package com.zmj.excel.export.utils;

import com.zmj.excel.export.config.*;
import com.zmj.excel.export.data.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ZMJ
 * @Package com.zmj.excel.export.utils
 * @date 2023/10/7 20:13
 */

@Log4j2
public class ExcelGenerationUtil {
    private ExcelGenerationUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 变量匹配模式
     */
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);



    /**
     * 使用模版输出Excel文件到输出流
     * @param excelFileData
     * @param templateFileInputStream
     * @return void
     */
    public static ExcelWorkBookGenerationConfig generateExcel(ExcelFileData excelFileData,
                                                              InputStream templateFileInputStream) {
        try (InputStream fis = templateFileInputStream) {
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            //转换ExcelFileData 为 ExcelWorkBookGenerationConfig
            ExcelWorkBookGenerationConfig config = buildWorkBookConfig(excelFileData,wb);
            //根据Workbook配置生成填充Workbook
            //删除模版的Sheet
            List<XSSFSheet> templateSheetList = config.getTempelateSheetList();
            for(XSSFSheet xssfSheet: templateSheetList){
                wb.removeSheetAt(wb.getSheetIndex(xssfSheet));
            }
            //处理每个Sheet的生成
            List<ExcelSheetGenerationConfig> sheetList = config.getSheetList();
            for(ExcelSheetGenerationConfig sheetConfig:sheetList){
                List<ExcelTableGenerationConfig> tableList = sheetConfig.getTableList();
                XSSFSheet newSheet = sheetConfig.getNewSheet();
                //设置Sheet的名称
                wb.setSheetName(wb.getSheetIndex(newSheet),sheetConfig.getOutputSheetName());
                //填充变量的数据Map
                fillVariableDataToSheet(config.getVariableMap(),newSheet);

                if(CollectionUtils.isNotEmpty(tableList)){
                    for(ExcelTableGenerationConfig tableConfig:tableList){
                        //填充表格数据
                        fillTableDataToSheet(tableConfig,newSheet);
                    }
                }
            }
            return config;
        } catch (IOException e) {
            log.error("根据Excel模版文件生成新的Excel文件失败",e);
        }
        return null;
    }

    /**
     * 填充变量的值到Sheet
     * @param variableMap
     * @param sheet
     * @return void
     */
    private static void fillVariableDataToSheet(Map<String, ExcelCellGenerationConfig> variableMap,XSSFSheet sheet) {
        if(MapUtils.isEmpty(variableMap)) {
            return ;
        }
        int lastRowNo = sheet.getLastRowNum();
        for(int i= 0;i<=lastRowNo;i++ ) {
            XSSFRow row = sheet.getRow(i);
            if(row==null) {
                continue;
            }
            final short lastCellNum = row.getLastCellNum();
            for(int j=0;j<lastCellNum;j++){
                XSSFCell cell = row.getCell(j);
                if(cell==null) {
                    continue;
                }
                if(!cell.getCellType().equals(CellType.STRING)) {
                    continue;
                }
                String cellValue = StringUtils.trimToEmpty(cell.getStringCellValue());
                if(StringUtils.isBlank(cellValue)) {
                    continue;
                }
                Matcher matcher = matcher(cellValue);
                //替换文本域内容
                if (matcher.find()) {
                    ExcelCellGenerationConfig cellGenerationConfig = null;
                    do {
                        String key = matcher.group(1);
                        cellGenerationConfig = variableMap.get(key);
                        String value = String.valueOf(
                                Optional.ofNullable(cellGenerationConfig)
                                        .map(ExcelCellGenerationConfig::getValue)
                                        .orElse("")
                        );
                        cellValue = matcher.replaceFirst(value);
                    }
                    while ((matcher = matcher(cellValue)).find()) ;

                    cell.setCellValue(cellValue);
                    if(cellGenerationConfig!=null){
                        cellGenerationConfig.getFormatter().formatter(cell);
                    }
                }
            }
        }
    }

    /**
     * 填充表格数据到Sheet
     * @param tableConfig
     * @param newSheet
     * @return void
     */
    private static void fillTableDataToSheet(ExcelTableGenerationConfig tableConfig, XSSFSheet newSheet) {
        List<ExcelRowGenerationConfig> rowList = tableConfig.getRowList();
        if(CollectionUtils.isEmpty(rowList)){
            return;
        }
        int beginRowNo = tableConfig.getBeginRowNo();
        //插入空行，移动的行数为当前待插入的行数-1，因为第一行已经准备好了空白行
        ExcelUtil.insertRowsAfterRow(newSheet,beginRowNo,rowList.size()-1,beginRowNo);
        //使用ShiftRows 方法，移动的行数为当前待插入的行数-1，因为第一行已经准备好了空白行
//        ExcelUtil.insertRowsAfterRowWithShiftRows(newSheet,beginRowNo,rowList.size()-1,beginRowNo);
        int currentRowNo = beginRowNo;
        for(ExcelRowGenerationConfig rowConfig: rowList){
            XSSFRow currentRow = newSheet.getRow(currentRowNo);
            if (currentRow == null){
                currentRow = newSheet.createRow(currentRowNo);
            }
            fillRowDataToSheet(rowConfig,currentRow);
            currentRowNo++;
        }

    }

    /**
     * 填充行的数据到Sheet
     * @param rowConfig
     * @param row
     * @return void
     */
    private static void fillRowDataToSheet(ExcelRowGenerationConfig rowConfig, XSSFRow row){
        List<ExcelCellGenerationConfig> cellList = rowConfig.getCellList();
        if(CollectionUtils.isEmpty(cellList)){
            return;
        }
        for(int x = 0;x<cellList.size();x++ ) {
            ExcelCellGenerationConfig cellConfig = cellList.get(x);
            Object value = cellConfig.getValue();
            if (value == null) {
                continue;
            }
            XSSFCell cell = row.getCell(x);
            if(cell==null) {
                cell = row.createCell(x);
            }
            fillCellDataToSheet(cellConfig,cell);
        }
    }

    /**
     * 填充单元格数据到Sheet
     * @param cellConfig
     * @param cell
     * @return void
     */
    private static void fillCellDataToSheet(ExcelCellGenerationConfig cellConfig,XSSFCell cell){
        cellConfig.getSetValueFun().accept(cell,cellConfig.getValue());
        cellConfig.getFormatter().formatter(cell);
    }




    /**
     * 正则匹配字符串
     * @author 徐明龙 XuMingLong 2023-03-01
     * @param str
     * @return java.util.regex.Matcher
     */
    private static Matcher matcher(String str) {
        return VARIABLE_PATTERN.matcher(str);
    }


    /**
     * 构建WorkBook配置
     * @param excelFileData
     * @param workbook
     * @return com.feiyizhan.excel.export.utils.config.ExcelWorkBookGenerationConfig
     */
    private static ExcelWorkBookGenerationConfig buildWorkBookConfig(ExcelFileData excelFileData,
                                                                     XSSFWorkbook workbook){
        ExcelWorkBookGenerationConfig workBookGenerationConfig = new ExcelWorkBookGenerationConfig();
        //设置输出的名称
        workBookGenerationConfig.setOutputFileName(excelFileData.getFileName());
        //填充WorkBook的数据
        fillWorkBookData(excelFileData,workBookGenerationConfig,workbook);
        return workBookGenerationConfig;
    }

    /**
     * 填充WorkBook的数据
     * @param excelFileData
     * @param workBookGenerationConfig
     * @param workbook
     * @return void
     */
    private static void fillWorkBookData(ExcelFileData excelFileData,
                                         ExcelWorkBookGenerationConfig workBookGenerationConfig,XSSFWorkbook workbook){
        workBookGenerationConfig.setWorkbook(workbook);
        workBookGenerationConfig.setOutputFileName(excelFileData.getFileName());

        List<XSSFSheet> templateSheetList = new ArrayList<>();
        List<ExcelSheetGenerationConfig> sheetList = new ArrayList<>();


        workBookGenerationConfig.setTempelateSheetList(templateSheetList);
        workBookGenerationConfig.setSheetList(sheetList);

        List<ExcelSheetData> sheetDataList = excelFileData.getSheetDataList();
        if(CollectionUtils.isEmpty(sheetDataList)){
            return ;
        }
        Set<Integer> templateSheetIndexSet = new HashSet<>();
        for(ExcelSheetData sheetData: sheetDataList){
            XSSFSheet templateSheet = workbook.getSheetAt(sheetData.getTemplateSheetIndex());
            if(templateSheetIndexSet.add(sheetData.getTemplateSheetIndex())){
                templateSheetList.add(templateSheet);
            }
            //空表格不生成新的Sheet
            if(sheetData.isEmptied()){
                continue;
            }
            //生成Sheet数据
            ExcelSheetGenerationConfig sheetConfig = new ExcelSheetGenerationConfig();
            XSSFSheet newSheet = workbook.cloneSheet(workbook.getSheetIndex(templateSheet));
            sheetConfig.setNewSheet(newSheet);
            if(StringUtils.isBlank(sheetData.getOutputSheetName())){
                sheetConfig.setOutputSheetName(templateSheet.getSheetName());
            }else{
                sheetConfig.setOutputSheetName(sheetData.getOutputSheetName());
            }
            fillSheetData(sheetData, sheetConfig);
            sheetList.add(sheetConfig);
        }

        //生成全局变量Map
        workBookGenerationConfig.setVariableMap(buildVariableConfigMap(excelFileData.getVariableMap()));

    }

    /**
     * 填充Sheet的生成配置
     * @param sheetData
     * @param sheetConfig
     * @return void
     */
    private static void fillSheetData(ExcelSheetData sheetData, ExcelSheetGenerationConfig sheetConfig) {
        //生成表格数据
        List<ExcelTableGenerationConfig> tableGenerationConfigList = new ArrayList<>();
        sheetConfig.setTableList(tableGenerationConfigList);
        List<ExcelTableData> tableDataList = sheetData.getTableDataList();
        if(CollectionUtils.isEmpty(tableDataList)){
            return;
        }
        for(ExcelTableData tableData:tableDataList){
            ExcelTableGenerationConfig tableGenerationConfig = new ExcelTableGenerationConfig();
            tableGenerationConfigList.add(tableGenerationConfig);
            tableGenerationConfig.setBeginRowNo(tableData.getBeginRowNo());
            fillTableData(tableData,tableGenerationConfig);

        }
    }

    /**
     * 填充表格的生成配置
     * @param tableData
     * @param tableGenerationConfig
     * @return void
     */
    private static void fillTableData(ExcelTableData tableData, ExcelTableGenerationConfig tableGenerationConfig) {
        //生成行的数据
        List<ExcelRowGenerationConfig> rowGenerationConfigList = new ArrayList<>();
        tableGenerationConfig.setRowList(rowGenerationConfigList);
        List<ExcelRowData> rowDataList = tableData.getRowDataList();
        if(CollectionUtils.isEmpty(rowDataList)){
            return;
        }
        for(ExcelRowData rowData:rowDataList){
            ExcelRowGenerationConfig rowGenerationConfig = new ExcelRowGenerationConfig();
            rowGenerationConfigList.add(rowGenerationConfig);
            fillRowData(rowData,rowGenerationConfig);
        }
    }

    /**
     * 填充行的生成配置
     * @param rowData
     * @param rowGenerationConfig
     * @return void
     */
    private static void fillRowData(ExcelRowData rowData, ExcelRowGenerationConfig rowGenerationConfig) {
        List<ExcelCellData> cellDataList = rowData.getCellDataList();
        if(CollectionUtils.isEmpty(cellDataList)){
            return;
        }
        List<ExcelCellGenerationConfig> cellList  = new ArrayList<>();
        rowGenerationConfig.setCellList(cellList);
        //生成单元格数据
        for(ExcelCellData cellData: cellDataList){
            cellList.add(buildCellGenerationConfig(cellData));
        }

    }

    /**
     * 构建单元格生成配置
     * @param cellData
     * @return com.feiyizhan.excel.export.utils.config.ExcelCellGenerationConfig
     */
    private static ExcelCellGenerationConfig buildCellGenerationConfig(ExcelCellData cellData){
        ExcelCellGenerationConfig cellGenerationConfig = new ExcelCellGenerationConfig();
        cellGenerationConfig.setValue(cellData.getValue());
        ExcelCellCustomData celCustomData = cellData.getCellCustomData();
        if(celCustomData!=null){
            //设置填充值的方法
            cellGenerationConfig.setSetValueFun(celCustomData.getSetValueFun());
            //设置格式化处理器
            cellGenerationConfig.setFormatter(celCustomData.getCellFormatter());
        }else{
            //设置填充值的方法
            cellGenerationConfig.setSetValueFun(ExcelCellCustomData.DEFAULT_SAT_VALUE_FUN);
            //设置格式化处理器
            cellGenerationConfig.setFormatter(ExcelCellFormatter.DEFAULT_FORMATTER);
        }
        return cellGenerationConfig;
    }

    /**
     * 构建变量配置的Map
     * @param variableMap
     * @return java.util.Map<java.lang.String,com.feiyizh
            * an.excel.export.utils.config.ExcelCellGenerationConfig>
     */
    private static  Map<String, ExcelCellGenerationConfig> buildVariableConfigMap(
            Map<String, ExcelCellData> variableMap){
        Map<String, ExcelCellGenerationConfig> configMap = new HashMap<>(variableMap.size());
        variableMap.forEach((k,v)->{
            configMap.put(k,buildCellGenerationConfig(v));
        });
        return configMap;
    }

}
