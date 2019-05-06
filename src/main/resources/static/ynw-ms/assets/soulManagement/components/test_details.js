$(function () {
    //返回
    getBack();
    //获取数据
    getData();
});

//获取数据————————————————————————————
function getData() {
    var kbExampaperTestId = $("#kbExampaperTestId").val();
    $.ajax({
        type:"post",
        url:"../../../examPaperTest/findByExamId",
        data: {
            "kbExampaperTestId":kbExampaperTestId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '';
                $(".data-body").css('height', res.result.length*180);
                $.each(res.result,function (index,value) {
                    var date = getDate(value.createTime);
                    src += '<div class="exam-body">';
                    src += '<div class="data-top">'+ date +'</div>';
                    src += '<div class="exam-body-comment" style="background: #fff576">'+ value.subjectName +'</div>';
                    src += '<div class="exam-body-comment" style="background: #f6facc"><div class="exam-body-left"><img src="../../img/check.png"></div>';
                    src += '<div class="exam-body-right">'+ value.subjectItemName +'</div>';
                    src += '</div></div>';
                });
                $(".data-body").html(src);
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
        var URL = $("#URL").val();
        $(location).attr("href",'../../../'+URL+'?msResourceId='+msResourceId);
    });
}