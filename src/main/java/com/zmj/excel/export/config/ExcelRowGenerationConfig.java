package com.zmj.excel.export.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZMJ
 * @Package com.zmj.excel.export.config
 * @date 2023/10/6 8:53
 * @describe Excel行生成配置
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelRowGenerationConfig {
    /**一行单元格列表*/
    private List<ExcelCellGenerationConfig> cellList;
}
