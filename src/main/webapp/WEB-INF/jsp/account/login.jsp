<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>滑动拼图验证码</title>
    <style>
        .container {
            width: 310px;
            margin: 100px auto;
        }
        input {
            display: block;
            width: 290px;
            line-height: 40px;
            margin: 10px 0;
            padding: 0 10px;
            outline: none;
            border:1px solid #c8cccf;
            border-radius: 4px;
            color:#6a6f77;
        }
        #msg {
            width: 100%;
            line-height: 40px;
            font-size: 14px;
            text-align: center;
        }
        a:link,a:visited,a:hover,a:active {
            margin-left: 100px;
            color: #0366D6;
        }

        .my_container{
            width: 400px;
            margin: 250px auto;
        }

    </style>
    <link rel="stylesheet" href="/css/jigsaw.css">
</head>
<body>
<div class="container">
    <input value="admin" readonly/>
    <input type="password" value="1234567890" readonly/>
    <div id="captcha"></div>
    <div id="msg"></div>
</div>

<div class="my_container">
    <div id="my_captcha"></div>
    <div id="my_msg"></div>
</div>
<%--<script>--%>
<%--    if (!window.jigsaw) {--%>
<%--        document.write('<script type="module" src="/js/jigsaw.js"><\/script>')--%>
<%--    }--%>
<%--</script>--%>
<script src="/js/jigsaw.js"></script>
<script src="/js/my_jigsaw.js"></script>
<script>
    jigsaw.init({
        el: document.getElementById('captcha'),
        onSuccess: function() {
            document.getElementById('msg').innerHTML = '登录成功！'
        },
        onFail: cleanMsg,
        onRefresh: cleanMsg
    })
    function cleanMsg() {
        document.getElementById('msg').innerHTML = ''
    }

    var my_jigsaw = new my_Jigsaw({
        el: document.getElementById('my_captcha'),
        option:{
            width:400,
            height:250,
        },

        onSuccess: function() {
            document.getElementById('my_msg').innerHTML = '登录成功！'
        },
        onFail: cleanMyMsg,
        onRefresh: cleanMyMsg
    });

    function cleanMyMsg() {
        document.getElementById('my_msg').innerHTML = ''
    }
</script>
</body>
</html>