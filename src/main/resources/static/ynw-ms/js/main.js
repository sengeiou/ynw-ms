$(function () {
    var msUserId = $("#user-name").val();
    $.ajax({
        type:"post",
        url:"resource/conditionQueryResourceByUserId",
        data: {
            "type": 0,
            "is_hide": 0,
            "msUserId":msUserId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                str += '<ul class="nav ue-clear top-nav">';
                $.each(res.result,function (index,value) {
                    str += '<li href="'+value.sourceUrl+'?msResourceId='+value.msResourceId+'&name='+ value.name +'"><a href="javascript:;">'+value.name+'</a></li>';
                })
                str += '</ul>';
                $("#nav-wrap").html(str);
            }
        }
    });
    // $(".menu").live("click",function () {
    //     // var url = $(".menu").children(".url").val();
    //     // $(location).attr("href",url);
    //     $("#mainIframe").attr("src",$(this).attr("href"))
    // })
})