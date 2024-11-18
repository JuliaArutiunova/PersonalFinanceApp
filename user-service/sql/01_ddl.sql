CREATE SCHEMA user_data
    AUTHORIZATION postgres;



CREATE TABLE user_data."user"
(
    user_id uuid NOT NULL,
    fio character varying(255) NOT NULL,
    mail character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    dt_create timestamp without time zone NOT NULL,
    dt_update timestamp without time zone NULL,
    role character varying(50) NOT NULL,
    status character varying(50) NOT NULL,
    code character varying,
    PRIMARY KEY (user_id),
    CONSTRAINT unique_col UNIQUE (mail)
);

ALTER TABLE IF EXISTS user_data."user"
    OWNER to postgres;

