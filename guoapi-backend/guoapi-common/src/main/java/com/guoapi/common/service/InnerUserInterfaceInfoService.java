package com.guoapi.common.service;

/**
 * 用户接口信息
 *
 * @author rivenzhou
 * @createDate 2023-2-2 14:39:44
 */
public interface InnerUserInterfaceInfoService {
    /**
     * 调用接口后次数加一
     *
     * @param interfaceInfoId 接口信息
     * @param userId          用户信息
     * @return 接口统计是否正常完成
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}
