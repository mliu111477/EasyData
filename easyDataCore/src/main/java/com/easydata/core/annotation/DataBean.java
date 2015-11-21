package com.easydata.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于与底层操作中表名称的确认。如果尚未指定则显示为类名。<br />
 *
 * @author Mr.Pro
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataBean {

	String value();
}
