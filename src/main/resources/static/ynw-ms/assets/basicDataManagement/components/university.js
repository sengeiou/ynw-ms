var num = 1;
var bdUniversityId = "";
var name = "";
var provinceId = '';
var cityId = '';
$(function () {
    //第一页数据
    getData(1);
    //获取按钮
    getButton('高校管理');
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
    //获取省份
    getProvince();
    //获取城市
    // 城市筛选
    $("#province").change(function () {
        getCity('screen','','');
    });
    // 城市添加修改
    $("#add-university-data select[name='province']").change(function () {
        getCity('operation','add',"");
    });
    // 城市编辑修改
    $("#update-university-data select[name='province']").change(function () {
        getCity('operation','update','');
    });
})

/**
 *  未来点击事件
 */
function bottom_click() {

    //添加
    $(document).on('click','#add_date',function(){
        $("#add-university input").val("");
        $("#add-university select").val("");
        $("#add-university-data").show();
    });

    $("#add-university-confirm").click(function () {
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
     *  编辑
     */
    $(document).on('click','#update_date',function(){
        var flag = getBox();
        if (flag == 1) {
            //获取高校编号
            get_role_data();
            //获取编辑框数据
            // getUniversity();
            $("#update-university input[name='name']").val(name);
            $("#update-university select[name='province']").val(provinceId);
            getCity('operation','update','one');
            // alert(1)
            $("#update-university-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    $("#update-university-confirm").click(function () {
        name = $("#update-university input[name='name']").val();
        if (name == "") {
            layer.msg("高校名称不能为空,请重新输入",{icon:2});
            return false;
        }
        var bdCityId = $("#update-university select[name='city']").val();
        if (bdCityId == '') {
            layer.msg("城市不能为空,请重新输入",{icon:2});
            return false;
        }
        $.ajax({
            type:"post",
            url:"../../../university/update",
            data: {
                "bdUniversityId":bdUniversityId,
                "name":name,
                "bdCityId": bdCityId,
                "LogContent":"编辑高校【 高校名称："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#update-university-data").hide();
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
     *  删除
     */
    $(document).on('click','#delete_date',function(){
        var flag = getBox();
        if (flag == 1) {
            get_role_data();
            $(".role-delete-body span").text(name);
            $(".operation-delete-data").show();
        } else {
            layer.msg("请选择一个对象或者只能选一个",{icon:2});
        }
    });

    $(".confirm").click(function () {
        $.ajax({
            type:"post",
            url:"../../../university/delete",
            data: {
                "bdUniversityId":bdUniversityId,
                "LogContent":"删除高校【 高校名称："+ name +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("删除成功",{icon:1});
                    $(".operation-delete-data").hide();
                    var count = getNum();
                    setPage(count);
                    getData(num);
                } else {
                    layer.msg(res.message,{icon:2});
                }
                $("#delete-data").hide();
            }
        });
    });

}

//根据高校编号查询单条数据-------------------------------
// function getUniversity() {
//     $.ajax({
//         type:"post",
//         url:"../../../university/findById",
//         data: {
//             "bdUniversityId": bdUniversityId
//         },
//         // contentType: "application/json",
//         dataType: "json",
//         success: function(res){
//             if (res.code == 1) {
//                 $("#update-university input[name='name']").val(res.result.name);
//                 $("#update-university select[name='province']").val(res.result.province);
//                 getCity('operation','update');
//                 // alert(1);
//                 $("#update-university-data select[name='city']").val(res.result.bdCityId);
//                 // getCity('operation','update');
//                 // alert(res.result.bdCityId);
//                 // cityId = res.result.bdCityId;
//                 // $("#update-university select[name='city']").val(res.result.bdCityId);
//             } else {
//                 layer.msg(res.message,{icon:2});
//             }
//         }
//     });
// }

//获取勾选的数据-------------------------------
function get_role_data() {
    var box = $("input[type='checkbox'][name='checkedres']");
    var length = box.length;
    for (var i = 0; i < length; i++) {
        if (box[i].checked) {
            bdUniversityId = box[i].value;
            name = box[i].parentNode.nextSibling.textContent;
            // provinceName = box[i].parentNode.nextSibling.nextSibling.nextSibling.textContent;
            // cityName = box[i].parentNode.nextSibling.nextSibling.nextSibling.nextSibling.textContent;
            provinceId = box[i].nextSibling.value;
            cityId = box[i].nextSibling.nextSibling.value;
            break;
        }
    }
}

//添加-------------------------------
function add_data() {
    name = $("#add-university-data input[name='name']").val();
    if (name == "") {
        layer.msg("高校名称不能为空,请重新输入",{icon:2});
        return false;
    }
    var bdCityId = $("#add-university-data select[name='city']").val();
    if (bdCityId == '') {
        layer.msg("城市不能为空,请重新输入",{icon:2});
        return false;
    }
    $.ajax({
        type:"post",
        url:"../../../university/insert",
        data: {
            "name":name,
            "bdCityId":bdCityId,
            "LogContent":"添加高校【 高校名称："+ name +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#add-university-data").hide();
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

//获取省份-------------------------------
function getProvince() {
    $.ajax({
        type:"post",
        url:"../../../province/findProvinceAll",
        data: {
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var provinceSelect ='<option value="">--请选择--</option>';
                $.each(res.result,function (index,value) {
                    provinceSelect += '<option value="'+ value.bdProvinceId +'">'+ value.provinceName +'</option>';
                });
                $("select[name='province']").html(provinceSelect);
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取城市-------------------------------
function getCity(select,handle,sort) {
    var province = '';
    if (select == 'screen') {
        province = $("#province").val();
    }
    if (select == 'operation') {
        if (handle == 'add') {
            province = $("#add-university-data select[name='province']").val();
        }
        if (handle == 'update') {
            province = $("#update-university-data select[name='province']").val();
        }
        // $(".role-body select[name='city']").empty();
    }
    // $("#city").empty();
    // $(".role-body select[name='city']").empty();
    $.ajax({
        type:"post",
        url:"../../../city/findCityByProvince",
        data: {
            "bdProvinceId":province
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            var citySelect = '<option value="">--请选择--</option>';
            if (res.code == 1) {
                $.each(res.result,function (index,value) {
                    citySelect += '<option value="'+ value.bdCityId +'">'+ value.name +'</option>';
                });
                if (select == 'screen') {
                    $("#city").html(citySelect);
                } else if (select == 'operation') {
                    $(".role-body select[name='city']").html(citySelect);
                    //解决放外面不能选中指定城市的问题
                    if (sort == 'one') {
                        $("#update-university select[name='city']").val(cityId);
                    }
                }
            } else {
                if (res.code == 100) {
                    $("#city").html('<option value="">--请选择--</option>');
                    $(".operation-data select[name='city']").html('<option value="">--请选择--</option>');
                }
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
    name = $("#name").val();
    var city = $("#city").val();
    var province = $("#province").val();
    $.ajax({
        type:"post",
        url:"../../../university/count",
        data: {
            "name":name,
            "city":city,
            "province":province
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
    name = $("#name").val();
    var city = $("#city").val();
    var province = $("#province").val();
    $.ajax({
        type:"post",
        url:"../../../university/conditionQueryUniversity",
        data: {
            "name":name,
            "city":city,
            "province":province,
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
                        '<td id="td_input"><input type="checkbox" class="checkedBox" name="checkedres" value="'+value.bdUniversityId+'"/>' +
                        '<input type="hidden" value="'+ value.provinceId +'"/><input type="hidden" value="'+ value.bdCityId +'"/></td>';
                    str += '<td>'+value.name+'</td>';
                    str += '<td>'+value.province+'</td>';
                    str += '<td>'+value.city+'</td>';
                    str += '<td>'+date+'</td>';
                 });
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