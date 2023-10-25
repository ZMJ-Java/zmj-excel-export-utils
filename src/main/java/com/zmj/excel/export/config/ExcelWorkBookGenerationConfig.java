package com.zmj.excel.export.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

/**
 * @author ZMJ
 * @Package com.zmj.excel.export.config
 * @date 2023/10/6 9:05
 * @describe excel文件生成配置
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelWorkBookGenerationConfig {
    /**
     * Excel文件
     * */
    private XSSFWorkbook workbook;

    /**
     * 模板的Sheet的列表
     * */
    private List<XSSFSheet> tempelateSheetList;

    /**
     * 输出的文件名
     * */
    private String outputFileName;

    /**
     * Sheet列表
     * */
    private List<ExcelSheetGenerationConfig> sheetList;

    /**
     * 变量数据
     * */
    private Map<String,ExcelCellGenerationConfig> variableMap;

}
