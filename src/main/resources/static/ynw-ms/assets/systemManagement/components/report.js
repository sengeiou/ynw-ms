var num = 1;
var typeId = '';
var typeName = '';
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('举报信息管理');
    //获取数据总数并分页
    getNum();
    //未来点击事件
    bottom_click();
    //获取字典
    getDictionary();
    //获取举报类型
    getReportType();
    //隐藏操作框
    control_tip();
    //隐藏举报分类操作弹窗
    control_tip_report();
    //链接带参时
    setInterface();
});

//链接带参时
function setInterface() {
    var name = GetQueryString("name");
    if (name != null) {
        $("#name").val(name);
    }
    var phone = GetQueryString("phone");
    if (phone != null) {
        $("#phoneNumber").val(phone);
    }
    var status = GetQueryString("status");
    if (status != null) {
        $("#status").val(status);
    }
    var num1 = GetQueryString("num");
    if (num1 != null) {
        num = num1;
    }
    var count = getNum();
    getData(num);
    setPage(count);
}

//隐藏举报分类操作弹窗
function control_tip_report() {
    $(".report-cancel").click(function () {
        $(".unified-closed").hide();
    });
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
        getData(num)
    });

    /**
     *  点击跳转统计详情
     */
    $(document).on('click','#statistice',function(){
        var msResourceId = $("#resourceId").val();
        var name = $("#name").val();
        var phoneNumber = $("#phoneNumber").val();
        var syReportCtgyId = $("#syReportCtgyId").val();
        var targetType = $("#targetType").val();
        var status = $("#status").val();
        var url = 'report_mood_management?msResourceId='+ msResourceId + '&name=' + name +
            '&phone=' + phoneNumber + '&syReportCtgyId=' + syReportCtgyId + '&targetType=' + targetType +
            '&status='+ status + "&num=" + num;
        $(location).attr("href", url);
    });

    /**
     * 点击跳转举报对象详情
     */
    $(document).on("click",".target-details", function () {
        var targetId = $(this).prev().val();
        var resourceId = $("#resourceId").val();
        var name = $("#name").val();
        var phoneNumber = $("#phoneNumber").val();
        var syReportCtgyId = $("#syReportCtgyId").val();
        var targetType = $("#targetType").val();
        var status = $("#status").val();
        var url = 'mood_details_management?targetId='+ targetId + '&msResourceId='+ resourceId + '&name=' + name +
            '&phone=' + phoneNumber + '&syReportCtgyId=' + syReportCtgyId + '&targetType=' + targetType +
        '&status='+ status + "&num=" + num;
        $(location).attr("href", url);
    });

    $(document).on('click','#report_class',function(){
        $("#nowPage-date").show();
    });

    $(document).on('click','#drop_out',function(){
        $("#nowPage-date").hide();
    });

    //添加举报分类
    $(document).on('click','#add_date',function(){
        $("#add-report input").val("");
        $("#add-report-classify-data").show();
    });

    //添加
    $("#add-report-confirm").click(function () {
        var name = $("#add-report-classify-data input[name='name']").val();
        if (name == '') {
            layer.msg('举报分类名称不能为空',{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../report/insert",
            data: {
                "name":name,
                "LogContent":"添加举报分类【 举报分类名称："+ name +"】"
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

    //编辑举报分类
    $(document).on('click','#update_date',function(){
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
            url:"../../../report/update",
            data: {
                "name":name,
                "syReportCtgyId":typeId,
                "LogContent":"编辑举报分类【 举报分类名称："+ name +"】"
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
    $(document).on('click','#delete_date',function(){
        //获取选择的举报分类信息
        get_report_typr_data();
        $(".update-body span").text(typeName);
        $("#delete-data").show();
    });

    //删除
    $(".confirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../report/delete",
            data: {
                "syReportCtgyId":typeId,
                "LogContent":"删除举报分类【 举报分类名称："+ typeName +"】"
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

    //上移
    $(document).on('click','#move_up',function(){
        //获取选择的举报分类信息
        get_report_typr_data();
        $.ajax({
            type:"post",
            url:"../../../report/moveUp",
            data: {
                "syReportCtgyId":typeId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("移动成功",{icon:1});
                    getReportType();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //下移
    $(document).on('click','#move_down',function(){
        //获取选择的举报分类信息
        get_report_typr_data();
        $.ajax({
            type:"post",
            url:"../../../report/moveDown",
            data: {
                "syReportCtgyId":typeId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("移动成功",{icon:1});
                    getReportType();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

}

//获取举报分类选中文本和值-------------------------------
function get_report_typr_data() {
    typeId = $(".nowPage-left select").val();
    typeName = $(".nowPage-left select").find("option:selected").text();
}

//获取举报类别-------------------------------
function getReportType() {
    $.ajax({
        type:"post",
        url:"../../../report/findReportTypeAll",
        data: {
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                var str = '<select style="width: 110%" size="'+ res.result.length +'">';
                $.each(res.result,function (index,value) {
                    src += '<option value="'+ value.syReportCtgyId +'">'+ value.name +'</option>';
                    str += '<option value="'+ value.syReportCtgyId +'">'+ value.name +'</option>';
                });
                str += '</select>';
                $("#syReportCtgyId").html(src);
                $(".nowPage-left").html(str);
                $('.nowPage-left select option:first').prop('selected', 'selected');
                //链接带参进入时
                var syReportCtgyId = GetQueryString("syReportCtgyId");
                if (syReportCtgyId != null) {
                    $("#syReportCtgyId").val(syReportCtgyId);
                    var count = getNum();
                    getData(num);
                    setPage(count);
                }
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取字典-------------------------------
function getDictionary() {
    $.ajax({
        type:"post",
        url:"../../../dictionary/findDictionaryByGroupKey",
        data: {
            "groupKey":'REPORT_TARGET_TYPE'
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                $.each(res.result,function (index,value) {
                    src += '<option value="'+ value.itemKey +'">'+ value.itemValue +'</option>';
                });
                $("#targetType").html(src);
                //链接带参进入时
                var targetType = GetQueryString("targetType");
                if (targetType != null) {
                    $("#targetType").val(targetType);
                    var count = getNum();
                    getData(num);
                    setPage(count);
                }
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
    var phone = $("#phoneNumber").val();
    var syReportCtgyId = $("#endDate").val();
    var targetType = $("#targetType").val();
    var status = $("#status").val();
    $.ajax({
        type:"post",
        url:"../../../report/count",
        data: {
            "acUserName":name,
            "acUserPhone":phone,
            "syReportCtgyId":syReportCtgyId,
            "status":status,
            "targetType":targetType
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
    var phone = $("#phoneNumber").val();
    var syReportCtgyId = $("#syReportCtgyId").val();
    var targetType = $("#targetType").val();
    var status = $("#status").val();
    // alert(name + ":"+phone + ":"+syReportCtgyId + ":"+targetType + ":")
    $.ajax({
        type:"post",
        url:"../../../report/conditionQueryReport",
        data: {
            "acUserName":name,
            "acUserPhone":phone,
            "syReportCtgyId":syReportCtgyId,
            "targetType":targetType,
            "status":status,
            "nowPage":nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                $.each(res.result,function (index,value) {
                    var date = getDate(value.createTime);
                    if (value.status == 1) {
                        value.status = '正常';
                    } else if (value.status == 0) {
                        value.status = '无效';
                    } else if (value.status == -1) {
                        value.status = '已删除';
                    }
                    str += '<tr class="gradeX">';
                    str += '<td>'+value.acUserPhone+'</td>';
                    str += '<td>'+value.acUserName+'</td>';
                    str += '<td>'+value.reportCtgyName+'</td>';
                    str += '<td>'+value.content+'</td>';
                    str += '<td>'+date+'</td>';
                    str += '<td>'+value.status+'</td>';
                    str += '<td><input type="hidden" value="'+ value.targetId +'"/><a class="target-details" href="javascript:;" style="margin-left: 10px">详情</a></td></tr>';
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