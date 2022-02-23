<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Gamest书评网</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="/resources/bootstrap/bootstrap.css">
    <link rel="stylesheet" href="/resources/raty/lib/jquery.raty.css">
    <script src="/resources/jquery.3.3.1.min.js"></script>
    <script src="/resources/bootstrap/bootstrap.min.js"></script>
    <script src="/resources/art-template.js"></script>
    <script src="/resources/raty/lib/jquery.raty.js"></script>
    <style>
        .highlight {
            color: #ffcd0c !important;
        }

        a:active {
            text-decoration: none !important;
        }
    </style>


    <style>
        .container {
            padding: 0px;
            margin: 0px;
        }

        .row {
            padding: 0px;
            margin: 0px;
        }

        .col- * {
            padding: 0px;
        }
    </style>
    <#--定义js模板,type说明script中内容的类型,id设置为tpl-->
    <script type="text/html" id="tpl">
        <#--看ajax返回数据的时候的属性名和{{}}里的一致-->
        <a href="/book/{{bookId}}" style="color: inherit"><#--每一块超链接都对应了图书的详细信息-->
            <div class="row mt-2 book">
                <div class="col-4 mb-2 pr-2">
                    <#--图书封面-->
                    <img class="img-fluid" src="{{cover}}">
                </div>
                <div class="col-8  mb-2 pl-0">
                    <h5 class="text-truncate">{{bookName}}</h5><#--图书名称-->
                    <#--作者-->
                    <div class="mb-2 bg-light small  p-2 w-100 text-truncate">{{author}}</div>

                    <#--图书子标题-->
                    <div class="mb-2 w-100">{{subTitle}}</div>

                    <p><#--评分信息-->
                        <span class="stars" data-score="{{evaluationScore}}" title="gorgeous"></span>
                        <span class="mt-2 ml-2">{{evaluationScore}}</span>
                        <span class="mt-2 ml-2">{{evaluationQuantity}}人已评</span>
                    </p>
                </div>
            </div>
        </a>
    </script>

    <script>
        /*设置存储评价图片的目录*/
        $.fn.raty.defaults.path = "/resources/raty/lib/images";

        /*定义一个加载更多函数 标志位isReset表示重置*/
        function loadMore(isReset) {
            if (isReset == true) {
                $("#bookList").html("");//清空原有的页面数据;
                $("#nextPage").val(1);//下一页的数据设置为1；
            }
            /*获取隐藏域的值*/
            var nextPage = $("#nextPage").val();
            var category = $("#categoryId").val();
            var order = $("#order").val();
            $.ajax({
                url: "/books",
                /*data里的参数名要和controller中的一致,
                * JavaScript在发送请求是会默认将其看成字符串"categoryId"*/
                data: {p: nextPage, categoryId: category, order: order},
                type: "get",
                dataType: "json",
                success: function (json) {
                    console.info(json);
                    var list = json.records;
                    for (var i = 0; i < list.length; i++) {
                        var book = json.records[i];/*不止一本书*/
                        var html = template("tpl", book);
                        $("#bookList").append(html);
                    }
                    $(".stars").raty({readOnly: true});
                    /*如果当前页小于总页数,则显示点击更多按钮*/
                    if (json.current < json.pages) {
                        $("#nextPage").val(parseInt(json.current) + 1);
                        $("#btnMore").show();//显示组件
                        $("#divNoMore").hide();//隐藏组件
                    } else {
                        $("#btnMore").hide();//显示组件
                        $("#divNoMore").show();//隐藏组件
                    }
                }
            })
        }

        $(function () {
            /*重构之前的代码
            $.ajax({
                url : "/books",
                data : {p:1},
                type : "get",
                dataType : "json",
                success: function (json) {
                    console.info(json);
                    var list = json.records;
                    for (var i = 0; i < list.length; i++) {
                        var book = json.records[i];
                        //字符串的拼接方法var html = "<li>" + book.bookName + "</li>";
                        /*利用script Id选择器选中存放图书信息的div,然后利用append
                        * 方法将html追加进去*!/
                        //将数据结合tpl模板，生成html
                        var html = template("tpl", book);
                        //console.info(html);
                        $("#bookList").append(html);
                    }
                    //显示星型评价组件,并设置只读
                    $(".stars").raty({readOnly:true});
                }
            })*/
            loadMore(true);//设置为true表示需要重置

        });
        //绑定加载更多按钮单击事件
        $(function () {
            $("#btnMore").click(function () {
                loadMore();
            });
            /*.category是进行类选择*/
            $(".category").click(function () {
                $(".category").removeClass("highlight");//移除原先所拥有的高亮css
                $(".category").addClass("text-black-50");//设置为灰色
                $(this).addClass("highlight");//设置当前点击的为高亮
                /*$(this).data 对应自定义属性data后边的值*/
                $("#categoryId").val($(this).data("category"));
                loadMore(true);
            });

            //对排序的进行设置
            $(".order").click(function () {
                $(".order").removeClass("highlight");//移除原先所拥有的高亮css
                $(".order").addClass("text-black-50");//设置为灰色
                $(this).addClass("highlight");//设置当前点击的为高亮
                /*进行数据的绑定*/
                $("#order").val($(this).data("order"));
                loadMore(true);
            });
        });

    </script>

</head>
<body>
<div class="container">
    <nav class="navbar navbar-light bg-white shadow mr-auto">
        <ul class="nav">
            <li class="nav-item">
                <a href="/">
                    <img src="./images/gamest_logo.png" class="mt-1" style="width: 120px">
                </a>
            </li>

        </ul>
        <#if loginMember??> <#--??表示前面的属性存在 ,然后显示登录用户的昵称-->
            <h6 class="mt-1">
                <img style="width: 2rem;margin-top: -5px" class="mr-1"
                     src="./images/user_icon.png">${loginMember.nickname}
            </h6>
        <#else>
            <a href="/login.html" class="btn btn-light btn-sm">
                <img style="width: 2rem;margin-top: -5px" class="mr-1" src="./images/user_icon.png">登录
            </a>
        </#if>
    </nav>
    <div class="row mt-2">


        <div class="col-8 mt-2">
            <h4>热评好书推荐</h4>
        </div>

        <div class="col-8 mt-2">
            <span data-category="-1" style="cursor: pointer" class="highlight  font-weight-bold category">全部</span>
            <#--循环遍历，用category来得到categoryList的单个值-->
            <#list categoryList as category>
                <a style="cursor: pointer" data-category="${category.categoryId}"
                   class="text-black-50 font-weight-bold category">${category.categoryName}</a>
            <#--这里的_has_next表示是否还有其他元素，如果有则执行if块中的语句-->
                <#if category_has_next>|</#if>
            </#list>
        </div>

        <div class="col-8 mt-2">
            <span data-order="quantity" style="cursor: pointer"
                  class="order highlight  font-weight-bold mr-3">按热度</span>

            <span data-order="score" style="cursor: pointer"
                  class="order text-black-50 mr-3 font-weight-bold">按评分</span>
        </div>
    </div>
    <#--隐藏域，用来传递给后台的数据，有下一页、分类id、排序方式-->
    <div class="d-none">
        <input type="hidden" id="nextPage" value="1">
        <input type="hidden" id="categoryId" value="-1">
        <input type="hidden" id="order" value="quantity">
    </div>

    <div id="bookList"><#--用于循环产生一块一块的超链接<a>-->

    </div>
    <button type="button" id="btnMore" data-next-page="1" class="mb-5 btn btn-outline-primary btn-lg btn-block">
        点击加载更多...
    </button>
    <div id="divNoMore" class="text-center text-black-50 mb-5" style="display: none;">没有其他数据了</div>
</div>

</body>
</html>