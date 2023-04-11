package com.edu.guoapi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.guoapi.common.DeleteRequest;
import com.edu.guoapi.common.IdRequest;
import com.edu.guoapi.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.edu.guoapi.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.edu.guoapi.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.edu.guoapi.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.edu.guoapi.model.entity.InterfaceInfo;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    //参数校验
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);

    /**
     * 添加接口
     *
     * @param interfaceInfoAddRequest 接口信息
     * @param request                 用户请求
     * @return 接口主键
     */
    Long addInterfaceInfo(InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request);

    /**
     * 删除接口
     *
     * @param deleteRequest 要删除的接口的信息
     * @param request       用户请求
     * @return 是否删除成功
     */
    boolean deleteInterfaceInfo(DeleteRequest deleteRequest, HttpServletRequest request);

    /**
     * 更新接口信息
     *
     * @param interfaceInfoUpdateRequest 要更新的接口信息
     * @param request                    用户请求
     * @return 是否更新成功
     */
    boolean updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest, HttpServletRequest request);

    /**
     * 根据 id 查询接口信息
     *
     * @param id 接口 id
     * @return 接口信息
     */
    InterfaceInfo getInterfaceInfoById(long id);

    /**
     * 查询接口信息列表
     *
     * @param interfaceInfoQueryRequest 查询条件
     * @return 接口信息列表
     */
    List<InterfaceInfo> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    /**
     * 分页获取列表
     *
     * @param interfaceInfoQueryRequest 接口信息查询条件
     * @return 分页之后的列表信息
     */
    Page<InterfaceInfo> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    /**
     * 发布接口
     *
     * @param idRequest 接口 id
     * @return 是否发布成功
     */
    boolean onlineInterfaceInfo(IdRequest idRequest);

    /**
     * 下线接口
     *
     * @param idRequest 接口名称
     * @return 是否下线成功
     */
    Boolean offlineInterfaceInfo(IdRequest idRequest);

    /**
     * 调用测试接口
     * @param interfaceInfoInvokeRequest 接口信息
     * @param request 用户请求
     * @return 远程接口返回值
     */
    Object invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest, HttpServletRequest request);
}
