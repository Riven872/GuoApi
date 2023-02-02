package com.guoapi.project.service.impl.inner;

import com.guoapi.common.service.InnerUserInterfaceInfoService;
import com.guoapi.project.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 增加调用次数
     *
     * @param interfaceInfoId 调用接口id
     * @param userId          调用者id
     * @return 调用次数是否更新
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }
}
