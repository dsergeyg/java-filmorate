INSERT INTO genre (name) SELECT name FROM (
SELECT '1' AS id, 'Комедия' AS Name UNION
SELECT '2' AS id, 'Драма' AS Name UNION 
SELECT '3' AS id, 'Мультфильм' AS Name UNION
SELECT '4' AS id, 'Триллер' AS Name UNION 
SELECT '5' AS id, 'Документальный' AS Name UNION
SELECT '6' AS id, 'Боевик' AS Name) WHERE NOT EXISTS (SELECT * FROM genre g WHERE g.name = name) ORDER BY ID;

--------------------------------------------------

INSERT INTO mpa_rating (name) SELECT name FROM (
SELECT '1' AS id, 'G' AS Name UNION
SELECT '2' AS id, 'PG' AS Name UNION 
SELECT '3' AS id, 'PG-13' AS Name UNION
SELECT '4' AS id, 'R' AS Name UNION 
SELECT '5' AS id, 'NC-17' AS Name) WHERE NOT EXISTS (SELECT * FROM mpa_rating g WHERE g.name = name) ORDER BY id;