MERGE INTO genre (genre_id, name) KEY(genre_id) VALUES (1, 'Боевик');
MERGE INTO genre (genre_id, name) KEY(genre_id) VALUES (2, 'Документальный');
MERGE INTO genre (genre_id, name) KEY(genre_id) VALUES (3, 'Драма');
MERGE INTO genre (genre_id, name) KEY(genre_id) VALUES (4, 'Комедия');
MERGE INTO genre (genre_id, name) KEY(genre_id) VALUES (5, 'Мультфильм');
MERGE INTO genre (genre_id, name) KEY(genre_id) VALUES (6, 'Триллер');

--------------------------------------------------

MERGE INTO mpa_rating (rating_id, name) KEY(rating_id) VALUES (1, 'G');
MERGE INTO mpa_rating (rating_id, name) KEY(rating_id) VALUES (2, 'NC-17');
MERGE INTO mpa_rating (rating_id, name) KEY(rating_id) VALUES (3, 'PG');
MERGE INTO mpa_rating (rating_id, name) KEY(rating_id) VALUES (4, 'PG-13');
MERGE INTO mpa_rating (rating_id, name) KEY(rating_id) VALUES (5, 'R');