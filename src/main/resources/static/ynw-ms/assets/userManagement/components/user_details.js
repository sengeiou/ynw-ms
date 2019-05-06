var imageId = "";
$(function () {
    //获取单个用户
    getAcUser();
    //返回
    getBack();
    // 获取用户标签
    getUserLabel();
    //获取用户兴趣标签
    // getInterest();
    //获取用户照片
    getAcUserImage();
    //获取用户推送消息
    getNotify();
    //获取用户隐私消息
    getPrivacy();
    //照片审核
    setImageStatus();
    //点击隐藏
    $(".screenshot-cancel").click(function () {
        $(".screenshot-user").hide();
    });
    //隐藏操作框
    control_tip();
    //修改用户身份证状态
    setIdCardStatus();
    //获取身份证字典数据
    getDictionary('VERIFICATUION_OF_ IDENTITY_CARD');
    //获取照片字典数据
    getDictionary('PHOTO_REVIEW_FAILED');
});

//修改用户身份证状态
function setIdCardStatus() {
    $(".IDCardPass").click(function () {
        updateIDByUserId(1,null);
    });
    $(".IDCardNoPass").click(function () {
        // updateIDByUserId(-2);
        $("#idCardAudit select").val("");
        $("#idCardAudit").show();
    });
    $("#id-card-confirm").click(function () {
        var content = $("#idCardAudit select").val();
        if (content == "") {
            layer.msg("请选择审核不通过原因",{icon:2});
            return false;
        }
        updateIDByUserId(-2,content);
    });
}

//获取字典数据
function getDictionary(groupKey) {
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
                var src = '<option value="">---请选择---</option>'
                $.each(res.result,function (index,value) {
                    src += '<option value="'+ value.itemValue +'">---'+ value.itemValue +'---</option>';
                });
                if (groupKey == 'PHOTO_REVIEW_FAILED') {
                    $("#photoAudit select").html(src);
                } else if (groupKey == 'VERIFICATUION_OF_ IDENTITY_CARD') {
                    $("#idCardAudit select").html(src);
                }
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//修改用户身份证状态
function updateIDByUserId(status,content) {
    var userId = $("#acUserId").val();
    $.ajax({
        type:"post",
        url:"../../../acUser/updateIDByUserId",
        data: {
            "acUserId":userId,
            "idVerifyStatus":status,
            "content":content
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                $("#idCardAudit").hide();
                layer.msg(res.message,{icon:1});
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//照片审核
function setImageStatus() {
    //通过
    $(document).on('click','.get-through',function () {
        imageId = $(this).prevAll('input[name="imageId"]').val();
        setStatus(imageId, 1, null);
    })

//不通过
    $(document).on('click','.no-pass',function () {
        $("#photoAudit select").val("");
        imageId = $(this).prevAll('input[name="imageId"]').val();
        $("#photoAudit").show();
    });

    $("#photo-confirm").click(function () {
        var content = $("#photoAudit select").val();
        if (content == "") {
            layer.msg("请选择审核失败原因",{icon:2});
            return false;
        }
        setStatus(imageId, -1, content);
    });

    /**
     *  点击查看
     */
    $(document).on("click","#img img,.id_audit_content img", function () {
        var url = $(this).prop("src");
        $("#user-img img").prop("src", url);
        $("#user-img").show();
    })

    /**
     *  点击查看
     */
    $(document).on("click","#user_img img", function () {
        var url = $(this).prop("src");
        $("#user-head-portrait img").prop("src", url);
        $("#user-head-portrait").show();
    });

    /**
     *  替换头像
     */
    $("#replace-head").click(function () {
        var userId = $("#acUserId").val();
        $.ajax({
            type:"post",
            url:"../../../acUser/replaceHeadById",
            data: {
                "acUserId":userId,
                "LogContent":"替换用户头像【 用户编号："+ userId +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    $("#user-head-portrait").hide();
                    //刷新
                    getAcUser();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

}

//照片审核
function setStatus(imageId, status, content) {
    $.ajax({
        type:"post",
        url:"../../../acUser/updateAcUserImageById",
        data: {
            "acUImageId":imageId,
            "status":status,
            "content":content
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                getAcUserImage();
                $("#photoAudit").hide();
                layer.msg(res.message,{icon:1});
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取用户隐私消息-------------------------------
function getPrivacy() {
    var acUserId = $("#acUserId").val();
    $.ajax({
        type:"post",
        url:"../../../acUser/findPrivacyByUserId",
        data: {
            "acUserId":acUserId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var value = res.result;
                if (value.pSearchFlag == 1) {
                    $('.pSearchFlag .closes').css('background','darkgrey');
                } else {
                    $('.pSearchFlag .open').css('background','darkgrey');
                }
                if (value.noSearchFlag == 1) {
                    $('.noSearchFlag .closes').css('background','darkgrey');
                } else {
                    $('.noSearchFlag .open').css('background','darkgrey');
                }
                if (value.cCommendFlag == 1) {
                    $('.cCommendFlag .closes').css('background','darkgrey');
                } else {
                    $('.cCommendFlag .open').css('background','darkgrey');
                }
            } else {
                if (res.code != 8) {
                    layer.msg(res.message,{icon:2});
                }
            }
        }
    });
}

//获取用户推送消息-------------------------------
function getNotify() {
    var acUserId = $("#acUserId").val();
    $.ajax({
        type:"post",
        url:"../../../acUser/findNotifyByUserId",
        data: {
            "acUserId":acUserId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var value = res.result;
                if (value.admireFlag == 1) {
                    $('.admireFlag .closes').css('background','darkgrey');
                } else {
                    $('.admireFlag .open').css('background','darkgrey');
                }
                if (value.attentionFlag == 1) {
                    $('.attentionFlag .closes').css('background','darkgrey');
                } else {
                    $('.attentionFlag .open').css('background','darkgrey');
                }
                if (value.commentFlag == 1) {
                    $('.commentFlag .closes').css('background','darkgrey');
                } else {
                    $('.commentFlag .open').css('background','darkgrey');
                }
                if (value.dStateFlag == 1) {
                    $('.dStateFlag .closes').css('background','darkgrey');
                } else {
                    $('.dStateFlag .open').css('background','darkgrey');
                }
                if (value.vUpdateFlag == 1) {
                    $('.vUpdateFlag .closes').css('background','darkgrey');
                } else {
                    $('.vUpdateFlag .open').css('background','darkgrey');
                }
                if (value.addFriendFlag == 1) {
                    $('.addFriendFlag .closes').css('background','darkgrey');
                } else {
                    $('.addFriendFlag .open').css('background','darkgrey');
                }
            } else {
                if (res.code != 8) {
                    layer.msg(res.message,{icon:2});
                }
            }
        }
    });
}

//获取用户照片-------------------------------
function getAcUserImage() {
    var acUserId = $("#acUserId").val();
    $.ajax({
        type:"post",
        url:"../../../acUser/findAcUserImageByUserId",
        data: {
            "acUserId":acUserId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                // 获取数据除以5并向上取整
                var imgSize = Math.ceil(res.result.length/5)*190+70;
                $(".user_img").css("height", imgSize);
                //获取user_body的height
                var userBodyLength = $("#user_body").height();
                var increase = (userBodyLength - 640) - imgSize;
                //判断user_img是否超长，超长增加user_body长度
                if (increase < 0) {
                    $("#user_body").css("height",userBodyLength - increase);
                }
                var src = '';
                $.each(res.result,function (index,value) {
                    src += '<div class="img-body"><div class="img-body-content"><img src="'+ value.imageUrl +'"></div>';
                    if (value.status == 0) {
                        src += '<div class="img-body-button"><input name="imageId" type="hidden" value="'+value.acUImageId+'">' +
                            '<input class="get-through" type="button" value="通过">' +
                            '<input class="no-pass" type="button" value="不通过"></div>';
                    } else if (value.status == -1) {
                        src += '<div class="img-body-button-one"><input name="imageId" type="hidden" value="'+value.acUImageId+'">' +
                            '<input class="get-through" type="button" value="通过"></div>';
                    } else if (value.status == 1) {
                        src += '<div class="img-body-button-one"><input name="imageId" type="hidden" value="'+value.acUImageId+'">' +
                            '<input class="no-pass" type="button" value="不通过"></div>';
                    }
                    src += '</div>';
                });
                $("#img").html(src);
            } else {
                if (res.code == 8) {
                    layer.msg("未上传图片",{icon:2});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        }
    });
}

//获取用户标签-------------------------------
function getUserLabel() {
    var acUserId = $("#acUserId").val();
    $.ajax({
        type:"post",
        url:"../../../user_label/findAllByUserId",
        data: {
            "userId":acUserId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '';
                //获取map长度，然后修改div的height
                var labelLength = Object.keys(res.result).length * 40 + 20;
                $(".user_label_all").css("height",labelLength);
                //获取user_body的height
                var userBodyLength = $("#user_body").height();
                var increase = (userBodyLength - 435) - labelLength;
                //判断user_label_all是否超长，超长增加user_body长度
                if (increase < 0) {
                    $("#user_body").css("height",userBodyLength - increase);
                }
                $.each(res.result,function(key,values){
                    // console.log(key);
                    src += '<div id="interest" class="user_label">';
                    src += '<div>'+ key +':</div>';
                    $(values).each(function(){
                        src += '<div class="interest label">' + this.name + '</div>';
                        // console.log("/t" + this);
                    });
                    src += '</div>';
                });
                $(".user_label_all").html(src);
            } else {
                $(".user_label_all").hide();
                // layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取用户兴趣标签-------------------------------
function getInterest() {
    var acUserId = $("#acUserId").val();
    $.ajax({
        type:"post",
        url:"../../../interest/findInterestByAcUserId",
        data: {
            "acUserId":acUserId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '<div>我的兴趣:</div>';
                $.each(res.result,function (index,value) {
                    src += '<div class="interest label">'+ value.name +'</div>';
                });
                $("#interest").html(src);
            } else {
                if (res.code == 8) {
                    $("#interest").html("<div>我的兴趣:</div>")
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        }
    });
}

//获取单个用户-------------------------------
function getAcUser() {
    var acUserId = $("#acUserId").val();
    $.ajax({
        type:"post",
        url:"../../../acUser/findById",
        data: {
            "acUserId":acUserId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var value = res.result;
                $("#user_img img").prop("src", value.headImageUrl);
                $("#user_img span").text(value.signature);
                $('#user_details span:nth-child(1)').text(value.nickname);
                $('#user_details span:nth-child(3)').text(value.phoneNumber);
                $('#user_details span:nth-child(5)').text(value.no);
                if (value.sex == 0) {
                    $('#user_details span:nth-child(7)').text('男');
                } else {
                    $('#user_details span:nth-child(7)').text('女');
                }
                value.birthday = getDate(value.birthday);
                $('#user_details span:nth-child(9)').text(value.birthday);
                $('#user_details span:nth-child(11)').text(value.age);
                $('#user_details span:nth-child(13)').text(value.zodiacName);
                $('#user_details span:nth-child(15)').text(value.bdBusinessName);
                $('#user_details span:nth-child(17)').text(value.bdDegreesName);
                $('#user_details span:nth-child(19)').text(value.bdUniversityName);
                $('#user_details span:nth-child(21)').text(value.hometownName);
                $('#user_details span:nth-child(23)').text(value.bdCityName);
                value.createTime = getDate(value.createTime);
                $('#user_details span:nth-child(25)').text(value.createTime);
                if (value.status == 0) {
                    $('#user_details span:nth-child(27)').text('无效');
                } else {
                    $('#user_details span:nth-child(27)').text('有效');
                }
                $('#user_details span:nth-child(29)').text(value.referrerName);
                $('#user_details span:nth-child(31)').text(value.idNumber);
                if (value.idVerifyStatus != -1) {
                    $(".idImageFrontUrl").html('<img src="'+ value.idImageFrontUrl +'">');
                    $(".idImageBackUrl").html('<img src="'+ value.idImageBackUrl +'">');
                    // if (value.idVerifyStatus == -2) {
                    //     $(".IDCardPass").hide();
                    //     $(".IDPush").show();
                    // }
                } else {
                    //没有上传身份证件照片，隐藏按钮
                    $(".id_audit_button").hide();
                }
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//返回————————————————————————————
function getBack() {
    $(".back").click(function () {
        var msResourceId = $("#msResourceId").val();
        var URL = $("#URL").val();
        $(location).attr("href",'../../../'+URL+'?msResourceId='+msResourceId + "&name="+ $("#name").val() + "&idVerifyStatus="+ $("#idVerifyStatus").val() +
            "&phone="+ $("#phone").val() + "&no=" + $("#no").val() + "&sex=" + $("#sex").val() + "&province=" + $("#province").val() +
            "&city=" + $("#city").val() + "&status=" + $("#status").val() + "&imageStatus=" + $("#imageStatus").val() + "&num=" + $("#num").val());
    });
}