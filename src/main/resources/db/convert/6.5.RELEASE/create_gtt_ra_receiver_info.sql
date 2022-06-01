--liquibase formatted sql

--changeset sundaram:1
--comment Create new temp table for RA functionality
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_tables where table_name='GTT_RA_RECEIVER_INFO'
  CREATE GLOBAL TEMPORARY TABLE GTT_RA_RECEIVER_INFO
(
  USER_ID  NUMBER(18),
  DATE_PAX_IN  DATE,
  DAT_MGR_IN   DATE,
  RCT_DT     DATE
)
ON COMMIT PRESERVE ROWS
NOCACHE;
--rollback not required

--changeset gorantla:2
--comment rename column name
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM user_tab_columns WHERE table_name='GTT_RA_RECEIVER_INFO' AND column_name ='RCT_DATE'
 alter table gtt_ra_receiver_info rename column rct_dt to rct_date ;
--rollback ALTER TABLE gtt_ra_receiver_info rename COLUMN rct_date to rct_dt;