$(function () {
    //返回
    getBack();
    //获取当前动态信息
    getMoodOne();
    //获取评论
    getComment();
    //删除被举报话题
    deleteTarget();
    //隐藏操作框
    control_tip();
});

//获取评论-----------------------
function getComment() {
    var moodId = $("#moodId").val();
    $.ajax({
        type:"post",
        url:"../../../mood/findMoodCommentByRootMoodId",
        data: {
            "rootMoodId":moodId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            var src = '<div class="data-top">话题评论</div>';
            if (res.code == 1) {
                var size = Math.ceil(res.result.length);
                $(".mood-comment-data").css('height', res.result.length*150 + 60);
                $.each(res.result,function (indexs,value) {
                    var date = getDate(value.createTime);
                    src += '<div class="mood-comment-body">';
                    src += '<div class="mood-comment-body-left"><img src="'+ value.userImage +'"></div>';
                    src += '<div class="mood-comment-body-right">';
                    src += '<div class="body-right-up"><span>'+ value.userName +'@'+ value.replyUserName +'</span>：'+value.content;
                    src += '</div><div class="body-right-down">'+ date +'</div>';
                    src += '</div></div>';
                });
            } else {
                if (res.code == 8) {
                    layer.msg("没有用户评论",{icon:2});
                } else {
                    layer.msg(res.message,{icon:2});
                }

            }
            $(".mood-comment-data").html(src);
        }
    });
}

//获取当前动态信息-----------------------------
function getMoodOne() {
    var moodId = $("#moodId").val();
    $.ajax({
        type:"post",
        url:"../../../mood/findById",
        data: {
            "dsMoodId":moodId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var value = res.result;
                if (value.status == -1) {
                    //已经删除的隐藏删除按钮
                    $(".delete-target").hide();
                }
                var date = getDate(value.createTime);
                $(".data-left").html('<img src="'+ value.userImage +'"><h4>'+ value.userName +'</h4>'+value.userPhone);
                var moodLabel = '';
                // var length = value.labelList.length;
                $.each(value.labelList,function (indexs,labels) {
                    moodLabel += '<span>'+ labels +'</span>';
                });
                $(".data-right-top").html( moodLabel + date);
                $("#data-right-body-content").text(value.content);
                var src = '';
                $.each(value.moodImgList,function (num,img) {
                    src += '<img src="'+ img +'">';
                });
                $("#data-right-body-img").html(src);
            } else {
                if (res.code == 8) {
                    layer.msg("没有查询到动态",{icon:2});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        }
    });
}

//删除被举报话题
function deleteTarget() {

    //获取所有举报类型
    getReportType();
    
    $(".delete-target").click(function () {
        $("#idCardAudit select").val("");
        $("#idCardAudit").show();
    });

    $("#id-card-confirm").click(function () {
        var content = $("#idCardAudit select").val();
        if (content == "") {
            layer.msg("请选择举报类型",{icon:2});
        }
        var targetId = $("#moodId").val();
        $.ajax({
            type:"post",
            url:"../../../mood/delete",
            data: {
                "dsMoodId":targetId,
                "LogContent": "删除举报对象【 举报对象编号："+ targetId +"】",
                "content":"您发布的动态涉嫌违反我们的规定（"+content+"），现已删除！！！"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    //已经删除的隐藏删除按钮
                    $(".delete-target").hide();
                    $("#idCardAudit").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

}

//获取所有举报类型
function getReportType() {
    $.ajax({
        type:"post",
        url:"../../../report/findReportTypeAll",
        data: {
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                $.each(res.result,function (index,value) {
                    src += '<option value="'+ value.name +'">'+ value.name +'</option>';
                });
                $("#idCardAudit select").html(src);
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//返回————————————————————————————
function getBack() {
    $(".back").click(function () {
        var msResourceId = $("#msResourceId").val();
        var URL = 'report_management';
        var name = $("#name").val();
        var phone = $("#phone").val();
        var syReportCtgyId = $("#syReportCtgyId").val();
        var targetType = $("#targetType").val();
        var status = $("#status").val();
        var num = $("#num").val();
        $(location).attr("href",URL+'?msResourceId='+ msResourceId + '&name=' + name +
            '&phone=' + phone + '&syReportCtgyId=' + syReportCtgyId + '&targetType=' + targetType +
            '&status='+ status + "&num=" + num);
    });
}