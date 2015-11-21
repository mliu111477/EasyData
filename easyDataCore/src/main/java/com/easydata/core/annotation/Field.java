package com.easydata.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于与底层操作中域的确认。如果域不增加本注解同样也会被确认，增加该域主要是为了和真实操作中的名称区分。<br />
 *
 * @author Mr.Pro
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {

	/**
	 * 指定该域在存储时的真实名称
	 * @return 名称
	 */
	String value();
}
