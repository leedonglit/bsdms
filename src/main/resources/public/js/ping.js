ping();
const pt = setInterval(function () {
    ping()
}, 10000);

function ping() {
    const start = new Date().getTime();
    $.ajax({
        //请求的方式：get/post
        type: 'GET',
        //请求的url地址
        url: '/api/ping',
        timeout: 2000,
        //这次请求要携带的数据（不需要参数可以省略）
        data: {},
        //请求成功之后的回调函数
        success: function (res) {
            let end = new Date().getTime();
            console.log("network delay",end - start)
            if (end - start > 5000) {
                layer.msg('Network restricted in speed, wait for a while or change another network.',{offset:'t',time:10000})
                clearInterval(pt);
            }
        },
        error: function (res) {
            layer.msg('Network restricted in speed, wait for a while or change another network.',{offset:'t',time:10000})
            clearInterval(pt);
        },
    });
}