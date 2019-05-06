var num = 1;
var rsSubjectId = "";
var content = "";
var rsSubjectItemId = "";
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('雷达搜题库管理');
    //获取数据总数并分页
    getNum();
    //未来点击事件
    bottom_click();
    //获取题目类型
    getSubjectType();
    //获取系统用户信息
    getSystemUser();
    //隐藏操作框
    control_tip();
    //隐藏题目选项弹窗
    control_tip_report();
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
        getData(num);
    });

    //添加雷达题目
    $(document).on('click', '#add_date', function () {
        $("#add-radar-subject .unified-closed-body input").val("");
        $("#add-radar-subject textarea").val("");
        $("#add-radar-subject").show();
    });
    
    //添加
    $("#add-radar-subject-confirm").click(function () {
        var type = $("#add-radar-subject select[name='type']").val();
        if (type == "") {
            layer.msg('题目类型不能为空',{icon:2});
            return false;
        }
        var fromType = $("#add-radar-subject select[name='fromType']").val();
        if (fromType == "") {
            layer.msg('题目出处不能为空',{icon:2});
            return false;
        }
        var acUserId = $("#add-radar-subject select[name='acUserId']").val();
        if (acUserId == "") {
            layer.msg('出题人不能为空',{icon:2});
            return false;
        }
        var applied = $("#add-radar-subject select[name='applied']").val();
        if (applied == "") {
            layer.msg('适用范围不能为空',{icon:2});
            return false;
        }
        content = $("#add-radar-subject textarea").val();
        if (content == "") {
            layer.msg('题目类型不能为空',{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../radarSubject/insert",
            data: {
                "type":type,
                "fromType":fromType,
                "acUserId":acUserId,
                "applied":applied,
                "content":content,
                "LogContent":"添加雷达题目【 雷达题目："+ content +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    $("#add-radar-subject").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //更新
    $(document).on('click', '#update_date', function () {
        var flag = getBox();
        if (flag == 1) {
            //获取选择的信息
            getChecked();
            //根据信息显示修改原数据
            getSubjectOne();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    //更新
    $("#update-radar-subject-confirm").click(function () {
        var type = $("#update-radar-subject select[name='type']").val();
        if (type == "") {
            layer.msg('题目类型不能为空',{icon:2});
            return false;
        }
        var fromType = $("#update-radar-subject select[name='fromType']").val();
        if (fromType == "") {
            layer.msg('题目出处不能为空',{icon:2});
            return false;
        }
        var acUserId = $("#update-radar-subject select[name='acUserId']").val();
        if (acUserId == "") {
            layer.msg('出题人不能为空',{icon:2});
            return false;
        }
        var applied = $("#update-radar-subject select[name='applied']").val();
        if (applied == "") {
            layer.msg('适用范围不能为空',{icon:2});
            return false;
        }
        content = $("#update-radar-subject textarea").val();
        if (content == "") {
            layer.msg('题目类型不能为空',{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../radarSubject/update",
            data: {
                "type":type,
                "fromType":fromType,
                "acUserId":acUserId,
                "applied":applied,
                "content":content,
                "rsSubjectId":rsSubjectId,
                "LogContent":"修改雷达题目【 雷达题目："+ content +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    $("#update-radar-subject").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    // 删除
    $(document).on('click','#delete_date',function () {
       getChecked();
        $("#delete-subject span").text(content);
        $("#delete-subject").show();
    });

    $("#delete-subject-confirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../radarSubject/delete",
            data: {
                "rsSubjectId":rsSubjectId,
                "LogContent":"删除雷达题目【 雷达题目："+ content +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    $("#delete-subject").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //雷达搜题目选项管理
    $(document).on('click',"#subject_item", function () {
        var flag = getBox();
        if (flag == 1) {
            //获取选择的信息
            getChecked();
            //根据信息获取题目选项
            getSubjectItem();
            $("#nowPage-date").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }

    });


    /**
     * 隐藏雷达搜题目选项管理
     */
    $(document).on('click','#drop_out',function(){
        $("#nowPage-date").hide();
    });

    //添加题目选项
    $(document).on('click', '#add_subject_item', function () {
        $("#add-subject-item .role-body input").val("");
        $("#add-subject-item").show();
    });

    $("#add-subject-item-confirm").click(function () {
        content = $("#add-subject-item input[name='content']").val();
        $.ajax({
            type:"post",
            url:"../../../radarSubjectItem/insert",
            data: {
                "content":content,
                "rsSubjectId":rsSubjectId,
                "LogContent":"添加雷达题目选项【 雷达题目选项："+ content +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    getSubjectItem();
                    $("#add-subject-item").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //更新题目选项
    $(document).on('click', '#update_subject_item', function () {
        get_checked_data();
        $("#update-subject-item .role-body input").val(content);
        $("#update-subject-item").show();
    });

    $("#update-subject-item-confirm").click(function () {
        content = $("#update-subject-item input[name='content']").val();
        $.ajax({
            type:"post",
            url:"../../../radarSubjectItem/update",
            data: {
                "content":content,
                "rsSubjectItemId":rsSubjectItemId,
                "LogContent":"编辑雷达题目选项【 雷达题目选项："+ content +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    getSubjectItem();
                    $("#update-subject-item").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    // 删除
    $(document).on('click','#delete_subject_item',function () {
        get_checked_data();
        $("#delete-subject-item span").text(content);
        $("#delete-subject-item").show();
    });

    $("#delete-subject-item-confirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../radarSubjectItem/delete",
            data: {
                "rsSubjectItemId":rsSubjectItemId,
                "LogContent":"删除雷达题目选项【 雷达题目选项："+ content +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    getSubjectItem();
                    $("#delete-subject-item").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

}

//获取选中文本和值-------------------------------
function get_checked_data() {
    rsSubjectItemId = $(".nowPage-left select").val();
    content = $(".nowPage-left select").find("option:selected").text();
}

//隐藏雷达搜题目选项弹窗
function control_tip_report() {
    $(".report-cancel").click(function () {
        $(".operation-data").hide();
        $(".general-delete").hide();
    });
}


//获取雷达搜题目选项-------------------------------
function getSubjectItem() {
    $.ajax({
        type:"post",
        url:"../../../radarSubjectItem/findAllBySubjectId",
        data: {
            "subjectId":rsSubjectId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '<select style="width: 110%" size="'+ res.result.length +'">';
                $.each(res.result,function (index,value) {
                    str += '<option value="'+ value.rsSubjectItemId +'">'+ value.content +'</option>';
                });
                str += '</select>';
                $(".nowPage-left").html(str);
                $('.nowPage-left select option:first').prop('selected', 'selected');
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取选中的用户标签
function getChecked() {
    var box = $("input[type='checkbox'][name='checkedres']");
    var length = box.length;
    for (var i = 0; i < length; i++) {
        if (box[i].checked) {
            rsSubjectId = box[i].value;
            content = box[i].parentNode.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.textContent;
            // category = box[i].parentNode.nextSibling.nextSibling.nextSibling.textContent;
            break;
        }
    }
}

//获取题目类型
function getSubjectOne() {
    $.ajax({
        type:"post",
        url:"../../../radarSubject/selectOne",
        data: {
            "rsSubjectId":rsSubjectId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#update-radar-subject select[name='type']").val(res.result.type);
                $("#update-radar-subject select[name='fromType']").val(res.result.fromType);
                $("#update-radar-subject select[name='acUserId']").val(res.result.acUserId);
                $("#update-radar-subject select[name='applied']").val(res.result.applied);
                $("#update-radar-subject textarea").val(res.result.content);
                $("#update-radar-subject").show();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取系统用户信息
function getSystemUser() {
    $.ajax({
        type:"post",
        url:"../../../acUser/findUserIdAndUserNameByNo",
        data: {
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                $.each(res.result, function (index,value) {
                    src += '<option value="'+ value.acUserId +'">--'+ value.nickname +'--</option>';
                });
                $("select[name='acUserId']").html(src);
            }
        }
    });
}

//获取题目类型
function getSubjectType() {
    $.ajax({
        type:"post",
        url:"../../../dictionary/findDictionaryByGroupKey",
        data: {
            "groupKey":"SUBJECT_TYPE"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                $.each(res.result, function (index,value) {
                   src += '<option value="'+ value.itemKey +'">--'+ value.itemValue +'--</option>';
                });
                $("#type,select[name='type']").html(src);
            }
        }
    });
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
    var fromType = $("#fromType").val();
    var applied = $("#applied").val();
    $.ajax({
        type:"post",
        url:"../../../radarSubject/count",
        data: {
            "userName":userName,
            "type":type,
            "applied":applied,
            "fromType":fromType
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
    var fromType = $("#fromType").val();
    var applied = $("#applied").val();
    // alert(msUserId + ":"+beginDate + ":"+endDate + ":")
    $.ajax({
        type:"post",
        url:"../../../radarSubject/conditionQueryRadarSubject",
        data: {
            "userName":userName,
            "type":type,
            "applied":applied,
            "fromType":fromType,
            "nowPage":nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                $.each(res.result,function (index,value) {
                    var date = getDate(value.createTime);
                    if (value.fromType == 0) {
                        value.fromType = "系统";
                    } else if (value.applied == 1) {
                        value.fromType = "用户自定义";
                    }
                    if (value.applied == 0) {
                        value.applied = "适用男生回答";
                    } else if (value.applied == 1) {
                        value.applied = "适用女生回答";
                    } else if (value.applied == 2) {
                        value.applied = "男女生都适用";
                    }
                    str += '<tr class="gradeX"><td id="td_input"><input type="checkbox" name="checkedres" value="'+value.rsSubjectId+'"></td>';
                    str += '<td>'+value.typeName+'</td>';
                    str += '<td>'+value.fromType+'</td>';
                    str += '<td>'+value.userName+'</td>';
                    str += '<td>'+value.applied+'</td>';
                    str += '<td>'+value.content+'</td>';
                    str += '<td>'+value.sort+'</td>';
                    str += '<td>'+date+'</td></tr>';
                    // str += '<td><a href="test_details_management?kbExampaperTestId='+ value.kbExampaperTestId +'&URL='+ URL +'&msResourceId='+ resourceId +'" style="margin-left: 10px">详情</a></td></tr>';
                })
                $("#report").html(str);
            } else if (res.code == 8) {
                $("#report").empty();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}