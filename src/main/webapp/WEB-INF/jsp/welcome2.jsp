<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="listDiffs" value="${listDiffs}"/>
<c:set var="providers" value="${mapProviders}"/>
<c:set var="listAverages" value="${listAverages}"/>
<c:set var="message" value="${message}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Welcome page</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <!-- /container -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="${contextPath}/resources/js/bootstrap.min.js"></script>

    <script src= "https://cdn.zingchart.com/zingchart.min.js"></script>
    <script> zingchart.MODULESDIR = "https://cdn.zingchart.com/modules/";
    ZC.LICENSE = ["569d52cefae586f634c54f86dc99e6a9","ee6b7db5b51705a13dc2339db3edaf6d"];</script>
</head>
<body>
<div class="container">

    <div id="myChart" style="height:100%;width:100%;"></div>


</div>
</body>
</html>

<script>


    var myConfig = {
        type: "hbar",
        backgroundColor : "#2A2B3A",
        tooltip:{visible:false},
        scaleX : {
            lineColor : "transparent",
            tick : {
                visible : false
            },
            labels : [ "Dev", "R&D", "Testing"],
            item : {
                fontColor : "#e8e8e8",
                fontSize : 16
            }
        },
        scaleY :{
            visible : false,
            lineColor : "transparent",
            guide : {
                visible : false
            },
            tick : {
                visible : false
            }
        },
        plotarea : {
            marginLeft : "80",
            marginTop : "30",
            marginBottom : "30"
        },
        plot : {
            stacked : true,
            barsSpaceLeft : "20px",
            barsSpaceRight : "20px",
            valueBox : {
                visible : true,
                text : "%v0%",
                fontColor : "#2A2B3A",
                fontSize: 14
            },
            tooltip : {
                borderWidth : 0,
                borderRadius : 2
            },
            animation:{
                effect:3,
                sequence:3,
                method:3
            }
        },
        series : [
            {
                values : [3,2,6],
                borderRadius : "50px 0px 0px 50px",
                backgroundColor : "#E71D36",
                rules : [
                    {
                        rule : "%i === 0",
                        backgroundColor : "#E71D36"
                    },
                    {
                        rule : "%i === 1",
                        backgroundColor : "#2EC4B6"
                    },
                    {
                        rule : "%i === 2",
                        backgroundColor : "#FF9F1C"
                    }
                ]
            },
            {
                values : [7,8,4],
                borderRadius : "0px 50px 50px 0px",
                backgroundColor : "#E71D36",
                //alpha : 0.8,
                rules : [
                    {
                        rule : "%i === 0",
                        backgroundColor : "#e85d6f"
                    },
                    {
                        rule : "%i === 1",
                        backgroundColor : "#90eae2"
                    },
                    {
                        rule : "%i === 2",
                        backgroundColor : "#f7be70"
                    }
                ]
            }
        ]
    };

    zingchart.render({
        id : 'myChart',
        data : myConfig,
        height: '100%',
        width: '100%'
    });


</script>
