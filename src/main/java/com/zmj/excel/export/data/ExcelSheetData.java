package com.zmj.excel.export.data;

import lombok.Data;

import java.util.List;

/**
 * @author ZMJ
 * @describe Excel sheet数据对象
 * @Package com.zmj.excel.export.data
 * @date 2023/10/7 9:57
 */
@Data
public class ExcelSheetData {
    /**
     * 使用模板的Sheet索引
     * */
    private int templateSheetIndex;

    /**
     * 输出的sheet名称
     * */
    private String outputSheetName;

    /**
     * 表格列表，多个二维表
     * */
    private List<ExcelTableData> tableDataList;

    /**
     * 是否为空表
     * */
    private boolean emptied;
}
