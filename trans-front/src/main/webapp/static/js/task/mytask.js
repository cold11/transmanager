$(function () {
    $('#myTasksmenu').addClass('show');
    if(taskType==1){
        $('#t_task').addClass('active');
    }else {
        $('#p_task').addClass('active');
    }
    loadData('transtask');
});


function loadData(tabId) {
    $('#'+tabId).bootstrapTable({
        method: 'post',
        //striped: true,
        dataType: "json",
        pagination: true,
        queryParamsType: "limit",
        singleSelect: false,
        contentType: "application/x-www-form-urlencoded",
        pageSize: 20,
        locale:'zh-CN',
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
                field: 'endTime',
                title: '提交时间',
                align: 'center',
                valign: 'middle',
                sortable:true,
                formatter:function (value,row) {
                    if(value){
                        var date = new Date(value);
                        return date.toLocaleString();
                    }else{
                        return '-';
                    }


                }
            },
            // {
            //     field: 'transFileCount',
            //     title: '翻译/参考文件个数',
            //     align: 'center',
            //     valign: 'middle',
            //     formatter:function (value,row) {
            //         return value+'/'+row.referenceFileCount;
            //
            //     }
            // },
            {
                field: 'filenames',
                title: '文件名',
                align: 'center',
                valign: 'middle'
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
    let taskType = $("#taskType").val();

    return {
        taskType : taskType,
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
    $('#transtask').bootstrapTable('refresh', {});
}

