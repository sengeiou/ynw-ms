package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 用户模板
 *  @author ChengZhi
 *  @version 2018-12/11
 */
@Data
@ApiModel("APP用户表的实体类")
@Table(name = "t_ac_user")
public class AcUser extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.acUserId=id;
    }

    public AcUser(String acUserId) {
        this.acUserId = acUserId;
    }

    public AcUser() {
    }

    @Override
    public void preInsert() {
        super.preInsert();
    }

    @Id
    @ApiModelProperty(value = "用户id",example = "123")
    private String acUserId;//用户id

    @ApiModelProperty(value = "用户编号",example = "1213")
    private String no;//用户编号，即显示给用户看的用户ID号。

    @ApiModelProperty(value = "用户的昵称",example = "123")
    private String nickname;//昵称
    @ApiModelProperty(value = "真实姓名",example = "123")
    private String realName;//真实姓名

    @ApiModelProperty(value = "身份证号码",example = "3601****5847")
    private String idNumber;//身份证号码（号码需校验，要符合二代身份证号码规则）

    @ApiModelProperty(value = "手持身份证正面照片url",example = "123")
    private String idImageFrontUrl;//手持身份证正面照片url

    @ApiModelProperty(value = "手持身份证反面照片url",example = "123")
    private String idImageBackUrl;//手持身份证反面照片url

    @ApiModelProperty(value = "实名认证状态",example = "0")
    private Integer idVerifyStatus;//实名认证状态（-1：未实名认证   0：实名认证审核中   1：已实名认证 -2：实名认证失败）

    @ApiModelProperty(value = "性别（0：男    1：女）",example = "0")
    private Integer sex;//性别（0：男    1：女）

    @ApiModelProperty(value = "生日",example = "19XX.XX.XX")
    private Date birthday;//生日

    @ApiModelProperty(value = "年龄",example = "XX")
    private String age;//年龄

    @ApiModelProperty(value = "星座",example = "key")
    private String zodiac;//星座，取字典表中定义的key。

    @ApiModelProperty(value = "行业id",example = "123")
    private String bdBusinessId;//行业id

    @ApiModelProperty(value = "高校id",example = "123")
    private String bdUniversityId;//高校id

    @ApiModelProperty(value = "学历id",example = "123")
    private String bdDegreesId;//学历id

    @ApiModelProperty(value = "个性签名",example = "断剑重铸之日，骑士归来之时")
    private String signature;//个性签名

    @ApiModelProperty(value = "用户头像url",example = "/user/img/XXX")
    private String headImageUrl;//用户头像url，存url的尾路径，根路径统一配置到系统参数表中

    @ApiModelProperty(value = "用户手机号码",example = "1527XXXXXXX")
    private String phoneNumber;//用户手机号码，也是app端的登录账号

    @ApiModelProperty(value = "故乡",example = "123456")
    private String hometown;//故乡，存故乡的区县id。

    @ApiModelProperty(value = "用户当前所在城市ID",example = "123455")
    private String bdCityId;//用户当前所在城市ID，该值随系统每次获取用户定位所在城市而变，只存最后一次定位到的城市。

    @ApiModelProperty(value = "用户状态（0：无效   1：有效）",example = "1")
    private Integer status;//用户状态（0：无效   1：有效）

    @ApiModelProperty(value = "推荐人用户id",example = "123456")
    private String referrerId;//推荐人用户id

    @ApiModelProperty(value = "推荐码",example = "123456")
    private String referralCode;//推荐码,现在用于推荐CP,进行AES编码后变成了普通的推荐码

    @ApiModelProperty(value = "im系统的账户id",example = "123456")
    private String imUserId;//im系统的账户id，即在环信那边注册的用户id。

    private Date lastCallTime;//最后登录时间

    @Column(name = "`type`")
    private Integer type;//用户类型（0：普通用户   1：系统用户   2：营销机器人）

    @ApiModelProperty(value = "推荐人用户名",example = "飞羽")
    @Transient
    private String referrerName;//推荐人用户名

    @ApiModelProperty(value = "用户当前所在城市名",example = "南昌")
    @Transient
    private String bdCityName;//用户当前所在城市名

    @ApiModelProperty(value = "故乡名",example = "南昌")
    @Transient
    private String hometownName;//故乡名

    @ApiModelProperty(value = "学历名",example = "本科")
    @Transient
    private String bdDegreesName;//学历名

    @ApiModelProperty(value = "高校名",example = "南昌大学")
    @Transient
    private String bdUniversityName;//高校名

    @ApiModelProperty(value = "行业名",example = "学生")
    @Transient
    private String bdBusinessName;//行业名

    @ApiModelProperty(value = "星座名",example = "双鱼")
    @Transient
    private String zodiacName;//星座名

    @Transient
    private String LogContent;//添加系统日志的主体

    @ApiModelProperty(value = "是否存在未审核图片",example = "1")
    @Transient
    private Integer imageStatus;//是否存在未审核图片

}
