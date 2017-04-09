<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<%--previous 7 days--%>
<c:set var="listDiffs" value="${listDiffs}"/>

<%--rating mistakes--%>
<c:set var="listAverages" value="${listAverages}"/>

<%--next 7 days--%>
<c:set var="mapForecasts" value="${mapForecasts}"/>
<c:set var="dates" value="${dates}"/>

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

    <div id="menu" style="text-align:center;">
        <p>
            <a href="#" onclick="updateForecasts()">Update forecasts</a>
            <a href="#" onclick="updateActuals()">Update actual weather</a>
        </p>

        <%--
        <p>
            <a href="${contextPath}/forecasts/get/all">Get total analysis</a>
        </p>
        <p>
            <a href="#" onclick="updateAverageDiffForAllDays()">Update all average diffs (for admin)</a>
        </p>
        --%>
    </div>

    <div id="actions">
        <p>
            <button id="flipLast7" type="button" class="btn">Last 7 days</button>
            <button id="flipRating" type="button" class="btn">Rating mistakes</button>
            <button id="flipNext7" type="button" class="btn">Next 7 days</button>
        </p>
    </div>

    <%--MODAL no add or update--%>
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

    <%--MODAL add & update with update Welcome--%>
    <div class="modal fade" id="openModalUpdated" role="dialog">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title" id="modalUpdatedName"><%--content--%></h4>
                </div>
                <div class="modal-body" id="modalUpdatedData">
                    <p><strong><%--content--%></strong></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal"
                        onclick="updateWelcome()">Close</button>
                </div>
            </div>
        </div>
    </div>

    <div class="loader" id="loader"></div>
    <div class="chart" id="chart7Days"></div>
    <div class="chart" id="forecasts"></div>

    <div class="chart" id="ratings">
        <c:forEach items="${listAverages}" var="avDiff" varStatus="loop">
            <table class="table">
                <tbody>
                    <tr id="flipDetails${loop.index}">
                        <td id="provider" data-toggle="tooltip" title="Click for open details">
                            <img src="${contextPath}/resources/images/${avDiff.provider.rowLogo}">
                        </td>
                        <td id="bar">
                            <div class="rating">
                                Average mistake:
                                    <div class="ratings ${avDiff.provider}">${avDiff.value}%</div>
                                for ${avDiff.days} days
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>

            <div id="details${loop.index}" style="display:none">
                    ${avDiff.details}
            </div>
        </c:forEach>
    </div>

</div>
</body>
</html>

<script>
    $(document).ready(function(){
        $("#flipLast7").click(function(){
            $("#ratings").slideUp("slow");
            $("#forecasts").slideUp("slow");

            $("#chart7Days").slideToggle("slow");
        });
        $("#flipRating").click(function(){
            $("#chart7Days").slideUp("slow");
            $("#forecasts").slideUp("slow");

            $("#ratings").slideToggle("slow");
        });
        $("#flipNext7").click(function(){
            $("#chart7Days").slideUp("slow");
            $("#ratings").slideUp("slow");

            $("#forecasts").slideToggle("slow");
        });

        $("#flipDetails0").click(function(){
            $("#details0").slideToggle("slow");
        });
        $("#flipDetails1").click(function(){
            $("#details1").slideToggle("slow");
        });
        $("#flipDetails2").click(function(){
            $("#details2").slideToggle("slow");
        });
    });

    function openModalWithUpdate(name, data) {
        $('#loader').hide();
        $("#modalUpdatedName").html(name);
        $("#modalUpdatedData").html(data);
        $("#openModalUpdated").modal('show');
    }

    function openModalWithoutUpdate(name, data) {
        $('#loader').hide();
        $("#modalName").html(name);
        $("#modalData").html(data);
        $("#openModal").modal('show');
    }

    function updateForecasts() {
        $('#loader').show();
        $.ajax({
            url: '${contextPath}/forecasts/get/new',
            type: 'GET'
        }).success(function (resp) {
            if (resp.indexOf("There is no") == -1) {
                openModalWithUpdate("Update forecasts info:", resp);
            } else {
                openModalWithoutUpdate("Update forecasts info:", resp);
            }
        }).error(function (resp) {
            openModalWithoutUpdate("Error updating forecasts:", resp);
        })
    }

    function updateActuals() {
        $('#loader').show();
        $.ajax({
            url: '${contextPath}/actuals/get/new',
            type: 'GET'
        }).success(function (resp) {
            if (resp.indexOf("There is no") == -1) {
                openModalWithUpdate("Update actuals info:", resp);
            } else {
                openModalWithoutUpdate("Update actuals info:", resp);
            }
        }).error(function (resp) {
            openModalWithoutUpdate("Error updating forecasts:", resp);
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
                openModalWithoutUpdate("Error getting info:", resp);
            } else {
                window.location.assign("${contextPath}/forecasts/show/day?ids=" + resp);
            }
        }).error(function (resp) {
            openModalWithoutUpdate("Error getting info:", resp);
        })
    }

    function updateAverageDiffForAllDays() {
        $('#loader').show();
        $.ajax({
            url: '${contextPath}/update/all/average_diff',
            type: 'GET'
        }).success(function (resp) {
            openModalWithUpdate("Updating average diff info:", resp);

        }).error(function (resp) {
            openModalWithoutUpdate("Error getting info:", resp);
        })
    }

    function updateWelcome() {
        window.location.assign("${contextPath}/welcome");
    }
</script>

<script>
    var config7Days = {
        "type": "heatmap",
        "backgroundColor":"transparent",
        "background-fit":'xy',
        "title":{
            "text":"Most accurate providers of last 7 days",
            "font-color":"#000000"
        },
        "plot":{
            "tooltip":{
                "text":"Best rating <br>of this day is <br>%data-weather% mistake.<br>Click for analysis<br>of this day",
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
                <c:forEach items="${listDiffs}" var="diff" varStatus="status">
                {
                    "rule":"%v == ${diff.provider.number}",
                    "background-image":"${contextPath}/resources/images/${diff.provider.logo}"
                }<c:if test="${!status.last}">,</c:if>
                </c:forEach>
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
                "font-color":"#000000",
                "font-size":13
            }
        },
        "scale-y":{
            /*"labels":["Last 7 days"],*/
            "line-color":"none",
            "guide":{
                "line-style":"solid",
                "line-color":"#FFF"
            },
            "tick":{
                "visible":false
            },
            "item":{
                "font-color":"#000000",
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
                        <c:out value="${diff.provider.number}" /><c:if test="${!status.last}">,</c:if>
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
    var configForecasts = {
        type: "range",
        backgroundColor : "transparent",
        title : {
            text : "Temperature of next 7 days",
            backgroundColor : "transparent",
            fontColor : "#000"
        },
        legend : {
            layout : "x4",
            verticalAlign:'bottom',
            align:'center',
            shadow : 0,
            borderColor : "transparent",
            backgroundColor: 'transparent'
        },
        plot : {
            aspect : "spline",
            marker : {
                visible : false
            },
            lineWidth : 0,
            alphaArea : 1,
            hoverState:{
                backgroundColor:'none'
            }
        },
        tooltip:{visible:false},
        scaleY : {
            label:{
                text:'Celsius',
                fontColor: "#000000"
            },
            lineWidth : 1,
            tick : {
                lineWidth : "1"
            },
            item : {
                offsetX : "0px",
                textAlign : "left",
                fontColor: "#000000"
            }
        },
        guide:{
            marker:{
                type:'triangle',
                size:7
            },
            plotLabel:{
                headerText:'%kt',
                text:'<span style="color:%color">%t</span><span style="color:%color"> Min: %node-min-value  Max: %node-max-value</span> ',
                fontSize:15,
                borderRadius:5,
                fontColor:'#FFF',
                backgroundColor:'#000'
            }
        },
        scaleX :{
            label:{
                text:'Date',
                fontColor: "#000000"
            },
            lineWidth : 1,
            tick : {
                placement : "outer",
                size : "10px",
                lineWidth : "1"
            },
            guide : {
                lineWidth : 1,
                lineStyle : "solid",
                alpha : 1
            },
            item : {
                offsetX : "0px",
                textAlign : "left",
                fontColor: "#000000"
            },
            labels : [
                <c:forEach items="${dates}" var="date" varStatus="status">
                    "${date}"<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            ]
        },
        series : [
            <c:forEach items="${mapForecasts}" var="map" varStatus="status">
                {
                    values : [
                        <c:forEach items="${map.value}" var="forecast" varStatus="status">
                            [${forecast.tempMin},${forecast.tempMax}]<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    ],
                    backgroundColor : "${map.key.color}",
                    lineColor : "${map.key.color}",
                    alphaArea : "0.6",
                    text:'${map.key.name}'
                }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ]
    };

    zingchart.render({
        id : 'forecasts',
        data : configForecasts,
        height: 500,
        width: 725
    });


</script>

<style>
    body {
        background-image: url("${contextPath}/resources/images/Sky_Background-56.jpg");
        background-repeat:no-repeat;
        background-size:cover;
    }

    a {
        text-decoration: none;
        display: inline-block;
        padding: 8px 16px;
        color: black;
    }

    a:hover {
        background-color: #ddd;
        color: black;
    }

    * {box-sizing: border-box}

    #actions {
        text-align:center;
    }

    #chart7Days {
        text-align:center;
        display:none;
        position: absolute;
        left: 20%;
        top: 25%;
    }

    #ratings {
        text-align:center;
        display:none;
    }

    .ratings {
        text-align: right;
        padding-right: 20px;
        line-height: 40px;
        color: white;
    }

    <c:forEach items="${listAverages}" var="avDiff">
        .${avDiff.provider} {
            width: ${avDiff.value}%;
            background-color: ${avDiff.provider.color};
        }
    </c:forEach>

    #forecasts {
        text-align:center;
        display:none;
        position: absolute;
        left: 20%;
        top: 25%;
    }

    #bar{
        text-align:left;
        width:50%;
    }

    #provider {
        text-align: right;
        width:50%;
    }
</style>

<style>
    .loader {
        position: absolute;
        left: 50%;
        top: 50%;
        border: 16px solid #f3f3f3;
        border-radius: 50%;
        border-top: 16px solid #3498db;
        width: 120px;
        height: 120px;
        -webkit-animation: spin 2s linear infinite;
        animation: spin 2s linear infinite;
        display: none;
    }

    @-webkit-keyframes spin {
        0% { -webkit-transform: rotate(0deg); }
        100% { -webkit-transform: rotate(360deg); }
    }

    @keyframes spin {
        0% { transform: rotate(0deg); }
        100% { transform: rotate(360deg); }
    }
</style>