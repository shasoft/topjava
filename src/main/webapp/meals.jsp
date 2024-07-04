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
<div><a href="editMeal?id=0">Добавить</a></div>
<table>
    <caption>Meals (список еды)</caption>
    <tr>
        <th>№</th>
        <th>Дата/время</th>
        <th>Описание</th>
        <th>Каллории</th>
    </tr>
    <c:forEach items="${meals}" var="mealTo">
    <tr style="color:<c:out value="${mealTo.excess ? 'red' : 'green'}" />">
        <td>${mealTo.id}</td>
        <td>${dateTimeFormatter.format(mealTo.dateTime)}</td>
        <td>${mealTo.description}</td>
        <td>${mealTo.calories}</td>
        <td><a href="editMeal?id=${mealTo.id}">Изменить</a></td>
        <td><a href="editMeal?id=${mealTo.id}&delete=1">Удалить</a></td>
    </tr>
    </c:forEach>
</table>
</body>
</html>