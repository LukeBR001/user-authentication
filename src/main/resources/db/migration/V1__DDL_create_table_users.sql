CREATE TABLE public.users (
    id int8 PRIMARY KEY UNIQUE NOT NULL,
    aggregate_id varchar UNIQUE NOT NULL,
    username varchar(255) UNIQUE NOT NULL,
    password varchar NOT NULL,
    description varchar(255),
    status varchar NOT NULL,
    role varchar NOT NULL
);

create sequence public.users_seq