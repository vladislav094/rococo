create extension if not exists "uuid-ossp";

create table if not exists "country"
(
    id   UUID unique        not null default uuid_generate_v1() primary key,
    name varchar(60) unique not null
);

alter table "country"
    owner to postgres;
insert into country (name)
values ('Belarus'),
       ('Russian Federation'),
       ('France');

create table if not exists "geo"
(
    id         UUID unique        not null default uuid_generate_v1() primary key,
    city       varchar(50) unique not null,
    country_id UUID               not null,
    constraint fk_geo_country foreign key (country_id) references "country" (id)
);

alter table "geo"
    owner to postgres;
insert into geo(city, country_id)
values ('Minsk', (select id from country where name = 'Belarus')),
       ('Moscow', (select id from country where name = 'Russian Federation')),
       ('Paris', (select id from country where name = 'France'));

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

insert into museum(title, description, geo_id)
values ('Национальный художественный музей Беларуси',
        'Национальный художественный музей Беларуси — это крупнейшее в стране собрание произведений искусства,' ||
        ' включающее более 30 тысяч экспонатов.',
        (select id from geo where city = 'Minsk')),

       ('Третьяковская галерея',
        'Государственная Третьяковская галерея — российский государственный художественный музей в Москве.',
        (select id from geo where city = 'Moscow')),

       ('Лувр', 'Один из крупнейших и самый популярный художественный музей мира. Музей расположен в ' ||
                'центре Парижа, на правом берегу Сены, на улице Риволи, в 1-м округе столицы.',
        (select id from geo where city = 'Paris'));