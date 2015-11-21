package com.easydata.core.manager;

/**
 * 检测是否可以正常的链接到数据源<br />
 *
 * @author Mr.Pro
 */
public interface Connectable {

    /**
     * 检测是否链接正常
     * @return true代表链接成功，否则为false
     */
    public boolean isConnectSuccess();
}
