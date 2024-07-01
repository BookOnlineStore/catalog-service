CREATE TABLE books
(
    id               BIGSERIAL    NOT NULL PRIMARY KEY,
    isbn             varchar(255) NOT NULL UNIQUE,
    title            varchar(255) NOT NULL,
    author           varchar(255) NOT NULL,
    publisher        varchar(255) NOT NULL,
    supplier         varchar(255) NOT NULL,
    description      text,
    price            bigint       NOT NULL,
    inventory        int          NOT NULL,
    language         varchar(255) NOT NULL,
    weight           float8       NOT NULL,
    cover_type       varchar(255) NOT NULL,
    number_of_pages  int          NOT NULL,
    width            float8       NOT NULL,
    height           float8       NOT NULL,
    thickness        float8       NOT NULL,
    photos           varchar(255)[],
    purchases        int          NOT NULL,
    created_at       timestamp    NOT NULL,
    created_by       varchar(255),
    last_modified_at timestamp    NOT NULL,
    last_modified_by varchar(255),
    version          int          NOT NULL
);

create table cart_items
(
    id                 serial       not null primary key,
    isbn               varchar(255) not null,
    quantity           integer      not null,
    total_price        bigint       not null,
    created_date       timestamp    NOT NULL,
    created_by         varchar(255),
    last_modified_date timestamp    NOT NULL,
    last_modified_by   varchar(255)
);
