CREATE OR REPLACE PROCEDURE PRC_RA_PROMO_LEVEL (p_in_user_id      IN  application_user.user_id%TYPE,
                                                p_out_return_code OUT NUMBER)  
IS                                                 
    /*******************************************************************************
   -- Purpose: Populate eligible ptomotions and receivers data for RA
   --
   -- Person        Date         Comments
   -- -----------   --------     -----------------------------------------------------
   -- Gorantla      02/17/2018   Initial creation   
   -- Gorantla      07/09/2018   GitLab#1211 - Modify RA algorithm to include a manager from lower node                                               
   -- Loganathan    08/06/2019	 Bug 79087 - RA overdue notifications shows large overdue days when pax 
   								 moved from One Manager to Other Manager
   *******************************************************************************/
  c_process_name          CONSTANT execution_log.process_name%TYPE:='PRC_RA_PROMO_LEVEL';

  v_user_node             user_node.node_id%TYPE;
  v_stage                 VARCHAR2(400);
  v_level                 PLS_INTEGER;
  v_orphan_nodes          PLS_INTEGER;
  v_no_of_levels          os_propertyset.int_val%TYPE;
    
BEGIN
  --prc_execution_log_entry(c_process_name,1,'INFO','Process Started '||p_in_user_id,null);
  
  v_stage := 'Number of levels to check under a manager';
  SELECT int_val
    INTO v_no_of_levels
    FROM os_propertyset
   WHERE entity_name = 'ra.recog.levels.check';

  v_stage := 'Get eligible promotion list'; 
  DELETE FROM gtt_ra_promotion;
  INSERT INTO gtt_ra_promotion
    WITH promo as (
    SELECT p.*
      FROM promotion p,
           promo_recognition pr
     WHERE p.promotion_id = pr.promotion_id
       AND pr.is_include_purl = 0
       AND pr.include_celebrations = 0
       AND promotion_type = 'recognition'
       AND award_type != 'merchandise'
       AND promotion_status IN ('live','expired')
       AND is_fileload_entry = 0 )
    -- Get the list of promotion assocaited to pax
    SELECT p.promotion_id,
           promotion_status,
           primary_audience_type,
           DECODE(secondary_audience_type, 'sameasprimaryaudience',primary_audience_type,secondary_audience_type) secondary_audience_type,
           secondary_audience_type orgl_secondary_audience_type
      FROM participant_audience pax_aud,
           promo_audience promo_aud,
           promo p
     WHERE pax_aud.audience_id = promo_aud.audience_id 
       AND promo_audience_type = 'PRIMARY' 
       AND promo_aud.promotion_id = p.promotion_id
       AND pax_aud.user_id = p_in_user_id
    UNION
    -- Get the list of promotion assocaited to pax(all active pax - pax is part of allactive)
    SELECT p.promotion_id,
           promotion_status,
           primary_audience_type,
           DECODE(secondary_audience_type, 'sameasprimaryaudience',primary_audience_type,secondary_audience_type) secondary_audience_type,
           secondary_audience_type orgl_secondary_audience_type
      FROM promo p 
     WHERE p.primary_audience_type = 'allactivepaxaudience';

    v_stage := 'Get list of nodes for all levels'; 
    DELETE FROM  gtt_ra_node; 
    FOR rec_node IN (SELECT node_id  -- if user is mgr/own for more than one node
                       FROM user_node
                      WHERE role IN ('mgr','own')
                        AND status = 1
                        AND user_id = p_in_user_id)
    LOOP   --START if no orphan nodes exists                
      INSERT INTO gtt_ra_node  
      SELECT n.node_id, 
             n.lvl, 
             n.parent_node_id, 
             un.node_id mgr_id, 
             n.lvl new_lvl,
             rec_node.node_id curr_node
        FROM (SELECT LEVEL lvl, 
                     n.node_id, 
                     n.parent_node_id
                FROM node n
          START WITH n.node_id = rec_node.node_id           
    CONNECT BY PRIOR n.node_id = n.parent_node_id) n,
             (SELECT distinct node_id
                FROM user_node
               WHERE role IN ('mgr','own')
                 AND status = 1) un
       WHERE n.node_id = un.node_id(+);
           
    END LOOP;  --END if no orphan nodes exists
 
    v_stage := 'Check for existance of Orphan nodes'; 
    SELECT count(*)
      INTO v_orphan_nodes
      FROM gtt_ra_node
     WHERE mgr_id IS NULL;

    IF v_orphan_nodes > 0 THEN  --START if orphan nodes exists
  
      v_stage := 'Get all the nodes from logged in pax'; 
      DELETE FROM  gtt_ra_node; 
      FOR rec_node IN (SELECT node_id -- if user is mgr/own for more than one node
                         FROM user_node
                        WHERE role IN ('mgr','own')
                          AND status = 1
                          AND user_id = p_in_user_id)
      LOOP  --START Loop only if orphan nodes exists      
        INSERT INTO gtt_ra_node  
        SELECT n.node_id, 
               n.lvl, 
               n.parent_node_id, 
               un.node_id mgr_id, 
               n.lvl new_lvl,
               rec_node.node_id curr_node
          FROM (SELECT LEVEL lvl, 
                       n.node_id, 
                       n.parent_node_id
                  FROM node n
            START WITH n.node_id = rec_node.node_id          
      CONNECT BY PRIOR n.node_id = n.parent_node_id) n,
               (SELECT distinct node_id
                  FROM user_node
                 WHERE role IN ('mgr','own')
                   AND status = 1) un
         WHERE n.node_id = un.node_id(+);
     
        FOR rec_lvl IN (SELECT * 
                          FROM gtt_ra_node 
                         WHERE curr_node = rec_node.node_id 
                           AND node_level > 1 
                         ORDER BY node_level)  
        LOOP --START loop adjust node levels
          v_level := 0;    
           
          v_stage := 'Get parent node levels '||rec_lvl.parent_node_id; 
          SELECT distinct new_level
            INTO v_level
            FROM gtt_ra_node
           WHERE node_id = rec_lvl.parent_node_id
             AND curr_node = rec_node.node_id;     
            
          IF rec_lvl.mgr_id IS NOT NULL THEN
            v_level := v_level + 1;
          END IF; 
          
          v_stage := 'Update to adjusted node levels '||rec_lvl.node_id; 
          UPDATE gtt_ra_node
             SET new_level = v_level
           WHERE node_id = rec_lvl.node_id
             AND new_level <> v_level
             AND curr_node = rec_node.node_id;

        END LOOP;  --END loop adjust node levels

      END LOOP;  --END Loop only if orphan nodes exists   
    
    END IF;  --END only if orphan nodes exists
      
    v_stage := 'Get distinct users for given levels'; 
    DELETE FROM gtt_ra_receiver;
    INSERT INTO gtt_ra_receiver
      SELECT user_id   -- 07/09/2018 removed distinct
        FROM gtt_ra_node nt,
             user_node un
       WHERE nt.node_id = un.node_id
         AND un.status = 1
         AND un.user_id <> p_in_user_id
           -- Ignore own/mgrs of logged in user
         AND (un.node_id,un.user_id) NOT IN (SELECT un1.node_id,un1.user_id
                                               FROM user_node un,
                                                    user_node un1
                                              WHERE un.node_id = un1.node_id
                                                AND un1.role IN ('mgr','own')
                                                AND node_level = 1
                                                AND un.user_id = p_in_user_id)
         AND new_level <= v_no_of_levels
   -- 07/09/2018 start         
    UNION    
    SELECT user_id
      FROM gtt_ra_node nt,
           user_node un
     WHERE nt.node_id = un.node_id
       AND un.status = 1
       AND un.role IN ('mgr','own')
       AND new_level = v_no_of_levels+1;
    -- 07/09/2018 end    
         
		v_stage := 'Get distinct users for given levels with additional information'; 			--08/06/2019 Bug 79087  Starts here
		DELETE GTT_RA_RECEIVER_INFO;	
		INSERT INTO GTT_RA_RECEIVER_INFO
			SELECT user_id,DATE_CREATED,man_dt,GREATEST(DATE_CREATED,man_dt) 
			FROM (SELECT user_id   -- 07/09/2018 removed distinct
      					 ,DATE_CREATED,
      				(select case 
      				   			when unh.role is null then un.date_created
								when un.role<>nvl(unh.role,'xxx') then greatest(un.date_created,un.date_modified) else un.date_created 
							end date_created 
						FROM user_node un,
					(select * from (
						select a.*,ROW_NUMBER() over(partition by USER_ID, NODE_ID order by A.user_node_history_id desc) rn 
						FROM USER_NODE_HISTORY a)
						where rn=1) unh
						where un.user_id=unh.user_id(+)
						and   un.node_id=unh.node_id(+)
						and   un.node_id=curr_node
						and   un.user_id=p_in_user_id) man_dt,
				      	row_number() over(partition by user_id order by NEW_LEVEL) rn
        	 FROM gtt_ra_node nt,user_node un
       WHERE nt.node_id = un.node_id
         AND un.status = 1
         AND un.user_id <> p_in_user_id
           -- Ignore own/mgrs of logged in user
         AND (un.node_id,un.user_id) NOT IN (SELECT un1.node_id,un1.user_id
                                               FROM user_node un,
                                                    user_node un1
                                              WHERE un.node_id = un1.node_id
                                                AND un1.role IN ('mgr','own')
                                                AND node_level = 1
                                                AND un.user_id = p_in_user_id)
         AND new_level <= v_no_of_levels
   -- 07/09/2018 start         
    UNION    
    SELECT user_id,DATE_CREATED,
      			(select	case 
      					  when unh.role is null then un.date_created
						  when un.role<>nvl(unh.role,'xxx') then greatest(un.date_created,un.date_modified) else un.date_created 
						 end date_created 
				  from user_node un,
						(select * from (
								select a.*,ROW_NUMBER() over(partition by USER_ID, NODE_ID order by A.user_node_history_id desc) rn 
								from USER_NODE_HISTORY a)
								where rn=1) unh
								where un.user_id=unh.user_id(+)
								and   un.node_id=unh.node_id(+)
								and   un.node_id=curr_node
								and   un.user_id=p_in_user_id) man_dt,
						   row_number() over(partition by user_id order by NEW_LEVEL) rn
      			  FROM gtt_ra_node nt, user_node un
     			  WHERE nt.node_id = un.node_id
       			  AND un.status = 1
       			  AND un.role IN ('mgr','own')
                  AND new_level = v_no_of_levels+1)
       		      where rn=1;													--08/06/2019 Bug 79087  ended here
  COMMIT;
  --prc_execution_log_entry(c_process_name,1,'INFO','Process Completed ',null);
  p_out_return_code:=0;         
EXCEPTION   
  WHEN OTHERS THEN
    ROLLBACK;
    p_out_return_code :=  99 ;
    prc_execution_log_entry(c_process_name,1,'ERROR',v_stage||' - '||SQLERRM,null);
END;
/
