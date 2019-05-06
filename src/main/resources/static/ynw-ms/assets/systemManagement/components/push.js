var num = 1;
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('推送管理');
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
    //获取字典数据并赋值
    setDictionary();
    //跟换样式
    setCss();
});

//获取字典数据并赋值
function setDictionary() {
    //获取消息类型
    findDictionary("PUSH_MESSAGE_TYPE");
    $("select[name='type']").change(function () {
        var type = $("select[name='type']").val();
        if (type == 'PMT_MODULE') {
            $("#add-APP-data .webUrl").hide();
            $("#add-APP-data .targetModuleName").show();
            $("#add-APP-data .targetModulePara").show();
        } else if (type == 'PMT_WEB') {
            $("#add-APP-data .webUrl").show();
            $("#add-APP-data .targetModuleName").hide();
            $("#add-APP-data .targetModulePara").hide();
        } else {
            $("#add-APP-data .webUrl").hide();
            $("#add-APP-data .targetModuleName").hide();
            $("#add-APP-data .targetModulePara").hide();
        }
    });
    //获取消息分组
    findDictionary("PUSH_MESSAGE_GROUP");
    //获取消息范围
    findDictionary("PUSH_MESSAGE_SCOPE");
    $("select[name='sendScope']").change(function () {
        var sendScope = $("select[name='sendScope']").val();
        if (sendScope == 'PMS_ALL') {
            $("#add-APP input[name='userId']").val('all');
            $("#add-APP-data .userExcel").hide();
            $("#add-APP-data .userId").hide();
        } else if (sendScope == 'PMS_GROUP') {
            $("#add-APP input[name='userId']").val("");
            $("#add-APP-data .userExcel").show();
            $("#add-APP-data .userId").hide();
        } else if (sendScope == 'PMS_SINGLE') {
            $("#add-APP-data .userExcel").hide();
            $("#add-APP input[name='userId']").val("");
            $("#add-APP-data .userId").show();
        } else {
            $("#add-APP-data .userExcel").hide();
            $("#add-APP input[name='userId']").val("");
            $("#add-APP-data .userId").hide();
        }
    });
    //获取消息业务类型
    findDictionary("PUSH_MESSAGE_BUSINESS_TYPE");
    //获取目标功能模块
    findDictionary("PUSH_MESSAGE_TARGET_MODULE");
    //图片点击绑定
    $(".photo img").click(function(){
        $("input[name='fileName']").click();
    });
    /**
     * 显示选择图片路径
     */
    $("input[name='fileName']").change(function(){
        // alert(fileUrl)
        // fileUrl = fileUrl.substring(fileUrl.lastIndexOf("\\")+1);
        var objUrl = getObjectURL(this.files[0]);
        if (objUrl) {
            // 在这里修改图片的地址属性
            $(".photo img").prop("src",objUrl);
            $("#add-APP input[name='imageUrl']").val(objUrl)
        }
    });
    //excel点击绑定
    $(".userExcel input[name='browse']").click(function(){
        $("input[name='file']").click();
    });
    /**
     * 显示选择excel路径
     */
    $("input[name='file']").change(function(){
        var objUrl = getObjectURL(this.files[0]);
        if (objUrl) {
            // 在这里修改图片的地址属性
            $(".userExcel input[name='excelName']").val(objUrl);
        }
    });
}

//根据不同浏览器获取本地图片url
function getObjectURL(file) {
    var url = null ;
    // 下面函数执行的效果是一样的，只是需要针对不同的浏览器执行不同的 js 函数而已
    if (window.createObjectURL!=undefined) { // basic
        url = window.createObjectURL(file) ;
    } else if (window.URL!=undefined) { // mozilla(firefox)
        url = window.URL.createObjectURL(file) ;
    } else if (window.webkitURL!=undefined) { // webkit or chrome
        url = window.webkitURL.createObjectURL(file) ;
    }
    return url ;
}

function setCss() {
    $(".newsPictures img").css("width", $(".newsPictures img").height());
}

/**
 *  未来点击事件
 */
function bottom_click() {

    //添加APP弹窗
    $(document).on('click','#add_date',function(){
        $("#add-APP-data .webUrl").hide();
        $("#add-APP-data .targetModuleName").hide();
        $("#add-APP-data .targetModulePara").hide();
        $("#add-APP-data .userExcel").hide();
        $("#add-APP-data .userId").hide();
        $("#add-APP-data .photo img").prop("src","../../img/heda.png");
        $("#add-APP select").val("");
        $("#add-APP textarea").val("");
        $("#add-APP input").val("");
        $("#add-APP input[name='browse']").val("浏览");
        $("#add-APP-data").show();
    });

    //添加
    $("#add-APP-confirm").click(function () {
        var type = $("#add-APP select[name='type']").val();
        if (type == "") {
            layer.msg("消息类型不能为空,请重新输入",{icon:2});
            return false;
        } else {
            if (type == 'PMT_MODULE') {
                var targetModuleName = $("#add-APP select[name='targetModuleName']").val();
                if (targetModuleName == '') {
                    layer.msg("目标功能模块名不能为空,请重新输入",{icon:2});
                    return false;
                }
            } else if (type == 'PMT_WEB') {
                var webUrl = $("#add-APP input[name='webUrl']").val();
                if (webUrl == '') {
                    layer.msg("消息网页地址不能为空,请重新输入",{icon:2});
                    return false;
                }
            }
        }
        var group = $("#add-APP select[name='group']").val();
        if (group == "") {
            layer.msg("消息分组不能为空,请重新输入",{icon:2});
            return false;
        }
        var sendScope = $("#add-APP select[name='sendScope']").val();
        if (sendScope == "") {
            layer.msg("消息目标范围不能为空,请重新输入",{icon:2});
            return false;
        } else {
            if (sendScope == 'PMS_GROUP') {
                var excelName = $("#add-APP input[name='excelName']").val();
                if (excelName == "") {
                    layer.msg("请选择excel表",{icon:2});
                    return false;
                }
            } else if (sendScope == 'PMS_SINGLE') {
                var userId =  $("#add-APP input[name='userId']").val();
                if (userId == "") {
                    layer.msg("用户编号不能为空,请重新输入",{icon:2});
                    return false;
                }
            }
        }
        var businessType = $("#add-APP select[name='businessType']").val();
        if (businessType == "") {
            layer.msg("消息业务类型不能为空,请重新输入",{icon:2});
            return false;
        }
        var title = $("#add-APP-data input[name='title']").val();
        if (title == "") {
            layer.msg("消息标题不能为空,请重新输入",{icon:2});
            return false;
        }
        var content = $("#add-APP-data textarea[name='content']").val();
        if (content == "") {
            layer.msg("消息内容不能为空,请重新输入",{icon:2});
            return false;
        }
        $("#add-APP-data input[name='LogContent']").val("推送信息【 推送标题："+ title +"】");
        var formDate = new FormData($( ".form-submit")[0]);
        // alert(number+":"+visitorOsType+":"+size+":"+isForceUpdate+":"+packageUrl+":"+description+":"+content+":")
        $.ajax({
            type:"post",
            url:"../../../push/insert",
            data: formDate,
            // contentType: "application/json",
            dataType: "json",
            contentType: false,
            processData: false,
            success: function(res){
                if (res.code == 1) {
                    $("#add-APP-data").hide();
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    layer.msg("推送成功",{icon:1});
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

    //跳转用户意见反馈
    $(document).on('click','#advice_manage',function(){
        var msResourceId = $(this).prev().val();
        $(location).attr("href","advice_management?msResourceId="+msResourceId + "&parentId="+$("#resourceId").val());
    });

}

//获取字典数据-------------------------------
function findDictionary(groupKey) {
    $.ajax({
        type:"post",
        url:"../../../dictionary/findDictionaryByGroupKey",
        data: {
            "groupKey":groupKey
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                $.each(res.result, function (index,value) {
                    src += '<option value="'+ value.itemKey +'">--'+ value.itemValue +'--</option>';
                });
                if (groupKey == 'PUSH_MESSAGE_TYPE') {
                    $("select[name='type'],#type").html(src);
                } else if (groupKey == 'PUSH_MESSAGE_GROUP') {
                    $("select[name='group'],#group").html(src);
                } else if (groupKey == 'PUSH_MESSAGE_SCOPE') {
                    $("select[name='sendScope'],#sendScope").html(src);
                } else if (groupKey == 'PUSH_MESSAGE_BUSINESS_TYPE') {
                    $("select[name='businessType'],#businessType").html(src);
                } else if (groupKey == 'PUSH_MESSAGE_TARGET_MODULE') {
                    $("select[name='targetModuleName']").html(src);
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
            var _this = box[i];
            syDictionaryId = _this.value;
            // _this = _this.parentNode.nextSibling;
            // groupKey = _this.textContent;
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
    var title = $("#title").val();
    var businessType = $("#businessType").val();
    var type = $("#type").val();
    var group = $("#group").val();
    var sendScope = $("#sendScope").val();
    var send = $("#send").val();
    var read = $("#read").val();
    $.ajax({
        type:"post",
        url:"../../../push/pushBodyCount",
        data: {
            "title":title,
            "businessType":businessType,
            "type":type,
            "group":group,
            "sendScope":sendScope,
            "send":send,
            "read":read
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
    var title = $("#title").val();
    var businessType = $("#businessType").val();
    var type = $("#type").val();
    var group = $("#group").val();
    var sendScope = $("#sendScope").val();
    var send = $("#send").val();
    var read = $("#read").val();
    $.ajax({
        type:"post",
        url:"../../../push/conditionQueryPshBody",
        data: {
            "title":title,
            "businessType":businessType,
            "type":type,
            "group":group,
            "sendScope":sendScope,
            "send":send,
            "read":read,
            "nowPage":nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                $.each(res.result,function (index,value) {
                    value.createTime = getDate(value.createTime);
                    if (null != value.beginTime) {
                        value.beginTime = getDate(value.beginTime);
                    } else {
                        value.beginTime = "未定时";
                    }
                    if (null != value.endTime) {
                        value.endTime = getDate(value.endTime);
                    } else {
                        value.endTime = "未定时";
                    }
                    if (value.send == 0) {
                        value.send = "未发送";
                    } else if (value.send == 1) {
                        value.send = "已发送";
                    }
                    if (null == value.imageUrl || value.imageUrl == "") {
                        value.imageUrl = "../../img/haven'tUploaded.png";
                    }
                    str += '<tr class="gradeX">' +
                        '<td id="td_input"><input type="checkbox" name="checkedres" value="'+value.mgBodyId+'"></td>';
                    str += '<td>'+value.title+'</td>';
                    str += '<td>'+value.businessType+'</td>';
                    str += '<td>'+value.typeName+'</td>';
                    str += '<td>'+value.groupName+'</td>';
                    str += '<td>'+value.sendScopeName+'</td>';
                    str += '<td>'+value.content+'</td>';
                    str += '<td><img src="'+ value.imageUrl +'" style="width: 60px;height: 40px"></td>';
                    str += '<td>'+value.webUrl+'</td>';
                    str += '<td>'+value.targetModule+'</td>';
                    str += '<td>'+value.targetModulePara+'</td>';
                    str += '<td>'+value.send+'</td>';
                    str += '<td>'+value.beginTime+'</td>';
                    str += '<td>'+value.endTime+'</td>';
                    str += '<td>'+value.createTime+'</td>';
                    str += '<td>'+value.sort+'</td>';
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