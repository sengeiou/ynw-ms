var num = 1;
var registerId = "";
var checkStatus = "";
$(function () {
    setCss();
    //第一页数据
    getData(1);
    //获取按钮
    getButton('约活动管理');
    //获取数据总数并分页
    getNum();
    //未来点击事件
    bottom_click();
    //隐藏操作框
    control_tip();
    //获取字典
    // getDictionary();
    getReportType();
    // 全选复选框
    $("#checkAll").click(function () {
        selectAll();
    })
    //如果不是全选取消全选框
    $(document).on("click","input[type='checkbox'][name='checkedres']",function(){
        selectBox();
    });
    monitorClick();
    //链接带参时
    setInterface();
});


//获取url头部参数
function GetQueryString(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  decodeURI(r[2]); return null;
}

function setInterface() {
    var name = GetQueryString("name");
    if (name != null) {
        $("#name").val(name);
    }
    var userName = GetQueryString("userName");
    if (userName != null) {
        $("#userName").val(userName);
    }
    var phone = GetQueryString("phone");
    if (phone != null) {
        $("#phone").val(phone);
    }
    var checkStatus = GetQueryString("checkStatus");
    if (checkStatus != null) {
        $("#type").val(checkStatus);
    }
    var beginDate = GetQueryString("beginDate");
    if (beginDate != null) {
        $("#beginDate").val(beginDate);
    }
    var num1 = GetQueryString("num");
    if (num1 != null) {
        var count = getNum();
        getData(num1);
        setPage(count)
    }
}

function setCss() {
    // $("#QRCode").css("height", $("#QRCode").width());
    // alert($("#QRCode").width()+":"+$("#QRCode").height())
}

/**
 *  未来点击事件
 */
function bottom_click() {

    /**
     *  筛选
     */
    $(document).on('click','#screen_date',function(){
        num = 1;
        var count = getNum();
        getData(1);
        setPage(count)
    });
    /**
     *  刷新
     */
    $(document).on('click','#refresh_date',function(){
        var count = getNum();
        setPage(count);
        getData(num);
    });

    /**
     *  详情
     */
    $(document).on('click', '#particulars', function () {
        if (getBox() == 1) {
            getChecked();
            findById();
            $("#getThrough,#noThrough").hide();
            $(".detail").show();
            $("#activityDetail").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    /**
     *  审核
     */
    $(document).on('click', '#audit', function () {
        if (getBox() == 1) {
            getChecked();
            findById();
            $(".detail").hide();
            $("#getThrough,#noThrough").show();
            $("#activityDetail").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    /**
     * 审核通过
     */
    $("#getThrough").click(function () {
        setCheckStatus(1, null);
    });

    /**
     *  审核不通过
     */
    $("#noThrough").click(function () {
        $("#reason").val("");
        $("#rejected").show();
    });

    $("#withdrawal_confirm").click(function () {
        var reason = $("#reason").val();
        if (reason == "") {
            layer.msg("不通过理由不能为空",{icon:2});
            return false;
        }
        setCheckStatus(-1, reason);
    });

    /**
     * 跳转报名用户
     */
    $(document).on("click","#joinUser", function () {
        if (getBox() == 1) {
            getChecked();
            //获取自己的资源编号
            var msResourceId = $(this).prev().val();
            skip("invite_user?parentId="+$("#resourceId").val() +"&msResourceId="+ msResourceId + "&registerId=" + registerId);
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    /**
     * 跳转主题管理
     */
    $(document).on("click","#inviteTheme", function () {
        //获取自己的资源编号
        var msResourceId = $(this).prev().val();
        skip("invite_theme?parentId="+$("#resourceId").val() +"&msResourceId="+ msResourceId);
    });

}

/**
 * 更新活动审核状态
 * @param status
 */
function setCheckStatus(status, reason) {
    $.ajax({
        type:"post",
        url:"../../../engagement/updateStatus",
        data: {
            "atRegisterId": registerId,
            "checkStatus": status,
            "reason":reason,
            "LogContent":"修改活动状态【 活动状态改为："+ status +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var count = getNum();
                setPage(count);
                getData(num);
                $(".unified-closed").hide()
                layer.msg(res.message,{icon:1});
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

/**
 *  跳转并获取参数
 */
function skip(url) {
    url += "&name="+ $("#name").val() + "&userName="+ $("#userName").val() + "&phone="+ $("#phone").val() + "&atCtgyId=" + $("#atCtgyId").val() +
        "&atInviteThemeId=" + $("#atInviteThemeId").val() + "&checkStatus=" + $("#checkStatus").val() + "&beginDate=" + $("#beginDate").val() +
        "&num=" + num;
    $(location).attr("href", url);
}

//获取选中的-------------------------------
function getChecked() {
    var box = $("input[type='checkbox'][name='checkedres']");
    var length = box.length;
    for (var i = 0; i < length; i++) {
        if (box[i].checked) {
            registerId = box[i].value;
            checkStatus = box[i].parentNode.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.textContent;
        }
    }
}

function findById() {
    $.ajax({
        type:"post",
        url:"../../../engagement/findById",
        data: {
            "registerId": registerId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var value = res.result;
                $("#activityDetail label[name='name']").text(value.name);
                $("#activityDetail label[name='classifyName']").text(value.classifyName);
                $("#activityDetail label[name='themeName']").text(value.themeName);
                $("#activityDetail label[name='userName']").text(value.userName);
                $("#activityDetail label[name='userPhone']").text(value.userPhone);
                if (value.limitSex == 0) {
                    value.limitSex = '男';
                } else if (value.limitSex == 1) {
                    value.limitSex = '女';
                }  else {
                    value.limitSex = '都可以';
                }
                $("#activityDetail label[name='limitSex']").text(value.limitSex);
                value.applyEndTime = getDate(value.applyEndTime);
                $("#activityDetail label[name='applyEndTime']").text(value.applyEndTime);
                value.beginTime = getDate(value.beginTime);
                value.endTime = getDate(value.endTime);
                $("#activityDetail span[name='beginTime']").text(value.beginTime);
                $("#activityDetail span[name='endTime']").text(value.endTime);
                $("#activityDetail label[name='addrDetail']").text(value.addrDetail);
                $("#activityDetail label[name='content']").text(value.content);
                if (value.feeStatus == 0) {
                    value.feeStatus = 'AA';
                } else if (value.feeStatus == 1) {
                    value.feeStatus = '自定义';
                } else {
                    value.feeStatus = '自费'
                }
                $("#activityDetail label[name='feeStatus']").text(value.feeStatus);
                $("#activityDetail span[name='singleFee']").text(value.singleFee);
                $("#activityDetail span[name='minNumber']").text(value.minNumber);
                $("#activityDetail span[name='maxNumber']").text(value.maxNumber);
                if (value.checkStatus == 0) {
                    value.checkStatus = '审核中';
                } else if (value.checkStatus == 1) {
                    value.checkStatus = '审核通过';
                }  else {
                    value.checkStatus = '审核不通过';
                }
                $("#activityDetail label[name='checkStatus']").text(value.checkStatus);
                value.updateTime = getDate(value.updateTime);
                value.createTime = getDate(value.createTime);
                $("#activityDetail label[name='updateTime']").text(value.updateTime);
                $("#activityDetail label[name='createTime']").text(value.createTime);
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取活动类别-------------------------------
function getReportType() {
    $.ajax({
        type:"post",
        url:"../../../register-classify/conditionQueryRegisterClassify",
        data: {
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                // var str = '<select style="width: 110%" size="'+ res.result.length +'">';
                $.each(res.result,function (index,value) {
                    src += '<option value="'+ value.atCtgyId +'">'+ value.name +'</option>';
                    // str += '<option value="'+ value.atCtgyId +'">'+ value.name +'</option>';
                });
                // str += '</select>';
                $("#atCtgyId").html(src);
                // $(".nowPage-left").html(str);
                // $('.nowPage-left select option:first').prop('selected', 'selected');
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取主题-------------------------------
function getCity() {
    $.ajax({
        type:"post",
        url:"../../../dictionary/findDictionaryByGroupKey",
        data: {
            "groupKey":'SUGAR_TYPE'

        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                $.each(res.result,function (index,value) {
                    src += '<option value="'+ value.itemKey +'">'+ value.itemValue +'</option>';
                });
                $("#withdrawDeposit").html(src);
                var withdrawDeposit = GetQueryString("withdrawDeposit");
                if (withdrawDeposit != null) {
                    $("#withdrawDeposit").val(withdrawDeposit);
                }
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取字典-------------------------------
function getDictionary() {
    $.ajax({
        type:"post",
        url:"../../../dictionary/findDictionaryByGroupKey",
        data: {
            "groupKey":'SUGAR_TYPE'

        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                $.each(res.result,function (index,value) {
                    src += '<option value="'+ value.itemKey +'">'+ value.itemValue +'</option>';
                });
                $("#withdrawDeposit").html(src);
                var withdrawDeposit = GetQueryString("withdrawDeposit");
                if (withdrawDeposit != null) {
                    $("#withdrawDeposit").val(withdrawDeposit);
                }
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//分页-------------------------------
function setPage(total) {
    Helper.ui.page("#page", {
        total: total,
        pageSize: 10,
        showTotal: true,
        showTo: true,
        currentPage: num,
        change: function ( i ) {
            num = i;
            getData(i);
        }
    });
}

//获取总数-------------------------------
function getNum() {
    var name = $("#name").val();
    var userName = $("#userName").val();
    var phone = $("#phone").val();
    var atCtgyId = $("#atCtgyId").val();
    var atInviteThemeId = $("#atInviteThemeId").val();
    checkStatus = $("#checkStatus").val();
    var beginDate = $("#beginDate").val();
    $.ajax({
        type:"post",
        url:"../../../engagement/count",
        data: {
            "name":name,
            "userName":userName,
            "userPhone":phone,
            "atCtgyId":atCtgyId,
            "atInviteThemeId":atInviteThemeId,
            "checkStatus":checkStatus,
            "beginTime":beginDate
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                setPage(res.result);
            }
        }
    });
}

//获取数据主体
function getData(nowPage) {
    var name = $("#name").val();
    var userName = $("#userName").val();
    var phone = $("#phone").val();
    var atCtgyId = $("#atCtgyId").val();
    var atInviteThemeId = $("#atInviteThemeId").val();
    checkStatus = $("#checkStatus").val();
    var beginDate = $("#beginDate").val();
    $.ajax({
        type:"post",
        url:"../../../engagement/conditionQueryEngagement",
        data: {
            "name":name,
            "userName":userName,
            "userPhone":phone,
            "atCtgyId":atCtgyId,
            "atInviteThemeId":atInviteThemeId,
            "checkStatus":checkStatus,
            "beginTime":beginDate,
            "nowPage": nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                $.each(res.result,function (index,value) {
                    // var time = new Date(value.createTime);
                    value.createTime = getDate(value.createTime);
                    value.beginTime = getDate(value.beginTime);
                    value.endTime = getDate(value.endTime);
                    str += '<tr class="gradeX"><td id="td_input"><input type="checkbox" name="checkedres" value="'+value.atRegisterId+'">';
                    str += '<td>'+value.name+'</td>';
                    str += '<td>'+value.classifyName+'</td>';
                    str += '<td>'+value.themeName+'</td>';
                    str += '<td>'+value.userName+'</td>';
                    str += '<td>'+value.userPhone+'</td>';
                    str += '<td>'+value.beginTime+'</td>';
                    str += '<td>'+value.endTime+'</td>';
                    str += '<td>'+value.addrDetail+'</td>';
                    if (value.checkStatus == 1) {
                        str += '<td>通过</td>';
                    } else if (value.checkStatus == 0) {
                        str += '<td style="color: red">审核中</td>';
                    } else {
                        str += '<td>不通过</td>';
                    }
                    str += '<td>'+value.userNum+'</td>';
                    str += '<td>'+value.createTime+'</td> </tr>';
                })
                // javascript:;
                $("#admin").html(str);
                //选中状态发生改变是隐藏功能弹窗
            } else if (res.code == 8) {
                $("#admin").empty();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}