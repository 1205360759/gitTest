function addZero(n) {
    if (n < 10) {
        n = '0' + n
    }
    return n;
}
setmonths()
function setmonths() {
    var myDate = new Date();//获取系统当前时间
    var years = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
    var months = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
    var days = myDate.getDate();
    if (days === 1) {
        months = months - 1;
        if (months === 0) {
            months = 12;
            years = years - 1;
        }
    }
    var m = years + "-" + addZero(months);
    // $('.form_time').attr('value', m)
    $('.form_time').attr('value', '2018-09')

}
setdays()
function setdays() {
    var myDate = new Date();//获取系统当前时间
    var years = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
    var months = myDate.getMonth(); //获取当前月份(0-11,0代表1月)
    var days = myDate.getDate() - 1; //获取当前日(1-31)
    if (days === 00) {
        months = months - 1;
        var d = new Date();
        d.setMonth(months);
        d.setDate(0)
        days = d.getDate()
    }
    var d = years + "-" + addZero(months + 1) + "-" + addZero(days)
    //console.log(d+'-----------------------')
    $('.form_date').attr('value', d)
}


function formTime(ele, dateFn) {
    $.fn.datetimepicker.dates['zh-CN'] = {
        days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
        daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
        daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
        months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        today: "今天",
        suffix: [],
        meridiem: ["上午", "下午"]
    };
    $(ele)
        .datetimepicker({
            format: 'yyyymm',
            weekStart: 1,
            autoclose: true,
            startView: 3,
            minView: 3,
            forceParse: false,
            language: 'zh-CN',
            format: 'yyyy-mm'
        })
        .on('changeDate', dateFn);
}
function formDate(ele, is_) {
    $.fn.datetimepicker.dates['zh-CN'] = {
        days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
        daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
        daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
        months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        today: "今天",
        suffix: [],
        meridiem: ["上午", "下午"]
    };
    $(ele)
        .datetimepicker({
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2,
            forceParse: false,
            language: 'zh-CN',
            format: 'yyyy-mm-dd'
        })
        .on('changeDate', function (ev) {
            var val = ev.target.value.replace('-', is_)
            console.log(val)
            return val;
        });
}
