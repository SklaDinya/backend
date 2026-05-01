create table storages
(
    created_at  timestamp(6) not null,
    updated_at  timestamp(6) not null,
    storage_id  uuid         not null
        primary key,
    address     varchar(255) not null,
    description varchar(255),
    name        varchar(255) not null,
    status      varchar(255) not null
        constraint storages_status_check
            check ((status)::text = ANY ((ARRAY ['Created'::character varying, 'Active'::character varying])::text[]))
);

alter table storages
    owner to local;

create table cells
(
    created_at timestamp(6) not null,
    cell_id    uuid         not null
        primary key,
    storage_fk uuid         not null
        constraint fkgl4k0pta9dvehta21odc4ghpy
            references storages,
    cell_class varchar(255) not null,
    name       varchar(255) not null
);

alter table cells
    owner to local;

create table prices
(
    price      numeric(38, 2) not null,
    created_at timestamp(6)   not null,
    price_id   uuid           not null
        primary key,
    storage_fk uuid           not null
        constraint fkj258hmyyty1jjdvqymnbh061u
            references storages,
    cell_class varchar(255)   not null
);

alter table prices
    owner to local;

create table users
(
    banned     boolean      not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    user_id    uuid         not null
        primary key,
    email      varchar(255)
        unique,
    name       varchar(255) not null,
    password   varchar(255) not null,
    role       varchar(255) not null
        constraint users_role_check
            check ((role)::text = ANY
                   ((ARRAY ['Client'::character varying, 'StorageOperator'::character varying, 'Admin'::character varying])::text[])),
    username   varchar(255) not null
        unique
);

alter table users
    owner to local;

create table bookings
(
    price             numeric(38, 2) not null,
    booking_time_hour bigint         not null,
    created_at        timestamp(6)   not null,
    finished_at       timestamp(6)   not null,
    start_time        timestamp(6)   not null,
    booking_id        uuid           not null
        primary key,
    storage_fk        uuid           not null
        constraint fkkl2td80rsy66l2pddy8xh1lqr
            references storages,
    user_fk           uuid           not null
        constraint fk2u9nxxaugtvv2gum0bwr4vs6l
            references users,
    status            varchar(255)   not null
        constraint bookings_status_check
            check ((status)::text = ANY
                   ((ARRAY ['Created'::character varying, 'Paid'::character varying, 'InProcess'::character varying, 'Finished'::character varying, 'Cancelled'::character varying])::text[]))
);

alter table bookings
    owner to local;

create table booking_cells
(
    booking_fk uuid not null
        constraint fklx0dnp9q5o6iogp7s4mb2rxcs
            references bookings,
    cell_fk    uuid not null
        constraint fklhqdom6j3vymlqbmcyjouu0dl
            references cells,
    primary key (booking_fk, cell_fk)
);

alter table booking_cells
    owner to local;

create table operators
(
    id         uuid         not null
        primary key,
    storage_fk uuid         not null
        constraint fkesa4oskvxe995j8ym97xo4ait
            references storages,
    user_fk    uuid         not null
        constraint fk11iepwr9n7mkxd8vb5u1o8euk
            references users,
    role       varchar(255) not null
        constraint operators_role_check
            check ((role)::text = ANY
                   ((ARRAY ['MainOperator'::character varying, 'OrdinaryOperator'::character varying])::text[]))
);

alter table operators
    owner to local;

create table payments
(
    booking_fk   uuid         not null
        constraint fk4i6vsyacy4tvvjads5x065wqj
            references bookings,
    payment_id   uuid         not null
        primary key,
    payload      text         not null,
    payment_type varchar(255) not null
        constraint payments_payment_type_check
            check ((payment_type)::text = ANY ((ARRAY ['NoOp'::character varying, 'Random'::character varying])::text[]))
);

alter table payments
    owner to local;


