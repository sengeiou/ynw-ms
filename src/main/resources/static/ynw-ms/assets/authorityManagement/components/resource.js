var num = 1;
var msResourceId = "";
var name = ""
    $(function () {
    //第一页数据
    getData(1);
    //获取数据总数并分页
    getNum();
    //获取按钮
    getButton('资源管理');
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
    //获取添加与编辑的父类资源
    getResource();
})


/**
 *  未来点击事件
 */
function bottom_click() {

    /**
     *  添加资源
     */
    $(document).on('click','#add_date',function(){
        $("#add-resource input").val("");
        $("#add-resource select").val("");
        $("#add-resource-description textarea").val("");
        $("#add-resource-data").show();
    });

    //添加
    $("#add-resource-confirm").click(function () {
        name = $("#add-resource input[name='name']").val();
        if (name == "") {
            layer.msg("资源名称不能为空,请重新输入",{icon:2});
            return false;
        }
        var sourceKey = $("#add-resource input[name='sourceKey']").val();
        if (sourceKey == "") {
            layer.msg("资源Key不能为空,请重新输入",{icon:2});
            return false;
        }
        var sourceUrl = $("#add-resource input[name='sourceUrl']").val();
        if (sourceUrl == "") {
            layer.msg("资源URL不能为空,请重新输入",{icon:2});
            return false;
        }
        var parentId = $("#add-resource select[name='parentId']").val();
        var type = $("#add-resource select[name='type']").val();
        var level = $("#add-resource select[name='level']").val();
        var sort = $("#add-resource input[name='sort']").val();
        var isHide = $("#add-resource select[name='isHide']").val();
        var description = $("#add-resource-description textarea").val();
        if (description == "") {
            layer.msg("描述不能为空,请重新输入",{icon:2});
            return false;
        }
        // alert(name +":"+sourceKey +":"+sourceUrl +":"+parentName +":"+type +":"+level +":"+sort +":"+isHide +":"+description +":")
        $.ajax({
            type:"post",
            url:"../../../resource/insert",
            data: {
                "msResourceId":msResourceId,
                "name":name,
                "sourceKey":sourceKey,
                "sourceUrl":sourceUrl,
                "parentId":parentId,
                "type":type,
                "level":level,
                "sort":sort,
                "isHide":isHide,
                "describe":description,
                "LogContent":"添加资源【 资源名称："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#add-resource-data").hide();
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    getResource();
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
     *  编辑资源
     */
    $(document).on('click','#update_date',function(){
        var flag = getBox();
        if (flag == 1) {
            get_role_data();
            // getResource('findByTypeLessThanOne');
            selectResourceOne();
            $("#update-resource-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    //编辑
    $("#update-resource-confirm").click(function () {
        name = $("#update-resource input[name='name']").val();
        if (name == "") {
            layer.msg("资源名称不能为空,请重新输入",{icon:2});
            return false;
        }
        var sourceKey = $("#update-resource input[name='sourceKey']").val();
        if (sourceKey == "") {
            layer.msg("资源Key不能为空,请重新输入",{icon:2});
            return false;
        }
        var sourceUrl = $("#update-resource input[name='sourceUrl']").val();
        if (sourceUrl == "") {
            layer.msg("资源URL不能为空,请重新输入",{icon:2});
            return false;
        }
        var parentId = $("#update-resource select[name='parentId']").val();
        var type = $("#update-resource select[name='type']").val();
        var level = $("#update-resource select[name='level']").val();
        var sort = $("#update-resource input[name='sort']").val();
        var isHide = $("#update-resource select[name='isHide']").val();
        var description = $("#update-resource-description textarea").val();
        if (description == "") {
            layer.msg("描述不能为空,请重新输入",{icon:2});
            return false;
        }
        // alert(name +":"+sourceKey +":"+sourceUrl +":"+parentName +":"+type +":"+level +":"+sort +":"+isHide +":"+description +":")
        $.ajax({
            type:"post",
            url:"../../../resource/update",
            data: {
                "msResourceId":msResourceId,
                "name":name,
                "sourceKey":sourceKey,
                "sourceUrl":sourceUrl,
                "parentId":parentId,
                "type":type,
                "level":level,
                "sort":sort,
                "isHide":isHide,
                "describe":description,
                "LogContent":"编辑资源【 资源名称："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#update-resource-data").hide();
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    getResource();
                    layer.msg("编辑成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    })

    /**
     *  删除资源弹窗
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
    $("#confirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../resource/delete",
            data: {
                "msResourceId":msResourceId,
                "LogContent":"删除资源【 资源名称："+ name +"】"
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
            msResourceId = box[i].value;
            name = box[i].parentNode.nextSibling.textContent;
            break;
        }
    }
}


//获取单个资源------------------------------
function selectResourceOne() {
    $.ajax({
        type:"post",
        url:"../../../resource/selectOne",
        data: {
            "msResourceId":msResourceId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var resource = res.result;
                $("#update-resource input[name='name']").val(resource.name);
                $("#update-resource input[name='sourceKey']").val(resource.sourceKey);
                $("#update-resource input[name='sourceUrl']").val(resource.sourceUrl);
                // alert(resource.parentId)
                $("#update-resource select[name='parentId']").val(resource.parentId);
                $("#update-resource select[name='type']").val(resource.type);
                $("#update-resource select[name='level']").val(resource.level);
                $("#update-resource input[name='sort']").val(resource.sort);
                $("#update-resource select[name='isHide']").val(resource.isHide);
                $("#update-resource-description textarea").val(resource.describe);
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}


//获取所有父类资源------------------------------
function getResource() {
    $.ajax({
        type:"post",
        url:"../../../resource/findByTypeLessThanOne",
        data: {
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var parent = '<option value="">--请选择--</option>';
                $.each(res.result,function (index,value) {
                    parent += '<option value="'+ value.msResourceId +'">'+ value.name +'</option>';
                })
                // if (url == 'findByTypeLessThanOne') {
                //     $(".resource-body select[name='parentId']").html(parent);
                // }
                $(".resource-body select[name='parentId']").html(parent);
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

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

/**
 *  获取总数
 */
function getNum() {
    var name = $("#name").val();
    var type = $("#type").val();
    var level = $("#level").val();
    var isHide = $("#isHide").val();
    $.ajax({
        type:"post",
        url:"../../../resource/count",
        data: {
            "name":name,
            "type":type,
            "level":level,
            "isHide":isHide
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

/**
 *  获取数据
 * @param nowPage
 */
function getData(nowPage) {
    var name = $("#name").val();
    var type = $("#type").val();
    var level = $("#level").val();
    var isHide = $("#isHide").val();
    $.ajax({
        type:"post",
        url:"../../../resource/conditionQueryResourceAll",
        data: {
            "name":name,
            "type":type,
            "level":level,
            "isHide":isHide,
            "nowPage":nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                $.each(res.result,function (index,value) {
                    // var time = new Date(value.createTime);
                    var date = getDate(value.createTime)
                    if (value.type == 1) {
                        value.type = "菜单";
                    }else if (value.type == 0) {
                        value.type = "目录";
                    } else if (value.type == 2) {
                        value.type = "按钮";
                    } else if (value.type == 3) {
                        value.type = "跳转";
                    }
                    if (value.isHide == 0) {
                        value.isHide = "显示";
                    } else {
                        value.isHide = "隐藏";
                    }
                    str += '<tr class="gradeX"><td id="td_input"><input type="checkbox" name="checkedres" value="'+value.msResourceId+'"></td>';
                    str += '<td>'+value.name+'</td>';
                    str += '<td>'+value.sourceKey+'</td>';
                    str += '<td>'+value.type+'</td>';
                    str += '<td>'+value.sourceUrl+'</td>';
                    str += '<td>'+value.describe+'</td>';
                    str += '<td>'+value.level+'</td>';
                    str += '<td>'+date+'</td>';
                    str += '<td>'+value.isHide+'</td></tr>';
                })
                $("#resource").html(str);
            } else if (res.code == 8) {
                $("#resource").empty();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}