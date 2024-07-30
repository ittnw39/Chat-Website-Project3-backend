DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id      BIGINT      NOT NULL AUTO_INCREMENT,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(255) NOT NULL,
    last_login TIMESTAMP,
    is_on_connection BOOLEAN NOT NULL,
    role VARCHAR(255) NOT NULL,
    is_activated BOOLEAN NOT NULL,

    PRIMARY KEY (id)
);
