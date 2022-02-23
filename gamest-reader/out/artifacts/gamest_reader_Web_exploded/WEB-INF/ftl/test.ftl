<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <#--引用wangEditor-->
    <script src="/resources/wangEditor.min.js"></script>
</head>
<body>
<div>
    <button id="btnRead">读取内容</button>
    <button id="btnWrite">写入内容</button>
</div>
<#--使用div容器来显示富文本编辑器-->
<div id="divEditor" style="width: 800px;height:600px"></div>
<script>
    var E = window.wangEditor;//获取对象
    var editor = new E("#divEditor");//完成富文本编辑器初始化
    editor.create();//创建富文本编辑器，显示在页面上
    //为按钮绑定单击事件，因为没有引入jquery，所以使用原生的document；
    document.getElementById("btnRead").onclick = function () {
        var content = editor.txt.html();//获取编辑器现有的html内容
        alert(content);
    };
    //写入,可以看出和jQuery的语法很像,html无参则为读取，有参则为写入
    document.getElementById("btnWrite").onclick = function () {
        var content = "<li style='color:red'>李凭中国弹箜篌</li>";
        editor.txt.html(content);
    };
</script>
</body>
</html>