package com.xzf.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户登陆所需数据
 * @author 呵呵厉害了
 * @date 2021/06/22 19:05
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "登陆对象",description = "")
@Accessors(chain = true)
public class LoginParam {
    @ApiModelProperty(required = true)
    private String username;
    @ApiModelProperty(required = true)
    private String password;
}
