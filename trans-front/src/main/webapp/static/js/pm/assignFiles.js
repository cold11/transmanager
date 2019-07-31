$(function () {
    loadData('orderList');
    $("select.js-data-select2-ajax").each(function () {
        var $this = $(this);
        $this.select2({
            placeholder: "===请选择用户===",
            language : "zh-CN",
            theme: "bootstrap",
            allowClear: true,
            //minimumInputLength: 2,
            escapeMarkup: function (markup) {
                return markup;
            },
            templateResult: function formatRepo(repo) {
                if (repo.loading) {
                    return repo.text;
                }
                return repo.text+'<small class="text-muted">-当前任务数:'+repo.taskCount+'</small>';
            },

            templateSelection: function formatRepoSelection(repo) {
                return repo.text;
            },
            ajax : {
                url : $this.attr("href"),
                dataType : 'json',
                delay : 250,// 延迟显示
                cache: true,
                data : function(params) {
                    return {
                        username : params.term,
                        pageNo : params.page,// 第几页
                        pageSize : 5// 每页显示多少行
                    };
                },
                processResults: function (data, params) {
                    params.page = params.page || 1;
                    let list = data.msg.result;
                    let results = [];
                    $.each(list,function (i,e) {
                        let obj = {};
                        obj.id=e.userId;
                        obj.text = e.username;
                        obj.taskCount = e.taskCount;
                        results.push(obj);
                    });
                    return {
                        results: results,
                        pagination : {
                            more : params.page < data.msg.totalPages//
                        }
                    };
                }
            },

        });
    });



    // $("#transUserId").select2({
    //     placeholder: "===请选择用户===",
    //     theme: "bootstrap",
    //     ajax: {
    //         url: ctxPath+'findUsers',
    //         dataType: 'json',
    //         data: function (params) {
    //             var query = {
    //                 userType: 2
    //             }
    //             return query;
    //         },
    //         processResults: function (data) {
    //             console.log(data);
    //             let list = data.msg;
    //             let results = [];
    //             $.each(list,function (i,e) {
    //                 let obj = {};
    //                 obj.id=e.userId;
    //                 obj.text = e.username+'-当前任务数:'+e.taskCount;
    //                 results.push(obj);
    //             })
    //             return {
    //                 results: results
    //             };
    //         },
    //         cache: true
    //     }
    // });
    $('#proofCol').hide();
    $('#proofTimeCol').hide();
    $('#proofTime').attr('disabled',true);
    $("#transType").change(function(){
        var ss = $(this).children('option:selected').val();
        if(ss==2){
            $('#proofCol').show();
            $('#proofTimeCol').show();
            $('#proofTime').attr('disabled',false);
        }else{
            $('#proofCol').hide();
            $('#proofTimeCol').hide();
            $('#proofTime').attr('disabled',true);
        }
    });
    $('#assignModal').on('hide.bs.modal', function () {
        $("#assignFrm input").val("");
        //$('#assignFrm select').prop('selectedIndex', 0);
        $('#assignFrm select').val('').trigger('change');
        $('#proofCol').hide();
        $('#proofTimeCol').hide();
        $('#proofTime').attr('disabled',true);
    });
    $('#assignFrm').validator({
        focusCleanup: true,
        stopOnError:false,
        timely: 2,
        theme:'yellow_top',
        fields: {
            'unitPrice' :'单价:required;integer[+]'
           // 'transTime':'翻译时间:required'
        }
        // valid: function(form) {
        //     console.log(form);
        // }
    }).on('validation', function(e, current){
        var form = this;
        if(form.isValid){
            $('#sava-edit-btn').attr('disabled', false);
        }else{
            $('#sava-edit-btn').attr('disabled', true);
        }

    }).on('valid.form', function(e, form){
        var form = this;
        //form.holdSubmit();
        $('#sava-edit-btn').attr('disabled', true);
        $('#spinner').show();
        var formData = $(form).serialize();
        $.post(ctxPath+"pm/createTask",formData).done(function (data) {
            if(data.success){
                layer.alert('生成任务成功', {
                    icon:1,
                    closeBtn: 1
                    ,anim: 4 //动画类型
                }, function(){
                    layer.closeAll();
                    $('#closeBtn').trigger("click");
                });

                $('#orderList').bootstrapTable('refresh',{});
            }else {
                layer.alert('生成任务失败',{icon:2});
            }

        }).fail(function () {
            layer.alert('服务器出现故障',{icon:2});
        }).always(function () {
            $('#sava-edit-btn').attr('disabled', false);
            $('#spinner').hide();
        });

    });
});


function loadData(tabId) {
    $('#'+tabId).bootstrapTable({
        method: 'post',
        url: ctxPath+'pm/assignOrderList',
        striped: true,
        dataType: "json",
        pagination: true,
        queryParamsType: "limit",
        singleSelect: false,
        contentType: "application/x-www-form-urlencoded",
        pageSize: 20,
        idField:'orderId',
        uniqueId: "orderId",
        showRefresh: true,
        locale:'zh-CN',
        pageList: [10, 20, 50, 100, 200],
        pageNumber:1,
        search: false,
        showColumns: false,
        detailView: true,
        escape: true,
        sidePagination: "server",
        queryParams: queryParams,
        responseHandler: responseHandler,
        columns: [
            {
                field: 'orderNum',
                title: '订单编号',
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
                //events: operateEvents,
                formatter: operateFormatter
            }
        ],
        onExpandRow: function (index, row, $detail) {
            InitSubTable(index, row, $detail);
        },
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
    return {
        sort: params.sort,
        order: params.order,
        pageSize: params.limit,
        pageNo: pageNo
    };

}

function operateFormatter(value, row, index) {
    var memo = row.requirement;
    if(memo==''||memo==null)memo='无';
    var str = '<button data-toggle="popover" data-content="'+memo+'" data-placement="top" data-trigger="hover" class="btn btn-sm btn-primary">项目要求</button>';
    return str;
}
var subTable;
var InitSubTable = function (index, row, $detail) {
    var orderId = row.orderId;
    subTable = $detail.html('<table></table>').find('table');
    $(subTable).bootstrapTable({
        url: ctxPath+'pm/orderFileList',
        method: 'post',
        pagination: true,
        queryParamsType: "limit",
        queryParams: subQueryParams,
        pageNumber:1,
        search: false,
        showColumns: false,
        showFooter: true,
        sidePagination: "server",
        contentType: "application/x-www-form-urlencoded",
        uniqueId: "fileId",
        pageSize: 10,
        pageList: [10, 25],
        searchTimeOut:5000,
        responseHandler: responseHandler,
        columns: [
            {
                field: 'checkbox',
                align: 'center',
                valign: 'middle',
                checkbox: true,
                titleTooltip: '请选择一项',
                showSelectTitle: true,
                clickToSelect: true
            },
            {
                field: 'filename',
                title: '文件名',
                align: 'center',
                valign: 'middle'
            },
            {
                field: 'fileType',
                title: '文件类型',
                align: 'center',
                valign: 'middle',
                formatter:function (value,row)  {
                    console.log(value);
                    if(value==1){
                        return '翻译文件';
                    }else{
                        return '参考文件';
                    }
                }
            },
            {
                field: 'language',
                title: '语种',
                align: 'center',
                valign: 'middle',
                formatter:function (value,row)  {
                        return row.sourceLanName+' - '+row.targetLanName;
                }
            },
            {
                field: 'subOperate',
                title:'',
                footerFormatter: function (value) {
                    var assignBtn = "<button id='btn-"+orderId+"' disabled='true' onclick='showModal("+orderId+")' class='btn btn-sm btn-info'>生成任务</button>";
                    return assignBtn;
                }
            }
        ]
        // onCheckAll:function(rows){
        //     $('#btn-'+orderId).prop('disabled', !rows.length);
        // }
    });
    $(subTable).on('check.bs.table uncheck.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        $('#btn-'+orderId).prop('disabled', !subTable.bootstrapTable('getSelections').length);
    });
    function subQueryParams(params) {
        var pageNo = Math.ceil((params.offset+1)/params.limit);
        return {
            orderId:orderId,
            sort: params.sort,
            order: params.order,
            pageSize: params.limit,
            pageNo: pageNo
        };
    }


};
function getIdSelections() {
    return $.map(subTable.bootstrapTable('getSelections'), function (row) {
        return  row.fileId ;
    });
}
function showModal(orderId) {
    var row = $('#orderList').bootstrapTable('getRowByUniqueId', orderId);
    var selects = getIdSelections().join(',');
    $('#orderId').val(orderId);
    $('#fileIds').val(selects);
    $('#orderNum').val(row.orderNum);
    $('#title').val(row.title);
    $('#customer').val(row.customer);
    //$('#caseNo').val(row.caseNo);
    $('#expirationDate').val(row.expirationDate);
    $('#requirement').val(row.requirement);
    $('#memo').val(row.memo);
    $('#assignModal').modal('show');
}