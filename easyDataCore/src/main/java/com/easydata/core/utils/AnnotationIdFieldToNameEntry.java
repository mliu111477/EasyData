package com.easydata.core.utils;

import com.easydata.core.annotation.Id;

import java.lang.reflect.Field;

/**
 * ID字段和真实应该使用的实体名称。<br />
 *
 * @author Mr.Pro
 */
public class AnnotationIdFieldToNameEntry extends AnnotationBasicFieldToNameEntry {

    private Id id;

    public AnnotationIdFieldToNameEntry(Id id, Field field, String useName) {
        super(field, useName);
        this.id = id;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }
}
