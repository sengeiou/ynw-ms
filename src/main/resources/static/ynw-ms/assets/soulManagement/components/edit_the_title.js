var subjectId = '';
var subjectItemId = '';
var sort = '';
var answerId = '';
var pathId = '';
var subjectContent = '';
var subjectItemContent = '';
var answerContent = '';
// var answerText = '';
$(function () {
    $(".back").click(function () {
        //返回
        getBack();
    });
    //获取题目
    getSubject();
    //获取答案
    getAnswer();
    //未来点击事件
    button_click();
    //获取题目类型
    getDictionary('SUBJECT_TYPE');
    //获取路径类型
    getDictionary('SUBJECT_PATH_TYPE');
    //隐藏操作框
    control_tip();
});

/**
 *  未来点击事件
 */
function button_click() {

    //开启答案输入框
    $(document).on('click','.answer-input',function(){
        $(".answer-body-left textarea").prop('disabled',true);
        $(".answer-body-left input").prop('disabled',true);
        $(this).parents('.answer-body-right').prev().children('textarea').prop('disabled',false);
        $(this).parents('.answer-body-right').prev().children('input').prop('disabled',false);
        // answerText = $(this).parents('.answer-body-right').prev().children('textarea').val();
    });

    //关闭答案输入框
    $(document).on('click','.inhibit_input',function(){
        // $(this).parent().prev().children('.answer-body-left').children('textarea').prop('disabled',true);
        //获取答案
        getAnswer();
    });

    //添加题目
    $(document).on('click','#add-subject',function(){
        $(".operation-data").hide();
        $("#add-subject-data textarea").val("");
        $("#add-subject-data").show();
        //返回顶部
        window.scroll(0,0);
    });

    //添加
    $("#add-subject-button").click(function () {
        var content = $("#add-subject-data textarea").val();
        if (content == '') {
            layer.msg('题目内容不能为空',{icon:2});
        }
        var type = $("#add-subject-data select[name='type']").val();
        var pathType = $("#add-subject-data select[name='pathType']").val();
        var kbExampaperId = $("#kbExamPaperId").val();
        // alert(content+":"+type+":"+pathType+":"+kbExampaperId)
        $.ajax({
            type:"post",
            url:"../../../subject/insert",
            data: {
                "content":content,
                "type":type,
                "pathType":pathType,
                "kbExampaperId":kbExampaperId,
                "LogContent":"添加题目【 题目内容："+ content +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    getSubject();
                    $("#add-subject-data").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //编辑题目弹窗
    $(document).on('click','.update-subject-comment',function() {
        $(".operation-data").hide();
        subjectId = $(this).prev().val();
        findBySubjectId();
        $("#update-subject-data").show();
        //返回顶部
        window.scroll(0,0);
    });

    //编辑题目
    $("#update-subject-button").click(function () {
        var content = $("#update-subject-data textarea").val();
        if (content == '') {
            layer.msg('题目内容不能为空',{icon:2});
        }
        var type = $("#update-subject-data select[name='type']").val();
        var pathType = $("#update-subject-data select[name='pathType']").val();
        // alert(content+":"+type+":"+pathType+":"+kbExampaperId)
        $.ajax({
            type:"post",
            url:"../../../subject/update",
            data: {
                "content":content,
                "type":type,
                "pathType":pathType,
                "kbSubjectId":subjectId,
                "LogContent":"编辑题目【 题目内容："+ content +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    getSubject();
                    $("#update-subject-data").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //删除题目弹窗
    $(document).on('click','.delete-subject',function () {
        $(".operation-data").hide();
        subjectId = $(this).prevAll('input').val();
        var comment = $(this).parent().next().children('.body-left').text();
        subjectContent = comment;
        $("#delete-subject .delete-data-body-comment").text(comment);
        $("#delete-subject").show();
        //返回顶部
        window.scroll(0,0);
    });

    //删除
    $("#delete-subject-confirm").click(function () {
        // alert(content+":"+isPathEnd+":"+nextSubjectId)
        $.ajax({
            type:"post",
            url:"../../../subject/delete",
            data: {
                "kbSubjectId":subjectId,
                "LogContent":"删除题目【 题目内容："+ subjectContent +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    getSubject();
                    $("#delete-subject").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //添加题目选项
    $(document).on('click','.add-subject-item-now',function () {
        $(".operation-data").hide();
        $("#add-subject-item-data select[name='isPathEnd']").val(0);
        subjectId = $(this).prevAll('input').val();
        sort = $(this).parent().prevAll('.sort').val();
        var route = $("#route").val();
        $("#add-subject-item-data textarea").val('');
        if (route == 'ET_SCORE') {
            $("#add-subject-item-data select[name='isPathEnd']").html('<option value="0">否</option>');
            $("#add-subject-item-data .dynamic-condition").hide();
            $("#add-subject-item-data span").text('分值:');
            $("#add-subject-item-data .score input").val('');
        } else {
            $("#add-subject-item-data .score").hide();
            $(".operation-subject-item span").text('下一题:');
            findByNextSubjectId('');
        }
        $("#add-subject-item-data").show();
        //返回顶部
        // window.scroll(0,0);
    });

    //改变题目路基时样式变化
    $(document).on('change','#add-subject-item-data select[name="isPathEnd"]',function () {
    // $(".operation-subject-item select[name='isPathEnd']").change(function () {
        if ($("#add-subject-item-data select[name='isPathEnd']").val() == 1) {
            $("#add-subject-item-data span").text('答案:');
            getEndAnswer('');
        } else {
            findByNextSubjectId('');
            $("#add-subject-item-data span").text('下一题:');
        }
    });

    //添加
    $("#add-subject-item-confirm").click(function () {
        var content = $("#add-subject-item-data textarea").val();
        if (content == '') {
            layer.msg('题目选项内容不能为空',{icon:2});
            return false;
        }
        var isPathEnd = $("#add-subject-item-data select[name='isPathEnd']").val();
        var kbExampaperAnswerId ='';
        var nextSubjectId ='';
        var score ='';
        var route = $("#route").val();
        if (route == 'ET_PATH') {
            if (isPathEnd == 1) {
                kbExampaperAnswerId = $("#add-subject-item-data .dynamic-condition select").val();
                if (kbExampaperAnswerId == null) {
                    layer.msg('题目选项答案不能为空',{icon:2});
                    return false;
                }
            } else {
                nextSubjectId = $("#add-subject-item-data .dynamic-condition select").val();
                if (nextSubjectId == null) {
                    layer.msg('题目选项下一题不能为空',{icon:2});
                    return false;
                }
            }
        } else if (route == 'ET_SCORE') {
            score = $("#add-subject-item-data .score input").val();
            if (score == '') {
                layer.msg('分数不能为空',{icon:2});
                return false;
            }
        }
        // alert(content+":"+isPathEnd+":"+nextSubjectId)
        $.ajax({
            type:"post",
            url:"../../../subjectItem/insert",
            data: {
                "content":content,
                "isPathEnd":isPathEnd,
                "kbExampaperAnswerId":kbExampaperAnswerId,
                "nextSubjectId":nextSubjectId,
                "kbSubjectId":subjectId,
                "score":score,
                "LogContent":"添加题目选项【 题目选项内容："+ content +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    getSubject();
                    $("#add-subject-item-data").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //编辑题目选项弹窗
    $(document).on('click','.update-subject-item',function () {
        $(".operation-data").hide();
        $("#update-subject-item-data select[name='isPathEnd']").val(0);
        subjectItemId = $(this).prevAll('input').val();
        sort = $(this).parents('.subject-item').prevAll('.sort').val();
        findBySubjectItemId();
        $("#update-subject-item-data").show();
        //返回顶部
        // window.scroll(0,0);
    });

    $(document).on('change','#update-subject-item-data select[name="isPathEnd"]',function () {
        // $(".operation-subject-item select[name='isPathEnd']").change(function () {
        if ($("#update-subject-item-data select[name='isPathEnd']").val() == 1) {
            $("#update-subject-item-data span").text('答案:');
            getEndAnswer('');
        } else {
            findByNextSubjectId('');
            $("#update-subject-item-data span").text('下一题:');
        }
    });

    //编辑
    $("#update-subject-item-confirm").click(function () {
        var content = $("#update-subject-item-data textarea").val();
        if (content == '') {
            layer.msg('题目选项内容不能为空',{icon:2});
            return false;
        }
        var isPathEnd = $("#update-subject-item-data select[name='isPathEnd']").val();
        var route = $("#route").val();
        var kbExampaperAnswerId ='';
        var nextSubjectId ='';
        var score ='';
        if (route == 'ET_PATH') {
            if (isPathEnd == 1) {
                kbExampaperAnswerId = $("#update-subject-item-data .dynamic-condition select").val();
                if (kbExampaperAnswerId == null) {
                    layer.msg('题目选项答案不能不选',{icon:2});
                    return false;
                }
            } else {
                nextSubjectId = $("#update-subject-item-data .dynamic-condition select").val();
                if (nextSubjectId == null) {
                    layer.msg('题目选项下一题不能不选',{icon:2});
                    return false;
                }
            }
        } else if (route == 'ET_SCORE') {
            score = $("#update-subject-item-data .score input").val();
            if (score == '') {
                layer.msg('分数不能为空',{icon:2});
                return false;
            }
        }
        // alert(content+":"+isPathEnd+":"+nextSubjectId)
        $.ajax({
            type:"post",
            url:"../../../subjectItem/update",
            data: {
                "content":content,
                "isPathEnd":isPathEnd,
                "kbExampaperAnswerId":kbExampaperAnswerId,
                "nextSubjectId":nextSubjectId,
                "kbSubjectItemId":subjectItemId,
                "score":score,
                "LogContent":"编辑题目选项【 题目选项内容："+ content +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    getSubject();
                    $("#update-subject-item-data").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //删除题目选项弹窗
    $(document).on('click','.delete-subject-item',function () {
        $(".operation-data").hide();
        subjectItemId = $(this).prevAll('input').val();
        var comment = $(this).parent().prev().text();
        subjectItemContent = comment;
        $("#delete-subject-item .delete-data-body-comment").text(comment);
        $("#delete-subject-item").show();
        //返回顶部
        // window.scroll(0,0);
    });

    //删除
    $("#delete-subject-item-confirm").click(function () {
        // alert(content+":"+isPathEnd+":"+nextSubjectId)
        $.ajax({
            type:"post",
            url:"../../../subjectItem/delete",
            data: {
                "kbSubjectItemId":subjectItemId,
                "LogContent":"删除题目选项【 题目选项内容："+ subjectItemContent +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    getSubject();
                    $("#delete-subject-item").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //添加答案
    $(document).on('click','.add-subject-answer',function () {
        $(".operation-data").hide();
        $(".operation-subject-answer textarea").val('');
        if ($("#route").val() == 'ET_PATH') {
            $(".add-subject-answer-route").hide();
            $(".operation-subject-answer").css('height',300);
        }
        $(".operation-subject-answer").show();
    });

    //添加
    $("#add-subject-answer").click(function () {
        var kbExamPaperId = $("#kbExamPaperId").val();
        var content = $(".operation-subject-answer textarea").val();
        if (content == '') {
            layer.msg('答案内容不能为空',{icon:2});
            return false;
        }
        var minScore = '';
        var maxScore = '';
        if ($("#route").val() == 'ET_SCORE') {
            minScore = $(".operation-subject-answer input[name='minScore']").val();
            if (minScore == '') {
                layer.msg('最小分值不能为空',{icon:2});
                return false;
            }
            maxScore = $(".operation-subject-answer input[name='maxScore']").val();
            if (maxScore == '') {
                layer.msg('最大分值不能为空',{icon:2});
                return false;
            }
        }
        $.ajax({
            type:"post",
            url:"../../../examPaperAnswer/insert",
            data: {
                "kbExampaperId":kbExamPaperId,
                "content":content,
                "minScore":minScore,
                "maxScore":maxScore,
                "LogContent":"添加答案【 答案内容："+ content +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    getAnswer();
                    // getSubject();
                    $(".operation-subject-answer").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

    //编辑答案
    $(document).on('click','.save-input',function(){
        if ($(this).parent().prev().children('.answer-body-left').children('textarea').prop('disabled') == false) {
            var content = $(this).parent().prev().children('.answer-body-left').children('textarea').val();
            if (content == '') {
                layer.msg('答案内容不能为空',{icon:2});
                return false;
            }
            var minScore = '';
            var maxScore = '';
            if ($("#route").val() == 'ET_SCORE') {
                minScore = $(this).parent().prev().children('.answer-body-left').children('.minScore').val();
                if (minScore == '') {
                    layer.msg('最小分值不能为空',{icon:2});
                    return false;
                }
                maxScore = $(this).parent().prev().children('.answer-body-left').children('.maxScore').val();
                if (maxScore == '') {
                    layer.msg('最大分值不能为空',{icon:2});
                    return false;
                }
            }
            var kbExampaperAnswerId = $(this).parent().prevAll('.data-top').children('input').val();
            $.ajax({
                type:"post",
                url:"../../../examPaperAnswer/update",
                data: {
                    "kbExampaperAnswerId":kbExampaperAnswerId,
                    "content":content,
                    "minScore":minScore,
                    "maxScore":maxScore,
                    "LogContent":"编辑答案【 答案内容："+ content +"】"
                },
                // contentType: "application/json",
                dataType: "json",
                success: function(res){
                    if (res.code == 1) {
                        getAnswer();
                        // getSubject();
                        layer.msg(res.message,{icon:1});
                    } else {
                        layer.msg(res.message,{icon:2});
                    }
                }
            });
        } else {
            layer.msg('请先编辑答案内容',{icon:2});
        }
        $(this).parent().prev().children('.answer-body-left').children('textarea').prop('disabled',true);
    });

    //删除答案
    $(document).on('click','.delete-subject-answer',function(){
        answerId = $(this).prev().val();
        var comment = $(this).parent().text();
        answerContent = comment;
        $(".delete-subject-answer-data span").text(comment);
        $(".delete-subject-answer-data").show();
    });

    //删除
    $("#delete-subject-answer").click(function () {
        $.ajax({
            type:"post",
            url:"../../../examPaperAnswer/delete",
            data: {
                "kbExampaperAnswerId":answerId,
                "LogContent":"删除答案【 答案内容："+ answerContent +"】"
            },
            // contentType: "application/json",
            dataType: "json",
            success: function(res){
                if (res.code == 1) {
                    getAnswer();
                    // getSubject();
                    $(".delete-subject-answer-data").hide();
                    layer.msg(res.message,{icon:1});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        });
    });

}

//获取终结点的答案
function getEndAnswer(sort) {
    var kbExamPaperId = $("#kbExamPaperId").val();
    $.ajax({
        type:"post",
        url:"../../../examPaperAnswer/conditionQueryExamPaperAnswer",
        data: {
            "kbExampaperId":kbExamPaperId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '<select style="width: 110%" size="'+ res.result.length +'">';
                $.each(res.result,function (index,value) {
                    str += '<option value="'+ value.kbExampaperAnswerId +'">'+ value.content +'</option>';
                });
                str += '</select>';
                $(".dynamic-condition").html(str);
                if (sort == 'update-item') {
                    $("#update-subject-item-data .dynamic-condition select").val(pathId);
                }
            } else {
                if (res.code == 8) {
                    $(".operation-data").hide();
                    layer.msg('请先添加答案',{icon:2});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        }
    });
}

//获取未选择的题目
function findByNextSubjectId(path) {
    var kbExamPaperId = $("#kbExamPaperId").val();
    $.ajax({
        type:"post",
        url:"../../../subject/findByNextSubjectId",
        data: {
            "kbExampaperId":kbExamPaperId,
            "sort":sort
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var str = '<select style="width: 110%" size="'+ res.result.length +'">';
                $.each(res.result,function (index,value) {
                    str += '<option value="'+ value.kbSubjectId +'">'+ value.content +'</option>';
                });
                str += '</select>';
                $(".dynamic-condition").html(str);
                if (path == 'update-item') {
                    $("#update-subject-item-data .dynamic-condition select").val(pathId);
                }
                // $(".operation-subject-item select[name='isPathEnd']").html('<option value="0">否</option><option value="1">是</option>');
            } else {
                if (res.code == 8) {
                    $(".operation-subject-item select[name='isPathEnd']").val(1);
                    $(".operation-subject-item span").text('答案:');
                    getEndAnswer('');
                    layer.msg('请添加下一题',{icon:2});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
        }
    });
}

//根据编号获取单个题目选项---------------------------
function findBySubjectItemId() {
    $.ajax({
        type:"post",
        url:"../../../subjectItem/findById",
        data: {
            "kbSubjectItemId":subjectItemId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var value = res.result;
                $("#update-subject-item-data textarea").val(value.content);
                $("#update-subject-item-data select[name='isPathEnd']").val(value.isPathEnd);
                var route = $("#route").val();
                if (route == 'ET_SCORE') {
                    $("#update-subject-item-data select[name='isPathEnd']").html('<option value="0">否</option>');
                    $("#update-subject-item-data .dynamic-condition").hide();
                    $("#update-subject-item-data span").text('分值:');
                    $("#update-subject-item-data .score input").val(value.score);
                } else {
                    $("#update-subject-item-data .score").hide();
                    if (value.isPathEnd == 1) {
                        pathId = value.kbExampaperAnswerId;
                        getEndAnswer('update-item');
                        $("#update-subject-item-data span").text('答案:');
                    } else {
                        pathId = value.nextSubjectId;
                        findByNextSubjectId('update-item');
                        $("#update-subject-item-data span").text('下一题:');
                    }
                }
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//根据编号获取题目---------------------------
function findBySubjectId() {
    $.ajax({
        type:"post",
        url:"../../../subject/findById",
        data: {
            "kbSubjectId":subjectId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var value = res.result;
                $("#update-subject-data select[name='type']").val(value.type);
                $("#update-subject-data select[name='pathType']").val(value.pathType);
                $("#update-subject-data textarea").val(value.content);
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取字典---------------------------
function getDictionary(key) {
    var route = $("#route").val();
    $.ajax({
        type:"post",
        url:"../../../dictionary/findDictionaryByGroupKey",
        data: {
            "groupKey":key

        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            if (res.code == 1) {
                var src = '';
                $.each(res.result,function (index,value) {
                    if (key == 'SUBJECT_PATH_TYPE' && route == 'ET_SCORE') {
                        if (value.itemKey == 'SPT_NONE') {
                            src += '<option value="'+ value.itemKey +'">'+ value.itemValue +'</option>';
                        }
                    } else {
                        if (value.itemKey != 'SPT_NONE') {
                            src += '<option value="'+ value.itemKey +'">'+ value.itemValue +'</option>';
                        }
                    }
                });
                if (key == 'SUBJECT_TYPE') {
                    $(".add-subject-data select[name='type']").html(src);
                }  else if (key == 'SUBJECT_PATH_TYPE') {
                    $(".add-subject-data select[name='pathType']").html(src);
                }
            } else {
                layer.msg(res.message,{icon:2});
            }
        }
    });
}

//获取答案---------------------------
function getAnswer() {
    var kbExamPaperId = $("#kbExamPaperId").val();
    $.ajax({
        type:"post",
        url:"../../../examPaperAnswer/conditionQueryExamPaperAnswer",
        data: {
            "kbExampaperId":kbExamPaperId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            var src = '<div class="data-top">答案栏<a class="add-subject-answer" href="javascript:;"><img src="../../img/plus.png"></a></div>';
            if (res.code == 1) {
                var size = Math.ceil(res.result.length/4);
                if (size > 1) {
                    // alert(560 + size*500);
                    $(".answer").css('height', size*400);
                }
                $.each(res.result,function (index,value) {
                    var num = index + 1;
                    src += '<div class="answer-body">';
                    src += '<div class="data-top">答案项(<span>'+ num +'</span>)<input type="hidden" value="'+ value.kbExampaperAnswerId +'"/>';
                    src += '<a class="delete-subject-answer" href="javascript:;"><img src="../../img/trash.png"></a></div>';
                    src += '<div class="answer-body-comment">';
                    src += '<div class="answer-body-left">';
                    var route = $("#route").val();
                    if (route == 'ET_SCORE') {
                        src += '<span>最小分值:</span><input class="minScore" type="text" oninput = "value=value.replace(/[^\\d]/g,\'\')" value="'+ value.minScore +'" disabled="disabled"><br/>';
                        src += '<span>最大分值:</span><input class="maxScore" type="text" oninput = "value=value.replace(/[^\\d]/g,\'\')" value="'+ value.maxScore +'" disabled="disabled">';
                        src += '<h1>答案内容:</h1>';
                        src += '<textarea rows="4" disabled="disabled">';
                    } else {
                        src += '<h1>答案内容:</h1>';
                        src += '<textarea rows="8" disabled="disabled">';
                    }
                    src += value.content + '</textarea></div>';
                    src += '<div class="answer-body-right"><a class="answer-input" href="javascript:;">';
                    src += '<img src="../../img/redact.png" style="width: 30px;height: 30px;">';
                    src += '</a></div></div>';
                    src += '<div class="answer-button"><input class="save-input" type="button" value="保存"/>';
                    src += '<input class="inhibit_input" type="button" value="取消"/>';
                    src += '</div></div>';
                });
            } else {
                if (res.code == 8) {
                    layer.msg("未添加答案",{icon:2});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
            $(".answer").html(src);
        }
    });
}

//获取题目--------------------------
function getSubject() {
    var kbExamPaperId = $("#kbExamPaperId").val();
    $.ajax({
        type:"post",
        url:"../../../subject/conditionQuerySubject",
        data: {
            "kbExampaperId":kbExamPaperId
        },
        // contentType: "application/json",
        dataType: "json",
        success: function(res){
            var src = '<div class="data-top">题目栏<a href="javascript:;"><img id="add-subject" src="../../img/plus.png"></a></div>';
            if (res.code == 1) {
                var size = Math.ceil(res.result.length/3);
                if (size > 1) {
                    // alert(560 + size*500);
                    $(".subject").css('height',60 + size*500);
                }
                $.each(res.result,function (index,value) {
                    var num = index + 1;
                    src += '<div class="subject-body"><input class="sort" type="hidden" value="'+ value.sort +'"/>' +
                        '<div class="data-top">题目(<span>' + num + '</span>)<input type="hidden" value="'+ value.kbSubjectId +'"/>';
                    src += '<a  class="delete-subject" href="javascript:;"><img src="../../img/trash.png"></a><a class="add-subject-item-now" href="javascript:;"><img src="../../img/plus.png"></a></div>';
                    src += '<div class="subject-body-subjectivity"><div class="body-left">'+ value.content +'</div>';
                    src += '<div class="body-right"><input type="hidden" value="'+ value.kbSubjectId +'"/><a class="update-subject-comment" href="javascript:;">' +
                        '<img src="../../img/redact.png"></a></div></div>';
                    src += '<div class="subject-item">';
                    $.each(value.subjectItemList,function (itemNum,item) {
                        src += '<div class="subject-body-option"><div class="body-left">' + item.content + '</div>';
                        src += '<div class="body-right"><input type="hidden" value="'+ item.kbSubjectItemId +'"/>';
                        src += '<a class="update-subject-item" href="javascript:;"><img src="../../img/redact.png" style="width: 30%;height: 60%;object-fit: contain;"></a>';
                        src += '<a class="delete-subject-item" href="javascript:;"><img src="../../img/trash.png" style="width: 30%;height: 60%;object-fit: contain;"></a></div></div>';
                    });
                    src += '</div></div>';
                });
            } else {
                if (res.code == 8) {
                    layer.msg("未添加题目",{icon:2});
                } else {
                    layer.msg(res.message,{icon:2});
                }
            }
            $(".subject").html(src);
        }
    });
}

//返回————————————————————————————
function getBack() {
    var msResourceId = $("#msResourceId").val();
    var URL = $("#URL").val();
    $(location).attr("href",'../../../'+URL+'?msResourceId='+msResourceId);
}