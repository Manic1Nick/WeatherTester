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

    <h2>Select action:</h2>

    <p>
        <a href="#" onclick="updateForecasts()">Update forecasts</a>
    </p>
    <p>
        <a href="#" onclick="updateActuals()">Update actual weather</a>
    </p>
    <p>
        <a href="#" onclick="getDay()">Show analysis by date (today default)</a>
    </p>
    <p>
        <a href="${contextPath}/forecasts/get/all">Get total analysis</a>
    </p>
    <p>
        <a href="${contextPath}/welcome2">Get average diffs</a>
    </p>
    <p>
        <a href="#" onclick="updateAverageDiffForAllDays()">Update all average diffs (for admin)</a>
    </p>

    <%--MODAL add & update--%>
    <div class="modal fade" id="openModal" role="dialog">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title" id="modalName"><%--content--%></h4>
                </div>
                <div class="modal-body" id="modalData">
                    <p><strong><%--content--%></strong></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <div id="chart7Days"></div>

    <div id="chartAverages"></div>

</div>
</body>
</html>

<script>
    function updateForecasts() {
        $.ajax({
            url: '${contextPath}/forecasts/get/new',
            type: 'GET'
        }).success(function (resp) {
            $("#modalName").html("Update forecasts info:");
            $("#modalData").html(resp);
            $("#openModal").modal('show');
        }).error(function (resp) {
            $("#modalName").html("Error updating forecasts:");
            $("#modalData").html(resp);
            $("#openModal").modal('show');
        })
    }

    function updateActuals() {
        $.ajax({
            url: '${contextPath}/actuals/get/new',
            type: 'GET'
        }).success(function (resp) {
            $("#modalName").html("Update actuals info:");
            $("#modalData").html(resp);
            $("#openModal").modal('show');
        }).error(function (resp) {
            $("#modalName").html("Error updating actuals:");
            $("#modalData").html(resp);
            $("#openModal").modal('show');
        })
    }

    function getDay(date) {
        $.ajax({
            url: '${contextPath}/forecasts/find/ids',
            type: 'GET',
            data: {
                date:date
            }
        }).success(function (resp) {
            if (resp.indexOf(";") == -1) {
                $("#modalName").html("Error getting info:");
                $("#modalData").html(resp);
                $("#openModal").modal('show');

            } else {
                window.location.assign("${contextPath}/forecasts/show/day?ids=" + resp);
            }

        }).error(function (resp) {
            $("#modalName").html("Error getting info:");
            $("#modalData").html(resp);
            $("#openModal").modal('show');
        })
    }

    function updateAverageDiffForAllDays() {
        $.ajax({
            url: '${contextPath}/update/all/average_diff',
            type: 'GET'
        }).success(function (resp) {
            $("#modalName").html("Updating average diff info:");
            $("#modalData").html(resp);
            $("#openModal").modal('show');

        }).error(function (resp) {
            $("#modalName").html("Error getting info:");
            $("#modalData").html(resp);
            $("#openModal").modal('show');
        })
    }
</script>

<script>
    var config7Days = {
        "type": "heatmap",
        "backgroundImage":"${contextPath}/resources/images/sunny-sky.jpg",
        "background-fit":'xy',
        "title":{
            "text":"Most accurate providers of last 7 days",
            "font-color":"#024567"
        },
        "plot":{
            "tooltip":{
                "text":"This provider <br>has best result <br>%data-weather% mistake!",
                "background-color":"white",
                "alpha":0.9,
                "font-family":"Arial",
                "font-color":"#006699",
                "font-size":13
            },
            "aspect":"none",
            "background-color":"none",
            "background-repeat":false,
            "rules":[
                {
                    "rule":"%v == 0",
                    "background-image":"${contextPath}/resources/images/small_openweathermap.png"
                },
                {
                    "rule":"%v == 1",
                    "background-image":"${contextPath}/resources/images/small_wu-logo.jpg",
                },
                {
                    "rule":"%v == 2",
                    "background-image":"${contextPath}/resources/images/small_Foreca_logo_Label_Black.png"
                }
            ],
            "hover-state":{
                "background-color":"#eff3f4"
            }
        },
        "scale-x":{
            "labels":[
                <c:forEach items="${listDiffs}" var="diff" varStatus="status">
                    "${diff.date}"<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            ],
            "line-color":"none",
            "guide":{
                "line-style":"solid",
                "line-color":"#FFF"
            },
            "tick":{
                "visible":false
            },
            "item":{
                "font-color":"#024567",
                "font-size":13
            }
        },
        "scale-y":{
            "labels":["Last 7 days"],
            "line-color":"none",
            "guide":{
                "line-style":"solid",
                "line-color":"#FFF"
            },
            "tick":{
                "visible":false
            },
            "item":{
                "font-color":"#024567",
                "font-size":13
            }
        },
        "plotarea":{
            "margin-left":"dynamic",
            "border":'1px solid #FFF'
        },
        "series": [
            {
                "values": [
                    <c:forEach items="${listDiffs}" var="diff" varStatus="status">
                        <c:out value="${providers[diff.provider]}" /><c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                ],
                "data-weather":[
                    <c:forEach items="${listDiffs}" var="diff" varStatus="status">
                        "${diff.averageDayDiff}"<c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                ],
                "dates": [
                    <c:forEach items="${listDiffs}" var="diff" varStatus="status">
                        "${diff.date}"<c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                ]
            }
        ]
    };

    zingchart.render({
        id : 'chart7Days',
        data : config7Days,
        height: 250,
        width: 725
    });

    zingchart.bind("chart7Days","node_click",function(p){
        var series = config7Days.series;
        var date = series[0].dates[p.nodeindex];
        getDay(date);
    });
</script>

<script>
    var configAverages = {
        type: "hbar",
        backgroundColor : "none",
        tooltip:{visible:false},
        scaleX : {
            lineColor : "transparent",
            tick : {
                visible : false
            },
            labels : [
                <c:forEach items="${listAverages}" var="diff" varStatus="status">
                    "${diff.provider}"<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            ],
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
                fontSize: 28
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
                values : [
                    <c:forEach items="${listAverages}" var="diff" varStatus="status">
                        ${diff.value}<c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                ],
                borderRadius : "50px 0px 0px 50px",
                backgroundColor : "#E71D36",
                rules : [
                    {
                        rule : "%i === 0",
                        backgroundColor : "#E71D36"
                        //backgroundImage : "${contextPath}/resources/images/row_openweathermap1"
                        //background-image: url('images/checked.png')
                    },
                    {
                        rule : "%i === 1",
                        backgroundColor : "#2EC4B6"
                        //backgroundImage : "${contextPath}/resources/images/row_wu-color3"
                    },
                    {
                        rule : "%i === 2",
                        backgroundColor : "#FF9F1C"
                        //backgroundImage : "${contextPath}/resources/images/row_foreca_logo"
                    }
                ]
            },
            {
                values : [
                    <c:forEach items="${listAverages}" var="diff" varStatus="status">
                        100-${diff.value}<c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                ],
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
        id : 'chartAverages',
        data : configAverages,
        height: '100%',
        width: '100%'
    });

</script>