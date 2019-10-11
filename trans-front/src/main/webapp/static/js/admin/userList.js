$(function () {
    $('#user-m-li').addClass('active');
    $("#roles").select2({
        placeholder: "===选择角色===",
        theme: "bootstrap",
        allowClear: true,
        cache:true,
        language:'zh-CN'

    });
    $('#addUserModal').on('hide.bs.modal', function () {
        $("#addUserForm input").val("");
        $('#addUserForm select').val('').trigger('change');

    });
    $('#addUserForm').validator({
        focusCleanup: true,
        stopOnError:false,
        timely: 2,
        theme:'yellow_top',
        // display: function(elem){
        //     return $(elem).closest('.form-item').children('label:eq(0)').text();
        // },
        fields: {
            'username': '登录名:required;remote('+ctxPath+'checkUsername, username:#username)',
            'password' :'密码:required;password'
        }
    }).on('validation', function(e, current){
        var form = this;
        if(form.isValid){
            $('#sava-edit-btn').attr('disabled', false);
        }else{
            $('#sava-edit-btn').attr('disabled', true);
        }

    }).on('valid.form', function(e, form){
        var form = this;
        $.post(ctxPath+"admin/addUser",$('#addUserForm').serialize()).done(function (data) {
            if(data.success){
                search();
                hideModal();
            }else {
                layer.alert(data.msg,{icon:2});
            }
        }).fail(function () {
            layer.alert('服务器出现故障',{icon:2});
        });

    });
    loadData('userListTab');
});


function loadData(tabId) {
    $('#'+tabId).bootstrapTable({
        method: 'post',
        url: ctxPath+'admin/userListData',
        dataType: "json",
        pagination: true,
        queryParamsType: "limit",
        singleSelect: false,
        contentType: "application/x-www-form-urlencoded",
        pageSize: 20,
        locale:'zh-CN',
        toolbar : "#toolbar",
        //pageList: [10, 25, 50, 100, 200],
        pageNumber:1,
        search: false,
        showColumns: false,
        sidePagination: "server",
        idField :'userId',
        queryParams: queryParams,
        responseHandler: responseHandler,
        columns: [
            {
                field: 'username',
                title: '用户名',
                align: 'center',
                valign: 'middle',
                sortable:true
            },
            {
                field: 'roleDescribe',
                title: '角色',
                width:200,
                align: 'center',
                valign: 'middle'
            },
            {
                field: 'isDisable',
                title: '是否禁用',
                align: 'center',
                valign: 'middle',
                editable:{
                    type: 'select',
                    source: function () {
                        var dataSource = '[{value: 1, text: \'已禁用\'},{value: 0, text: \'未禁用\'}]';
                        return dataSource;
                    },
                    url: ctxPath+'admin/updateUserInfo',
                    params: function(params){
                        var data = {};
                        data['userId'] = params.pk;
                        data['isDisable'] = params.value;
                        return data;
                    }
                }
            },
            {
                field: 'operate',
                title: '操作',
                align: 'center',
                valign: 'middle',
                events: operateEvents,
                formatter: operateFormatter
            }

        ],
        onLoadSuccess:function(data){
        }

    });
}

function responseHandler(res) {
    if (res.success) {
        var result = res.msg;
        return {
            "rows": result.result,
            "total": result.totalRows
        };

    } else {
        return {
            "rows": [],
            "total": 0
        };
    }

}


function queryParams(params) {
    var pageNo = Math.ceil((params.offset+1)/params.limit);
    let roles = $("#searchroles").val();
    return {
        sort: params.sort,
        order: params.order,
        pageSize: params.limit,
        pageNo: pageNo,
        username:$('#searchUsername').val(),
        roles:roles
    };

}

function search() {
    $('#userListTab').bootstrapTable('refresh', {});
}
window.operateEvents = {
    'click .resetpwd': function (e, value, row, index) {
        let userId = row.userId;
        $.post(ctxPath+'admin/resetUserPwd',{userId:userId,username:row.username}).done(function (res) {
            if(res.success){
                layer.alert('重置成功,新密码为111111',{icon:1});
            }else{
                layer.alert(res.msg,{icon:2});
            }
        });
    },
    'click .removeuser': function (e, value, row, index) {
        let userId = row.userId;
        $.post(ctxPath+'admin/deleteUser',{userId:userId}).done(function (res) {
            if(res.success){
                layer.alert('删除用户成功',{icon:1});
                $('#userListTab').bootstrapTable('refresh', {
                    userId:userId
                });
            }else{
                layer.alert(res.msg,{icon:2});
            }
        });
    }
};
function operateFormatter(value, row, index) {
    return [
        '<div class="btn-group btn-group-sm">',
        '<button type="button" class="btn btn-outline-info resetpwd"><i class="fa fa-trash"></i>密码重置</button>',
        '<button type="button" class="btn btn-outline-danger removeuser"><i class="fa fa-remove"></i>删除</button>',
        '</div> '
    ].join('');
}
function showModal() {
    $('#addUserModal').modal('show');
}
function hideModal() {
    $('#addUserModal').modal('hide');
}
function addUser() {
    showModal();
}

