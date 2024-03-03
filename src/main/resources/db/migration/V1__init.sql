create table datapoint
(
    id         uuid PRIMARY KEY   default gen_random_uuid(),
    data       VARCHAR   NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

create table changefeed
(
    id           SERIAL PRIMARY KEY,
    datapoint_id uuid,
    action_type varchar,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT id FOREIGN KEY (datapoint_id) REFERENCES datapoint (id)
);


-----------------------------------------------------

create table status
(
    id     SERIAL PRIMARY KEY,
    status VARCHAR NOT NULL
);

INSERT INTO status(status)
VALUES ('ny');
INSERT INTO status(status)
VALUES ('behandles');
INSERT INTO status(status)
VALUES ('ferdig');

create table hendelser
(
    id         SERIAL PRIMARY KEY,
    payload    VARCHAR   NOT NULL,
    status     INTEGER   NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_status FOREIGN KEY (status) REFERENCES status (id)
);


------------- Postgres functions

CREATE OR REPLACE FUNCTION oppdater_changefeed()
    RETURNS TRIGGER AS
$$
BEGIN
    -- Insert a new row into destination_table with the primary key of the inserted/updated row in source_table
    -- Determine the action type
    IF TG_OP = 'INSERT' THEN
        INSERT INTO changefeed (datapoint_id, action_type)
        VALUES (NEW.id, 'INSERTED');
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO changefeed (datapoint_id, action_type)
        VALUES (NEW.id, 'UPDATED');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER source_table_after_insert_or_update
    AFTER INSERT OR UPDATE
    ON datapoint
    FOR EACH ROW
EXECUTE FUNCTION oppdater_changefeed();



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

CREATE TRIGGER set_last_updated_datakilde
    BEFORE UPDATE
    ON datapoint
    FOR EACH ROW
EXECUTE PROCEDURE set_last_updated_at();

CREATE TRIGGER set_last_updated_hendelser
    BEFORE UPDATE
    ON hendelser
    FOR EACH ROW
EXECUTE PROCEDURE set_last_updated_at();



