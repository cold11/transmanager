$(function () {
    var status = [];
    $.ajaxSettings.async = false;
    $.getJSON(ctxPath+'getTaskStatus',function(data){
        status = data;
    });
    $.ajaxSettings.async = true;
    $('#pm_tasksmenu').addClass('show');
    $('#createdTasksLi').addClass('active');
    $('#tasksTab').bootstrapTable({
        method: 'post',
        url: ctxPath+'pm/taskList',
        dataType: "json",
        pagination: true,
        queryParamsType: "limit",
        singleSelect: false,
        contentType: "application/x-www-form-urlencoded",
        pageSize: 20,
        locale:'zh-CN',
        pageList: [10, 25, 50, 100, 200],
        pageNumber:1,
        search: false,
        showColumns: true,
        idField : 'taskId',
        sidePagination: "server",
        queryParams: queryParams,
        responseHandler: responseHandler,
        columns: [
            {
                field: 'taskNo',
                title: '任务号',
                width: 100,
                align: 'center',
                valign: 'middle',
                sortable:true
            },
            {
                field: 'title',
                title: '标题',
                align: 'center',
                valign: 'middle'
            },
            {
                field: 'customer',
                title: '客户',
                align: 'center',
                valign: 'middle'
            },
            {
                field: 'transTypeDescribe',
                title: '翻译流程',
                align: 'center',
                valign: 'middle'
            },
            {
                field: 'transTime',
                title: '翻译到期时间',
                align: 'center',
                valign: 'middle',
                editable:  {
                    type: 'combodate',
                    format:'YYYY-MM-DD HH:mm:ss',
                    template:'YYYY-MM-DD HH:mm:ss',
                    ajaxOptions: {
                        type:'post',
                        dataType: 'json',
                    },
                    emptytext : '-',
                    url: ctxPath+'pm/updateTask',
                    params: function(params){
                        var data = {};
                        data['taskId'] = params.pk;
                        data['transTime'] = params.value;
                        return data;
                    },
                    success: function(response, newValue) {
                        if(!response.success){
                            layer.msg(response.msg);

                        }
                    }

                }
            },
            {
                field: 'proofTime',
                title: '校对到期时间',
                align: 'center',
                valign: 'middle',
                formatter:function (value,row) {
                    if(value!=null){
                        var dataUrl = ctxPath+'pm/updateTask'
                        var rowFormat = '<a href="#" class="editColCls" data-type="combodate" data-pk="'+row.taskId+'" data-value="'+value+'" data-url="'+dataUrl+'" title="选择到期时间" >'+value+'</a>';
                        return rowFormat;
                    }else{
                        return '-';
                    }
                }
            },
            // {
            //     field: 'unitPrice',
            //     title: '单价',
            //     align: 'center',
            //     valign: 'middle'
            // },
            {
                field: 'taskStatus',
                title: '任务状态',
                editable:  {
                    mode: 'popup',
                    type: 'select',
                    source: function (v) {
                        var data = status;
                        return data;
                    },
                    ajaxOptions: {
                        type:'post',
                        dataType: 'json',
                    },
                    emptytext : '-',
                    url: ctxPath+'pm/updateTask',
                    send:'auto',
                    params: function(params){
                        var data = {};
                        data['taskId'] = params.pk;
                        data['taskStatus'] = params.value;
                        return data;
                    },
                    success: function(response, newValue) {
                        if(!response.success){
                            layer.msg(response.msg);

                        }
                    }

                },
                align: 'center',
                valign: 'middle'
            },
            // {
            //     field: 'expirationDate',
            //     title: '到期时间',
            //     align: 'center',
            //     valign: 'middle',
            //     sortable:true,
            //     formatter:function (value,row) {
            //         if(row.transTime){
            //             var date = new Date(row.transTime);
            //             return date.toLocaleString();
            //         }else{
            //             var date = new Date(row.proofTime);
            //             return date.toLocaleString();
            //         }
            //     }
            // },
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
            $('#tasksTab a.editColCls').editable({
                    format:'YYYY-MM-DD HH:mm:ss',
                    template:'YYYY-MM-DD HH:mm:ss',
                    ajaxOptions: {
                        type:'post',
                        dataType: 'json',
                    },
                    emptytext : '-',
                    url: ctxPath+'pm/updateTask',
                    params: function(params){
                        var data = {};
                        data['taskId'] = params.pk;
                        data['proofTime'] = params.value;
                        return data;
                    },
                    success: function(response, newValue) {
                        if(!response.success){
                            layer.msg(response.msg);
                        }
                    }
            });
        }

    });
});
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
    return {
        sort: params.sort,
        order: params.order,
        pageSize: params.limit,
        pageNo: pageNo,
        taskNo:$('#taskNo').val(),
        beginTime:$('#beginTime').val(),
        endTime:$('#endTime').val()
    };

}

function search() {
    $('#tasksTab').bootstrapTable('refresh', {});
}
window.operateEvents = {
    'click .redistribute': function (e, value, row, index) {
        let taskNo = row.taskId;
        $.post(ctxPath+'pm/redistribute',{taskId:taskNo}).done(function (res) {
            if(res.success){
                layer.alert("成功",function () {
                    window.location.href = ctxPath+'pm/assign';
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
        '<button type="button" class="btn btn-outline-danger redistribute"><i class="fa fa-trash"></i>重新分配</button>',
        '</div> '
    ].join('');
}