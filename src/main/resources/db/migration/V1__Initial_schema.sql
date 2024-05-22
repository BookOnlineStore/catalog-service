CREATE TABLE books
(
    id               BIGSERIAL    NOT NULL PRIMARY KEY,
    isbn             varchar(255) NOT NULL UNIQUE,
    title            varchar(255) NOT NULL,
    author           varchar(255) NOT NULL,
    price            float8       NOT NULL,
    created_at       timestamp    NOT NULL,
    created_by       varchar(255) ,
    last_modified_at timestamp    NOT NULL,
    last_modified_by varchar(255) ,
    version          int          NOT NULL
);