--liquibase formatted sql

--changeset ramesh:1
--comment drop the OS_PROPERTYSET_TRACKING_FK from os_propertyset table
ALTER TABLE OS_PROPERTYSET DROP CONSTRAINT OS_PROPERTYSET_TRACKING_FK;
--rollback ALTER TABLE OS_PROPERTYSET ADD CONSTRAINT OS_PROPERTYSET_TRACKING_FK  FOREIGN KEY (TRACKING_ID) REFERENCES OS_PROPERTYSET_TRACKING(TRACKING_ID);

--changeset ramesh:2
--comment drop the unsed column(tracking_id) from the os_propertyset 
ALTER TABLE os_propertyset DROP COLUMN tracking_id;
--rollback ALTER TABLE OS_PROPERTYSET ADD TRACKING_ID NUMBER(18);

--changeset ramesh:3
--comment rename the table name from OS_PROPERTYSET_TRACKING to os_propertyset_history
ALTER TABLE os_propertyset_tracking RENAME TO os_propertyset_history;
--rollback ALTER TABLE os_propertyset_history RENAME TO os_propertyset_tracking;

--changeset ramesh:4
--comment rename the column name from tracking_id to os_propertyset_history_id
ALTER TABLE os_propertyset_history RENAME COLUMN tracking_id to os_propertyset_history_id;
--rollback ALTER TABLE os_propertyset_history RENAME COLUMN os_propertyset_history_id TO tracking_id;

--changeset ramesh:5
--comment rename the constraint name from os_propertyset_tracking_id_pk to os_propertyset_history_id_pk
ALTER TABLE os_propertyset_history RENAME CONSTRAINT os_propertyset_tracking_id_pk TO os_propertyset_history_id_pk
--rollback ALTER TABLE os_propertyset_history RENAME CONSTRAINT os_propertyset_history_id_pk TO os_propertyset_tracking_id_pk;
