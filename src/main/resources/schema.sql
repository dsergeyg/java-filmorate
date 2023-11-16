CREATE TABLE IF NOT EXISTS user_data (
	user_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	email varchar(100) NOT NULL UNIQUE,
	login varchar(100) NOT NULL UNIQUE,
	name varchar(100),
	birthday timestamp
);

CREATE TABLE IF NOT EXISTS friendship (
	user_id integer NOT NULL REFERENCES user_data (user_id) ON DELETE CASCADE,
	friend_id integer NOT NULL REFERENCES user_data (user_id) ON DELETE CASCADE,
	PRIMARY KEY (user_id, friend_id)
);

--"partial index" are not implemented in H2
--CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_friendship ON TABLE friendship (user_id, friend_id);

CREATE TABLE IF NOT EXISTS mpa_rating (
	rating_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name varchar(100) NOT NULL UNIQUE 
);

CREATE TABLE IF NOT EXISTS film_data (
	film_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name varchar(100) NOT NULL,
	description varchar(200),
	release_date timestamp,
	duration bigint,
	rating_id integer REFERENCES mpa_rating (rating_id)
);

CREATE TABLE IF NOT EXISTS like_list (
	film_id integer NOT NULL REFERENCES film_data (film_id) ON DELETE CASCADE,
	user_id integer NOT NULL REFERENCES user_data (user_id) ON DELETE CASCADE,
	PRIMARY KEY (film_data, user_id)
);

--"partial index" are not implemented in H2
--CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_like_list ON TABLE like_list INCLUDE (film_data, user_data);

CREATE TABLE IF NOT EXISTS genre (
	genre_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name varchar(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film_genre (
	film_id integer REFERENCES film_data (film_id) ON DELETE CASCADE,
	genre_id integer REFERENCES genre (genre_id) ON DELETE CASCADE
);