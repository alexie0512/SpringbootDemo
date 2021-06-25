package com.alexie.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 读取文件/写出文件
 *
 * @author Alexie on 2021/6/8
 * @ClassName FileUtils
 * @Description TODO
 * @Version 1.0
 */

public class FileUtils {


    //Logger
    protected static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 保存文件到本地
     * @param result  需要写入的数据
     * @param outPath   保存的路径
     * @param outFileName   保存的文件名
     * @throws Exception
     */
    public static void writeStrtoFile(String result, String outPath, String outFileName) throws Exception {
        File dir = new File(outPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File txt = new File(outPath + "/" + outFileName);
//         先删除；否则会直接追加在之前的内容后面，成几何倍数增长
        if (txt.isFile() && txt.exists()) {
            logger.info("文件已存在！");
//            txt.delete();
        }

        // 再创建
        if (!txt.exists()) {
            txt.createNewFile();
        }
        byte bytes[] = new byte[512];
        bytes = result.getBytes();
        int b = bytes.length; // 是字节的长度，不是字符串的长度
        FileOutputStream fos = new FileOutputStream(txt,true);
        fos.write(bytes);
        fos.flush();
        fos.close();
        logger.info("文件写入成功！");
    }



    /**
     * 读取本地文件（按行读取），因为存的时候没换行，所以按行读取
     * @param fileName  文件名
     */
    public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        String readData = "";
        BufferedReader reader = null;
        try {
            String tempString = null;
            reader = new BufferedReader(new FileReader(file));
            // 一次读一行，读入null时文件结束
            while ((tempString = reader.readLine()) != null) {
                readData += tempString;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return readData;

    }


}
