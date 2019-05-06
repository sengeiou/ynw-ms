var num = 1;
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('关注关系列表');
    //获取数据总数并分页
    getNum();
    //未来点击事件
    bottom_click();
    //隐藏操作框
    control_tip();
    //获取省份
    getProvince();
    // 城市筛选
    $("#province").change(function () {
        getCity();
    });
    $("#city").click(function () {
        var province = $("#province").val();
        if (province == '') {
            $("#city").html('<option value="">--请选择--</option>');
            layer.msg("请先选择省份",{icon:2});
            return false;
        }
    });
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


//获取城市-------------------------------
function getCity() {
    var province = $("#province").val();
    $.ajax({
        type:"post",
        url:"../../../city/findCityByProvince",
        data: {
            "bdProvinceId":province
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            var citySelect = '<option value="">--请选择--</option>';
            if (res.code == 1) {
                $.each(res.result,function (index,value) {
                    citySelect += '<option value="'+ value.bdCityId +'">'+ value.name +'</option>';
                });
                $("#city").html(citySelect);
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取省份-------------------------------
function getProvince() {
    $.ajax({
        type:"post",
        url:"../../../province/findProvinceAll",
        data: {
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var provinceSelect ='<option value="">--请选择--</option>';
                $.each(res.result,function (index,value) {
                    provinceSelect += '<option value="'+ value.bdProvinceId +'">'+ value.provinceName +'</option>';
                });
                $("#province").html(provinceSelect);
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
    var sex = $("#sex").val();
    var province = $("#province").val();
    var city = $("#city").val();
    $.ajax({
        type:"post",
        url:"../../../attention/count",
        data: {
            "phoneNumber":phone,
            "sex":sex,
            "provinceId":province,
            "bdCityId":city
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
    var sex = $("#sex").val();
    var province = $("#province").val();
    var city = $("#city").val();
    $.ajax({
        type:"post",
        url:"../../../attention/conditionQueryAttention",
        data: {
            "phoneNumber":phone,
            "sex":sex,
            "provinceId":province,
            "bdCityId":city,
            "nowPage": nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                var URL = $("#URL").val();
                var resourceId = $("#resourceId").val();
                $.each(res.result,function (index,value) {
                    // var time = new Date(value.createTime);
                    value.createTime = getDate(value.createTime);
                    if (value.srcUser.sex == 1) {
                        value.srcUser.sex = "女";
                    } else {
                        value.srcUser.sex = "男";
                    }
                    if (value.objUser.sex == 1) {
                        value.objUser.sex = "女";
                    } else {
                        value.objUser.sex = "男";
                    }
                    str += '<tr class="gradeX"><td><img src="'+ value.srcUser.headImageUrl +'" style="width: 60px;height: 40px"></td>';
                    str += '<td>'+value.srcUser.phoneNumber+'</td>';
                    str += '<td>'+value.srcUser.nickname+'</td>';
                    str += '<td>'+value.srcUser.sex+'</td>';
                    str += '<td>'+value.srcUser.bdCityName+'</td>';
                    str += '<td><a href="user_details_management?acUserId='+ value.srcUser.acUserId +'&URL='+ URL +'&msResourceId='+ resourceId +'" style="margin-left: 10px">详情</a></td>';
                    str += '<td><img src="'+ value.objUser.headImageUrl +'" style="width: 60px;height: 40px"></td>';
                    str += '<td>'+value.objUser.phoneNumber+'</td>';
                    str += '<td>'+value.objUser.nickname+'</td>';
                    str += '<td>'+value.objUser.sex+'</td>';
                    str += '<td>'+value.objUser.bdCityName+'</td>';
                    str += '<td><a href="user_details_management?acUserId='+ value.objUser.acUserId +'&URL='+ URL +'&msResourceId='+ resourceId +'" style="margin-left: 10px">详情</a></td>';
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