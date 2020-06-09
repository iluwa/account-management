create schema if not exists accounts;

create sequence accounts.users_seq start with 1 INCREMENT BY 50;

create table accounts.users (
    id          bigint primary key,
    login       varchar(200) unique not null
);

create sequence accounts.accounts_seq start with 1 INCREMENT BY 50;

create table accounts.accounts (
    id          bigint primary key,
    num         varchar(50) unique not null,
    amount      bigint not null default 0,
    currency    varchar(10) not null,
    user_id     bigint references accounts.users (id)
);