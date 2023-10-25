package com.zmj.excel.export.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author ZMJ
 * @describe Excel 文件数据对象
 * @Package com.zmj.excel.export.data
 * @date 2023/10/7 9:53
 */

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class ExcelFileData {
    /**
     * 输出的文件名
     * */
    private String fileName;

    /**
     * 每个sheet数据
     * */
    private List<ExcelSheetData> sheetDataList;


    /**
     * 变量数据
     * */
    private Map<String,ExcelCellData> variableMap;

}
