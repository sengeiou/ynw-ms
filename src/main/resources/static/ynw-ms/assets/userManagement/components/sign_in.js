var num = 1;
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('签到流水列表');
    //获取数据总数并分页
    getNum();
    //未来点击事件
    bottom_click();
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
    var phone = $("#phone").val();
    var signinDate = $("#signinDate").val();
    $.ajax({
        type:"post",
        url:"../../../signIn/count",
        data: {
            "acUserPhone":phone,
            "signinDate":signinDate
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

//获取数据主体
function getData(nowPage) {
    var phone = $("#phone").val();
    var signinDate = $("#signinDate").val();
    $.ajax({
        type:"post",
        url:"../../../signIn/conditionQuerySignIn",
        data: {
            "acUserPhone":phone,
            "signinDate":signinDate,
            "nowPage": nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                $.each(res.result,function (index,value) {
                    // var time = new Date(value.createTime);
                    value.createTime = getDate(value.createTime);
                    value.signinDate = getDate(value.signinDate);
                    str += '<tr class="gradeX"><td><img src="'+ value.acUserImg +'" style="width: 60px;height: 40px"></td>';
                    str += '<td>'+value.acUserPhone+'</td>';
                    str += '<td>'+value.acUserName+'</td>';
                    str += '<td>'+value.signinDate+'</td>';
                    str += '<td>'+value.createTime+'</td> </tr>';
                })
                // javascript:;
                $("#admin").html(str);
            } else if (res.code == 8) {
                $("#admin").empty();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}