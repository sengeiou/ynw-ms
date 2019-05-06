var num = 1;
var sweetsId = "";
var sweetsType = "";
$(function () {
    setCss();
    //第一页数据
    getData(1);
    //获取按钮
    getButton('糖果管理');
    //获取数据总数并分页
    getNum();
    //未来点击事件
    bottom_click();
    //隐藏操作框
    control_tip();
    //获取字典
    getDictionary();
    // 全选复选框
    $("#checkAll").click(function () {
        selectAll();
    })
    //如果不是全选取消全选框
    $(document).on("click","input[type='checkbox'][name='checkedres']",function(){
        selectBox();
    });
    monitorClick();
    //链接带参时
    setInterface();
});


//获取url头部参数
function GetQueryString(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  decodeURI(r[2]); return null;
}

function setInterface() {
    var name = GetQueryString("name");
    if (name != null) {
        $("#name").val(name);
    }
    var phone = GetQueryString("phone");
    if (phone != null) {
        $("#phone").val(phone);
    }
    var type = GetQueryString("type");
    if (type != null) {
        $("#type").val(type);
    }
    var beginDate = GetQueryString("beginDate");
    if (beginDate != null) {
        $("#beginDate").val(beginDate);
    }
    var endDate = GetQueryString("endDate");
    if (endDate != null) {
        $("#endDate").val(endDate);
    }
    var num1 = GetQueryString("num");
    if (num1 != null) {
        num = num1;
    }
    var count = getNum();
    getData(num);
    setPage(count)
}

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

    /**
     * 提现
     */
    $(document).on('click', '#withdraw', function () {
        if (getBox() == 1) {
            getChecked();
            if (sweetsType == '兑换中') {
                $.ajax({
                    type:"post",
                    url:"../../../sweets/findById",
                    data: {
                        "acSugarFlowId": sweetsId
                    },
                    // contentType: "application/json",
                    dataType: "json",
                    success: function(res){
                        if (res.code == 1) {
                            var value = res.result;
                            $("#alipayCode").prop("src", value.alipayCode);
                            // $("#sum").text(value.sum);
                            $("#acUserId").val(value.acUserId)
                            $("#quantity").text(value.quantity);
                            $("#money").text(Math.floor(value.quantity/100));
                            $("#alipayAccount").text(value.alipayAccount);
                            $("#realName").text(value.realName);
                            $("#withdraw_deposit").show();
                        } else {
                            layer.msg(res.message,{icon:2});
                        }
                    }
                });
            } else {
                layer.msg("请选择申请兑换的用户",{icon:2});
            }
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    $("#confirm").click(function () {
        var money = $("#money").text()*100;
        $.ajax({
            type:"post",
            url:"../../../sweets/update",
            data: {
                "quantity": money,
                "acSugarFlowId": sweetsId,
                "LogContent":"提现【流水编号:"+sweetsId+" 金额:"+money/100+"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#withdraw_deposit").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
        // var sum = $("#sum").text();
        // if (sum > money) {
        //
        // } else {
        //     $("#withdraw_deposit").hide();
        //     layer.msg("用户糖豆数量不够",{icon:2});
        // }
    });

    /**
     * 取消提现
     */
    $('#withdrawal').click(function () {
        $("#reason").val("");
        $("#cancelTheWithdrawal").show();
    });

    /**
     *  推送
     */
    $("#withdrawal_confirm").click(function () {
        var reason = $("#reason").val();
        if (reason == '') {
            layer.msg('推送理由不能为空',{icon:2});
            return false;
        }
        var money = $("#money").text()*100;
        var acUserId = $("#acUserId").val();
        getChecked();
        $.ajax({
            type:"post",
            url:"../../../sweets/withdrawalOfFailure",
            data: {
                "userId":acUserId,
                "content":reason,
                "quantity":money,
                "acSugarFlowId":sweetsId,
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $(".unified-closed").hide();
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    /**
     * 跳转糖果总量
     */
    $(document).on("click","#totalCandy", function () {
        //获取自己的资源编号
        var msResourceId = $(this).prev().val();
        skip("withdraw_sum?parentId="+$("#resourceId").val() +"&msResourceId="+ msResourceId);
    });

    /**
     * 跳转邀请任务
     */
    $(document).on("click","#inviteTask", function () {
        //获取自己的资源编号
        var msResourceId = $(this).prev().val();
        skip("invite_task?parentId="+$("#resourceId").val() +"&msResourceId="+ msResourceId);
    });

}

/**
 *  跳转并获取参数
 */
function skip(url) {
    url += "&name="+ $("#name").val() + "&phone="+ $("#phone").val() + "&type=" + $("#type").val() + "&beginDate=" + $("#beginDate").val() +
        "&endDate=" + $("#endDate").val() +
        "&withdrawDeposit=" + $("#withdrawDeposit").val() + "&num=" + num;
    $(location).attr("href", url);
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
                var withdrawDeposit = GetQueryString("withdrawDeposit");
                if (withdrawDeposit != null) {
                    $("#withdrawDeposit").val(withdrawDeposit);
                }
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
    var name = $("#name").val();
    var phone = $("#phone").val();
    var type = $("#type").val();
    var assoBusinessKey = $("#withdrawDeposit").val();
    var endDate = $("#endDate").val();
    var beginDate = $("#beginDate").val();
    $.ajax({
        type:"post",
        url:"../../../sweets/count",
        data: {
            "userName":name,
            "userPhone":phone,
            "type":type,
            "assoBusinessKey":assoBusinessKey,
            "endDate":endDate,
            "beginDate":beginDate
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
    var type = $("#type").val();
    var assoBusinessKey = $("#withdrawDeposit").val();
    var endDate = $("#endDate").val();
    var beginDate = $("#beginDate").val();
    $.ajax({
        type:"post",
        url:"../../../sweets/conditionQuerySweetsFlow",
        data: {
            "userName":name,
            "userPhone":phone,
            "type":type,
            "assoBusinessKey":assoBusinessKey,
            "endDate":endDate,
            "beginDate":beginDate,
            "nowPage": nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                var total = 0;
                var expend = 0;
                var income = 0;
                $.each(res.result,function (index,value) {
                    // var time = new Date(value.createTime);
                    value.createTime = getDate(value.createTime);
                    value.updateTime = getDate(value.updateTime);
                    if (value.type == 1) {
                        value.type = "支出";
                        expend += parseInt(value.quantity);
                    } else {
                        value.type = "收入";
                        income += parseInt(value.quantity);
                    }
                    total += parseInt(value.quantity);
                    str += '<tr class="gradeX"><td id="td_input"><input type="checkbox" name="checkedres" value="'+value.acSugarFlowId+'">' +
                        '<td><img src="'+ value.userImg +'" style="width: 60px;height: 40px"></td>';
                    str += '<td>'+value.userPhone+'</td>';
                    str += '<td>'+value.userName+'</td>';
                    str += '<td>'+value.type+'</td>';
                    str += '<td>'+value.quantity+'</td>';
                    str += '<td>'+value.businessKeyValue+'</td>';
                    str += '<td>'+value.updateTime+'</td>';
                    str += '<td>'+value.createTime+'</td> </tr>';
                })
                // javascript:;
                $("#admin").html(str);
                $(".total").text(total);
                $(".expend").text(expend);
                $(".income").text(income);
                //选中状态发生改变是隐藏功能弹窗

            } else if (res.code == 8) {
                $("#admin").empty();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}