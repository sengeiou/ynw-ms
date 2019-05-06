var num = 1;
var syParaConfId = "";
var name = "";
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('雷达频道管理');
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
        $("#add-parameter-data").show();
    });

    //添加
    $("#add-parameter-confirm").click(function () {
        name = $("#add-parameter input[name='name']").val();
        if (name == "") {
            layer.msg("频道名不能为空,请重新输入",{icon:2});
            return false;
        }
        // alert(key+":"+value+":"+desc+":")
        $.ajax({
            type:"post",
            url:"../../../channel/insert",
            data: {
                "name":name,
                "LogContent":"添加雷达搜频道【 频道名："+ name +"】"
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
            $("#update-parameter input[name='name']").val(name);
            $("#update-parameter-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    //编辑
    $("#update-parameter-confirm").click(function () {
        name = $("#update-parameter input[name='name']").val();
        if (name == "") {
            layer.msg("频道名不能为空,请重新输入",{icon:2});
            return false;
        }
        // alert(key +":"+value +":"+desc +":"+syParaConfId);
        $.ajax({
            type:"post",
            url:"../../../channel/update",
            data: {
                "rsChannelId":syParaConfId,
                "name":name,
                "LogContent":"编辑雷达搜频道【 频道名："+ name +"】"
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
            $(".role-delete-body span").text(name);
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
            url:"../../../channel/delete",
            data: {
                "rsChannelId":syParaConfId,
                "LogContent":"删除雷达搜频道【 频道名："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $(".operation-delete-data").hide();
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    layer.msg("删除成功",{icon:1});
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
            name = box[i].parentNode.nextSibling.textContent;
            // value = box[i].parentNode.nextSibling.nextSibling.textContent;
            // desc = box[i].parentNode.nextSibling.nextSibling.nextSibling.nextSibling.textContent;
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
    name = $("#name").val();
    $.ajax({
        type:"post",
        url:"../../../channel/count",
        data: {
            "name":name
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
    name = $("#name").val();
    $.ajax({
        type:"post",
        url:"../../../channel/conditionQueryChannel",
        data: {
            "name":name,
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
                        '<td id="td_input"><input type="checkbox" name="checkedres" value="'+value.rsChannelId+'"></td>';
                    str += '<td>'+value.name+'</td>';
                    str += '<td>'+date+'</td></tr>';
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