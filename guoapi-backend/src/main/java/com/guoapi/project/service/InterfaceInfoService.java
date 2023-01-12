package com.guoapi.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guoapi.project.model.entity.InterfaceInfo;

/**
 *
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    //参数校验
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);
}
