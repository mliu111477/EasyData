package com.easydata.cache;

import com.easydata.core.constants.ReturnCode;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * redis管理器。<br />
 *
 * @author Mr.Pro
 */
public interface IRedisCacheManager extends ICacheManager {

    /**
     * 开始一个Session，调用{@link IRedisCacheManager#endSession()}之前的所有的执行操作都是使用相同的链接对象。适用于大量的redis同时操作
     * {@code manager.startSession();  manager.setString(...); manager.setInt(...); manager.endSession();}
     */
    public void startSession();

    /**
     * 结束一个session操作
     */
    public void endSession();

    /**
     * 添加指定的缓存信息到列表左侧(字符串)
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
    public int addStringToListLeft(String key, String data);

    /**
     * 添加指定的缓存信息到列表左侧(字符串)
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
    public int addStringListToListLeft(String key, List<String> data);

    /**
     * 添加指定的缓存信息到列表右侧(字符串)
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
    public int addStringToListRight(String key, String data);

    /**
     * 添加指定的缓存信息到列表右侧(字符串)
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
    public int addStringListToListRight(String key, List<String> data);

    /**
     * 添加指定的缓存信息到列表左侧(整形)
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
    public int addIntToListLeft(String key, int data);

    /**
     * 添加指定的缓存信息到列表左侧(整形)
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
    public int addIntListToListLeft(String key, List<Integer> data);

    /**
     * 添加指定的缓存信息到列表右侧(整形)
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
    public int addIntToListRight(String key, int data);

    /**
     * 添加指定的缓存信息到列表右侧(整形)
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
    public int addIntListToListRight(String key, List<Integer> data);

    /**
     * 添加指定的缓存信息到列表左侧(长整形)
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
    public int addLongToListLeft(String key, long data);

    /**
     * 添加指定的缓存信息到列表左侧(长整形)
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
    public int addLongListToListLeft(String key, List<Long> data);

    /**
     * 添加指定的缓存信息到列表右侧(长整形)
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
    public int addLongToListRight(String key, long data);

    /**
     * 添加指定的缓存信息到列表右侧(长整形)
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
    public int addLongListToListRight(String key, List<Long> data);

    /**
     * 添加指定的缓存信息到列表左侧(布尔)
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
    public int addBooleanToListLeft(String key, boolean data);

    /**
     * 添加指定的缓存信息到列表左侧(布尔)
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
    public int addBooleanListToListLeft(String key, List<Boolean> data);

    /**
     * 添加指定的缓存信息到列表右侧(布尔)
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
    public int addBooleanToListRight(String key, boolean data);

    /**
     * 添加指定的缓存信息到列表右侧(布尔)
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
    public int addBooleanListToListRight(String key, List<Boolean> data);

    /**
     * 添加指定的缓存信息到列表左侧(单精度)
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
    public int addFloatToListLeft(String key, float data);

    /**
     * 添加指定的缓存信息到列表左侧(单精度)
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
    public int addFloatListToListLeft(String key, List<Float> data);

    /**
     * 添加指定的缓存信息到列表右侧(单精度)
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
    public int addFloatToListRight(String key, float data);

    /**
     * 添加指定的缓存信息到列表右侧(单精度)
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
    public int addFloatListToListRight(String key, List<Float> data);

    /**
     * 添加指定的缓存信息到列表左侧(双精度)
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
    public int addDoubleToListLeft(String key, double data);

    /**
     * 添加指定的缓存信息到列表左侧(双精度)
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
    public int addDoubleListToListLeft(String key, List<Double> data);

    /**
     * 添加指定的缓存信息到列表右侧(双精度)
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
    public int addDoubleToListRight(String key, double data);

    /**
     * 添加指定的缓存信息到列表右侧(双精度)
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
    public int addDoubleListToListRight(String key, List<Double> data);

    /**
     * 添加指定的缓存信息到列表左侧(实体)
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
    public <T> int addBeanToListLeft(String key, T data);

    /**
     * 添加指定的缓存信息到列表左侧(实体)
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
    public <T> int addBeanListToListLeft(String key, List<T> data);

    /**
     * 添加指定的缓存信息到列表右侧(实体)
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
    public <T> int addBeanToListRight(String key, T data);

    /**
     * 添加指定的缓存信息到列表右侧(实体)
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
    public <T> int addBeanListToListRight(String key, List<T> data);

    /**
     * 自定义执行器
     *
     * @param executor 自定义执行器
     * @param <T> 可以自定义返回内容，方法会进行返回该值
     * @return 实体类型
     */
    public <T> T execute(RedisExecutor<T> executor);

    /**
     * 自定义redis处理器
     * @param <T> 操作数据
     */
    public static interface RedisExecutor<T> {
        public T execute(Jedis jedis) throws Exception;
    }

}
