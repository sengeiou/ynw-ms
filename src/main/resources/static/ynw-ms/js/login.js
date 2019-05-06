


//bd大小

//$("#bd").height($(window).height());

$(function () {
    $("#forget").click(function () {
        alert("请联系系统管理员重置您的密码，获取重置的初始密码后请登录修改密码！")
    })
    $("#btn").click(function () {
        login();
    })
})

//监听回车事件
function changeEnter(e,index){
    var id = ["","#password"];
    if(e.keyCode == 13){
        if(index != 2 )
            $(id[index]).focus();
        else{
            login();
        }
    }
}

function login() {
    var msUserId = $("#userName").val();
    if (msUserId == "") {
        layer.msg("账号不能为空,请重新输入",{icon:2});
        return false;
    }
    var password = $("#password").val();
    if (password == "") {
        layer.msg("密码不能为空,请重新输入",{icon:2});
        return false;
    }
    $.ajax({
        type:"post",
        url:"admin/login",
        data: {
            "name": msUserId,
            "password":password
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                top.location.href = "../ynw-ms/main";
                // $(location).attr("href","../ynw-ms/main");
            } else {
                $("#password").val("");
                layer.msg(res.message,{icon:2});
            }
            // $(location).attr("href","book_list.html");
        }
    });
}