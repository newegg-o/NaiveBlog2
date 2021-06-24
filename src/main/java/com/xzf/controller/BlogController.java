package com.xzf.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xzf.entity.Blog;
import com.xzf.entity.CommonResult;
import com.xzf.entity.User;
import com.xzf.service.BlogService;
import com.xzf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xzf
 * @since 2021-06-22
 */
@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    BlogService blogService;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public CommonResult blogs(Integer currentPage) {
        if (currentPage == null || currentPage < 1) currentPage = 1;
        Page page = new Page(currentPage, 5);
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
        return CommonResult.success("操作成功", pageData);
    }

    @GetMapping("/blog/{id}")
    public CommonResult detail(@PathVariable(name = "id") Long id) {
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已删除！");
        return CommonResult.success("success", blog);
    }

    @PostMapping("/blog/edit")
    public CommonResult edit(@Validated @RequestBody Blog blog, Principal principal) {
        Blog temp = null;
        if (principal == null) {
            return null;
        }

        temp = blogService.getById(blog.getId());
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        if (temp == null) {
            temp=new Blog();
            temp.setUserId(user.getId());
            temp.setStatus(0);
            temp.setCreated(LocalDateTime.now());
            BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");
            blogService.saveOrUpdate(temp);
            return CommonResult.success("添加成功", blog);
        }
        if (temp.getUserId() != user.getId()) {
            return CommonResult.error("无法编辑他人博客");
        } else {
            temp.setUserId(user.getId());
            temp.setStatus(0);
            BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");
            blogService.saveOrUpdate(temp);
            return CommonResult.success("更新成功", blog);

        }


    }


}
