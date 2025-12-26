package com.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    private static final String DEV_PATH = "src/com/util/record.txt";

    private static final String PROD_DIR = System.getProperty("user.home") + File.separator + ".tank_game";
    private static final String PROD_PATH = PROD_DIR + File.separator + "record.txt";
    private static final String PROD_DATA = "Shortest kill time:114514ms";

    private static File resolveFile() {
        File devFile = new File(DEV_PATH);
        
        if (devFile.exists()) {
            System.out.println("检测到开发环境记录文件，使用: " + DEV_PATH);
            return devFile;
        }

        // 如果生产环境记录文件不存在，创建并写入初始数据
        File prodFile = new File(PROD_PATH);
        if (!prodFile.exists()) {
            try {
                File dir = new File(PROD_DIR);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                prodFile.createNewFile();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(prodFile))) {
                    writer.write(PROD_DATA);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("使用生产环境记录文件: " + PROD_PATH);
        
        return new File(PROD_PATH);
    }

    public static Long getRecord() {
        File file = resolveFile();

        if (!file.exists()) {
            return -1L;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line != null && line.contains("Shortest kill time:")) {
                String[] parts = line.split(":");
                if (parts.length > 1) {
                    String timeStr = parts[1].trim(); 
                    timeStr = timeStr.replace("ms", ""); 
                    try {
                        return Long.parseLong(timeStr);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return -1L;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    public static void writeRecord(Long record) {
        File file = resolveFile();
        
        try {
            if (file.getAbsolutePath().equals(new File(PROD_PATH).getAbsolutePath())) {
                File dir = new File(PROD_DIR);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            Long existingRecord = getRecord();
            if (existingRecord != -1L && record >= existingRecord) {
                System.out.println("现有记录更优，无需更新。");
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                writer.write("Shortest kill time:" + record + "ms");
                writer.newLine();
            }
            
            System.out.println("记录已更新至: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}