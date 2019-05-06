$(function () {
    //返回
    getBack();
    getContent();

});

//查询content
function getContent() {
    var atRegisterId = $("#atRegisterId").val();
    $.ajax({
        type:"post",
        url:"../../../register/findById",
        data: {
            "atRegisterId":atRegisterId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $(".content").html(res.result.content);
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