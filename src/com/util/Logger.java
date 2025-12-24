package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Logger {
    private static String BASE_PATH = "src/com/util/record.txt";

    public static Long getRecord() {
        File file = new File(BASE_PATH);
        if (!file.exists()) {
            System.out.println("记录文件不存在，返回默认值 -1");
            return -1L;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            // Example: Shortest kill time:1234ms
            if (line != null && line.contains("Shortest kill time:")) {
                String[] parts = line.split(":");
                if (parts.length > 1) {
                    String timeStr = parts[1].trim(); 
                    timeStr = timeStr.replace("ms", ""); 
                    try {
                        long ms = Long.parseLong(timeStr);
                        return ms;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return -1L;
                    }
                }
            } else {
                System.out.println("记录文件格式不正确，返回默认值 -1");
                return -1L;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1L;
        }
        return -1L;
    }

    public static void writeRecord(Long record) {
        File file = new File(BASE_PATH);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            Long existingRecord = getRecord();
            if (existingRecord != -1L && record >= existingRecord) {
                System.out.println("现有记录更优，无需更新。");
                return;
            }
            java.io.FileWriter writer = new java.io.FileWriter(file, false);
            writer.write("Shortest kill time:" + record + "ms\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}