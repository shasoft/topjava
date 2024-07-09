<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Изменить еду</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${meal.id==null ? 'Создание' : 'Изменение'} еды</h2>
<form method="post">
    <c:if test="meal.id!=null"><input name="id" type="hidden" value="${meal.id}"></c:if>
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