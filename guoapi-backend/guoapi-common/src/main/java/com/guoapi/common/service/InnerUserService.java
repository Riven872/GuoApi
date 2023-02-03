package com.guoapi.common.service;

import com.guoapi.common.model.entity.User;

/**
 * 用户信息
 *
 * @author rivenzhou
 * @createDate 2023-2-2 14:39:28
 */
public interface InnerUserService {
    /**
     * 查询数据库中是否已经分配给用户密钥
     *
     * @param accessKey
     * @return 返回用户信息
     */
    User getInvokeUser(String accessKey);
}
