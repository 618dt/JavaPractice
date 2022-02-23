<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${book.bookName}</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="/resources/bootstrap/bootstrap.css">
    <link rel="stylesheet" href="/resources/raty/lib/jquery.raty.css">
    <script src="/resources/jquery.3.3.1.min.js"></script>
    <script src="/resources/bootstrap/bootstrap.min.js"></script>
    <script src="/resources/art-template.js"></script>
    <script src="/resources/raty/lib/jquery.raty.js"></script>
    <style>
        .container {
            padding: 0px;
            margin: 0px;
        }

        .row {
            padding: 0px;
            margin: 0px;
        }

        .alert {
            padding-left: 0.5rem;
            padding-right: 0.5rem;
        }

        .col- * {
            padding: 0px;
        }

        .description p {
            text-indent: 2em;
        }

        .description img {
            width: 100%;
        }

        .highlight {
            background-color: lightskyblue !important;
        }

    </style>
    <script>
        $.fn.raty.defaults.path = '/resources/raty/lib/images';
        $(function () {
            $(".stars").raty({readOnly: true});
        })
        /*页面就绪函数，书写freemarker的脚本*/
        $(function () {
            <#if memberReadState ??>//存在阅读状态
            /*利用JavaScript的属性选择器，‘*’表示所有名为data-read-state且值为''的属性
            * 并将该属性设置为高亮，那么就可以将对应的想看或者看过的按钮设置为高亮显示了
            * 这里的值是后台通过mav传过来的*/
            $("*[data-read-state='${memberReadState.readState}']").addClass("highlight");
            </#if>
            <#if !loginMember ??>/*如果用户没有登录*/
            /*选择拥有data-read-state这个自定义属性的标签，而不管自定义属性的值是多少*/
            $("*[data-read-state]").click(function () {
                /*通过id选择bootstrap的对话框,然后根据其对话框函数执行show(显示)动作*/
                $("#exampleModalCenter").modal("show");
            })
            //将写短评以及点赞等功能进行相应的登录控制,这里利用组合选择减少代码
            $("#btnEvaluation,*[data-evaluation-id]").click(function () {
                $("#exampleModalCenter").modal("show");
            })
            </#if>

            <#if loginMember ??>
            $("*[data-read-state]").click(function () {
                var readState = $(this).data("read-state");//获取当前点击按钮的自定义属性的值,不用.val
                /*ajax方法的简化形式四个参数为
                (发送的地址，发送的参数列表,处理函数（用来处理服务器返回的数据）,服务器返回的数据类型)*/
                $.post("/update_read_state", {
                    memberId:${loginMember.memberId},
                    bookId:${book.bookId},
                    readState: readState
                }, function (json) {
                    console.info(json);
                    if (json.code == "0") {
                        //将原有高亮的css全部清除，让两个按钮回到默认的状态
                        $("*[data-read-state]").removeClass("highlight");
                        /*通过拼接字符串的方式选择刚才点击的那个按钮*/
                        $("*[data-read-state='" + readState + "']").addClass("highlight");
                    }
                }, "json")
            });

            $("#btnEvaluation").click(function () {
                $("#score").raty({});//将id为score的<span>标签转换为星星组件
                //弹出编辑框
                $("#dlgEvaluation").modal("show");
            });
            //短评对话框的提交按钮
            $("#btnSubmit").click(function () {
                var content = $("#content").val();//获取评论内容，<input>标签，不是data-*定义的属性，用.val
                var score = $("#score").raty("score");//获取评分
                /*$.trim()用于删掉前后空格,如果删掉后content为空或评分为0*/
                if (score == 0 || $.trim(content) == "") {
                    return;//直接返回终止方法以中断评论的提交
                }
                $.ajax({
                    url: "/evaluate",
                    data: {memberId:${loginMember.memberId}, bookId:${book.bookId}, content: content, score: score},
                    type: "post",
                    dataType: "json",
                    success: function (json) {
                        if (json.code == "0") {
                            window.location.reload();//刷新页面
                        }
                    }
                });

            });

            //评论点赞
            $("*[data-evaluation-id]").click(function () {
                var evaluationId = $(this).data("evaluation-id");
                $.post("/enjoy", {evaluationId: evaluationId}, function (json) {
                    if (json.code == 0) {
                        /*按照上下级进行选择，选择data-evaluation-id 下的span标签(即点赞数量)
                        * .text()表示显示文本内容*/
                        $("*[data-evaluation-id='" + evaluationId + "'] span").text(json.evaluation.enjoy);
                    }

                }, "json");

            });
            </#if>
        });
    </script>
</head>
<body>
<!--<div style="width: 375px;margin-left: auto;margin-right: auto;">-->
<div class="container ">
    <nav class="navbar navbar-light bg-white shadow mr-auto">
        <ul class="nav">
            <li class="nav-item">
                <a href="/">
                    <img src="/images/gamest_logo.png" class="mt-1"
                         style="width: 120px">
                </a>
            </li>

        </ul>
    </nav>


    <div class="container mt-2 p-2 m-0" style="background-color:rgb(127, 125, 121)">
        <div class="row">
            <div class="col-4 mb-2 pl-0 pr-0">
                <img style="width: 110px;height: 160px"
                     src="${book.cover}">
            </div>
            <div class="col-8 pt-2 mb-2 pl-0">
                <h6 class="text-white">${book.bookName}</h6>
                <div class="p-1 alert alert-warning small" role="alert">
                    ${book.subTitle}
                </div>
                <p class="mb-1">
                    <span class="text-white-50 small">${book.author}</span>
                </p>
                <div class="row pl-1 pr-2">
                    <div class="col-6 p-1">
                        <button type="button" data-read-state="1" class="btn btn-light btn-sm w-100">
                            <img style="width: 1rem;" class="mr-1"
                                 src="https://img3.doubanio.com/f/talion/cf2ab22e9cbc28a2c43de53e39fce7fbc93131d1/pics/card/ic_mark_todo_s.png"/>想看
                        </button>
                    </div>
                    <div class="col-6 p-1">
                        <button type="button" data-read-state="2" class="btn btn-light btn-sm  w-100">
                            <img style="width: 1rem;" class="mr-1"
                                 src="https://img3.doubanio.com/f/talion/78fc5f5f93ba22451fd7ab36836006cb9cc476ea/pics/card/ic_mark_done_s.png"/>看过
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="row" style="background-color: rgba(0,0,0,0.1);">
            <div class="col-2"><h2 class="text-white">${book.evaluationScore}</h2></div>
            <div class="col-5 pt-2">
                <span class="stars" data-score="${book.evaluationScore}"></span>
            </div>
            <div class="col-5  pt-2"><h5 class="text-white">${book.evaluationQuantity}人已评</h5></div>
        </div>
    </div>
    <div class="row p-2 description">
        ${book.description}
    </div>


    <div class="alert alert-primary w-100 mt-2" role="alert">短评
        <button type="button" id="btnEvaluation" class="btn btn-success btn-sm text-white float-right"
                style="margin-top: -3px;">
            写短评
        </button>
    </div>
    <div class="reply pl-2 pr-2">
        <#list evaluationList as  e>
            <div>
                <div>
                    <span class="pt-1 small text-black-50 mr-2">${e.createTime?string('MM-dd')}</span>
                    <span class="mr-2 small pt-1">${e.member.nickname}</span>
                    <span class="stars mr-2" data-score="${e.score}"></span>

                    <#--短评点赞按钮-->
                    <button type="button" data-evaluation-id="${e.evaluationId}"
                            class="btn btn-success btn-sm text-white float-right" style="margin-top: -3px;">
                        <img style="width: 24px;margin-top: -5px;" class="mr-1"
                             src="https://img3.doubanio.com/f/talion/7a0756b3b6e67b59ea88653bc0cfa14f61ff219d/pics/card/ic_like_gray.svg"/>
                        <span>${e.enjoy}</span>
                    </button>
                </div>

                <div class="row mt-2 small mb-3">
                    ${e.content}
                </div>
                <hr/>
            </div>
        </#list>
    </div>
</div>


<!-- Modal bootstrap的对话框-->
<div class="modal fade" id="exampleModalCenter" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
                您需要登录才可以操作哦~
            </div>
            <div class="modal-footer">
                <a href="/login.html" type="button" class="btn btn-primary">去登录</a>
            </div>
        </div>
    </div>
</div>

<!-- Modal -->
<div class="modal fade" id="dlgEvaluation" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <h6>为《${book.bookName}》写短评</h6>
                <form id="frmEvaluation">
                    <div class="input-group  mt-2 ">
                        <span id="score"></span>
                    </div>
                    <div class="input-group  mt-2 ">
                        <input type="text" id="content" name="content" class="form-control p-4" placeholder="这里输入短评">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" id="btnSubmit" class="btn btn-primary">提交</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>