<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals (список еды)</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals (список еды)</h2>
<div><a href="${pageContext.request.contextPath}/meals/edit">Создать</a></div>
<table border="1">
    <caption>Meals (список еды)</caption>
    <tr>
        <th>Дата/время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th colspan="2"></th>
    </tr>
    <c:forEach items="${meals}" var="mealTo">
        <tr style="color:${mealTo.excess ? 'red' : 'green'}">
            <td>${dateTimeFormatter.format(mealTo.dateTime)}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td><a href="${pageContext.request.contextPath}/meals/edit?id=${mealTo.id}">Изменить</a></td>
            <td><a href="${pageContext.request.contextPath}/meals/delete?id=${mealTo.id}">Удалить</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>