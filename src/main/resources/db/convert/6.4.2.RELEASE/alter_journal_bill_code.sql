--liquibase formatted sql

--changeset subramap:1
--Changing the column size to hold max number of characters
alter table journal_bill_code
modify  billing_code1 Varchar2(100 CHAR );
--rollback not required

--changeset subramap:2
--Changing the column size to hold max number of characters
alter table journal_bill_code
modify  billing_code2 Varchar2(100 CHAR );
--rollback not required

--changeset subramap:3
--Changing the column size to hold max number of characters
alter table journal_bill_code
modify  billing_code3 Varchar2(100 CHAR );
--rollback not required

--changeset subramap:4
--Changing the column size to hold max number of characters
alter table journal_bill_code
modify  billing_code4 Varchar2(100 CHAR );
--rollback not required

--changeset subramap:5
--Changing the column size to hold max number of characters
alter table journal_bill_code
modify  billing_code5 Varchar2(100 CHAR );
--rollback not required

--changeset subramap:6
--Changing the column size to hold max number of characters
alter table journal_bill_code
modify  billing_code6 Varchar2(100 CHAR );
--rollback not required

--changeset subramap:7
--Changing the column size to hold max number of characters
alter table journal_bill_code
modify  billing_code7 Varchar2(100 CHAR );
--rollback not required

--changeset subramap:8
--Changing the column size to hold max number of characters
alter table journal_bill_code
modify  billing_code8 Varchar2(100 CHAR );
--rollback not required

--changeset subramap:9
--Changing the column size to hold max number of characters
alter table journal_bill_code
modify  billing_code9 Varchar2(100 CHAR );
--rollback not required

--changeset subramap:10
--Changing the column size to hold max number of characters
alter table journal_bill_code
modify  billing_code10 Varchar2(100 CHAR );
--rollback not required