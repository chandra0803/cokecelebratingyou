CREATE OR REPLACE PROCEDURE prc_budget_convert_G54
AS
/*******************************************************************************
-- Purpose: Budget Convert script for G5.4 release. This includes dealing with the change requests done for 5.4 release and the budget_history table changes. 
-- MODIFICATION HISTORY
-- Person               Date            Comments
-- ---------            ----------      ------------------------------------------------
--  Ravi Dhanekula      09/08/2014      Creation
*******************************************************************************/ 
c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('prc_budget_convert_G54');
v_stage       VARCHAR2(250);
c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;

    file_handler                UTL_FILE.file_type;
    v_file_name                 VARCHAR2(80) := 'G5.4 Inactived budgets with future start_date'||'.txt';
    v_file_location             VARCHAR2(1000);    
   
    v_flag_start                BOOLEAN := TRUE;
     v_header                    VARCHAR2(4000);

    directory_error             exception;
    process_error               exception;

CURSOR C1 IS
SELECT * FROM budget_segment;

CURSOR C2 IS
SELECT fnc_cms_asset_code_val_extr(bm.cm_asset_code,'BUDGET_NAME','en_US') Budget_master_name,bm.budget_type,au.last_name||' , '||au.first_name  Budget_name,b.budget_id FROM budget b, budget_master bm,application_user au WHERE  TRUNC(b.START_DATE) > TRUNC(SYSDATE)
AND bm.budget_master_id = b.budget_master_id
AND b.user_id = au.user_id
UNION ALL
SELECT fnc_cms_asset_code_val_extr(bm.cm_asset_code,'BUDGET_NAME','en_US'),bm.budget_type,n.name,b.budget_id FROM budget b, budget_master bm,node n WHERE  TRUNC(b.START_DATE) > TRUNC(SYSDATE)
AND bm.budget_master_id = b.budget_master_id
AND b.node_id = n.node_id;

BEGIN

 v_stage := 'Start';
prc_execution_log_entry (c_process_name,c_release_level,'INFO',v_stage,NULL);   

v_stage := 'Insert data into budget_segment table';

INSERT INTO budget_segment (budget_segment_id,budget_master_id,name,status,start_date,is_allow_budget_reallocation,budget_reallocation_elig_type,created_by,date_created,version)
SELECT BUDGET_SEGMENT_PK_SQ.NEXTVAL,budget_master_id,'Initial Segment',1,TRUNC(SYSDATE),is_allow_budget_reallocation,budget_reallocation_elig_type,5662,SYSDATE,1 from budget_master;


v_stage := 'Populate budget_segment_id in budget table AND Fix the start date on budget_segment table';

FOR C1_R IN C1 LOOP
UPDATE budget
SET budget_segment_id = c1_r.budget_segment_id
WHERE budget_master_id = c1_r. budget_master_id;

UPDATE budget_segment
SET start_date = (SELECT NVL(MIN(start_date),SYSDATE) FROM budget WHERE budget_segment_id = c1_r.budget_segment_id)
WHERE budget_segment_id = c1_r.budget_segment_id;

UPDATE budget_master
SET start_date = (SELECT NVL(MIN(start_date),SYSDATE) FROM budget WHERE budget_segment_id = c1_r.budget_segment_id)
WHERE budget_master_id = c1_r.budget_master_id;

END LOOP;

 BEGIN
SELECT directory_name
         INTO v_file_location        
         FROM all_directories
        WHERE owner = 'SYS'
          AND directory_name = directory_path
          AND ROWNUM = 1;       
          
  EXCEPTION WHEN NO_DATA_FOUND THEN
    SELECT directory_name
         INTO v_file_location        
         FROM all_directories
        WHERE owner = 'SYS'
          AND directory_name = 'BEACON_EXTERNAL'
          AND ROWNUM = 1;   
    
    WHEN OTHERS THEN
    RAISE directory_error;
  END;

SELECT 'BUDGET_MASTER_NAME '||' , '||'BUDGET_NAME' INTO v_header FROM dual;

FOR C2_R IN C2 LOOP

UPDATE budget
SET status = 'closed'
WHERE budget_id = c2_r.budget_id;

     IF v_flag_start THEN
         file_handler := UTL_FILE.fopen(v_file_location, v_file_name, 'w',4096);
         UTL_FILE.put_line(file_handler, v_header);
         UTL_FILE.put_line(file_handler, c2_r.Budget_master_name||' '||c2_r.Budget_name);
         v_flag_start:= FALSE;
       ELSE
         UTL_FILE.put_line(file_handler, c2_r.Budget_master_name||' '||c2_r.Budget_name);
       END IF;
  END LOOP;

  UTL_FILE.fclose(file_handler);

v_stage := 'Fix the budget table to change budget table indexes.';

--EXECUTE IMMEDIATE 'CREATE INDEX BUDGET_BUDGET_SEGMENT_FK_IDX ON BUDGET (BUDGET_SEGMENT_ID)';
--
--EXECUTE IMMEDIATE 'ALTER TABLE BUDGET DROP CONSTRAINT BUDGET_ID_PK';
--
--EXECUTE IMMEDIATE 'ALTER TABLE BUDGET DROP CONSTRAINT BUDGET_UK';
--
--EXECUTE IMMEDIATE 'DROP INDEX BUDGET_UK';
--
--EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX BUDGET_UK ON BUDGET (BUDGET_SEGMENT_ID, USER_ID, NODE_ID)';
--
--EXECUTE IMMEDIATE 'ALTER TABLE BUDGET ADD (CONSTRAINT BUDGET_ID_PK PRIMARY KEY(BUDGET_ID)  USING INDEX BUDGET_ID_PK,  CONSTRAINT BUDGET_UK
--  UNIQUE (BUDGET_SEGMENT_ID, USER_ID, NODE_ID)  USING INDEX BUDGET_UK)';
--
--EXECUTE IMMEDIATE 'ALTER TABLE BUDGET ADD (CONSTRAINT BUDGET_BUDGET_SEGMENT_FK  FOREIGN KEY (BUDGET_SEGMENT_ID)
--  REFERENCES BUDGET_SEGMENT (BUDGET_SEGMENT_ID))';
  
v_stage := 'Alter the budget_history table. Insert data to action_type column';

UPDATE budget_history
SET action_type = 'transfer'
WHERE ORIGINAL_VALUE_BEFORE_XACTION = ORIGINAL_VALUE_AFTER_XACTION AND CURRENT_VALUE_AFTER_XACTION > CURRENT_VALUE_BEFORE_XACTION and CREATED_BY <> 5662
AND action_type IS NULL;
--Transfer

UPDATE budget_history
SET action_type = 'deduct'
WHERE ORIGINAL_VALUE_BEFORE_XACTION = ORIGINAL_VALUE_AFTER_XACTION AND CURRENT_VALUE_AFTER_XACTION > CURRENT_VALUE_BEFORE_XACTION and CREATED_BY = 5662
AND action_type IS NULL;

--Negative deduct 

UPDATE budget_history
SET action_type = 'deduct'
WHERE ORIGINAL_VALUE_BEFORE_XACTION = ORIGINAL_VALUE_AFTER_XACTION AND CURRENT_VALUE_AFTER_XACTION < CURRENT_VALUE_BEFORE_XACTION AND action_type IS NULL;

UPDATE budget_history
SET action_type = 'transfer'
WHERE ORIGINAL_VALUE_BEFORE_XACTION <> ORIGINAL_VALUE_AFTER_XACTION AND action_type IS NULL;

UPDATE budget_history
SET action_type = 'none'
WHERE action_type IS NULL ;

EXCEPTION WHEN OTHERS THEN
prc_execution_log_entry (c_process_name,c_release_level,'INFO',v_stage|| SQLERRM,NULL);   
END;
/
BEGIN

prc_budget_convert_G54;

END;
/
