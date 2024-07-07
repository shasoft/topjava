<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Изменить еду</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<c:choose>
    <c:when test="${meal.id==0}">
        <h2>Создание еды</h2>
    </c:when>
    <c:otherwise>
        <h2>Изменение еды</h2>
    </c:otherwise>
</c:choose>

<form method="post">
    <input name="id" type="hidden" value="${meal.id}">
    <div>
        <label for="dateTime">Дата/время:</label>
        <input id="dateTime" name="dateTime" required type="datetime-local" value="${meal.dateTime}">
    </div>

    <div>
        <label for="description">Описание:</label>
        <input id="description" name="description" required type="text" size="120" value="${meal.description}">
    </div>

    <div>
        <label for="calories">Калории:</label>
        <input id="calories" name="calories" required type="number" value="${meal.calories}">
    </div>
    <hr/>
    <div style="text-align: center">
        <input type="submit" name="actionSave" value="Сохранить">
        <button onclick="window.history.back()" type="button">Отменить</button>
    </div>
</form>
</body>
</html>