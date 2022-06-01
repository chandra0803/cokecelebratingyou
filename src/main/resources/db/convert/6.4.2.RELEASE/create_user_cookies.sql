--liquibase formatted sql

--changeset palaniss:1
--comment New table is created to save the acceptance details for cookies policy 
CREATE TABLE user_cookies_acceptance
(
  user_cookies_id     NUMBER(18) NOT NULL, 
  user_id         NUMBER(18) NOT NULL, 
  acceptance_date DATE, 
  policy_version NUMBER (18),
  created_by	  NUMBER (18)	NOT NULL, 
  date_created	  DATE NOT NULL, 
  modified_by	  NUMBER (18), 
  date_modified	  DATE, 
  version		  NUMBER (18)	NOT NULL,
  CONSTRAINT user_cookies_acceptance_pk PRIMARY KEY (user_cookies_id)
);
ALTER TABLE user_cookies_acceptance ADD ( CONSTRAINT user_cookies_acceptance_fk  FOREIGN KEY (USER_ID)  REFERENCES PARTICIPANT (USER_ID)  ENABLE VALIDATE);
--rollback DROP TABLE user_cookies_acceptance CASCADE CONSTRAINTS;

--changeset palaniss:2
--comment COOKIES_ACCEPTANCE_SQ sequence
CREATE SEQUENCE COOKIES_ACCEPTANCE_SQ START WITH 100 INCREMENT BY 1;
--rollback DROP SEQUENCE COOKIES_ACCEPTANCE_SQ;