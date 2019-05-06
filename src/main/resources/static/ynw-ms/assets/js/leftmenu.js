var Item = (function (selector, itemArr) {
    this.container = selector;
    this.menuArr = itemArr;

});
var menu_arr = [{
    name: "我是一级目录",
    children: [
        {
            name: "我是二级目录01",
            url: "components/mailbox.html",
        },
        {
            name: "我是二级目录02",
            children: [{name: "我是三级目录01", url: "components/mailbox.html"}, {name: "我是三级目录02",url: "components/mailbox.html"}]
        }
    ]
}];
Item.prototype._createContent = function (menuArr) {
    for (var i in menuArr) {
        var parentNodeli = $('<li class="nav-li current"></li>');
        var parentTitleA = $('<a href="javascript:;" class="ue-clear"><i class="nav-ivon"></i></a>');
        var parentTitleName = $('<span class="nav-text"></span>');
        parentTitleName.text(menuArr.name);
        parentTitleA.appendChild(parentTitleName);
        parentNodeli.appendChild(parentTitleA);
        Item._createContent(menuArr.children);
    }
}