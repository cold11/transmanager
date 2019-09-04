$(function () {

    $("#sdlxliffFile").fileinput({
        theme: "fa",
        uploadUrl:ctxPath+"corpus/uploadTaskFile", //上传的地址
        language: 'zh',
        maxFileSize : 51200,
        allowedFileExtensions: ['sdlxliff'],
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
        if(response.success){
            layer.alert('文件上传成功!',{icon:1},function () {
                window.location.reload();
            });
        }else{
            layer.alert('文件上传失败!'+response.msg,{icon:2});
        }
    }).on('fileuploaderror', function(event, data, msg) {
        layer.alert('文件上传失败!',{icon:2});
    }).on('filebatchuploadcomplete', function(event, preview, config, tags, extraData) {

    });
    $(".btn-upload").on("click", function() {
        let userTaskId = $('#userTaskId').val();
        console.log(userTaskId);
        $("#sdlxliffFile").fileinput('refresh', {
            uploadExtraData: {
                'userTaskId':userTaskId
            }
        });
        $("#sdlxliffFile").fileinput('upload');
    });
    $('#uploadModal').on('hide.bs.modal', function () {
        $("#sdlxliffFile").fileinput('clear');
    });


    $('#corpusTable').bootstrapTable({
        method: 'post',
        url: ctxPath+'corpus/corpusmatchList',
        striped: true,
        dataType: "json",
        pagination: true,
        queryParamsType: "limit",
        singleSelect: false,
        contentType: "application/x-www-form-urlencoded",
        pageSize: 20,
        idField:'fileId',
        uniqueId: "fileId",
        showRefresh: true,
        locale:'zh-CN',
        pageList: [10, 20, 50, 100, 200],
        pageNumber:1,
        search: false,
        toolbar : "#toolbar",
        showColumns: true,
        autoRefresh:true,
        autoRefreshInterval:300,
        escape: true,
        sidePagination: "server",
        queryParams: queryParams,
        responseHandler: responseHandler,
        columns: [
            {
                field: 'filename',
                title: '文件名称',
                align: 'center',
                valign: 'middle'
            },
            {
                field: 'taskNo',
                title: '任务编号',
                align: 'center',
                valign: 'middle'
            },
            {
                field: 'uploadTime',
                title: '上传时间',
                align: 'center',
                valign: 'middle',
                sortable:true,
                formatter:function (value) {
                    var date = new Date(value);
                    return date.toLocaleString();
                }
            },
            {
                field: 'status',
                title: '状态',
                align: 'center',
                valign: 'middle',
                sortable:true,
                formatter:function (value) {
                    if(value==2){
                        return "完成";
                    }else if(value==3) {
                        return "失败";
                    }else{
                        return "处理中";
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
        let pageNo = Math.ceil((params.offset+1)/params.limit);
        return {
            sort: params.sort,
            order: params.order,
            pageSize: params.limit,
            pageNo: pageNo,
            filename:$('#filename').val(),
            status:$('#status').val(),
            beginTime:$('#beginTime').val(),
            endTime:$('#endTime').val()
        };

    }
    function operateFormatter(value, row, index) {
        var status = row.status;
        if(status==2){
            return [
                '<a class="download" href="javascript:void(0)" title="下载">',
                '<i class="fa fa-download"></i>',
                '</a>  ',
                '<a class="remove" href="javascript:void(0)" title="删除">',
                '<i class="fa fa-remove"></i>',
                '</a>'
            ].join('');
        }else if(status==3){
            return [
                '<a class="remove" href="javascript:void(0)" title="删除">',
                '<i class="fa fa-remove"></i>',
                '</a>'
            ].join('');
        }
        else{
           return '-';
        }

    }



});
window.operateEvents = {
    'click .download': function (e, value, row, index) {
        window.location.href=ctxPath+"corpus/downCorpusFile/"+row.fileId;
    },
    'click .remove': function (e, value, row, index) {
        $('#corpusTable').bootstrapTable('remove', {
            field: 'fileId',
            values: [row.fileId]
        });
        $.post(ctxPath+"corpus/deleteCorpusFile",{fileId:row.fileId}).done(function (data) {
            layer.msg(data.msg);
        });
    }
};
function showModal() {
    $('#uploadModal').modal('show');
    $("#sdlxliffFile").fileinput('refresh', {});
}

function search() {
    $('#corpusTable').bootstrapTable('refresh', {});
}