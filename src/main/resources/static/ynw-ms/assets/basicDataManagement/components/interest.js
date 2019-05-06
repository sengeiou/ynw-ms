var num = 1;
var bdInterestId = "";
var name = "";
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('兴趣标签管理');
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
})

/**
 *  未来点击事件
 */
function bottom_click() {

    //添加
    $(document).on('click','#add_date',function(){
        $("#add-interest input").val("");
        $("#add-interest-data").show();
    });

    $("#add-interest-confirm").click(function () {
        add_data();
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
     *  编辑
     */
    $(document).on('click','#update_date',function(){
        var flag = getBox();
        if (flag == 1) {
            get_role_data();
            // alert(word + ":" + category+":");
            $("#update-interest input[name='name']").val(name);
            $("#update-interest-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    $("#update-interest-confirm").click(function () {
        name = $("#update-interest-data input[name='name']").val();
        if (name == "") {
            layer.msg("自我标签不能为空,请重新输入",{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../interest/update",
            data: {
                "name":name,
                "bdInterestId": bdInterestId,
                "LogContent":"编辑兴趣标签【 兴趣标签名称："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#update-interest-data").hide();
                    var count = getNum();
                    setPage(count);
                    // getData(num);
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

    $(".confirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../interest/delete",
            data: {
                "bdInterestId":bdInterestId,
                "LogContent":"删除兴趣标签【 兴趣标签名称："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("删除成功",{icon:1});
                    $(".operation-delete-data").hide();
                    var count = getNum();
                    setPage(count);
                    // getData(num)
                } else {
                    layer.msg(res.message,{icon:2});
                }
                $("#delete-data").hide();
            }
        });
    });

    /**
     *  上移
     */
    $(document).on('click','#moveUp',function(){
        bdInterestId = $(this).parent().children("input").val();
        $.ajax({
            type:"post",
            url:"../../../interest/moveUp",
            data: {
                "bdInterestId":bdInterestId,
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("移动成功",{icon:1});
                    var count = getNum();
                    setPage(count);
                    getData(num)
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    /**
     *  下移
     */
    $(document).on('click','#moveDown',function(){
        bdInterestId = $(this).parent().children("input").val();
        $.ajax({
            type:"post",
            url:"../../../interest/moveDown",
            data: {
                "bdInterestId":bdInterestId,
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("移动成功",{icon:1});
                    var count = getNum();
                    setPage(count);
                    getData(num)
                } else {
                    layer.msg(res.message,{icon:2});
                }
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
            bdInterestId = box[i].value;
            name = box[i].parentNode.nextSibling.textContent;
            // category = box[i].parentNode.nextSibling.nextSibling.nextSibling.textContent;
            break;
        }
    }
}

//添加-------------------------------
function add_data() {
    name = $("#add-interest-data input[name='name']").val();
    if (name == "") {
        layer.msg("兴趣标签不能为空,请重新输入",{icon:2});
        return false;
    }
    $.ajax({
        type:"post",
        url:"../../../interest/insert",
        data: {
            "name":name,
            "LogContent":"添加兴趣标签【 兴趣标签名称："+ name +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#add-interest-data").hide();
                var count = getNum();
                setPage(count);
                // getData(num);
                layer.msg("添加成功",{icon:1});
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
    name = $("#name").val();
    $.ajax({
        type:"post",
        url:"../../../interest/count",
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
        url:"../../../interest/conditionQueryInterest",
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
                        '<td id="td_input"><input type="checkbox" name="checkedres" value="'+value.bdInterestId+'"></td>';
                    str += '<td>'+value.name+'</td>';
                    str += '<td>'+value.sort+'</td>';
                    str += '<td>'+date+'</td>';
                    str += '<td style="text-align: center"><input type="hidden" value="'+ value.bdInterestId +'"/><a id="moveUp" href="javascript:;" style="margin-left: 10px">上移</a><a id="moveDown" style="margin-left: 10px" href="javascript:;">下移</a></td></tr>';
                })
                $("#role").html(str);
                //选中状态发生改变是隐藏功能弹窗
                control_tip_box();
            } else if (res.code == 8) {
                $("#role").empty();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}