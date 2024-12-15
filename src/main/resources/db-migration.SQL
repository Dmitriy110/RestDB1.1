-- Удаляем таблицы, если они существуют (для идемпотентности)

DROP TABLE IF EXISTS users;


-- Создаем таблицу users
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);




