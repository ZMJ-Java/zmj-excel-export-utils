package com.zmj.excel.export.data;

import com.zmj.excel.export.utils.ExcelCellFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.context.expression.CachedExpressionEvaluator;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author ZMJ
 * @describe 单元格值类型的数据
 * @Package com.zmj.excel.export.data
 * @date 2023/10/6 14:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelCellValueTypeData implements ExcelCellCustomData {

    /**
     * 数字的设置的方法
     */
    private static final BiConsumer<XSSFCell, Object> TYPE_NUMBER_SET_VALUE_FUN = (cell, data) -> {
        if (null != data) {
            cell.setCellValue(Double.parseDouble(data.toString()));
        }
    };


    /**
     * 字符串的设置值的方法
     */
    private static final BiConsumer<XSSFCell, Object> TYPE_SINGLE_LINE_TEXT_SET_VALUE_FUN = (cell, data) -> {
        if (null != data) {
            cell.setCellValue(String.valueOf(data.toString()));
        }
    };

    /**
     * 数字的设置值的方法
     */
    private static final BiConsumer<XSSFCell, Object> TYPE_MULTI_LINE_TEXT_SET_VALUE_FUN = (cell, data) -> {
        if (null != data) {
            List<String> stringList = (List<String>) data;
            cell.setCellValue(StringUtils.join(stringList, "\n"));
        }
    };

    /**
     * 单行文本
     */
    public static final int TYPE_SINGLE_LINE_TEXT = 10;

    /**
     * 多行文本
     */
    public static final int TYPE_MULTI_LINE_TEXT = 11;


    /**
     * 数字
     */
    public static final int TYPE_NUMBER = 20;

    /**
     * 单行文本样式
     */
    public static final ExcelCellValueTypeData SINGLE_LINE_TEXT = new ExcelCellValueTypeData(TYPE_SINGLE_LINE_TEXT);


    /**
     * 多行文本样式
     */
    public static final ExcelCellValueTypeData MULTI_LINE_TEXT = new ExcelCellValueTypeData(TYPE_MULTI_LINE_TEXT);


    /**
     * 数字的处理
     */
    public static final ExcelCellValueTypeData NUMBER = new ExcelCellValueTypeData(TYPE_NUMBER);

    /**
     * 值的类型
     */
    private int valueType;

    /**
     * 获取单元格值的方法
     */
    @Override
    public BiConsumer<XSSFCell, Object> getSetValueFun() {
        switch (valueType) {
            case TYPE_NUMBER:
                return TYPE_NUMBER_SET_VALUE_FUN;
            case TYPE_MULTI_LINE_TEXT:
                return TYPE_MULTI_LINE_TEXT_SET_VALUE_FUN;
            case TYPE_SINGLE_LINE_TEXT:
                return TYPE_SINGLE_LINE_TEXT_SET_VALUE_FUN;
            default:
                return DEFAULT_SAT_VALUE_FUN;
        }
    }


    /**
     * 获取单元格的格式化处理器
     */
    @Override
    public ExcelCellFormatter getCellFormatter() {
        switch (valueType) {
            case TYPE_MULTI_LINE_TEXT:
                return (cell) -> {
                    cell.getCellStyle().setWrapText(true);
                };
            default:
                return ExcelCellFormatter.DEFAULT_FORMATTER;
        }
    }
}
