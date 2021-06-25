package com.alexie.demo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义的公共方法
 *
 * @author Alexie on 2021/6/24
 * @ClassName Tools
 * @Description TODO
 * @Version 1.0
 */
public class Tools {

//
//    public String GetChinese(String str)
//    {
//        String chineseStr= "";
//        for(int i=0;i<str.length();i++)
//        {
//            if (System.Co.ToInt32(str[i])>128){
//                chineseStr +=str[i].ToString();
//            }
//        }
//        return chineseStr;
//    }

    public static boolean usingStream(List<Float> list, List<Float> list1) {
        List<String> newlist = new ArrayList<>(list.size());
        List<String> newList1 = new ArrayList<>(list1.size());
        for (Float myInt : list) {
            newlist.add(String.valueOf(myInt));
        }
        for (Float myInt1 : list1) {
            newList1.add(String.valueOf(myInt1));
        }
        /** 先将集合转成stream流进行排序然后转成字符串进行比较 */
        return newlist.stream().sorted().collect(Collectors.joining())
                .equals(newList1.stream().collect(Collectors.joining()));
    }
}
