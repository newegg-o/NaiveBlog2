package com.xzf.service;

import com.xzf.entity.CommonResult;
import com.xzf.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xzf
 * @since 2021-06-22
 */
public interface UserService extends IService<User> {

    CommonResult login(String username, String password, HttpServletRequest request);

    /**
     * 用户名获取用户
     * @return
     */
    User getUserByUsername(String username);


}
