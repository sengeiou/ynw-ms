var num = 1;
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('操作日志管理');
    //获取数据总数并分页
    getNum();
    //未来点击事件
    bottom_click();
    //获取所有操作员
    getUser();
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
        getData(num)
    });

}

//获取操作员姓名-------------------------------
function getUser() {
    $.ajax({
        type:"post",
        url:"../../../admin/selectAll",
        data: {
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                $.each(res.result,function (index,value) {
                    src += '<option value="' +value.msUserId+ '">' +value.realName+ '</option>';
                });
                $("#msUserId").html(src);
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
    var msUserId = $("#msUserId").val();
    var beginDate = $("#beginDate").val();
    var endDate = $("#endDate").val();
    $.ajax({
        type:"post",
        url:"../../../optLog/count",
        data: {
            "msUserId":msUserId,
            "beginDate":beginDate,
            "endDate":endDate
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
    var msUserId = $("#msUserId").val();
    var beginDate = $("#beginDate").val();
    var endDate = $("#endDate").val();
    // alert(msUserId + ":"+beginDate + ":"+endDate + ":")
    $.ajax({
        type:"post",
        url:"../../../optLog/conditionQueryOptLog",
        data: {
            "msUserId":msUserId,
            "beginDate":beginDate,
            "endDate":endDate,
            "nowPage":nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                $.each(res.result,function (index,value) {
                    var date = getDate(value.createTime);
                    str += '<tr class="gradeX">';
                    str += '<td>'+value.msUserName+'</td>';
                    str += '<td>'+value.loginIp+'</td>';
                    str += '<td>'+date+'</td>';
                    str += '<td>'+value.content+'</td></tr>';
                })
                $("#ParaConf").html(str);
            } else if (res.code == 8) {
                $("#ParaConf").empty();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}