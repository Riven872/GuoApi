package com.edu.guoapi.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.guoapi.client.GuoApiClient;
import com.edu.guoapi.common.DeleteRequest;
import com.edu.guoapi.common.ErrorCode;
import com.edu.guoapi.common.IdRequest;
import com.edu.guoapi.constant.CommonConstant;
import com.edu.guoapi.exception.BusinessException;
import com.edu.guoapi.mapper.InterfaceInfoMapper;
import com.edu.guoapi.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.edu.guoapi.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.edu.guoapi.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.edu.guoapi.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.edu.guoapi.model.entity.InterfaceInfo;
import com.edu.guoapi.model.entity.User;
import com.edu.guoapi.model.enums.InterfaceInfoStatusEnum;
import com.edu.guoapi.service.InterfaceInfoService;
import com.edu.guoapi.service.UserService;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Resource
    private UserService userService;

    @Resource
    private GuoApiClient guoApiClient;

    /**
     * 参数校验
     *
     * @param interfaceInfo
     * @param add
     */
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // Long id = interfaceInfo.getId();
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();
        String method = interfaceInfo.getMethod();

        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name, description, url, requestHeader, responseHeader, method)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

    /**
     * 添加接口
     *
     * @param interfaceInfoAddRequest 接口信息
     * @param request                 用户请求
     * @return 接口主键
     */
    @Override
    public Long addInterfaceInfo(InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        // 校验
        this.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = this.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return interfaceInfo.getId();
    }

    /**
     * 删除接口
     *
     * @param deleteRequest 要删除的接口的信息
     * @param request       用户请求
     * @return 是否删除成功
     */
    @Override
    public boolean deleteInterfaceInfo(DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return this.removeById(id);
    }

    /**
     * 更新接口信息
     *
     * @param interfaceInfoUpdateRequest 要更新的接口信息
     * @param request                    用户请求
     * @return 是否更新成功
     */
    @Override
    public boolean updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest, HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        this.validInterfaceInfo(interfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return this.updateById(interfaceInfo);
    }

    /**
     * 根据 id 查询接口信息
     *
     * @param id 接口 id
     * @return 接口信息
     */
    @Override
    public InterfaceInfo getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = this.getById(id);
        return interfaceInfo;
    }

    /**
     * 查询接口信息列表
     *
     * @param interfaceInfoQueryRequest 查询条件
     * @return 接口信息列表
     */
    @Override
    public List<InterfaceInfo> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if (interfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        return this.list(queryWrapper);
    }

    /**
     * 分页获取列表
     *
     * @param interfaceInfoQueryRequest 接口信息查询条件
     * @return
     */
    @Override
    public Page<InterfaceInfo> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        // description 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return this.page(new Page<>(current, size), queryWrapper);
    }

    /**
     * 发布接口
     *
     * @param idRequest 接口 id
     * @return 是否发布成功
     */
    @Override
    public boolean onlineInterfaceInfo(IdRequest idRequest) {
        // todo 全局变量来判定接口是否可以调通，但是有些接口调用不成功返回错误信息是字符串，因此这里要优化判定接口没有调用的方法
        ImmutablePair<Integer, String> res = null;
        // 判断传参是否规范
        if (idRequest == null || idRequest.getId() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long requestId = idRequest.getId();
        // 判断接口是否存在
        InterfaceInfo oldInterfaceInfo = this.getById(requestId);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 判断接口是否处于可调用状态（使用自定义的 SDK）
        //todo 这里进行需要查询出接口的地址，并进行一次调用测试该接口是否处于可调通状态，考虑根据查询出来的 url 动态（或者接口的名称？）的去匹配对应的方法
        if (oldInterfaceInfo.getUrl().contains("randomMessage")) {
            res = guoApiClient.randomMessage(oldInterfaceInfo.getRequestParams());
        }
        if (oldInterfaceInfo.getUrl().contains("test")) {
            // res = guoApiClient.testUrl(oldInterfaceInfo.getRequestParams());
        }

        if (res == null || res.getLeft() != 200) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口处于不可调用状态，请查看日志");
        }

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(requestId);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        return this.updateById(interfaceInfo);
    }

    /**
     * 下线接口
     *
     * @param idRequest 接口名称
     * @return 是否下线成功
     */
    @Override
    public Boolean offlineInterfaceInfo(IdRequest idRequest) {
        // 判断传参是否规范
        if (idRequest == null || idRequest.getId() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long requestId = idRequest.getId();
        // 判断接口是否存在
        InterfaceInfo oldInterfaceInfo = this.getById(requestId);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(requestId);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        return this.updateById(interfaceInfo);
    }

    /**
     * 调用测试接口
     *
     * @param interfaceInfoInvokeRequest 接口信息
     * @param request                    用户请求
     * @return 远程接口返回值
     */
    @Override
    @Transactional
    public Object invokeInterfaceInfo(InterfaceInfoInvokeRequest interfaceInfoInvokeRequest, HttpServletRequest request) {
        // todo 全局变量来判定接口是否可以调通，但是有些接口调用不成功返回错误信息是字符串，因此这里要优化判定接口没有调用的方法
        ImmutablePair<Integer, String> res = null;
        // 判断传参是否规范
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = interfaceInfoInvokeRequest.getId();
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
        // 判断接口是否存在
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 判断接口是否开启
        if (oldInterfaceInfo.getStatus() != InterfaceInfoStatusEnum.ONLINE.getValue()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已关闭");
        }
        // 解析用户的 key 和传进来的参数
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();

        GuoApiClient client = new GuoApiClient(accessKey, secretKey);
        try {
            if (oldInterfaceInfo.getUrl().contains("randomMessage")) {
                res = client.randomMessage(userRequestParams);
            }
            if (oldInterfaceInfo.getUrl().contains("test")){
                // res = client.testUrl(userRequestParams);
            }
        } catch (JsonSyntaxException exception) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不为 Json 格式");
        }
        if (res == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口错误，请联系管理员");
        }
        if (res.getLeft() != 200){
            throw new BusinessException(res.getLeft(), res.getRight());
        }
        return res.getRight();
    }
}




