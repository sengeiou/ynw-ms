package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *  活动报名人员模板
 */

@Data
@Table(name = "t_at_wkcp_user")
@ApiModel(value = "Activity", description = "活动报名人员模板")
public class Activity extends DateEntity {
    @Override
    public void setId(String id) {
        this.atWkcpUserId = id;
    }

    @Id
    @ApiModelProperty(value = "用户id")
    private String atWkcpUserId;//一周情侣活动报名用户id

    @ApiModelProperty(value = "活动id")
    private String atRegisterId;//活动登记id

    @ApiModelProperty(value = "线上用户账户id")
    private String acUserId;//线上用户账户id

    @Column(name = "`name`")
    @ApiModelProperty(value = "报名者姓名")
    private String name;//报名者姓名

    @ApiModelProperty(value = "性别", example = "1")
    private Integer sex;//性别（0：男   1：女）

    @ApiModelProperty(value = "年龄", example = "20")
    private Integer age;//年龄

    @ApiModelProperty(value = "报名者身份")
    private String identity;//报名者身份，学生或上班族之类，取字典表中定义的key。

    @ApiModelProperty(value = "学历id")
    private String bdDegreesId;//学历id，当报名者身份为非学生时有值。

    @ApiModelProperty(value = "学校id")
    private String bdUniversityId;//学校id，当报名者身份是学生时表示在读学校，当报名者身份是非学生时表示毕业学校。此处表示的是大学，当报名者学历低于大学时不填。

    @ApiModelProperty(value = "年级", example = "1")
    private Integer grade;//年级，当报名者身份是学生时有值。

    @ApiModelProperty(value = "身高", example = "160")
    private Integer height;//身高

    @ApiModelProperty(value = "体重",example = "50.0")
    private Double weight;//体重

    @ApiModelProperty(value = "情感经历")
    private String loveHistory;//情感经历，取字典表中定义的key。

    @ApiModelProperty(value = "性格类型")
    private String characterType;//性格类型，取字典表中定义的key。

    @ApiModelProperty(value = "用户照片url")
    private String photoUrl;//用户照片url

    @ApiModelProperty(value = "分享截图url")
    private String shareScreenshotUrl;//分享截图url

    @ApiModelProperty(value = "电话")
    private String phoneNumber;//电话

    @ApiModelProperty(value = "短信验证码")
    private String smsCode;//短信验证码

    @ApiModelProperty(value = "微信")
    private String wechat;//微信

    @ApiModelProperty(value = "自我备注")
    private String remark;//自我备注

    @ApiModelProperty(value = "要求的性别")
    private String wantSex;//要求的性别，取字典表中定义的key。

    @ApiModelProperty(value = "要求的身高")
    private String wantHeight;//要求的身高，取字典表中定义的key。

    @ApiModelProperty(value = "要求的体重")
    private String wantWeight;//要求的体重，取字典表中定义的key。

    @ApiModelProperty(value = "要求的年龄")
    private String wantAge;//要求的年龄，取字典表中定义的key。

    @ApiModelProperty(value = "要求的情感经历情况")
    private String wantLoveHistory;//要求的情感经历情况，取字典表中定义的key。

    @ApiModelProperty(value = "要求的性格类型")
    private String wantCharacterType;//要求的性格类型，取字典表中定义的key。

    @ApiModelProperty(value = "其他要求")
    private String wantOther;//其他要求

    @ApiModelProperty(value = "状态", example = "0")
    private Integer status;//状态（0：待匹配   1：已匹配待通知   2：已通知）

    @ApiModelProperty(value = "匹配编号")
    private String matchNo;//匹配编号

    @ApiModelProperty(value = "匹配的cp编号",example = "1")
    private Integer cpNo;//匹配的cp编号

//    @ApiModelProperty(value = "个人好感排名")
    @Transient
    private List<Activity> favorableRanking = new ArrayList<>();//个人好感排名

    @Transient
//    @ApiModelProperty(value = "个人好感度")
    private List<Interest> favorAbility = new ArrayList<>();//个人好感度

    @Transient
    @ApiModelProperty(value = "好感度",example = "1")
    private Integer feeling;//好感度

    @Transient
    @ApiModelProperty(value = "经度", example = "28.1")
    private Double longitude;//经度

    @Transient
    @ApiModelProperty(value = "纬度",example = "115.27")
    private Double latitude;//纬度

    @Transient
    @ApiModelProperty(value = "用户环信id")
    private String imUserId;//用户环信id

    public Activity(String atRegisterId, Integer status) {
        this.atRegisterId = atRegisterId;
        this.status = status;
    }

    public Activity(String atWkcpUserId) {
        this.atWkcpUserId = atWkcpUserId;
    }

    public Activity() {
    }

    public Activity(String atWkcpUserId, String matchNo) {
        this.atWkcpUserId = atWkcpUserId;
        this.matchNo = matchNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;
        if (!super.equals(o)) return false;
        Activity activity = (Activity) o;
        return getAtWkcpUserId().equals(activity.getAtWkcpUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAtWkcpUserId());
    }

}
