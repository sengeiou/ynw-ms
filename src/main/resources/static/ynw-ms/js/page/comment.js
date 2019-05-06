//监听点击事件
function monitorClick() {
    $('.pageBody').bind('click',function(){
        $(".unified-closed").hide();
    });
}

//选中的主体数据发生变化时，隐藏功能框
function control_tip_box() {
    $("input[type='checkbox'][name='checkedres']").change(function () {
        $(".delete-data").hide();
        $(".operation-data").hide();
        $(".operation-exam-data").hide();
        $(".unified-closed").hide();
    });
}


//隐藏
function control_tip() {
    $(".cancel").click(function () {
        $("#add-date").hide();
        $("#update-data").hide();
        $("#delete-data").hide();
        $(".delete-data").hide();
        $("#nowPage-date").hide();
        $("#userAndRole-date").hide();
        $(".operation-data").hide();
        $(".operation-delete-data").hide();
        $(".comment-data").hide();
        $(".operation-exam-data").hide();
        $(".unified-closed").hide();
        $(".general-delete").hide();
    })
}

//鼠标触碰
function data_hover() {
    $("#manage_date").hover(function(){
        $(".bottom_manage").show();
    },function(){
        $(".bottom_manage").hide();
    });
    $(".bottom_manage input").hover(function(){
        $(".bottom_manage").show();
    },function(){
        $(".bottom_manage").hide();
    });
}

/**
 *  获取跳转页面按钮
 */
function getSkip(parentName) {
    var userId = $("#userId").val();
    var parentId = $("#resourceId").val();
    $.ajax({
        type:"post",
        url:"../../../resource/conditionQueryResourceByUserId",
        data: {
            "msUserId": userId,
            "type": 3,
            "isHide":0,
            "parentId":parentId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var bottom_left = '';
                var bottom_right = '';
                var bottom_manage = '';
                var bottom_report = '';
                $.each(res.result,function (index,value) {
                    if (value.level == 1) {
                        bottom_left += '<input id="'+value.sourceUrl+'" type="button" value="'+value.name+'">';
                    } else if (value.level == 2) {
                        if (value.name == "管理" && parentName == "资源管理") {
                        } else {
                            bottom_right += '<input id="'+value.sourceUrl+'" type="button" value="'+value.name+'">';
                        }

                    } else if (value.level == 3) {
                        if (value.parentName == parentName) {
                            bottom_manage += '<input id="'+value.sourceUrl+'" type="button" value="'+value.name+'"><br/>';
                        }
                    } else if (value.level == 4 && value.parentName == parentName) {
                        bottom_report += '<input id="'+value.sourceUrl+'" type="button" value="'+value.name+'"><br/>';
                    }
                });
                $("#bottom-left").html(bottom_left);
                $("#bottom-right").html(bottom_right);
                $(".bottom_manage").html(bottom_manage);
                $(".nowPage-right").html(bottom_report);
                data_hover();
            }
        }
    });
}

/**
 *  获取按钮
 */
function getButton(parentName) {
    var userId = $("#userId").val();
    var parentId = $("#resourceId").val();
    $.ajax({
        type:"post",
        url:"../../../resource/conditionQueryResourceByUserId",
        data: {
            "msUserId": userId,
            "type": 2,
            "isHide":0,
            "parentId":parentId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var bottom_left = '';
                var bottom_right = '';
                var bottom_manage = '';
                var bottom_report = '';
                $.each(res.result,function (index,value) {
                    if (value.level == 1) {
                        bottom_left += '<input id="'+value.sourceUrl+'" type="button" value="'+value.name+'">';
                    } else if (value.level == 2) {
                        if (value.name == "管理" && parentName == "资源管理") {
                        } else {
                            bottom_right += '<input id="'+value.sourceUrl+'" type="button" value="'+value.name+'">';
                        }

                    } else if (value.level == 3) {
                        if (value.parentName == parentName) {
                            bottom_manage += '<input type="hidden" value="'+ value.msResourceId +'"/><input id="'+value.sourceUrl+'" type="button" value="'+value.name+'"><br/>';
                        }
                    } else if (value.level == 4 && value.parentName == parentName) {
                        bottom_report += '<input id="'+value.sourceUrl+'" type="button" value="'+value.name+'"><br/>';
                    }
                });
                $("#bottom-left").html(bottom_left);
                $("#bottom-right").html(bottom_right);
                $(".bottom_manage").html(bottom_manage);
                $(".nowPage-right").html(bottom_report);
                data_hover();
            }
        }
    });
}

//获取第一个选中的复选框的value
function getBoxValue() {
    var box = $("input[type='checkbox'][name='checkedres']");
    var length = box.length;
    for (var i = 0; i < length; i++) {
        if (box[i].checked) {
            alert(box[i].value)
            return box[i].value;
        }
    }
    return "";
}

//判断是否选择一个对象
function getBox() {
    var box = $("input[type='checkbox'][name='checkedres']");
    var flag = 0; //标记判断是否选中一个
    var length = box.length;
    for (var i = 0; i < length; i++) {
        if (box[i].checked) {
            flag += 1;
        }
    }
    return flag;
}


//如果不是全选取消全选框
function selectBox() {
    var box = $("input[type='checkbox'][name='checkedres']");
    var flag = false; //标记判断是否选中一个
    var length = box.length;
    for (var i = 0; i < length; i++) {
        if (!box[i].checked) {
            flag = true ;
            break ;
        }
    }
    if (flag) {
        $("#checkAll").prop("checked", false);
    } else {
        $("#checkAll").prop("checked", true);
    }
}

// 全选复选框
function selectAll() {
    if ($("#checkAll").prop("checked")) {
        $("input[type='checkbox'][name='checkedres']").prop("checked", true); //全选
    } else {
        $("input[type='checkbox'][name='checkedres']").prop("checked", false); //取消全选
    }
}