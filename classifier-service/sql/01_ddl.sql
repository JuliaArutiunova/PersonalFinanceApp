CREATE SCHEMA classifier_data
    AUTHORIZATION postgres;


CREATE TABLE classifier_data.currency
(
    currency_id uuid NOT NULL,
    dt_create timestamp(3) without time zone NOT NULL,
    dt_update timestamp(3) without time zone NOT NULL,
    title character varying(50) NOT NULL,
    discription character varying(255) NOT NULL,
    PRIMARY KEY (currency_id)
);

ALTER TABLE IF EXISTS classifier_data.currency
    OWNER to postgres;



CREATE TABLE classifier_data.operation_category
(
    operation_category_id uuid NOT NULL,
    dt_create timestamp(3) without time zone NOT NULL,
    dt_update timestamp(3) without time zone NOT NULL,
    title character varying(255) NOT NULL,
    PRIMARY KEY (operation_category_id)
);

ALTER TABLE IF EXISTS classifier_data.operation_category
    OWNER to postgres;