package com.edu.guoapi.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.guoapi.common.DeleteRequest;
import com.edu.guoapi.model.dto.user.UserAddRequest;
import com.edu.guoapi.model.dto.user.UserQueryRequest;
import com.edu.guoapi.model.dto.user.UserRegisterRequest;
import com.edu.guoapi.model.dto.user.UserUpdateRequest;
import com.edu.guoapi.model.entity.User;
import com.edu.guoapi.model.vo.UserVO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 *
 * @author guoapi
 */
public interface UserService extends IService<User> {
    /**
     * @param userRegisterRequest 用户登录信息
     * @return 用户主键 id
     */
    Long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    UserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 创建用户
     *
     * @param userAddRequest 用户信息
     * @return 用户 id
     */
    Long addUser(UserAddRequest userAddRequest);

    /**
     * 删除用户
     *
     * @param deleteRequest 删除用户参数
     * @return 是否删除成功
     */
    Boolean deleteUser(@RequestBody DeleteRequest deleteRequest);

    /**
     * 更新用户
     *
     * @param userUpdateRequest 更新用户参数
     * @return 是否更新成功
     */
    Boolean updateUser(@RequestBody UserUpdateRequest userUpdateRequest);

    /**
     * 根据 id 获取用户
     *
     * @param id 用户 id
     * @return 用户信息
     */
    UserVO getUserById(Integer id);

    /**
     * 获取用户列表
     *
     * @param userQueryRequest 用户查询条件
     * @return 用户列表
     */
    List<UserVO> listUser(UserQueryRequest userQueryRequest);

    /**
     * 分页查询用户列表
     * @param userQueryRequest 用户查询条件
     * @return 用户列表
     */
    Page<UserVO> listUserByPage(UserQueryRequest userQueryRequest);
}
