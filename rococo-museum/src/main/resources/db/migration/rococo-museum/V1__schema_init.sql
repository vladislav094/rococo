create extension if not exists "uuid-ossp";

create table if not exists "country"
(
    id   UUID unique        not null default uuid_generate_v1() primary key,
    name varchar(60) unique not null
);

alter table "country"
    owner to postgres;

create table if not exists "geo"
(
    id         UUID unique        not null default uuid_generate_v1() primary key,
    city       varchar(50) unique not null,
    country_id UUID               not null,
    constraint fk_geo_country foreign key (country_id) references "country" (id)
);

alter table "geo"
    owner to postgres;

create table if not exists "museum"
(
    id          UUID unique        not null default uuid_generate_v1() primary key,
    title       varchar(50) unique not null,
    description varchar(255)       not null,
    photo       bytea,
    geo_id      UUID               not null,
    constraint fk_museum_geo foreign key (geo_id) references "geo" (id)
);

alter table "museum"
    owner to postgres;
