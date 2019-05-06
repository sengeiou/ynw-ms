var num = 1;
var acUserId = "";
var userName = '';
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('用户信息管理');
    //获取数据总数并分页
    getNum();
    $("#checkAll").click(function () {
        selectAll();
    })
    $(document).on("click","input[type='checkbox'][name='checkedres']",function(){
        selectBox();
    });
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
    //未来点击事件
    bottom_click();
    //隐藏操作框
    control_tip();
    //链接带参时
    setInterface();
});

function setInterface() {
    var name = GetQueryString("name");
    if (name != null) {
        $("#name").val(name);
    }
    var phone = GetQueryString("phone");
    if (phone != null) {
        $("#phone").val(phone);
    }
    var no = GetQueryString("no");
    if (no != null) {
        $("#no").val(no);
    }
    var sex = GetQueryString("sex");
    if (sex != null) {
        $("#sex").val(sex);
    }
    var status = GetQueryString("status");
    if (status != null) {
        $("#status").val(status);
    }
    var imageStatus = GetQueryString("imageStatus");
    if (imageStatus != null) {
        $("#imageStatus").val(imageStatus);
    }
    var idVerifyStatus = GetQueryString("idVerifyStatus");
    if (idVerifyStatus != null) {
        $("#idVerifyStatus").val(idVerifyStatus);
    }
    var num1 = GetQueryString("num");
    if (num1 != null) {
        num = num1;
    }
    var count = getNum();
    getData(num);
    setPage(count)
}

//获取url头部参数
function GetQueryString(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  decodeURI(r[2]); return null;
}

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

    //设置用户状态
    $(document).on('click','#update_status',function(){
        var flag = getBox();
        if (flag == 1) {
            getAcUser();
            $(".operation-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    //设置用户状态
    $(".status_confirm").click(function () {
        getUserId();
        update_status();
    });

    /**
     *  点击跳转详情
     */
    $(document).on("click",".data-content", function () {
       var url = $(this).prev().val();
       skip(url);
    });

    /**
     *  跳转用户标签管理
     */
    $(document).on("click","#user_label", function () {
        //获取自己的资源编号
        var msResourceId = $(this).prev().val();
        skip("user_label_management?parentId="+$("#resourceId").val() +"&msResourceId="+ msResourceId);
    });

    /**
     *  跳转等级管理
     */
    $(document).on("click","#hierarchy", function () {
        var msResourceId = $(this).prev().val();
        skip("hierarchy_management?parentId="+$("#resourceId").val() +"&msResourceId="+ msResourceId);
    });

    /**
     * 下载昨天的活跃用户信息
     */
    $(document).on("click","#activeYesterday", function () {
        window.location.href="../../../acUser/activeYesterday";
    });

    /**
     * 下载昨日注册用户信息
     */
    $(document).on("click","#yesterdayRegistered", function () {
        window.location.href="../../../acUser/yesterdayRegistered";
    });

    /**
     * 下载昨日用户统计信息
     */
    $(document).on("click","#yesterdayStatistics", function () {
        window.location.href="../../../acUser/yesterdayStatistics";
    });

    $(document).on('click','#roboticize', function () {
        $("#robot input[name='robotNum']").val("");
        $("#robot").show();
    });

    $("#confirm").click(function () {
        var robotNum = $("#robot input[name='robotNum']").val();
        if (robotNum == '') {
            layer.msg( '请输入机器人数量',{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../acUser/roboticize",
            data: {
                "robotNumber":robotNum
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    $("#robot").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });
    
}

/**
 *  跳转并获取参数
 */
function skip(url) {
    url += "&idVerifyStatus="+ $("#idVerifyStatus").val() + "&name="+ $("#name").val() + "&phone="+ $("#phone").val() + "&no=" + $("#no").val() + "&sex=" + $("#sex").val() + "&province=" + $("#province").val() +
        "&city=" + $("#city").val() + "&status=" + $("#status").val() + "&imageStatus=" + $("#imageStatus").val() + "&num=" + num;
    $(location).attr("href", url);
}

//修改用户状态
function update_status() {
    var status = $(".role-body-right input[name='status']:checked").val();
    $.ajax({
        type:"post",
        url:"../../../acUser/update",
        data: {
            "acUserId":acUserId,
            "LogContent":"修改用户状态【姓名:"+userName+" 编号:"+acUserId+"】",
            "status":status
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                layer.msg("修改成功",{icon:1});
                $(".operation-data").hide();
                var count = getNum();
                setPage(count);
                getData(num)
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取选中的用户编号-------------------------------
function getUserId() {
    var box = $("input[type='checkbox'][name='checkedres']");
    var length = box.length;
    for (var i = 0; i < length; i++) {
        if (box[i].checked) {
            acUserId = box[i].value;
            userName = box[i].parentNode.nextSibling.nextSibling.nextSibling.textContent;
        }
    }
}

//获取单个用户信息-------------------------------
function getAcUser() {
    getUserId();
    $.ajax({
        type:"post",
        url:"../../../acUser/findById",
        data: {
            "acUserId":acUserId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var result = res.result;
                $('.role-body-right span:nth-child(1)').text(result.nickname);
                $('.role-body-right span:nth-child(3)').text(result.phoneNumber);
                $(".role-body-left img").prop("src",result.headImageUrl);
                if (result.sex == 1) {
                    $('.role-body-right span:nth-child(5)').text('女');
                } else {
                    $('.role-body-right span:nth-child(5)').text('男');
                }
                $('.role-body-right span:nth-child(7)').text(result.no);
                if (result.status == 0) {
                    $(".role-body-right input[name='status']:last").prop("checked",true);
                } else {
                    $(".role-body-right input[name='status']:first").prop("checked",true);
                }
            }
        }
    });
}


//获取城市-------------------------------
function getCity() {
    var province = $("#province").val();
    if (province == "") {
        return false;
    }
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
                var city = GetQueryString("city");
                if (city != null) {
                    $("#city").val(city);
                }
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
                var province = GetQueryString("province");
                if (province != null) {
                    $("#province").val(province);
                    getCity();
                }
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
    var nickname = $("#name").val();
    var phone = $("#phone").val();
    var no = $("#no").val();
    var sex = $("#sex").val();
    var province = $("#province").val();
    var city = $("#city").val();
    var status = $("#status").val();
    var imageStatus = $("#imageStatus").val();
    var idVerifyStatus = $("#idVerifyStatus").val();
    $.ajax({
        type:"post",
        url:"../../../acUser/count",
        data: {
            "nickname": nickname,
            "phoneNumber":phone,
            "no":no,
            "sex":sex,
            "provinceId":province,
            "bdCityId":city,
            "status":status,
            "imageStatus":imageStatus,
            "idVerifyStatus":idVerifyStatus
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
    var nickname = $("#name").val();
    var phone = $("#phone").val();
    var no = $("#no").val();
    var sex = $("#sex").val();
    var province = $("#province").val();
    var city = $("#city").val();
    var status = $("#status").val();
    var imageStatus = $("#imageStatus").val();
    var idVerifyStatus = $("#idVerifyStatus").val();
    $.ajax({
        type:"post",
        url:"../../../acUser/conditionQueryAcUser",
        data: {
            "nickname": nickname,
            "phoneNumber":phone,
            "no":no,
            "sex":sex,
            "provinceId":province,
            "bdCityId":city,
            "status":status,
            "imageStatus":imageStatus,
            "idVerifyStatus":idVerifyStatus,
            "nowPage": nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                var URL = $("#URL").val();
                var resourceId = $("#resourceId").val();
                var imageStatus = '';
                $.each(res.result,function (index,value) {
                    // var time = new Date(value.createTime);
                    value.createTime = getDate(value.createTime);
                    if (value.sex == 1) {
                        value.sex = "女";
                    } else {
                        value.sex = "男";
                    }
                    if (value.status == 1) {
                        value.status = "正常";
                    } else {
                        value.status = "失效";
                    }
                    if (value.imageStatus == 0) {
                        imageStatus = '<td>已审核</td>';
                    } else {
                        imageStatus = '<td style="color: red">未审核</td>';
                    }
                    if (value.idVerifyStatus == -1) {
                        value.idVerifyStatus = '<td>未实名认证</td>'
                    } else if (value.idVerifyStatus == 0) {
                        value.idVerifyStatus = '<td style="color: red">实名认证审核中</td>'
                    } else if (value.idVerifyStatus == 1) {
                        value.idVerifyStatus = '<td>已实名认证</td>'
                    } else {
                        value.idVerifyStatus = '<td>实名认证失败</td>'
                    }
                    str += '<tr class="gradeX"><td id="td_input"><input type="checkbox" name="checkedres" value="'+value.acUserId+'"></td>';
                    str += '<td><img src="'+ value.headImageUrl +'" style="width: 60px;height: 40px"></td>';
                    str += '<td>'+value.phoneNumber+'</td>';
                    str += '<td>'+value.nickname+'</td>';
                    str += '<td>'+value.no+'</td>';
                    str += '<td>'+value.sex+'</td>';
                    str += '<td>'+value.age+'</td>';
                    str += '<td>'+value.bdCityName+'</td>';
                    str += '<td>'+value.createTime+'</td>';
                    str += '<td>'+value.status+'</td>';
                    str += imageStatus;
                    str += value.idVerifyStatus;
                    str += '<td><input type="hidden" value="user_details_management?acUserId='+ value.acUserId +'&URL='+ URL +'&msResourceId='+ resourceId +'"/>' +
                        '<a class="data-content" href="javascript:;" style="margin-left: 10px">详情</a></td></tr>';
                })
                // javascript:;
                $("#admin").html(str);
                //选中状态发生改变是隐藏功能弹窗
                control_tip_box();
            } else if (res.code == 8) {
                $("#admin").empty();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}