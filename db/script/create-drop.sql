
    drop table if exists t_city;

    drop table if exists t_coplxid1entity;

    drop table if exists t_coplxid2entity;

    create table t_city (
       id varchar(36) not null,
        age integer,
        createDate datetime(6),
        name varchar(30),
        price double precision,
        sex varchar(10),
        primary key (id)
    ) engine=InnoDB;

    create table t_coplxid1entity (
       id1 varchar(255) not null,
        id2 varchar(255) not null,
        name varchar(255),
        primary key (id1, id2)
    ) engine=InnoDB;

    create table t_coplxid2entity (
       id1 varchar(255) not null,
        id2 varchar(255) not null,
        name varchar(255),
        primary key (id1, id2)
    ) engine=InnoDB;
