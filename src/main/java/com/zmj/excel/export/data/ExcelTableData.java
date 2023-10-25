package com.zmj.excel.export.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZMJ
 * @decribe Excel 行数据
 * @Package com.zmj.excel.export.data
 * @date 2023/10/7 10:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelTableData {
    /**
     * 起始的行号
     * */
    private int beginRowNo;

    /**
     * 行数据列表
     * */
    private List<ExcelRowData> rowDataList;


}
