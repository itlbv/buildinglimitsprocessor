create sequence hibernate_sequence
    start with 1
    cache 1;

create table building_limit
(
    id      BIGINT PRIMARY KEY DEFAULT nextval('hibernate_sequence'),
    points  JSONB NOT NULL
);

create table height_plateau
(
    id      BIGINT PRIMARY KEY DEFAULT nextval('hibernate_sequence'),
    points  JSONB NOT NULL,
    height  NUMERIC(19, 2) NOT NULL
);

create table building_limit_split
(
    id      BIGINT PRIMARY KEY DEFAULT nextval('hibernate_sequence'),
    points  JSONB NOT NULL,
    height  NUMERIC(19, 2) NOT NULL
);
