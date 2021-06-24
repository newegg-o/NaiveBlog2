package com.xzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xzf.Utils.JwtUtil;
import com.xzf.entity.CommonResult;
import com.xzf.entity.User;
import com.xzf.mapper.UserMapper;
import com.xzf.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xzf
 * @since 2021-06-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public CommonResult login(String username, String password, HttpServletRequest request) {
        //登入
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        System.out.println(userDetails.getPassword());
        if (null == userDetails || !passwordEncoder.matches(password, userDetails.getPassword())) {
            return CommonResult.error("用户名不正确");
        }
//        if(!userDetails.isEnabled()){
//            return CommonResult.error("此账号已禁用");
//
//        }

        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //生成token
        String token = jwtUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("tokenHead", tokenHead);
        tokenMap.put("token", token);
        return CommonResult.success("登陆成功", tokenMap);

    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username",username));
    }
}
