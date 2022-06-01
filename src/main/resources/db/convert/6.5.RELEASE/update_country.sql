--liquibase formatted sql

--changeset esakkimu:1
--comment change value of SMS Capable from false to true Note 
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM country WHERE COUNTRY_CODE IN ('ca')
UPDATE country SET IS_SMS_CAPABLE=1 WHERE COUNTRY_CODE IN ('ca');
--rollback UPDATE country SET IS_SMS_CAPABLE=0 WHERE COUNTRY_CODE IN ('ca');

