package com.edu.guoapi.interfaces.User;

import com.edu.guoapi.model.entity.User;

public interface RPCUser {
    /**
     *  查询数据库中是否已经分配密钥给该用户
     * @param accessKey 用户的密钥
     * @return 查询到的用户
     */
    User getInvokeUser(String accessKey);
}
