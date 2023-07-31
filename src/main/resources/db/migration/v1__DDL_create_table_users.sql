CREATE TABLE users (
    id int PRIMARY KEY UNIQUE NOT NULL,
    username varchar(255) UNIQUE NOT NULL,
    password varchar(11) NOT NULL,
    status TEXT NOT NULL,
    role TEXT NOT NULL
);