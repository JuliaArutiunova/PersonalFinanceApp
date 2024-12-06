\c audit

CREATE SCHEMA audit_data
    AUTHORIZATION audit_app_user;


CREATE TABLE IF NOT EXISTS audit_data.audit
(
    audit_id uuid NOT NULL,
    dt_create timestamp(3) without time zone NOT NULL,
    dt_update timestamp(3) without time zone NOT NULL,
    user_id uuid NOT NULL,
    text character varying NOT NULL,
    essence_type character varying(50) NOT NULL,
    entity_id uuid NOT NULL,
    CONSTRAINT audit_pkey PRIMARY KEY (audit_id)
);

ALTER TABLE IF EXISTS audit_data.audit
    OWNER to audit_app_user;