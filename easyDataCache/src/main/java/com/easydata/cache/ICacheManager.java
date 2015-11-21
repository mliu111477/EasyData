package com.easydata.cache;

import com.easydata.core.constants.ReturnCode;
import com.easydata.core.manager.Connectable;
import com.easydata.core.manager.IBaseManager;

import java.util.List;

/**
 * 缓存管理器。<br />
 *
 * @author Mr.Pro
 */
public interface ICacheManager extends IBaseManager, Connectable {

    /**
     * 放置指定的缓存信息(字符串)
     *
     * @param key 主键
     * @param data 值
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setString(String key, String data);

    /**
     * 放置指定的缓存列表(字符串)
     *
     * @param key 主键
     * @param data 值
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setStringList(String key, List<String> data);

    /**
     * 放置指定的缓存信息(字符串)
     *
     * @param key 主键
     * @param data 值
     * @param exp 超时时间(秒)
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setString(String key, String data, int exp);

    /**
     * 放置指定的缓存列表(字符串)
     *
     * @param key 主键
     * @param data 值
     * @param exp 超时时间(秒)
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setStringList(String key, List<String> data, int exp);

    /**
     * 放置指定的缓存信息(整形)
     *
     * @param key 主键
     * @param data 值
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setInt(String key, int data);

    /**
     * 放置指定的缓存列表(整形)
     *
     * @param key 主键
     * @param data 值
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setIntList(String key, List<Integer> data);

    /**
     * 放置指定的缓存信息(整形)
     *
     * @param key 主键
     * @param data 值
     * @param exp 超时时间(秒)
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setInt(String key, int data, int exp);

    /**
     * 放置指定的缓存列表(整形)
     *
     * @param key 主键
     * @param data 值
     * @param exp 超时时间(秒)
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setIntList(String key, List<Integer> data, int exp);

    /**
     * 放置指定的缓存信息(长整形)
     *
     * @param key 主键
     * @param data 值
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setLong(String key, long data);

    /**
     * 放置指定的缓存列表(长整形)
     *
     * @param key 主键
     * @param data 值
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setLongList(String key, List<Long> data);

    /**
     * 放置指定的缓存信息(长整形)
     *
     * @param key 主键
     * @param data 值
     * @param exp 超时时间(秒)
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setLong(String key, long data, int exp);

    /**
     * 放置指定的缓存列表(长整形)
     *
     * @param key 主键
     * @param data 值
     * @param exp 超时时间(秒)
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setLongList(String key, List<Long> data, int exp);

    /**
     * 放置指定的缓存信息(布尔)
     *
     * @param key 主键
     * @param data 值
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setBoolean(String key, boolean data);

    /**
     * 放置指定的缓存列表(布尔)
     *
     * @param key 主键
     * @param data 值
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setBooleanList(String key, List<Boolean> data);

    /**
     * 放置指定的缓存信息(布尔)
     *
     * @param key 主键
     * @param data 值
     * @param exp 超时时间(秒)
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setBoolean(String key, boolean data, int exp);

    /**
     * 放置指定的缓存列表(布尔)
     *
     * @param key 主键
     * @param data 值
     * @param exp 超时时间(秒)
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setBooleanList(String key, List<Boolean> data, int exp);

    /**
     * 放置指定的缓存信息(单精度)
     *
     * @param key 主键
     * @param data 值
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setFloat(String key, float data);

    /**
     * 放置指定的缓存列表(单精度)
     *
     * @param key 主键
     * @param data 值
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setFloatList(String key, List<Float> data);

    /**
     * 放置指定的缓存信息(单精度)
     *
     * @param key 主键
     * @param data 值
     * @param exp 超时时间(秒)
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setFloat(String key, float data, int exp);

    /**
     * 放置指定的缓存列表(单精度)
     *
     * @param key 主键
     * @param data 值
     * @param exp 超时时间(秒)
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setFloatList(String key, List<Float> data, int exp);

    /**
     * 放置指定的缓存信息(双精度)
     *
     * @param key 主键
     * @param data 值
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setDouble(String key, double data);

    /**
     * 放置指定的缓存列表(双精度)
     *
     * @param key 主键
     * @param data 值
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setDoubleList(String key, List<Double> data);

    /**
     * 放置指定的缓存信息(双精度)
     *
     * @param key 主键
     * @param data 值
     * @param exp 超时时间(秒)
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setDouble(String key, double data, int exp);

    /**
     * 放置指定的缓存列表(双精度)
     *
     * @param key 主键
     * @param data 值
     * @param exp 超时时间(秒)
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setDoubleList(String key, List<Double> data, int exp);

    /**
     * 设置指定的实体对象数据类型，会按照#{@link com.easydata.core.annotation}包种的类型类进行设定实体信息
     *
     * @param beanData 实体信息,主键为在实体中Id规定的值
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setBean(Object beanData);

    /**
     * 设置指定的实体对象数据类型列表，会按照#{@link com.easydata.core.annotation}包种的类型类进行设定实体信息
     *
     * @param key 指定自定义key
     * @param beanList 实体信息
     * @param <T> 实体类型
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public <T> int setBeanListToSingleKey(String key, List<T> beanList);

    /**
     * 设置指定的实体对象数据类型列表，会按照#{@link com.easydata.core.annotation}包种的类型类进行设定实体信息
     *
     * @param key 指定自定义key
     * @param beanList 实体信息
     * @param exp 超时时间(秒)
     * @param <T> 实体类型
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public <T> int setBeanListToSingleKey(String key, List<T> beanList, int exp);

    /**
     * 设置指定的实体对象数据类型，会按照#{@link com.easydata.core.annotation}包种的类型类进行设定实体信息
     *
     * @param beanData 实体信息,主键为在实体中Id规定的值
     * @param exp 超时时间(秒)
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-8:设置失败。{@link ReturnCode#ADD_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int setBean(Object beanData, int exp);

    /**
     * 获取缓存数据(字符串)
     *
     * @param key 主键
     * @return 缓存数据
     */
    public String getString(String key);

    /**
     * 获取缓存数据列表(字符串)
     *
     * @param key 主键
     * @return 缓存数据
     */
    public List<String> getStringList(String key);

    /**
     * 获取缓存数据(字符串)
     *
     * @param key 主键
     * @param defaultValue 如果主键不存在的话，返回指定默认值
     * @return 缓存数据
     */
    public String getString(String key, String defaultValue);

    /**
     * 获取缓存数据列表(字符串)
     *
     * @param key 主键
     * @param defaultValue 如果主键不存在的话，返回指定默认值
     * @return 缓存数据
     */
    public List<String> getStringList(String key, List<String> defaultValue);

    /**
     * 获取缓存数据(整形)
     *
     * @param key 主键
     * @return 缓存数据
     */
    public int getInt(String key);

    /**
     * 获取缓存数据列表(整形)
     *
     * @param key 主键
     * @return 缓存数据
     */
    public List<Integer> getIntList(String key);

    /**
     * 获取缓存数据(整形)
     *
     * @param key 主键
     * @param defaultValue 如果主键不存在的话，返回指定默认值
     * @return 缓存数据
     */
    public int getInt(String key, int defaultValue);

    /**
     * 获取缓存数据列表(整形)
     *
     * @param key 主键
     * @param defaultValue 如果主键不存在的话，返回指定默认值
     * @return 缓存数据
     */
    public List<Integer> getIntList(String key, List<Integer> defaultValue);

    /**
     * 获取缓存数据(长整形)
     *
     * @param key 主键
     * @return 缓存数据
     */
    public long getLong(String key);

    /**
     * 获取缓存数据列表(长整形)
     *
     * @param key 主键
     * @return 缓存数据
     */
    public List<Long> getLongList(String key);

    /**
     * 获取缓存数据(长整形)
     *
     * @param key 主键
     * @param defaultValue 如果主键不存在的话，返回指定默认值
     * @return 缓存数据
     */
    public long getLong(String key, long defaultValue);

    /**
     * 获取缓存数据列表(长整形)
     *
     * @param key 主键
     * @param defaultValue 如果主键不存在的话，返回指定默认值
     * @return 缓存数据
     */
    public List<Long> getLongList(String key, List<Long> defaultValue);

    /**
     * 获取缓存数据(布尔)
     *
     * @param key 主键
     * @return 缓存数据
     */
    public boolean getBoolean(String key);

    /**
     * 获取缓存数据列表(布尔)
     *
     * @param key 主键
     * @return 缓存数据
     */
    public List<Boolean> getBooleanList(String key);

    /**
     * 获取缓存数据(布尔)
     *
     * @param key 主键
     * @param defaultValue 如果主键不存在的话，返回指定默认值
     * @return 缓存数据
     */
    public boolean getBoolean(String key, boolean defaultValue);

    /**
     * 获取缓存数据列表(布尔)
     *
     * @param key 主键
     * @param defaultValue 如果主键不存在的话，返回指定默认值
     * @return 缓存数据
     */
    public List<Boolean> getBooleanList(String key, List<Boolean> defaultValue);

    /**
     * 获取实体，会按照#{@link com.easydata.core.annotation}包种的类型类进行设定实体信息
     *
     * @param beanClass 实体对象Class信息
     * @param idValue id值
     * @return 实体数据信息
     */
    public <T extends Object> T getBean(Class<T> beanClass, Object... idValue);

    /**
     * 获取实体，会按照#{@link com.easydata.core.annotation}包种的类型类进行设定实体信息
     *
     * @param beanClass 实体对象Class信息
     * @param idValue id值
     * @param defaultValue 如果主键不存在的话，返回指定默认值
     * @return a T object.
     */
    public <T extends Object> T getBean(Class<T> beanClass, T defaultValue, Object... idValue);

    /**
     * 获取实体列表，会按照#{@link com.easydata.core.annotation}包种的类型类进行设定实体信息
     *
     * @param key 自定义主键
     * @param cls 缓存数据类型
     * @param defaultValue 默认值
     * @param <T> 实体对象
     * @return a {@link List} object.
     */
    public <T> List<T> getBeanListBySingleKey(String key, Class<T> cls, List<T> defaultValue);

    /**
     * 根据主键来进行删除缓存信息
     *
     * @param key 主键
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-7:删除失败。{@link ReturnCode#DELETE_FAILED}</li>
     * 		<li>-11:参数错误。{@link ReturnCode#PARAMETER_MISSING}</li>
     * </ul>
     */
    public int remove(String key);

    /**
     * 清空所有缓存
     *
     * @return 状态码<br />
     * <ul>
     * 		<li>1: 成功。{@link ReturnCode#SUCCESS} </li>
     * 		<li>-401:清空缓存失败。{@link ReturnCode#CLEAR_CACHE_FAILED}</li>
     * </ul>
     */
    public int clearCache();

    /**
     * 判断主键是否存在
     *
     * @param key 主键
     * @return 是否存在该主键
     */
    public boolean exists(String key);
}
