CREATE OR REPLACE PACKAGE pkg_report_characteristic  IS
/*******************************************************************************

   Purpose:  This package is for code to populate the rpt_characteristic table.
             This table will have 5 characteristics per user and 5 characteristics
             per node that will be displayed on reports.

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray    11/09/2005   Initial creation
   Raju N      06/26/2006   Added new procedure to be used in extracts.
   Raju N      01/04/2007  Bug # 14582 have he characteristic name as the column
                           label for all the report extracts.
   Arun S      03/18/2009  Bug # 19713 fix, Overloading function f_get_characteristic_label 
                           to get Giver or Recipient specific characteristic labels 
   Ravi Dhanekula 01/27/2014  Bug # 50969 fix, added new procedure to process multi_select user characteristics.   
                              Bug # 51304 fix, increased the size of the variable v_characteristic_val to 1000.                        
*******************************************************************************/

PROCEDURE P_RPT_CHARACTERISTICS;

PROCEDURE P_CHARACTERISTIC_DETAILS
 (p_in_characteristic_type IN VARCHAR2, -- in parm  USER or NT
  p_in_user_nt_id IN NUMBER, -- in parm  user_id or node_id
  p_characteristic_rec OUT rpt_characteristic%ROWTYPE);

PROCEDURE P_INSERT_RPT_CHARACTERISTIC
  (p_rptchar_rec IN rpt_characteristic%ROWTYPE);

PROCEDURE P_GET_CHARACTERISTIC
  ( p_in_characteristic_type IN VARCHAR2 -- in parm  USER or NT
    ,p_in_user_nt_id IN NUMBER -- in parm  user_id or node_id
    ,p_rptchar_str OUT VARCHAR2) ;
PROCEDURE p_get_characteristic_value
  ( p_in_characteristic_type IN VARCHAR2 -- in parm  USER or NT
    ,p_in_user_nt_id IN NUMBER -- in parm  user_id or node_id
    ,p_rptchar_str OUT VARCHAR2) ;
FUNCTION f_get_characteristic_label ( p_in_characteristic_type IN VARCHAR2 -- in parm  USER or NT
                                     )   RETURN VARCHAR2 ;
FUNCTION f_get_characteristic ( p_in_characteristic_type IN VARCHAR2 -- in parm  USER or NT
    ,p_in_user_nt_id IN NUMBER) RETURN VARCHAR2  ;

FUNCTION f_get_characteristic_label ( p_in_characteristic_type IN VARCHAR2, -- in parm  USER or NT
                                      p_in_user_type IN VARCHAR2)   RETURN VARCHAR2 ;

PROCEDURE prc_rpt_user_char_refresh ( p_user_id      IN  NUMBER,
  p_return_code           OUT NUMBER,
  p_out_error_message         OUT VARCHAR2);
  
END; -- Package spec
/

CREATE OR REPLACE PACKAGE BODY pkg_report_characteristic IS
/*******************************************************************************
   Purpose:  This package is for code to populate the rpt_characteristic table.
             This table will have 5 characteristics per user and 5 characteristics
             per node that will be displayed on reports.

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray    11/09/2005  Initial creation
   Raju N      01/04/2007  Bug # 14582 have he characteristic name as the column
                           label for all the report extracts.
*******************************************************************************/

-- private package constants
c_sev_err                  CONSTANT execution_log.severity%TYPE  := 'ERROR';
c_sev_info                 CONSTANT execution_log.severity%TYPE  := 'INFO';
c_chr_type_user            CONSTANT rpt_characteristic.characteristic_type%TYPE := 'USER';
c_chr_type_node            CONSTANT rpt_characteristic.characteristic_type%TYPE := 'NT';

 -- package variable for turning the debugging on
  v_debug varchar2(1) :=  'N' ;

FUNCTION  fnc_user_charactersitic_value
               ( p_in_charid IN NUMBER ,
                 p_in_id  IN NUMBER ) return varchar2  IS
/*******************************************************************************
   Purpose:  This function rerturns the characteristic value for the user. If
             the characterisitic doesn't exist then returns a null value.
   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   Raju N        11/14/2005  Initial creation

*******************************************************************************/
  v_char_value   varchar2(30) ;
BEGIN
   SELECT uc.characteristic_value
     INTO v_char_value
     FROM user_characteristic uc
    WHERE user_id = p_in_id
      AND characteristic_id = p_in_charid ;
    RETURN  v_char_value ;
EXCEPTION
   WHEN no_data_found THEN
         v_char_value := NULL ;
         RETURN  v_char_value ;
    WHEN OTHERS THEN
         v_char_value := NULL ;
         RETURN  v_char_value ;
END  ;
--------------------------------------------------------------------------------
FUNCTION  fnc_nt_charactersitic_value
               ( p_in_charid IN NUMBER ,
                 p_in_id  IN NUMBER ) return varchar2  IS
/*******************************************************************************
   Purpose:  This function rerturns the characteristic value for the node. If
             the characterisitic doesn't exist then returns a null value.
   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   Raju N        11/14/2005  Initial creation
*******************************************************************************/
  v_char_value   varchar2(30) ;
BEGIN
   SELECT uc.characteristic_value
     INTO v_char_value
     FROM node_characteristic uc
    WHERE node_id = p_in_id
      AND characteristic_id = p_in_charid ;
    RETURN  v_char_value ;
EXCEPTION
   WHEN no_data_found THEN
         v_char_value := NULL ;
         RETURN  v_char_value ;
    WHEN OTHERS THEN
         v_char_value := NULL ;
         RETURN  v_char_value ;
END  ;
--------------------------------------------------------------------------------

/*** J Flees 05/30/2013, Commented out loop processing code for performance tuning
PROCEDURE P_RPT_CHARACTERISTICS
   IS
/*******************************************************************************
   Purpose:  Populate the rpt_characteristic table that will be used for reports.
             Each record_type (USER, NT, etc.) will have the first 5 characteristics
             stored in the table.

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray      11/09/2005 Initial Creation
*******************************************************************************/

/*** J Flees 05/30/2013, Commented out loop processing code for performance tuning
   CURSOR cur_characteristic IS
     SELECT DISTINCT r.characteristic_type
       FROM rpt_characteristic_lookup r
      WHERE characteristic_type IN ('USER','NT')
      ORDER BY r.characteristic_type ;

   CURSOR cur_user IS
     SELECT user_id
       FROM application_user ;

   CURSOR cur_node IS
     SELECT DISTINCT node_id
       FROM user_node;

   characteristic_rec            rpt_characteristic%ROWTYPE;

BEGIN

  DELETE    rpt_characteristic;


  FOR rec_characteristic IN cur_characteristic LOOP
    IF rec_characteristic.characteristic_type = 'USER' THEN
      FOR rec_user IN cur_user LOOP
        IF v_debug = 'Y' THEN
          dbms_output.put_line(rec_characteristic.characteristic_type) ;
          dbms_output.put_line(rec_user.user_id) ;
        END IF ;
        P_CHARACTERISTIC_DETAILS(rec_characteristic.characteristic_type, rec_user.user_id,  -- input parms
                                                   characteristic_rec);
        IF characteristic_rec.characteristic_id1 IS NOT NULL THEN
          P_INSERT_RPT_CHARACTERISTIC(characteristic_rec); -- input parms
        END IF;
      END LOOP; -- cur_user
    ELSIF rec_characteristic.characteristic_type = 'NT' THEN
      FOR rec_node IN cur_node LOOP
        P_CHARACTERISTIC_DETAILS(rec_characteristic.characteristic_type, rec_node.node_id,  -- input parms
                                                   characteristic_rec);
        IF characteristic_rec.characteristic_id1 IS NOT NULL THEN
          P_INSERT_RPT_CHARACTERISTIC(characteristic_rec); -- input parms
        END IF;
      END LOOP; -- cur_node
    END IF;  -- characteristic_type

  END LOOP; -- cur_characteristic
EXCEPTION
  WHEN OTHERS THEN
    prc_execution_log_entry('P_RPT_CHARACTERISTICS',1,'ERROR',SQLERRM,null);
END; -- p_rpt_characteristics
*** J Flees 05/30/2013, Commented out loop processing code for performance tuning */

PROCEDURE P_RPT_CHARACTERISTICS
IS
/*******************************************************************************
   Purpose:  Populate the rpt_characteristic table that will be used for reports.
             Each record_type (USER, NT, etc.) will have the first 5 characteristics
             stored in the table.

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray      11/09/2005 Initial Creation
   J Flees       08/31/2011 Dexter 26129: Report Refresh - Performance Tuning.
                            Replaced looping process with bulk SQL statements.
   Ravi Dhanekula 06/18/2013  Implemented the performance tuning efforts done for large clients
    nagarajs      03/03/2016  Bug 65699 All report extracts which contains pax characteristics should show all 20 characteristics
    Gorantla      09/27/2017  JIRA# G6-3018 Modified reports and extract to mask pax characteristics based on do not show flag 
    Chidamba      12/28/2017  JIRA# G6-3654 Remove the hidden characteristics from existing report data on every report refresh run.
*******************************************************************************/
   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('P_RPT_CHARACTERISTICS');
   c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;

   v_stage              execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;

BEGIN
   v_stage := 'Start';
   prc_execution_log_entry(c_process_name, c_release_level, c_sev_info, v_stage, NULL);

   ------------
   -- remove obsolete records
   v_stage := 'DELETE obsolete rpt_characteristic >' || c_chr_type_user || '<';
   DELETE rpt_characteristic
    WHERE ROWID IN
          ( -- associated record not found
            SELECT rc.ROWID
              FROM rpt_characteristic rc,
                   application_user au,
                   ( -- confirm whether any characteristic lookup records exist for characteristic type
                     SELECT COUNT(1) AS chr_type_cnt
                       FROM rpt_characteristic_lookup cl
                      WHERE cl.characteristic_type = c_chr_type_user
                   ) cc
             WHERE rc.characteristic_type = c_chr_type_user
               AND rc.user_nt_id = au.user_id (+)
               AND (  cc.chr_type_cnt = 0    -- no lookup records, remove all characteristic type records
                   OR au.ROWID IS NULL)
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, c_sev_info, v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   v_stage := 'DELETE obsolete rpt_characteristic >' || c_chr_type_node || '<';
   DELETE rpt_characteristic
    WHERE ROWID IN
          ( -- associated record not found
            SELECT DISTINCT rc.ROWID
              FROM rpt_characteristic rc,
                   user_node un,
                   ( -- confirm whether any characteristic lookup records exist for characteristic type
                     SELECT COUNT(1) AS chr_type_cnt
                       FROM rpt_characteristic_lookup cl
                      WHERE cl.characteristic_type = c_chr_type_node
                   ) cc
             WHERE rc.characteristic_type = c_chr_type_node
               AND rc.user_nt_id = un.node_id (+)
               AND (  cc.chr_type_cnt = 0    -- no lookup records, remove all characteristic type records
                   OR un.ROWID IS NULL)
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, c_sev_info, v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
   
   /********************12/28/2017************************/
   v_stage := 'DELETE Hidden rpt_characteristic ';
   DELETE rpt_characteristic
    WHERE user_nt_id IN
          ( SELECT uc.user_id 
              FROM user_characteristic uc
             WHERE EXISTS (SELECT 1
                             FROM characteristic s 
                            WHERE s.visibility = 'hidden' 
                              AND characteristic_type = c_chr_type_user
                              AND uc.characteristic_id = s.characteristic_id)  
                  UNION ALL
            SELECT uc.node_id 
              FROM node_characteristic uc
             WHERE EXISTS (SELECT 1
                             FROM characteristic s 
                            WHERE s.visibility = 'hidden' 
                              AND characteristic_type = c_chr_type_node
                              AND uc.characteristic_id = s.characteristic_id)                      
                      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, c_sev_info, v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);   
   /******************12/28/2017*****************************/  
   ------------
   -- refresh report records
   v_stage := 'MERGE rpt_characteristic >' || c_chr_type_user || '<';
   MERGE INTO rpt_characteristic d
   USING (  -- get associated characteristic value and pivot into a single record by key fields
            SELECT -- key fields
                   doc.user_nt_id,
                   doc.characteristic_type,
                   -- pivot characteristics
                   MAX(DECODE(doc.field_seq, 1, doc.characteristic_id,        NULL)) AS characteristic_id1,
                   MAX(DECODE(doc.field_seq, 1, doc.characteristic_data_type, NULL)) AS characteristic_datatype1,
                   MAX(DECODE(doc.field_seq, 1, uc.characteristic_value,      NULL)) AS characteristic_charvalue1,
                   MAX(DECODE(doc.field_seq, 2, doc.characteristic_id,        NULL)) AS characteristic_id2,
                   MAX(DECODE(doc.field_seq, 2, doc.characteristic_data_type, NULL)) AS characteristic_datatype2,
                   MAX(DECODE(doc.field_seq, 2, uc.characteristic_value,      NULL)) AS characteristic_charvalue2,
                   MAX(DECODE(doc.field_seq, 3, doc.characteristic_id,        NULL)) AS characteristic_id3,
                   MAX(DECODE(doc.field_seq, 3, doc.characteristic_data_type, NULL)) AS characteristic_datatype3,
                   MAX(DECODE(doc.field_seq, 3, uc.characteristic_value,      NULL)) AS characteristic_charvalue3,
                   MAX(DECODE(doc.field_seq, 4, doc.characteristic_id,        NULL)) AS characteristic_id4,
                   MAX(DECODE(doc.field_seq, 4, doc.characteristic_data_type, NULL)) AS characteristic_datatype4,
                   MAX(DECODE(doc.field_seq, 4, uc.characteristic_value,      NULL)) AS characteristic_charvalue4,
                   MAX(DECODE(doc.field_seq, 5, doc.characteristic_id,        NULL)) AS characteristic_id5,
                   MAX(DECODE(doc.field_seq, 5, doc.characteristic_data_type, NULL)) AS characteristic_datatype5,
                   MAX(DECODE(doc.field_seq, 5, uc.characteristic_value,      NULL)) AS characteristic_charvalue5,
                   MAX(DECODE(doc.field_seq, 6, doc.characteristic_id,        NULL)) AS characteristic_id6,
                   MAX(DECODE(doc.field_seq, 6, doc.characteristic_data_type, NULL)) AS characteristic_datatype6,
                   MAX(DECODE(doc.field_seq, 6, uc.characteristic_value,      NULL)) AS characteristic_charvalue6,
                   MAX(DECODE(doc.field_seq, 7, doc.characteristic_id,        NULL)) AS characteristic_id7,
                   MAX(DECODE(doc.field_seq, 7, doc.characteristic_data_type, NULL)) AS characteristic_datatype7,
                   MAX(DECODE(doc.field_seq, 7, uc.characteristic_value,      NULL)) AS characteristic_charvalue7,
                   MAX(DECODE(doc.field_seq, 8, doc.characteristic_id,        NULL)) AS characteristic_id8,
                   MAX(DECODE(doc.field_seq, 8, doc.characteristic_data_type, NULL)) AS characteristic_datatype8,
                   MAX(DECODE(doc.field_seq, 8, uc.characteristic_value,      NULL)) AS characteristic_charvalue8,
                   MAX(DECODE(doc.field_seq, 9, doc.characteristic_id,        NULL)) AS characteristic_id9,
                   MAX(DECODE(doc.field_seq, 9, doc.characteristic_data_type, NULL)) AS characteristic_datatype9,
                   MAX(DECODE(doc.field_seq, 9, uc.characteristic_value,      NULL)) AS characteristic_charvalue9,
                   MAX(DECODE(doc.field_seq, 10, doc.characteristic_id,        NULL)) AS characteristic_id10,
                   MAX(DECODE(doc.field_seq, 10, doc.characteristic_data_type, NULL)) AS characteristic_datatype10,
                   MAX(DECODE(doc.field_seq, 10, uc.characteristic_value,      NULL)) AS characteristic_charvalue10,
                   MAX(DECODE(doc.field_seq, 11, doc.characteristic_id,        NULL)) AS characteristic_id11,
                   MAX(DECODE(doc.field_seq, 11, doc.characteristic_data_type, NULL)) AS characteristic_datatype11,
                   MAX(DECODE(doc.field_seq, 11, uc.characteristic_value,      NULL)) AS characteristic_charvalue11,
                   MAX(DECODE(doc.field_seq, 12, doc.characteristic_id,        NULL)) AS characteristic_id12,
                   MAX(DECODE(doc.field_seq, 12, doc.characteristic_data_type, NULL)) AS characteristic_datatype12,
                   MAX(DECODE(doc.field_seq, 12, uc.characteristic_value,      NULL)) AS characteristic_charvalue12,
                   MAX(DECODE(doc.field_seq, 13, doc.characteristic_id,        NULL)) AS characteristic_id13,
                   MAX(DECODE(doc.field_seq, 13, doc.characteristic_data_type, NULL)) AS characteristic_datatype13,
                   MAX(DECODE(doc.field_seq, 13, uc.characteristic_value,      NULL)) AS characteristic_charvalue13,
                   MAX(DECODE(doc.field_seq, 14, doc.characteristic_id,        NULL)) AS characteristic_id14,
                   MAX(DECODE(doc.field_seq, 14, doc.characteristic_data_type, NULL)) AS characteristic_datatype14,
                   MAX(DECODE(doc.field_seq, 14, uc.characteristic_value,      NULL)) AS characteristic_charvalue14,
                   MAX(DECODE(doc.field_seq, 15, doc.characteristic_id,        NULL)) AS characteristic_id15,
                   MAX(DECODE(doc.field_seq, 15, doc.characteristic_data_type, NULL)) AS characteristic_datatype15,
                   MAX(DECODE(doc.field_seq, 15, uc.characteristic_value,      NULL)) AS characteristic_charvalue15,
                   MAX(DECODE(doc.field_seq, 16, doc.characteristic_id,        NULL)) AS characteristic_id16,           --03/03/2016 Start
                   MAX(DECODE(doc.field_seq, 16, doc.characteristic_data_type, NULL)) AS characteristic_datatype16,
                   MAX(DECODE(doc.field_seq, 16, uc.characteristic_value,      NULL)) AS characteristic_charvalue16,
                   MAX(DECODE(doc.field_seq, 17, doc.characteristic_id,        NULL)) AS characteristic_id17,
                   MAX(DECODE(doc.field_seq, 17, doc.characteristic_data_type, NULL)) AS characteristic_datatype17,
                   MAX(DECODE(doc.field_seq, 17, uc.characteristic_value,      NULL)) AS characteristic_charvalue17,
                   MAX(DECODE(doc.field_seq, 18, doc.characteristic_id,        NULL)) AS characteristic_id18,
                   MAX(DECODE(doc.field_seq, 18, doc.characteristic_data_type, NULL)) AS characteristic_datatype18,
                   MAX(DECODE(doc.field_seq, 18, uc.characteristic_value,      NULL)) AS characteristic_charvalue18,
                   MAX(DECODE(doc.field_seq, 19, doc.characteristic_id,        NULL)) AS characteristic_id19,
                   MAX(DECODE(doc.field_seq, 19, doc.characteristic_data_type, NULL)) AS characteristic_datatype19,
                   MAX(DECODE(doc.field_seq, 19, uc.characteristic_value,      NULL)) AS characteristic_charvalue19,
                   MAX(DECODE(doc.field_seq, 20, doc.characteristic_id,        NULL)) AS characteristic_id20,
                   MAX(DECODE(doc.field_seq, 20, doc.characteristic_data_type, NULL)) AS characteristic_datatype20,
                   MAX(DECODE(doc.field_seq, 20, uc.characteristic_value,      NULL)) AS characteristic_charvalue20     --03/03/2016 End
              FROM ( -- get top 5 display order characteristics
                     SELECT au.user_id AS user_nt_id,
                            sc.characteristic_type,
                            sc.characteristic_id,
                            sc.characteristic_data_type,
                            sc.field_seq
                       FROM ( -- sequence characteristics
                              SELECT cl.*,
                                     ROW_NUMBER() OVER (ORDER BY cl.display_order, cl.characteristic_id) AS field_seq
                                FROM rpt_characteristic_lookup cl
                               WHERE cl.characteristic_type = c_chr_type_user
                            ) sc,
                            application_user au
                         -- cartesean join user to lookup characteristics
                      WHERE sc.field_seq <=  20 --15 --03/03/2016
                   ) doc,
                   user_characteristic uc
                -- outer join, user may not have the characteristic
             WHERE doc.user_nt_id = uc.user_id (+)
               AND doc.characteristic_id = uc.characteristic_id (+)
               AND uc.characteristic_id NOT IN (SELECT characteristic_id        -- 09/27/2017
                                                  FROM characteristic           -- 09/27/2017
                                                 WHERE visibility = 'hidden')   -- 09/27/2017
             GROUP BY doc.user_nt_id,
                   doc.characteristic_type
         ) s
      ON (   d.user_nt_id = s.user_nt_id
         AND d.characteristic_type = s.characteristic_type )
    WHEN MATCHED THEN
      UPDATE SET
         d.characteristic_id1          = s.characteristic_id1,
         d.characteristic_datatype1    = s.characteristic_datatype1,
         d.characteristic_charvalue1   = s.characteristic_charvalue1,
         d.characteristic_id2          = s.characteristic_id2,
         d.characteristic_datatype2    = s.characteristic_datatype2,
         d.characteristic_charvalue2   = s.characteristic_charvalue2,
         d.characteristic_id3          = s.characteristic_id3,
         d.characteristic_datatype3    = s.characteristic_datatype3,
         d.characteristic_charvalue3   = s.characteristic_charvalue3,
         d.characteristic_id4          = s.characteristic_id4,
         d.characteristic_datatype4    = s.characteristic_datatype4,
         d.characteristic_charvalue4   = s.characteristic_charvalue4,
         d.characteristic_id5          = s.characteristic_id5,
         d.characteristic_datatype5    = s.characteristic_datatype5,
         d.characteristic_charvalue5   = s.characteristic_charvalue5,       
         d.characteristic_id6          = s.characteristic_id6,
         d.characteristic_datatype6    = s.characteristic_datatype6,
         d.characteristic_charvalue6   = s.characteristic_charvalue6,
         d.characteristic_id7          = s.characteristic_id7,
         d.characteristic_datatype7    = s.characteristic_datatype7,
         d.characteristic_charvalue7   = s.characteristic_charvalue7,
         d.characteristic_id8          = s.characteristic_id8,
         d.characteristic_datatype8    = s.characteristic_datatype8,
         d.characteristic_charvalue8   = s.characteristic_charvalue8,
         d.characteristic_id9          = s.characteristic_id9,
         d.characteristic_datatype9    = s.characteristic_datatype9,
         d.characteristic_charvalue9   = s.characteristic_charvalue9,
         d.characteristic_id10          = s.characteristic_id10,
         d.characteristic_datatype10    = s.characteristic_datatype10,
         d.characteristic_charvalue10   = s.characteristic_charvalue10,
         d.characteristic_id11          = s.characteristic_id11,
         d.characteristic_datatype11    = s.characteristic_datatype11,
         d.characteristic_charvalue11   = s.characteristic_charvalue11,
         d.characteristic_id12          = s.characteristic_id12,
         d.characteristic_datatype12    = s.characteristic_datatype12,
         d.characteristic_charvalue12   = s.characteristic_charvalue12,
         d.characteristic_id13          = s.characteristic_id13,
         d.characteristic_datatype13    = s.characteristic_datatype13,
         d.characteristic_charvalue13   = s.characteristic_charvalue13,
         d.characteristic_id14          = s.characteristic_id14,
         d.characteristic_datatype14    = s.characteristic_datatype14,
         d.characteristic_charvalue14   = s.characteristic_charvalue14,
         d.characteristic_id15          = s.characteristic_id15,
         d.characteristic_datatype15    = s.characteristic_datatype15,
         d.characteristic_charvalue15   = s.characteristic_charvalue15,
         d.characteristic_id16          = s.characteristic_id16,                --03/03/2016 Start
         d.characteristic_datatype16    = s.characteristic_datatype16,
         d.characteristic_charvalue16   = s.characteristic_charvalue16,
         d.characteristic_id17          = s.characteristic_id17,
         d.characteristic_datatype17    = s.characteristic_datatype17,
         d.characteristic_charvalue17   = s.characteristic_charvalue17,
         d.characteristic_id18          = s.characteristic_id18,
         d.characteristic_datatype18    = s.characteristic_datatype18,
         d.characteristic_charvalue18   = s.characteristic_charvalue18,
         d.characteristic_id19          = s.characteristic_id19,
         d.characteristic_datatype19    = s.characteristic_datatype19,
         d.characteristic_charvalue19   = s.characteristic_charvalue19,
         d.characteristic_id20          = s.characteristic_id20,
         d.characteristic_datatype20    = s.characteristic_datatype20,
         d.characteristic_charvalue20   = s.characteristic_charvalue20          --03/03/2016 Start
       WHERE ( -- only update records with different values
                DECODE(d.characteristic_id1,        s.characteristic_id1,        1, 0) = 0
             OR DECODE(d.characteristic_datatype1,  s.characteristic_datatype1,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue1, s.characteristic_charvalue1, 1, 0) = 0
             OR DECODE(d.characteristic_id2,        s.characteristic_id2,        1, 0) = 0
             OR DECODE(d.characteristic_datatype2,  s.characteristic_datatype2,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue2, s.characteristic_charvalue2, 1, 0) = 0
             OR DECODE(d.characteristic_id3,        s.characteristic_id3,        1, 0) = 0
             OR DECODE(d.characteristic_datatype3,  s.characteristic_datatype3,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue3, s.characteristic_charvalue3, 1, 0) = 0
             OR DECODE(d.characteristic_id4,        s.characteristic_id4,        1, 0) = 0
             OR DECODE(d.characteristic_datatype4,  s.characteristic_datatype4,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue4, s.characteristic_charvalue4, 1, 0) = 0
             OR DECODE(d.characteristic_id5,        s.characteristic_id5,        1, 0) = 0
             OR DECODE(d.characteristic_datatype5,  s.characteristic_datatype5,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue5, s.characteristic_charvalue5, 1, 0) = 0
             OR DECODE(d.characteristic_id6,        s.characteristic_id6,        1, 0) = 0
             OR DECODE(d.characteristic_datatype6,  s.characteristic_datatype6,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue6, s.characteristic_charvalue6, 1, 0) = 0
             OR DECODE(d.characteristic_id7,        s.characteristic_id7,        1, 0) = 0
             OR DECODE(d.characteristic_datatype7,  s.characteristic_datatype7,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue7, s.characteristic_charvalue7, 1, 0) = 0
             OR DECODE(d.characteristic_id8,        s.characteristic_id8,        1, 0) = 0
             OR DECODE(d.characteristic_datatype8,  s.characteristic_datatype8,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue8, s.characteristic_charvalue8, 1, 0) = 0
             OR DECODE(d.characteristic_id9,        s.characteristic_id9,        1, 0) = 0
             OR DECODE(d.characteristic_datatype9,  s.characteristic_datatype9,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue9, s.characteristic_charvalue9, 1, 0) = 0
             OR DECODE(d.characteristic_id10,        s.characteristic_id10,        1, 0) = 0
             OR DECODE(d.characteristic_datatype10,  s.characteristic_datatype10,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue10, s.characteristic_charvalue10, 1, 0) = 0
             OR DECODE(d.characteristic_id11,        s.characteristic_id11,        1, 0) = 0
             OR DECODE(d.characteristic_datatype11,  s.characteristic_datatype11,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue11, s.characteristic_charvalue11, 1, 0) = 0
             OR DECODE(d.characteristic_id12,        s.characteristic_id12,        1, 0) = 0
             OR DECODE(d.characteristic_datatype12,  s.characteristic_datatype12,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue12, s.characteristic_charvalue12, 1, 0) = 0
             OR DECODE(d.characteristic_id13,        s.characteristic_id13,        1, 0) = 0
             OR DECODE(d.characteristic_datatype13,  s.characteristic_datatype13,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue13, s.characteristic_charvalue13, 1, 0) = 0
             OR DECODE(d.characteristic_id14,        s.characteristic_id14,        1, 0) = 0
             OR DECODE(d.characteristic_datatype14,  s.characteristic_datatype14,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue14, s.characteristic_charvalue14, 1, 0) = 0
             OR DECODE(d.characteristic_id15,        s.characteristic_id15,        1, 0) = 0
             OR DECODE(d.characteristic_datatype15,  s.characteristic_datatype15,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue15, s.characteristic_charvalue15, 1, 0) = 0
             OR DECODE(d.characteristic_id16,        s.characteristic_id16,        1, 0) = 0    --03/03/2016 Start
             OR DECODE(d.characteristic_datatype16,  s.characteristic_datatype16,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue16, s.characteristic_charvalue16, 1, 0) = 0
             OR DECODE(d.characteristic_id17,        s.characteristic_id17,        1, 0) = 0
             OR DECODE(d.characteristic_datatype17,  s.characteristic_datatype17,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue17, s.characteristic_charvalue17, 1, 0) = 0
             OR DECODE(d.characteristic_id18,        s.characteristic_id18,        1, 0) = 0
             OR DECODE(d.characteristic_datatype18,  s.characteristic_datatype18,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue18, s.characteristic_charvalue18, 1, 0) = 0
             OR DECODE(d.characteristic_id19,        s.characteristic_id19,        1, 0) = 0
             OR DECODE(d.characteristic_datatype19,  s.characteristic_datatype19,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue19, s.characteristic_charvalue19, 1, 0) = 0
             OR DECODE(d.characteristic_id20,        s.characteristic_id20,        1, 0) = 0
             OR DECODE(d.characteristic_datatype20,  s.characteristic_datatype20,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue20, s.characteristic_charvalue20, 1, 0) = 0    --03/03/2016 End
             )
    WHEN NOT MATCHED THEN
      INSERT
      (  -- key fields
         user_nt_id,
         characteristic_type,
         -- reference fields
         characteristic_id1,
         characteristic_datatype1,
         characteristic_charvalue1,
         characteristic_id2,
         characteristic_datatype2,
         characteristic_charvalue2,
         characteristic_id3,
         characteristic_datatype3,
         characteristic_charvalue3,
         characteristic_id4,
         characteristic_datatype4,
         characteristic_charvalue4,
         characteristic_id5,
         characteristic_datatype5,
         characteristic_charvalue5,         
         characteristic_id6,
         characteristic_datatype6,
         characteristic_charvalue6,
         characteristic_id7,
         characteristic_datatype7,
         characteristic_charvalue7,
         characteristic_id8,
         characteristic_datatype8,
         characteristic_charvalue8,
         characteristic_id9,
         characteristic_datatype9,
         characteristic_charvalue9,
         characteristic_id10,
         characteristic_datatype10,
         characteristic_charvalue10,         
         characteristic_id11,
         characteristic_datatype11,
         characteristic_charvalue11,
         characteristic_id12,
         characteristic_datatype12,
         characteristic_charvalue12,
         characteristic_id13,
         characteristic_datatype13,
         characteristic_charvalue13,
         characteristic_id14,
         characteristic_datatype14,
         characteristic_charvalue14,
         characteristic_id15,
         characteristic_datatype15,
         characteristic_charvalue15,
         characteristic_id16,           --03/03/2016 Start
         characteristic_datatype16,
         characteristic_charvalue16,
         characteristic_id17,
         characteristic_datatype17,
         characteristic_charvalue17,
         characteristic_id18,
         characteristic_datatype18,
         characteristic_charvalue18,
         characteristic_id19,
         characteristic_datatype19,
         characteristic_charvalue19,
         characteristic_id20,
         characteristic_datatype20,
         characteristic_charvalue20     --03/03/2016 End  
      )
      VALUES
      (  -- key fields
         s.user_nt_id,
         s.characteristic_type,
         -- reference fields
         s.characteristic_id1,
         s.characteristic_datatype1,
         s.characteristic_charvalue1,
         s.characteristic_id2,
         s.characteristic_datatype2,
         s.characteristic_charvalue2,
         s.characteristic_id3,
         s.characteristic_datatype3,
         s.characteristic_charvalue3,
         s.characteristic_id4,
         s.characteristic_datatype4,
         s.characteristic_charvalue4,
         s.characteristic_id5,
         s.characteristic_datatype5,
         s.characteristic_charvalue5,
          s.characteristic_id6,
          s.characteristic_datatype6,
          s.characteristic_charvalue6,
          s.characteristic_id7,
          s.characteristic_datatype7,
          s.characteristic_charvalue7,
          s.characteristic_id8,
          s.characteristic_datatype8,
          s.characteristic_charvalue8,
          s.characteristic_id9,
          s.characteristic_datatype9,
          s.characteristic_charvalue9,
          s.characteristic_id10,
          s.characteristic_datatype10,
          s.characteristic_charvalue10,         
          s.characteristic_id11,
          s.characteristic_datatype11,
          s.characteristic_charvalue11,
          s.characteristic_id12,
          s.characteristic_datatype12,
          s.characteristic_charvalue12,
          s.characteristic_id13,
          s.characteristic_datatype13,
          s.characteristic_charvalue13,
          s.characteristic_id14,
          s.characteristic_datatype14,
          s.characteristic_charvalue14,
          s.characteristic_id15,
          s.characteristic_datatype15,
          s.characteristic_charvalue15,
          s.characteristic_id16,           --03/03/2016 Start
          s.characteristic_datatype16,
          s.characteristic_charvalue16,
          s.characteristic_id17,
          s.characteristic_datatype17,
          s.characteristic_charvalue17,
          s.characteristic_id18,
          s.characteristic_datatype18,
          s.characteristic_charvalue18,
          s.characteristic_id19,
          s.characteristic_datatype19,
          s.characteristic_charvalue19,
          s.characteristic_id20,
          s.characteristic_datatype20,
          s.characteristic_charvalue20     --03/03/2016 End
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, c_sev_info, v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   v_stage := 'MERGE rpt_characteristic >' || c_chr_type_node || '<';
   MERGE INTO rpt_characteristic d
   USING (  -- get associated characteristic value and pivot into a single record by key fields
            SELECT -- key fields
                   doc.user_nt_id,
                   doc.characteristic_type,
                   -- pivot characteristics
                   MAX(DECODE(doc.field_seq, 1, doc.characteristic_id,        NULL)) AS characteristic_id1,
                   MAX(DECODE(doc.field_seq, 1, doc.characteristic_data_type, NULL)) AS characteristic_datatype1,
                   MAX(DECODE(doc.field_seq, 1, nc.characteristic_value,      NULL)) AS characteristic_charvalue1,
                   MAX(DECODE(doc.field_seq, 2, doc.characteristic_id,        NULL)) AS characteristic_id2,
                   MAX(DECODE(doc.field_seq, 2, doc.characteristic_data_type, NULL)) AS characteristic_datatype2,
                   MAX(DECODE(doc.field_seq, 2, nc.characteristic_value,      NULL)) AS characteristic_charvalue2,
                   MAX(DECODE(doc.field_seq, 3, doc.characteristic_id,        NULL)) AS characteristic_id3,
                   MAX(DECODE(doc.field_seq, 3, doc.characteristic_data_type, NULL)) AS characteristic_datatype3,
                   MAX(DECODE(doc.field_seq, 3, nc.characteristic_value,      NULL)) AS characteristic_charvalue3,
                   MAX(DECODE(doc.field_seq, 4, doc.characteristic_id,        NULL)) AS characteristic_id4,
                   MAX(DECODE(doc.field_seq, 4, doc.characteristic_data_type, NULL)) AS characteristic_datatype4,
                   MAX(DECODE(doc.field_seq, 4, nc.characteristic_value,      NULL)) AS characteristic_charvalue4,
                   MAX(DECODE(doc.field_seq, 5, doc.characteristic_id,        NULL)) AS characteristic_id5,
                   MAX(DECODE(doc.field_seq, 5, doc.characteristic_data_type, NULL)) AS characteristic_datatype5,
                   MAX(DECODE(doc.field_seq, 5, nc.characteristic_value,      NULL)) AS characteristic_charvalue5,
                   MAX(DECODE(doc.field_seq, 6, doc.characteristic_id,        NULL)) AS characteristic_id6,
                   MAX(DECODE(doc.field_seq, 6, doc.characteristic_data_type, NULL)) AS characteristic_datatype6,
                   MAX(DECODE(doc.field_seq, 6, nc.characteristic_value,      NULL)) AS characteristic_charvalue6,
                   MAX(DECODE(doc.field_seq, 7, doc.characteristic_id,        NULL)) AS characteristic_id7,
                   MAX(DECODE(doc.field_seq, 7, doc.characteristic_data_type, NULL)) AS characteristic_datatype7,
                   MAX(DECODE(doc.field_seq, 7, nc.characteristic_value,      NULL)) AS characteristic_charvalue7,
                   MAX(DECODE(doc.field_seq, 8, doc.characteristic_id,        NULL)) AS characteristic_id8,
                   MAX(DECODE(doc.field_seq, 8, doc.characteristic_data_type, NULL)) AS characteristic_datatype8,
                   MAX(DECODE(doc.field_seq, 8, nc.characteristic_value,      NULL)) AS characteristic_charvalue8,
                   MAX(DECODE(doc.field_seq, 9, doc.characteristic_id,        NULL)) AS characteristic_id9,
                   MAX(DECODE(doc.field_seq, 9, doc.characteristic_data_type, NULL)) AS characteristic_datatype9,
                   MAX(DECODE(doc.field_seq, 9, nc.characteristic_value,      NULL)) AS characteristic_charvalue9,
                   MAX(DECODE(doc.field_seq, 10, doc.characteristic_id,        NULL)) AS characteristic_id10,
                   MAX(DECODE(doc.field_seq, 10, doc.characteristic_data_type, NULL)) AS characteristic_datatype10,
                   MAX(DECODE(doc.field_seq, 10, nc.characteristic_value,      NULL)) AS characteristic_charvalue10
              FROM ( -- get top 10 display order characteristics
                     SELECT DISTINCT
                            un.node_id AS user_nt_id,
                            sc.characteristic_type,
                            sc.characteristic_id,
                            sc.characteristic_data_type,
                            sc.field_seq
                       FROM ( -- sequence characteristics
                              SELECT cl.*,
                                     ROW_NUMBER() OVER (ORDER BY cl.display_order, cl.characteristic_id) AS field_seq
                                FROM rpt_characteristic_lookup cl
                               WHERE cl.characteristic_type = c_chr_type_node
                            ) sc,
                            user_node un
                         -- cartesean join node to lookup characteristics
                      WHERE sc.field_seq <= 10
                   ) doc,
                   node_characteristic nc
                -- outer join, node may not have the characteristic
             WHERE doc.user_nt_id = nc.node_id (+)
               AND doc.characteristic_id = nc.characteristic_id (+)
             GROUP BY doc.user_nt_id,
                   doc.characteristic_type
         ) s
      ON (   d.user_nt_id = s.user_nt_id
         AND d.characteristic_type = s.characteristic_type )
    WHEN MATCHED THEN
      UPDATE SET
         d.characteristic_id1          = s.characteristic_id1,
         d.characteristic_datatype1    = s.characteristic_datatype1,
         d.characteristic_charvalue1   = s.characteristic_charvalue1,
         d.characteristic_id2          = s.characteristic_id2,
         d.characteristic_datatype2    = s.characteristic_datatype2,
         d.characteristic_charvalue2   = s.characteristic_charvalue2,
         d.characteristic_id3          = s.characteristic_id3,
         d.characteristic_datatype3    = s.characteristic_datatype3,
         d.characteristic_charvalue3   = s.characteristic_charvalue3,
         d.characteristic_id4          = s.characteristic_id4,
         d.characteristic_datatype4    = s.characteristic_datatype4,
         d.characteristic_charvalue4   = s.characteristic_charvalue4,
         d.characteristic_id5          = s.characteristic_id5,
         d.characteristic_datatype5    = s.characteristic_datatype5,
         d.characteristic_charvalue5   = s.characteristic_charvalue5,       
         d.characteristic_id6          = s.characteristic_id6,
         d.characteristic_datatype6    = s.characteristic_datatype6,
         d.characteristic_charvalue6   = s.characteristic_charvalue6,
         d.characteristic_id7          = s.characteristic_id7,
         d.characteristic_datatype7    = s.characteristic_datatype7,
         d.characteristic_charvalue7   = s.characteristic_charvalue7,
         d.characteristic_id8          = s.characteristic_id8,
         d.characteristic_datatype8    = s.characteristic_datatype8,
         d.characteristic_charvalue8   = s.characteristic_charvalue8,
         d.characteristic_id9          = s.characteristic_id9,
         d.characteristic_datatype9    = s.characteristic_datatype9,
         d.characteristic_charvalue9   = s.characteristic_charvalue9,
         d.characteristic_id10          = s.characteristic_id10,
         d.characteristic_datatype10    = s.characteristic_datatype10,
         d.characteristic_charvalue10   = s.characteristic_charvalue10
       WHERE ( -- only update records with different values
                DECODE(d.characteristic_id1,        s.characteristic_id1,        1, 0) = 0
             OR DECODE(d.characteristic_datatype1,  s.characteristic_datatype1,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue1, s.characteristic_charvalue1, 1, 0) = 0
             OR DECODE(d.characteristic_id2,        s.characteristic_id2,        1, 0) = 0
             OR DECODE(d.characteristic_datatype2,  s.characteristic_datatype2,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue2, s.characteristic_charvalue2, 1, 0) = 0
             OR DECODE(d.characteristic_id3,        s.characteristic_id3,        1, 0) = 0
             OR DECODE(d.characteristic_datatype3,  s.characteristic_datatype3,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue3, s.characteristic_charvalue3, 1, 0) = 0
             OR DECODE(d.characteristic_id4,        s.characteristic_id4,        1, 0) = 0
             OR DECODE(d.characteristic_datatype4,  s.characteristic_datatype4,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue4, s.characteristic_charvalue4, 1, 0) = 0
             OR DECODE(d.characteristic_id5,        s.characteristic_id5,        1, 0) = 0
             OR DECODE(d.characteristic_datatype5,  s.characteristic_datatype5,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue5, s.characteristic_charvalue5, 1, 0) = 0
             OR DECODE(d.characteristic_id6,        s.characteristic_id6,        1, 0) = 0
             OR DECODE(d.characteristic_datatype6,  s.characteristic_datatype6,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue6, s.characteristic_charvalue6, 1, 0) = 0
             OR DECODE(d.characteristic_id7,        s.characteristic_id7,        1, 0) = 0
             OR DECODE(d.characteristic_datatype7,  s.characteristic_datatype7,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue7, s.characteristic_charvalue7, 1, 0) = 0
             OR DECODE(d.characteristic_id8,        s.characteristic_id8,        1, 0) = 0
             OR DECODE(d.characteristic_datatype8,  s.characteristic_datatype8,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue8, s.characteristic_charvalue8, 1, 0) = 0
             OR DECODE(d.characteristic_id9,        s.characteristic_id9,        1, 0) = 0
             OR DECODE(d.characteristic_datatype9,  s.characteristic_datatype9,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue9, s.characteristic_charvalue9, 1, 0) = 0
             OR DECODE(d.characteristic_id10,        s.characteristic_id10,        1, 0) = 0
             OR DECODE(d.characteristic_datatype10,  s.characteristic_datatype10,  1, 0) = 0
             OR DECODE(d.characteristic_charvalue10, s.characteristic_charvalue10, 1, 0) = 0
             )
    WHEN NOT MATCHED THEN
      INSERT
      (  -- key fields
         user_nt_id,
         characteristic_type,
         -- reference fields
         characteristic_id1,
         characteristic_datatype1,
         characteristic_charvalue1,
         characteristic_id2,
         characteristic_datatype2,
         characteristic_charvalue2,
         characteristic_id3,
         characteristic_datatype3,
         characteristic_charvalue3,
         characteristic_id4,
         characteristic_datatype4,
         characteristic_charvalue4,
         characteristic_id5,
         characteristic_datatype5,
         characteristic_charvalue5,         
         characteristic_id6,
         characteristic_datatype6,
         characteristic_charvalue6,
         characteristic_id7,
         characteristic_datatype7,
         characteristic_charvalue7,
         characteristic_id8,
         characteristic_datatype8,
         characteristic_charvalue8,
         characteristic_id9,
         characteristic_datatype9,
         characteristic_charvalue9,
         characteristic_id10,
         characteristic_datatype10,
         characteristic_charvalue10
      )
      VALUES
      (  -- key fields
         s.user_nt_id,
         s.characteristic_type,
         -- reference fields
         s.characteristic_id1,
         s.characteristic_datatype1,
         s.characteristic_charvalue1,
         s.characteristic_id2,
         s.characteristic_datatype2,
         s.characteristic_charvalue2,
         s.characteristic_id3,
         s.characteristic_datatype3,
         s.characteristic_charvalue3,
         s.characteristic_id4,
         s.characteristic_datatype4,
         s.characteristic_charvalue4,
         s.characteristic_id5,
         s.characteristic_datatype5,
         s.characteristic_charvalue5,
          s.characteristic_id6,
          s.characteristic_datatype6,
          s.characteristic_charvalue6,
          s.characteristic_id7,
          s.characteristic_datatype7,
          s.characteristic_charvalue7,
          s.characteristic_id8,
          s.characteristic_datatype8,
          s.characteristic_charvalue8,
          s.characteristic_id9,
          s.characteristic_datatype9,
          s.characteristic_charvalue9,
          s.characteristic_id10,
          s.characteristic_datatype10,
          s.characteristic_charvalue10
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, c_sev_info, v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   v_stage := 'Success';
   prc_execution_log_entry(c_process_name, c_release_level, c_sev_info, v_stage, NULL);

EXCEPTION
  WHEN others THEN
      prc_execution_log_entry(c_process_name, c_release_level, c_sev_err,
                              ' FAILED AT Stage: ' || v_stage || ' --> ' || SQLERRM, NULL);
END P_RPT_CHARACTERISTICS;

---------------------------------------------------------------------------------

PROCEDURE P_CHARACTERISTIC_DETAILS
 (p_in_characteristic_type IN VARCHAR2, -- in parm  USER or NT
  p_in_user_nt_id IN NUMBER, -- in parm  user_id or node_id
  p_characteristic_rec OUT rpt_characteristic%ROWTYPE) IS

/*******************************************************************************

   Purpose:  Return up to 5 USER characteristics or 5 NT (node) characteristics
             per person that reports will use.

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray    11/07/2005  Initial packaging of common procedures
   Raju N      11/14/2005  Fixed the code to make sure that the same characteristics
                           line up in the samee column throughout the table.
   Raju N      11/16/2005  Modified the code to accomodate for the new table
                            structure for rpt_characteristic.
   Raju N      06/26/2006  included the data type for characteristic in the cursor
                           date fetched from RPT_CHARACTERISTIC_LOOKUP (cur_characteristic).
                           Changed the code to get this value assined and inserted
                           into rpt_characteristic.
*******************************************************************************/
   CURSOR cur_characteristic (p_char_type  VARCHAR2) IS
     SELECT characteristic_id, cm_asset_code,characteristic_data_type
       FROM RPT_CHARACTERISTIC_LOOKUP
      WHERE characteristic_type = p_char_type
        AND ROWNUM <= 5; -- will need an order by or other criteria otherwise
                         -- no guarentee that you will always get the same 5 characteristics

   CURSOR cur_pax_char (p_char_id  NUMBER, p_id  NUMBER) IS
     SELECT characteristic_value
       FROM user_characteristic uc
      WHERE characteristic_id = p_char_id
        AND uc.user_id = p_id;

   CURSOR cur_node_char (p_char_id  NUMBER, p_id  NUMBER) IS
     SELECT characteristic_value
       FROM node_characteristic
      WHERE characteristic_id = p_char_id
        AND node_id = p_id;

  characteristic_rec  rpt_characteristic%ROWTYPE;
  v_rec_cnt    NUMBER := 0;

BEGIN
  p_characteristic_rec.user_nt_id := p_in_user_nt_id;
  p_characteristic_rec.characteristic_type := p_in_characteristic_type;
  IF v_debug = 'Y' THEN
    dbms_output.put_line('p_in_user_nt_id '||p_in_user_nt_id);
    dbms_output.put_line('p_in_characteristic_type '||p_in_characteristic_type);
  END IF ;
  FOR rec_characteristic IN cur_characteristic (p_in_characteristic_type) LOOP
  BEGIN
    IF p_in_characteristic_type = 'USER' THEN
          v_rec_cnt := v_rec_cnt + 1;

          IF v_rec_cnt = 1 THEN
            p_characteristic_rec.characteristic_datatype1 :=
                       rec_characteristic.characteristic_data_type ;
            p_characteristic_rec.characteristic_id1 := rec_characteristic.characteristic_id;
            p_characteristic_rec.characteristic_charvalue1 :=
                   fnc_user_charactersitic_value(rec_characteristic.characteristic_id,
                                                 p_in_user_nt_id);
          IF v_debug = 'Y' THEN
              dbms_output.put_line(rec_characteristic.characteristic_id);
              dbms_output.put_line('p_in_user_nt_id'||p_in_user_nt_id);
              dbms_output.put_line(p_characteristic_rec.characteristic_charvalue1);
          END IF ;
          ELSIF v_rec_cnt = 2 THEN
            p_characteristic_rec.characteristic_datatype2 :=
                       rec_characteristic.characteristic_data_type ;
            p_characteristic_rec.characteristic_id2 := rec_characteristic.characteristic_id;
            p_characteristic_rec.characteristic_charvalue2 :=
                   fnc_user_charactersitic_value(rec_characteristic.characteristic_id,
                                                 p_in_user_nt_id);
          ELSIF v_rec_cnt = 3 THEN
            p_characteristic_rec.characteristic_datatype3 :=
                       rec_characteristic.characteristic_data_type ;
            p_characteristic_rec.characteristic_id3 := rec_characteristic.characteristic_id;
            p_characteristic_rec.characteristic_charvalue3 :=
                   fnc_user_charactersitic_value(rec_characteristic.characteristic_id,
                                                 p_in_user_nt_id);
          ELSIF v_rec_cnt = 4 THEN
            p_characteristic_rec.characteristic_datatype4 :=
                       rec_characteristic.characteristic_data_type ;
            p_characteristic_rec.characteristic_id4 := rec_characteristic.characteristic_id;
            p_characteristic_rec.characteristic_charvalue4 :=
                   fnc_user_charactersitic_value(rec_characteristic.characteristic_id,
                                                 p_in_user_nt_id);
          ELSIF v_rec_cnt = 5 THEN
            p_characteristic_rec.characteristic_datatype5 :=
                       rec_characteristic.characteristic_data_type ;
            p_characteristic_rec.characteristic_id5 := rec_characteristic.characteristic_id;
            p_characteristic_rec.characteristic_charvalue5 :=
                   fnc_user_charactersitic_value(rec_characteristic.characteristic_id,
                                                 p_in_user_nt_id);
          ELSE
            EXIT;
          END IF; -- v_rec_cnt
    END IF; -- 'USER'
  EXCEPTION
    WHEN OTHERS  THEN
      prc_execution_log_entry('P_CHARACTERISTIC_DETAILS',1,'ERROR',SQLERRM,null);
      p_characteristic_rec := NULL;
      RETURN ;
  END;

  BEGIN
    IF p_in_characteristic_type = 'NT' THEN
          v_rec_cnt := v_rec_cnt + 1;
          IF v_rec_cnt = 1 THEN
            p_characteristic_rec.characteristic_datatype1 :=
                       rec_characteristic.characteristic_data_type ;
            p_characteristic_rec.characteristic_id1 := rec_characteristic.characteristic_id;
            p_characteristic_rec.characteristic_charvalue1 :=
                   fnc_nt_charactersitic_value(rec_characteristic.characteristic_id,
                                                 p_in_user_nt_id);
          ELSIF v_rec_cnt = 2 THEN
            p_characteristic_rec.characteristic_datatype2 :=
                       rec_characteristic.characteristic_data_type ;
            p_characteristic_rec.characteristic_id2 := rec_characteristic.characteristic_id;
            p_characteristic_rec.characteristic_charvalue2 :=
                   fnc_nt_charactersitic_value(rec_characteristic.characteristic_id,
                                                 p_in_user_nt_id);
          ELSIF v_rec_cnt = 3 THEN
            p_characteristic_rec.characteristic_datatype3 :=
                       rec_characteristic.characteristic_data_type ;
            p_characteristic_rec.characteristic_id3 := rec_characteristic.characteristic_id;
            p_characteristic_rec.characteristic_charvalue3 :=
                   fnc_nt_charactersitic_value(rec_characteristic.characteristic_id,
                                                 p_in_user_nt_id);
          ELSIF v_rec_cnt = 4 THEN
            p_characteristic_rec.characteristic_datatype4 :=
                       rec_characteristic.characteristic_data_type ;
            p_characteristic_rec.characteristic_id4 := rec_characteristic.characteristic_id;
            p_characteristic_rec.characteristic_charvalue4 :=
                   fnc_nt_charactersitic_value(rec_characteristic.characteristic_id,
                                                 p_in_user_nt_id);
          ELSIF v_rec_cnt = 5 THEN
            p_characteristic_rec.characteristic_datatype5 :=
                       rec_characteristic.characteristic_data_type ;
            p_characteristic_rec.characteristic_id5 := rec_characteristic.characteristic_id;
            p_characteristic_rec.characteristic_charvalue5 :=
                   fnc_nt_charactersitic_value(rec_characteristic.characteristic_id,
                                                 p_in_user_nt_id);
          ELSE
            EXIT;
          END IF; -- v_rec_cnt
    END IF; -- 'NT'
  EXCEPTION
    WHEN OTHERS  THEN
      prc_execution_log_entry('P_CHARACTERISTIC_DETAILS',1,'ERROR',SQLERRM,null);
      p_characteristic_rec := NULL;
      RETURN ;
  END;
  END LOOP; -- cur_characteristic

EXCEPTION
  WHEN OTHERS  THEN
    prc_execution_log_entry('P_CHARACTERISTIC_DETAILS',1,'ERROR',SQLERRM,null);
    p_characteristic_rec := NULL;
    RETURN ;
END; -- p_characteristic_details
--------------------------------------------------------------------------------

PROCEDURE P_INSERT_RPT_CHARACTERISTIC
  (p_rptchar_rec IN rpt_characteristic%ROWTYPE) IS
/*******************************************************************************

   Purpose:  Insert characteristic record into rpt_characteristic reporting table

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray    11/09/2005  Initial creation
   Raju N      11/16/2005  Modifications for the udpated table structure.
*******************************************************************************/

BEGIN
   INSERT INTO rpt_characteristic
   (user_nt_id, characteristic_type,
    characteristic_id1,characteristic_datatype1,characteristic_datevalue1,
                       characteristic_numvalue1,characteristic_charvalue1,
    characteristic_id2,characteristic_datatype2,characteristic_datevalue2,
                       characteristic_numvalue2,characteristic_charvalue2,
    characteristic_id3,characteristic_datatype3,characteristic_datevalue3,
                       characteristic_numvalue3,characteristic_charvalue3,
    characteristic_id4,characteristic_datatype4,characteristic_datevalue4,
                       characteristic_numvalue4,characteristic_charvalue4,
    characteristic_id5,characteristic_datatype5, characteristic_datevalue5,
                       characteristic_numvalue5, characteristic_charvalue5)
   VALUES
   (p_rptchar_rec.user_nt_id, p_rptchar_rec.characteristic_type,
    p_rptchar_rec.characteristic_id1, p_rptchar_rec.characteristic_datatype1,
      p_rptchar_rec.characteristic_datevalue1,p_rptchar_rec.characteristic_numvalue1,
      p_rptchar_rec.characteristic_charvalue1,
    p_rptchar_rec.characteristic_id2, p_rptchar_rec.characteristic_datatype2,
      p_rptchar_rec.characteristic_datevalue2,p_rptchar_rec.characteristic_numvalue2,
      p_rptchar_rec.characteristic_charvalue2,
    p_rptchar_rec.characteristic_id3, p_rptchar_rec.characteristic_datatype3,
      p_rptchar_rec.characteristic_datevalue3,p_rptchar_rec.characteristic_numvalue3,
      p_rptchar_rec.characteristic_charvalue3,
    p_rptchar_rec.characteristic_id4, p_rptchar_rec.characteristic_datatype4,
      p_rptchar_rec.characteristic_datevalue4,p_rptchar_rec.characteristic_numvalue4,
      p_rptchar_rec.characteristic_charvalue4,
    p_rptchar_rec.characteristic_id5, p_rptchar_rec.characteristic_datatype5,
      p_rptchar_rec.characteristic_datevalue5,p_rptchar_rec.characteristic_numvalue5,
      p_rptchar_rec.characteristic_charvalue5) ;
EXCEPTION
   WHEN others THEN
      prc_execution_log_entry('P_CHARACTERISTIC_DETAILS',1,'ERROR',SQLERRM,null);
END; -- p_insert_rpt_characteristic

PROCEDURE P_GET_CHARACTERISTIC
  ( p_in_characteristic_type IN VARCHAR2 -- in parm  USER or NT
    ,p_in_user_nt_id IN NUMBER -- in parm  user_id or node_id
    ,p_rptchar_str OUT VARCHAR2) IS
/*******************************************************************************

   Purpose:  Get the five characterisitics for the given id an type either node
             or user

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   Raju N        06/26/2006 Creation
*******************************************************************************/

BEGIN
   SELECT characteristic_id1||','||decode(characteristic_datatype1,'date', characteristic_datevalue1,
                                                              'int',characteristic_numvalue1,characteristic_charvalue1) ||','||
          characteristic_id2||','||decode(characteristic_datatype2,'date', characteristic_datevalue2,
                                                              'int',characteristic_numvalue2,characteristic_charvalue2) ||','||
          characteristic_id3||','||decode(characteristic_datatype3,'date', characteristic_datevalue3,
                                                              'int',characteristic_numvalue3,characteristic_charvalue3) ||','||
          characteristic_id4||','||decode(characteristic_datatype4,'date', characteristic_datevalue4,
                                                              'int',characteristic_numvalue4,characteristic_charvalue4) ||','||
          characteristic_id5||','||decode(characteristic_datatype5,'date', characteristic_datevalue5,
                                                              'int',characteristic_numvalue5,characteristic_charvalue5)
     INTO p_rptchar_str
     FROM rpt_characteristic
    WHERE user_nt_id = p_in_user_nt_id
      AND characteristic_type = upper(p_in_characteristic_type) ;
EXCEPTION
   WHEN others THEN
      prc_execution_log_entry('P_GET_CHARACTERISTIC',1,'ERROR',SQLERRM,null);
END; -- P_GET_CHARACTERISTIC
PROCEDURE p_get_characteristic_value
  ( p_in_characteristic_type IN VARCHAR2 -- in parm  USER or NT
    ,p_in_user_nt_id IN NUMBER -- in parm  user_id or node_id
    ,p_rptchar_str OUT VARCHAR2) IS
/*******************************************************************************

   Purpose:  Get the five characterisitics for the given id an type either node
             or user

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   Raju N        06/26/2006 Creation
   Raju N        01/15/2007 Bug# 14963
   Raju N        01/23/2007 Padded the fields with double quotes to handle the
                            multi select values as the values are separated by commas.
*******************************************************************************/

BEGIN
   SELECT '"'||characteristic_charvalue1 ||'"'||','||
          '"'||characteristic_charvalue2 ||'"'||','||
          '"'||characteristic_charvalue3 ||'"'||','||
          '"'||characteristic_charvalue4 ||'"'||','||
          '"'||characteristic_charvalue5 ||'"'
     INTO p_rptchar_str
     FROM rpt_characteristic
    WHERE user_nt_id = p_in_user_nt_id
      AND characteristic_type = upper(p_in_characteristic_type) ;
EXCEPTION
   WHEN others THEN
      prc_execution_log_entry('p_get_characteristic_value',1,'ERROR',SQLERRM,null);
END; -- p_get_characteristic_value

FUNCTION f_get_characteristic_label ( p_in_characteristic_type IN VARCHAR2 -- in parm  USER or NT
                                     )   RETURN VARCHAR2 IS

/*******************************************************************************
   Purpose:  Get the characterisitics label for the given type either node
             or user

  Person        Date        Comments
 -----------   ----------  -----------------------------------------------------

 Arun S        01/09/2009  Bug # 20595 fix, Placed double quotes (") around all export
                           field records
*******************************************************************************/

    v_rptcharlabel_str varchar2(2000) ;
    C_dblquotes        VARCHAR2(1) := '"';

BEGIN
   SELECT C_dblquotes||FNC_CMS_ASSET_CODE_VALUE((select c.cm_asset_code
                                     from characteristic c
                                    where c.characteristic_id = r.characteristic_id1
                                      AND c.characteristic_type = r.characteristic_type))
          ||C_dblquotes||','||C_dblquotes||
          FNC_CMS_ASSET_CODE_VALUE((select c.cm_asset_code
                                     from characteristic c
                                    where c.characteristic_id = r.characteristic_id2
                                      AND c.characteristic_type = r.characteristic_type))
          ||C_dblquotes||','||C_dblquotes||
          FNC_CMS_ASSET_CODE_VALUE((select c.cm_asset_code
                                     from characteristic c
                                    where c.characteristic_id = r.characteristic_id3
                                      AND c.characteristic_type = r.characteristic_type))
          ||C_dblquotes||','||C_dblquotes||
          FNC_CMS_ASSET_CODE_VALUE((select c.cm_asset_code
                                     from characteristic c
                                    where c.characteristic_id = r.characteristic_id4
                                      AND c.characteristic_type = r.characteristic_type))
          ||C_dblquotes||','||C_dblquotes||
          FNC_CMS_ASSET_CODE_VALUE((select c.cm_asset_code
                                     from characteristic c
                                    where c.characteristic_id = r.characteristic_id5
                                      AND c.characteristic_type = r.characteristic_type))
          ||C_dblquotes
INTO v_rptcharlabel_str
FROM
(   SELECT DISTINCT  characteristic_type,characteristic_id1,characteristic_id2,characteristic_id3,characteristic_id4,characteristic_id5
    FROM rpt_characteristic
   WHERE characteristic_type = p_in_characteristic_type) r ;
RETURN    v_rptcharlabel_str ;
EXCEPTION
   WHEN OTHERS THEN
    v_rptcharlabel_str := '"","","","",""' ;
    RETURN v_rptcharlabel_str ;
END ;
FUNCTION f_get_characteristic ( p_in_characteristic_type IN VARCHAR2 -- in parm  USER or NT
    ,p_in_user_nt_id IN NUMBER) RETURN VARCHAR2  IS
    v_rptchar_str varchar2(2000) ;
BEGIN
   p_get_characteristic_value(p_in_characteristic_type,p_in_user_nt_id,v_rptchar_str) ;
   RETURN v_rptchar_str ;
EXCEPTION
   WHEN OTHERS THEN
    v_rptchar_str := '"","","","",""' ;
    RETURN v_rptchar_str ;
END  ;

FUNCTION f_get_characteristic_label ( p_in_characteristic_type IN VARCHAR2, -- in parm  USER or NT
                                      p_in_user_type IN VARCHAR2)   RETURN VARCHAR2 IS

/*******************************************************************************
   Purpose:  Get the characterisitics label for the given type either node
             or user

  Person        Date        Comments
 -----------   ----------  -----------------------------------------------------

 Arun S        01/09/2009  Bug # 20595 fix, Placed double quotes (") around all export
                           field records
 Arun S        03/18/2009  Bug # 19713 fix, Overloading function f_get_characteristic_label
                           to get Giver or Recipient specific characteristic labels
*******************************************************************************/

    v_rptcharlabel_str varchar2(2000) ;
    C_dblquotes        VARCHAR2(1) := '"';

BEGIN
   SELECT C_dblquotes||p_in_user_type||' '||FNC_CMS_ASSET_CODE_VALUE((select c.cm_asset_code
                                     from characteristic c
                                    where c.characteristic_id = r.characteristic_id1
                                      AND c.characteristic_type = r.characteristic_type))
          ||C_dblquotes||','||C_dblquotes||p_in_user_type||' '||
          FNC_CMS_ASSET_CODE_VALUE((select c.cm_asset_code
                                     from characteristic c
                                    where c.characteristic_id = r.characteristic_id2
                                      AND c.characteristic_type = r.characteristic_type))
          ||C_dblquotes||','||C_dblquotes||p_in_user_type||' '||
          FNC_CMS_ASSET_CODE_VALUE((select c.cm_asset_code
                                     from characteristic c
                                    where c.characteristic_id = r.characteristic_id3
                                      AND c.characteristic_type = r.characteristic_type))
          ||C_dblquotes||','||C_dblquotes||p_in_user_type||' '||
          FNC_CMS_ASSET_CODE_VALUE((select c.cm_asset_code
                                     from characteristic c
                                    where c.characteristic_id = r.characteristic_id4
                                      AND c.characteristic_type = r.characteristic_type))
          ||C_dblquotes||','||C_dblquotes||p_in_user_type||' '||
          FNC_CMS_ASSET_CODE_VALUE((select c.cm_asset_code
                                     from characteristic c
                                    where c.characteristic_id = r.characteristic_id5
                                      AND c.characteristic_type = r.characteristic_type))
          ||C_dblquotes
INTO v_rptcharlabel_str
FROM
(   SELECT DISTINCT  characteristic_type,characteristic_id1,characteristic_id2,characteristic_id3,characteristic_id4,characteristic_id5
    FROM rpt_characteristic
   WHERE characteristic_type = p_in_characteristic_type) r ;
RETURN    v_rptcharlabel_str ;
EXCEPTION
   WHEN OTHERS THEN
    v_rptcharlabel_str := '"","","","",""' ;
    RETURN v_rptcharlabel_str ;
END ;

PROCEDURE prc_rpt_user_char_refresh (p_user_id      IN  NUMBER,
  p_return_code           OUT NUMBER,
  p_out_error_message         OUT VARCHAR2)
  IS
  
  v_characteristic_val VARCHAR2(1000); --02/06/2014
  
  cursor c1 is
    select DISTINCT characteristic_value,characteristic_id from user_characteristic WHERE characteristic_id IN (
select characteristic_id from characteristic where characteristic_data_type='multi_select') and characteristic_value like '%,%';

cursor c2 is
SELECT DISTINCT locale from vw_cms_code_value;
            
     c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('prc_rpt_user_char_refresh');
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;  
  v_stage                   execution_log.text_line%TYPE;
  v_rec_cnt                 INTEGER;
  
    
BEGIN

  v_stage := 'Start';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
  
  EXECUTE IMMEDIATE ('truncate table rpt_user_characteristic');
  
FOR C1_R IN C1 LOOP

FOR C2_R IN C2 LOOP
select 
                    listagg(cms_name, ',')
                    within group (order by NULL) INTO v_characteristic_val 
                           from (
SELECT vw.cms_name,vw.locale FROM vw_cms_code_value vw,
(select regexp_substr(c1_r.characteristic_value,'[^,]+', 1, level) characteristic_value
FROM DUAL
connect by regexp_substr(c1_r.characteristic_value, '[^,]+', 1, level) is not null) cvalue
WHERE vw.cms_code=cvalue.characteristic_value AND asset_code<>'picklist.participant.certifications.items' AND vw.locale=c2_r.locale);

INSERT INTO rpt_user_characteristic(locale,cms_name,cms_code,characteristic_id,created_by,date_created)
VALUES (C2_R.locale,v_characteristic_val,c1_r.characteristic_value,c1_r.characteristic_id,p_user_id,SYSDATE);

END LOOP;


END LOOP;

  v_stage := 'Success';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  p_return_code := 0;

EXCEPTION
  WHEN OTHERS THEN
    p_return_code := 99;
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage || ': ' || SQLCODE || ', ' || SQLERRM, NULL);
END prc_rpt_user_char_refresh;

END;
/
