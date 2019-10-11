$(function () {
    $('#tasksTable').bootstrapTable({
        method: 'post',
        url: ctxPath+'task/assignedList',
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
                field: 'taskTypeDescribe',
                title: '任务类型',
                align: 'center',
                valign: 'middle',
                sortable:true
            },
            {
                field: 'filenames',
                title: '文件名',
                align: 'center',
                valign: 'middle'
            },

            {
                field: 'beginTime',
                title: '领取时间',
                align: 'center',
                valign: 'middle',
                sortable:true,
                formatter:function (value,row) {
                    var date = new Date(value);
                    return date.toLocaleString();

                }
            },
            {
                field: 'expirationDate',
                title: '到期时间',
                align: 'center',
                valign: 'middle',
                sortable:true,
                formatter:function (value,row) {
                    var date = new Date(value);
                    return date.toLocaleString();

                }
            },
            {
                field: 'operate',
                title: '操作',
                align: 'center',
                valign: 'middle',
                events:operateEvents,
                formatter:operateFormatter
            }

        ],
        onLoadSuccess:function(data){
        }

    });
    $('#tasksmenu').addClass('show');
    $('#privateTask').addClass('active');
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
    let taskType = $("#taskType").val();

    return {
        taskType : taskType,
        sort: params.sort,
        order: params.order,
        pageSize: params.limit,
        pageNo: pageNo
    };

}
window.operateEvents = {
    'click .download': function (e, value, row, index) {
        let taskNo = row.taskId;
        window.location.href=ctxPath+'task/downloadTaskFile/'+taskNo;
    },
    'click .upload': function (e, value, row, index) {
        showModal(row.userTaskId);
    },
    'click .corpusmatch': function (e, value, row, index) {
    },
    'click .giveup': function (e, value, row, index) {
        giveupTask( row.userTaskId);
    }
};
function operateFormatter(value, row, index) {
    return [
        '<div class="btn-group btn-group-sm">',
        // '<button type="button" class="btn btn-outline-success corpusmatch"><i class="fa fa-file-excel-o"></i>预处理</button>',
        '<button type="button" class="btn btn-outline-primary download"><i class="fa fa-download"></i>下载原文</button>',
        '<button type="button" class="btn btn-outline-info upload"><i class="fa fa-upload"></i>上传译文</button>',
        '<button type="button" class="btn btn-outline-danger giveup"><i class="fa fa-trash"></i>放弃</button>',
        '</div> '
    ].join('');
}

function giveupTask(taskId) {
    $.post(ctxPath+'task/giveupTask',{taskId:taskId}).done(function (data) {
        console.log(data);
        if(data.success){
            layer.alert('放弃任务成功',{icon:1},function () {
                window.location.href=ctxPath+data.msg;
            })
        }else{
            layer.alert('放弃任务失败',{icon:2},function () {
                window.location.href=ctxPath+data.msg;
            })
        }
    });
}