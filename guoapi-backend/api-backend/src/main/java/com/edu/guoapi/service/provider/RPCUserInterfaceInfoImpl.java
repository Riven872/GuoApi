package com.edu.guoapi.service.provider;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.edu.guoapi.common.ErrorCode;
import com.edu.guoapi.exception.BusinessException;
import com.edu.guoapi.interfaces.UserInterfaceInfo.RPCUserInterfaceInfo;
import com.edu.guoapi.model.entity.UserInterfaceInfo;
import com.edu.guoapi.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class RPCUserInterfaceInfoImpl implements RPCUserInterfaceInfo {
    @Resource
    private UserInterfaceInfoService interfaceInfoService;

    /**
     * 调用接口统计次数
     *
     * @param interfaceInfoId 接口 id
     * @param userId          用户 id
     * @return 是否操作成功
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId < 1 || userId < 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaUpdateWrapper<UserInterfaceInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId)
                .eq(UserInterfaceInfo::getUserId, userId)
                .setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return interfaceInfoService.update(updateWrapper);
    }
}
