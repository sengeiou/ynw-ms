var num = 1;
$(function () {
    //返回
    getBack();
    setCss();
    //第一页数据
    getData(1);
    //获取按钮
    getSkip('参与用户');
    //获取数据总数并分页
    getNum();
    //未来点击事件
    bottom_click();
    //隐藏操作框
    control_tip();
    // 全选复选框
    $("#checkAll").click(function () {
        selectAll();
    })
    //如果不是全选取消全选框
    $(document).on("click","input[type='checkbox'][name='checkedres']",function(){
        selectBox();
    });
    monitorClick();
});

function setCss() {
    // $("#QRCode").css("height", $("#QRCode").width());
    // alert($("#QRCode").width()+":"+$("#QRCode").height())
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
        getData(num);
    });

}

//获取选中的-------------------------------
function getChecked() {
    var box = $("input[type='checkbox'][name='checkedres']");
    var length = box.length;
    for (var i = 0; i < length; i++) {
        if (box[i].checked) {
            registerId = box[i].value;
            checkStatus = box[i].parentNode.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.textContent;
        }
    }
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
    var name = $("#name").val();
    var phone = $("#phone").val();
    var registerId = $("#registerId").val();
    $.ajax({
        type:"post",
        url:"../../../engagement/countInvite",
        data: {
            "atRegisterId":registerId,
            "realName":name,
            "phoneNumber":phone
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

//获取数据主体
function getData(nowPage) {
    var name = $("#name").val();
    var phone = $("#phone").val();
    var registerId = $("#registerId").val();
    $.ajax({
        type:"post",
        url:"../../../engagement/conditionQueryInvite",
        data: {
            "realName":name,
            "phoneNumber":phone,
            "atRegisterId":registerId,
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
                    str += '<tr class="gradeX"><td id="td_input"><input type="checkbox" name="checkedres" value="'+value.atInviteUserId+'">';
                    str += '<td>'+value.realName+'</td>';
                    str += '<td>'+value.phoneNumber+'</td>';
                    if (value.userType == 0) {
                        str += '<td>参加人</td>';
                    } else {
                        str += '<td>发起人</td>';
                    }
                    str += '<td>'+value.createTime+'</td> </tr>';
                })
                // javascript:;
                $("#report").html(str);
                //选中状态发生改变是隐藏功能弹窗
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
        $(location).attr("href",'engagement_management?msResourceId='+resourceId + "&name="+ $("#inviteName").val() + "&userName="+ $("#userName").val() +
            "&phone="+ $("#userPhone").val() + "&atCtgyId=" + $("#atCtgyId").val() +
        "&atInviteThemeId=" + $("#atInviteThemeId").val() + "&checkStatus=" + $("#checkStatus").val() + "&beginDate=" + $("#beginDate").val() +
        "&num=" +  $("#num").val());
    });
}