-- create database "rococo-userdata" with owner postgres;

create extension if not exists "uuid-ossp";

create table if not exists "users"
(
    id        UUID unique        not null default uuid_generate_v1(),
    username  varchar(50) unique not null,
    firstname varchar(255),
    lastname  varchar(255),
    avatar    bytea,
    primary key (id)
);

alter table "users"
    owner to postgres;
