--users Table
INSERT INTO users (id, email, password, nickname, last_login, is_on_connection, role, is_activated)
values (
        11111,
        'admin@elice.com',
        '{bcrypt}$2a$10$27OCqEzbbIBf0HD6d6rJ4uftk6t9Eta.spFInZ6VhJPzQpgQXw4TK', -- 1234
        'nickname',
        '2024-07-31 15:30:00',
        TRUE,
        'admin',
        TRUE);

INSERT INTO users (id, email, password, nickname, last_login, is_on_connection, role, is_activated)
values (
        22222,
        'test@elice.com',
        '{bcrypt}$2a$10$27OCqEzbbIBf0HD6d6rJ4uftk6t9Eta.spFInZ6VhJPzQpgQXw4TK', -- 1234
        'nickname',
        '2024-07-31 15:30:00',
        TRUE,
        'user',
        TRUE);

