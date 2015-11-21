package com.easydata.cache.memcached;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.easydata.cache.ICacheManager;
import com.easydata.core.utils.AnnotationBean;
import com.easydata.core.utils.AnnotationBeanUtil;
import com.easydata.core.utils.StringUtil;
import net.rubyeye.xmemcached.*;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.TextCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;
import net.rubyeye.xmemcached.transcoders.Transcoder;
import net.rubyeye.xmemcached.utils.AddrUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.List;

import static com.easydata.core.constants.Constants.CACHE_EXP_FOREVER;

/**
 * MemCache缓存管理器。<br />
 *
 * @author Mr.Pro
 */
public class MemCachedManager implements ICacheManager {

    private static final Log logger = LogFactory.getLog(MemCachedManager.class);

    private MemcachedClientBuilder builder;

    //协议工厂,默认使用TextCommandFactory
    private CommandFactory commandFactory=new TextCommandFactory();

    //分布策略,默认使用KetamaMemcachedSessionLocator
    private MemcachedSessionLocator sessionLocator=new KetamaMemcachedSessionLocator();

    //序列化转换器，默认使用SerializingTranscoder
    @SuppressWarnings("rawtypes")
    private Transcoder transcoder=new SerializingTranscoder();

    private MemcachedClient memcachedClient;

    /**
     * <p>Constructor for MemCachedManager.</p>
     *
     * @param servers a {@link String} object.
     * @param poolSize a int.
     * @param authInfos a {@link String} object.
     */
    public MemCachedManager(String servers, int poolSize, String authInfos){
        try{
            List<String> serverList= StringUtil.splitToList(",", authInfos);
            List<String> authInfoList= StringUtil.splitToList(",", authInfos);
            this.builder=new XMemcachedClientBuilder(servers);
            for(int i=0;i<serverList.size();i++){
                String server=serverList.get(i);
                if(!authInfoList.get(i).equals("null")){
                    List<String> nameAndPwd= StringUtil.splitToList(":", authInfoList.get(i));
                    String userName=nameAndPwd.get(0);
                    String pwd=nameAndPwd.get(1);
                    this.builder.addAuthInfo(AddrUtil.getOneAddress(server), AuthInfo.typical(userName, pwd));
                }

            }
            builder.setConnectionPoolSize(poolSize);
            builder.setCommandFactory(commandFactory);
            builder.setSessionLocator(sessionLocator);
            builder.setTranscoder(transcoder);
            memcachedClient=builder.build();
        }catch(Exception e){
            logger.error("memcached初始化失败",e);
        }
    }

    public int setString(String key, String data) {
        return setString(key, data, CACHE_EXP_FOREVER);
    }

    public int setStringList(String key, List<String> data) {
        return setBasicDataList(key, data, CACHE_EXP_FOREVER);
    }

    public int setString(String key, String data, int exp) {
        if (StringUtil.isEmpty(key) || StringUtil.isEmpty(data)) {
            logger.error("进行设置缓存信息时名称和值不能为空");
            return PARAMETER_MISSING;
        }
        try {
            memcachedClient.set(key, exp, data);
        } catch (Exception e) {
            logger.error("设置缓存失败！", e);
            return ADD_FAILED;
        }
        return SUCCESS;
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
        // 将数据转换为json形式
        JSONArray dataArray = new JSONArray(beanList.size());
        for (T t : beanList) {
            AnnotationBean bean = AnnotationBeanUtil.parseBean(t);
            dataArray.add(bean.toJSONObject(true));
        }

        return setString(key, dataArray.toJSONString(), exp);
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
        if(StringUtil.isEmpty(key)){
            logger.error("进行获取缓存信息中的主键不能为空");
            return null;
        }
        try {
            String value = memcachedClient.get(key);
            return value;
        } catch (Exception e) {
            logger.error("获取缓存信息失败！", e);
            return null;
        }
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
        String cacheValue = getString(key, null);
        if (StringUtil.isEmpty(cacheValue)) {
            logger.error(new StringBuilder().append("不存在指定的key(").append(key).append(")或者该key的值为空"));
            return defaultValue;
        }
        AnnotationBean bean = AnnotationBeanUtil.parseBasicBeanByClass(cls);
        try {
            return bean.parseData(JSONArray.parseArray(cacheValue));
        } catch (Exception e) {
            logger.error(new StringBuilder().append("未能成功对当前的数据结果(").append(cacheValue).append(")转换为实体列表"), e);
            return defaultValue;
        }
    }

    public int remove(String key) {
        if(StringUtil.isEmpty(key)){
            logger.error("进行获取缓存信息时主键不能为空");
            return PARAMETER_MISSING;
        }
        try {
            memcachedClient.delete(key);
        } catch (Exception e) {
            logger.error("删除缓存信息失败！", e);
            return DELETE_FAILED;
        }
        return SUCCESS;
    }

    /**
     * <p>clearCache.</p>
     *
     * @return a int.
     */
    public int clearCache() {
        try{
            memcachedClient.flushAll();
        }catch(Exception e){
            logger.error("清空所有缓存失败", e);
            return CLEAR_CACHE_FAILED;
        }
        return SUCCESS;
    }

    public boolean exists(String key) {
        if(StringUtil.isEmpty(key)){
            logger.error("进行判断缓存信息时主键不能为空");
            return false;
        }
        try {
            return !memcachedClient.add(key, 1, "");
        } catch (Exception e) {
            logger.error("获取缓存信息失败", e);
            return false;
        }
    }

    private <T extends Object> int setBasicDataList(String key, List<T> list, int exp) {
        if (StringUtil.isEmpty(key) || list == null) {
            logger.error("进行设置缓存信息时中的名称或者值不能为空");
            return PARAMETER_MISSING;
        }

        JSONArray array = new JSONArray();
        array.addAll(list);
        return setString(key, array.toJSONString(), exp);
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
        String dataArray = getString(key);
        if (StringUtil.isEmpty(dataArray)) {
            return defaultValue;
        }
        try {
            return JSON.parseArray(dataArray, cls);
        } catch (Exception e) {
            logger.error(new StringBuilder().append("进行数据转换时出错，请确认指定key(").append(key).append(")确实为").append(cls.getSimpleName()).append("列表类型"));
        }
        return defaultValue;
    }

    public boolean isConnectSuccess() {
        try {
            memcachedClient.getStats();
            return true;
        } catch (Exception e) {
            logger.error("未能成功建立与Memcache的链接", e);
        }
        return false;
    }
}
