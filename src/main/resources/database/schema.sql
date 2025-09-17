DROP TABLE IF EXISTS ProductPriceHistory;
DROP TABLE IF EXISTS Product;

CREATE TABLE product
(
    id          UUID PRIMARY KEY,
    title       VARCHAR(50) NOT NULL,
    description VARCHAR(50) NOT NULL,
    price       INTEGER     NOT NULL
) engine = InnoDB;

CREATE TABLE productpricehistory
(
    id         UUID PRIMARY KEY,
    product_id UUID    NOT NULL,
    start_date TIMESTAMP NOT NULL,
    price      INTEGER   NOT NULL,
    CONSTRAINT product_pk FOREIGN KEY (product_id) REFERENCES product (id)
) engine = InnoDB;