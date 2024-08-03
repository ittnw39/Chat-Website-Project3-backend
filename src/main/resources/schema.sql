DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id      BIGINT      NOT NULL AUTO_INCREMENT,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(255) NOT NULL,
    last_login TIMESTAMP NOT NULL,
    is_on_connection BOOLEAN NOT NULL,
    role VARCHAR(255) NOT NULL,
    is_activated BOOLEAN NOT NULL,

    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS voicechats;

CREATE TABLE voicechats
(
    id      BIGINT          NOT NULL AUTO_INCREMENT,
    name    VARCHAR(255)    NOT NULL,
    PRIMARY KEY (id)
);
