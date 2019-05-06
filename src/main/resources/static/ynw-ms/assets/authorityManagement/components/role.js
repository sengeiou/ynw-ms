var num = 1;
var roles = "";
var msRoleId = "";
var name = "";
var roleKey = "";
var resourceIds = "";
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('角色管理');
    //获取数据总数并分页
    getNum();
    //获取角色
    getRole();
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
    //伸缩分配权限
    flexible();
})

/**
 * 伸缩分配权限
 */
function flexible() {

    $(document).on('click','.item-1',function(){
        $(this).parent().find(".nav-second").slideToggle(500);
        $(this).children("i").toggleClass("unfold");
    });
    $(document).on('click','.item-2',function(){
        $(this).parent().find(".nav-three").slideToggle(500);
        $(this).children("i").toggleClass("unfold");
    });
    $(document).on('click','.item-3',function(){
        $(this).parent().find(".nav-four").slideToggle(500);
        $(this).children("i").toggleClass("unfold");
    });
    $(document).on('click','.item-4',function(){
        $(this).parent().find(".nav-four").slideToggle(500);
        $(this).children("i").toggleClass("unfold");
    });

}

/**
 *  未来点击事件
 */
function bottom_click() {

    //添加角色
    $(document).on('click','#add_date',function(){
        $("#add-role input").val("");
        // $("#add-role select").val("");
        $("#add-role-data").show();
    });

    //添加
    $("#add-role-confirm").click(function () {
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
     *  编辑角色
     */
    $(document).on('click','#update_date',function(){
        var flag = getBox();
        if (flag == 1) {
            get_role_data();
            // alert(name + ":" + roleKey+":"+msRoleId+":");
            $("#update-role input[name='name']").val(name);
            $("#update-role input[name='roleKey']").val(roleKey);
            $("#update-role-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    //编辑
    $("#update-role-confirm").click(function () {
        name = $("#update-role input[name='name']").val();
        if (name == "") {
            layer.msg("角色名称不能为空,请重新输入",{icon:2});
            return false;
        }
        var roleKey = $("#update-role input[name='roleKey']").val();
        if (roleKey == "") {
            layer.msg("角色KEY不能为空,请重新输入",{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../role/update",
            data: {
                "msRoleId":msRoleId,
                "name":name,
                "roleKey":roleKey,
                "LogContent":"编辑角色【 角色名称："+ name +"】"
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
    });

    /**
     *  删除角色
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
            url:"../../../role/delete",
            data: {
                "msRoleId":msRoleId,
                "LogContent":"删除角色【 角色名称："+ name +"】"
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
            }
        });
    });

    /**
     *  分配资源
     */
    $(document).on('click','#allocatingResource',function(){
        var flag = getBox();
        if (flag == 1) {
            get_role_data();
            getAllResource();
            $(".allocatingResource").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    //分配
    $("#comment-confirm").click(function () {
        //获取复选框的值
        get_resource_data();
        $.ajax({
            type:"post",
            url:"../../../admin/operationResourceAndRole",
            data: {
                "roleId": msRoleId,
                "resourceIds":resourceIds
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("操作成功",{icon:1});
                    $(".allocatingResource").hide();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //查看角色关联
    $(document).on("click",".examine", function () {
       var roleId = $(this).prev().val();
        $.ajax({
            type:"post",
            url:"../../../role/findUserByRoleId",
            data: {
                "roleId": roleId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    var src = '';
                    $.each(res.result,function (index,value) {
                        src += '<div>'+ value.realName +'<span>'+ value.name +'</span></div>';
                    });
                    $(".examineBody").html(src);
                    $("#roleExamine").show();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

}

//获取角色资源的数据-------------------------------
function getUserResource() {
    $.ajax({
        type:"post",
        url:"../../../resource/findResourceByRoleId",
        data: {
            "roleId": msRoleId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $.each(res.result,function (index,value) {
                    $(".resourceCheck").each(function(k){
                        if ($(this).val() == value.msResourceId) {
                            // alert($(this).val()+":"+value.msResourceId);
                            $(this).prop("checked", true);
                        }
                    })
                });
            } else if (res.code != 8) {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取所有资源的数据-------------------------------
function getAllResource() {
    $.ajax({
        type:"post",
        url:"../../../resource/relevanceFindResource",
        data: {
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<ul>';
                $.each(res.result, function (index,value) {
                    src += '<li><ul class="nav-first"><li><a  class="item-1"><i></i>' +
                        '<input class="resourceCheck" type="checkbox" value="'+value.msResourceId+'">【'+value.name+'】</a>';
                    $.each(value.resourceList,function (index1,one) {
                        src += '<ul class="nav-second fold "><li > <a class="item-2"><i></i>' +
                            '<input class="resourceCheck" type="checkbox" value="'+one.msResourceId+'">【'+one.name+'】</a>';
                        $.each(one.resourceList,function (index2,two) {
                            src += '<ul class="nav-three fold" ><li> <a class="item-3"><i></i>';
                            src += '<input class="resourceCheck" type="checkbox" value="'+two.msResourceId+'">【'+two.describe+'】</a>';
                            $.each(two.resourceList,function (index3,three) {
                                src += '<ul class="nav-four fold" ><li> <a class="item-4"><i></i>';
                                src += '<input class="resourceCheck" type="checkbox" value="'+three.msResourceId+'">【'+three.describe+'】</a>';
                                src += '</li></ul>';
                            });
                            src += '</li></ul>';
                        });
                        src += '</li></ul>';
                    });
                    src += '</li></ul></li>';
                });
                src += '</ul>';
                $(".allocatingResourceBody").html(src);
                // resourceCheck();
                getUserResource();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取复选框的值-------------------
function get_resource_data() {
    // var selectAll = $(".checkAll").parent().nextAll().children().children("input");
    resourceIds = "";
    var selectAll = $(".resourceCheck");
    var length = selectAll.length;
    for (var i = 0; i < length; i++) {
        if (selectAll[i].checked) {
            resourceIds += selectAll[i].value;
            if (i != length - 1) {
                resourceIds += ",";
            }
        }
    }
}

//分配资源复选框-------------------
function resourceCheck() {
    $(document).on('click','.resourceCheck',function(){
        if ($(".resourceCheck").prop("checked")) {
            $(".resourceCheck").parent().nextAll().children().children("input").prop("checked", true); //全选
        } else {
            $(".resourceCheck").parent().nextAll().children().children("input").prop("checked", false); //取消全选
        }
    })
    $(document).on('click','.checkOne',function(){
        if ($(".checkOne").prop("checked")) {
            $(".checkOne").parent().nextAll().children(".checkTwo").prop("checked", true); //全选
            // $("input[type='checkbox'][name='checkedres']").prop("checked", true); //全选
        } else {
            $(".checkOne").parent().nextAll().children(".checkTwo").prop("checked", false); //取消全选
        }
    });
    // //如果不是全选取消全选框
    // $(document).on("click","input[type='checkbox'][name='checkedres']",function(){
    //     selectBox();
    // });
}

//获取勾选的数据-------------------------------
function get_role_data() {
    var box = $("input[type='checkbox'][name='checkedres']");
    var length = box.length;
    for (var i = 0; i < length; i++) {
        if (box[i].checked) {
            msRoleId = box[i].value;
            name = box[i].parentNode.nextSibling.textContent;
            roleKey = box[i].parentNode.nextSibling.nextSibling.textContent;
            break;
        }
    }
}

//添加-------------------------------
function add_data() {
    name = $("#add-role input[name='name']").val();
    if (name == "") {
        layer.msg("角色名称不能为空,请重新输入",{icon:2});
        return false;
    }
    var roleKey = $("#add-role input[name='roleKey']").val();
    if (roleKey == "") {
        layer.msg("角色KEY不能为空,请重新输入",{icon:2});
        return false;
    }
    $.ajax({
        type:"post",
        url:"../../../role/insert",
        data: {
            "name":name,
            "roleKey":roleKey,
            "LogContent":"添加角色【 角色名称："+ name +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                layer.msg("添加成功",{icon:1});
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

//获取角色-------------------------------
function getRole() {
    $.ajax({
        type:"post",
        url:"../../../role/findAll",
        data: {
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                roles = res.result;
                // set_role_screen();
                add_role();
            }
        }
    });
}

//添加用户的关联角色
function add_role() {
    if (roles != "") {
        var str = '<option value="">--请选择--</option>';
        $.each(roles,function (index,value) {
            str += '<option value="'+value.roleKey+'">'+value.roleKey+'</option>';
        })
        $("#roleKey").html(str);
        // $("#add-role select").html(str);

        // $(".user-role-body select").html(str);
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
/**
 *  获取总数
 */
function getNum() {
    var name = $("#name").val();
    var roleKey = $("#roleKey").val();
    $.ajax({
        type:"post",
        url:"../../../role/count",
        data: {
            "name":name,
            "roleKey":roleKey
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
//主体数据
function getData(nowPage) {
    var name = $("#name").val();
    var roleKey = $("#roleKey").val();
    $.ajax({
        type:"post",
        url:"../../../role/conditionQueryRoleAll",
        data: {
            "name":name,
            "roleKey":roleKey,
            "nowPage":nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                $.each(res.result,function (index,value) {
                    // var time = new Date(value.createTime);
                    // var userName = "";
                    // $.each(value.userList,function (index,user) {
                    //     if (index > 0){
                    //         userName += ',';
                    //     }
                    //     userName += user.name;
                    // });
                    var date = getDate(value.createTime);
                    str += '<tr class="gradeX">' +
                        '<td id="td_input"><input type="checkbox" name="checkedres" value="'+value.msRoleId+'"></td>';
                    str += '<td>'+value.name+'</td>';
                    str += '<td>'+value.roleKey+'</td>';
                    str += '<td>'+date+'</td>';
                    str += '<td><input type="hidden" value="'+ value.msRoleId +'"/><a href="javascript:;" class="examine">查看详情</a></td></tr>';
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