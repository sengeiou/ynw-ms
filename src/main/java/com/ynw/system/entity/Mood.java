package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

/**
 * 话题模板
 *  @author ChengZhi
 *  @version 2018-12/14
 */
@Data
@ApiModel(value = "Mood", description = "话题模板")
public class Mood extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.dsMoodId=id;
    }

    @Id
    @ApiModelProperty(value = "动态id")
    private String dsMoodId;//动态id

    @ApiModelProperty(value = "用户id")
    private String acUserId;//用户id

    @ApiModelProperty(value = "动态内容")
    private String content;//动态内容

    @ApiModelProperty(value = "动态的权限类型")
    private String authType;//动态的权限类型，即对什么范围内的人公开，取字典表中定义的key。

    @ApiModelProperty(value = "是否匿名", dataType = "Long", example = "0")
    private Integer isAnonymous;//是否匿名（1：是   0：否）

    @ApiModelProperty(value = "是否转发", dataType = "Long", example = "0")
    private Integer isTranspond;//是否转发（1：是   0：否），当该动态是对其他动态的评论时有值，当该动态是原创动态时置空。

    @ApiModelProperty(value = "是否被评为", dataType = "Long", example = "0")
    private Integer isGodComment;//是否被评为‘神评论’（1：是   0：否），当该动态是对其他动态的评论时有值且必须有值，当该动态为原创动态时置空。

    @ApiModelProperty(value = "父动态id")
    private String parentMoodId;//父动态id，当该动态为对其他动态的评论时存被评论动态的id，当该动态为原创动态时置空。

    @ApiModelProperty(value = "根动态id")
    private String rootMoodId;//根动态id，当该动态是原创动态时置空，当该动态是针对其他动态的评论或评论的评论时存原创动态的id。

    @ApiModelProperty(value = "状态", dataType = "Long", example = "1")
    private Integer status;//状态（1：有效   0：无效   -1：已删除）

    @ApiModelProperty(value = "话题标签名集合")
    private List<String> labelList = new ArrayList<>();//话题标签名集合

    @ApiModelProperty(value = "用户头像")
    private String userImage;//用户头像

    @ApiModelProperty(value = "用户昵称")
    private String userName;//用户昵称

    @ApiModelProperty(value = "用户手机")
    private String userPhone;//用户手机

    @ApiModelProperty(value = "评论量", dataType = "Long", example = "0")
    private Integer commentNum;//评论量

    @ApiModelProperty(value = "点赞量", dataType = "Long", example = "0")
    private Integer likeNum;//点赞量

    @ApiModelProperty(value = "转发量", dataType = "Long", example = "0")
    private Integer transPondNum;//转发量

    @ApiModelProperty(value = "浏览量", dataType = "Long", example = "0")
    private Integer lookNum;//浏览量

    @ApiModelProperty(value = "照片集合")
    private List<String> moodImgList = new ArrayList<>();//照片集合

    @ApiModelProperty(value = "被回复的昵称")
    private String replyUserName;//被回复的昵称

    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public void assignment() {
        if (null == commentNum) {
            commentNum = 0;
        }
        if (null == likeNum) {
            likeNum = 0;
        }
        if (null == transPondNum) {
            transPondNum = 0;
        }
        if (null == lookNum) {
            lookNum = 1;
        }
    }

//    @Override
//    public void preInsert() {
//        if (null == isAnonymous) {
//            isAnonymous = 0;
//        }
//        if (null == isTranspond) {
//            isTranspond = 0;
//        }
//        if (null == isGodComment) {
//            isGodComment = 0;
//        }
//        if (null == status) {
//            status = 1;
//        }
//        super.preInsert();
//    }
}
