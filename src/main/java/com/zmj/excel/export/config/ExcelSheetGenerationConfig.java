package com.zmj.excel.export.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.List;

/**
 * @author ZMJ
 * @Package com.zmj.excel.export.config
 * @date 2023/10/6 8:56
 * @describe Excel页生成配置
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelSheetGenerationConfig {
    /**
     * 新的sheet
     * */
    private XSSFSheet newSheet;

    /**
     * 输出的sheet的名称
     * */
    private String outputSheetName;

    /**
     * 表格和列表
     * */
    private List<ExcelTableGenerationConfig> tableList;
}
