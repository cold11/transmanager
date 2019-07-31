var loginInit = {
    //初始化页面跳转，为了防止从iframe跳转到login页面直接在iframe中显示login页面
    initPage : function() {
        //alert(location.href);
        if(window.top != window.self){
            top.location.href = location.href;
        }
    }
}
$(function () {

    loginInit.initPage();
    $("#username").focus();
    $("#loginForm input").keyup(function(event) {
        if (event.keyCode == "13") {
            $("#btnLogin").trigger("click");
        }
    });
    /**
     * 登陆表单
     */
    $('#loginForm').validator({
        focusCleanup: true,
        stopOnError:false,
        //debug: true,
        timely: 2,
        theme:'yellow_top',
        // display: function(elem){
        //     return $(elem).closest('.form-item').children('label:eq(0)').text();
        // },
        fields: {
            'username': '登录名:required;remote('+ctxPath+'checkLoginName, username:#username)',
            'password' :'密码:required;password'
        }
    }).on('validation', function(e, current){
        var form = this;
        // form 中是否所有字段都验证通过
        if(form.isValid){
            $('#btnLogin').attr('disabled', false);
        }else{
            $('#btnLogin').attr('disabled', true);
        }

    }).on('valid.form', function(e, form){
        var form = this;
        //form.holdSubmit();
        $.post(ctxPath+"doLogin",$('#loginForm').serialize()).done(function (data) {
            console.log(data);
            if(data.code=='200'){
                window.location.href=ctxPath+data.msg;
            }else {
                layer.alert(data.msg,{icon:2});
            }
        }).fail(function () {
            layer.alert('服务器出现故障',{icon:2});
        });

    });
});