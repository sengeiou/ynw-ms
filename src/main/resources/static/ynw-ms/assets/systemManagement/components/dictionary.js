var num = 1;
var syDictionaryId = "";
var groupKey = "";
var groupDesc = "";
var itemKey = "";
var itemValue = "";
var describe = "";
var sort = "";
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('字典管理');
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
    //获取组键数据
    findDictionaryByGroupKy();
});

/**
 *  未来点击事件
 */
function bottom_click() {

    //添加APP弹窗
    $(document).on('click','#add_date',function(){
        $("#add-APP input").val("");
        $("#add-APP-data").show();
    });

    //添加
    $("#add-APP-confirm").click(function () {
        groupKey = $("#add-APP input[name='groupKey']").val();
        if (groupKey == "") {
            layer.msg("组键不能为空,请重新输入",{icon:2});
            return false;
        }
        groupDesc = $("#add-APP input[name='groupDesc']").val();
        if (groupDesc == "") {
            layer.msg("组描述为空,请重新输入",{icon:2});
            return false;
        }
        itemKey = $("#add-APP input[name='itemKey']").val();
        if (itemKey == "") {
            layer.msg("键不能为空,请重新输入",{icon:2});
            return false;
        }
        itemValue = $("#add-APP input[name='itemValue']").val();
        if (itemValue == null) {
            layer.msg("值不能为空,请重新输入",{icon:2});
            return false;
        }
        describe = $("#add-APP-data input[name='describe']").val();
        if (describe == "") {
            layer.msg("描述不能为空,请重新输入",{icon:2});
            return false;
        }
        sort = $("#add-APP-data input[name='sort']").val();
        // alert(number+":"+visitorOsType+":"+size+":"+isForceUpdate+":"+packageUrl+":"+description+":"+content+":")
        $.ajax({
            type:"post",
            url:"../../../dictionary/insert",
            data: {
                "groupKey":groupKey,
                "groupDesc":groupDesc,
                "itemKey":itemKey,
                "itemValue":itemValue,
                "describe":describe,
                "sort":sort,
                "LogContent":"添加字典【 字典键："+ itemKey +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#add-APP-data").hide();
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
     *  编辑字典
     */
    $(document).on('click','#update_date',function(){
        var flag = getBox();
        if (flag == 1) {
            //获取选择的编号id
            get_role_data();
            //赋值
            $("#update-APP-data input[name='groupKey']").val(groupKey);
            $("#update-APP-data input[name='groupDesc']").val(groupDesc);
            $("#update-APP-data input[name='itemKey']").val(itemKey);
            $("#update-APP-data input[name='itemValue']").val(itemValue);
            $("#update-APP-data input[name='describe']").val(describe);
            $("#update-APP-data input[name='sort']").val(sort);
            $("#update-APP-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    //编辑
    $("#update-APP-confirm").click(function () {
        groupKey = $("#update-APP-data input[name='groupKey']").val();
        if (groupKey == "") {
            layer.msg("组键不能为空,请重新输入",{icon:2});
            return false;
        }
        groupDesc = $("#update-APP-data input[name='groupDesc']").val();
        if (groupDesc == "") {
            layer.msg("组描述为空,请重新输入",{icon:2});
            return false;
        }
        itemKey = $("#update-APP-data input[name='itemKey']").val();
        if (itemKey == "") {
            layer.msg("键不能为空,请重新输入",{icon:2});
            return false;
        }
        itemValue = $("#update-APP-data input[name='itemValue']").val();
        if (itemValue == null) {
            layer.msg("值不能为空,请重新输入",{icon:2});
            return false;
        }
        describe = $("#update-APP-data input[name='describe']").val();
        if (describe == "") {
            layer.msg("描述不能为空,请重新输入",{icon:2});
            return false;
        }
        sort = $("#update-APP-data input[name='sort']").val();
        // alert(number+":"+visitorOsType+":"+size+":"+isForceUpdate+":"+packageUrl+":"+description+":"+content+":")
        $.ajax({
            type:"post",
            url:"../../../dictionary/update",
            data: {
                "groupKey":groupKey,
                "groupDesc":groupDesc,
                "itemKey":itemKey,
                "itemValue":itemValue,
                "describe":describe,
                "sort":sort,
                "syDictionaryId":syDictionaryId,
                "LogContent":"编辑字典【 字典键："+ itemKey +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#update-APP-data").hide();
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
            $(".role-delete-body span").text(itemKey);
            $(".operation-delete-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    $(".confirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../dictionary/delete",
            data: {
                "syDictionaryId":syDictionaryId,
                "LogContent":"删除字典【 字典键："+ itemKey +"】"
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

//获取组键的数据-------------------------------
function findDictionaryByGroupKy() {
    $.ajax({
        type:"post",
        url:"../../../dictionary/findDictionaryByGroupKy",
        data: {
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                $.each(res.result, function (index,value) {
                    src += '<option value="'+ value.groupKey +'">--'+ value.groupDesc +'--</option>';
                });
                $("#groupKey").html(src);
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取勾选的数据-------------------------------
function get_role_data() {
    var box = $("input[type='checkbox'][name='checkedres']");
    var length = box.length;
    for (var i = 0; i < length; i++) {
        if (box[i].checked) {
            var _this = box[i];
            syDictionaryId = _this.value;
            _this = _this.parentNode.nextSibling;
            groupKey = _this.textContent;
            _this = _this.nextSibling;
            groupDesc = _this.textContent;
            _this = _this.nextSibling;
            itemKey = _this.textContent;
            _this = _this.nextSibling;
            itemValue = _this.textContent;
            _this = _this.nextSibling;
            describe = _this.textContent;
            _this = _this.nextSibling;
            sort = _this.textContent;
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
    var groupKey = $("#groupKey").val();
    $.ajax({
        type:"post",
        url:"../../../dictionary/count",
        data: {
            "groupKey":groupKey
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
    var groupKey = $("#groupKey").val();
    $.ajax({
        type:"post",
        url:"../../../dictionary/conditionQueryDictionary",
        data: {
            "groupKey":groupKey,
            "nowPage":nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                $.each(res.result,function (index,value) {
                    str += '<tr class="gradeX">' +
                        '<td id="td_input"><input type="checkbox" name="checkedres" value="'+value.syDictionaryId+'"></td>';
                    str += '<td>'+value.groupKey+'</td>';
                    str += '<td>'+value.groupDesc+'</td>';
                    str += '<td>'+value.itemKey+'</td>';
                    str += '<td>'+value.itemValue+'</td>';
                    str += '<td>'+value.describe+'</td>';
                    str += '<td>'+value.sort+'</td></tr>';
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