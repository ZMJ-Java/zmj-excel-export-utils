package com.zmj.excel.export.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZMJ
 * @Package com.zmj.excel.export.config
 * @date 2023/10/6 9:00
 * @describe Excel表格生成配置
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelTableGenerationConfig {
    /**
     * 开始行号
     * */
    private int beginRowNo;


    /**
     * 表格行列表
     * */
    private List<ExcelRowGenerationConfig> rowList;
}
