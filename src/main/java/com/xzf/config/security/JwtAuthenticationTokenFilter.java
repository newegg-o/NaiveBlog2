package com.xzf.config.security;

import com.xzf.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt过滤器
 *
 * @author 呵呵厉害了
 * @date 2021/06/22 19:39
 **/

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = httpServletRequest.getHeader(tokenHeader);
        if(authHeader!=null&& authHeader.startsWith(tokenHead)){
            String authToken =  authHeader.substring(tokenHead.length());
            String username = jwtTokenUtil.getUserNameFromToken(authToken);

            //tokrn存在用户名，但未登录
            if(username != null&& SecurityContextHolder.getContext().getAuthentication()==null){
                //登陆
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //验证token是否有效，重新设置
                if(jwtTokenUtil.validateToken(authToken,userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }
}
