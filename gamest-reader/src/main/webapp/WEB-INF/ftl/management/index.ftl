<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Gamest书评网数据管理系统</title>
    <link rel="stylesheet" href="/resources/layui/css/layui.css">
    <script src="/resources/jquery.3.3.1.min.js"></script>
</head>

<body class="layui-layout-body">
<!-- Layui后台布局CSS -->
<div class="layui-layout layui-layout-admin">
    <!--头部导航栏-->
    <div class="layui-header">
        <!--系统标题-->
        <div class="layui-logo" style="font-size:14px">Gamest书评网数据管理系统</div>
        <!--右侧当前用户信息-->
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:void(0)">
                    <!--图标-->
                    <span class="layui-icon layui-icon-user" style="font-size: 20px">
                    </span>
                    <!--管理员信息-->
                    ${loginAdmin.username}
                </a>
            </li>
            <li class="layui-nav-item"><a href="/management/logout">注销</a></li>
        </ul>
    </div>
    <!--左侧菜单栏-->
    <div class="layui-side layui-bg-black">
        <!--可滚动菜单-->
        <div class="layui-side-scroll">
            <!--可折叠导航栏-->
            <ul class="layui-nav layui-nav-tree">


                <li class="layui-nav-item layui-nav-itemed">
                    <a href="javascript:void(0)">数据管理</a>
                    <dl class="layui-nav-child module" data-node-id="xxx">
                        <dd><a href="/management/book/index.html" target="ifmMain">图书管理</a></dd>
                        <dd><a href="/management/evaluation/index.html" target="ifmMain">短评管理
                            </a></dd>
                    </dl>
                </li>


            </ul>
        </div>
    </div>
    <!--主体部分采用iframe嵌入其他页面-->
    <div class="layui-body" style="overflow-y: hidden">
        <#--默认展示图书管理页面-->
        <iframe name="ifmMain" style="border: 0px;width: 100%;height: 100%" src="/management/book/index.html"></iframe>
    </div>
    <!--版权信息-->
    <div class="layui-footer">
        Copyright © Gamest. All Rights Reserved.
    </div>
</div>
<!--LayUI JS文件-->
<script src="/resources/layui/layui.all.js"></script>
<script>
    //将所有功能根据parent_id移动到指定模块下
    layui.$(".function").each(function () {
        var func = layui.$(this);
        var parentId = func.data("parent-id");
        layui.$("dl[data-node-id=" + parentId + "]").append(func);
    });
    //刷新折叠菜单
    layui.element.render('nav');
</script>
</body>
</html>