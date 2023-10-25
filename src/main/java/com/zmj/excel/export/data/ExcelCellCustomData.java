package com.zmj.excel.export.data;

import com.zmj.excel.export.utils.ExcelCellFormatter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author ZMJ
 * @describe 单元格自定义数据
 * @Package com.zmj.excel.export.data
 * @date 2023/10/6 14:21
 */
public interface ExcelCellCustomData {
    /**
     * 默认的设置的方法
     */
    BiConsumer<XSSFCell, Object> DEFAULT_SAT_VALUE_FUN = (cell, data) -> {
      if (null != data){
          if (data instanceof Boolean){
              cell.setCellValue(((Boolean)data).booleanValue());
          }else if (data instanceof Number){
              cell.setCellValue(((Number)data).doubleValue());
          }else if (data instanceof List){
              cell.setCellValue(StringUtils.join((List<String>)data,"\n" ));
          }else {
              cell.setCellValue(String.valueOf(data));
          }
      }
    };


    /**
     * 获取设置单元格的方法
     * */
    default BiConsumer<XSSFCell,Object> getSetValueFun(){
        return DEFAULT_SAT_VALUE_FUN;
    }

    /**
     * 获取单元格的格式化处理器
     * */
    default ExcelCellFormatter getCellFormatter(){
        return ExcelCellFormatter.DEFAULT_FORMATTER;
    }
}
