package com.zmj.excel.export.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZMJ
 * @describe Excel 行数据
 * @Package com.zmj.excel.export.data
 * @date 2023/10/7 10:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelRowData {
    /**
     * 单元格数据列表
     */
    private List<ExcelCellData> cellDataList;
}
