DROP TABLE IF EXISTS Product;
CREATE TABLE Product
(
    id          bigint AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(50) NOT NULL,
    description VARCHAR(50) NOT NULL
);

