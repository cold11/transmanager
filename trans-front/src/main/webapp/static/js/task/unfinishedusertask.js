$(function () {
    $("#resulttaskFile").fileinput({
        theme: "fa",
        uploadUrl:ctxPath+"task/uploadTaskFile", //上传的地址
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

function showModal(taskId) {
    $('#uploadModal').modal('show');
    $("#resulttaskFile").fileinput('refresh', {
        uploadExtraData: {
            'taskId':taskId //参数
        }
    });
}