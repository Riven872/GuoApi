package com.edu.guoapi.service.provider;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.edu.guoapi.common.ErrorCode;
import com.edu.guoapi.exception.BusinessException;
import com.edu.guoapi.interfaces.User.RPCUser;
import com.edu.guoapi.model.entity.User;
import com.edu.guoapi.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class RPCUserImpl implements RPCUser {
    @Resource
    private UserService userService;

    /**
     * 查询数据库中是否已经分配密钥给该用户
     *
     * @param accessKey 用户的密钥
     * @return 查询到的用户
     */
    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isBlank(accessKey)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccessKey, accessKey);
        return userService.getOne(queryWrapper);
    }
}
