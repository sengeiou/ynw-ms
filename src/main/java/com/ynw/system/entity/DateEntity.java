package com.ynw.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DateEntity implements Entity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
    }

    @ApiModelProperty(value = "创建时间")
    private Date createTime;//创建时间

    /**
     *  插入之前调用
     */
    public void preInsert(){
        this.createTime = new Date();
    }

    public void beforeInsert(){
        this.createTime = new Date(System.currentTimeMillis()-1000);
    }

    /**
     *  更新之前调用
     */
    public void preUpdate(){
    }

}
