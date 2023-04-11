package com.edu.guoapi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.guoapi.annotation.AuthCheck;
import com.edu.guoapi.common.BaseResponse;
import com.edu.guoapi.common.DeleteRequest;
import com.edu.guoapi.common.ResultUtils;
import com.edu.guoapi.constant.UserConstant;
import com.edu.guoapi.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import com.edu.guoapi.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.edu.guoapi.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import com.edu.guoapi.model.entity.UserInterfaceInfo;
import com.edu.guoapi.service.UserInterfaceInfoService;
import com.edu.guoapi.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口关系管理
 *
 * @author rivenzhou
 */
@RestController
@RequestMapping("/userInterfaceInfo")
public class UserInterfaceInfoController {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    /**
     * 创建
     *
     * @param userInterfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request) {
        Long res = userInterfaceInfoService.addUserInterfaceInfo(userInterfaceInfoAddRequest, request);
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
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        Boolean res = userInterfaceInfoService.deleteUserInterfaceInfo(deleteRequest, request);
        return ResultUtils.success(res);
    }

    /**
     * 更新
     *
     * @param userInterfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest,HttpServletRequest request) {
        Boolean res = userInterfaceInfoService.updateUserInterfaceInfo(userInterfaceInfoUpdateRequest, request);
        return ResultUtils.success(res);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserInterfaceInfo> getUserInterfaceInfoById(long id) {
        UserInterfaceInfo res = userInterfaceInfoService.getUserInterfaceInfoById(id);
        return ResultUtils.success(res);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<UserInterfaceInfo>> listUserInterfaceInfo(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        List<UserInterfaceInfo> res = userInterfaceInfoService.listUserInterfaceInfo(userInterfaceInfoQueryRequest);
        return ResultUtils.success(res);
    }

    /**
     * 分页获取列表
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        Page<UserInterfaceInfo> res = userInterfaceInfoService.listUserInterfaceInfoByPage(userInterfaceInfoQueryRequest);
        return ResultUtils.success(res);
    }
}
