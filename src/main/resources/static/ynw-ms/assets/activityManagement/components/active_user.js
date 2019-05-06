var num = 1;
$(function () {
    //第一页数据
    getData(1);
    //获取数据总数并分页
    getNum();
    //获取字典中的身份数据
    getDictionary("ACTIVITY_USER_IDENTITY_TYPE");
    //返回
    getBack();
    //鼠标触碰
    data_hover();
    //点击事件
    bottom_click();
    //点击隐藏
    $(".cancel").click(function () {
        $(".active-user-details").hide();
        $(".sendTextMessage").hide();
        $(".screenshot-user").hide();
    });
    $(".screenshot-cancel").click(function () {
        $(".screenshot-user").hide();
    });
});

// //样式
// function setCss() {
//     $(".user-photo div").css("width", $(".user-photo").height())
// }

/**
 *  点击事件
 */
function bottom_click() {
    /**
     *  筛选
     */
    $("#screen_date").click(function(){
        num = 1;
        var count = getNum();
        getData(1);
        setPage(count)
    });
    /**
     *  刷新
     */
    $("#refresh_date").click(function(){
        var count = getNum();
        setPage(count);
        getData(num)
    });

    /**
     *  参与活动用户详情
     */
    $(document).on("click",".particulars", function () {
       var id = $(this).prev().val();
        $.ajax({
            type:"post",
            url: '../../../register/findByActivityId',
            data: {
                "activityId": id
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    var value = res.result;
                    if (value.identity == "学生") {
                        $(".degrees").hide();
                        $("span[name='bdUniversityId']").text(value.bdUniversityId);
                    } else if (value.identity == "上班族") {
                        $(".university").hide();
                        $("span[name='bdDegreesId']").text(value.bdDegreesId);
                    }
                    if (value.sex == 1) {
                        value.sex = '女';
                    }  else {
                        value.sex = '男';
                    }
                    if (value.photoUrl != "") {
                        $(".userPhotoUrl").prop("src",value.photoUrl);
                    }
                    $(".shareScreenshotUrl").prop("src",value.shareScreenshotUrl);
                    $("span[name='name']").text(value.name);
                    $("span[name='sex']").text(value.sex);
                    $("span[name='identity']").text(value.identity);
                    $("span[name='height']").text(value.height+"cm");
                    $("span[name='loveHistory']").text(value.loveHistory);
                    $("span[name='wechat']").text(value.wechat);
                    $("span[name='phoneNumber']").text(value.phoneNumber);
                    $("span[name='age']").text(value.age);
                    $("span[name='weight']").text(value.weight+"kg");
                    $("span[name='characterType']").text(value.characterType);
                    $("span[name='wantSex']").text(value.wantSex);
                    $("span[name='wantWeight']").text(value.wantWeight);
                    $("span[name='wantLoveHistory']").text(value.wantLoveHistory);
                    $("span[name='wantHeight']").text(value.wantHeight);
                    $("span[name='wantAge']").text(value.wantAge);
                    $("span[name='wantCharacterType']").text(value.wantCharacterType);
                    $(".want-user-remark .content-body").text(value.remark);
                    $(".user-remark .content-body").text(value.wantOther);
                    $(".active-user-details").show();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    /**
     *  匹配
     */
    $(".matching").click(function () {
        var registerId = $("#atRegisterId").val();
        $.ajax({
            type:"post",
            url: '../../../register/findBySexAndWantSex',
            data: {
                "registerId":registerId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    var count = getNum();
                    setPage(count);
                    getData(num)
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
                $(".sendTextMessage").hide();
            }
        });
    });

    /**
     *  导出excel
     */
    $(".exportExcel").click(function () {
        window.location.href="../../../register/exportExcel?registerId=" + $("#atRegisterId").val();
    });

    /**
     *  建立活动聊天室和活动cp聊天室
     */
    $(".establishGroup").click(function () {
        var registerId = $("#atRegisterId").val();
        $.ajax({
            type:"post",
            url: '../../../register/establishGroup',
            data: {
                "registerId":registerId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    /**
     *  发送短信
     */
    $(".note").click(function () {
        // $(".sendTextMessage-body inputn").val("");
        // $(".sendTextMessage").show();
        var registerId = $("#atRegisterId").val();
        $.ajax({
            type:"post",
            url: '../../../register/sendTextMessage',
            data: {
                "registerId":registerId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
                // $(".sendTextMessage").hide();
            }
        });
    });

    /**
     *  发送群短信
     */
    $(".sendGroupMessage").click(function () {
        // $(".sendTextMessage-body inputn").val("");
        // $(".sendTextMessage").show();
        var registerId = $("#atRegisterId").val();
        $.ajax({
            type:"post",
            url: '../../../register/sendGroupMessage',
            data: {
                "registerId":registerId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
                // $(".sendTextMessage").hide();
            }
        });
    });

    $("#note-confirm").click(function () {
        var grouping = $("input[name='grouping']").val();
        if (grouping == "") {
            layer.msg('分组字母不能为空',{icon:2});
            return false;
        }
        var weChat = $("input[name='weChat']").val();
        if (weChat == "") {
            layer.msg('分组微信不能为空',{icon:2});
            return false;
        }
        var registerId = $("#atRegisterId").val();
        $.ajax({
            type:"post",
            url: '../../../register/sendTextMessage',
            data: {
                "grouping": grouping,
                "weChat":weChat,
                "registerId":registerId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
                $(".sendTextMessage").hide();
            }
        });
    });

    /**
     *  更换cp
     */
    $(".changeCp").click(function () {
        var registerId = $("#atRegisterId").val();
        $.ajax({
            type:"post",
            url: '../../../register/changeCp',
            data: {
                "registerId":registerId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    $(".changeColonization").click(function () {
        var registerId = $("#atRegisterId").val();
        $.ajax({
            type:"post",
            url: '../../../register/colonization',
            data: {
                "registerId":registerId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //加群
    $(".addGroup").click(function () {
        $("#addGroup .sendTextMessage-body input").val("");
        $("#addGroup").show();
    });

    $("#add_group_confirm").click(function () {
        var groupId = $("#addGroup input[name='groupId']").val();
        if (groupId == "") {
            layer.msg("群组环信编号不能为空",{icon:2});
            return false;
        }
        var imUserId = $("#addGroup input[name='imUserId']").val();
        if (imUserId == "") {
            layer.msg("用户环信编号不能为空",{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url: '../../../register/addGroup',
            data: {
                "groupId":groupId,
                "imUserId":imUserId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#addGroup").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //退群
    $(".deleteGroup").click(function () {
        $("#removeOutGroup .sendTextMessage-body input").val("");
        $("#removeOutGroup").show();
    });

    $("#removeOutGroupConfirm").click(function () {
        var groupId = $("#removeOutGroup input[name='groupId']").val();
        if (groupId == "") {
            layer.msg("群组环信编号不能为空",{icon:2});
            return false;
        }
        var members = $("#removeOutGroup input[name='imUserId']").val();
        if (members == "") {
            layer.msg("用户环信编号不能为空",{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url: '../../../register/removeOutGroup',
            data: {
                "groupId":groupId,
                "members":members
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#removeOutGroup").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    /**
     * 查看图片
     */
    $(document).on("click",".screenshot", function () {
        var url = $(this).children("img").prop("src");
        $(".screenshot-user img").prop("src", url);
        $(".screenshot-user").show();
    })

    $(".user-photo img").click(function () {
        var url = $(this).prop("src");
        $(".screenshot-user img").prop("src", url);
        $(".screenshot-user").show();
    })

}

//查询字典
function getDictionary(groupKey) {
    $.ajax({
        type:"post",
        url: '../../../dictionary/findDictionaryByGroupKey',
        data: {
            "groupKey": groupKey
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">----请选择----</option>';
                $.each(res.result,function (index,value) {
                    src += '<option value="'+ value.itemKey +'">'+ value.itemValue +'</option>';
                });
                $("#identity").html(src);
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//分页-------------------------------
/**
 *  分页
 */
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
    var phoneNumber = $("#phoneNumber").val();
    var sex = $("#sex").val();
    var identity = $("#identity").val();
    var atRegisterId = $("#atRegisterId").val();
    var matchNo = $("#matchNo").val();
    $.ajax({
        type:"post",
        url:"../../../register/count-activity",
        data: {
            "name":name,
            "phoneNumber":phoneNumber,
            "sex":sex,
            "atRegisterId":atRegisterId,
            "matchNo":matchNo,
            "identity":identity
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

//主体数据-------------------------------
function getData(nowPage) {
    var name = $("#name").val();
    var phoneNumber = $("#phoneNumber").val();
    var sex = $("#sex").val();
    var identity = $("#identity").val();
    var atRegisterId = $("#atRegisterId").val();
    var matchNo = $("#matchNo").val();
    // alert(name + ":"+phone + ":"+syReportCtgyId + ":"+targetType + ":")
    $.ajax({
        type:"post",
        url:"../../../register/conditionQueryActivity",
        data: {
            "name":name,
            "phoneNumber":phoneNumber,
            "sex":sex,
            "identity":identity,
            "atRegisterId":atRegisterId,
            "matchNo":matchNo,
            "nowPage": nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                var resourceId = $("#resourceId").val();
                $.each(res.result,function (index,value) {
                    value.createTime = getDate(value.createTime);
                    if (value.sex == 1) {
                        value.sex = "女";
                    } else if (value.sex == 0) {
                        value.sex = "男"
                    }
                    if (value.matchNo == "") {
                        value.matchNo = "未匹配"
                    }
                    str += '<tr class="gradeX">';
                    str += '<td class="screenshot"><img src="'+ value.photoUrl +'"></td>';
                    // str += '<td class="screenshot"><img src="../../img/a3.jpg"></td>';
                    str += '<td>'+value.name+'</td>';
                    str += '<td>'+value.phoneNumber+'</td>';
                    str += '<td>'+value.sex+'</td>';
                    str += '<td>'+value.identity+'</td>';
                    str += '<td>'+value.age+'</td>';
                    str += '<td>'+value.height+'cm</td>';
                    str += "<td>"+ value.weight +"kg</td>";
                    str += '<td>'+value.loveHistory+'</td>';
                    str += '<td>'+value.characterType+'</td>';
                    str += '<td>'+value.createTime+'</td>';
                    str += "<td>"+ value.matchNo +"</td>";
                    str += '<td><input type="hidden" value="'+ value.atWkcpUserId +'" /><a class="particulars" href="javascript:;">详细</a></td></tr>';
                })
                $("#report").html(str);
            } else if (res.code == 8) {
                $("#report").empty();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//返回————————————————————————————
function getBack() {
    $(".back").click(function () {
        var resourceId = $("#resourceId").val();
        $(location).attr("href",'activity_management?msResourceId='+resourceId);
    });
}