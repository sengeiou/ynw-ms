var num = 1;
var syAdviceId = "";
var userId = '';
$(function () {
    //返回
    getBack();
    //第一页数据
    getData(1);
    //获取按钮
    getSkip('用户反馈管理');
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
    //点击隐藏
    $(".screenshot-cancel").click(function () {
        $(".screenshot-user").hide();
    });
    //获取字典数据并赋值
    findDictionary();
});

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
        getData(num)
    });

    /**
     *  回复
     */
    $(document).on('click','#reply_data',function(){
        var flag = getBox();
        if (flag == 1) {
            get_role_data();
            $("#add-report textarea").val("");
            $("#add-report-classify-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    $("#add-report-confirm").click(function () {
        var type = $("#add-report select[name='type']").val();
        if (type == "") {
            layer.msg("消息类型不能为空,请重新输入",{icon:2});
            return false;
        } else {
            if (type == "PMT_WEB") {
                var webUrl = $("#add-report input[name='webUrl']").val();
                if (webUrl == "") {
                    layer.msg("消息网页地址不能为空,请重新输入",{icon:2});
                    return false;
                }
            }
        }
        var content = $("#add-report textarea").val();
        if (content == "") {
            layer.msg("回复内容不能为空,请重新输入",{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../push/insert",
            data: {
                "userId":userId,
                "infoOne":syAdviceId,
                "type":type,
                "webUrl":webUrl,
                "group":"PMG_SYSTEM",
                "sendScope":"PMS_SINGLE",
                "businessType":"PMBT_ADVICE",
                "title":"用户意见反馈回复",
                "content":content,
                "content":content,
                "LogContent":"推送信息【 推送标题：用户"+ syAdviceId +"意见反馈回复】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#add-report-classify-data").hide();
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

    //查看图片
    $(document).on("click",".advicePhoto",function () {
       var url = $(this).prop("src");
        $("#user-img img").prop("src",url);
       $("#user-img").show();
    });

}

//获取字典数据-------------------------------
function findDictionary() {
    $.ajax({
        type:"post",
        url:"../../../dictionary/findDictionaryByGroupKey",
        data: {
            "groupKey":"PUSH_MESSAGE_TYPE"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                $.each(res.result, function (index,value) {
                    if (value.itemKey != 'PMT_MODULE') {
                        src += '<option value="'+ value.itemKey +'">--'+ value.itemValue +'--</option>';
                    }
                });
                $("select[name='type']").html(src);
                $("select[name='type']").change(function () {
                    var type = $("select[name='type']").val();
                    if (type == 'PMT_WEB') {
                        $("#add-report input[name='webUrl']").val("");
                        $("#add-report .webUrl").show();
                    } else {
                        $("#add-report input[name='webUrl']").val("");
                        $("#add-report .webUrl").hide();
                    }
                });
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
            syAdviceId = _this.value;
            _this = _this.parentNode.nextSibling;
            userId = _this.textContent;
            // _this = _this.nextSibling;
            // groupDesc = _this.textContent;
            // _this = _this.nextSibling;
            // itemKey = _this.textContent;
            // _this = _this.nextSibling;
            // itemValue = _this.textContent;
            // _this = _this.nextSibling;
            // describe = _this.textContent;
            // _this = _this.nextSibling;
            // sort = _this.textContent;
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
    var userName = $("#userName").val();
    var type = $("#type").val();
    var reply = $("#reply").val();
    $.ajax({
        type:"post",
        url:"../../../push/adviceCount",
        data: {
            "userName":userName,
            "type":type,
            "reply":reply,
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
    var userName = $("#userName").val();
    var type = $("#type").val();
    var reply = $("#reply").val();
    $.ajax({
        type:"post",
        url:"../../../push/conditionQueryAdvice",
        data: {
            "userName":userName,
            "type":type,
            "reply":reply,
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
                        '<td id="td_input"><input type="checkbox" name="checkedres" value="'+value.syAdviceId+'"></td>';
                    str += '<td style="display:none;">'+value.acUserId+'</td>';
                    str += '<td>'+value.userName+'</td>';
                    if (value.type == 0) {
                        str += '<td>文本</td>';
                        str += '<td>'+value.content+'</td>';
                    } else if (value.type == 1) {
                        str += '<td>图片</td>';
                        str += '<td><img class="advicePhoto" style="width: 60px;height: 40px;object-fit: contain" src="'+value.content+'" /></td>';
                    }
                    if (value.businessId == "") {
                        str += '<td style="color: red">未回复</td>';
                    } else {
                        str += '<td>已回复</td>';
                    }
                    str += '<td>'+date+'</td>';
                    str += '</tr>';
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

//返回————————————————————————————
function getBack() {
    $(".back").click(function () {
        var resourceId = $("#parentId").val();
        $(location).attr("href","push_management?msResourceId="+resourceId);
    });
}