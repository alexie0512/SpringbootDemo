package com.alexie.demo.utils;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Yaml文件流读取/写入Yaml配置文件
 *
 * @author Alexie on 2021/6/10
 * @ClassName YamlUtils
 * @Description TODO
 * @Version 1.0
 */
public class YamlUtils {

    public static void test(File file) throws IOException {
        BufferedOutputStream f = new BufferedOutputStream(new FileOutputStream(file));
        HashMap<Object,Object> map = new HashMap<>();
        Yaml yml = new Yaml();
        f.write(yml.dumpAsMap(map).getBytes());
        f.flush();
        f.close();
    }



}
