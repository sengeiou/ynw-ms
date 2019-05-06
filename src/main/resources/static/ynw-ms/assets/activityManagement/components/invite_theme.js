var num = 1;
var atInviteThemeId = "";
var checkStatus = "";
var icon = '';
var bgImageUrl = '';
$(function () {
    getBack();
    setCss();
    //第一页数据
    getData(1);
    //获取按钮
    getSkip('主题管理');
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
    // previewFile('icon');
    // previewFile('bgImageUrl');
    $(".phone").click(function () {
        $(this).next().click();
    });
    /**
     * 显示选择截图路径
     */
    $(".fileName").change(function(){
        var objUrl = getObjectURL(this.files[0]);
        if (objUrl) {
            // 在这里修改图片的地址属性
            $(this).prev().prop("src",objUrl);
            // $(this).next().val(1);
        }
    });
});

//根据不同浏览器获取本地图片url
function getObjectURL(file) {
    var url = null ;
    // 下面函数执行的效果是一样的，只是需要针对不同的浏览器执行不同的 js 函数
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
     *  新增
     */
    $(document).on('click', '#add_date', function () {
        $("#add input[name='name'],#add select[name='status']").val("");
        $("#add .bgImageUrl,#add .icon").prev().prop("src", "../../img/addQRCODE.png")
        // $("#add .fileStatus").val(0);
        $("#add").show();
    });
    
    $("#add_confirm").click(function () {
        var name = $("#add input[name='name']").val();
        if (name == '') {
            layer.msg('请输入主题名称',{icon:2});
            return false;
        }
        var status = $("#add select[name='status']").val();
        if (status == '') {
            layer.msg('请选择主题状态',{icon:2});
            return false;
        }
        icon = $("#add .icon").val();
        if (icon.length == 0) {
            layer.msg('请选择icon图片',{icon:2});
            return false;
        }
        bgImageUrl = $("#add .bgImageUrl").val();
        if (bgImageUrl.length == 0) {
            layer.msg('请选择主题背景',{icon:2});
            return false;
        }
        $("#add input[name='LogContent']").val("添加约活动主题【主题名称："+ name +"】")
        var formDate = new FormData($( ".addTheme")[0]);
        $.ajax({
            type:"post",
            url:"../../../inviteInviteTheme/insert",
            data: formDate,
            // contentType: "application/json",
            contentType: false,
            processData: false,
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    $("#add").hide()
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    $(document).on('click', '#update_date', function () {
        if (getBox() == 1) {
            getChecked();
            findById();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    $("#update_confirm").click(function () {
        var name = $("#update input[name='name']").val();
        if (name == '') {
            layer.msg('请输入主题名称',{icon:2});
            return false;
        }
        var status = $("#update select[name='status']").val();
        if (status == '') {
            layer.msg('请选择主题状态',{icon:2});
            return false;
        }
        $("#update input[name='LogContent']").val("修改约活动主题【主题名称："+ name +"】");
        $("#update input[name='atInviteThemeId']").val(atInviteThemeId);
        var formDate = new FormData($( ".addTheme")[1]);
        $.ajax({
            type:"post",
            url:"../../../inviteInviteTheme/update",
            data: formDate,
            // contentType: "application/json",
            contentType: false,
            processData: false,
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    $("#update").hide()
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

}

//压缩图片
function previewFile(fileName){
    $("input[name='"+ fileName +"']").change(function () {
        var _this = $(this);
        $("input[name='icon']").imageCompress({
            'quality': 50,
            'onloadStart': function(result){
                console.log('读取图片开始'+result);
            },
            'onloadEnd': function(result){
                console.log('读取图片结束'+result);
            },
            'oncompressStart': function(result){
                console.log('压缩图片开始'+result);
            },
            'oncompressEnd': function(result){
                console.log('压缩图片结束'+result);
                $('#preview').append(result);
                $('#preview').find('img').addClass('preview');
                var src = $(".preview").prop("src");
                if (fileName == 'icon') {
                    bgImageUrl = src;
                }
                if (fileName == 'icon') {
                    bgImageUrl = src;
                }
                _this.prev().prop("src", src);
            },
            'callback': function(){
                console.log('处理完毕');
                alert($("input[name='photoFileName']")[0].files[0].size);
            }
        });
    });
}

function findById() {
    $.ajax({
        type:"post",
        url:"../../../inviteInviteTheme/findById",
        data: {
            "inviteThemeId": atInviteThemeId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var value = res.result;
                $("#update input[name='name']").val(value.name);
                $("#update select[name='status']").val(value.status);
                $("#update input[name='iconPhone']").prev().prop('src', value.icon);
                $("#update input[name='imageUrl']").prev().prop('src',value.bgImageUrl);
                $("#update").show();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

/**
 * 更新活动审核状态
 * @param status
 */
function setCheckStatus(status, reason) {
    $.ajax({
        type:"post",
        url:"../../../engagement/updateStatus",
        data: {
            "atRegisterId": registerId,
            "checkStatus": status,
            "reason":reason,
            "LogContent":"修改活动状态【 活动状态改为："+ status +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var count = getNum();
                setPage(count);
                getData(num);
                $(".unified-closed").hide()
                layer.msg(res.message,{icon:1});
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取选中的-------------------------------
function getChecked() {
    var box = $("input[type='checkbox'][name='checkedres']");
    var length = box.length;
    for (var i = 0; i < length; i++) {
        if (box[i].checked) {
            atInviteThemeId = box[i].value;
            // checkStatus = box[i].parentNode.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.textContent;
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
    $.ajax({
        type:"post",
        url:"../../../inviteInviteTheme/count",
        data: {
            "name":name
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
    $.ajax({
        type:"post",
        url:"../../../inviteInviteTheme/conditionQueryInviteTheme",
        data: {
            "name":name,
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
                    str += '<tr class="gradeX"><td id="td_input"><input type="checkbox" name="checkedres" value="'+value.atInviteThemeId+'">';
                    str += '<td>'+value.name+'</td>';
                    str += '<td><img src="'+ value.icon +'" style="width: 60px;height: 40px"></td>';
                    str += '<td><img src="'+ value.bgImageUrl +'" style="width: 120px;height: 40px"></td>';
                    str += '<td>'+value.sort+'</td>';
                    if (value.status == 1) {
                        str += '<td>正常</td>';
                    } else if (value.status == 0) {
                        str += '<td>删除</td>';
                    }
                    str += '<td>'+value.createTime+'</td> </tr>';
                })
                // javascript:;
                $("#report").html(str);
                // $('#report img').zoomify();
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