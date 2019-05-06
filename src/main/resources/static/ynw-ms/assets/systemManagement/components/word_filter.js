var num = 1;
var syWordfilterId = "";
var word = "";
var category = "";
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('敏感词管理');
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
    //获取字典
    getDictionary();
})

/**
 *  未来点击事件
 */
function bottom_click() {

    //添加弹窗
    $(document).on('click','#add_date',function(){
        $("#add-filter input").val("");
        $("#add-filter select").val("");
        $("#add-filter-data").show();
    });

    //添加
    $("#add-filter-confirm").click(function () {
        add_data();
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

    /**
     *  编辑弹窗
     */
    $(document).on('click','#update_date',function(){
        var flag = getBox();
        if (flag == 1) {
            get_role_data();
            // alert(word + ":" + category+":");
            $("#update-filter-data input[name='word']").val(word);
            $("#update-filter-data select[name='category']").val(category);
            $("#update-filter-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    //编辑
    $("#update-filter-confirm").click(function () {
        word = $("#update-filter-data input[name='word']").val();
        if (word == "") {
            layer.msg("敏感词不能为空,请重新输入",{icon:2});
            return false;
        }
        category = $("#update-filter-data select[name='category']").val();
        if (category == "") {
            layer.msg("字典key不能为空,请重新输入",{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../wordFilter/update",
            data: {
                "syWordfilterId":syWordfilterId,
                "word":word,
                "category":category,
                "LogContent":"编辑敏感词【 敏感词名称："+ word +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#update-filter-data").hide();
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


    /**
     *  删除弹窗
     */
    $(document).on('click','#delete_date',function(){
        var flag = getBox();
        if (flag == 1) {
            get_role_data();
            $(".role-delete-body span").text(word);
            $(".operation-delete-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    //删除
    $(".confirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../wordFilter/delete",
            data: {
                "syWordfilterId":syWordfilterId,
                "LogContent":"删除敏感词【 敏感词名称："+ word +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $(".operation-delete-data").hide();
                    var count = getNum();
                    setPage(count);
                    getData(num);
                    layer.msg("删除成功",{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    })

}

//获取勾选的数据-------------------------------
function get_role_data() {
    var box = $("input[type='checkbox'][name='checkedres']");
    var length = box.length;
    for (var i = 0; i < length; i++) {
        if (box[i].checked) {
            syWordfilterId = box[i].value;
            word = box[i].parentNode.nextSibling.textContent;
            category = box[i].parentNode.nextSibling.nextSibling.nextSibling.textContent;
            break;
        }
    }
}

//获取字典-------------------------------
function getDictionary() {
    $.ajax({
        type:"post",
        url:"../../../dictionary/findDictionaryByGroupKey",
        data: {
            "groupKey":'SENSITIVE_WORD_TYPE'

        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<option value="">--请选择--</option>';
                $.each(res.result,function (index,value) {
                   src += '<option value="'+ value.itemKey +'">'+ value.itemValue +'</option>';
                });
                $("#category").html(src);
                $(".role-body select[name='category']").html(src);
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//添加-------------------------------
function add_data() {
    word = $("#add-filter-data input[name='word']").val();
    if (word == "") {
        layer.msg("敏感词不能为空,请重新输入",{icon:2});
        return false;
    }
    category = $("#add-filter-data select[name='category']").val();
    if (category == "") {
        layer.msg("字典key不能为空,请重新输入",{icon:2});
        return false;
    }
    $.ajax({
        type:"post",
        url:"../../../wordFilter/insert",
        data: {
            "word":word,
            "category":category,
            "LogContent":"添加敏感词【 敏感词名称："+ word +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#add-filter-data").hide();
                var count = getNum();
                setPage(count);
                getData(num);
                layer.msg("添加成功",{icon:1});
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
    word = $("#word").val();
    category = $("#category").val();
    $.ajax({
        type:"post",
        url:"../../../wordFilter/count",
        data: {
            "word":word,
            "category":category
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
    word = $("#word").val();
    category = $("#category").val();
    $.ajax({
        type:"post",
        url:"../../../wordFilter/conditionQueryWordFilter",
        data: {
            "word":word,
            "category":category,
            "nowPage":nowPage
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '';
                $.each(res.result,function (index,value) {
                    var date = getDate(value.createTime);
                    var status = "";
                    if (value.status == 1) {
                        status = "有效";
                    } else {
                        status = "无效";
                    }
                    str += '<tr class="gradeX">' +
                        '<td id="td_input"><input type="checkbox" name="checkedres" value="'+value.syWordfilterId+'"></td>';
                    str += '<td>'+value.word+'</td>';
                    str += '<td>'+value.categoryName+'</td>';
                    str += '<td>'+value.category+'</td>';
                    str += '<td>'+status+'</td>';
                    str += '<td>'+date+'</td></tr>';
                })
                $("#role").html(str);
                //选中状态发生改变是隐藏功能弹窗
                control_tip_box();
            } else if (res.code == 8) {
                $("#role").empty();
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}