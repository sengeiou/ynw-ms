/**
 * 时间转换
 * @param timestamp
 * @returns {string}
 */
function getDate(timestamp) {
    var time = new Date(timestamp);
    var year = time.getFullYear();
    var month = time.getMonth()+1;
    if (month < 10) {
        month = "0"+month;
    }
    var date = time.getDate();
    if (date < 10) {
        date = "0"+date;
    }
    var hours = time.getHours();
    var minutes = time.getMinutes();
    var seconds = time.getSeconds();
    // return year+'-'+month+'-'+date+' '+hours+':'+minutes+':'+seconds;
    return year+'-'+month+'-'+date;
}
/**
 * 时间转换
 * @param timestamp
 * @returns {string}
 */
function getNowDate() {
    var time = new Date();
    var year = time.getFullYear();
    var month = time.getMonth()+1;
    var date = time.getDate();
    var hours = time.getHours();
    var minutes = time.getMinutes();
    var seconds = time.getSeconds();
    return year+'年'+month+'月'+date+'日';
}