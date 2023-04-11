package com.edu.guoapi.service.provider;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.edu.guoapi.common.ErrorCode;
import com.edu.guoapi.exception.BusinessException;
import com.edu.guoapi.interfaces.InterfaceInfo.RPCInterfaceInfo;
import com.edu.guoapi.model.entity.InterfaceInfo;
import com.edu.guoapi.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class RPCInterfaceInfoImpl implements RPCInterfaceInfo {
    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 查询数据库中调用的接口是否存在
     *
     * @param path   接口路径
     * @param method 接口调用方式
     * @return 接口信息
     */
    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        if (StringUtils.isAnyBlank(path, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该接口不存在");
        }
        LambdaQueryWrapper<InterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InterfaceInfo::getUrl, path)
                .eq(InterfaceInfo::getMethod, method);
        return interfaceInfoService.getOne(queryWrapper);
    }
}
