package com.zmj.excel.export.data;

import com.zmj.excel.export.utils.ExcelCellFormatter;
import com.zmj.excel.export.utils.ExcelCellLocation;
import com.zmj.excel.export.utils.ExcelUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author ZMJ
 * @describe Excel 参考单元格样式数据
 * @Package com.zmj.excel.export.data
 * @date 2023/10/7 10:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelReferCellStyleData implements ExcelCellCustomData {
    /**
     * 自定义单元格的值的方法
     */
    private BiConsumer<XSSFCell, Object> customCellValueFun;

    /**
     * 参考样式的单元格
     */
    private ExcelCellLocation referStyleCell;


    @Override
    public BiConsumer<XSSFCell, Object> getSetValueFun() {
        return this.customCellValueFun == null ? DEFAULT_SAT_VALUE_FUN : this.customCellValueFun;
    }

    /**
     * 获取单元格的格式化处理
     */
    @Override
    public ExcelCellFormatter getCellFormatter() {
        return (cell) -> {
            XSSFSheet sheet = cell.getSheet();
            XSSFCell referCell = sheet.getRow(referStyleCell.getRow()).getCell(referStyleCell.getColumn());
            if (referCell == null) {
                sheet.getRow(referStyleCell.getRow()).createCell(referStyleCell.getColumn());
            }
            cell.setCellStyle(ExcelUtil.createShareCellStyle(referCell));
        };
    }
}
