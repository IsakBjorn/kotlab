create table status
(
    id     SERIAL PRIMARY KEY,
    status VARCHAR NOT NULL
);

INSERT INTO status(status) VALUES ('ny');
INSERT INTO status(status) VALUES ('behandles');
INSERT INTO status(status) VALUES ('ferdig');

create table hendelser
(
    id         SERIAL PRIMARY KEY,
    type       VARCHAR   NOT NULL,
    payload    VARCHAR   NOT NULL,
    status     INTEGER   NOT NULL DEFAULT 1,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_status FOREIGN KEY (status) REFERENCES status (id)
);

CREATE
    OR REPLACE FUNCTION set_last_updated_at()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$
    language 'plpgsql';

CREATE TRIGGER set_last_updated_at_column_hendelser
    BEFORE UPDATE
    ON hendelser
    FOR EACH ROW
EXECUTE PROCEDURE set_last_updated_at();
