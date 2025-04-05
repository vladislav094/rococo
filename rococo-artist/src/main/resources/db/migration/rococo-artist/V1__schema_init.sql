create extension if not exists "uuid-ossp";

create table if not exists "artist"
(
    id        UUID unique         not null default uuid_generate_v1() primary key,
    name      varchar(255) unique not null,
    biography varchar(2000)       not null,
    photo     bytea

);

alter table "artist"
    owner to postgres;

insert into artist(name, biography)
values ('Шишкин', 'Русский живописец, рисовальщик и гравёр-офортист, один из главных мастеров реалистического пейзажа второй половины XIX века'),
       ('Ренуар', 'Французский живописец, график и скульптор, один из основных представителей импрессионизма.'),
       ('Левитан', 'Исаа́к Ильи́ч Левита́н — русский живописец и рисовальщик еврейского происхождения, один из крупнейших и наиболее плодовитых мастеров реалистического пейзажа второй половины XIX века. Академик Императорской Академии художеств.');

