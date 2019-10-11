$(function () {
    $('#pm_tasksmenu').addClass('show');
    $('#recevicedTaskLi').addClass('active');
    loadData('usertasksTab');
    $("#resulttaskFile").fileinput({
        theme: "fa",
        uploadUrl:ctxPath+"pm/uploadTaskFile", //上传的地址
        language: 'zh',
        maxFileSize : 51200,
        allowedFileExtensions: ['txt','doc','docx','zip','rar'],
        showRemove: false,
        hideThumbnailContent:true,
        maxFileCount : 1,
        required: true,
        showUpload: false,
        dropZoneTitle:'拖拽文件到这里 &hellip;',
        msgFilesTooMany : "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
        layoutTemplates :{
            actionUpload:'',
            actionZoom:''
        }
    }).on('fileuploaded',  function (event, data, previewId, index) {
        var response = data.response;
        console.log(response);
        if(response.success){
            layer.alert('文件上传成功!',{icon:1},function () {
                window.location.reload();
            });
        }else{
            layer.alert('文件上传失败!',{icon:2});
        }
    }).on('fileuploaderror', function(event, data, msg) {
        layer.alert('文件上传失败!',{icon:2});
    }).on('filebatchuploadcomplete', function(event, preview, config, tags, extraData) {

    });
    $(".btn-upload").on("click", function() {
        $("#resulttaskFile").fileinput('upload');
    });
    $('#uploadModal').on('hide.bs.modal', function () {
        $("#resulttaskFile").fileinput('clear');
    });
});


function loadData(tabId) {
    $('#'+tabId).bootstrapTable({
        method: 'post',
        url: ctxPath+'pm/receivedList',
        dataType: "json",
        pagination: true,
        queryParamsType: "limit",
        singleSelect: false,
        contentType: "application/x-www-form-urlencoded",
        pageSize: 20,
        locale:'zh-CN',
        //pageList: [10, 25, 50, 100, 200],
        pageNumber:1,
        idField : 'userTaskId',
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
                valign: 'middle'
            },
            {
                field: 'taskWords',
                title: '字数',
                align: 'center',
                valign: 'middle'
            },
            {
                field: 'username',
                title: '领取人',
                align: 'center',
                valign: 'middle',
                formatter:function (value,row) {
                    if(row.taskType==2&&row.endTime==null){
                        var dataUrl = ctxPath+'pm/updateUserTask'
                        var rowFormat = '<a href="#" class="editColCls" data-type="select" data-pk="'+row.userTaskId+'" data-value="'+value+'" data-url="'+dataUrl+'" title="选择译员" >'+value+'</a>';
                        return rowFormat;
                    }else{
                        return value;
                    }

                }
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
            {
                field: 'expirationDate',
                title: '到期时间',
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
                    url: ctxPath+'pm/updateUserTask',
                    params: function(params){
                        console.log(params);
                        var data = {};
                        data['userTaskId'] = params.pk;
                        data['expirationDate'] = params.value;
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
                field: 'filenames',
                title: '文件名',
                align: 'center',
                valign: 'middle'
            },
            {
                field: 'isPmAssign',
                title: '是否指定',
                align: 'center',
                valign: 'middle',
                formatter:function (value,row) {
                    if(value){
                        return '是';
                    }else return '否';

                }
            },
            {
                field: 'operate',
                title: '操作',
                align: 'center',
                valign: 'middle',
                width: 200,
                events: operateEvents,
                formatter: operateFormatter
            }

        ],
        onLoadSuccess:function(data){
            // $('#usertasksTab a.editColCls').each(function(i,e){
            //    console.log(e);
            // });
            $('#usertasksTab a.editColCls').editable({
                source: [],
                ajaxOptions: {
                    type:'post',
                    dataType: 'json',
                },
                emptytext : '-',
                url: ctxPath+'pm/updateUserTask',
                params: function(params){
                    var data = {};
                    data['userTaskId'] = params.pk;
                    data['userId'] = params.value;
                    return data;
                },
                success: function(response, newValue) {
                    console.log(this);
                    if(!response.success){
                        layer.msg(response.msg);
                    }else{
                        search();
                    }

                }
            }).on('shown', function(e, editable){
                editable.input.$input.select2({
                    placeholder: "===请选择用户===",
                    language : "zh-CN",
                    theme: "bootstrap",
                    allowClear: true,
                    width: 300,
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
                        url : ctxPath+'/findUsers?userType=2',
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
                    }
                });
                editable.input.$input.select2('val', editable.input.$input.val());
            });
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
    $('#usertasksTab').bootstrapTable('refresh', {});
}
window.operateEvents = {
    'click .download': function (e, value, row, index) {
        let userTaskId = row.userTaskId;
        window.location.href=ctxPath+'pm/downloadTaskFile?userTaskId='+userTaskId+'&taskType='+row.taskType;
    },
    'click .upload': function (e, value, row, index) {
        showModal(row.userTaskId);
    }
};
function operateFormatter(value, row, index) {
    var operateHtml = ['<div class="btn-group btn-group-sm">'];
    if(row.endTime!=null&&row.endTime!=''){
        operateHtml.push('<button type="button" class="btn btn-outline-primary download"><i class="fa fa-download"></i>下载译文</button>');
    }
    operateHtml.push('<button type="button" class="btn btn-outline-info upload"><i class="fa fa-upload"></i>上传终稿</button>');
    operateHtml.push('</div> ');
    return operateHtml.join('');
}

function showModal(taskId) {
    $('#uploadModal').modal('show');
    $("#resulttaskFile").fileinput('refresh', {
        uploadExtraData: {
            'taskId':taskId //参数
        }
    });
}

