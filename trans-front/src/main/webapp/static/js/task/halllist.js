$(function () {

//loadData();
    $('#myTab a[href="#trans"]').tab('show');
    $('#myTab a[href="#trans"]').on('shown.bs.tab', function(e){
        $('#taskType').val(1);
        loadData('task');
    });
    $('#myTab a[href="#proof"]').on('shown.bs.tab', function(e){
        $('#taskType').val(2);
        loadData('prooftask');
    });
});
window.operateEvents = {
    'click .download': function (e, value, row, index) {
        $.post(ctxPath+'trans/receive',{orderNum:row.orderNum}).done(function (data) {
            if(data.success){
                window.location.href = ctxPath+"task/unfinished";
            }else if(data.msg=='unfinished'){
                layer.alert("有未完成的任务", {icon: 8},function(){
                    window.location.href = ctxPath+"task/unfinished";
                });
            }else{
                layer.alert(data.msg, {icon: 8});
            }
        }).fail(function () {
            layer.msg('领取失败');
        });
        //window.location.href=ctxPath+"trans/receive?orderNum="+row.orderNum;
    }
};

function loadData(tabId) {
    $('#'+tabId).bootstrapTable({
        method: 'post',
        url: ctxPath+'task/hallList',
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
                field: 'orderNum',
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
                field: 'caseNo',
                title: '案号',
                align: 'center',
                valign: 'middle'
            },
            {
                field: 'expirationDate',
                title: '到期时间',
                align: 'center',
                valign: 'middle',
                sortable:true,
                formatter:function (value) {
                    var date = new Date(value);
                    return date.toLocaleString();
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
    return {
        status : $("#taskType").val(),
        sort: params.sort,
        order: params.order,
        pageSize: params.limit,
        pageNo: pageNo
    };

}

function operateFormatter(value, row, index) {

    return [
        '<a class="download" href="javascript:void(0)" title="领取">',
        '<i class="fa fa-download"></i>领取',
        '</a>  '
    ].join('');
}