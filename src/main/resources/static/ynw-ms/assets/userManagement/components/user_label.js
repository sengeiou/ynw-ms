var num = 1;
var labelId = '';
var labelName = '';
var typeId = '';
var typeName = '';
$(function () {
    //返回
    getBack();
    //第一页数据
    getData(1);
    //获取按钮
    getSkip('用户标签管理');
    //获取数据总数并分页
    getNum();
    //未来点击事件
    bottom_click();
    //获取用户标签分类
    getUserLabelType();
    //隐藏操作框
    control_tip();
    //隐藏举报分类操作弹窗
    control_tip_report();
    // 全选复选框
    $("#checkAll").click(function () {
        selectAll();
    })
    //如果不是全选取消全选框
    $(document).on("click","input[type='checkbox'][name='checkedres']",function(){
        selectBox();
    });
});

//隐藏举报分类操作弹窗
function control_tip_report() {
    $(".report-cancel").click(function () {
        $(".operation-data").hide();
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
     *  添加用户标签
     */
    $(document).on("click","#add_date", function () {
        $("#add-report input").val("");
        $("#add-report select").val("");
        $("#add-report-classify-data").show();
    });

    /**
     *  添加
     */
    $("#add-report-confirm").click(function () {
        var name = $("#add-report-classify-data input[name='name']").val();
        if (name == "") {
            layer.msg('用户标签名称不能为空',{icon:2});
            return false;
        }
        var labelClassify = $("#add-report-classify-data select[name='acLabelCtgyId']").val();
        if (labelClassify == "") {
            layer.msg('用户标签分类不能为空',{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../user_label/insert",
            data: {
                "name":name,
                "acLabelCtgyId":labelClassify,
                "LogContent":"添加用户标签【 用户标签名称："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#add-report-classify-data").hide();
                    var count = getNum();
                    setPage(count);
                    getData(num)
                    layer.msg("添加成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    /**
     *  编辑用户标签
     */
    $(document).on("click","#update_date",function () {
        var flag = getBox();
        if (flag == 1) {
            //获取选择的信息
            getLabelClassify();
            //根据信息显示修改原数据
            getLabelSelectOne();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    /**
     *  更新
     */
    $("#update-report-confirm").click(function () {
        var name = $("#update-report-classify-data input[name='name']").val();
        if (name == "") {
            layer.msg('用户标签名称不能为空',{icon:2});
            return false;
        }
        var labelClassify = $("#update-report-classify-data select[name='acLabelCtgyId']").val();
        if (labelClassify == "") {
            layer.msg('用户标签分类不能为空',{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../user_label/update",
            data: {
                "name":name,
                "acLabelCtgyId":labelClassify,
                "acLabelId": labelId,
                "LogContent":"更新用户标签【 用户标签名称："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#update-report-classify-data").hide();
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    layer.msg("更新成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    /**
     *  删除用户标签
     */
    $(document).on("click","#delete_date",function () {
        var flag = getBox();
        if (flag == 1) {
            getLabelClassify();
            $("#delete-data span").text(labelName);
            $("#delete-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    /**
     *  删除
     */
    $("#label-confirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../user_label/delete",
            data: {
                "acLabelId": labelId,
                "LogContent":"删除用户标签【 用户标签名称："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#delete-data").hide();
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

    //上移用户标签
    $(document).on('click', '.moveUp', function(){
        //获取选择的举报分类信息
       var labelId = $(this).parent().children("input").val();
        $.ajax({
            type:"post",
            url:"../../../user_label/moveUp",
            data: {
                "labelId":labelId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    layer.msg("移动成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //下移用户标签
    $(document).on('click', '.moveDown', function(){
        //获取选择的举报分类信息
        var labelId = $(this).parent().children("input").val();
        $.ajax({
            type:"post",
            url:"../../../user_label/moveDown",
            data: {
                "labelId":labelId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    layer.msg("移动成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    /**
     *  显示用户标签分类管理
     */
    $(document).on('click','#user_label_classify',function(){
        $("#nowPage-date").show();
    });

    /**
     * 隐藏用户标签分类管理
     */
    $(document).on('click','#drop_out',function(){
        $("#nowPage-date").hide();
        $(".unified-closed").hide();
    });

    //添加用户标签分类
    $(document).on('click','#add_label_classify',function(){
        $("#add-report input").val("");
        $("#add-label-classify-data").show();
    });

    //添加
    $("#add-label-classify-confirm").click(function () {
        var name = $("#add-label-classify-data input[name='name']").val();
        if (name == '') {
            layer.msg('用户标签分类名称不能为空',{icon:2});
            return false;
        }
        var key = $("#add-label-classify-data input[name='key']").val();
        if (key == '') {
            layer.msg('用户标签分类key不能为空',{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../user_label_classify/insert",
            data: {
                "name":name,
                "key":key,
                "LogContent":"添加用户标签分类【 用户标签分类名称："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#add-label-classify-data").hide();
                    getUserLabelType();
                    layer.msg("添加成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //编辑用户标签分类
    $(document).on('click','#update_label_classify',function(){
        //获取选择的用户标签分类信息
        get_report_typr_data();
        // alert(typeId+":"+typeName)
        getLabelClassifySelectOne();
    });

    $("#update-label-classify-confirm").click(function () {
        var name = $("#update-label-classify-data input[name='name']").val();
        if (name == '') {
            layer.msg('用户标签分类名称不能为空',{icon:2});
            return false;
        }
        var key = $("#update-label-classify-data input[name='key']").val();
        if (key == '') {
            layer.msg('用户标签分类key不能为空',{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../user_label_classify/update",
            data: {
                "name":name,
                "key":key,
                "acLabelCtgyId":typeId,
                "LogContent":"编辑用户标签分类【 用户标签分类名称："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#update-label-classify-data").hide();
                    getUserLabelType();
                    layer.msg("编辑成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //删除
    $(document).on('click','#delete_label_classify',function(){
        //获取选择的用户标签分类信息
        get_report_typr_data();
        $(".delete-label-classify span").text(typeName);
        $("#delete-label-classify-data").show();
    });

    //删除
    $("#delete-label-classify-confirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../user_label_classify/delete",
            data: {
                "acLabelCtgyId":typeId,
                "LogContent":"删除用户标签分类【 用户标签分类名称："+ typeName +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#delete-label-classify-data").hide();
                    getUserLabelType();
                    layer.msg("删除成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //上移用户标签分类
    $(document).on('click','#move_up',function(){
        //获取选择的举报分类信息
        get_report_typr_data();
        $.ajax({
            type:"post",
            url:"../../../user_label_classify/moveUp",
            data: {
                "labelClassifyId":typeId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("移动成功",{icon:1});
                    getUserLabelType();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //下移用户标签分类
    $(document).on('click','#move_down',function(){
        //获取选择的举报分类信息
        get_report_typr_data();
        $.ajax({
            type:"post",
            url:"../../../user_label_classify/moveDown",
            data: {
                "labelClassifyId":typeId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("移动成功",{icon:1});
                    getUserLabelType();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

}

//获取选中的用户标签
function getLabelClassify() {
    var box = $("input[type='checkbox'][name='checkedres']");
    var length = box.length;
    for (var i = 0; i < length; i++) {
        if (box[i].checked) {
            labelId = box[i].value;
            labelName = box[i].parentNode.nextSibling.textContent;
            // category = box[i].parentNode.nextSibling.nextSibling.nextSibling.textContent;
            break;
        }
    }
}

//获取标签分类单条数据
function getLabelClassifySelectOne() {
    $.ajax({
        type:"post",
        url:"../../../user_label_classify/selectOne",
        data: {
            "acLabelCtgyId":typeId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#update-label-classify-data input[name='name']").val(typeName);
                $("#update-label-classify-data input[name='key']").val(res.result.key);
                $("#update-label-classify-data").show();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取标签单条数据
function getLabelSelectOne() {
    $.ajax({
        type:"post",
        url:"../../../user_label/selectOne",
        data: {
            "acLabelId":labelId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#update-report-classify-data input[name='name']").val(labelName)
                $("#update-report-classify-data select[name='acLabelCtgyId']").val(res.result.acLabelCtgyId);
                $("#update-report-classify-data").show()
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取举报分类选中文本和值-------------------------------
function get_report_typr_data() {
    typeId = $(".nowPage-left select").val();
    typeName = $(".nowPage-left select").find("option:selected").text();
}

//获取用户标签类别-------------------------------
function getUserLabelType() {
    $.ajax({
        type:"post",
        url:"../../../user_label_classify/findUserLabelClassify",
        data: {
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                var str = '<select style="width: 110%" size="'+ res.result.length +'">';
                $.each(res.result,function (index,value) {
                    src += '<option value="'+ value.acLabelCtgyId +'">'+ value.name +'</option>';
                    str += '<option value="'+ value.acLabelCtgyId +'">'+ value.name +'</option>';
                });
                str += '</select>';
                $("#labelClassify,.unified-closed select[name='acLabelCtgyId']").html(src);
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
    var labelClassify = $("#labelClassify").val();
    $.ajax({
        type:"post",
        url:"../../../user_label/count",
        data: {
            "name":name,
            "acLabelCtgyId":labelClassify
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
    var labelClassify = $("#labelClassify").val();
    // alert(name + ":"+phone + ":"+syReportCtgyId + ":"+targetType + ":")
    $.ajax({
        type:"post",
        url:"../../../user_label/conditionQueryUserLabel",
        data: {
            "name":name,
            "acLabelCtgyId":labelClassify,
            "nowPage":nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                $.each(res.result,function (index,value) {
                    var date = getDate(value.createTime);
                    str += '<tr class="gradeX"><td id="td_input"><input type="checkbox" name="checkedres" value="'+value.acLabelId+'"></td>';
                    str += '<td>'+value.name+'</td>';
                    str += '<td>'+value.acLabelClassifyName+'</td>';
                    str += '<td>'+value.sort+'</td>';
                    str += '<td>'+date+'</td>';
                    str += '<td><input type="hidden" value="'+ value.acLabelId +'"/><a class="moveUp" href="javascript:;" ' +
                        'style="margin-left: 10px">上移</a><a class="moveDown" style="margin-left: 10px" href="javascript:;">下移</a></td></tr>';
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


//返回————————————————————————————
function getBack() {
    $(".back").click(function () {
        var resourceId = $("#parentId").val();
        $(location).attr("href","user_info_management?msResourceId="+resourceId + "&name="+ $("#userName").val() + "&idVerifyStatus="+ $("#idVerifyStatus").val() +
            "&phone="+ $("#phone").val() + "&no=" + $("#no").val() + "&sex=" + $("#sex").val() + "&province=" + $("#province").val() +
            "&city=" + $("#city").val() + "&status=" + $("#status").val() + "&imageStatus=" + $("#imageStatus").val() + "&num=" + $("#num").val());
    });
}