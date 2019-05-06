var num = 1;
var syParaConfId = "";
var desc = "";
var key = "";
var value = "";
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('系统参数配置');
    //获取数据总数并分页
    getNum();
    // 全选复选框
    $("#checkAll").click(function () {
        selectAll();
    })
    //如果不是全选取消全选框
    $(document).on("click","input[type='checkbox'][name='checkedres']",function(){
        selectBox();
    });
    //未来点击事件
    bottom_click();
    //隐藏操作框
    control_tip();
});

/**
 *  未来点击事件
 */
function bottom_click() {

    //添加系统参数配置
    $(document).on('click','#add_date',function(){
        $("#add-parameter input").val("");
        $("#add-parameter textarea").val("");
        $("#add-parameter-data").show();
    });

    //添加
    $("#add-parameter-confirm").click(function () {
        key = $("#add-parameter input[name='key']").val();
        if (key == "") {
            layer.msg("键不能为空,请重新输入",{icon:2});
            return false;
        }
        value = $("#add-parameter input[name='value']").val();
        if (value == "") {
            layer.msg("值不能为空,请重新输入",{icon:2});
            return false;
        }
        desc = $("#add-parameter textarea").val();
        if (desc == "") {
            layer.msg("描述不能为空,请重新输入",{icon:2});
            return false;
        }
        // alert(key+":"+value+":"+desc+":")
        $.ajax({
            type:"post",
            url:"../../../paraConf/insert",
            data: {
                "key":key,
                "value":value,
                "describe":desc,
                "LogContent":"添加系统参数【 系统参数描述："+ desc +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#add-parameter-data").hide();
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    layer.msg("添加成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

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
     *  编辑系统参数配置
     */
    $(document).on('click','#update_date',function(){
        var flag = getBox();
        if (flag == 1) {
            get_role_data();
            // alert(name + ":" + roleKey+":"+msRoleId+":");
            $("#update-parameter input[name='key']").val(key);
            $("#update-parameter input[name='value']").val(value);
            $("#update-parameter textarea").val(desc);
            $("#update-parameter-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    //编辑
    $("#update-parameter-confirm").click(function () {
        get_role_data();
        key = $("#update-parameter input[name='key']").val();
        if (key == "") {
            layer.msg("键不能为空,请重新输入",{icon:2});
            return false;
        }
        value = $("#update-parameter input[name='value']").val();
        if (value == "") {
            layer.msg("值不能为空,请重新输入",{icon:2});
            return false;
        }
        desc = $("#update-parameter textarea").val();
        if (desc == "") {
            layer.msg("描述不能为空,请重新输入",{icon:2});
            return false;
        }
        // alert(key +":"+value +":"+desc +":"+syParaConfId);
        $.ajax({
            type:"post",
            url:"../../../paraConf/update",
            data: {
                "syParaConfId":syParaConfId,
                "key":key,
                "value":value,
                "describe":desc,
                "LogContent":"编辑系统参数【 系统参数描述："+ desc +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#update-parameter-data").hide();
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    layer.msg("修改成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    /**
     *  删除
     */
    $(document).on('click','#delete_date',function(){
        var flag = getBox();
        if (flag == 1) {
            get_role_data();
            $(".role-delete-body span").text(key);
            $(".operation-delete-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    //删除
    $(".confirm").click(function () {
        get_role_data();
        $.ajax({
            type:"post",
            url:"../../../paraConf/delete",
            data: {
                "syParaConfId":syParaConfId,
                "LogContent":"删除系统参数【 系统参数描述："+ desc +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("删除成功",{icon:1});
                    $(".operation-delete-data").hide();
                    var count = getNum();
                    setPage(count);
                    getData(num)
                } else {
                    layer.msg(res.message,{icon:2});
                }
                $("#delete-data").hide();
            }
        });
    });

}

//获取勾选的数据-------------------------------
function get_role_data() {
    var box = $("input[type='checkbox'][name='checkedres']");
    var length = box.length;
    for (var i = 0; i < length; i++) {
        if (box[i].checked) {
            syParaConfId = box[i].value;
            key = box[i].parentNode.nextSibling.textContent;
            value = box[i].parentNode.nextSibling.nextSibling.textContent;
            desc = box[i].parentNode.nextSibling.nextSibling.nextSibling.nextSibling.textContent;
            break;
        }
    }
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
    key = $("#key").val();
    value = $("#value").val();
    $.ajax({
        type:"post",
        url:"../../../paraConf/count",
        data: {
            "key":key,
            "value":value,
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
    key = $("#key").val();
    value = $("#value").val();
    $.ajax({
        type:"post",
        url:"../../../paraConf/conditionQueryParaConf",
        data: {
            "key":key,
            "value":value,
            "nowPage":nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                $.each(res.result,function (index,value) {
                    var date = getDate(value.createTime);
                    str += '<tr class="gradeX">' +
                        '<td id="td_input"><input type="checkbox" name="checkedres" value="'+value.syParaConfId+'"></td>';
                    str += '<td>'+value.key+'</td>';
                    str += '<td>'+value.value+'</td>';
                    str += '<td>'+date+'</td>';
                    str += '<td>'+value.describe+'</td></tr>';
                })
                $("#ParaConf").html(str);
                //选中状态发生改变是隐藏功能弹窗
                control_tip_box();
            } else if (res.code == 8) {
                $("#ParaConf").empty();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}