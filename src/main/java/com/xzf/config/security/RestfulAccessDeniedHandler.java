package com.xzf.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzf.entity.CommonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 呵呵厉害了
 * @date 2021/06/23 19:29
 **/
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json");
        PrintWriter out = httpServletResponse.getWriter();
        CommonResult commonResult= CommonResult.error("权限不足，联系管理员");
        commonResult.setCode(403);
        out.write(new ObjectMapper().writeValueAsString(commonResult));
        out.flush();
        out.close();
    }
}
