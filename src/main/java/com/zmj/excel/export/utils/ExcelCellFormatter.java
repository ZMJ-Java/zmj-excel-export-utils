package com.zmj.excel.export.utils;

import org.apache.poi.xssf.usermodel.XSSFCell;

/**
 * @author ZMJ
 * @Package com.zmj.excel.export.utils
 * @date 2023/10/6 8:47
 * @describe 单元格格式化处理器
 */

@FunctionalInterface
public interface ExcelCellFormatter {

    /**
     * 默认的格式化方法
     */
    ExcelCellFormatter DEFAULT_FORMATTER = cell -> {};


    /**
     * 格式化单元格
     * */
    void formatter(XSSFCell xssfCell);
}
