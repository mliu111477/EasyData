package com.easydata.core.utils;

import java.lang.reflect.Field;

/**
 * 字段和真实应该使用的实体名称。<br />
 *
 * @author Mr.Pro
 */
public class AnnotationFieldToNameEntry extends AnnotationBasicFieldToNameEntry{

    public AnnotationFieldToNameEntry(Field field, String useName) {
        super(field, useName);
    }
}
