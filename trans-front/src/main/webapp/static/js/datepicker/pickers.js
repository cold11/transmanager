$(function() {
    var option = {
        singleDatePicker: true,
        showDropdowns: true,
        minDate:moment(),
        timePicker : true,
        //"drops": "up",
        timePicker24Hour: true, //时间制
        timePickerSeconds: true, //时间显示到秒
        locale: {
            format: "YYYY-MM-DD HH:mm:ss",
            separator: " - ",
            applyLabel: "确认",
            cancelLabel: "清空",
            daysOfWeek: ["日","一","二","三","四","五","六"],
            monthNames: ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"]
        }
    };
    $('.date-picker').daterangepicker(option, function(start, end, label) {
    }).on('cancel.daterangepicker', function(ev, picker) {
        $(this).val('');
    });

});