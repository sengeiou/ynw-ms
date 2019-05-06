package com.ynw.system.enums;

/**
 * 错误返回码和提示信息统一处理枚举类
 */
public enum ResultEnums {

    UNKNOWN_ERROR(-1,"系统错误，请稍后重试"),
    SUCCESS_LOGIN(1,"成功"),
    ADDITION_FAILED(2,"数据操作失败"),
    DATA_DUPLICATION_ERR0R(3,"数据重复"),
    DELETE_ERROR(4,"删除失败"),
    ACC_PASS_ERROR(5,"账号或者密码错误"),
    LOCK_ERROR(6,"账号已经注销"),
    DATA_FORMANT_ERROR(7,"请先删除关联数据"),
    NO_DATA_ERROR(8, "未查询到数据"),
    CHECKED_IN_ERROR(9, "已签到"),
    USER_ERROR(10, "请确认用户"),
    STATISTICS_ERROR(11,"统计错误"),
    ATTENTION_ERROR(12,"已经关注"),
    LIKE_ERROR(13,"已经点赞"),
    MOVE_UP_ERROR(14,"已经是最上了"),
    MOVE_DOWN_ERROR(15,"已经是最下了"),
    UNSORTED_ERROR(16,"未排序"),
    IS_COVER_ERROR(17,"封面设置错误"),
    DATA_RELEVANCE_ERROR(18,"关联数据错误"),
    EXPORT_EXCEL_ERROR(19,"excel导出失败"),
    MATCH_NO_ERROR(20,"匹配失败"),
    NO_DATA_OR_MULTI(21,"数据不存在或者存在多个数据"),
    YOU_HAVE_CP(22,"匹配用户已经有cp，请重新匹配"),
    INCOMPLETE_INFORMATION(100,"信息不完整"),
    PAGE_ERROR(101,"页码错误"),
    PHONE_ERROR(102,"非法手机号"),
    IO_ERROR(103,"输入流异常"),
    FILE_ERROR(104,"文件上传异常"),
    BUSINESS_LIMIT_CONTROL(105,"请求频繁，请稍后再试"),
    Token_ERROR(109,"TOKEN验证错误"),
    URL_ERROR(110,"url不能为空"),
    SEND_SMS_CODE(111,"短信发送失败"),
    PORTION_SMS_CODE(112,"部分短信发送失败"),
    FILE_FORMAT_ERROR(113,"文件格式不符合"),
    THE_SAME_STATE(114,"已经是该状态"),
    SAME_DAY_TASK(115,"每个活动一天只能创建一个任务"),
    USER_QUESTIONS_CANNOT_BE_MODIFIED(116, "不能修改用户题目"),
    UPLOADING_EXCEL(117,"请上传一个excel"),
    HUAN_XIN_GROUP_ERROR(118,"环信群组创建失败"),
    HUAN_XIN_GROUP_DISSOLVE_ERROR(119,"环信群组解散失败"),
    ACTIVITY_NOT_OVER(120,"活动还没结束"),
    ACTIVITY_NOT_BEGIN(120,"活动还没开始呢"),
    HUAN_XIN_GROUP_EXIST(121,"环信群组已经存在，请先解散群组"),
    HUAN_XIN_GROUP_DISSOLVE(122,"环信群组已经解散"),
    LESS_THAN_HELF(123,"查询的数据不足一半"),
    ILLEGAL_OPERATION(124,"非法操作"),
    ACTIVITY_NOT_STARTED(125, "报名未结束"),
    ACTIVITY_OVER(126, "活动已开始"),
    ;

    private Integer code =0;
    private String message = "";

    ResultEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
