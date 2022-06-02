DROP TABLE IF EXISTS HIBERNATE_SEQUENCE;

DROP TABLE IF EXISTS Product;

CREATE TABLE Product
(
    id          bigint AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(50) NOT NULL,
    description VARCHAR(50) NOT NULL,
    price       INTEGER     NOT NULL,
    primary key (id)
) engine = InnoDB;

create table hibernate_sequence
(
    next_val bigint
) engine = InnoDB;

insert into hibernate_sequence
values (10);

