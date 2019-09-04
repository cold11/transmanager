(function($){

    "use strict";


    $('.date-rage').daterangepicker({
        ranges: {
            //'清空': [null,null],
            '今天': [moment(), moment().add(1, 'days')],
            '昨天': [moment().subtract(1, 'days'),moment()],
            '7天': [moment().subtract(7, 'days'), moment()],
            '30天': [moment().subtract(30, 'days'), moment()],
            '这个月': [moment().startOf('month'), moment().endOf('month')],
            '上个月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')] },
        showDropdowns: true,//当设置值为true的时候，允许年份和月份通过下拉框的形式选择 默认false
        maxDate:moment(),
        autoUpdateInput : false,
        locale: {
            minDate: '2015-01-01',
            format: "YYYY-MM-DD",
            separator: " - ",
            //dateLimit: {days: 3600}, //起止时间的最大间隔
            applyLabel: "确认",
            cancelLabel: "清空",
            fromLabel: "开始时间",
            toLabel: "结束时间",
            customRangeLabel: '自定义',
            daysOfWeek: ["日","一","二","三","四","五","六"],
            monthNames: ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"]
        }
    }).on('cancel.daterangepicker', function(ev, picker) {
        $("#dataRange").val("");
        $("#beginTime").val("");
        $("#endTime").val("");
    }).on('apply.daterangepicker', function(ev, picker) {
        $("#beginTime").val(picker.startDate.format('YYYY-MM-DD'));
        $("#endTime").val(picker.endDate.format('YYYY-MM-DD'));
        $("#dataRange").val(picker.startDate.format('YYYY-MM-DD')+" 至 "+picker.endDate.format('YYYY-MM-DD'));
    }, function(ev, picker) {

        //$("#dataRange").val(picker.startDate.format('YYYY-MM-DD')+" 至 "+picker.endDate.format('YYYY-MM-DD'));
        // console.log(this.startDate.format(this.locale.format));
        // console.log(this.endDate.format(this.locale.format));
        if(!picker.startDate){
            $("#dataRange").val("");
            $("#beginTime").val("");
            $("#endTime").val("");
        }else{
            $("#beginTime").val(picker.startDate.format('YYYY-MM-DD'));
            $("#endTime").val(picker.endDate.format('YYYY-MM-DD'));
            $("#dataRange").val(picker.startDate.format('YYYY-MM-DD')+" 至 "+picker.endDate.format('YYYY-MM-DD'));
        }
    });

    $('.date-time-range').daterangepicker({
        timePicker: true,
        startDate: moment().startOf('hour'),
        endDate: moment().startOf('hour').add(32, 'hour'),
        locale: {
            format: 'M/DD hh:mm A'
        }
    });

    $('.date-picker').daterangepicker({
        singleDatePicker: true,
        showDropdowns: true,
        minYear: 1901,
        maxYear: parseInt(moment().format('YYYY'),10)
    }, function(start, end, label) {
        var years = moment().diff(start, 'years');
        alert("You are " + years + " years old!");
    });


    var start = moment().subtract(29, 'days');
    var end = moment();

    function cb(start, end) {
        $('.predefined-date-range span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
    }

    $('.predefined-date-range').daterangepicker({
        startDate: start,
        endDate: end,
        ranges: {
            'Today': [moment(), moment()],
            'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
            'This Month': [moment().startOf('month'), moment().endOf('month')],
            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        }
    }, cb);

    cb(start, end);






})(jQuery);