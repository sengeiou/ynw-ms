/**
 * 时间转换
 * @param timestamp
 * @returns {string}
 */
function getDate(timestamp) {
    var time = new Date(timestamp);
    var year = time.getFullYear();
    var month = time.getMonth()+1;
    var date = time.getDate();
    var hours = time.getHours();
    var minutes = time.getMinutes();
    var seconds = time.getSeconds();
    return year+'-'+month+'-'+date+' '+hours+':'+minutes+':'+seconds;
}