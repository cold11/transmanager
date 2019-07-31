$(document).ready(function() {
    $.getJSON(ctxPath+'selectLanguage',function(json){
        if(json.success){
            languages = json.msg;
        }else{
            return false;
        }
    });
    var transUser = $("#transUserId");
    transUser.select2({
        placeholder: "请选择用户",
        theme: "bootstrap",
        cache:true,
        language:'zh-CN'
        // ajax: {
        //     url: ctxPath+'order/users',
        //     data: function (params) {
        //         var query = {
        //             roleId: 2
        //
        //         }
        //         // Query parameters will be ?search=[term]&page=[page]
        //         return query;
        //     },
        //     processResults: function (data) {
        //         console.log(data);
        //         let list = data.msg;
        //         let results = [];
        //         $.each(list,function (i,e) {
        //             console.log(i,e);
        //             let obj = {};
        //             obj.id=e.userId;
        //             obj.text = e.username;
        //             results.push(obj);
        //         })
        //         return {
        //             results: results
        //         };
        //     }
        // }

    });
    var proofUser = $("#proofUserId");
    proofUser.select2({
        placeholder: "请选择用户",
        theme: "bootstrap",
        cache:true,
        language:'zh-CN'
    });
    $('#orderForm').validator({
        focusCleanup: true,
        stopOnError:false,
        timely: 2,
        theme:'yellow_top',
        // display: function(elem){
        //     return $(elem).closest('.form-item').children('label:eq(0)').text();
        // },
        fields: {
            'title': '订单标题:required',
            'customer' :'客户名称:required',
            'expirationDate':'到期时间:required'
        }
    }).on('validation', function(e, current){
        var form = this;
        console.log(form.isValid);
        // form 中是否所有字段都验证通过
        // if(form.isValid){
        //     $('#submitbtn').attr('disabled', false);
        // }else{
        //     $('#submitbtn').attr('disabled', true);
        // }

    }).on('valid.form', function(e, form){
        var form = this;
        //form.holdSubmit();
        $('#submitbtn').attr('disabled', true);
        $.each(files,function (i,e) {
            var uuid = e.uuid;
            var checkText=$('#srcLan_'+uuid).find("option:selected").text();
            e.sourceLanguageId = $('#srcLan_'+uuid).val();
            e.sourceLanName = checkText;
            var checkText2=$('#tgtLan_'+uuid).find("option:selected").text();
            e.targetLanName = checkText2;
            e.targetLanguageId = $('#tgtLan_'+uuid).val();
        });
        var orderJson = $('#orderForm').serializeJson();
        orderJson.tbOrderFiles = files;



        $.ajax({
            // headers必须添加，否则会报415错误
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: 'POST',
            dataType: "json",
            data: JSON.stringify(orderJson),
            url: ctxPath+"order/submitOrder",
            success: function(data){
                if(data.success){
                    layer.alert('发布成功!',function () {
                        location.href = ctxPath+"task/hall";
                    });
                }else{
                    layer.alert(data.message);
                }

            },
            error:function (XMLHttpRequest, textStatus, errorThrown) {
                console.log(textStatus,errorThrown);
                layer.alert('出现异常!');
                $('#submitbtn').attr('disabled',"false");
            }

        });

    });
});

function buildLanguage(index) {
    var options = '<select id="srcLan_'+index+'" name="srcLan" style="width:90px;"onchange="changeLan(\''+index+'\');">';
    $.each(languages,function (i,e) {
        options+="<option value='" +e.languageId+"'";
        if(e.languageCode=='en'){
            options+=" selected";
        }
        options+=">" + languages[i].languageName + "</option>";
    });

    return options+"</select>";
}

function buildTargetLanguage(index) {
    var options = '<select id="tgtLan_'+index+'" name="tgtLan" style="width:90px;">';
    $.each(languages,function (i,e) {
        options+="<option value='" +e.languageId+"'";
        options+="'>" + e.languageName + "</option>";
    });
    return options+"</select>";
}

function changeLan(index) {
    var lanId = $('#srcLan_'+index).val();
    var tgtLanSelect = $('#tgtLan_'+index);
    tgtLanSelect.empty();
    $.each(languages,function (i,e) {
        if(e.languageId==lanId){
            return true;
        }
        $("<option value='" +e.languageId + "'>" + e.languageName + "</option>").appendTo(tgtLanSelect);
    });

}
function del_up_task_file(fileId) {
    var delid = $('#up_task_file_tr_s_'+fileId);
    delid.remove();
    for(var i = 0; i < files.length; ) {
        if(files[i].fileId==fileId) {
            files.splice(i, 1);
        } else {
            i++;
        }
    }
}