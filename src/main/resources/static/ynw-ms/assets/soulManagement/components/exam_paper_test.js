var num = 1;
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('用户测试管理');
    //获取数据总数并分页
    getNum();
    //未来点击事件
    bottom_click();

});


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
    var examTitle = $("#examTitle").val();
    $.ajax({
        type:"post",
        url:"../../../examPaperTest/count",
        data: {
            "userPhone":userPhone,
            "examTitle":examTitle
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
    var examTitle = $("#examTitle").val();
    // alert(msUserId + ":"+beginDate + ":"+endDate + ":")
    $.ajax({
        type:"post",
        url:"../../../examPaperTest/conditionQueryExamPaperTest",
        data: {
            "userPhone":userPhone,
            "examTitle":examTitle,
            "nowPage":nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                var URL = $("#URL").val();
                var resourceId = $("#resourceId").val();
                $.each(res.result,function (index,value) {
                    var date = getDate(value.createTime);
                    if (value.sumScore == 0) {
                        value.sumScore = '-';
                    }
                    str += '<tr class="gradeX">';
                    str += '<td><img src="'+ value.userImage +'" style="width: 60px;height: 40px"></td>';
                    str += '<td>'+value.userName+'</td>';
                    str += '<td>'+value.userPhone+'</td>';
                    str += '<td>'+value.examTitle+'</td>';
                    str += '<td>'+value.answer+'</td>';
                    str += '<td>'+value.sumScore+'</td>';
                    str += '<td>'+date+'</td>';
                    str += '<td><a href="test_details_management?kbExampaperTestId='+ value.kbExampaperTestId +'&URL='+ URL +'&msResourceId='+ resourceId +'" style="margin-left: 10px">详情</a></td></tr>';
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