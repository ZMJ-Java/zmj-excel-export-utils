package com.zmj.excel.export.config;

import com.zmj.excel.export.utils.ExcelCellFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.util.function.BiConsumer;

/**
 * @author ZMJ
 * @describe Excel单元格生成配置
 * @Package com.zmj.excel.export.config
 * @date 2023/10/6 8:28
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelCellGenerationConfig {
    /**
     * 单元格的值
     * */
    private Object value;


    /**
     * 设置单元格值的方法
     * */
    private BiConsumer<XSSFCell,Object> setValueFun;

    /**
     * 格式化处理
     * */
     private ExcelCellFormatter formatter;
}
