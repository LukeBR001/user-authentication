CREATE TABLE users (
    id int PRIMARY KEY UNIQUE NOT NULL,
    username varchar(255) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    status varchar NOT NULL,
    role varchar NOT NULL
);

CREATE SEQUENCE public.users_seq