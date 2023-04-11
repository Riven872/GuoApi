package com.edu.guoapi.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.guoapi.common.DeleteRequest;
import com.edu.guoapi.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import com.edu.guoapi.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.edu.guoapi.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import com.edu.guoapi.model.entity.UserInterfaceInfo;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author rivenzhou
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
 * @createDate 2023-01-30 18:47:41
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    /**
     * 创建用户接口关系
     *
     * @param userInterfaceInfoAddRequest 用户接口信息
     * @param request                     用户请求
     * @return 用户接口关系表主键
     */
    Long addUserInterfaceInfo(UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request);

    /**
     * 删除
     *
     * @param deleteRequest 删除条件
     * @param request       用户请求
     * @return 是否删除成功
     */
    Boolean deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request);

    /**
     * 更新
     *
     * @param userInterfaceInfoUpdateRequest 更新条件
     * @param request                        用户请求
     * @return 是否更新成功
     */
    Boolean updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest, HttpServletRequest request);

    /**
     * 根据 id 查询用户接口关系
     *
     * @param id 用户接口关系 id
     * @return 用户接口关系信息
     */
    UserInterfaceInfo getUserInterfaceInfoById(long id);

    /**
     * 获取列表
     *
     * @param userInterfaceInfoQueryRequest 列表查询条件
     * @return 用户接口列表信息
     */
    List<UserInterfaceInfo> listUserInterfaceInfo(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);

    /**
     * 分页获取列表
     * @param userInterfaceInfoQueryRequest 分页条件
     * @return 分页列表
     */
    Page<UserInterfaceInfo> listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);

    void validInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    boolean invokeCount(long interfaceInfoId, long userId);
}
