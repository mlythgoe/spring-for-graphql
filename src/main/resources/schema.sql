DROP TABLE IF EXISTS HIBERNATE_SEQUENCE;

DROP TABLE IF EXISTS ProductPriceHistory;
DROP TABLE IF EXISTS Product;

CREATE TABLE product
(
    id          bigint AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(50) NOT NULL,
    description VARCHAR(50) NOT NULL,
    price       INTEGER     NOT NULL
) engine = InnoDB;

CREATE TABLE productpricehistory
(
    id         bigint AUTO_INCREMENT PRIMARY KEY,
    product_id bigint    NOT NULL,
    start_date TIMESTAMP NOT NULL,
    price      INTEGER   NOT NULL,
    CONSTRAINT product_pk FOREIGN KEY (product_id) REFERENCES product (id)
) engine = InnoDB;

create table hibernate_sequence
(
    next_val bigint
) engine = InnoDB;

insert into hibernate_sequence
values (20);