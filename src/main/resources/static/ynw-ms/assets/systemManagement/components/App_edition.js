var num = 1;
var msAppVUpdateId = "";
var number = "";
var visitorOsType = "";
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('APP版本管理');
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

    //添加APP弹窗
    $(document).on('click','#add_date',function(){
        $("#add-APP select").val("");
        $(".body").val("");
        $("#add-APP-data input[name='isForceUpdate']:first").prop("checked",true);
        $("#add-APP-data input[name='packageUrl']").val("");
        $("#add-APP-data textarea").val("");
        $("#add-APP-data").show();
    });

    //添加
    $("#add-APP-confirm").click(function () {
        number = $("#add-APP input[name='number']").val();
        if (number == "") {
            layer.msg("版本号不能为空,请重新输入",{icon:2});
            return false;
        }
        visitorOsType = $("#add-APP select[name='visitorOsType']").val();
        if (visitorOsType == "") {
            layer.msg("客户端类型不能为空,请重新输入",{icon:2});
            return false;
        }
        var size = $("#add-APP input[name='size']").val();
        if (size == "") {
            layer.msg("版本大小不能为空,请重新输入",{icon:2});
            return false;
        }
        var isForceUpdate = $("#add-APP input[name='isForceUpdate']:checked").val();
        if (isForceUpdate == null) {
            layer.msg("强制更新不能为空,请重新输入",{icon:2});
            return false;
        }
        var packageUrl = $("#add-APP-data input[name='packageUrl']").val();
        if (packageUrl == "") {
            layer.msg("安装包URL不能为空,请重新输入",{icon:2});
            return false;
        }
        var content = $("#add-APP-data .content textarea").val();
        if (content == "") {
            layer.msg("版本内容不能为空,请重新输入",{icon:2});
            return false;
        }
        var description = $("#add-APP-description textarea").val();
        if (description == "") {
            layer.msg("版本更新描述不能为空,请重新输入",{icon:2});
            return false;
        }
        // alert(number+":"+visitorOsType+":"+size+":"+isForceUpdate+":"+packageUrl+":"+description+":"+content+":")
        $.ajax({
            type:"post",
            url:"../../../appEdition/insert",
            data: {
                "number":number,
                "visitorOsType":visitorOsType,
                "size":size,
                "isForceUpdate":isForceUpdate,
                "packageUrl":packageUrl,
                "describe":description,
                "content":content,
                "LogContent":"添加app版本【 版本号："+ number +"】"
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
     *  编辑APP弹窗
     */
    $(document).on('click','#update_date',function(){
        var flag = getBox();
        if (flag == 1) {
            //获取选择的编号id
            get_role_data();
            //赋值
            findById();
            $("#update-APP-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    //编辑
    $("#update-APP-confirm").click(function () {
        number = $("#update-APP-data input[name='number']").val();
        if (number == "") {
            layer.msg("版本号不能为空,请重新输入",{icon:2});
            return false;
        }
        visitorOsType = $("#update-APP-data select[name='visitorOsType']").val();
        if (visitorOsType == "") {
            layer.msg("客户端类型不能为空,请重新输入",{icon:2});
            return false;
        }
        var size = $("#update-APP-data input[name='size']").val();
        if (size == "") {
            layer.msg("版本大小不能为空,请重新输入",{icon:2});
            return false;
        }
        var isForceUpdate = $("#update-APP-data input[name='isForceUpdate']:checked").val();
        if (isForceUpdate == null) {
            layer.msg("强制更新不能为空,请重新输入",{icon:2});
            return false;
        }
        var packageUrl = $("#update-APP-data input[name='packageUrl']").val();
        if (packageUrl == "") {
            layer.msg("安装包URL不能为空,请重新输入",{icon:2});
            return false;
        }
        var content = $("#update-APP-data .content textarea").val();
        if (content == "") {
            layer.msg("版本内容不能为空,请重新输入",{icon:2});
            return false;
        }
        var description = $("#update-APP-description textarea").val();
        if (description == "") {
            layer.msg("版本更新描述不能为空,请重新输入",{icon:2});
            return false;
        }
        // alert(number+":"+visitorOsType+":"+size+":"+isForceUpdate+":"+packageUrl+":"+description+":"+content+":")
        $.ajax({
            type:"post",
            url:"../../../appEdition/update",
            data: {
                "number":number,
                "visitorOsType":visitorOsType,
                "size":size,
                "isForceUpdate":isForceUpdate,
                "packageUrl":packageUrl,
                "describe":description,
                "content":content,
                "msAppVUpdateId":msAppVUpdateId,
                "LogContent":"编辑app版本【 版本号："+ number +"】"
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
            $(".role-delete-body span").text(number);
            $(".operation-delete-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    $(".confirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../appEdition/delete",
            data: {
                "msAppVUpdateId":msAppVUpdateId,
                "LogContent":"删除app版本【 版本号："+ number +"】"
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

//获取单个的数据-------------------------------
function findById() {
    $.ajax({
        type:"post",
        url:"../../../appEdition/findById",
        data: {
            "msAppVUpdateId":msAppVUpdateId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var value = res.result;
                $("#update-APP-data input[name='number']").val(value.number);
                $("#update-APP-data select[name='visitorOsType']").val(value.visitorOsType);
                $("#update-APP-data input[name='size']").val(value.size);
                $("#update-APP-data input[name='packageUrl']").val(value.packageUrl);
                $("#update-APP-data .content textarea").val(value.content);
                $("#update-APP-description textarea").val(value.describe);
                if (value.isForceUpdate == 0) {
                    $("#update-APP-data input[name='isForceUpdate']:first").prop("checked",true);
                } else {
                    $("#update-APP-isUpdate").prop("checked",true);
                }
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
            msAppVUpdateId = box[i].value;
            number = box[i].parentNode.nextSibling.textContent;
            // value = box[i].parentNode.nextSibling.nextSibling.textContent;
            // desc = box[i].parentNode.nextSibling.nextSibling.nextSibling.textContent;
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
    number = $("#number").val();
    visitorOsType = $("#visitorOsType").val();
    $.ajax({
        type:"post",
        url:"../../../appEdition/count",
        data: {
            "number":number,
            "visitorOsType":visitorOsType
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
    number = $("#number").val();
    visitorOsType = $("#visitorOsType").val();
    $.ajax({
        type:"post",
        url:"../../../appEdition/conditionQueryAPPEdition",
        data: {
            "number":number,
            "visitorOsType":visitorOsType,
            "nowPage":nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                $.each(res.result,function (index,value) {
                    var date = getDate(value.createTime);
                    var isForce = "";
                    if (value.isForceUpdate == 0) {
                        isForce = "否";
                    } else if (value.isForceUpdate == 0) {
                        isForce = "是";
                    }
                    str += '<tr class="gradeX">' +
                        '<td id="td_input"><input type="checkbox" name="checkedres" value="'+value.msAppVUpdateId+'"></td>';
                    str += '<td>'+value.number+'</td>';
                    str += '<td>'+value.content+'</td>';
                    str += '<td>'+value.visitorOsTypeName+'</td>';
                    str += '<td>'+value.describe+'</td>';
                    str += '<td>'+isForce+'</td>';
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