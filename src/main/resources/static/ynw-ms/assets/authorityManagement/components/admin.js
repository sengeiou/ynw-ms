var num = 1;
var roles = "";
var userId = "";
var name = '';
$(function () {
    getData(1);
    getNum();
    getRole()
    getButton('账户管理');
    bottom_click();
    $("#checkAll").click(function () {
        selectAll();
    })
    $(document).on("click","input[type='checkbox'][name='checkedres']",function(){
        selectBox();
    });
    control_tip();
})

/**
 *  未来点击事件
 */
function bottom_click() {

    /**
     *  添加
     */
    $(document).on('click','#add_date',function(){
        $(".initialize input").val("")
        $(".initialize select").val("")
        $("#add-date").show();
    });

    //添加
    $("#add-confirm").click(function () {
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
        getData(num);
    });

    /**
     *  编辑
     */
    $(document).on('click','#update_date',function(){
        var flag = getBox();
        if (flag == 1) {
            getUser();
            $("#update-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    //编辑
    $("#update-confirm").click(function () {
        update_data();
    })

    /**
     *  删除
     */
    $(document).on('click','#delete_date',function(){
        var flag = getBox();
        if (flag == 1) {
            var box = $("input[type='checkbox'][name='checkedres']");
            var length = box.length;
            for (var i = 0; i < length; i++) {
                if (box[i].checked) {
                    userId = box[i].value;
                    name = box[i].parentNode.nextSibling.textContent;
                    break;
                }
            }
            $(".delete-body span").text(name);
            $("#delete-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    //删除
    $("#delete-confirm").click(function () {
        delete_date();
    })

    //重置密码
    $(document).on('click','#nowPassword',function(){
        var name = "";
        var flag = getBox();
        if (flag == 1) {
            var box = $("input[type='checkbox'][name='checkedres']");
            var length = box.length;
            for (var i = 0; i < length; i++) {
                if (box[i].checked) {
                    userId = box[i].value;
                    name = box[i].parentNode.nextSibling.textContent;
                    break;
                }
            }
            $(".nowPage-body span").text(name);
            setPassword(name);
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });
    //关联角色
    $(document).on('click','#roleAndUser',function(){
        var name = "";
        var flag = getBox();
        if (flag == 1) {
            var box = $("input[type='checkbox'][name='checkedres']");
            var length = box.length;
            for (var i = 0; i < length; i++) {
                if (box[i].checked) {
                    userId = box[i].value;
                    name = box[i].parentNode.nextSibling.textContent;
                    break;
                }
            }
            $(".user-role-body span").text(name);
            $("#userAndRole-date").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    //关联角色
    $("#user-role-confirm").click(function () {
        setUserAndRole();
    });

    //修改管理员状态
    $(document).on("click","#update_status",function () {
        var flag = getBox();
        if (flag == 1) {
            getUser();
            $.ajax({
                type:"post",
                url:"../../../admin/updateUserStatus",
                data: {
                    "userId":userId
                },
                // contentType: "application/json",
                dataType: "json",
                success: function(res){
                    if (res.code == 1) {
                        var count = getNum();
                        setPage(count);
                        getData(num);
                        layer.msg(res.message,{icon:1});
                    } else {
                        layer.msg(res.message,{icon:2});
                    }
                }
            });
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

}

//关联角色
function setUserAndRole() {
    var roleId = $(".user-role-body select").val();
    if (roleId == "" || null == roleId) {
        layer.msg("请选择一个角色",{icon:2});
        return false;
    }
    $.ajax({
        type:"post",
        url:"../../../admin/insertUserAndRole",
        data: {
            "msUserId":userId,
            "msRoleId":roleId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#userAndRole-date").hide();
                var count = getNum();
                setPage(count);
                getData(num);
                layer.msg("关联成功",{icon:1});
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//重置密码123456
function setPassword(name) {
    $.ajax({
        type:"post",
        url:"../../../admin/updatePassword",
        data: {
            "msUserId":userId,
            "LogContent":"重置管理员密码【 管理员名称："+ name +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#nowPage-date").show();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//删除管理员
function delete_date() {
    $.ajax({
        type:"post",
        url:"../../../admin/delete",
        data: {
            "msUserId":userId,
            "LogContent":"删除管理员【 管理员名称："+ name +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#delete-data").hide();
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
}

//获取管理员信息
function getUser() {
    var box = $("input[type='checkbox'][name='checkedres']");
    var length = box.length;
    for (var i = 0; i < length; i++) {
        if (box[i].checked) {
            userId = box[i].value;
        }
    }
    $.ajax({
            type:"post",
            url:"../../../admin/findById",
            data: {
                "msUserId":userId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    var result = res.result;
                    $("#update-name").val(result.name);
                    $("#update-password").val(result.password);
                    $("#update-realName").val(result.realName);
                    $("#update-phone").val(result.phoneNumber);
                    $("#update-sex").val(result.sex);
                    $("#update-email").val(result.email);
                }
            }
        });
        }

//编辑数据
function update_data() {
    var name = $("#update-name").val();
    if (name == "") {
        layer.msg("用户名不能为空,请重新输入",{icon:2});
        return false;
    }
    var password = $("#update-password").val();
    if (password == "") {
        layer.msg("m密码不能为空,请重新输入",{icon:2});
        return false;
    }
    var realName = $("#update-realName").val();
    if (realName == "") {
        layer.msg("真实姓名不能为空,请重新输入",{icon:2});
        return false;
    }
    var phone = $("#update-phone").val();
    if (phone == "") {
        layer.msg("电话不能为空,请重新输入",{icon:2});
        return false;
    }
    var sex = $("#update-sex").val();
    if (sex == "") {
        layer.msg("性别不能为空,请重新输入",{icon:2});
        return false;
    }
    var email = $("#update-email").val();
    // alert(name+":"+password+":"+realName+":"+phone+":"+sex+":"+email);
    $.ajax({
        type:"post",
        url:"../../../admin/update",
        data: {
            "msUserId":userId,
            "password":password,
            "name":name,
            "realName":realName,
            "phoneNumber":phone,
            "sex":sex,
            "email":email,
            "LogContent":"编辑管理员【 管理员名称："+ name +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#update-data").hide();
                var count = getNum();
                setPage(count);
                getData(num);
                layer.msg("修改成功",{icon:1});
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//监听回车事件
function changeEnter(e,index){
    var id = ["","#password","#realName","#phone","#sex","#email","#add-role select",
        "update-name","#update-password","#update-realName","#update-phone","#update-sex","#update-email"];
    if(e.keyCode == 13){
        if(index != 7 )
            if (index != 13) {
                $(id[index]).focus();
            } else {
                update_data();
            }
        else{
            add_data();
        }
    }
}

//添加管理员
function add_data() {
    var name = $("#add-name").val();
    if (name == "") {
        layer.msg("用户名不能为空,请重新输入",{icon:2});
        return false;
    }
    var password = $("#password").val();
    if (password == "") {
        layer.msg("m密码不能为空,请重新输入",{icon:2});
        return false;
    }
    var realName = $("#realName").val();
    if (realName == "") {
        layer.msg("真实姓名不能为空,请重新输入",{icon:2});
        return false;
    }
    var phone = $("#phone").val();
    if (phone == "") {
        layer.msg("电话不能为空,请重新输入",{icon:2});
        return false;
    }
    var sex = $("#sex").val();
    if (sex == "") {
        layer.msg("性别不能为空,请重新输入",{icon:2});
        return false;
    }
    var add_role = $("#add-role select").val();
    var email = $("#email").val();
    // alert(name+":"+password+":"+realName+":"+phone+":"+sex+":"+add_role+":"+email);
    $.ajax({
        type:"post",
        url:"../../../admin/insert",
        data: {
            "password":password,
            "name":name,
            "realName":realName,
            "phoneNumber":phone,
            "sex":sex,
            "roleId":add_role,
            "email":email,
            "LogContent":"添加管理员【 管理员名称："+ name +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                layer.msg("添加成功",{icon:1});
                $("#add-date").hide();
                var count = getNum();
                setPage(count);
                getData(num)
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
 *  获取角色
 */
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
            str += '<option value="'+value.msRoleId+'">'+value.name+'</option>';
        })
        $("#add-role select").html(str);
        $("#role").html(str);
        $(".user-role-body select").html(str);
    }
}

// //角色筛选
// function set_role_screen() {
//     if (roles != "") {
//         var str = '<option value="">--请选择--</option>';
//         $.each(roles,function (index,value) {
//             str += '<option value="'+value.msRoleId+'">'+value.name+'</option>';
//         })
//         $("#role").html(str);
//     }
// }
// //账户关联角色
// function set_role_screen() {
//     if (roles != "") {
//         var str = '<option value="">--请选择--</option>';
//         $.each(roles,function (index,value) {
//             str += '<option value="'+value.msRoleId+'">'+value.name+'</option>';
//         })
//         $("#role").html(str);
//     }
// }

/**
 *  获取总数
 */
function getNum() {
    var name = $("#name").val();
    var role = $("#role").val();
    var phoneNumber = $("#phoneNumber").val();
    var status = $("#status").val();
    $.ajax({
        type:"post",
        url:"../../../admin/count",
        data: {
            "name":name,
            "roleId":role,
            "phoneNumber":phoneNumber,
            "status":status
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                setPage(res.result);
            } else {
                setPage(0);
            }
        }
    });
}

/**
 *  获取数据
 */
function getData(nowPage) {
    var name = $("#name").val();
    var role = $("#role").val();
    var phoneNumber = $("#phoneNumber").val();
    var status = $("#status").val();
    $.ajax({
        type:"post",
        url:"../../../admin/conditionQueryUserAll",
        data: {
            "name":name,
            "roleId":role,
            "phoneNumber":phoneNumber,
            "status":status,
            "nowPage": nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                $.each(res.result,function (index,value) {
                    // var time = new Date(value.createTime);
                    value.createTime = getDate(value.createTime);
                    var role = "";
                    if (value.roleList.length > 0) {
                        role = value.roleList[0];
                    }
                    if (value.sex == 1) {
                        value.sex = "女";
                    } else {
                        value.sex = "男";
                    }
                    if (value.status == 1) {
                        value.status = "正常";
                    } else {
                        value.status = "失效";
                    }
                    str += '<tr class="gradeX"><td id="td_input"><input type="checkbox" name="checkedres" value="'+value.msUserId+'"></td>';
                    str += '<td>'+value.name+'</td>';
                    if (role == "") {
                        str += '<td>未设置角色</td>';
                    } else {
                        str += '<td>'+role.name+'</td>';
                    }
                    str += '<td>'+value.phoneNumber+'</td>';
                    str += '<td>'+value.realName+'</td>';
                    str += '<td>'+value.sex+'</td>';
                    str += '<td>'+value.email+'</td>';
                    str += '<td>'+value.createTime+'</td>';
                    str += '<td>'+value.status+'</td></tr>';
                })
                $("#admin").html(str);
                //选中状态发生改变是隐藏功能弹窗
                control_tip_box();
            } else if (res.code == 8) {
                $("#admin").empty();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}