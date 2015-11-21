package com.easydata.cache.redis;

import com.easydata.cache.IRedisCacheManager;
import com.easydata.core.query.datatype.DataType;
import com.easydata.core.query.datatype.impl.DefaultDataTypeAcceptor;
import com.easydata.core.utils.AnnotationBean;
import com.easydata.core.utils.AnnotationBeanUtil;
import com.easydata.core.utils.StringUtil;
import com.google.common.collect.ImmutableList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.easydata.core.constants.Constants.CACHE_EXP_FOREVER;

/**
 * Redis版本的缓存实现。<br />
 *
 * @author Mr.Pro
 */
public class RedisManager implements IRedisCacheManager {

    private static Log logger = LogFactory.getLog(RedisManager.class);

    private ThreadLocal<JedisWapper> threadLocal = new ThreadLocal<JedisWapper>();

    private JedisPool pool;

    private DefaultDataTypeAcceptor defaultDataTypeAcceptor = new DefaultDataTypeAcceptor();

    public RedisManager(String host, int port) {
        this(new JedisPoolConfig(), host, port);
    }

    public RedisManager(String host, int port, int timeout) {
        this(new JedisPoolConfig(), host, port, null, null, 0, timeout);
    }

    public RedisManager(JedisPoolConfig poolConfig, String host, int port) {
        this(poolConfig, host, port, null, null, 0, 2000);
    }

    public RedisManager(JedisPoolConfig poolConfig, String host, int port, String clientName, String password, int database, int timeout) {
        this.pool = new JedisPool(poolConfig, host, port, timeout, password, database, clientName);
    }

    public int setString(String key, String data) {
        return setString(key, data, CACHE_EXP_FOREVER);
    }

    public int setStringList(String key, List<String> data) {
        return setStringList(key, data, CACHE_EXP_FOREVER);
    }

    public int setString(String key, String data, int exp) {
        if (StringUtil.isEmpty(key) || StringUtil.isEmpty(data)) {
            logger.error("进行设置缓存信息时中的名称或者值不能喂空");
            return PARAMETER_MISSING;
        }

        JedisWapper jedisWapper = null;
        try {
            jedisWapper = getJedis();

            jedisWapper.getJedis().set(key, data);
            if (exp != CACHE_EXP_FOREVER) {
                jedisWapper.getJedis().expire(key, exp);
            }
        } catch (Exception e) {
            logger.error("设置缓存信息失败", e);
            return ADD_FAILED;
        } finally {
            if (jedisWapper != null)
                jedisWapper.releaseFromPool(pool);
        }
        return SUCCESS;
    }

    private <T extends Object> int setBasicDataList(String key, List<T> list, int exp) {
        if (StringUtil.isEmpty(key) || list == null) {
            logger.error("进行设置缓存信息时中的名称或者值不能为空");
            return PARAMETER_MISSING;
        }

        JedisWapper jedisWapper = null;
        try {
            jedisWapper = getJedis();

            Jedis jedis = jedisWapper.getJedis();
            jedis.del(key);

            for (T data : list) {
                jedis.rpush(key, String.valueOf(data));
            }

            jedis.expire(key, exp);
        } catch (Exception e) {
            logger.error("设置缓存信息失败", e);
            return ADD_FAILED;
        } finally {
            if (jedisWapper != null)
                jedisWapper.releaseFromPool(pool);
        }

        return SUCCESS;
    }

    private <T extends Object> int addBasicDataList(String key, List<T> list, boolean left) {
        if (StringUtil.isEmpty(key) || list == null) {
            logger.error("进行添加缓存信息时候名称或者值不能为空");
            return PARAMETER_MISSING;
        }

        JedisWapper jedisWapper = null;
        try {
            jedisWapper = getJedis();

            String[] listArray = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                listArray[i] = String.valueOf(list.get(i));
            }

            if (left) {
                jedisWapper.getJedis().lpush(key, listArray);
            } else {
                jedisWapper.getJedis().rpush(key, listArray);
            }
        } catch (Exception e) {
            logger.error("添加缓存信息失败", e);
            return ADD_FAILED;
        } finally {
            if (jedisWapper != null) {
                jedisWapper.releaseFromPool(pool);
            }
        }

        return SUCCESS;
    }

    private <T extends Object> int addBeanDataList(String key, List<T> list, boolean left) {
        if (StringUtil.isEmpty(key) || list == null) {
            logger.error("进行添加缓存信息时候名称或者值不能为空");
            return PARAMETER_MISSING;
        }

        JedisWapper jedisWapper = null;
        try {
            jedisWapper = getJedis();

            List <String> beanStringList = new ArrayList<String>(list.size());
            for (T data : list) {
                if (data == null) {
                    beanStringList.add("null");
                } else {
                    beanStringList.add(AnnotationBeanUtil.parseBean(data).toJSONString(true));
                }
            }

            return addBasicDataList(key, beanStringList, left);
        } catch (Exception e) {
            logger.error("添加缓存信息失败", e);
            return ADD_FAILED;
        } finally {
            if (jedisWapper != null) {
                jedisWapper.releaseFromPool(pool);
            }
        }

    }

    /**
     * <p>getBasicDataList.</p>
     *
     * @param key a {@link String} object.
     * @param cls a {@link Class} object.
     * @param defaultValue a {@link List} object.
     * @param <T> a T object.
     * @return a {@link List} object.
     */
    public <T extends Object> List<T> getBasicDataList(String key, Class<T> cls, List<T> defaultValue) {
        if (StringUtil.isEmpty(key) || cls == null) {
            logger.error("进行获取缓存信息时中的名称不能为空");
            return defaultValue;
        }

        // 不存在数据
        JedisWapper jedisWapper = null;
        try {
            jedisWapper = getJedis();

            Jedis jedis = jedisWapper.getJedis();
            List<String> list = jedis.lrange(key, 0, -1);
            if (list == null)
                return defaultValue;

            List<T> willReturn = new ArrayList<T>(list.size());
            DataType dataType = DataType.parseDataTypeFromClass(cls);

            for (String data : list) {
                if (StringUtil.isEmpty(data) || "null".equals(data)) {
                    willReturn.add(null);
                } else {
                    willReturn.add((T) defaultDataTypeAcceptor.accept(dataType, data, null));
                }
            }
            return willReturn;
        } catch (Exception e) {
            logger.error("未能成功进行获取缓存信息！", e);
            return defaultValue;
        } finally {
            if (jedisWapper != null) {
                jedisWapper.releaseFromPool(pool);
            }
        }

//        String dataArray = getString(key);
//        if (StringUtil.isEmpty(dataArray)) {
//            return defaultValue;
//        }
//        try {
//
//            return JSON.parseArray(dataArray, cls);
//        } catch (Exception e) {
//            logger.error(new StringBuilder().append("进行数据转换时出错，请确认指定key(").append(key).append(")确实为").append(cls.getSimpleName()).append("列表类型"));
//        }
//        return defaultValue;
    }

    public int setStringList(String key, List<String> data, int exp) {
        return setBasicDataList(key, data, exp);
    }

    public int setInt(String key, int data) {
        return setString(key, String.valueOf(data));
    }

    public int setIntList(String key, List<Integer> data) {
        return setIntList(key, data, CACHE_EXP_FOREVER);
    }

    public int setInt(String key, int data, int exp) {
        return setString(key, String.valueOf(data), exp);
    }

    public int setIntList(String key, List<Integer> data, int exp) {
        return setBasicDataList(key, data, exp);
    }

    public int setLong(String key, long data) {
        return setString(key, String.valueOf(data));
    }

    public int setLongList(String key, List<Long> data) {
        return setLongList(key, data, CACHE_EXP_FOREVER);
    }

    public int setLong(String key, long data, int exp) {
        return setString(key, String.valueOf(data), exp);
    }

    public int setLongList(String key, List<Long> data, int exp) {
        return setBasicDataList(key, data, exp);
    }

    public int setBoolean(String key, boolean data) {
        return setString(key, String.valueOf(data));
    }

    public int setBooleanList(String key, List<Boolean> data) {
        return setBooleanList(key, data, CACHE_EXP_FOREVER);
    }

    public int setBoolean(String key, boolean data, int exp) {
        return setString(key, String.valueOf(data), exp);
    }

    public int setBooleanList(String key, List<Boolean> data, int exp) {
        return setBasicDataList(key, data, exp);
    }

    public int setFloat(String key, float data) {
        return setString(key, String.valueOf(data));
    }

    public int setFloatList(String key, List<Float> data) {
        return setFloatList(key, data, CACHE_EXP_FOREVER);
    }

    public int setFloat(String key, float data, int exp) {
        return setString(key, String.valueOf(data), exp);
    }

    public int setFloatList(String key, List<Float> data, int exp) {
        return setBasicDataList(key, data, exp);
    }

    public int setDouble(String key, double data) {
        return setString(key, String.valueOf(data));
    }

    public int setDoubleList(String key, List<Double> data) {
        return setDoubleList(key, data, CACHE_EXP_FOREVER);
    }

    public int setDouble(String key, double data, int exp) {
        return setString(key, String.valueOf(data), exp);
    }

    public int setDoubleList(String key, List<Double> data, int exp) {
        return setBasicDataList(key, data, exp);
    }

    public int setBean(Object beanData) {
        return setBean(beanData, CACHE_EXP_FOREVER);
    }

    public <T> int setBeanListToSingleKey(String key, List<T> beanList) {
        return setBeanListToSingleKey(key, beanList, CACHE_EXP_FOREVER);
    }

    public <T> int setBeanListToSingleKey(String key, List<T> beanList, int exp) {
        if (StringUtil.isEmpty(key) || beanList == null || beanList.size() <= 0) {
            logger.error("进行设置缓存信息中的主键和实体列表不能为空");
            return PARAMETER_MISSING;
        }

        List<String> beanStringList = null;
        try {
            beanStringList = new ArrayList<String>(beanList.size());
            for (T data : beanList) {
                if (data == null) {
                    beanStringList.add("null");
                } else {
                    beanStringList.add(AnnotationBeanUtil.parseBean(data).toJSONString(true));
                }
            }
        } catch (Exception e) {
            logger.error("进行转换数据内容信息失败！", e);
            return ADD_FAILED;
        }


        return setStringList(key, beanStringList, exp);
    }

    public int setBean(Object beanData, int exp) {
        AnnotationBean bean = AnnotationBeanUtil.parseBean(beanData);
        String willKey = bean.toIdString();
        String willValue = bean.toJSONString(true);
        if (StringUtil.isEmpty(willKey) || StringUtil.isEmpty(willValue)) {
            logger.error("当前的数据类("+bean.getClass()+")未能成功进行解析操作！请确认类信息！");
            return ADD_FAILED;
        }
        return setString(willKey, willValue, exp);
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public List<String> getStringList(String key) {
        return getBasicDataList(key, String.class, null);
    }

    public String getString(String key, String defaultValue) {
        if (StringUtil.isEmpty(key)) {
            logger.error("进行获取缓存信息时中的名称不能为空");
            return defaultValue;
        }
        JedisWapper jedisWapper = null;
        try {
            jedisWapper = getJedis();
            String val = jedisWapper.getJedis().get(key);
            return StringUtil.isEmpty(val, defaultValue);
        } catch (Exception e) {
            logger.error("未能成功进行获取缓存信息！", e);
        } finally {
            if (jedisWapper != null) {
                jedisWapper.releaseFromPool(pool);
            }
        }
        return defaultValue;
    }

    public List<String> getStringList(String key, List<String> defaultValue) {
        return getBasicDataList(key, String.class, defaultValue);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public List<Integer> getIntList(String key) {
        return getBasicDataList(key, Integer.class, null);
    }

    public int getInt(String key, int defaultValue) {
        String stringVal = getString(key, String.valueOf(defaultValue));
        try {
            return Integer.valueOf(stringVal);
        } catch (NumberFormatException e) {
            logger.error(new StringBuilder().append("未能成功对当前数据结果(").append(stringVal).append(")转换为数字！"), e);
            return defaultValue;
        }
    }

    public List<Integer> getIntList(String key, List<Integer> defaultValue) {
        return getBasicDataList(key, Integer.class, defaultValue);
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public List<Long> getLongList(String key) {
        return getBasicDataList(key, Long.class, null);
    }

    public long getLong(String key, long defaultValue) {
        String stringVal = getString(key, String.valueOf(defaultValue));
        try {
            return Long.valueOf(stringVal);
        } catch (NumberFormatException e) {
            logger.error(new StringBuilder().append("未能成功对当前数据结果(").append(stringVal).append(")转换为数字！"), e);
            return defaultValue;
        }
    }

    public List<Long> getLongList(String key, List<Long> defaultValue) {
        return getBasicDataList(key, Long.class, defaultValue);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public List<Boolean> getBooleanList(String key) {
        return getBasicDataList(key, Boolean.class, null);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String stringVal = getString(key, String.valueOf(defaultValue));
        try {
            return Boolean.valueOf(stringVal);
        } catch (NumberFormatException e) {
            logger.error(new StringBuilder().append("未能成功对当前数据结果(").append(stringVal).append(")转换为布尔！"), e);
            return defaultValue;
        }
    }

    public List<Boolean> getBooleanList(String key, List<Boolean> defaultValue) {
        return getBasicDataList(key, Boolean.class, defaultValue);
    }

    /**
     * <p>getBean.</p>
     *
     * @param beanClass a {@link Class} object.
     * @param idValue a {@link Object} object.
     * @param <T> a T object.
     * @return a T object.
     */
    public <T> T getBean(Class<T> beanClass, Object... idValue) {
        return getBean(beanClass, null, idValue);
    }

    /**
     * <p>getBean.</p>
     *
     * @param beanClass a {@link Class} object.
     * @param defaultValue a T object.
     * @param idValue a {@link Object} object.
     * @param <T> a T object.
     * @return a T object.
     */
    public <T> T getBean(Class<T> beanClass, T defaultValue, Object... idValue) {
        if (beanClass == null || idValue == null || idValue.length <= 0) {
            logger.error("进行获取缓存信息时中的数据类型和id值不能为空");
            return defaultValue;
        }

        // 先进行构建key
        AnnotationBean bean = AnnotationBeanUtil.parseBasicBeanByClass(beanClass);
        if (!bean.getHasIdField()) {
            logger.error(new StringBuilder().append("当前实体类(").append(beanClass).append(")不存在包含ID域，请确定该类包含@Id注解"));
            return defaultValue;
        }
        bean.setIdFiledValues(Arrays.asList(idValue));
        bean.setHasSetValues(true);

        // 获取该key的值
        String cacheKey = bean.toIdString();
        String cacheValue = getString(cacheKey);
        if (StringUtil.isEmpty(cacheValue)) {
            return defaultValue;
        } else {
            // 将其转换为bean信息
            try {
                T data = bean.parseData(cacheValue);
                return data;
            } catch (Exception e) {
                logger.error("未能成功将缓存中的数据转换成实体数据类型，数据如下："+ cacheValue, e);
                return defaultValue;
            }
        }
    }

    public <T> List<T> getBeanListBySingleKey(String key, Class<T> cls, List<T> defaultValue) {
        if (StringUtil.isEmpty(key) || cls == null) {
            logger.error("进行获取缓存信息时主键和实体类型不能为空");
            return defaultValue;
        }

        // 不存在数据
        JedisWapper jedisWapper = null;
        try {
            jedisWapper = getJedis();

            Jedis jedis = jedisWapper.getJedis();
            List<String> list = jedis.lrange(key, 0, -1);
            if (list == null)
                return defaultValue;

            List<T> willReturn = new ArrayList<T>(list.size());

            AnnotationBean bean = AnnotationBeanUtil.parseBasicBeanByClass(cls);
            for (String data : list) {
                if (StringUtil.isEmpty(data) || "null".equals(data)) {
                    willReturn.add(null);
                } else {
                    willReturn.add(bean.<T>parseData(data));
                }
            }
            return willReturn;
        } catch (Exception e) {
            logger.error("未能成功进行获取缓存信息！", e);
            return defaultValue;
        } finally {
            if (jedisWapper != null) {
                jedisWapper.releaseFromPool(pool);
            }
        }
    }

    public int remove(String key) {
        if (StringUtil.isEmpty(key)) {
            logger.error("进行删除缓存信息的时候，主键不能为空");
            return PARAMETER_MISSING;
        }
        JedisWapper jedisWapper = null;
        try {
            jedisWapper = getJedis();
            jedisWapper.getJedis().del(key);
        } catch (Exception e) {
            logger.error("进行删除缓存信息失败！", e);
            return DELETE_FAILED;
        } finally {
            if (jedisWapper != null)
                jedisWapper.releaseFromPool(pool);
        }
        return SUCCESS;
    }

    public int clearCache() {
        JedisWapper jedisWapper = null;
        try {
            jedisWapper = getJedis();
            jedisWapper.getJedis().flushDB();
        } catch (Exception e) {
            logger.error("进行清空缓存信息失败！", e);
            return CLEAR_CACHE_FAILED;
        } finally {
            if (jedisWapper != null) {
                jedisWapper.releaseFromPool(pool);
            }
        }
        return SUCCESS;
    }

    public boolean exists(String key) {
        JedisWapper jedisWapper = null;
        try {
            jedisWapper = getJedis();
            return jedisWapper.getJedis().exists(key);
        } catch (Exception e) {
            logger.error("进行判断是否存在指定key失败！", e);
            return false;
        } finally {
            if (jedisWapper != null) {
                jedisWapper.releaseFromPool(pool);
            }
        }
    }

    /**
     * 获取Jedis
     * @return
     */
    private JedisWapper getJedis(){
        JedisWapper jedis = threadLocal.get();
        if (jedis == null) {
            jedis = new JedisWapper(pool.getResource(), true);
        }
        return jedis;
    }

    public void startSession() {
        try {
            JedisWapper wapper = new JedisWapper(pool.getResource(), false);
            threadLocal.set(wapper);
        } catch (Exception e) { }
    }

    public void endSession() {
        try {
            threadLocal.remove();
        } catch (Exception e) { }
    }

    public int addStringToListLeft(String key, String data) {
        List<String> dataList = new ArrayList<String>(1);
        dataList.add(data);
        return addStringListToListLeft(key, dataList);
    }

    public int addStringListToListLeft(String key, List<String> data) {
        return addBasicDataList(key, data, true);
    }

    public int addStringToListRight(String key, String data) {
        List<String> dataList = new ArrayList<String>(1);
        dataList.add(data);
        return addStringListToListRight(key, dataList);
    }

    public int addStringListToListRight(String key, List<String> data) {
        return addBasicDataList(key, data, false);
    }

    public int addIntToListLeft(String key, int data) {
        return addIntListToListLeft(key, ImmutableList.of(data));
    }

    public int addIntListToListLeft(String key, List<Integer> data) {
        return addBasicDataList(key, data, true);
    }

    public int addIntToListRight(String key, int data) {
        return addIntListToListRight(key, ImmutableList.of(data));
    }

    public int addIntListToListRight(String key, List<Integer> data) {
        return addBasicDataList(key, data, false);
    }

    public int addLongToListLeft(String key, long data) {
        return addLongListToListLeft(key, ImmutableList.of(data));
    }

    public int addLongListToListLeft(String key, List<Long> data) {
        return addBasicDataList(key, data, true);
    }

    public int addLongToListRight(String key, long data) {
        return addLongListToListRight(key, ImmutableList.of(data));
    }

    public int addLongListToListRight(String key, List<Long> data) {
        return addBasicDataList(key, data, false);
    }

    public int addBooleanToListLeft(String key, boolean data) {
        return addBooleanListToListLeft(key, ImmutableList.of(data));
    }

    public int addBooleanListToListLeft(String key, List<Boolean> data) {
        return addBasicDataList(key, data, true);
    }

    public int addBooleanToListRight(String key, boolean data) {
        return addBooleanListToListRight(key, ImmutableList.of(data));
    }

    public int addBooleanListToListRight(String key, List<Boolean> data) {
        return addBasicDataList(key, data, false);
    }

    public int addFloatToListLeft(String key, float data) {
        return addFloatListToListLeft(key, ImmutableList.of(data));
    }

    public int addFloatListToListLeft(String key, List<Float> data) {
        return addBasicDataList(key, data, true);
    }

    public int addFloatToListRight(String key, float data) {
        return addFloatListToListRight(key, ImmutableList.of(data));
    }

    public int addFloatListToListRight(String key, List<Float> data) {
        return addBasicDataList(key, data, false);
    }

    public int addDoubleToListLeft(String key, double data) {
        return addDoubleListToListLeft(key, ImmutableList.of(data));
    }

    public int addDoubleListToListLeft(String key, List<Double> data) {
        return addBasicDataList(key, data, true);
    }

    public int addDoubleToListRight(String key, double data) {
        return addDoubleListToListRight(key, ImmutableList.of(data));
    }

    public int addDoubleListToListRight(String key, List<Double> data) {
        return addBasicDataList(key, data, false);
    }

    public <T> int addBeanToListLeft(String key, T data) {
        ArrayList<T> dataList = new ArrayList<T>(1);
        dataList.add(data);
        return addBeanListToListLeft(key, dataList);
    }

    public <T> int addBeanListToListLeft(String key, List<T> data) {
        return addBeanDataList(key, data, true);
    }

    public <T> int addBeanToListRight(String key, T data) {
        ArrayList<T> dataList = new ArrayList<T>(1);
        dataList.add(data);
        return addBeanListToListRight(key, dataList);
    }

    public <T> int addBeanListToListRight(String key, List<T> data) {
        return addBeanDataList(key, data, false);
    }

    public <T> T execute(RedisExecutor<T> executor) {
        JedisWapper wapper = null;
        try {
            wapper = getJedis();
            T result = executor.execute(wapper.getJedis());
            return result;
        } catch (Exception e) {
            logger.error("制定自定义处理器失败", e);
            return null;
        } finally {
            if (wapper != null)
                wapper.releaseFromPool(pool);
        }
    }

    public boolean isConnectSuccess() {
        JedisWapper wapper = null;
        try {
            wapper = getJedis();
            return wapper.getJedis().isConnected() && wapper.getJedis().ping().equals("PONG");
        } catch (Exception e) {
            logger.error("未能成功建立与Redis的链接", e);
            return false;
        } finally {
            if (wapper != null)
                wapper.releaseFromPool(pool);
        }
    }
}
