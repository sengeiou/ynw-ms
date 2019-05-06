$(function () {
    //返回
    getBack();
    //获取当前动态信息
    getMoodOne();
    //获取评论
    getComment();
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

//返回————————————————————————————
function getBack() {
    $(".back").click(function () {
        var msResourceId = $("#msResourceId").val();
        var URL = $("#URL").val();
        $(location).attr("href",'../../../'+URL+'?msResourceId='+msResourceId);
    });
}