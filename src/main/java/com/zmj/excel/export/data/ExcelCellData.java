package com.zmj.excel.export.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMJ
 * @describe 单元格数据
 * @Package com.zmj.excel.export.data
 * @date 2023/10/6 14:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelCellData {
    /**
     * 单元格的值
     * */

    private Object value;


    /**
     * 单元格自定义数据
     * */
    private ExcelCellCustomData cellCustomData;

}
