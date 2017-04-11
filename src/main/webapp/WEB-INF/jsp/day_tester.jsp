<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="date" value="${date}"/>
<c:set var="mapItemTester" value="${mapItemTester}"/>
<c:set var="listAvTester" value="${listAverageTester}"/>
<c:set var="message" value="${message}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Day tester page</title>

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/css/dataTables.bootstrap.min.css" rel="stylesheet"/>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

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

    <style>
        a {
            text-decoration: none;
            display: inline-block;
            padding: 8px 16px;
        }

        a:hover {
            background-color: #ddd;
            color: black;
        }

        .previous {
            background-color: #f1f1f1;
            color: black;
        }

        .next {
            background-color: #f1f1f1;
            color: black;
        }

        .round {
            border-radius: 50%;
        }
    </style>
</head>
<body>
<div class="container">

    <p>
        <a href="${contextPath}/welcome">Return to main menu</a>
    </p>

    <h3>
        <a href="#" onclick="changeDay('${date}','-1')" class="previous round"
           data-toggle="tooltip" title="Previous date">&laquo;</a>
            Analysis on date: ${date}
        <a href="#" onclick="changeDay('${date}','1')" class="next round"
           data-toggle="tooltip" title="Next date">&raquo;</a>
    </h3>

    <div class="col-md-6">

        <c:forEach items="${listAvTester}" var="average" varStatus="loop">
            <div id="flip${loop.index}">
                <img src="${contextPath}/resources/images/${average.provider.rowLogo}"
                     data-toggle="tooltip" title="Click for open"/><a href="#">open</a>
            </div>
            <p>Average mistake for date: ${average.valueDay} %</p>
            <p>Total average mistake: ${average.valueTotal} % for the ${average.valueDays} days</p>

            <div id="table${loop.index}" style="display:none">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Item</th>
                        <th>Forecast value</th>
                        <th>Actual value</th>
                        <th>Different,%</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:set value="${mapItemTester[average.provider]}" var="listItems"/>
                    <c:forEach items="${listItems}" var="item">
                        <tr>
                            <td><c:out value="${item.name}" /></td>
                            <td><c:out value="${item.valueForecast}" /></td>
                            <td><c:out value="${item.valueActual}" /></td>
                            <td><c:out value="${item.valueDiff}" /></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:forEach>

    </div>


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
</div>
</body>
</html>

<script>
    $(document).ready(function(){
        <c:forEach items="${listAvTester}" var="average" varStatus="loop">
            $("#flip${loop.index}").click(function(){
                $("#table${loop.index}").slideToggle("slow");
            });
        </c:forEach>
    });

    function changeDay(date, index) {
        $.ajax({
            url: '${contextPath}/forecasts/find/ids',
            type: 'GET',
            data: {
                date : date,
                index: index
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
</script>