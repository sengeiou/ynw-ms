var num = 1;
var day = 1;
var typeId = '';
var typeName = '';
$(function () {
    //返回
    getBack();
    //第一页数据
    getData(1);
    //获取按钮
    getSkip('一周情侣活动');
    //获取数据总数并分页
    getNum();
    //未来点击事件
    bottom_click();
    //获取活动任务
    getActiveTask();
    //隐藏操作框
    control_tip();
    //隐藏举报分类操作弹窗
    control_tip_report();
});

//隐藏举报分类操作弹窗
function control_tip_report() {
    $(".report-cancel").click(function () {
        $(".unified-closed").hide();
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
        getData(num)
    });

    /**
     *  显示用户标签分类管理
     */
    $(document).on('click','#task_management',function(){
        $("#nowPage-date").show();
    });

    /**
     * 隐藏用户标签分类管理
     */
    $(document).on('click','#drop_out',function(){
        $("#nowPage-date").hide();
        $(".unified-closed").hide();
    });

    //添加用户标签分类
    $(document).on('click','#add_active_task',function(){
        $("#add-label-classify input").val("");
        $("#add-label-classify textarea").val("");
        $("#add-label-classify-data").show();
    });

    //添加
    $("#add-label-classify-confirm").click(function () {
        var day = $("#add-label-classify-data input[name='day']").val();
        if (day == '') {
            layer.msg('活动任务第几天不能为空',{icon:2});
            return false;
        }
        var taskDes = $("#add-label-classify-data textarea[name='taskDes']").val();
        if (taskDes == '') {
            layer.msg('活动任务描述不能为空',{icon:2});
            return false;
        }
        var atRegisterId = $("#atRegisterId").val();
        $.ajax({
            type:"post",
            url:"../../../activeTaskClock/insert",
            data: {
                "day":day,
                "taskDes":taskDes,
                "atRegisterId":atRegisterId,
                "LogContent":"添加活动任务【 活动任务描述："+ taskDes +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#add-label-classify-data").hide();
                    getActiveTask();
                    layer.msg("添加成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //编辑用户标签分类
    $(document).on('click','#update_active_task',function(){
        //获取选择的用户标签分类信息
        get_report_typr_data();
        // alert(typeId+":"+typeName)
        getLabelClassifySelectOne("update");
    });

    $("#update-label-classify-confirm").click(function () {
        var taskDes = $("#update-label-classify-data textarea[name='taskDes']").val();
        if (taskDes == '') {
            layer.msg('活动任务描述不能为空',{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../activeTaskClock/update",
            data: {
                "taskDes":taskDes,
                "atWkcpTaskId":typeId,
                "LogContent":"编辑第"+ day +"天活动任务【 第"+ day +"天活动任务描述："+ taskDes +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#update-label-classify-data").hide();
                    getActiveTask();
                    layer.msg("编辑成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //删除
    $(document).on('click','#delete_active_task',function(){
        //获取选择的用户标签分类信息
        get_report_typr_data();
        getLabelClassifySelectOne("delete");
        $("#delete-label-classify-data").show();
    });

    //删除
    $("#delete-label-classify-confirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../activeTaskClock/delete",
            data: {
                "atWkcpTaskId":typeId,
                "LogContent":"删除活动任务【 活动任务描述："+ typeName +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#delete-label-classify-data").hide();
                    getActiveTask();
                    layer.msg("删除成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    $(document).on("click","#taskToPush", function () {
       var atRegisterId = $("#atRegisterId").val();
        $.ajax({
            type:"post",
            url:"../../../activeTaskClock/taskToPush",
            data: {
                "atRegisterId":atRegisterId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

}

//获取标签分类单条数据
function getLabelClassifySelectOne(type) {
    $.ajax({
        type:"post",
        url:"../../../activeTaskClock/findById",
        data: {
            "atWkcpTaskId":typeId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                day = res.result.day;
                if (type == "update") {
                    $("#update-label-classify-data .data-top").text("编辑第"+ day +"天活动任务");
                    $("#update-label-classify-data textarea[name='taskDes']").val(res.result.taskDes);
                    $("#update-label-classify-data").show();
                } else if (type == "delete") {
                    $("#delete-label-classify-data span").text(day);
                }
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取活动任务选中文本和值-------------------------------
function get_report_typr_data() {
    typeId = $(".nowPage-left select").val();
    typeName = $(".nowPage-left select").find("option:selected").text();
}

//获取活动任务-------------------------------
function getActiveTask() {
    var atRegisterId = $("#atRegisterId").val();
    $.ajax({
        type:"post",
        url:"../../../activeTaskClock/findAllByActivityId",
        data: {
            "registerId":atRegisterId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '<select style="width: 110%" size="'+ res.result.length +'">';
                $.each(res.result,function (index,value) {
                    str += '<option value="'+ value.atWkcpTaskId +'">'+ value.taskDes +'</option>';
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
    var name = $("#name").val();
    var matchNo = $("#matchNo").val();
    var missionDay = $("#missionDay").val();
    var status = $("#status").val();
    var atRegisterId = $("#atRegisterId").val();
    $.ajax({
        type:"post",
        url:"../../../activeTaskClock/count",
        data: {
            "reqName":name,
            "matchNo":matchNo,
            "missionDay":missionDay,
            "status":status,
            "atRegisterId":atRegisterId
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
    var name = $("#name").val();
    var matchNo = $("#matchNo").val();
    var missionDay = $("#missionDay").val();
    var status = $("#status").val();
    var atRegisterId = $("#atRegisterId").val();
    $.ajax({
        type:"post",
        url:"../../../activeTaskClock/conditionQueryActiveTaskClockRegister",
        data: {
            "reqName":name,
            "matchNo":matchNo,
            "missionDay":missionDay,
            "status":status,
            "atRegisterId":atRegisterId,
            "nowPage":nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                $.each(res.result,function (index,value) {
                    var date = getDate(value.createTime);
                    if (value.status == 1) {
                        value.status = '打卡成功';
                    } else if (value.status == -1) {
                        value.status = 'cp不同意';
                    } else  if (value.status == 0) {
                        value.status = '等待cp同意中';
                    }
                    str += '<tr class="gradeX">';
                    str += '<td>'+value.reqName+ '&' + value.resName +'</td>';
                    str += '<td>'+value.matchNo+'</td>';
                    str += '<td>'+value.missionName+'</td>';
                    str += '<td>第'+value.missionDay+'天</td>';
                    str += '<td>'+value.status+'</td>';
                    str += '<td>'+date+'</td></tr>';
                })
                $("#report").html(str);
                //选中状态发生改变是隐藏功能弹窗
                // control_tip_box();
            } else if (res.code == 8) {
                $("#report").empty();
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
        var resourceId = $("#resourceId").val();
        $(location).attr("href",'activity_management?msResourceId='+resourceId);
    });
}