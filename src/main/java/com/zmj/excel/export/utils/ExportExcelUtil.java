package com.zmj.excel.export.utils;

import lombok.extern.log4j.Log4j2;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ZMJ
 * @Package com.zmj.excel.export.utils
 * @date 2023/10/7 10:46
 */
@Log4j2
public class ExportExcelUtil {
    private ExportExcelUtil() {
        throw new IllegalArgumentException("Utility class");
    }

    /**
     * 变量匹配模式
     */
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);


    /**
     * 默认导出的Excel响应头Map
     */
    private static final Map<String, String> DEFAULT_EXPORT_EXCEL_RESP_HEADER_MAP = Stream.of(
            new AbstractMap.SimpleImmutableEntry<>("Cache-Control", "must-revalidate"),
            new AbstractMap.SimpleImmutableEntry<>("Pragma", "public"),
            //告诉浏览器用什么软件可以打开此文件
            new AbstractMap.SimpleImmutableEntry<>("content-Type", "application/vnd.ms-excel"),
            new AbstractMap.SimpleImmutableEntry<>("Access-Control-Expose-Headers", "Content-Disposition")
    ).collect(Collectors.toMap(item -> item.getKey(), item -> item.getValue()));


    /**
     * 获取指定输出的文件名导出的Excel的文件响应头
     */
    public static Map<String, String> getExportExcelRespHeader(String outputFileName) {
        Map<String, String> headerMap = new HashMap<>(DEFAULT_EXPORT_EXCEL_RESP_HEADER_MAP);
        try {
            headerMap.put("Content-Disposition", "attachment;filename="
                    + URLEncoder.encode(outputFileName, StandardCharsets.UTF_8.name()).replace("+", "%20"));
        } catch (UnsupportedEncodingException e) {
            log.debug("转换输出文件异常", e);
            log.debug("使用不转换的文件名称");
            headerMap.put("Content-Disposition", "attachment;filename=" + outputFileName);
        }
        return headerMap;
    }
}
