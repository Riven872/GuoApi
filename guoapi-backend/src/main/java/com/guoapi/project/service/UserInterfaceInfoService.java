package com.guoapi.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.guoapi.common.model.entity.UserInterfaceInfo;

/**
* @author rivenzhou
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-01-30 18:47:41
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    boolean invokeCount(long interfaceInfoId, long userId);
}
