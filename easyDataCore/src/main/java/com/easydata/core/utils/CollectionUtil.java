package com.easydata.core.utils;

import java.util.Map;

/**
 * 容器工具。<br />
 *
 * @author Mr.Pro
 */
public class CollectionUtil {
    public static <K, V> V get(Map<K, V> map, K key, V defaultValue) {
        V val = map.get(key);
        return val == null ? defaultValue : val;
    }
}
