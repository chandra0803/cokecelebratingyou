--liquibase formatted sql

--changeset sivanand:1
--comment added column PROMOTION_END_DATE in GTT_RA_PROGRAM
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='GTT_RA_PROGRAM' and COLUMN_NAME ='PROMOTION_END_DATE'
ALTER TABLE GTT_RA_PROGRAM ADD PROMOTION_END_DATE DATE;
--rollback ALTER TABLE GTT_RA_PROGRAM DROP (PROMOTION_END_DATE);

--changeset sivanand:2
--comment added column AWARD_AMOUNT_TYPE_FIXED in GTT_RA_PROGRAM
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='GTT_RA_PROGRAM' and COLUMN_NAME ='AWARD_AMOUNT_TYPE_FIXED'
ALTER TABLE GTT_RA_PROGRAM ADD AWARD_AMOUNT_TYPE_FIXED NUMBER(1,0);
--rollback ALTER TABLE GTT_RA_PROGRAM DROP (AWARD_AMOUNT_TYPE_FIXED);

--changeset sivanand:3
--comment added column AWARD_AMOUNT_FIXED in GTT_RA_PROGRAM
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='GTT_RA_PROGRAM' and COLUMN_NAME ='AWARD_AMOUNT_FIXED'
ALTER TABLE GTT_RA_PROGRAM ADD AWARD_AMOUNT_FIXED NUMBER(14,2);
--rollback ALTER TABLE GTT_RA_PROGRAM DROP (AWARD_AMOUNT_FIXED);

--changeset sivanand:4
--comment added column AWARD_AMOUNT_MIN in GTT_RA_PROGRAM
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='GTT_RA_PROGRAM' and COLUMN_NAME ='AWARD_AMOUNT_MIN'
ALTER TABLE GTT_RA_PROGRAM ADD AWARD_AMOUNT_MIN NUMBER(14,2);
--rollback ALTER TABLE GTT_RA_PROGRAM DROP (AWARD_AMOUNT_MIN);

--changeset sivanand:5
--comment added column Modified the column width
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='TMP_APPROVER_NOMI_DETAIL' and COLUMN_NAME ='AVATOR_URL'
ALTER TABLE TMP_APPROVER_NOMI_DETAIL MODIFY AVATOR_URL VARCHAR2(500);
--rollback not required

--changeset sivanand:6
--comment added column Modified the column width
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='TMP_APPROVER_NOMI_DETAIL' and COLUMN_NAME ='ECARD_IMAGE'
ALTER TABLE TMP_APPROVER_NOMI_DETAIL MODIFY ECARD_IMAGE VARCHAR2(500);
--rollback not required

--changeset chandrsa:7
--comment added column Modified the column width
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='TMP_WINNER_NOMINATION_DETAIL' and COLUMN_NAME ='NOMINATOR_AVATAR_URL'
ALTER TABLE TMP_WINNER_NOMINATION_DETAIL MODIFY NOMINATOR_AVATAR_URL VARCHAR2(500);
--rollback not required

--changeset chandrsa:8
--comment added column Modified the column width
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='TMP_WINNER_NOMINATION_DETAIL' and COLUMN_NAME ='NOMINEE_AVATAR_URL'
ALTER TABLE TMP_WINNER_NOMINATION_DETAIL MODIFY NOMINEE_AVATAR_URL VARCHAR2(500);
--rollback not required

