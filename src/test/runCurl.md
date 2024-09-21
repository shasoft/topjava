## Тесты CURL 

### getAll

```bat
curl -v http://localhost:8080/topjava/rest/meals
```

### get

```bat
curl -v http://localhost:8080/topjava/rest/meals/100003
```

### createWithLocation

```bat
curl -v -d "{\"dateTime\":\"2024-09-21T16:27\",\"description\":\"night snack\",\"calories\":\"666\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/topjava/rest/meals
```

### delete

```bat
curl -v -X DELETE http://localhost:8080/topjava/rest/meals/100003
```

### update

```bat
curl -v -X PUT -d "{\"dateTime\":\"2024-09-21T16:24\",\"description\":\"snack\",\"calories\":\"777\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/topjava/rest/meals/100004
```

### getBetween

```bat
curl -v "http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-31&startTime=13:00:00&endDate=2020-01-31&endTime=23:59:59"
```