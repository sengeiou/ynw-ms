var num = 1;
$(function () {
    //第一页数据
    getData(1);
    //获取数据总数并分页
    getNum();
    //未来点击事件
    bottom_click();
    //返回
    getBack();
    //隐藏操作框
    control_tip();
});


/**
 *  未来点击事件
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
        getData(num);
    });

    //根据举报对象查询举报信息
    $(document).on("click",".report-details", function () {
       var targetId = $(this).prev().val();
        $.ajax({
            type:"post",
            url:"../../../report/findReportByTarget",
            data: {
                "targetId":targetId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    var src = '<div class="report-body-content-item">';
                    $.each(res.result,function (index,value) {
                        var date = getDate(value.createTime);
                        src += '<div>'+ value.acUserName +'</div>';
                        src += '<div>'+ value.reportCtgyName +'</div>';
                        src += '<div>'+ date +'</div>';
                    });
                    src += '</div>';
                    $(".report-body-content").html(src);
                    $(".unified-closed").show();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
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
    var userPhone = $("#userPhone").val();
    var name = $("#userName").val();
    $.ajax({
        type:"post",
        url:"../../../report/countReportTarget",
        data: {
            "phone":userPhone,
            "name":name
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
    var userPhone = $("#userPhone").val();
    var name = $("#userName").val();
    // alert(msUserId + ":"+beginDate + ":"+endDate + ":")
    $.ajax({
        type:"post",
        url:"../../../report/findReportTarget",
        data: {
            "phone":userPhone,
            "name":name,
            "nowPage":nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                $.each(res.result,function (index,value) {
                    var date = getDate(value.mood.createTime);
                    var length = value.mood.labelList.length;
                    var moodLabel = '';
                    $.each(value.mood.labelList,function (indexs,labels) {
                        moodLabel += labels;
                        if (indexs != length - 1) {
                            moodLabel += '&';
                        }
                    });
                    str += '<tr class="gradeX">';
                    str += '<td><img src="'+ value.mood.userImage +'" style="width: 60px;height: 40px"></td>';
                    str += '<td>'+value.mood.userName+'</td>';
                    str += '<td>'+value.mood.userPhone+'</td>';
                    str += '<td>'+moodLabel+'</td>';
                    str += '<td>'+value.reportNumber+'</td>';
                    str += '<td>'+date+'</td>';
                    str += '<td><input type="hidden" value="'+ value.mood.dsMoodId +'" /><a class="report-details" href="javascript:;" style="margin-left: 10px">详情</a></td></tr>';
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
        var msResourceId = $("#resourceId").val();
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