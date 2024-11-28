CREATE SCHEMA account_data
    AUTHORIZATION postgres;


CREATE TABLE account_data.users
(
    user_id uuid NOT NULL,
    PRIMARY KEY (user_id)
);

ALTER TABLE IF EXISTS account_data.users
    OWNER to postgres;



CREATE TABLE account_data.currency
(
    currency_id uuid NOT NULL,
    PRIMARY KEY (currency_id)
);

ALTER TABLE IF EXISTS account_data.currency
    OWNER to postgres;


CREATE TABLE account_data.operation_category
(
    operation_category_id uuid NOT NULL,
    PRIMARY KEY (operation_category_id)
);

ALTER TABLE IF EXISTS account_data.operation_category
    OWNER to postgres;


CREATE TABLE account_data.account
(
    account_id uuid NOT NULL,
    user_id uuid NOT NULL,
    dt_create timestamp(3) without time zone NOT NULL,
    dt_update timestamp(3) without time zone NOT NULL,
    title character varying(255) NOT NULL,
    description character varying NOT NULL,
    balance numeric NOT NULL DEFAULT 0,
    type character varying(50) NOT NULL,
    currency_id uuid NOT NULL,
    PRIMARY KEY (account_id)
);

ALTER TABLE IF EXISTS account_data.account
    OWNER to postgres;


CREATE TABLE IF NOT EXISTS account_data.account
(
    account_id uuid NOT NULL,
    user_id uuid NOT NULL,
    dt_create timestamp(3) without time zone NOT NULL,
    dt_update timestamp(3) without time zone NOT NULL,
    title character varying(255)  NOT NULL,
    description character varying NOT NULL,
    balance numeric NOT NULL DEFAULT 0,
    type character varying(50) NOT NULL,
    currency uuid NOT NULL,
    CONSTRAINT account_pkey PRIMARY KEY (account_id),
    CONSTRAINT "currency_id_FK" FOREIGN KEY (currency)
        REFERENCES account_data.currency (currency_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT "user_id_FK" FOREIGN KEY (user_id)
        REFERENCES account_data.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


ALTER TABLE IF EXISTS account_data.account
    OWNER to postgres;



CREATE TABLE IF NOT EXISTS account_data.operation
(
    operation_id uuid NOT NULL,
    account uuid NOT NULL,
    dt_create timestamp(3) without time zone NOT NULL,
    dt_update timestamp(3) without time zone NOT NULL,
    date date NOT NULL,
    description character varying NOT NULL,
    operation_category uuid NOT NULL,
    value numeric NOT NULL,
    currency uuid NOT NULL,
    CONSTRAINT operation_pkey PRIMARY KEY (operation_id),
    CONSTRAINT "currency_FK" FOREIGN KEY (currency)
        REFERENCES account_data.currency (currency_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT "operation_category_FK" FOREIGN KEY (operation_category)
        REFERENCES account_data.operation_category (operation_category_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

ALTER TABLE IF EXISTS account_data.operation
    OWNER to postgres;