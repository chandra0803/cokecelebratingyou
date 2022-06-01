--liquibase formatted sql

--changeset kancherla:1
--comment Deleting unused suppliers Edenred and OPISA
DELETE FROM country_suppliers WHERE supplier_id = (SELECT supplier_id FROM supplier WHERE supplier_name = 'Edenred');
DELETE FROM country_suppliers WHERE supplier_id = (SELECT supplier_id FROM supplier WHERE supplier_name = 'OPISA');
DELETE FROM supplier WHERE supplier_name = 'Edenred';
DELETE FROM supplier WHERE supplier_name = 'OPISA';
--rollback not required