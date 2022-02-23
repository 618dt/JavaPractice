<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>书评网数据管理系统</title>
    <link rel="stylesheet" href="/resources/layui/css/layui.css">
    <style>
        body {
            background-color: #f2f2f2;
        }

        .oa-container {
            position: absolute;
            width: 400px;
            height: 350px;
            top: 50%;
            left: 50%;
            padding: 20px;
            margin-left: -200px;
            margin-top: -175px;
        }

        #username, #password {
            text-align: center;
            font-size: 22px;
        }
    </style>
</head>
<body>
<div class="oa-container">
    <h1 style="text-align: center;margin-bottom: 20px">书评网数据管理系统</h1>
    <form class="layui-form" id="formLogin">
        <div class="layui-form-item">
            <!--required表示是必须填写的,必填校验-->
            <input type="text" id="username" lay-verify="required" name="username" placeholder="请输入用户名"
                   autocomplete="off" class="layui-input">
        </div>
        <div class="layui-form-item">
            <input type="password" id="password" lay-verify="required" name="password" placeholder="请输入密码"
                   autocomplete="off" class="layui-input">
        </div>
        <!--lay-filter即layui专属的id，autocomple表示是否显示历史输入
          不要将代码换行，可能会出现问题-->
        <div class="layui-form-item">
            <button class="layui-btn layui-btn-fluid" lay-submit lay-filter="login">登录</button>
        </div>
    </form>
</div>
<script src="/resources/layui/layui.all.js"></script>
<script>
    // 表单提交事件
    /*on代表捕捉事件，里面参数为submit即提交事件，login表示绑定哪个提交按钮才会触发这段代码*/
    layui.form.on("submit(login)", function () {
        //发送ajax请求进行登录校验
        layui.$.ajax({
            url: "/management/check_login",
            data: layui.$("#formLogin").serialize(), //提交表单数据,序列化
            type: "post",
            dataType: "json",
            success: function (json) {
                console.log(json);
                if (json.code == "0") { //登录校验成功
                    // layui.layer.msg("登录成功");替换为下面的
                    //跳转url
                    window.location.href = "/management/index.html";
                } else {
                    layui.layer.msg(json.msg);
                }
            }
        })
        return false;//submit提交事件返回true则表单提交,false则阻止表单提交
    })
</script>
</body>
</html>