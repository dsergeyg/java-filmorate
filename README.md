# java-filmorate project

## ER - диаграмма
![ER-диаграмма](https://github.com/dsergeyg/java-filmorate/blob/6a489fdca055ac0252ffc5f38908e729fb2bfa71/ER%20-%20%D0%B4%D0%B8%D0%B0%D0%B3%D1%80%D0%B0%D0%BC%D0%BC%D0%B0.png)

## Запросы к БД 
### Получение спсика всех фильмов пользователя
\
SELECT film_id   
  FROM like_list   
  INNER JOIN film ON like_list.filmid = film.film_id   
WHERE user_id = {user_id}  
<br>
### Получение спсика всех фильмов пользователя
\
SELECT film_id  
  FROM like_list INNER JOIN film ON like_list.filmid = film.film_id  
WHERE user_id = {user_id}  
<br>
### Получение спсика топ - N популярных фильмов
\
SELECT  film.*  
       ,COUNT(film_id) AS count_likes  
FROM like_list INNER JOIN film ON like_list.filmid = film.film_id  
GROUP BY film.*  
ORDER BY count_likes DESC  
LIMIT {N}   
<br>
### Получение спсика получение списка общих друзей
\
SELECT u.*  
FROM user AS u  
INNER JOIN friendship AS f1 ON f1.friend_id = u.user_id  
                            AND u.user_id = {otherId}  
WHERE EXISTS(SELECT *   
               FROM friendship AS f2   
             WHERE f2.user_id = {id}   
               AND f2.friend_id = f1.friend_id)   
