<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="message" value="${message}"/>
<c:set var="providers" value="${providers}"/>
<c:set var="avTotal" value="${averagesTotal}"/>
<c:set var="temps1" value="${temps1}"/>
<c:set var="temps2" value="${temps2}"/>
<c:set var="temps3" value="${temps3}"/>
<c:set var="items" value="${items}"/>
<c:set var="values" value="${values}"/>

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

    <script src="https://cdn.zingchart.com/zingchart.min.js"></script>
    <script> zingchart.MODULESDIR = "https://cdn.zingchart.com/modules/";
    ZC.LICENSE = ["569d52cefae586f634c54f86dc99e6a9","ee6b7db5b51705a13dc2339db3edaf6d"];</script>
</head>
<body>
<div class="container">
    <p>
        <a href="${contextPath}/welcome" onclick="updateForecasts()">Return to main menu</a>
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

    <div id='myChart'></div>

</div>
</body>
</html>

<script>
    zingchart.THEME="classic";
    var myConfig = {
        "background-color":"#ecf2f6",
        "graphset":[
            {
                "type":"bar",
                "background-color":"#fff",
                "border-color":"#dae5ec",
                "border-width":"1px",
                "height":"30%",
                "width":"96%",
                "x":"2%",
                "y":"3%",
                "title":{
                    "margin-top":"7px",
                    "margin-left":"9px",
                    "font-family":"Arial",
                    "text":"PROVIDERS MISTAKES, %",
                    "background-color":"none",
                    "shadow":0,
                    "text-align":"left",
                    "font-size":"11px",
                    "font-weight":"bold",
                    "font-color":"#707d94"
                },
                "scale-y":{
                    "values":"0:100:30",
                    "max-ticks":4,
                    "max-items":4,
                    "line-color":"none",
                    "tick":{
                        "visible":false
                    },
                    "item":{
                        "font-color":"#8391a5",
                        "font-family":"Arial",
                        "font-size":"10px",
                        "padding-right":"5px"
                    },
                    "guide":{
                        "rules":[
                            {
                                "rule":"%i == 0",
                                "line-width":0
                            },
                            {
                                "rule":"%i > 0",
                                "line-style":"solid",
                                "line-width":"1px",
                                "line-color":"#d2dae2",
                                "alpha":0.4
                            }

                        ]
                    }
                },
                "scale-x":{
                    "items-overlap":true,
                    "max-items":9999,
                    "values":[
                        <c:forEach var="provider" items="${providers}" varStatus="status">
                            "${provider}"
                        <c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    ],
                    "offset-y":"1px",
                    "line-color":"#d2dae2",
                    "item":{
                        "font-color":"#8391a5",
                        "font-family":"Arial",
                        "font-size":"11px",
                        "padding-top":"2px"
                    },
                    "tick":{
                        "visible":false,
                        "line-color":"#d2dae2"
                    },
                    "guide":{
                        "visible":false
                    }
                },
                "plotarea":{
                    "margin":"45px 20px 38px 45px"
                },
                "plot":{
                    "bar-width":"33px",
                    "hover-state":{
                        "visible":false
                    },
                    "animation":{
                        "delay":500,
                        "effect":"ANIMATION_SLIDE_BOTTOM"
                    },
                    "tooltip":{
                        "font-color":"#fff",
                        "font-family":"Arial",
                        "font-size":"11px",
                        "border-radius":"6px",
                        "shadow":false,
                        "padding":"5px 10px",
                        "background-color":"#707e94"
                    }
                },
                "series":[
                    {
                        "values":[
                            <c:forEach var="average" items="${avTotal}" varStatus="status">
                                ${average.value}
                            <c:if test="${!status.last}">,</c:if>
                            </c:forEach>
                        ],
                        "styles":[
                            {
                                "background-color":"#4dbac0"
                            },
                            {
                                "background-color":"#25a6f7"
                            },
                            {
                                "background-color":"#ad6bae"
                            }/*,
                            {
                                "background-color":"#707d94"
                            },
                            {
                                "background-color":"#f3950d"
                            },
                            {
                                "background-color":"#e62163"
                            },
                            {
                                "background-color":"#4e74c0"
                            },
                            {
                                "background-color":"#9dc012"
                            }*/
                        ]
                    }
                ]
            },
            {
                "type":"hbar",
                "background-color":"#fff",
                "border-color":"#dae5ec",
                "border-width":"1px",
                "x":"2%",
                "y":"35.2%",
                "height":"63%",
                "width":"30%",
                "title":{
                    "margin-top":"7px",
                    "margin-left":"9px",
                    "text":"MISTAKES BY ITEMS, %",
                    "background-color":"none",
                    "shadow":0,
                    "text-align":"left",
                    "font-family":"Arial",
                    "font-size":"11px",
                    "font-color":"#707d94"
                },
                "scale-y":{
                    "line-color":"none",
                    "tick":{
                        "visible":false
                    },
                    "item":{
                        "visible":false
                    },
                    "guide":{
                        "visible":false
                    }
                },
                "scale-x":{
                    "values":[
                        <c:forEach var="item" items="${items}" varStatus="status">
                             "${item}"
                        <c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    ],
                    "line-color":"none",
                    "tick":{
                        "visible":false
                    },
                    "item":{
                        "width":200,
                        "text-align":"left",
                        "offset-x":206,
                        "offset-y":-12,
                        "font-color":"#8391a5",
                        "font-family":"Arial",
                        "font-size":"11px",
                        "padding-bottom":"8px"
                    },
                    "guide":{
                        "visible":false
                    }
                },
                "plot":{
                    "bars-overlap":"100%",
                    "bar-width":"12px",
                    "thousands-separator":",",
                    "tooltip":{
                        "font-color":"#ffffff",
                        "background-color":"#707e94",
                        "font-family":"Arial",
                        "font-size":"11px",
                        "border-radius":"6px",
                        "shadow":false,
                        "padding":"5px 10px"
                    },
                    "hover-state":{
                        "background-color":"#707e94"
                    },
                    "animation":{
                        "delay":500,
                        "effect":"ANIMATION_EXPAND_LEFT"
                    }
                },
                "plotarea":{
                    "margin":"50px 15px 10px 15px"
                },
                "series":[
                    {
                        "values":[
                            <c:forEach var="value" items="${values}" varStatus="status">
                                ${value}
                            <c:if test="${!status.last}">,</c:if>
                            </c:forEach>
                        ],
                        "z-index":2,
                        "styles":[
                            {
                                "background-color":"#4dbac0"
                            },
                            {
                                "background-color":"#4dbac0"
                            },
                            {
                                "background-color":"#4dbac0"
                            },
                            {
                                "background-color":"#4dbac0"
                            },
                            {
                                "background-color":"#4dbac0"
                            },
                            {
                                "background-color":"#4dbac0"
                            }
                        ],
                        "tooltip-text":"%node-value%"
                    },
                    {
                        "max-trackers":0,
                        "values":[100,100,100,100,100,100],
                        "data-rvalues":[
                            <c:forEach var="value" items="${values}" varStatus="status">
                                ${value}
                            <c:if test="${!status.last}">,</c:if>
                            </c:forEach>
                        ],
                        "background-color":"#d9e4eb",
                        "z-index":1,
                        "value-box":{
                            "visible":true,
                            "offset-y":"-12px",
                            "offset-x":"-54px",
                            "text-align":"right",
                            "font-color":"#8391a5",
                            "font-family":"Arial",
                            "font-size":"11px",
                            "text":"%data-rvalues%",
                            "padding-bottom":"8px"
                        }
                    }
                ]
            },
            {
                "type":"line",
                "background-color":"#fff",
                "border-color":"#dae5ec",
                "border-width":"1px",
                "width":"64%",
                "height":"63%",
                "x":"34%",
                "y":"35.2%",
                "title":{
                    "margin-top":"7px",
                    "margin-left":"12px",
                    "text":"FORECASTS FROM PROVIDERS",
                    "background-color":"none",
                    "shadow":0,
                    "text-align":"left",
                    "font-family":"Arial",
                    "font-size":"11px",
                    "font-color":"#707d94"
                },
                "plot":{
                    "animation":{
                        "delay":500,
                        "effect":"ANIMATION_SLIDE_LEFT"
                    }
                },
                "plotarea":{
                    "margin":"50px 25px 70px 46px"
                },
                "scale-y":{
                    "values":"0:30:5",
                    "line-color":"none",
                    "guide":{
                        "line-style":"solid",
                        "line-color":"#d2dae2",
                        "line-width":"1px",
                        "alpha":0.5
                    },
                    "tick":{
                        "visible":false
                    },
                    "item":{
                        "font-color":"#8391a5",
                        "font-family":"Arial",
                        "font-size":"10px",
                        "padding-right":"5px"
                    }
                },
                "scale-x":{
                    "line-color":"#d2dae2",
                    "line-width":"2px",
                    "values":["2017/03/31","2017/04/01","2017/04/02","2017/04/03","2017/04/04","2017/04/05","2017/04/06"],
                    "tick":{
                        "line-color":"#d2dae2",
                        "line-width":"1px"
                    },
                    "guide":{
                        "visible":false
                    },
                    "item":{
                        "font-color":"#8391a5",
                        "font-family":"Arial",
                        "font-size":"10px",
                        "padding-top":"5px"
                    }
                },
                "legend":{
                    "layout":"x4",
                    "background-color":"none",
                    "shadow":0,
                    "margin":"auto auto 15 auto",
                    "border-width":0,
                    "item":{
                        "font-color":"#707d94",
                        "font-family":"Arial",
                        "padding":"0px",
                        "margin":"0px",
                        "font-size":"9px"
                    },
                    "marker":{
                        "show-line":"true",
                        "type":"match",
                        "font-family":"Arial",
                        "font-size":"10px",
                        "size":4,
                        "line-width":2,
                        "padding":"3px"
                    }
                },
                "crosshair-x":{
                    "lineWidth":1,
                    "line-color":"#707d94",
                    "plotLabel":{
                        "shadow":false,
                        "font-color":"#000",
                        "font-family":"Arial",
                        "font-size":"10px",
                        "padding":"5px 10px",
                        "border-radius":"5px",
                        "alpha":1
                    },
                    "scale-label":{
                        "font-color":"#ffffff",
                        "background-color":"#707d94",
                        "font-family":"Arial",
                        "font-size":"10px",
                        "padding":"5px 10px",
                        "border-radius":"5px"
                    }
                },
                "tooltip":{
                    "visible":false
                },
                "series":[
                    {
                        "values":[
                            <c:forEach var="temp" items="${temps1}" varStatus="status">
                                ${temp}
                            <c:if test="${!status.last}">,</c:if>
                            </c:forEach>
                        ],
                        "text":"OPENWEATHER",
                        "line-color":"#4dbac0",
                        "line-width":"2px",
                        "shadow":0,
                        "marker":{
                            "background-color":"#fff",
                            "size":3,
                            "border-width":1,
                            "border-color":"#36a2a8",
                            "shadow":0
                        },
                        "palette":0
                    },
                    {
                        "values":[
                            <c:forEach var="temp" items="${temps2}" varStatus="status">
                               ${temp}
                            <c:if test="${!status.last}">,</c:if>
                            </c:forEach>
                        ],
                        "text":"WUNDERGROUND",
                        "line-width":"2px",
                        "line-color":"#25a6f7",
                        "shadow":0,
                        "marker":{
                            "background-color":"#fff",
                            "size":3,
                            "border-width":1,
                            "border-color":"#1993e0",
                            "shadow":0
                        },
                        "palette":1,
                        "visible":1
                    },
                    {
                        "values":[
                            <c:forEach var="temp" items="${temps3}" varStatus="status">
                            ${temp}
                            <c:if test="${!status.last}">,</c:if>
                            </c:forEach>
                        ],
                        "text":"FORECA",
                        "line-width":"2px",
                        "line-color":"#ad6bae",
                        "shadow":0,
                        "marker":{
                            "background-color":"#fff",
                            "size":3,
                            "border-width":1,
                            "border-color":"#1993e0",
                            "shadow":0
                        },
                        "palette":1,
                        "visible":1
                    }
                ]
            }
        ]
    };

    zingchart.render({
        id : 'myChart',
        data : myConfig,
        height: 500,
        width: '100%'
    });
</script>
