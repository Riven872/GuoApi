package com.guoapi.common.service;

import com.guoapi.common.model.entity.InterfaceInfo;

/**
 * 接口信息
 *
 * @author rivenzhou
 * @createDate 2023-2-2 14:39:08
 */
public interface InnerInterfaceInfoService {
    /**
     * 查询数据中模拟接口是否存在
     *
     * @param path   请求接口路径
     * @param method 请求接口方法
     * @return 返回接口信息
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}
