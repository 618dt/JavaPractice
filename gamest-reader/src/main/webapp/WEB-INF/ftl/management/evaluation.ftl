<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>短评管理</title>
    <style>
        #dlgBook {
            padding: 10px
        }
    </style>
    <link rel="stylesheet" href="/resources/layui/css/layui.css">

</head>
<body>


<div class="layui-container">
    <blockquote class="layui-elem-quote">近期短评列表</blockquote>
    <table id="grdEvaluation" lay-filter="grdEvaluation"></table>
</div>
<!--table日期转换格式-->
<script>
    function Format(datetime, fmt) {
        if (parseInt(datetime) == datetime) {
            if (datetime.length == 10) {
                datetime = parseInt(datetime) * 1000;
            } else if (datetime.length == 13) {
                datetime = parseInt(datetime);
            }
        }
        datetime = new Date(datetime);
        var o = {
            "M+": datetime.getMonth() + 1,                 //月份
            "d+": datetime.getDate(),                    //日
            "h+": datetime.getHours(),                   //小时
            "m+": datetime.getMinutes(),                 //分
            "s+": datetime.getSeconds(),                 //秒
            "q+": Math.floor((datetime.getMonth() + 3) / 3), //季度
            "S": datetime.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (datetime.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }
</script>


<script src="/resources/layui/layui.all.js"></script>
<script>

    var table = layui.table;
    var $ = layui.$;
    var editor = null;
    //第一个实例
    table.render({
        elem: '#grdEvaluation'
        , id: "evaluationList"
        , url: "/management/evaluation/list" //数据接口
        , page: true //开启分页
        , limit: 10
        , cols: [[ //表头
            {
                field: "createTime",
                title: '发布时间',
                width: '200',
                templet: '<div>{{ Format(d.createTime,"yyyy-MM-dd")}}</div>'
            }
            , {field: 'content', title: '短评', width: '400'}
            , {
                type: "space", title: '图书', width: '200', templet: function (d) {
                    return d.book.bookName;
                }
            }
            , {
                type: "space", title: '用户名', width: '100', templet: function (d) {
                    console.info(d);
                    return d.member.username;
                }
            }
            , {
                type: 'space', title: '操作', width: '100', templet: function (d) {
                    if (d.state == "enable") {
                        return "<button class='layui-btn layui-btn-sm '  data-id='" + d.evaluationId + "' onclick='disableEvaluation(this)'>禁用</button>";
                    } else if (d.state == "disable") {
                        return "已禁用";
                    }
                }
            }
        ]]
    });

    function disableEvaluation(obj) {
        var evaluationId = $(obj).data("id");
        layui.layer.prompt({
            title: '请输入禁用原因',
        }, function (value, index, elem) {
            $.post("/management/evaluation/disable", {
                evaluationId: evaluationId, reason: value
            }, function (json) {
                if (json.code == "0") {
                    layui.layer.close(index);
                    layui.layer.msg("短评已禁用")
                    table.reload('evaluationList');
                }
            }, "json")
        });
    }
</script>
</body>
</html>