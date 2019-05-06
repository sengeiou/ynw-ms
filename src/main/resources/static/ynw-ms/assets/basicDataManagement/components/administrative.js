var province = '';
var provinceName = '';
var city = '';
var cityName = '';
var district = '';
var districtName = '';
$(function () {
    //获取所有省份
    getProvince();
    //筛选级别默认选择第一个
    $(".rank:first").prop("checked",true);
    //获取按钮
    getButtons();
    //未来点击事件
    bottom_click();
    //隐藏操作框
    control_tip();
});

/**
 *  未来点击事件
 */
function bottom_click() {
    //省份点击事件-------------------------------
    //添加
    $(document).on('click','.province_button #add_date',function(){
        $("#add-province input").val("");
        $("#add-province-data").show();
    });

    $("#add-province-confirm").click(function () {
        add_province();
    });

    //编辑
    $(document).on('click','.province_button #update_date',function(){
        //获取省份选中值
        get_province_data();
        // alert(province+":" +provinceName);
        $("#update-province-data input[name='name']").val(provinceName);
        $("#update-province-data").show();
    });

    $("#update-province-confirm").click(function () {
        update_province();
    });

    //删除
    $(document).on('click','.province_button #delete_date',function(){
        //获取省份选中值
        get_province_data();
        $("#delete-province-data span").text(provinceName);
        $("#delete-province-data").show();
    });

    $("#delete-province-confirm").click(function () {
        delete_province();
    });

    //上移
    $(document).on('click','.province_button #move_up',function(){
        get_province_data();
        $.ajax({
            type:"post",
            url:"../../../province/moveUp",
            data: {
                "bdProvinceId":province
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("移动成功",{icon:1});
                    getProvince();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //下移
    $(document).on('click','.province_button #move_down',function(){
        get_province_data();
        $.ajax({
            type:"post",
            url:"../../../province/moveDown",
            data: {
                "bdProvinceId":province
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("移动成功",{icon:1});
                    getProvince();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //城市点击事件-------------------------------
    //添加
    $(document).on('click','.city_button #add_date',function(){
        $("#add-city input").val("");
        $("#add-city-data").show();
    });

    $("#add-city-confirm").click(function () {
        add_city();
    });

    //编辑
    $(document).on('click','.city_button #update_date',function(){
        //获取省份选中值
        get_province_data();
        //获取城市选中值
        get_city_data();
        // alert(province+":" +provinceName);
        $("#update-city-data input[name='name']").val(cityName);
        $("#update-city-data").show();
    });

    $("#update-city-confirm").click(function () {
        update_city();
    });

    //删除
    $(document).on('click','.city_button #delete_date',function(){
        //获取城市选中值
        get_city_data();
        $("#delete-city-data span").text(cityName);
        $("#delete-city-data").show();
    });

    $("#delete-city-confirm").click(function () {
        delete_city();
    });

    //上移
    $(document).on('click','.city_button #move_up',function(){
        get_city_data();
        get_province_data();
        $.ajax({
            type:"post",
            url:"../../../city/moveUp",
            data: {
                "bdCityId":city,
                "bdProvinceId":province
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("移动成功",{icon:1});
                    getCity();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //下移
    $(document).on('click','.city_button #move_down',function(){
        get_district_data();
        get_province_data();
        $.ajax({
            type:"post",
            url:"../../../city/moveDown",
            data: {
                "bdCityId":city,
                "bdProvinceId":province
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("移动成功",{icon:1});
                    getCity();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //县区点击事件-------------------------------
    //添加
    $(document).on('click','.district_button #add_date',function(){
        $("#add-district input").val("");
        $("#add-district-data").show();
    });

    $("#add-district-confirm").click(function () {
        add_district();
    });

    //编辑
    $(document).on('click','.district_button #update_date',function(){
        //获取县区选中值
        get_district_data();
        //获取城市选中值
        get_city_data();
        // alert(province+":" +provinceName);
        $("#update-district-data input[name='name']").val(districtName);
        $("#update-district-data").show();
    });

    $("#update-district-confirm").click(function () {
        update_district();
    });

    //删除
    $(document).on('click','.district_button #delete_date',function(){
        //获取县区选中值
        get_district_data();
        $("#delete-district-data span").text(districtName);
        $("#delete-district-data").show();
    });

    $("#delete-district-confirm").click(function () {
        delete_district();
    });

    //上移
    $(document).on('click','.district_button #move_up',function(){
        get_district_data();
        get_city_data();
        $.ajax({
            type:"post",
            url:"../../../district/moveUp",
            data: {
                "bdCityId":city,
                "bdDistrictId":district
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("移动成功",{icon:1});
                    getDistrict();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //下移
    $(document).on('click','.district_button #move_down',function(){
        get_district_data();
        get_city_data();
        $.ajax({
            type:"post",
            url:"../../../district/moveDown",
            data: {
                "bdCityId":city,
                "bdDistrictId":district
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    layer.msg("移动成功",{icon:1});
                    getDistrict();
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

}

//县区点击事件-------------------------------
//删除
function delete_district() {
    $.ajax({
        type:"post",
        url:"../../../district/delete",
        data: {
            "bdDistrictId":district,
            "LogContent":"删除县区【 县区名称："+ districtName +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#delete-district-data").hide();
                getDistrict();
                layer.msg("删除成功",{icon:1});
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//编辑
function update_district() {
    var name = $("#update-district-data input[name='name']").val();
    if (name == '') {
        layer.msg('县区名称不能为空',{icon:2});
        return false;
    }
    $.ajax({
        type:"post",
        url:"../../../district/update",
        data: {
            "name":name,
            "bdCityId":city,
            "bdDistrictId":district,
            "LogContent":"编辑县区【 县区名称："+ name +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#update-district-data").hide();
                getDistrict();
                layer.msg("编辑成功",{icon:1});
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//添加
function add_district() {
    var name = $("#add-district-data input[name='name']").val();
    if (name == '') {
        layer.msg('县区名称不能为空',{icon:2});
        return false;
    }
    $.ajax({
        type:"post",
        url:"../../../district/insert",
        data: {
            "name":name,
            "bdCityId":city,
            "LogContent":"添加县区【 县区名称："+ name +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#add-district-data").hide();
                getDistrict();
                layer.msg("添加成功",{icon:1});
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//城市点击事件-------------------------------
//删除
function delete_city() {
    $.ajax({
        type:"post",
        url:"../../../city/delete",
        data: {
            "bdCityId":city,
            "LogContent":"删除城市【 城市名称："+ cityName +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#delete-city-data").hide();
                getProvince();
                layer.msg("删除成功",{icon:1});
            } else {
                if (res.code == 7) {
                    $(".operation-delete-data").hide();
                }
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//编辑
function update_city() {
    var name = $("#update-city-data input[name='name']").val();
    if (name == '') {
        layer.msg('城市名称不能为空',{icon:2});
        return false;
    }
    $.ajax({
        type:"post",
        url:"../../../city/update",
        data: {
            "name":name,
            "bdCityId":city,
            "bdProvinceId":province,
            "LogContent":"编辑城市【 城市名称："+ name +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#update-city-data").hide();
                getCity();
                layer.msg("编辑成功",{icon:1});
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//添加
function add_city() {
    var name = $("#add-city-data input[name='name']").val();
    if (name == '') {
        layer.msg('城市名称不能为空',{icon:2});
        return false;
    }
    $.ajax({
        type:"post",
        url:"../../../city/insert",
        data: {
            "name":name,
            "bdProvinceId":province,
            "LogContent":"添加城市【 城市名称："+ name +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#add-city-data").hide();
                getCity();
                layer.msg("添加成功",{icon:1});
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//省份点击事件-------------------------------
//删除
function delete_province() {
    $.ajax({
        type:"post",
        url:"../../../province/delete",
        data: {
            "bdProvinceId":province,
            "LogContent":"删除省份【 省份名称："+ provinceName +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#delete-province-data").hide();
                getProvince();
                layer.msg("删除成功",{icon:1});
            } else {
                if (res.code == 7) {
                    $(".operation-delete-data").hide();
                }
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//编辑
function update_province() {
    var name = $("#update-province-data input[name='name']").val();
    if (name == '') {
        layer.msg('省份名称不能为空',{icon:2});
        return false;
    }
    $.ajax({
        type:"post",
        url:"../../../province/update",
        data: {
            "provinceName":name,
            "bdProvinceId":province,
            "LogContent":"编辑省份【 省份名称："+ name +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#update-province-data").hide();
                getProvince();
                layer.msg("编辑成功",{icon:1});
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//添加
function add_province() {
    var name = $("#add-province-data input[name='name']").val();
    if (name == '') {
        layer.msg('省份名称不能为空',{icon:2});
        return false;
    }
    $.ajax({
        type:"post",
        url:"../../../province/insert",
        data: {
            "provinceName":name,
            "LogContent":"添加省份【 省份名称："+ name +"】"
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#add-province-data").hide();
                getProvince();
                layer.msg("添加成功",{icon:1});
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取省份选中文本和值-------------------------------
function get_province_data() {
    province = $(".province select").val();
    provinceName = $(".province select").find("option:selected").text();
}

//获取城市选中文本和值-------------------------------
function get_city_data() {
    city = $(".city select").val();
    cityName = $(".city select").find("option:selected").text();
}

//获取县区选中文本和值-------------------------------
function get_district_data() {
    district = $(".district select").val();
    districtName = $(".district select").find("option:selected").text();
}

//获取按钮-------------------------------
function getButtons() {
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
                var province_button = '';
                var city_button = '';
                var district_button = '';
                $.each(res.result,function (index,value) {
                    if (value.level == 1) {
                        province_button += '<input id="'+value.sourceUrl+'" type="button" value="'+value.name+'">';
                    } else if (value.level == 2) {
                        city_button += '<input id="'+value.sourceUrl+'" type="button" value="'+value.name+'">';
                    } else if (value.level == 3) {
                        district_button += '<input id="'+value.sourceUrl+'" type="button" value="'+value.name+'">';
                    }
                });
                $(".province_button").html(province_button);
                $(".city_button").html(city_button);
                $(".district_button").html(district_button);
            }
        }
    });
}

//获取所有省份-------------------------------
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
                var str = '<select style="width: 110%" size="'+ res.result.length +'">';
                $.each(res.result,function (index,value) {
                    str += '<option value="'+ value.bdProvinceId +'">'+ value.provinceName +'</option>';
                });
                str += '</select>';
                $(".province").html(str);
                // $('.province select').each(function (i, j) {
                //     $(j).find("option:selected").attr("selected", false);
                //     $(j).find("option").first().attr("selected", true);
                // });
                $('.province select option:first').prop('selected', 'selected');
                getCity();
                $('.province select').change(function () {
                    getCity();
                });
            } else {
                if (res.code == 8) {
                    $(".province").empty();
                    $(".city").empty();
                    $(".district").empty()
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        }
    });
}

//获取省份下的城市-------------------------------
function getCity() {
    province = $('.province select').val();
    if (province == "") {
        layer.msg("请先选择省份",{icon:2});
        return false;
    }
    $.ajax({
        type:"post",
        url:"../../../city/findCityByProvince",
        data: {
            "bdProvinceId":province
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '<select style="width: 110%" size="'+ res.result.length +'">';
                $.each(res.result,function (index,value) {
                    str += '<option value="'+ value.bdCityId +'">'+ value.name +'</option>';
                });
                str += '</select>';
                $(".city").html(str);
                $('.city select option:first').prop('selected', 'selected');
                getDistrict();
                $('.city select').change(function () {
                    getDistrict();
                });
            } else {
                if (res.code == 8) {
                    $(".city").empty();
                    $(".district").empty()
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        }
    });
}

//获取城市下的县区-------------------------------
function getDistrict() {
    city = $('.city select').val();
    if (city == "") {
        layer.msg("请先选择城市",{icon:2});
        return false;
    }
    $.ajax({
        type:"post",
        url:"../../../district/findDistrictByCityId",
        data: {
            "bdCityId":city
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '<select style="width: 110%" size="'+ res.result.length +'">';
                $.each(res.result,function (index,value) {
                    str += '<option value="'+ value.bdDistrictId +'">'+ value.name +'</option>';
                });
                str += '</select>';
                $(".district").html(str);
                $('.district select option:first').prop('selected', 'selected');
            } else {
                if (res.code == 8) {
                    $(".district").empty()
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        }
    });
}