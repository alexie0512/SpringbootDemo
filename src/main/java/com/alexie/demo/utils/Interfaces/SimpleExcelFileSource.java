package com.alexie.demo.utils.Interfaces;

import org.junit.jupiter.params.provider.ArgumentsSource;
import java.lang.annotation.*;

/**
 * Excel 传参注解
 *
 * @author Alexie on 2021/6/18
 * @ClassName SimpleExcelFileSource
 * @Description TODO
 * @Version 1.0
 */



@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ArgumentsSource(SimpleExcelFileArgumentsProvider.class)
public @interface SimpleExcelFileSource {

    String resource();

    String sheetNameToRead() default "Sheet 1";

    String encoding() default "UTF-8";

    String lineSeparator() default "\n";

    char delimiter() default '\u0000';

    String delimiterString() default "";

    int headerLineNum() default 0;

    String emptyValue() default "";

    String[] nullValues() default {};
}
