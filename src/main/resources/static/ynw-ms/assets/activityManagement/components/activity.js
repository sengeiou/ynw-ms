var num = 1;
var typeId = '';
var typeName = '';
var activityId = '';
var activityName = '';
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('一周情侣活动');
    //获取数据总数并分页
    getNum();
    //未来点击事件
    bottom_click();
    //获取活动类型
    getReportType();
    //隐藏操作框
    control_tip();
    //隐藏举报分类操作弹窗
    control_tip_report();
    $("#checkAll").click(function () {
        selectAll();
    })
    $(document).on("click","input[type='checkbox'][name='checkedres']",function(){
        selectBox();
    });
    //选择照片
    $(".picture-body img").click(function(){
        $(this).next().click();
    });
    /**
     * 显示选择图片路径
     */
    $("input[name='fileName']").change(function(){
        var file = $(this);
        // alert(fileUrl)
        // fileUrl = fileUrl.substring(fileUrl.lastIndexOf("\\")+1);
        var objUrl = getObjectURL(this.files[0]);
        if (objUrl) {
            // 在这里修改图片的地址属性
            file.prev().prop("src",objUrl);
            file.next().val(1);
        }
    });

});

//根据不同浏览器获取本地图片url
function getObjectURL(file) {
    var url = null ;
    // 下面函数执行的效果是一样的，只是需要针对不同的浏览器执行不同的 js 函数而已
    if (window.createObjectURL!=undefined) { // basic
        url = window.createObjectURL(file) ;
    } else if (window.URL!=undefined) { // mozilla(firefox)
        url = window.URL.createObjectURL(file) ;
    } else if (window.webkitURL!=undefined) { // webkit or chrome
        url = window.webkitURL.createObjectURL(file) ;
    }
    return url ;
}

//隐藏举报分类操作弹窗
function control_tip_report() {
    $(".report-classify-cancel").click(function () {
        $(".operation-data,#delete-data").hide();
    });
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
        getData(num)
    });

    /**
     *  添加活动
     */
    $(document).on('click','#add_date', function () {
       $("#add-activity-data .operation-activity-body input").val('');
        $("#add-activity-data .operation-activity-body select").val('');
        $("#add-activity-data .operation-activity-body textarea").val('');
        $("#add-activity-data").show();
    });

    /**
     *  添加
     */
    $("#add-activity-confirm").click(function () {
        var name = $("#add-activity-data input[name='name']").val();
        if (name == '') {
            layer.msg("活动名称不能为空,请重新输入",{icon:2});
            return false;
        }
        var atCtgyId = $("#add-activity-data select[name='atCtgyId']").val();
        if (atCtgyId == '') {
            layer.msg("活动分类不能为空,请重新输入",{icon:2});
            return false;
        }
        var beginTime = $("#add-activity-data input[name='beginTime']").val();
        if (beginTime == '') {
            layer.msg("活动开始时间不能为空,请重新输入",{icon:2});
            return false;
        }
        var endTime = $("#add-activity-data input[name='endTime']").val();
        if (endTime == '') {
            layer.msg("活动结束时间不能为空,请重新输入",{icon:2});
            return false;
        }
        var applyBeginTime = $("#add-activity-data input[name='applyBeginTime']").val();
        if (applyBeginTime == '') {
            layer.msg("活动开始报名时间不能为空,请重新输入",{icon:2});
            return false;
        }
        var applyEndTime = $("#add-activity-data input[name='applyEndTime']").val();
        if (applyEndTime == '') {
            layer.msg("活动报名结束不能为空,请重新输入",{icon:2});
            return false;
        }
        if ($("#add-activity-data .picture-tip").val() != 1) {
            layer.msg("请上传图片",{icon:2});
            return false;
        }
        var content = $("#add-activity-data textarea").val();
        if (content == '') {
            layer.msg("活动活动内容描述不能为空,请重新输入",{icon:2});
            return false;
        }
        $("#add-activity-data input[name='LogContent']").val("新增活动【 活动名称："+ name +"】")
        var formDate = new FormData($("#add-activity")[0]);
        $.ajax({
            type:"post",
            url: '../../../register/insert',
            data:formDate,
            contentType: false,
            processData: false,
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#add-activity-data").hide();
                    var count = getNum();
                    setPage(count);
                    getData(num)
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    /**
     *  更新活动
     */
    $(document).on('click','#update_date', function () {
        var flag = getBox();
        if (flag == 1) {
            //获取勾选数据
            getCheckData();
            //获取初始数据
            getActivity();
            $("#update-activity-data pictureTip").val(0);
            $("#update-activity-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    $("#update-activity-confirm").click(function () {
        var name = $("#update-activity-data input[name='name']").val();
        if (name == '') {
            layer.msg("活动名称不能为空,请重新输入",{icon:2});
            return false;
        }
        var atCtgyId = $("#update-activity-data select[name='atCtgyId']").val();
        if (atCtgyId == '') {
            layer.msg("活动分类不能为空,请重新输入",{icon:2});
            return false;
        }
        var beginTime = $("#update-activity-data input[name='beginTime']").val();
        if (beginTime == '') {
            layer.msg("活动开始时间不能为空,请重新输入",{icon:2});
            return false;
        }
        var endTime = $("#update-activity-data input[name='endTime']").val();
        if (endTime == '') {
            layer.msg("活动结束时间不能为空,请重新输入",{icon:2});
            return false;
        }
        var applyBeginTime = $("#update-activity-data input[name='applyBeginTime']").val();
        if (applyBeginTime == '') {
            layer.msg("活动开始报名时间不能为空,请重新输入",{icon:2});
            return false;
        }
        var applyEndTime = $("#update-activity-data input[name='applyEndTime']").val();
        if (applyEndTime == '') {
            layer.msg("活动报名结束不能为空,请重新输入",{icon:2});
            return false;
        }
        var content = $("#update-activity-data textarea").val();
        if (content == '') {
            layer.msg("活动活动内容描述不能为空,请重新输入",{icon:2});
            return false;
        }
        $("#update-activity-data input[name='LogContent']").val("更新活动【 活动名称："+ name +"】")
        $("#update-activity-data input[name='atRegisterId']").val(activityId);
        var formDate = new FormData($("#update-activity")[0]);
        $.ajax({
            type:"post",
            url: '../../../register/update',
            data: formDate,
            // contentType: "application/json",
            dataType: "json",
            contentType: false,
            processData: false,
            success: function(res){
                if (res.code == 1) {
                    var count = getNum();
                    setPage(count);
                    getData(num)
                    $("#update-activity-data").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    /**
     *  删除活动
     */
    $(document).on('click','#delete_date', function () {
        if (getBox() == 1) {
            //获取勾选数据
            getCheckData();
            $("#delete-activity-data span").text(activityName);
            $("#delete-activity-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    /**
     *  删除
     */
    $("#delete-activity-confirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../register/delete",
            data: {
                "atRegisterId":activityId,
                "LogContent":"删除活动【 活动名称："+ activityName +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#delete-activity-data").hide();
                    var count = getNum();
                    setPage(count);
                    getData(num)
                    layer.msg("删除成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //分类管理
    $(document).on('click','#register_classify',function(){
        $("#nowPage-date").show();
    });

    //退出活动分类管理
    $(document).on('click','#drop_out',function(){
        $("#nowPage-date").hide();
    });

    //添加举报分类
    $(document).on('click','#add_classify_date',function(){
        $("#add-report input").val("");
        $("#add-report-classify-data").show();
    });

    //添加
    $("#add-report-confirm").click(function () {
        var name = $("#add-report-classify-data input[name='name']").val();
        if (name == '') {
            layer.msg('活动分类名称不能为空',{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../register-classify/insert",
            data: {
                "name":name,
                "LogContent":"添加活动分类【 活动分类名称："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#add-report-classify-data").hide();
                    getReportType();
                    layer.msg("添加成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //编辑活动分类
    $(document).on('click','#update_classify_date',function(){
        //获取选择的举报分类信息
        get_report_typr_data();
        // alert(typeId+":"+typeName)
        $("#update-report-classify-data input[name='name']").val(typeName);
        $("#update-report-classify-data").show();
    });

    $("#update-report-confirm").click(function () {
        var name = $("#update-report-classify-data input[name='name']").val();
        if (name == '') {
            layer.msg('举报分类名称不能为空',{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../register-classify/update",
            data: {
                "name":name,
                "atCtgyId":typeId,
                "LogContent":"编辑活动分类【 活动分类名称："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#update-report-classify-data").hide();
                    getReportType();
                    layer.msg("编辑成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //删除
    $(document).on('click','#delete_classify_date',function(){
        //获取选择的举报分类信息
        get_report_typr_data();
        $(".update-body span").text(typeName);
        $("#delete-data").show();
    });

    //删除
    $(".confirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../register-classify/delete",
            data: {
                "atCtgyId":typeId,
                "LogContent":"删除活动分类【 活动分类名称："+ typeName +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#delete-data").hide();
                    getReportType();
                    layer.msg("删除成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //解散群聊
    $(document).on("click","#dissolve_group", function () {
        if (getBox() == 1) {
            //获取勾选数据
            getCheckData();
            $(".dissolveGroupBody span").text(activityName).css("color","red");
            $("#dissolveGroup").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    $("#dissolveGroupConfirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../register/deleteGroup",
            data: {
                "registerId":activityId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#dissolveGroup").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

}

//获取单个活动信息
function getActivity() {
    $.ajax({
        type:"post",
        url:"../../../register/findById",
        data: {
            "atRegisterId":activityId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var result = res.result;
                //转时间格式
                result.beginTime = getDate(result.beginTime);
                result.endTime = getDate(result.endTime);
                result.applyBeginTime = getDate(result.applyBeginTime);
                result.applyEndTime = getDate(result.applyEndTime);
                //截取成符合条件的字符
                result.beginTime = result.beginTime.substr(0,10)
                result.endTime = result.endTime.substr(0,10);
                result.applyBeginTime = result.applyBeginTime.substr(0,10);
                result.applyEndTime = result.applyEndTime.substr(0,10);
                $("#update-activity-data input[name='name']").val(result.name);
                $("#update-activity-data select[name='atCtgyId']").val(result.atCtgyId);
                $("#update-activity-data input[name='beginTime']").val(result.beginTime);
                $("#update-activity-data input[name='endTime']").val(result.endTime);
                $("#update-activity-data input[name='applyBeginTime']").val(result.applyBeginTime);
                $("#update-activity-data input[name='applyEndTime']").val(result.applyEndTime);
                $("#update-activity-data .picture img").prop("src", result.bgImageUrl);
                $("#update-activity-data textarea").val(result.content);
            }
        }
    });
}

/**
 *  获取选中的数据
 */
function getCheckData() {
    var box = $("input[type='checkbox'][name='checkedres']");
    var length = box.length;
    for (var i = 0; i < length; i++) {
        if (box[i].checked) {
            activityId = box[i].value;
            activityName = box[i].parentNode.nextSibling.textContent;
        }
    }
}

//获取举报分类选中文本和值-------------------------------
function get_report_typr_data() {
    typeId = $(".nowPage-left select").val();
    typeName = $(".nowPage-left select").find("option:selected").text();
}

//获取活动类别-------------------------------
function getReportType() {
    $.ajax({
        type:"post",
        url:"../../../register-classify/conditionQueryRegisterClassify",
        data: {
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                var str = '<select style="width: 110%" size="'+ res.result.length +'">';
                $.each(res.result,function (index,value) {
                    src += '<option value="'+ value.atCtgyId +'">'+ value.name +'</option>';
                    str += '<option value="'+ value.atCtgyId +'">'+ value.name +'</option>';
                });
                str += '</select>';
                $("#registerClassify,select[name='atCtgyId']").html(src);
                $(".nowPage-left").html(str);
                $('.nowPage-left select option:first').prop('selected', 'selected');
            } else {
                layer.msg(res.message,{icon:2});
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
    var name = $("#name").val();
    // var atCtgyId = $("#registerClassify").val();
    var status = $("#status").val();
    $.ajax({
        type:"post",
        url:"../../../register/count",
        data: {
            "name":name,
            "status":status,
            // "atCtgyId":atCtgyId
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
    var name = $("#name").val();
    // var atCtgyId = $("#registerClassify").val();
    var status = $("#status").val();
    // alert(name + ":"+phone + ":"+syReportCtgyId + ":"+targetType + ":")
    $.ajax({
        type:"post",
        url:"../../../register/conditionQueryRegister",
        data: {
            "name":name,
            // "atCtgyId":atCtgyId,
            "status":status,
            "nowPage": nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                var resourceId = $("#resourceId").val();
                $.each(res.result,function (index,information) {
                    var value = information.register;
                    value.beginTime = getDate(value.beginTime);
                    value.endTime = getDate(value.endTime);
                    value.applyBeginTime = getDate(value.applyBeginTime);
                    value.applyEndTime = getDate(value.applyEndTime);
                    value.createTime = getDate(value.createTime);
                    value.updateTime = getDate(value.createTime);
                    var status = information.status;
                    if (status == 1) {
                        status = "未开始";
                    } else if (status == 2) {
                        status = "进行中"
                    } else if (status == 3) {
                        status = "已结束"
                    }
                    str += '<tr class="gradeX"><td id="td_input"><input type="checkbox" name="checkedres" value="'+value.atRegisterId+'"></td>';
                    str += '<td>'+value.name+'</td>';
                    str += '<td>'+value.classifyName+'</td>';
                    str += '<td>'+value.beginTime+'</td>';
                    str += '<td>'+value.endTime+'</td>';
                    str += '<td>'+value.applyBeginTime+'</td>';
                    str += '<td>'+value.applyEndTime+'</td>';
                    str += "<td><a href='activity_content?atRegisterId="+ value.atRegisterId +"&resourceId="+ resourceId +"' style='margin-left: 10px'>查看详情</a></td>";
                    str += '<td>'+value.createTime+'</td>';
                    str += '<td>'+value.updateTime+'</td>';
                    str += '<td>'+status+'</td>';
                    str += '<td>'+value.registrationNumber+'</td>';
                    str += "<td><a href='active_user?atRegisterId="+ value.atRegisterId +"&resourceId="+ resourceId +"' style='margin-left: 10px'>查看详情</a></td>";
                    str += "<td><a href='active_task_clock?atRegisterId="+ value.atRegisterId +"&resourceId="+ resourceId +"' style='margin-left: 10px'>查看详情</a></td></tr>";
                })
                $("#report").html(str);
                //选中状态发生改变是隐藏功能弹窗
                control_tip_box();
            } else if (res.code == 8) {
                $("#report").empty();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}