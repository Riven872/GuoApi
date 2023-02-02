package com.guoapi.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guoapi.common.model.entity.User;
import com.guoapi.common.service.InnerUserService;
import com.guoapi.project.common.ErrorCode;
import com.guoapi.project.exception.BusinessException;
import com.guoapi.project.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {
    @Resource
    private UserMapper userMapper;

    /**
     * 查询数据库中是否已经分配给用户密钥
     *
     * @param accessKey
     * @param secretKey
     * @return 返回用户信息
     */
    @Override
    public User getInvokeUser(String accessKey, String secretKey) {
        if (StringUtils.isAnyBlank(accessKey, secretKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAccessKey, accessKey)
                .eq(User::getSecretKey, secretKey);

        return userMapper.selectOne(wrapper);
    }
}
