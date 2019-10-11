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
    $('#tasksmenu').addClass('show');
    $('#publicTask').addClass('active');
});
window.operateEvents = {
    'click .download': function (e, value, row, index) {
        $(this).attr("disabled",true);
        let url = ctxPath+'trans/receive';
        if($('#taskType').val()==2){
            url = ctxPath+'proof/receive';
        }
        $.post(url,{taskNo:row.taskNo}).done(function (data) {
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
            $(this).attr("disabled",false);
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
                field: 'taskWords',
                title: '字数',
                align: 'center',
                valign: 'middle'
            },
            {
                field: 'unitPrice',
                title: '单价',
                align: 'center',
                valign: 'middle'
            },
            {
                field: 'transFileCount',
                title: '案件数',
                align: 'center',
                valign: 'middle'
            },
            {
                field: 'languages',
                title: '语种',
                align: 'center',
                valign: 'middle',
                width: 100
            },
            {
                field: 'expirationDate',
                title: '到期时间',
                align: 'center',
                valign: 'middle',
                sortable:true,
                formatter:function (value,row) {
                    if(tabId=='task'){
                        var date = new Date(row.transTime);
                        return date.toLocaleString();
                    }else{
                        var date = new Date(row.proofTime);
                        return date.toLocaleString();
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
            $("[data-toggle='popover']").popover();
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
    if(params.sort == 'expirationDate'){
        if(taskType=='1'){
            params.sort = 'transTime';
        }else{
            params.sort = 'proofTime';
        }
    }

    return {
        taskType : taskType,
        sort: params.sort,
        order: params.order,
        pageSize: params.limit,
        pageNo: pageNo
    };

}

function operateFormatter(value, row, index) {
    var memo = row.requirement;
    if(memo==''||memo==null)memo='无';
    return [
        '<button class="btn btn-sm btn-primary download" data-toggle="popover" data-content="'+memo+'" data-placement="top" data-trigger="hover" title="领取">',
        '<i class="fa fa-download"></i>领取',
        '</button>  '
    ].join('');
}

function PrefixInteger(num, length) {
    return (Array(length).join('0') + num).slice(-length);
}
