package com.alexie.demo.utils.Interfaces;


import com.alexie.demo.utils.Interfaces.SimpleExcelFileSource;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.PreconditionViolationException;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Alexie on 2021/6/18
 * @ClassName SimpleExcelFileArgumentsProvider
 * @Description TODO
 * @Version 1.0
 */

public class SimpleExcelFileArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<SimpleExcelFileSource> {

    private final BiFunction<Class<?>, String, InputStream> inputStreamProvider;
    private SimpleExcelFileSource annotation;
    private String resource;
    private Charset charset;
    private int headerLineNum;
    private String sheetName;
    private String emptyValue;

    public SimpleExcelFileArgumentsProvider(BiFunction<Class<?>, String, InputStream> inputStreamProvider) {
        this.inputStreamProvider = inputStreamProvider;
    }

    public SimpleExcelFileArgumentsProvider() {
        this(Class::getResourceAsStream);
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception{
        return this.getTestDataStreamFromExcelFile(resource,sheetName,this.headerLineNum);
    }

    private Stream<? extends Arguments> getTestDataStreamFromExcelFile(String resourcePath, String sheetName, int numLinesToSkip) {
        if(resourcePath==null){
            System.out.println("Filename must not be null!");
            return null;
        }

        if(sheetName==null){
            System.out.println("Sheet name must not be null!");
            return null;
        }
        Stream<Arguments> returnStream = Stream.empty();
//        System.out.println(returnStream);
        //定义单元格数据格式处理对象
        DataFormatter myDataFormatter = new DataFormatter();

        //获取工作簿对象
        try(Workbook excelFile =
                    WorkbookFactory.create(new File(resourcePath),null,true)) {
            //获取工作表对象
            Sheet sheet = excelFile.getSheet(sheetName);
            //行数据处理，忽略标题行，行数据作为后续参数List
            Row header = sheet.getRow(numLinesToSkip);
            int colNum = header.getPhysicalNumberOfCells();
            //System.out.println("column num: " + colNum);
            for(Row row: sheet){
                if(row.getRowNum()==numLinesToSkip) {
                    continue;
                }
                System.out.println(row.getPhysicalNumberOfCells());
                ArrayList<Object> rowArrayList = new ArrayList<>();
                //需要用固定列数，使得返回的参数都固定
                for (int i = 0; i < colNum; i++) {
                    if(row.getCell(i)==null|| row.getCell(i).toString()==""){
                        continue;
                    }else {
                        rowArrayList.add(myDataFormatter.formatCellValue(row.getCell(i)));
                    }

                }

                System.out.println(rowArrayList);//输出一个list

                //如果只有在行单元格有值的情况下做对象转换！！
                if(rowArrayList.size()!=0) {
                    //转换为MethodSource的Arguments对象
                    Arguments arguments = Arguments.of(rowArrayList.toArray());
                    //Arguments转换为Stream
                    returnStream = Stream.concat(returnStream, Stream.of(arguments));

                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnStream;

    }

    @Override
    public void accept(SimpleExcelFileSource annotation) {
        this.annotation = annotation;
        this.resource = annotation.resource();
        this.charset = this.getCharsetFrom(annotation);
        this.headerLineNum = annotation.headerLineNum();
        this.sheetName = annotation.sheetNameToRead();
        this.emptyValue = annotation.emptyValue();

    }

    private Charset getCharsetFrom(SimpleExcelFileSource annotation) {
        try{
            return Charset.forName(annotation.encoding());
        }catch (Exception var3){
            throw new PreconditionViolationException("The charset supplied in " + annotation + " is invalid", var3);
        }
    }

//
//    public static boolean isRowEmpty(Row row) {
//        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
//            Cell cell = row.getCell(c);
//            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
//                return false;
//            }
//        }
//        return true;
//    }

    @Override
    public Consumer<SimpleExcelFileSource> andThen(Consumer<? super SimpleExcelFileSource> after) {
        return null;
    }
}
