create sequence hibernate_sequence start 1 increment 1
create table city (
      id int8 not null,
      name varchar(255) not null,
      description varchar(2048),
      primary key (id)
);