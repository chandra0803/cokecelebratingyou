CREATE OR REPLACE PROCEDURE prc_budget_transfer
        (p_in_budgetid       IN   NUMBER,
         p_in_budget_mas_id  IN   NUMBER,
         p_in_budget_seg_id  IN   NUMBER,
         p_in_media_ratio    IN   NUMBER,
         p_out_return_code  OUT   VARCHAR2,
         p_out_result_set   OUT   sys_refcursor) 
AS
   PRAGMA AUTONOMOUS_TRANSACTION;
  /*********************************************************
  -- Purpose: 
  -- This procedure returns the result set which will be used for 'Extract Existing budget of either node or pax' for Admin.
  -- Person      Date        Comments
  -- ---------   ----------  -----------------------------------------------------
  -- kiran kumar 01/28/2015 Initial Versionl  
  --Suresh J     04/22/2015 Bug # 61421 - Fixed SQL Exception ORA-01422: exact fetch returns more than requested number of rows    
  --nagarajs     07/06/2016 Bug 66879 - Participant Budget Transfer is creating Zero Point Budgets
  --Cheri Bethke 02/17/2017 Bug # 71538 - Budget Transfer Fails When BI-ADMIN is Inactive
  --DeepakrajS   04/22/2019 Git#98 - Budget trasnfer is allowed for closed budgets  
  *********************************************************/
  v_budget_master_id     budget_master.budget_master_id%TYPE;
  v_primaudtype          NUMBER:=0;
  
  -- constants
  c_delimiter            CONSTANT VARCHAR2(1) := ',' ;
  c2_delimiter           CONSTANT VARCHAR2(1) := '"' ;    
  c_process_name         CONSTANT execution_log.process_name%TYPE:='PRC_BUDGET_TRANSFER';
  c_severity_i           CONSTANT execution_log.severity%TYPE:='INFO';
  c_severity_e           CONSTANT execution_log.severity%TYPE:='ERROR';
  
  v_textmsg              varchar2(1000);
  -- local variables
  v_budgettype           budget_master.budget_type%TYPE;
  --Node based
  v_nodeid               budget.node_id%TYPE;
  v_createdid            application_user.user_id%TYPE;
  v_budget_Id            budget.budget_id%TYPE;
  v_newbudgetId          budget.budget_id%TYPE;    
  
  v_budget_segment_id    budget_segment.budget_segment_id%TYPE;
  v_budgetreallalow      budget_segment.is_allow_budget_reallocation%TYPE;
  v_budgetrealleligType  budget_segment.budget_reallocation_elig_type%TYPE;
  --pax based
  v_PaxId                budget.user_id%TYPE;  
  v_nodecnt              NUMBER:=0;    
  v_rowcnt               NUMBER:=0;
    
  flderr                 EXCEPTION;    
  v_idmismatch           BOOLEAN:=FALSE;
  v_nodeuserflg          BOOLEAN:=FALSE;
  v_errormsg             VARCHAR2(400):=null;
  v_budg_his_id          budget_history.budget_history_id%TYPE;
  c_action_type          CONSTANT BUDGET.action_type%TYPE:='transfer';
    
BEGIN  

  --v_budget_Id        := to_number(trim(p_in_budgetId));
  --v_budget_master_id    := to_number(trim(p_in_budget_mas_id));
  v_budget_Id        := p_in_budgetId;
  v_budget_master_id  := p_in_budget_mas_id;
  v_budget_segment_id := p_in_budget_seg_id;
  --Retrieving the Budget Type
  v_errormsg    := 'fetching Budget Type ['||v_budget_master_id||']';
  v_textmsg     := 'Params: p_in_budgetId: '||p_in_budgetId||'p_in_budget_mas_id: '||p_in_budget_mas_id;
  prc_execution_log_entry(c_process_name,1,c_severity_i,'Process Started '||v_textmsg,null);
  
  --check if p_in_budget_mas_id exists in budget_master
  BEGIN 
  
    SELECT budget_type
      INTO v_budgettype
      FROM budget_master
     WHERE budget_master_id = v_budget_master_id;
    
  EXCEPTION
    WHEN NO_DATA_FOUND THEN    
      v_errormsg    := 'Budget Master Id not available in the system '||SQLERRM;
      RAISE flderr;
      
  END;
  
   --check if p_in_budget_seg_id exists in budget_segment
  BEGIN 
  
    SELECT is_allow_budget_reallocation,
           budget_reallocation_elig_type
      INTO v_budgetreallalow,
           v_budgetrealleligType
      FROM budget_segment
     WHERE budget_segment_id = v_budget_segment_id
       AND budget_master_id  = v_budget_master_id;
    
  EXCEPTION
    WHEN NO_DATA_FOUND THEN    
      v_errormsg    := 'Budget Segment Id not available in the system '||SQLERRM;
      RAISE flderr;
      
  END;
  /* --02/17/2017
  v_errormsg    := 'Retrieving the Creator Id';
  SELECT user_id 
    INTO v_createdid
    FROM application_user
   WHERE user_name = 'BI-ADMIN' 
     AND is_active = 1;*/
  
  DELETE FROM tmp_budget_transfer;  --07/06/2016
  
  v_errormsg    := 'Fetching Node id for ['||v_budget_Id||'] ['||v_budget_master_id||']';
  IF (v_budgettype = 'node') THEN --budget type validation if starts
  
    --Retrieving the node id for the budget allocated
    BEGIN --Node id block starts
    
      SELECT NVL(node_id,-1) 
        INTO v_nodeid
        FROM budget bt, budget_segment bs
       WHERE budget_id = v_budget_Id 
         AND bt.budget_segment_id = bs.budget_segment_id
         AND bt.budget_segment_id = v_budget_segment_id
         AND bs.budget_master_id = v_budget_master_id         ;
      
    EXCEPTION    
      WHEN NO_DATA_FOUND THEN
      
        BEGIN
        
          SELECT NVL(node_id,-1) 
            INTO v_nodeid
            FROM budget 
           WHERE budget_id = v_budget_Id 
             AND budget_segment_id <> v_budget_segment_id;
          v_idmismatch    := TRUE;
          
        EXCEPTION        
          WHEN OTHERS THEN          
            v_errormsg    := 'budget id not available in the system '||SQLERRM;
            RAISE flderr;
            
        END;
        
    END; -- Node id block ends
    
    IF v_nodeid <> -1 THEN --Node id validation if starts
    
      if v_idmismatch THEN --Budget id and budget segment id validation if starts
      
        v_errormsg    := 'Budget ID and Budget Segment id are not related';
        RAISE flderr;
        
      ELSE
      
        SELECT COUNT(1)
          INTO  v_nodecnt
          FROM  node n
         WHERE  is_deleted = 0 
           AND NOT EXISTS (SELECT 'X' 
                             FROM budget b, budget_segment bs
                            WHERE b.node_id = n.node_id                               
                              AND b.budget_segment_id = v_budget_segment_id
                              AND b.budget_segment_id = bs.budget_segment_id
                              AND bs.budget_master_id = v_budget_master_id)
         START WITH node_id     = v_nodeid 
       CONNECT BY PRIOR node_id = parent_node_id;
              
        IF v_nodecnt>0 THEN --Budget not available for nodes if starts
        
          INSERT INTO tmp_budget_transfer --07/06/2016
            SELECT node_id
                  FROM node n
                 WHERE is_deleted = 0
                   AND NOT EXISTS (SELECT 'X' 
                                     FROM budget b, budget_segment bs
                                    WHERE b.node_id = n.node_id                               
                                      AND b.budget_segment_id = v_budget_segment_id
                                      AND b.budget_segment_id = bs.budget_segment_id
                                      AND bs.budget_master_id = v_budget_master_id)
                 START WITH NODE_ID       = v_nodeid
               CONNECT BY PRIOR NODE_ID   = PARENT_NODE_ID;
               
          --Budget insertion
          /*MERGE INTO BUDGET B --07/06/2016
          USING
               (SELECT node_id, 
                       v_budget_segment_id budgSegId
                  FROM node n
                 WHERE is_deleted = 0
                   AND NOT EXISTS (SELECT 'X' 
                                     FROM budget b, budget_segment bs
                                    WHERE b.node_id = n.node_id                               
                                      AND b.budget_segment_id = v_budget_segment_id
                                      AND b.budget_segment_id = bs.budget_segment_id
                                      AND bs.budget_master_id = v_budget_master_id)
                 START WITH NODE_ID       = v_nodeid
               CONNECT BY PRIOR NODE_ID   = PARENT_NODE_ID) T
          ON (b.budget_segment_id = T.budgSegId
              AND b.NODE_ID      = T.NODE_ID)
          WHEN NOT MATCHED THEN
            INSERT (budget_id,
                    budget_segment_id,
                    user_id,
                    node_id,
                    original_value,
                    current_value,
                    overdrawn,
                    status,
                   -- start_date,
                   -- end_date,
                    action_type,
                    created_by,
                    date_created,
                    modified_by,
                    date_modified,
                    version,
                    effective_date)
            VALUES
                    (budget_pk_sq.NEXTVAL,
                    T.budgSegId,
                    '',
                    T.node_id,
                    0,
                    0,
                    NULL,
                    'active',
                    --SYSDATE,
                    --NULL,
                    c_action_type,
                    v_createdid,
                    SYSDATE,
                    NULL,
                    NULL,
                    0,
                    NULL);
                    
           --Budget History insertion
           MERGE INTO budget_history bh
           USING (
                  SELECT budget_id
                    FROM budget bt, budget_segment bs
                   WHERE bt.budget_segment_id = v_budget_segment_id
                     AND bt.budget_segment_id = bs.budget_segment_id 
                     AND bs.budget_master_id = v_budget_master_id 
           ) b
           ON (b.budget_id = bh.budget_id)
           WHEN NOT MATCHED THEN
            INSERT (budget_history_id,
                    budget_id,
                    original_value_before_xaction,
                    current_value_before_xaction,
                    original_value_after_xaction,
                    current_value_after_xaction,
                    action_type,
                    created_by,
                    date_created)
            VALUES
                    (budget_history_pk_sq.NEXTVAL,
                    b.budget_id,
                    0,
                    0,
                    0,
                    0,
                    c_action_type,
                    v_createdid,
                    SYSDATE);*/

        END IF; --Budget not available for nodes if ends
        
      END IF; --Budget id and budget master id validation if ends
      
    ELSE
    
      v_errormsg    := 'budget is not assigned to node';
      RAISE flderr;
      
    END IF; --Node id validation if ends
    
    OPEN p_out_result_set FOR
        SELECT budget_id      
               ,own_node_id    
               ,node_id        
               ,NAME    
               ,NULL user_id       
               ,budget_spent   
               ,current_value   
          FROM (SELECT d.node_id        node_id,
                       d.name           name,
                       ROUND(((b.original_value-b.current_value) * nvl(p_in_media_ratio,0)),2) budget_spent,
                       ROUND((b.current_value  * nvl(p_in_media_ratio,0)),2)                   current_value, 
                       d.parent_node_id parent_node_id,
                       b.budget_id      budget_id,
                       v_nodeid         own_node_id
                  FROM budget b,
                       budget_segment bs, 
                       node d
                 WHERE b.node_id          = d.node_id 
                   AND d.is_deleted       = 0 
                   AND b.budget_segment_id = v_budget_segment_id
                   AND b.budget_segment_id = bs.budget_segment_id
                   AND bs.budget_master_id = v_budget_master_id
                 UNION
                SELECT n.node_id, --07/06/2016
                       n.name,
                       0 budget_spent,
                       0 current_value,
                       n.parent_node_id,
                       NULL budget_id, 
                       v_nodeid own_node_id
                  FROM tmp_budget_transfer t,
                       node  n
                 WHERE t.user_node_id = n.node_id
                   )
         WHERE node_id <> v_nodeid 
         START WITH node_id     = v_nodeid 
       CONNECT BY PRIOR node_id = parent_node_id;

  ELSE
  
    --Pax Budget retrieval
    v_errormsg    := 'Processing for pax';
    BEGIN --pax id fetching block starts
    
      SELECT NVL(user_id,-1)
        INTO v_PaxId
        FROM budget b, budget_segment bs
       WHERE budget_id        = v_budget_Id 
         AND b.budget_segment_id = v_budget_segment_id
         AND b.budget_segment_id = bs.budget_segment_id
         AND bs.budget_master_id = v_budget_master_id;
             
    EXCEPTION    
      WHEN NO_DATA_FOUND THEN
      
        BEGIN
        
           SELECT NVL(user_id,-1)
        INTO v_PaxId
        FROM budget b, budget_segment bs
       WHERE budget_id        = v_budget_Id 
         AND b.budget_segment_id = v_budget_segment_id
         AND b.budget_segment_id <> bs.budget_segment_id
         AND bs.budget_master_id = v_budget_master_id;
                  
          v_idmismatch    := TRUE;
          
        EXCEPTION        
          WHEN NO_DATA_FOUND THEN
          
            v_errormsg    := 'budget not available '||SQLERRM;
            RAISE flderr;
            
        END;
        
    END; --pax id fetching block ends
    
    IF v_PaxId <> -1 THEN --pax id validation if starts
    
      IF(v_idmismatch) THEN --Budget id and budget master id validation if starts
      
        v_errormsg    := 'Budget id and budget master id are not releated';
        RAISE flderr;
        
      ELSE
      
        SELECT node_id
          INTO v_nodeid
          FROM user_node
         WHERE user_id = v_PaxId
               AND is_primary = 1;   --04/22/2015 
        
        IF v_budgetreallalow <> 0 THEN --budget allocation available validation if starts
        
          SELECT SUM(CASE WHEN primary_audience_type = 'allactivepaxaudience' THEN 1 ELSE 0 END) 
            INTO v_primaudtype 
            FROM promotion 
           WHERE award_budget_master_id = v_budget_master_id 
             AND promotion_status = 'live';
             
          IF v_primaudtype >0 THEN --(
            v_nodeuserflg := TRUE;
          END IF;--)
          
            IF v_budgetrealleligType = 'orgunit' THEN --(
              INSERT INTO tmp_budget_transfer --07/06/2016
                SELECT user_id 
                    FROM (SELECT un.user_id AS user_id 
                            FROM user_node un
                           WHERE un.node_id = v_nodeid 
                             AND EXISTS (SELECT 1 
                                           FROM application_user au 
                                          WHERE au.user_id   = un.user_id 
                                            AND au.is_active = 1)
                              AND EXISTS (SELECT 1 
                                            FROM node n 
                                           WHERE n.node_id    = un.node_id 
                                             AND n.is_deleted = 0)
    
                          INTERSECT
                          (
                             SELECT parau.user_id AS user_id 
                               FROM participant_audience parau ,
                                    promo_audience proau, 
                                    promotion pro
                              WHERE parau.audience_id          = proau.audience_id 
                                AND proau.promotion_id         = pro.promotion_id 
                                AND proau.promo_audience_type  = 'PRIMARY' 
                                AND pro.award_budget_master_id = v_budget_master_id 
                                AND pro.promotion_status       = 'live' 
                                AND EXISTS (SELECT 1 
                                              FROM application_user au 
                                             WHERE au.user_id   = parau.user_id 
                                               AND au.is_active = 1)
                             UNION            
                             SELECT un.user_id AS user_id 
                               FROM user_node un
                              WHERE un.node_id  IN (SELECT n.node_id 
                                                      FROM node n 
                                                     WHERE n.is_deleted       = 0  
                                                     START WITH n.node_id     = v_nodeid 
                                                   CONNECT BY PRIOR n.node_id = n.parent_node_id)
                                AND 0< v_primaudtype
                          )
                   ) T
                   WHERE NOT EXISTS (SELECT 1 
                                       FROM budget b, budget_segment bs
                                      WHERE NVL(b.user_id,-1)  = t.user_id 
                                        AND b.budget_segment_id = v_budget_segment_id
                                        AND b.budget_segment_id = bs.budget_segment_id
                                        AND bs.budget_master_id = v_budget_master_id);
                                        
                --Budget for orgunit insertion 
              /*MERGE INTO BUDGET B --07/06/2016
              USING
              (
                  SELECT user_id ,
                         v_budget_segment_id budgSegId
                    FROM (SELECT un.user_id AS user_id 
                            FROM user_node un
                           WHERE un.node_id = v_nodeid 
                             AND EXISTS (SELECT 1 
                                           FROM application_user au 
                                          WHERE au.user_id   = un.user_id 
                                            AND au.is_active = 1)
                              AND EXISTS (SELECT 1 
                                            FROM node n 
                                           WHERE n.node_id    = un.node_id 
                                             AND n.is_deleted = 0)
    
                          INTERSECT
                          (
                             SELECT parau.user_id AS user_id 
                               FROM participant_audience parau ,
                                    promo_audience proau, 
                                    promotion pro
                              WHERE parau.audience_id          = proau.audience_id 
                                AND proau.promotion_id         = pro.promotion_id 
                                AND proau.promo_audience_type  = 'PRIMARY' 
                                AND pro.award_budget_master_id = v_budget_master_id 
                                AND pro.promotion_status       = 'live' 
                                AND EXISTS (SELECT 1 
                                              FROM application_user au 
                                             WHERE au.user_id   = parau.user_id 
                                               AND au.is_active = 1)
                             UNION            
                             SELECT un.user_id AS user_id 
                               FROM user_node un
                              WHERE un.node_id  IN (SELECT n.node_id 
                                                      FROM node n 
                                                     WHERE n.is_deleted       = 0  
                                                     START WITH n.node_id     = v_nodeid 
                                                   CONNECT BY PRIOR n.node_id = n.parent_node_id)
                                AND 0< v_primaudtype
                          )
                   ) T
                   WHERE NOT EXISTS (SELECT 1 
                                       FROM budget b, budget_segment bs
                                      WHERE NVL(b.user_id,-1)  = t.user_id 
                                        AND b.budget_segment_id = v_budget_segment_id
                                        AND b.budget_segment_id = bs.budget_segment_id
                                        AND bs.budget_master_id = v_budget_master_id)
              ) t
              ON (b.budget_segment_id = t.budgSegId
                  AND b.USER_ID = t.USER_ID)
              WHEN NOT MATCHED THEN
                  INSERT (budget_id,
                          budget_segment_id,
                          user_id,
                          node_id,
                          original_value,
                          current_value,
                          overdrawn,
                          status,
                          --start_date,
                          --end_date,
                          action_type,
                          created_by,
                          date_created,
                          modified_by,
                          date_modified,
                          version,
                          effective_date
                      ) VALUES (
                          budget_pk_sq.NEXTVAL,
                          t.budgSegId,
                          t.USER_ID,
                          '',
                          0,
                          0,
                          NULL,
                          'active',
                         -- SYSDATE,
                         --NULL,
                          c_action_type,
                          v_createdid,
                          SYSDATE,
                          NULL,
                          NULL,
                          0,
                          NULL);
              --Budget History for org unit insertion      
              MERGE INTO budget_history bh
              USING (
                      SELECT budget_id
                        FROM budget bt, budget_segment bs
                       WHERE bt.budget_segment_id = v_budget_segment_id
                         AND bt.budget_segment_id = bs.budget_segment_id
                         AND bs.budget_master_id = v_budget_master_id
              ) b
              ON (b.budget_id = bh.budget_id)
              WHEN NOT MATCHED THEN
                  INSERT (budget_history_id,
                          budget_id,
                          original_value_before_xaction,
                          current_value_before_xaction,
                          original_value_after_xaction,
                          current_value_after_xaction,
                          action_type,
                          created_by,
                          date_created)
                  VALUES
                          (budget_history_pk_sq.NEXTVAL,
                           b.budget_id,
                           0,
                           0,
                           0,
                           0,
                           c_action_type,
                           v_createdid,
                           SYSDATE);  */            
            ELSE
              --Budget insertion for orgunibelow
              INSERT INTO tmp_budget_transfer --07/06/2016
                SELECT user_id 
                       FROM (SELECT un.user_id AS user_id 
                               FROM user_node un
                              WHERE un.node_id  IN (SELECT n.node_id 
                                                      FROM node n 
                                                     WHERE n.is_deleted       = 0  
                                                     START WITH n.node_id     = v_nodeid 
                                                   CONNECT BY PRIOR n.node_id = n.parent_node_id) 
                                AND EXISTS (SELECT 1 
                                              FROM application_user au 
                                             WHERE au.user_id   = un.user_id 
                                               AND au.is_active = 1)
                             INTERSECT
                             (
                                SELECT parau.user_id AS user_id 
                                  FROM participant_audience parau ,
                                       promo_audience proau, 
                                       promotion pro
                                 WHERE parau.audience_id          = proau.audience_id 
                                   AND proau.promotion_id         = pro.promotion_id 
                                   AND proau.promo_audience_type  = 'PRIMARY' 
                                   AND pro.award_budget_master_id = v_budget_master_id 
                                   AND pro.promotion_status       = 'live' 
                                   AND EXISTS (SELECT 1 
                                                 FROM application_user au 
                                                WHERE au.user_id   = parau.user_id 
                                                  AND au.is_active = 1)
                                UNION            
                                SELECT un.user_id AS user_id 
                                  FROM user_node un
                                 WHERE un.node_id  IN (SELECT n.node_id 
                                                         FROM node n 
                                                        WHERE n.is_deleted       = 0  
                                                        START WITH n.node_id     = v_nodeid 
                                                      CONNECT BY PRIOR n.node_id = n.parent_node_id)
                                AND 0< v_primaudtype 
                             )
                            ) T
                      WHERE NOT EXISTS (SELECT 1 
                                          FROM budget B, budget_segment BS
                                         WHERE NVL(b.user_id,-1)  = t.user_id 
                                           AND b.budget_segment_id = v_budget_segment_id
                                           AND b.budget_segment_id = bs.budget_segment_id
                                           AND bs.budget_master_id = v_budget_master_id);
                                           
              /*MERGE INTO BUDGET B
              USING (
                    SELECT user_id , 
                            v_budget_segment_id budgSegId
                       FROM (SELECT un.user_id AS user_id 
                               FROM user_node un
                              WHERE un.node_id  IN (SELECT n.node_id 
                                                      FROM node n 
                                                     WHERE n.is_deleted       = 0  
                                                     START WITH n.node_id     = v_nodeid 
                                                   CONNECT BY PRIOR n.node_id = n.parent_node_id) 
                                AND EXISTS (SELECT 1 
                                              FROM application_user au 
                                             WHERE au.user_id   = un.user_id 
                                               AND au.is_active = 1)
                             INTERSECT
                             (
                                SELECT parau.user_id AS user_id 
                                  FROM participant_audience parau ,
                                       promo_audience proau, 
                                       promotion pro
                                 WHERE parau.audience_id          = proau.audience_id 
                                   AND proau.promotion_id         = pro.promotion_id 
                                   AND proau.promo_audience_type  = 'PRIMARY' 
                                   AND pro.award_budget_master_id = v_budget_master_id 
                                   AND pro.promotion_status       = 'live' 
                                   AND EXISTS (SELECT 1 
                                                 FROM application_user au 
                                                WHERE au.user_id   = parau.user_id 
                                                  AND au.is_active = 1)
                                UNION            
                                SELECT un.user_id AS user_id 
                                  FROM user_node un
                                 WHERE un.node_id  IN (SELECT n.node_id 
                                                         FROM node n 
                                                        WHERE n.is_deleted       = 0  
                                                        START WITH n.node_id     = v_nodeid 
                                                      CONNECT BY PRIOR n.node_id = n.parent_node_id)
                                AND 0< v_primaudtype 
                             )
                            ) T
                      WHERE NOT EXISTS (SELECT 1 
                                          FROM budget B, budget_segment BS
                                         WHERE NVL(b.user_id,-1)  = t.user_id 
                                           AND b.budget_segment_id = v_budget_segment_id
                                           AND b.budget_segment_id = bs.budget_segment_id
                                           AND bs.budget_master_id = v_budget_master_id)
                    ) R
              ON (b.budget_segment_id = r.budgSegId
                    AND b.USER_ID = r.USER_ID
                 )
              WHEN NOT MATCHED THEN
                    INSERT (budget_id,
                            budget_segment_id,
                            user_id,
                            node_id,
                            original_value,
                            current_value,
                            overdrawn,
                            status,
                            --start_date,
                            --end_date,
                            action_type,
                            created_by,
                            date_created,
                            modified_by,
                            date_modified,
                            version,
                            effective_date)
                    VALUES
                           (budget_pk_sq.NEXTVAL,
                            r.budgSegId,
                            r.USER_ID,
                            '',
                            0,
                            0,
                            NULL,
                            'active',
                            --SYSDATE,
                            --NULL,
                            c_action_type,
                            v_createdid,
                            SYSDATE,
                            NULL,
                            NULL,
                            0,
                            NULL);
              --Budgethistory insertion for orgunibelow
              MERGE INTO budget_history bh
              USING (
                    SELECT budget_id
                      FROM budget bt, budget_segment bs
                     WHERE bt.budget_segment_id = v_budget_segment_id
                       AND bt.budget_segment_id = bs.budget_segment_id
                       AND bs.budget_master_id = v_budget_master_id
                    ) b
              ON (b.budget_id = bh.budget_id)
              WHEN NOT MATCHED THEN
                    INSERT (budget_history_id,
                            budget_id,
                            original_value_before_xaction,
                            current_value_before_xaction,
                            original_value_after_xaction,
                            current_value_after_xaction,
                            action_type,
                            created_by,
                            date_created)
                    VALUES
                            (budget_history_pk_sq.NEXTVAL,
                             b.budget_id,
                             0,
                             0,
                             0,
                             0,
                             c_action_type,
                             v_createdid,
                             SYSDATE
                             );    */            
              
            END IF;--)
          
                
        END IF; --budget allocation available validation if ends
        
        /*
        *  budget transfer nodes list retcursor
        */
        
        IF v_nodeuserflg AND v_budgetrealleligType = 'orgunit' THEN 
        
          OPEN p_out_result_set FOR
          SELECT budget_id        
                 ,own_node_id     
                 ,first_name      
                 ,last_name 
                 ,user_id      
                 ,budget_spent    
                 ,current_value   
            FROM (SELECT t.first_name   first_name,
                         t.last_name    last_name,
                         t.user_id,
                         ROUND(((b.original_value-b.current_value) * nvl(p_in_media_ratio,0)),2) budget_spent,
                         ROUND((b.current_value  * nvl(p_in_media_ratio,0)),2)                   current_value,
                         b.budget_id    budget_id,
                         ''             own_node_id
                    FROM budget b, budget_segment bs,
                         (SELECT un.user_id AS user_id ,
                                au.first_name first_name, 
                                au.last_name last_name 
                           FROM user_node un,application_user au 
                          WHERE un.node_id = v_nodeid 
                            AND au.user_id = un.user_id 
                            AND au.is_active = 1 
                            AND EXISTS (SELECT 1 
                                          FROM node n 
                                         WHERE n.node_id    = un.node_id 
                                           AND n.is_deleted = 0 )) T
                   WHERE NVL(b.user_id,-1)  = t.user_id 
                     AND b.budget_segment_id = v_budget_segment_id  
                     AND b.budget_segment_id = bs.budget_segment_id 
                     AND bs.budget_master_id = v_budget_master_id   
                     AND b.status = 'active' --04/22/2019                     
                     AND NVL(t.user_id,-1)  <> v_PaxId
                   UNION
                  SELECT au.first_name, --07/06/2016
                         au.last_name,
                         n.user_node_id,
                         0 budget_spent,
                         0 current_value,
                         NULL budget_id, 
                         '' as own_node_id
                    FROM tmp_budget_transfer n,
                         application_user au
                   WHERE n.user_node_id = au.user_id
            );
            
        ELSIF v_nodeuserflg AND v_budgetrealleligType = 'orgunitbelow' THEN 
        
          OPEN p_out_result_set FOR
          SELECT budget_id        
                 ,own_node_id     
                 ,first_name      
                 ,last_name   
                 ,user_id    
                 ,budget_spent    
                 ,current_value   
            FROM (SELECT t.first_name   first_name,
                         t.last_name    last_name,
                         t.user_id,
                         ROUND(((b.original_value-b.current_value) * nvl(p_in_media_ratio,0)),2) budget_spent,
                         ROUND((b.current_value  * nvl(p_in_media_ratio,0)),2)                   current_value,
                         b.budget_id    budget_id,
                         ''             own_node_id
                    FROM budget b,  budget_segment bs,
                         (SELECT un.user_id AS user_id ,
                                 au.first_name first_name, 
                                 au.last_name last_name 
                            FROM user_node un,
                                 application_user au 
                           WHERE un.node_id  in (SELECT n.node_id 
                                                   FROM node n 
                                                  WHERE n.is_deleted       = 0  
                                                  START WITH n.node_id     = v_nodeid 
                                                CONNECT BY PRIOR n.node_id = n.parent_node_id) 
                             AND au.user_id   = un.user_id 
                             AND au.is_active = 1) T
                   WHERE NVL(b.user_id,-1)  = t.user_id 
                     AND b.budget_segment_id = v_budget_segment_id    
                     AND b.budget_segment_id = bs.budget_segment_id   
                     AND bs.budget_master_id = v_budget_master_id     
                     AND b.status = 'active' --04/22/2019
                     AND NVL(t.user_id,-1)  <> v_paxid
                   UNION
                  SELECT au.first_name, --07/06/2016
                         au.last_name,
                         n.user_node_id,
                         0 budget_spent,
                         0 current_value,
                         NULL budget_id, 
                         '' as own_node_id
                    FROM tmp_budget_transfer n,
                         application_user au
                   WHERE n.user_node_id = au.user_id
            );
            
        ELSIF v_budgetrealleligType = 'orgunit' THEN
         
          OPEN p_out_result_set FOR
          SELECT budget_id        
                 ,own_node_id     
                 ,first_name      
                 ,last_name   
                 ,user_id    
                 ,budget_spent    
                 ,current_value   
            FROM (SELECT t.first_name   first_name,
                         t.last_name    last_name,
                         t.user_id,
                         ROUND(((b.original_value-b.current_value) * nvl(p_in_media_ratio,0)),2) budget_spent,
                         ROUND((b.current_value  * nvl(p_in_media_ratio,0)),2)                   current_value,
                         b.budget_id    budget_id,
                         ''             own_node_id
                    FROM budget b, budget_segment bs,
                         (SELECT un.user_id AS user_id, 
                                 au.first_name first_name, 
                                 au.last_name last_name
                            FROM user_node un,
                                 application_user au 
                           WHERE un.node_id   = v_nodeid 
                             AND au.user_id   = un.user_id 
                             AND au.is_active = 1 
                             AND EXISTS (SELECT 1 
                                           FROM node n 
                                          WHERE n.node_id    = un.node_id 
                                            AND n.is_deleted = 0 )
                          INTERSECT
                          SELECT parau.user_id AS user_id , 
                                 au.first_name first_name, 
                                 au.last_name last_name
                            FROM participant_audience parau ,
                                 promo_audience proau, 
                                 promotion pro, 
                                 application_user au
                           WHERE parau.audience_id          = proau.audience_id 
                             AND proau.promotion_id         = pro.promotion_id 
                             AND proau.promo_audience_type  = 'PRIMARY' 
                             AND pro.award_budget_master_id = v_budget_master_id 
                             AND pro.promotion_status       = 'live' 
                             AND au.user_id                 = parau.user_id 
                             AND au.is_active               = 1
                   ) T
           WHERE NVL(b.user_id,-1) = t.user_id 
             AND b.budget_segment_id = v_budget_segment_id   
             AND b.budget_segment_id = bs.budget_segment_id  
             AND bs.budget_master_id = v_budget_master_id    
             AND b.status = 'active' --04/22/2019
             AND NVL(t.user_id,-1) <> v_PaxId
           UNION
          SELECT au.first_name, --07/06/2016
                 au.last_name,
                 n.user_node_id,
                 0 budget_spent,
                 0 current_value,
                 NULL budget_id, 
                 '' as own_node_id
            FROM tmp_budget_transfer n,
                 application_user au
           WHERE n.user_node_id = au.user_id
            );
        ELSE
          OPEN p_out_result_set FOR
          SELECT budget_id        
                 ,own_node_id     
                 ,first_name      
                 ,last_name   
                 ,user_id    
                 ,budget_spent    
                 ,current_value   
            FROM (SELECT t.first_name   first_name,
                         t.last_name    last_name,
                         t.user_id,
                         ROUND(((b.original_value-b.current_value) * nvl(p_in_media_ratio,0)),2) budget_spent,
                         ROUND((b.current_value  * nvl(p_in_media_ratio,0)),2)                   current_value,
                         b.budget_id    budget_id,
                         ''             own_node_id
                    FROM BUDGET B, budget_segment bs,
                         (SELECT un.user_id AS user_id, 
                                 au.first_name first_name, 
                                 au.last_name last_name
                            FROM user_node un,
                                 application_user au 
                           WHERE un.node_id   in (SELECT n.node_id 
                                                    FROM node n 
                                                   WHERE n.is_deleted       = 0  
                                                   START WITH n.node_id     = v_nodeid 
                                                 CONNECT BY PRIOR n.node_id = n.parent_node_id)
                             AND au.user_id   = un.user_id 
                             AND au.is_active = 1 
                          INTERSECT
                          SELECT parau.user_id AS user_id , 
                                 au.first_name first_name, 
                                 au.last_name last_name
                            FROM participant_audience parau ,
                                 promo_audience proau, 
                                 promotion pro, 
                                 application_user au
                           WHERE parau.audience_id          = proau.audience_id 
                             AND proau.promotion_id         = pro.promotion_id 
                             AND proau.promo_audience_type  = 'PRIMARY' 
                             AND pro.award_budget_master_id = v_budget_master_id 
                             AND pro.promotion_status       = 'live' 
                             AND au.user_id                 = parau.user_id 
                             AND au.is_active               = 1
                    ) T
           WHERE NVL(b.user_id,-1)  = t.user_id 
             AND b.budget_segment_id = v_budget_segment_id  
             AND b.budget_segment_id = bs.budget_segment_id 
             AND bs.budget_master_id = v_budget_master_id  
             AND b.status = 'active' --04/22/2019
             AND NVL(t.user_id,-1)  <> v_PaxId
           UNION
          SELECT au.first_name, --07/06/2016
                 au.last_name,
                 n.user_node_id,
                 0 budget_spent,
                 0 current_value,
                 NULL budget_id, 
                 '' as own_node_id
            FROM tmp_budget_transfer n,
                 application_user au
           WHERE n.user_node_id = au.user_id
            );
        END IF;--)
        
      END IF;--pax id validation if ends
      
    ELSE
    
      v_errormsg    := 'pax is not assigned to the budget';
      RAISE flderr;
      
    END IF; --Budget id and budget master id validation if ends
    
  END IF; --budget type validation if ends
  
  COMMIT;
  prc_execution_log_entry(c_process_name,1,c_severity_i,'Process Completed ',null);
  p_out_return_code:=0;
EXCEPTION
  WHEN flderr THEN
    ROLLBACK;
    p_out_return_code :=  '99' ;
    prc_execution_log_entry(c_process_name,1,c_severity_e,v_textmsg||'-'||v_errormsg,null);
    OPEN p_out_result_set FOR SELECT NULL FROM dual  ;
  WHEN NO_DATA_FOUND THEN
    ROLLBACK;
    p_out_return_code :=  '99' ;
    prc_execution_log_entry(c_process_name,1,c_severity_e,v_errormsg||'-'||v_textmsg||'-'||SQLERRM,null);
    OPEN p_out_result_set FOR SELECT NULL FROM dual  ;
  WHEN OTHERS THEN
    ROLLBACK;
    p_out_return_code :=  '99' ;
    prc_execution_log_entry(c_process_name,1,c_severity_e,v_errormsg||'-'||v_textmsg||'-'||SQLERRM,null);
    OPEN p_out_result_set FOR SELECT NULL FROM dual  ;
END prc_budget_transfer;
/
