var num = 1;
var topicId = '';
var topicName = '';
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('话题信息管理');
    //获取数据总数并分页
    getNum();
    //未来点击事件
    bottom_click();
    //隐藏话题标签操作弹窗
    control_tip_report();
});

//隐藏话题标签操作弹窗
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
        getData(num);
    });

    /**
     *  话题标签
     */
    $(document).on('click','#mood_label',function(){
        getTopic();
        $("#nowPage-date").show();
    });

    /**
     *  退出话题标签管理
     */
    $(document).on('click','#drop_out',function(){
        $("#nowPage-date").hide();
    });

    //添加话题标签
    $(document).on('click','#add_topic_date',function(){
        $("#add-report input").val("");
        $("#add-report-classify-data").show();
    });

    //添加
    $("#add-report-confirm").click(function () {
        var name = $("#add-report-classify-data input[name='name']").val();
        if (name == '') {
            layer.msg('话题标签名称不能为空',{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../topic/insert",
            data: {
                "name":name,
                "LogContent":"添加话题标签【 话题标签名称："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#add-report-classify-data").hide();
                    getTopic();
                    layer.msg("添加成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //编辑话题标签
    $(document).on('click','#update_topic_date',function(){
        //获取选择的话题标签信息
        get_topic_data();
        $("#update-report-classify-data input[name='name']").val(topicName);
        $("#update-report-classify-data").show();
    });

    $("#update-report-confirm").click(function () {
        var name = $("#update-report-classify-data input[name='name']").val();
        if (name == '') {
            layer.msg('话题标签名称不能为空',{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../topic/delete",
            data: {
                "name":name,
                "dsLabelId":topicId,
                "LogContent":"编辑话题标签【 话题标签名称："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#update-report-classify-data").hide();
                    getTopic();
                    layer.msg("编辑成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //删除
    $(document).on('click','#delete_topic_date',function(){
        //获取选择的话题标签信息
        get_topic_data();
        $(".update-body span").text(topicName);
        $("#delete-data").show();
    });

    //删除
    $(".confirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../topic/delete",
            data: {
                "dsLabelId":topicId,
                "isHidden":1,
                "LogContent":"删除话题标签【 话题标签名称："+ topicName +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#delete-data").hide();
                    getTopic();
                    layer.msg("删除成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //上移
    $(document).on('click','#move_up',function(){
        //获取选择的话题标签信息
        get_topic_data();
        $.ajax({
            type:"post",
            url:"../../../topic/moveUp",
            data: {
                "dsLabelId":topicId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("移动成功",{icon:1});
                    getTopic();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //下移
    $(document).on('click','#move_down',function(){
        //获取选择的话题标签信息
        get_topic_data();
        $.ajax({
            type:"post",
            url:"../../../topic/moveDown",
            data: {
                "dsLabelId":topicId
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("移动成功",{icon:1});
                    getTopic();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

}

//获取话题选中文本和值-------------------------------
function get_topic_data() {
    topicId = $(".nowPage-left select").val();
    topicName = $(".nowPage-left select").find("option:selected").text();
}

//获取举报类别-------------------------------
function getTopic() {
    $.ajax({
        type:"post",
        url:"../../../topic/findAll",
        data: {
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '<select style="width: 110%" size="'+ res.result.length +'">';
                $.each(res.result,function (index,value) {
                    str += '<option value="'+ value.dsLabelId +'">'+ value.name +'</option>';
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
    var userPhone = $("#userPhone").val();
    var label = $("#label").val();
    $.ajax({
        type:"post",
        url:"../../../mood/count",
        data: {
            "userPhone":userPhone,
            "label":label
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
    var userPhone = $("#userPhone").val();
    var label = $("#label").val();
    // alert(msUserId + ":"+beginDate + ":"+endDate + ":")
    $.ajax({
        type:"post",
        url:"../../../mood/conditionQueryMood",
        data: {
            "userPhone":userPhone,
            "label":label,
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
                    var length = value.labelList.length;
                    var moodLabel = '';
                    $.each(value.labelList,function (indexs,labels) {
                        moodLabel += labels;
                        if (indexs != length - 1) {
                            moodLabel += '&';
                        }
                    });
                    str += '<tr class="gradeX">';
                    str += '<td><img src="'+ value.userImage +'" style="width: 60px;height: 40px"></td>';
                    str += '<td>'+value.userName+'</td>';
                    str += '<td>'+value.userPhone+'</td>';
                    str += '<td>'+moodLabel+'</td>';
                    str += '<td>'+value.commentNum+'</td>';
                    str += '<td>'+value.likeNum+'</td>';
                    str += '<td>'+value.transPondNum+'</td>';
                    str += '<td>'+value.lookNum+'</td>';
                    str += '<td>'+date+'</td>';
                    str += '<td><a href="mood_details_management?moodId='+ value.dsMoodId +'' +
                        '&URL='+ URL +'&msResourceId='+ resourceId +'" style="margin-left: 10px">详情</a></td></tr>';
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