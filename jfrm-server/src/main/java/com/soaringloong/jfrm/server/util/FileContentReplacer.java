package com.soaringloong.jfrm.server.util;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * idea实现快捷批量修改替换
 */
public class FileContentReplacer {

    public static void main(String[] args) {
        // 指定路径下的文件夹路径
        String directoryPath = "E:\\project\\Huaxia\\ERP\\erp";

        // 要查找和替换的字符串
        Map<String, String> replacementMap = new HashMap<>();
        replacementMap.put("\\* @date ", "\\* @since ");
        replacementMap.put("\\* @create ", "\\* @since ");
        replacementMap.put("import com.alibaba.fastjson.JSON;", "import com.tk.dep.json.JsonUtils;");
        replacementMap.put("import com.alibaba.fastjson.annotation.JSONField;", "");
        replacementMap.put("JSON.toJSONString", "JsonUtils.toJsonString");
        replacementMap.put("JSON.parseArray", "JsonUtils.parseArray");
        replacementMap.put("JSON.parseObject", "JsonUtils.parseObject");
        replacementMap.put("@JSONField\\(format = \"yyyy-MM-dd HH:mm:ss\"\\)", "");
        replacementMap.put("@JSONField\\(format = \"yyyy-MM-dd\"\\)", "");
        replacementMap.put("@JSONField\\(format=\"yyyy-MM-dd\"\\)", "");
        replacementMap.put("@JSONField\\(serialize = false\\)", "");

        String fileAppendix = ".java";
        // 调用递归方法替换文件夹中的内容
        replaceContentInDirectory(directoryPath, replacementMap, fileAppendix);
    }

    public static void replaceContentInDirectory(String directoryPath, Map<String, String> replacementMap, String fileAppendix) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果是文件夹，则递归调用
                    replaceContentInDirectory(file.getAbsolutePath(), replacementMap, fileAppendix);
                } else {
                    // 如果是文件，则替换文件内容
                    // 如果不满足后缀名要求，则跳过
                    if (StringUtils.isNotBlank(fileAppendix) && !file.getName().endsWith(fileAppendix)) {
                        continue;
                    }
                    replaceContentInFile(file, replacementMap);
                }
            }
        }
    }

    public static void replaceContentInFile(File file, Map<String, String> replacementMap) {
        try {
            // 读取文件内容
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                // 替换文件中的字符串
                Set<Map.Entry<String, String>> es = replacementMap.entrySet();
                for (Map.Entry<String, String> e : es) {
                    String searchString = e.getKey(), replacementString = e.getValue();
                    line = line.replaceAll(searchString, replacementString);
                }
                stringBuilder.append(line).append("\n");
            }
            reader.close();

            // 将替换后的内容写回文件
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(stringBuilder.toString());
            writer.close();

            System.out.println("文件 " + file.getAbsolutePath() + " 中的字符串替换完成。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
