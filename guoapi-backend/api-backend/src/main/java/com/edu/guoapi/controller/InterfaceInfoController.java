package com.edu.guoapi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.guoapi.annotation.AuthCheck;
import com.edu.guoapi.client.GuoApiClient;
import com.edu.guoapi.common.BaseResponse;
import com.edu.guoapi.common.DeleteRequest;
import com.edu.guoapi.common.IdRequest;
import com.edu.guoapi.common.ResultUtils;
import com.edu.guoapi.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.edu.guoapi.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.edu.guoapi.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.edu.guoapi.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.edu.guoapi.model.entity.InterfaceInfo;
import com.edu.guoapi.service.InterfaceInfoService;
import com.edu.guoapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 接口管理
 *
 * @author rivenzhou
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private GuoApiClient guoApiClient;

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        Long res = interfaceInfoService.addInterfaceInfo(interfaceInfoAddRequest, request);
        return ResultUtils.success(res);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        boolean res = interfaceInfoService.deleteInterfaceInfo(deleteRequest, request);
        return ResultUtils.success(res);
    }

    /**
     * 更新
     *
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                                     HttpServletRequest request) {
        boolean res = interfaceInfoService.updateInterfaceInfo(interfaceInfoUpdateRequest, request);
        return ResultUtils.success(res);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        InterfaceInfo res = interfaceInfoService.getInterfaceInfoById(id);
        return ResultUtils.success(res);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        List<InterfaceInfo> res = interfaceInfoService.listInterfaceInfo(interfaceInfoQueryRequest);
        return ResultUtils.success(res);
    }

    /**
     * 分页获取列表
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        Page<InterfaceInfo> res = interfaceInfoService.listInterfaceInfoByPage(interfaceInfoQueryRequest);
        return ResultUtils.success(res);
    }

    // endregion

    /**
     * 发布接口
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        boolean res = interfaceInfoService.onlineInterfaceInfo(idRequest);
        return ResultUtils.success(res);
    }

    /**
     * 下线接口
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        Boolean res = interfaceInfoService.offlineInterfaceInfo(idRequest);
        return ResultUtils.success(res);
    }

    /**
     * 调用测试接口
     *
     * @param interfaceInfoInvokeRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest, HttpServletRequest request) {
        Object res = interfaceInfoService.invokeInterfaceInfo(interfaceInfoInvokeRequest, request);
        return ResultUtils.success(res);
    }
}
