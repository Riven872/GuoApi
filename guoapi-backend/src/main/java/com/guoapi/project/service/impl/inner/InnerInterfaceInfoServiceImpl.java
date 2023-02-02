package com.guoapi.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guoapi.common.model.entity.InterfaceInfo;
import com.guoapi.common.service.InnerInterfaceInfoService;
import com.guoapi.project.common.ErrorCode;
import com.guoapi.project.exception.BusinessException;
import com.guoapi.project.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    /**
     * 查询数据中模拟接口是否存在
     *
     * @param path   请求接口路径
     * @param method 请求接口方法
     * @return
     */
    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        if (StringUtils.isAnyBlank(path, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<InterfaceInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterfaceInfo::getUrl, path)
                .eq(InterfaceInfo::getMethod, method);

        return interfaceInfoMapper.selectOne(wrapper);
    }
}
