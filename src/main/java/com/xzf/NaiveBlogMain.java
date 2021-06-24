package com.xzf;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 呵呵厉害了
 * @date 2021/06/17 18:38
 * 主启动类
 **/
@SpringBootApplication
@MapperScan("com.xzf.mapper")
public class NaiveBlogMain {
    public static void main(String[] args) {
        SpringApplication.run(NaiveBlogMain.class,args);
    }
}
