create extension if not exists "uuid-ossp";

create table if not exists "painting"
(
    id          UUID unique         not null default uuid_generate_v1() primary key,
    title       varchar(255) unique not null,
    description varchar(1000),
    artist_id   UUID,
    museum_id   UUID,
    content     bytea
);

alter table "painting"
    owner to postgres;