--liquibase formatted sql

--changeset ramesh:1
--comment updating the REQUIRE_POSTALCODE value as defalut value(0)
update country set REQUIRE_POSTALCODE = 0 where COUNTRY_CODE in ('aw','bs','fj','jm','mo','tt')
--rollback update country set REQUIRE_POSTALCODE = 1 where COUNTRY_CODE in ('aw','bs','fj','jm','mo','tt');