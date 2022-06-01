CREATE OR REPLACE PROCEDURE prc_budget_move_transfer
( p_import_file_id  IN NUMBER, 
  p_out_returncode  OUT NUMBER) 
IS
PRAGMA AUTONOMOUS_TRANSACTION;

/*------------------------------------------------------------------------------
Purpose:  maintain budgets, based on hierarchy recs saved in BUDGET_MOVE
          should only run after successful hierachy "L"oad
          
Rules:  for 'upd' to a new parent, budget will be moved to old parent, 
             since budget still belongs to that rollup structure
        for 'upd' to a new node name, budget will not be moved,
             since node (and their budget) still belongs to the same parent
        for 'del' the node, budget will be moved to old parent, 
             since budget still belongs to that rollup structure
        
        if immediate parent is not active (node.is_deleted = 1), 
             find closest active parent in rollup
             NOTE: a deleted parent should not happen - the standard hierarchy load prevents this

Person             Date           Comments
--------------------------------------------------------------------------------
-- Gorantla         03/05/2019     Initial
--                  02/08/2019     add addl amt to budget.original_value (same as is being done to budget.current_value)
--                  03/20/2019     add FOR UPDATE of to lock the budget to prevent anything else from altering this rec
-- Suresh J         05/03/2019     Issue#1957 Budget Transfer - The Budget that is now getting transferred as a result of Org unit update/movement is getting closed
------------------------------------------------------------------------------*/

  c_process_name           CONSTANT execution_log.process_name%TYPE:= 'PRC_BUDGET_MOVE_TRANSFER';
  c_xfer_method            CONSTANT budget.transfer_method%TYPE := 'AUTO ROLLUP';
  
  v_msg                    execution_log.text_line%TYPE;
  v_stage                  VARCHAR2(500) := 'Start';
  
  v_read_count             INT := 0;
  v_budget_count           INT := 0;
  v_parent_budget_ins      INT := 0;
  v_closed_parent_budget   INT := 0;
  v_email_count            INT := 0;
  v_no_node_count          INT;
  v_no_parent_count        INT;
  v_no_budgets_count       INT;
  v_ttl_count              INT;
    
  v_new_parent_id          node.node_id%TYPE;
  v_old_parent_id          node.node_id%TYPE;
  
  lr_node_budget           budget%ROWTYPE;
  lr_parent_budget         budget%ROWTYPE;
  lr_node_budget_hist      budget_history%ROWTYPE;
  lr_parent_budget_hist    budget_history%ROWTYPE;
  lr_email                 budget_move_email%ROWTYPE;
  
  CURSOR parent_new_cur IS
  SELECT DISTINCT bm.new_parent_node_id
  FROM   BUDGET_MOVE bm
  WHERE  bm.import_file_id = p_import_file_id
  AND    bm.date_processed IS NULL;      
  
  CURSOR parent_old_cur IS
  SELECT DISTINCT bm.old_parent_node_id
  FROM   BUDGET_MOVE bm
  WHERE  bm.import_file_id = p_import_file_id
  AND    bm.date_processed IS NULL;      
  
  CURSOR move_cur IS
  SELECT bm.budget_move_id,
         bm.import_file_id,
         bm.import_record_id,
         bm.node_id,
         bm.node_own_user_id,
         bm.budget_id_list,
         bm.old_parent_node_id move_to_node_id,
         bm.old_parent_own_user_id move_to_own_user_id
  FROM   BUDGET_MOVE bm
  WHERE  bm.import_file_id = p_import_file_id
  AND    bm.date_processed IS NULL
--  AND bm.node_id = 7004 -- testing
  AND    bm.is_budget_move_needed = 1;
  
  CURSOR budget_cur (cv_list IN VARCHAR2) IS
  SELECT budget_id
    FROM budget
   WHERE budget_id IN (SELECT * FROM TABLE(get_array_varchar(cv_list)));

BEGIN
  
  v_msg := 'Start for file_id: '||p_import_file_id;
  prc_execution_log_entry(c_process_name,1,'INFO',v_msg,NULL);

  p_out_returncode := 0;
                
--------------------------------------------------------------------------------
                
  v_stage := 'update BUDGET_MOVE.new_parent_node_id, based on BUDGET_MOVE.node_id';
  UPDATE BUDGET_MOVE bm
     SET bm.new_parent_node_id = ( SELECT parent_node_id
                                     FROM node
                                    WHERE node_id = bm.node_id)
   WHERE bm.import_file_id = p_import_file_id
     AND bm.node_id IS NOT NULL;              
                
  v_stage := 'update BUDGET_MOVE.budget_id_list, based on BUDGET_MOVE.node_id';
  -- elig budgets must be active, have a non-zero balance and be unexpired
  UPDATE BUDGET_MOVE bm
     SET bm.budget_id_list = ( SELECT LISTAGG(b.budget_id , ', ') WITHIN GROUP (ORDER BY budget_id) AS budget_id_list
                                 FROM budget b, budget_segment bs
                                WHERE b.budget_segment_id = bs.budget_segment_id
                                  AND NVL(bs.end_date,SYSDATE + 1) > TRUNC(SYSDATE)
                                  AND b.status = 'active'
                                  AND b.current_value > 0
                                  AND b.node_id = bm.node_id)
   WHERE bm.import_file_id = p_import_file_id
     AND bm.node_id IS NOT NULL;
                
   COMMIT;
                
--------------------------------------------------------------------------------

  v_stage := 'validate new_parent_node_id';
  FOR parent_rec IN parent_new_cur LOOP
    BEGIN
      v_stage := 'verify parent_node_id is active: '||parent_rec.new_parent_node_id;
      SELECT node_id
      INTO   v_new_parent_id
      FROM   node
      WHERE  node_id = parent_rec.new_parent_node_id
      AND    is_deleted = 0;
      
      -- active rec found, no addl action needed    
    EXCEPTION
      WHEN NO_DATA_FOUND THEN -- standard Hierarchy load should not let this happen
        BEGIN
          v_stage := 'check rollup of parent for first active parent_node_id: '||parent_rec.new_parent_node_id;   
          SELECT node_id
            INTO v_new_parent_id
            FROM (SELECT node_id
                    FROM node
                   WHERE is_deleted = 0
                   START WITH node_id = parent_rec.new_parent_node_id -- 02/12/2013
                 CONNECT BY PRIOR parent_node_id = node_id
                   ORDER BY LEVEL)
           WHERE ROWNUM = 1; -- this will return closest parent value because of "ORDER BY LEVEL"

        EXCEPTION
          WHEN OTHERS THEN
            v_new_parent_id := NULL; -- shouldn't happen
        END;
                                                                
        v_stage := 'update to new parent_node_id: '||parent_rec.new_parent_node_id; 
        UPDATE BUDGET_MOVE
           SET new_parent_node_id = v_new_parent_id
         WHERE import_file_id = p_import_file_id
           AND new_parent_node_id = parent_rec.new_parent_node_id;             
        
    END;  
    
  END LOOP; -- FOR parent_rec IN parent_new_cur
  
  COMMIT;
                
--------------------------------------------------------------------------------

  v_stage := 'validate old_parent_node_id';
  FOR parent_rec IN parent_old_cur LOOP
    BEGIN
    v_stage := 'verify parent_node_id is active: '||parent_rec.old_parent_node_id;
    SELECT node_id
      INTO v_old_parent_id
      FROM node
     WHERE node_id = parent_rec.old_parent_node_id
       AND is_deleted = 0;
      
      -- active rec found, no addl action needed
    EXCEPTION
      WHEN NO_DATA_FOUND THEN -- standard Hierarchy load should not let this happen
        BEGIN
        v_stage := 'check rollup of parent for first active parent_node_id: '||parent_rec.old_parent_node_id;   
        SELECT node_id
          INTO v_old_parent_id
          FROM (SELECT node_id
                  FROM node
                 WHERE is_deleted = 0
                 START WITH node_id = parent_rec.old_parent_node_id -- 02/12/2013
               CONNECT BY PRIOR parent_node_id = node_id
                 ORDER BY LEVEL)
         WHERE ROWNUM = 1; -- this will return closest parent value because of "ORDER BY LEVEL"

        EXCEPTION
        WHEN OTHERS THEN
        v_old_parent_id := NULL; -- shouldn't happen
        END;
                                                                
        v_stage := 'update to new parent_node_id: '||parent_rec.old_parent_node_id; 
        UPDATE BUDGET_MOVE
        SET    old_parent_node_id = v_old_parent_id
        WHERE  import_file_id = p_import_file_id
        AND    old_parent_node_id = parent_rec.old_parent_node_id;             
        
    END;  
    
  END LOOP; -- FOR parent_rec IN parent_old_cur
  
  COMMIT;
                
--------------------------------------------------------------------------------

  v_stage := 'write error for missing node_id';
  INSERT INTO BUDGET_MOVE_ERROR
  (budget_move_error_id, import_file_id, import_record_id, 
   error_msg, date_created, created_by)
  SELECT BUDGET_MOVE_ERROR_PK_SQ.nextval,
         import_file_id,
         import_record_id,
         'NODE_ID not found' error_msg,
         SYSDATE,
         0
  FROM   BUDGET_MOVE
  WHERE  import_file_id = p_import_file_id
  AND    node_id IS NULL;
  
  v_no_node_count := SQL%ROWCOUNT;

 v_stage := 'write error for missing parent_node_id';
  INSERT INTO BUDGET_MOVE_ERROR
  (budget_move_error_id, import_file_id, import_record_id, 
   error_msg, date_created, created_by)
  SELECT BUDGET_MOVE_ERROR_PK_SQ.nextval,
         import_file_id,
         import_record_id,
         'PARENT_NODE_ID not found' error_msg,
         SYSDATE,
         0
  FROM   BUDGET_MOVE
  WHERE  import_file_id = p_import_file_id
  AND    (new_parent_node_id IS NULL
                OR     old_parent_node_id IS NULL);
  
  v_no_parent_count := SQL%ROWCOUNT;
  
  COMMIT;
                
--------------------------------------------------------------------------------
                
  v_stage := 'update BUDGET_MOVE owner ids';
                UPDATE BUDGET_MOVE bm
                SET    bm.node_own_user_id = (SELECT user_id
                                              FROM   user_node
                                              WHERE  role = 'own'
                                              AND    node_id = bm.node_id)
                WHERE  bm.import_file_id = p_import_file_id
                AND    bm.node_id IS NOT NULL;
                
                UPDATE BUDGET_MOVE bm
                SET    bm.old_parent_own_user_id = (SELECT user_id
                                                                                                                                                                                                                                                                                                                FROM   user_node
                                                                                                                                                                                                                                                                                                                WHERE  role = 'own'
                                                                                                                                                                                                                                                                                                                AND    node_id = bm.old_parent_node_id)
                WHERE  bm.import_file_id = p_import_file_id
                AND    bm.old_parent_node_id IS NOT NULL;
                
                COMMIT;
                
--------------------------------------------------------------------------------
                
  v_stage := 'update BUDGET_MOVE.is_budget_move_needed for "upd" recs';
                UPDATE BUDGET_MOVE bm
                SET    bm.is_budget_move_needed = 1
                WHERE  bm.import_file_id = p_import_file_id
                AND    bm.budget_id_list IS NOT NULL
                AND    bm.record_status = 'upd'
                AND    bm.old_parent_node_id IS NOT NULL
                AND    bm.old_parent_node_id <> bm.new_parent_node_id -- nothing to move if parent hasn't changed
                AND NOT EXISTS (SELECT 'x'
                                FROM   BUDGET_MOVE_ERROR
                                WHERE  import_record_id = bm.import_record_id);        
                
  v_stage := 'update BUDGET_MOVE.is_budget_move_needed for "del" recs';
                UPDATE BUDGET_MOVE bm
                SET    bm.is_budget_move_needed = 1
                WHERE  bm.import_file_id = p_import_file_id
                AND    bm.budget_id_list IS NOT NULL
                AND    bm.record_status = 'del'
                AND    bm.old_parent_node_id IS NOT NULL
                AND NOT EXISTS (SELECT 'x'
                                FROM   BUDGET_MOVE_ERROR
                                WHERE  import_record_id = bm.import_record_id);
                
                COMMIT;
                
--------------------------------------------------------------------------------
                
  v_stage := 'loop thru recs in BUDGET_MOVE';
  FOR move_rec IN move_cur LOOP
    v_read_count := v_read_count + 1;        
                  
    v_stage := 'loop thru budgets for node: '||move_rec.node_id;
    FOR budget_rec IN budget_cur (move_rec.budget_id_list) LOOP
                                                                                                                                                    
      v_budget_count := v_budget_count + 1;
      
      lr_node_budget        := NULL;
      lr_parent_budget      := NULL;
      lr_node_budget_hist   := NULL;
      lr_parent_budget_hist := NULL;
      lr_email              := NULL;                                 
                
--------------------------------------------------------------------------------
---------------------------------GET NODE INFO----------------------------------
--------------------------------------------------------------------------------
                                
      v_stage := 'get node budget rec: '||budget_rec.budget_id;
      SELECT *
      INTO   lr_node_budget
      FROM   budget
      WHERE  budget_id = budget_rec.budget_id
      FOR UPDATE OF original_value; -- 03/20/2019 lock rec to prevent anything else from altering this rec
      
      v_stage := 'get node budget_history rec: '||lr_node_budget.budget_id;
      SELECT *
      INTO   lr_node_budget_hist
      FROM   budget_history bh
      WHERE  budget_id = lr_node_budget.budget_id
      AND    budget_history_id IN (SELECT MAX(budget_history_id)
                                   FROM   budget_history
                                   WHERE  budget_id = lr_node_budget.budget_id);
                                                
--------------------------------------------------------------------------------
-------------------------------GET PARENT_NODE INFO-----------------------------
--------------------------------------------------------------------------------
      
      BEGIN
        v_stage := 'get parent_node budget rec: '||move_rec.move_to_node_id||'~'||lr_node_budget.budget_segment_id;
        SELECT *
        INTO   lr_parent_budget
        FROM   budget
        WHERE  node_id = move_rec.move_to_node_id
        AND    budget_segment_id = lr_node_budget.budget_segment_id
				FOR UPDATE OF original_value; -- 03/20/2019 lock rec to prevent anything else from altering this rec
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          v_stage := 'build parent_node budget rec: '||move_rec.move_to_node_id||'~'||lr_node_budget.budget_segment_id;
          lr_parent_budget                   := NULL;
          lr_parent_budget.budget_id         := BUDGET_PK_SQ.nextval;
          lr_parent_budget.budget_segment_id := lr_node_budget.budget_segment_id;
          lr_parent_budget.node_id           := move_rec.move_to_node_id;
          lr_parent_budget.original_value    := 0;
          lr_parent_budget.current_value     := 0;
          lr_parent_budget.status            := 'active';
          lr_parent_budget.action_type       := 'deposit';
          lr_parent_budget.created_by        := 0;
          lr_parent_budget.date_created      := SYSDATE;
          lr_parent_budget.version           := 0;
          INSERT INTO budget VALUES lr_parent_budget;
          
          v_parent_budget_ins := v_parent_budget_ins + 1;
      END;
      
      BEGIN
        v_stage := 'get parent_node budget_history rec: '||lr_parent_budget.budget_id;
        SELECT *
        INTO   lr_parent_budget_hist
        FROM   budget_history bh
        WHERE  budget_id = lr_parent_budget.budget_id
        AND    budget_history_id IN (SELECT MAX(budget_history_id)
                                       FROM budget_history
                                      WHERE budget_id = lr_parent_budget.budget_id);
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        lr_parent_budget_hist := NULL;
    END;
                                                
    IF lr_parent_budget.status <> 'active' THEN
                                                
        v_stage := 'write error for inactive parent budget';
        INSERT INTO BUDGET_MOVE_ERROR
        (budget_move_error_id, import_file_id, import_record_id, 
         error_msg, date_created, created_by)
        VALUES
        (BUDGET_MOVE_ERROR_PK_SQ.nextval,
        move_rec.import_file_id,
        move_rec.import_record_id,
        'MOVE-TO parent_budget_id is not active: '||lr_parent_budget.budget_id,
        SYSDATE,
        0);
                                                                    
        v_closed_parent_budget := v_closed_parent_budget + 1;
        
    END IF; -- IF lr_parent_budget.status <> 'active'
                                
--------------------------------------------------------------------------------
---------------------------MAINTAIN PARENT_NODE REC-----------------------------
--------------------------------------------------------------------------------
                                                
    v_stage := 'build parent_node budget_hist rec: '||lr_parent_budget.budget_id;
    lr_parent_budget_hist.budget_history_id   := BUDGET_HISTORY_PK_SQ.nextval;
    lr_parent_budget_hist.budget_id           := lr_parent_budget.budget_id;
    lr_parent_budget_hist.original_value_before_xaction := NVL(lr_parent_budget_hist.original_value_after_xaction,0);
    lr_parent_budget_hist.current_value_before_xaction  := NVL(lr_parent_budget_hist.current_value_after_xaction,0);
    lr_parent_budget_hist.original_value_after_xaction  := NVL(lr_parent_budget_hist.original_value_after_xaction,0) + lr_node_budget_hist.current_value_after_xaction;
    lr_parent_budget_hist.current_value_after_xaction   := NVL(lr_parent_budget_hist.current_value_after_xaction,0) + lr_node_budget_hist.current_value_after_xaction;
    lr_parent_budget_hist.action_type         := 'deposit';
    lr_parent_budget_hist.created_by          := 0;
    lr_parent_budget_hist.date_created        := SYSDATE;
    lr_parent_budget_hist.reason_code    := 'transfer';
    lr_parent_budget_hist.from_budget_id := lr_node_budget.budget_id;
    lr_parent_budget_hist.to_budget_id   := NULL;
			lr_parent_budget_hist.claim_id            := NULL;
        
    INSERT INTO budget_history VALUES lr_parent_budget_hist;
                                                        
    v_stage := 'update parent_node budget rec: '||lr_parent_budget.budget_id;
    UPDATE budget
			SET    original_value = original_value + lr_node_budget_hist.current_value_after_xaction,  -- 02/08/2019
           current_value  = current_value + lr_node_budget_hist.current_value_after_xaction,
           action_type    = 'deposit',
           transfer_method = c_xfer_method,
           modified_by    = 0,
           date_modified  = SYSDATE,
           VERSION        = VERSION + 1
     WHERE budget_id      = lr_parent_budget.budget_id;                                     
                                   
--------------------------------------------------------------------------------
-------------------------------MAINTAIN NODE REC--------------------------------
--------------------------------------------------------------------------------
                                                
    v_stage := 'build node budget_hist rec: '||lr_node_budget.budget_id;
    lr_node_budget_hist.budget_history_id   := BUDGET_HISTORY_PK_SQ.nextval;
    lr_node_budget_hist.budget_id           := lr_node_budget.budget_id;
    lr_node_budget_hist.original_value_before_xaction := lr_node_budget_hist.original_value_after_xaction;
    lr_node_budget_hist.current_value_before_xaction  := lr_node_budget_hist.current_value_after_xaction;
    lr_node_budget_hist.original_value_after_xaction  := lr_node_budget_hist.original_value_after_xaction; -- retains same value
    lr_node_budget_hist.current_value_after_xaction   := 0; -- resetting balance
    lr_node_budget_hist.action_type         := 'deduct';
    lr_node_budget_hist.created_by          := 0;
    lr_node_budget_hist.date_created        := SYSDATE;
    lr_node_budget_hist.reason_code    := 'transfer';
    lr_node_budget_hist.from_budget_id := NULL;
    lr_node_budget_hist.to_budget_id   := lr_parent_budget.budget_id;
			lr_node_budget_hist.claim_id            := NULL;
                                                
    INSERT INTO budget_history VALUES lr_node_budget_hist;
                                                                                                
    v_stage := 'update node budget rec: '||lr_node_budget.budget_id;
    UPDATE budget
    SET    current_value  = 0, -- resetting balance
           --status         = 'closed',         --05/03/2019
           action_type    = 'deduct',
           transfer_method = c_xfer_method,
           modified_by    = 0,
           date_modified  = SYSDATE,
           VERSION        = VERSION + 1
    WHERE  budget_id      = lr_node_budget.budget_id;
                                                
--------------------------------------------------------------------------------
                                                
    v_stage := 'create BUDGET_MOVE_EMAIL for rec: '||move_rec.budget_move_id;
    lr_email.budget_move_email_id := BUDGET_MOVE_EMAIL_PK_SQ.nextval;
    lr_email.from_node_id         := move_rec.node_id;
    lr_email.from_node_owner_id   := move_rec.node_own_user_id;
    lr_email.from_budget_id       := budget_rec.budget_id;
    lr_email.to_node_id           := move_rec.move_to_node_id;
    lr_email.to_node_owner_id     := move_rec.move_to_own_user_id;
    lr_email.to_budget_id         := lr_parent_budget.budget_id;                                        
    lr_email.budget_amount        := lr_node_budget.current_value;         
    lr_email.budget_transfer_date := TRUNC(SYSDATE);
    lr_email.date_created         := SYSDATE;
    lr_email.created_by           := 0;
    
    INSERT INTO BUDGET_MOVE_EMAIL VALUES lr_email; 
    
    v_email_count := v_email_count + 1;
                                
    END LOOP; -- FOR budget_rec IN

        v_stage := 'update date_processed for rec: '||move_rec.budget_move_id;
        UPDATE budget_move
        SET    date_processed = SYSDATE
        WHERE  budget_move_id = move_rec.budget_move_id;
		
		COMMIT; -- 03/20/2019 to release FOR UPDATE OF locks
                                
    END LOOP; -- FOR move_rec IN move_cur
                
--------------------------------------------------------------------------------

    v_stage := 'update date_processed for remaining recs in file';
    UPDATE budget_move
    SET    date_processed = SYSDATE
    WHERE  import_file_id = p_import_file_id
    AND    is_budget_move_needed = 0
    AND    date_processed IS NULL;
    
    COMMIT; -- wait until end
                
--------------------------------------------------------------------------------
                
    SELECT COUNT(1)
    INTO   v_no_budgets_count
    FROM   budget_move
    WHERE  import_file_id = p_import_file_id
    AND    budget_id_list IS NULL;
    
    SELECT COUNT(1)
    INTO   v_ttl_count
    FROM   budget_move
    WHERE  import_file_id = p_import_file_id;
  
  v_msg := 'Success for file_id: '||p_import_file_id||CHR(10)||
           'Nbr recs on file: '||v_ttl_count||CHR(10)||
           'Nbr recs processed: '||v_read_count||CHR(10)||
           'Nbr budgets moved: '||v_budget_count||CHR(10)||
           'Nbr parent budgets inserted (because not already existing): '||v_parent_budget_ins||CHR(10)||
           'Nbr recs with no budgets to move: '||v_no_budgets_count||CHR(10)||
           'Nbr recs written to BUDGET_MOVE_EMAIL: '||v_email_count||CHR(10)||
           '**Nbr ERRORS, node_id not found: '||v_no_node_count||CHR(10)||
           '**Nbr ERRORS, parent_node_id not found: '||v_no_parent_count||CHR(10)||
           '**Nbr ERRORS, parent budget "closed": '||v_closed_parent_budget||CHR(10)||
           '**These recs are recorded in BUDGET_MOVE_ERROR and can be queried by import_file_id';
                prc_execution_log_entry(c_process_name,1,'INFO',v_msg,NULL);

EXCEPTION
  WHEN OTHERS THEN      
    ROLLBACK;
    p_out_returncode := 99;
    v_msg := 'OTHER error: '||SQLERRM||'-->stage: '||v_stage;
    prc_execution_log_entry(c_process_name,1,'ERROR',v_msg,NULL);
END prc_budget_move_transfer;
/
