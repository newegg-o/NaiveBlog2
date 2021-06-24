package com.xzf.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzf.entity.CommonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 呵呵厉害了
 * @date 2021/06/23 20:12
 **/
@Component
public class RestAuthorizationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json");
        PrintWriter out = httpServletResponse.getWriter();
        CommonResult bean = CommonResult.error("未登录，请登录");
        bean.setCode(401);
        out.write(new ObjectMapper().writeValueAsString(bean));
        out.flush();
        out.close();


    }
}
