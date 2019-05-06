$(function () {
    var msUserId = $("#userId").val();
    var msResourceId = $("#resourceId").val();
    var resourceName = $("#resourceName").val();
    $.ajax({
        type:"post",
        url:"../../resource/conditionQueryResourceByUserId",
        data: {
            "type": 1,
            "is_hide": 0,
            "msUserId": msUserId,
            "parentId": msResourceId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '<a href="javascript:;" class="ue-clear"><i class="nav-ivon"></i><span class="nav-text">'+ resourceName +'</span></a>';
                str += '<ul class="subnav">';
                $.each(res.result,function (index,value) {
                    var dateId = index +1;
                    str += '<li class="subnav-li" href="../../'+ value.sourceUrl +'?msResourceId='+value.msResourceId+'" data-id="'+ dateId +'">';
                    str += '<a href="javascript:;" class="ue-clear"><i class="subnav-icon"></i>';
                    str += '<span class="subnav-text">'+value.name+'</span></a></li>';
                })
                str += '</ul>';
                $("#current").html(str);
            }
        }
    });
})