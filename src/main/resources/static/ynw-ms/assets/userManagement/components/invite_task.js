var num = 1;
var sweetsId = "";
var sweetsType = "";
$(function () {
    //返回
    getBack();
    setCss();
    //第一页数据
    getData(1);
    //获取按钮
    getSkip('邀请任务');
    //获取数据总数并分页
    getNum();
    //未来点击事件
    bottom_click();
    //隐藏操作框
    control_tip();
    //获取字典
    // getDictionary();
    // 全选复选框
    $("#checkAll").click(function () {
        selectAll();
    })
    //如果不是全选取消全选框
    $(document).on("click","input[type='checkbox'][name='checkedres']",function(){
        selectBox();
    });
    // monitorClick();
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
            sweetsId = box[i].value;
            sweetsType = box[i].parentNode.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.textContent;
        }
    }
}

//获取字典-------------------------------
function getDictionary() {
    $.ajax({
        type:"post",
        url:"../../../dictionary/findDictionaryByGroupKey",
        data: {
            "groupKey":'SUGAR_TYPE'

        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                $.each(res.result,function (index,value) {
                    src += '<option value="'+ value.itemKey +'">'+ value.itemValue +'</option>';
                });
                $("#withdrawDeposit").html(src);
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
    var name = $("#userName").val();
    var phone = $("#userPhone").val();
    var inviteName = $("#inviteName").val();
    var invitePhone = $("#invitePhone").val();
    var status = $("#status").val();
    $.ajax({
        type:"post",
        url:"../../../sweets/countInviteTask",
        data: {
            "userName":name,
            "userPhone":phone,
            "inviteName":inviteName,
            "invitePhone":invitePhone,
            "status":status
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
    var name = $("#userName").val();
    var phone = $("#userPhone").val();
    var inviteName = $("#inviteName").val();
    var invitePhone = $("#invitePhone").val();
    var status = $("#status").val();
    $.ajax({
        type:"post",
        url:"../../../sweets/conditionQueryInviteTask",
        data: {
            "userName":name,
            "userPhone":phone,
            "inviteName":inviteName,
            "invitePhone":invitePhone,
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
                    value.updateTime = getDate(value.updateTime);
                    if (value.status == 0) {
                        value.status = '未完成';
                    } else {
                        value.status = '已完成';
                    }
                    str += '<tr class="gradeX"><td id="td_input"><input type="checkbox" name="checkedres" value="'+value.acTaskId+'">';
                    str += '<td>'+value.userPhone+'</td>';
                    str += '<td>'+value.userName+'</td>';
                    str += '<td>'+value.taskKeyValue+'</td>';
                    str += '<td>'+value.status+'</td>';
                    str += '<td>'+value.invitePhone+'</td>';
                    str += '<td>'+value.inviteName+'</td>';
                    str += '<td>'+value.updateTime+'</td>';
                    str += '<td>'+value.createTime+'</td> </tr>';
                })
                // javascript:;
                $("#admin").html(str);
                //选中状态发生改变是隐藏功能弹窗
            } else if (res.code == 8) {
                $("#admin").empty();
                layer.msg(res.message,{icon:2});
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
        $(location).attr("href","withdraw_deposit?msResourceId="+resourceId + "&name="+ $("#name").val() + "&phone="+ $("#phone").val() +
            "&type="+ $("#type").val() + "&beginDate=" + $("#beginDate").val() + "&endDate=" + $("#endDate").val() + "&withdrawDeposit=" + $("#withdrawDeposit").val() +
            "&num=" + $("#num").val());
    });
}