INSERT INTO genre (name) SELECT * FROM (
SELECT 'Комедия' UNION
SELECT 'Драма' UNION 
SELECT 'Мультфильм' UNION
SELECT 'Триллер' UNION 
SELECT 'Документальный' UNION
SELECT 'Боевик') WHERE NOT EXISTS (SELECT * FROM genre g WHERE g.name = name);

--------------------------------------------------

INSERT INTO mpa_rating (name) SELECT * FROM (
SELECT 'G' UNION
SELECT 'PG' UNION 
SELECT 'PG-13' UNION
SELECT 'R' UNION 
SELECT 'NC-17') WHERE NOT EXISTS (SELECT * FROM mpa_rating g WHERE g.name = name);