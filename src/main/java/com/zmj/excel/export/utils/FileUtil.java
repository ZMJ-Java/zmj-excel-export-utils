package com.zmj.excel.export.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @author ZMJ
 * @describe 文件工具类
 * @Package com.zmj.excel.export.utils
 * @date 2023/10/7 10:36
 */
public class FileUtil {
    private FileUtil() {
        throw new IllegalArgumentException("Utility class");
    }


    /**
     * 获取指定的文件输入流
     */
    public static InputStream getFileInputStream(String fileName) {
        Resource resource = new ClassPathResource(fileName);
        File templateFile = getFile(fileName);
        try {
            return templateFile.exists() ? new FileInputStream(templateFile) : resource.getInputStream();
        } catch (IOException e) {
           e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取指定文件
     */
    private static File getFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return file;
        }
        URL url = FileUtil.class.getClassLoader().getResource(fileName);
        if (url != null) {
            try {
                fileName = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.name());
                file = new File(fileName);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }
}
