CREATE OR REPLACE PROCEDURE PRC_RPT_GOAL_PARTNER(pi_requested_user_id IN NUMBER,                                               
                                                 po_return_code      OUT NUMBER,
                                                 po_error_message    OUT VARCHAR2)IS
/*******************************************************************************

   Purpose:  This procedure calls the Goal Partner Report Refresh procedure

   Person          Date        Comments
   -----------     ----------  -------------------------------------------------
   S. Majumder     03/27/2008  Initial Creation   
********************************************************************************/

  v_stage VARCHAR2(120) ;

BEGIN

    Prc_Execution_Log_Entry('P_RPT_GOAL_PARTNER',1,'INFO','Report refresh started',NULL);

    v_stage := 'P_RPT_GOAL_PARTNER' ;
    
    PKG_REPORT_GOALQUEST.P_RPT_GOAL_PARTNER(pi_requested_user_id,                                           
                                            po_return_code,
                                            po_error_message);

    Prc_Execution_Log_Entry('P_RPT_GOAL_PARTNER',1,'INFO','Report refresh completed',NULL);

EXCEPTION
  WHEN OTHERS THEN

      v_stage := SUBSTR('Error '||TO_CHAR(SQLCODE)||': '||SQLERRM, 1, 255) ;
      Prc_Execution_Log_Entry('P_RPT_GOAL_PARTNER',1,'ERROR',v_stage,NULL);

END ;
/