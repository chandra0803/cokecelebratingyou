DECLARE

CURSOR C1 is

SELECT c.approval_round,a.activity_id,ua.country_id,a.user_id
FROM
activity a,
claim c,
promotion p,
user_address ua
WHERE
p.promotion_type = 'nomination'
AND p.promotion_id = c.promotion_id
AND c.claim_id = a.claim_id
AND a.user_id =  ua.user_id
AND ua.is_primary = 1;

BEGIN

FOR C1_R IN C1 LOOP

UPDATE activity
SET approval_round = c1_r.approval_round,
    country_id = c1_r.country_id
WHERE activity_id = c1_r.activity_id
 AND  user_id    = c1_r.user_id;
 
 END LOOP;
 
 END;
/


DECLARE
  v_stage  VARCHAR2(100);

CURSOR C1 IS
SELECT * FROM BUDGET_SEGMENT;
  
begin
    v_stage := 'Create budgetsegment asset code';

FOR C1_R IN C1 LOOP

UPDATE BUDGET_SEGMENT
SET CM_ASSET_CODE = 'budget_period_name_data.budgetperiodname.'||CMS_UNIQUE_NAME_SQ.NEXTVAL
WHERE budget_segment_id = c1_r.budget_segment_id;


    BEGIN
      v_stage := 'prc_budget_segment_insert_cms';
      prc_budget_segment_insert_cms(c1_r.name, CMS_UNIQUE_NAME_SQ.CURRVAL);
    
    END;END LOOP;
  dbms_output.put_line('Success');

EXCEPTION
  WHEN OTHERS THEN
    dbms_output.put_line('Stage : '||v_stage||' Others Error : '||SQLERRM);
end;

/
ALTER TABLE BUDGET_SEGMENT
MODIFY CM_ASSET_CODE NOT NULL
/

DECLARE
     
     CURSOR C1 IS
     
     
      SELECT claim_id,participant_id,node_id,approval_round,claim_item_id,approval_status_type,date_approved,date_created,created_by,approver_user_id,rec_rank 
                                                        FROM ( 
                                                             SELECT RANK ()
                                                                     OVER (
                                                                        PARTITION BY c.claim_id
                                                                        ORDER BY
                                                                           rownum asc)
                                                                        AS rec_rank,
                                                                     c.claim_id,cp.participant_id,cp.node_id,c.approval_round,ci.claim_item_id,ci.approval_status_type,ci.date_approved,ci.date_created,ci.created_by,cia.approver_user_id
                                                                FROM claim_participant cp,claim c, promotion p,claim_item ci,claim_item_approver cia
     WHERE p.promotion_type = 'nomination'
       AND p.promotion_id = c.promotion_id
       AND c.claim_id = cp.claim_id 
       AND c.claim_id = ci.claim_id
       AND ci.claim_item_id = cia.claim_item_id(+));
       
       BEGIN
       
       FOR C1_R IN C1 LOOP
       
       IF C1_R.rec_rank = 1 THEN
       
       UPDATE claim_recipient
       SET participant_id = c1_r.participant_id,
       node_id = c1_r.node_id
       WHERE claim_item_id = c1_r.claim_item_id;
       
       UPDATE claim_item_approver
       SET approval_round = c1_r.approval_round
       WHERE claim_item_id = c1_r.claim_item_id;
       
       ELSE
       
       INSERT INTO claim_item (claim_item_id, claim_id,serial_id,approval_status_type,date_approved,date_created,created_by,version)
       VALUES (claim_item_pk_sq.NEXTVAL,C1_R.CLAIM_ID,SYS_GUID(),C1_R.approval_status_type,C1_R.date_approved,C1_R.date_created,C1_R.created_by,0);
       
       IF c1_r.approval_status_type <> 'pend' THEN --Create row only for non-pending claims
       
       insert into claim_item_approver (claim_item_approver_id,claim_item_id,claim_group_id,item_approver_discrim,approval_round,approval_status_type,approver_user_id,date_approved,date_created,created_by,version)
       VALUES (claim_item_approver_pk_sq.nextval,claim_item_pk_sq.CURRVAL,c1_r.claim_id,'cia',c1_r.approval_round,c1_r.approval_status_type,c1_r.approver_user_id,c1_r.date_approved,c1_r.date_created,c1_r.created_by,0);
       
        END IF;
        
        END IF;--Old noms create only one row in claim_item for team noms. Changed to multiple rows in 5.6.3.
       END LOOP;
    END;
/

INSERT INTO journal_bill_code (journal_bill_code_id, journal_id,billing_code1,billing_code2,created_by,date_created,modified_by,date_modified,version)
SELECT journal_bill_pk_sq.NEXTVAL,journal_id,PRIMARY_BILLING_CODE,SECONDARY_BILLING_CODE,created_by,date_created,modified_by,date_modified,version FROM JOURNAL
/
--Journal table
ALTER TABLE JOURNAL DROP COLUMN PRIMARY_BILLING_CODE
/
ALTER TABLE JOURNAL DROP COLUMN SECONDARY_BILLING_CODE
/

INSERT INTO NOMINATION_CLAIM_BEHAVIORS (NOMINATION_CLAIM_BEHAVIORS_ID ,claim_id,behavior)
SELECT NOMINATION_CLAIM_BEH_PK_SQ.NEXTVAL,CLAIM_ID,BEHAVIOR FROM NOMINATION_CLAIM
/
--nomination_claim table
ALTER TABLE NOMINATION_CLAIM DROP COLUMN BEHAVIOR
/

INSERT INTO PROMO_NOMINATION_LEVEL (level_id,promotion_id,AWARD_PAYOUT_TYPE,AWARD_AMOUNT_TYPE_FIXED,AWARD_AMOUNT_FIXED,AWARD_AMOUNT_MIN,AWARD_AMOUNT_MAX,created_by,date_created,version)
SELECT NOMINATION_LEVEL_PK_SQ.NEXTVAL,promotion_id,'points',AWARD_AMOUNT_TYPE_FIXED,AWARD_AMOUNT_FIXED,AWARD_AMOUNT_MIN,AWARD_AMOUNT_MAX,5662,SYSDATE,0 FROM promo_nomination where award_active = 1
/

--promo_nomination table
ALTER TABLE PROMO_NOMINATION DROP COLUMN AWARD_AMOUNT_TYPE_FIXED
/
ALTER TABLE PROMO_NOMINATION DROP COLUMN AWARD_AMOUNT_FIXED
/
ALTER TABLE PROMO_NOMINATION DROP COLUMN AWARD_AMOUNT_MIN
/
ALTER TABLE PROMO_NOMINATION DROP COLUMN AWARD_AMOUNT_MAX
/
ALTER TABLE PROMO_NOMINATION DROP COLUMN FILELOAD_BUDGET_AMOUNT
/
ALTER TABLE PROMO_NOMINATION DROP COLUMN AWARD_SPECIFIER_TYPE
/
UPDATE PROMO_NOMINATION 
SET CERTIFICATE_ACTIVE = 1
WHERE PROMOTION_ID IN (SELECT PROMOTION_ID FROM PROMO_NOMINATION PN WHERE EXISTS (SELECT * FROM PROMO_CERTIFICATE WHERE PROMOTION_ID = PN.PROMOTION_ID))
/
--promotion table
ALTER TABLE PROMOTION DROP COLUMN TRACK_BILLS_BY
/
ALTER TABLE PROMOTION DROP COLUMN PRIMARY_BILL_CODE
/
ALTER TABLE PROMOTION DROP COLUMN PRIMARY_CUSTOM_VALUE
/
ALTER TABLE PROMOTION DROP COLUMN SECONDARY_BILL_CODE
/
ALTER TABLE PROMOTION DROP COLUMN SECONDARY_CUSTOM_VALUE
/
ALTER TABLE PROMOTION DROP COLUMN SWP_PRIMARY_BILL_CODE
/
ALTER TABLE PROMOTION DROP COLUMN SWP_PRIMARY_CUSTOM_VALUE
/
ALTER TABLE PROMOTION DROP COLUMN SWP_SECONDARY_BILL_CODE
/
ALTER TABLE PROMOTION DROP COLUMN SWP_SECONDARY_CUSTOM_VALUE
/