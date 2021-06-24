package com.xzf.controller;

import com.xzf.entity.CommonResult;
import com.xzf.entity.LoginParam;
import com.xzf.entity.User;
import com.xzf.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * 登陆控制器
 *
 * @author 呵呵厉害了
 * @date 2021/06/22 19:07
 **/
@RestController
@Api(tags = "LoginController")
@Accessors
public class LoginController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "登入之后返回token")
    @PostMapping("/login")
    public CommonResult login(@RequestBody LoginParam loginParam, HttpServletRequest request) {
        return userService.login(loginParam.getUsername(),
                loginParam.getPassword(),request);

    }

    @ApiOperation(value = "退出登陆")
    @PostMapping("/logout")
    public CommonResult logout() {
        return CommonResult.success("注销成功");
    }

    @ApiOperation(value = "获取当前用户登陆信息")
    @GetMapping("/user/info")
    public User getAdminInfo(Principal principal) {

        if (principal == null) {
            return null;
        }
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        //设置密码为空
        user.setPassword(null);
//        admin.setRoles(adminService.getRoles(admin.getId()));
        return user;
    }


}
