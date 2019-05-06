package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ImUser", description = "im用户信息")
public class ImUser {
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password="88888888";

    public ImUser(String nickname, String username) {
        this.nickname = nickname;
        this.username = username;
    }

    public ImUser() {
    }
}
