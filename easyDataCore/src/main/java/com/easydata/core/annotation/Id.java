package com.easydata.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于与底层操作中主键的确认。<br />
 *
 * @author Mr.Pro
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {

	/**
	 * 确认主键名称(默认为当前的域名称)
	 * @return 主键名称
	 */
	String value() default "";

	/**
	 * 指定主键值中的前缀内容
	 * @return 前缀内容
	 */
	String prefix() default "";

	/**
	 * 指定主键值中的排序
	 * @return 主要应用于拥有多ID时，只要自行指定顺序即可
	 */
	int order() default 1;
}