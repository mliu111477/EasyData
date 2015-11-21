package com.easydata.core.utils;


import java.util.UUID;

/**
 * 随机生成GUID。<br />
 *
 * @author Mr.Pro
 */
public class RandomGUID{
	public static String getGUID() {
		return UUID.randomUUID().toString().replace("-","");
	}
}