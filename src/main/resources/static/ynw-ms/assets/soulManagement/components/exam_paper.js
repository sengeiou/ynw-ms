var num = 1;
var typeId = '';
var typeName = '';
var examId = '';
var examTitle = '';
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('题库管理');
    //获取数据总数并分页
    getNum();
    //获取字典
    getDictionary();
    //获取举报类型
    getExamPaperCtgy();
    //未来点击事件
    bottom_click();
    //隐藏操作框
    control_tip();
    $("#checkAll").click(function () {
        selectAll();
    })
    $(document).on("click","input[type='checkbox'][name='checkedres']",function(){
        selectBox();
    });
    //隐藏问卷分类功能框
    control_tip_exam_classify()
});

//隐藏问卷分类功能框
function control_tip_exam_classify() {
    $(".exam-classify-cancel").click(function () {
        $("#delete-exam-classify-data").hide();
        $(".operation-data").hide();
    });
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

    //添加问卷
    $(document).on('click','#add_date',function(){
        $("#add-exam select").val("");
        $("#add-exam textarea").val("");
        $("#add-exam-data").show();
    });

    //添加
    $("#add-exam-confirm").click(function () {
        var title = $("#add-exam textarea[name='title']").val();
        if (title == '') {
            layer.msg('问卷标题不能为空',{icon:2});
            return false;
        }
        var kbExampaperCtgyId = $("#add-exam select[name='kbExampaperCtgyId']").val();
        if (kbExampaperCtgyId == '') {
            layer.msg('问卷分类不能为空',{icon:2});
            return false;
        }
        var type = $("#add-exam select[name='type']").val();
        if (type == '') {
            layer.msg('问卷类型不能为空',{icon:2});
            return false;
        }
        var status = $("#add-exam select[name='status']").val();
        if (status == '') {
            layer.msg('问卷状态不能为空',{icon:2});
            return false;
        }
        // alert(title+":"+kbExampaperCtgyId+":"+type+":"+status+":")
        $.ajax({
            type:"post",
            url:"../../../examPaper/insert",
            data: {
                "title":title,
                "kbExampaperCtgyId":kbExampaperCtgyId,
                "type":type,
                "status":status,
                "LogContent":"添加问卷【 问卷标题："+ title +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#add-exam-data").hide();
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    layer.msg("添加成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //编辑问卷
    $(document).on('click','#update_date',function(){
        var flag = getBox();
        if (flag == 1) {
            getExamId();
            // alert(name + ":" + roleKey+":"+msRoleId+":");
            //赋值
            getExamOne();
            $("#update-exam-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    //编辑
    $("#update-exam-confirm").click(function () {
        var title = $("#update-exam-data textarea[name='title']").val();
        if (title == '') {
            layer.msg('问卷标题不能为空',{icon:2});
            return false;
        }
        var kbExampaperCtgyId = $("#update-exam-data select[name='kbExampaperCtgyId']").val();
        if (kbExampaperCtgyId == '') {
            layer.msg('问卷分类不能为空',{icon:2});
            return false;
        }
        var type = $("#update-exam-data select[name='type']").val();
        if (type == '') {
            layer.msg('问卷类型不能为空',{icon:2});
            return false;
        }
        var status = $("#update-exam-data select[name='status']").val();
        if (status == '') {
            layer.msg('问卷状态不能为空',{icon:2});
            return false;
        }
        // alert(key +":"+value +":"+desc +":"+syParaConfId);
        $.ajax({
            type:"post",
            url:"../../../examPaper/update",
            data: {
                "title":title,
                "kbExampaperCtgyId":kbExampaperCtgyId,
                "type":type,
                "status":status,
                "kbExampaperId":examId,
                "LogContent":"编辑问卷【 问卷标题："+ title +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#update-exam-data").hide();
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    layer.msg("修改成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //删除问卷
    $(document).on('click','#delete_date',function() {
        var flag = getBox();
        if (flag == 1) {
            getExamId();
            $("#delete-exam span").text(examTitle);
            $("#delete-exam-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    //删除
    $("#delete-exam-confirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../examPaper/delete",
            data: {
                "kbExampaperId":examId,
                "LogContent":"删除问卷【 问卷标题："+ examTitle +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("删除成功",{icon:1});
                    $("#delete-exam-data").hide();
                    var count = getNum();
                    setPage(count);
                    getData(num);
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //问卷分类显示
    $(document).on('click','#exam_classify',function(){
        $("#nowPage-date").show();
    });

    //点击隐藏
    $(document).on('click','#drop_out',function(){
        $("#nowPage-date").hide();
    });

    //添加问卷分类
    $(document).on('click','#add_classify_date',function(){
        $("#add-exam-classify input").val("");
        $("#add-exam-classify-data").show();
    });

    //添加
    $("#add-exam-classify-confirm").click(function () {
        var name = $(".role-body input[name='name']").val();
        if (name == '') {
            layer.msg('问卷分类名称不能为空',{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../examPaperCtgy/insert",
            data: {
                "name":name,
                "LogContent":"添加问卷分类【 问卷分类名称："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#add-exam-classify-data").hide();
                    //更新
                    getExamPaperCtgy();
                    layer.msg("添加成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //编辑问卷分类
    $(document).on('click','#update_classify_date',function(){
        //获取选择的问卷分类信息
        get_exam_typr_data();
        // alert(typeId+":"+typeName)
        $("#update-exam-classify input[name='name']").val(typeName);
        $("#update-exam-classify-data").show();
    });

    //编辑
    $("#update-exam-classify-confirm").click(function () {
        var name = $("#update-exam-classify input[name='name']").val();
        if (name == '') {
            layer.msg('举报问卷名称不能为空',{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../examPaperCtgy/update",
            data: {
                "name":name,
                "kbExampaperCtgyId":typeId,
                "LogContent":"编辑问卷分类【 问卷分类名称："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#update-exam-classify-data").hide();
                    getExamPaperCtgy();
                    layer.msg("编辑成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //删除问卷分类
    $(document).on('click','#delete_classify_date',function(){
        //获取选择的举报分类信息
        get_exam_typr_data();
        $("#delete-exam-classify span").text(typeName);
        $("#delete-exam-classify-data").show();
    });

    //删除
    $("#exam-classify-confirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../examPaperCtgy/delete",
            data: {
                "kbExampaperCtgyId":typeId,
                "LogContent":"删除问卷分类【 问卷分类名称："+ typeName +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#delete-exam-classify-data").hide();
                    getExamPaperCtgy();
                    layer.msg("删除成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //上移
    $(document).on('click','#move_up',function(){
        //获取选择的举报分类信息
        get_exam_typr_data();
        // alert(typeId +":" +typeName)
        $.ajax({
            type:"post",
            url:"../../../examPaperCtgy/moveUp",
            data: {
                "kbExampaperCtgyId":typeId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("移动成功",{icon:1});
                    getExamPaperCtgy();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //下移
    $(document).on('click','#move_down',function(){
        //获取选择的举报分类信息
        get_exam_typr_data();
        $.ajax({
            type:"post",
            url:"../../../examPaperCtgy/moveDown",
            data: {
                "kbExampaperCtgyId":typeId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("移动成功",{icon:1});
                    getExamPaperCtgy();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //修改状态
    $(document).on('click','#updateStatus',function(){
        var examPaperId = $(this).prevAll('input').val();
        $.ajax({
            type:"post",
            url:"../../../examPaper/updateStatus",
            data: {
                "kbExampaperId":examPaperId,
                "LogContent":"修改问卷状态【 问卷编号："+ examPaperId +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    layer.msg('修改成功',{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

}

//获取单个问卷数据
function getExamOne() {
    $.ajax({
        type:"post",
        url:"../../../examPaper/findById",
        data: {
            "kbExampaperId":examId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var value = res.result;
                $("#update-exam textarea[name='title']").val(value.title);
                $("#update-exam select[name='kbExampaperCtgyId']").val(value.kbExampaperCtgyId);
                $("#update-exam select[name='type']").val(value.type);
                $("#update-exam select[name='status']").val(value.status);
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取选中的问卷编号
function getExamId() {
    var box = $("input[type='checkbox'][name='checkedres']");
    var length = box.length;
    for (var i = 0; i < length; i++) {
        if (box[i].checked) {
            examId = box[i].value;
            examTitle = box[i].parentNode.nextSibling.textContent;
        }
    }
}

//获取问卷分类选中文本和值-------------------------------
function get_exam_typr_data() {
    typeId = $(".nowPage-left select").val();
    typeName = $(".nowPage-left select").find("option:selected").text();
}

//获取问卷类别-------------------------------
function getExamPaperCtgy() {
    $.ajax({
        type:"post",
        url:"../../../examPaperCtgy/findExamPaperCtgyAll",
        data: {
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                var str = '<select style="width: 110%" size="'+ res.result.length +'">';
                $.each(res.result,function (index,value) {
                    src += '<option value="'+ value.kbExampaperCtgyId +'">'+ value.name +'</option>';
                    str += '<option value="'+ value.kbExampaperCtgyId +'">'+ value.name +'</option>';
                });
                str += '</select>';
                $("#kbExampaperCtgyId").html(src);
                $(".exam-body select[name='kbExampaperCtgyId']").html(src);
                $(".nowPage-left").html(str);
                $('.nowPage-left select option:first').prop('selected', 'selected');
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取字典-------------------------------
function getDictionary() {
    $.ajax({
        type:"post",
        url:"../../../dictionary/findDictionaryByGroupKey",
        data: {
            "groupKey":'EXAMPAPER_TYPE'
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                $.each(res.result,function (index,value) {
                    src += '<option value="'+ value.itemKey +'">'+ value.itemValue +'</option>';
                });
                $("#type").html(src);
                $(".exam-body select[name='type']").html(src);
            } else {
                layer.msg(res.message,{icon:2});
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
    var title = $("#title").val();
    var kbExampaperCtgyId = $("#kbExampaperCtgyId").val();
    var type = $("#type").val();
    $.ajax({
        type:"post",
        url:"../../../examPaper/count",
        data: {
            "title":title,
            "kbExampaperCtgyId":kbExampaperCtgyId,
            "type":type
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
    var kbExampaperCtgyId = $("#kbExampaperCtgyId").val();
    var type = $("#type").val();
    // alert(msUserId + ":"+beginDate + ":"+endDate + ":")
    $.ajax({
        type:"post",
        url:"../../../examPaper/conditionQueryExamPaper",
        data: {
            "title":title,
            "kbExampaperCtgyId":kbExampaperCtgyId,
            "type":type,
            "nowPage":nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                var URL = $("#URL").val();
                var resourceId = $("#resourceId").val();
                $.each(res.result,function (index,value) {
                    var date = getDate(value.createTime);
                    if (value.status == 1) {
                        value.status = '有效';
                    } else {
                        value.status = '无效';
                    }
                    str += '<tr class="gradeX"><td id="td_input"><input type="checkbox" name="checkedres" value="'+value.kbExampaperId+'"></td>';
                    str += '<td>'+value.title+'</td>';
                    str += '<td>'+value.examPaperCtgyName+'</td>';
                    str += '<td>'+value.typeName+'</td>';
                    str += '<td>'+date+'</td>';
                    str += '<td>'+value.examNum+'</td>';
                    str += '<td>'+value.status+'</td>';
                    str += '<td><input type="hidden" value="'+ value.kbExampaperId +'"/>' +
                        '<a href="edit_the_title_management?type='+ value.type +'&kbExamPaperId='+ value.kbExampaperId +'&URL='+ URL +'&msResourceId='+ resourceId +'" ' +
                        'style="margin-left: 10px">编辑题目</a><a id="updateStatus" style="margin-left: 10px" ' +
                        'href="javascript:;">设置状态</a></td></tr>';
                })
                $("#report").html(str);
                //选中状态发生改变是隐藏功能弹窗
                control_tip_box();
            } else if (res.code == 8) {
                $("#report").empty();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}