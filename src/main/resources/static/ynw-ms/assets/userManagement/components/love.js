var num = 1;
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('情豆流水列表');
    //获取数据总数并分页
    getNum();
    //未来点击事件
    bottom_click();
    //隐藏操作框
    control_tip();
    //获取字典
    getDictionary();
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

//获取字典-------------------------------
function getDictionary() {
    $.ajax({
        type:"post",
        url:"../../../dictionary/findDictionaryByGroupKey",
        data: {
            "groupKey":'BEAN_BUSINESS_TYPE'

        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                $.each(res.result,function (index,value) {
                    src += '<option value="'+ value.itemKey +'">'+ value.itemValue +'</option>';
                });
                $("#assoBusinessKey").html(src);
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
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
    var type = $("#type").val();
    var assoBusinessKey = $("#assoBusinessKey").val();
    var endDate = $("#endDate").val();
    var beginDate = $("#beginDate").val();
    $.ajax({
        type:"post",
        url:"../../../love/count",
        data: {
            "acUserPhone":phone,
            "type":type,
            "assoBusinessKey":assoBusinessKey,
            "endDate":endDate,
            "beginDate":beginDate
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
    var type = $("#type").val();
    var assoBusinessKey = $("#assoBusinessKey").val();
    var endDate = $("#endDate").val();
    var beginDate = $("#beginDate").val();
    $.ajax({
        type:"post",
        url:"../../../love/conditionQueryLove",
        data: {
            "acUserPhone":phone,
            "type":type,
            "assoBusinessKey":assoBusinessKey,
            "endDate":endDate,
            "beginDate":beginDate,
            "nowPage": nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                var total = 0;
                var expend = 0;
                var income = 0;
                $.each(res.result,function (index,value) {
                    // var time = new Date(value.createTime);
                    value.createTime = getDate(value.createTime);
                    if (value.type == 1) {
                        value.type = "+";
                        income += parseInt(value.quantity);
                    } else {
                        value.type = "-";
                        expend += parseInt(value.quantity);
                    }
                    total += parseInt(value.quantity);
                    str += '<tr class="gradeX"><td><img src="'+ value.acUserImg +'" style="width: 60px;height: 40px"></td>';
                    str += '<td>'+value.acUserPhone+'</td>';
                    str += '<td>'+value.acUserName+'</td>';
                    str += '<td>'+value.type+'</td>';
                    str += '<td>'+value.quantity+'</td>';
                    str += '<td>'+value.assoBusinessName+'</td>';
                    str += '<td>'+value.createTime+'</td> </tr>';
                })
                // javascript:;
                $("#admin").html(str);
                $(".total").text(total);
                $(".expend").text(expend);
                $(".income").text(income);
            } else if (res.code == 8) {
                $("#admin").empty();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}