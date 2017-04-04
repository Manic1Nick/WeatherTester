<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="items" value="${items}"/>
<c:set var="dates" value="${dates}"/>
<c:set var="forecasts" value="${forecasts}"/>
<c:set var="actuals" value="${actuals}"/>
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

    <title>Forecasts page</title>

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/css/dataTables.bootstrap.min.css" rel="stylesheet"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/js/jquery.dataTables.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/js/dataTables.bootstrap.min.js"></script>

    <script src= "https://cdn.zingchart.com/zingchart.min.js"></script>
    <script> zingchart.MODULESDIR = "https://cdn.zingchart.com/modules/";
    ZC.LICENSE = ["569d52cefae586f634c54f86dc99e6a9","ee6b7db5b51705a13dc2339db3edaf6d"];</script>
</head>
<body>
<div class="container">

    <h2>OPENWEATHER:</h2>

    <table id="table" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%">
        <thead>
        <tr>
            <c:forEach items="${items}" var="item">
                <th><c:out value="${item}" /></th>
            </c:forEach>
        </tr>
        </thead>

        <tbody>
            <c:forEach items="${forecasts}" var="forecast">
                <tr>
                    <td>${forecast.date}</td>
                    <td>forecast</td>
                    <td>${forecast.tempMin}</td>
                    <td>${forecast.tempMax}</td>
                    <td>${forecast.pressure}</td>
                    <td>${forecast.clouds}</td>
                    <td>${forecast.windSpeed}</td>
                    <td>${forecast.description}</td>

                    <c:forEach items="${actuals}" var="actual">
                        <c:if test="${forecast.date == actual.date}">
                            <td>${actual.date}</td>
                            <td>actual</td>
                            <td>${actual.tempMin}</td>
                            <td>${actual.tempMax}</td>
                            <td>${actual.pressure}</td>
                            <td>${actual.clouds}</td>
                            <td>${actual.windSpeed}</td>
                            <td>${actual.description}</td>
                        </c:if>
                    </c:forEach>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>

<script>
    $(document).ready(function() {
        $('#table').dataTable( {
            "pageLength": 13
        } );
    });
</script>
