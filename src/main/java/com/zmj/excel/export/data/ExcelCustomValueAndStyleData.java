package com.zmj.excel.export.data;

import com.zmj.excel.export.utils.ExcelCellFormatter;
import com.zmj.excel.export.utils.ExcelCellLocation;
import com.zmj.excel.export.utils.ExcelUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author ZMJ
 * @describe 自定义单元格数据和样式数据
 * @Package com.zmj.excel.export.data
 * @date 2023/10/6 15:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelCustomValueAndStyleData implements ExcelCellCustomData {
    /**
     * 自定义单元格的值的方法
     */
    private BiConsumer<XSSFCell, Object> customCellValueFun;
    /**
     * 自定义单元格样式方法
     */
    private List<ExcelCellFormatter> customerFormatterList;
    /**
     * 参考单元格样式的方法列表
     */
    private ExcelCellLocation referStyleCell;


    /**
     * 获取单元格样式
     */
    private XSSFCellStyle getCellStyle(XSSFCell cell) {
        XSSFSheet sheet = cell.getSheet();
        XSSFCellStyle cellStyle;
        if (null != referStyleCell) {
            //根据参考的单元个的style 创建不共享的style
            XSSFCell referCell = sheet.getRow(referStyleCell.getRow()).getCell(referStyleCell.getColumn());
            if (null != referCell) {
                referCell = sheet.getRow(referStyleCell.getRow()).createCell(referStyleCell.getColumn());
            }
            cellStyle = ExcelUtil.createNotShareCellStyle(referCell);
        } else {
            //创建不共享的style
            cellStyle = ExcelUtil.createNotShareCellStyle(sheet.getRow(cell.getRowIndex())
                    .getCell(cell.getColumnIndex()));
        }
        return cellStyle;
    }

    /**
     * 获取单元格的格式化处理器
     */
    @Override
    public ExcelCellFormatter getCellFormatter() {
        return (cell) -> {
            cell.setCellStyle(getCellStyle(cell));
            //执行自定义样式处理
            if (CollectionUtils.isNotEmpty(customerFormatterList)) {
                customerFormatterList.forEach(item -> {
                    if (item != null) {
                        item.formatter(cell);
                    }
                });
            }
        };
    }

    /**
     * 获取设置单元格值的方法
     */
    @Override
    public BiConsumer<XSSFCell, Object> getSetValueFun() {
        return this.customCellValueFun == null ? DEFAULT_SAT_VALUE_FUN : this.customCellValueFun;
    }
}
