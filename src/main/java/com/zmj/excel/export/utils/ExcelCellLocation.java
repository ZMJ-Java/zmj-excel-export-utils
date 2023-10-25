package com.zmj.excel.export.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMJ
 * @Package com.zmj.excel.export.utils
 * @date 2023/10/6 15:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelCellLocation {

    /**
     * 行号
     * */
    private int row;


    /**
     * 列号
     * */
    private int column;
}
