DECLARE

CURSOR C1 IS 
SELECT report_parameter_id,report_id,sequence_num,(RANK ()
                                                                     OVER (
                                                                        PARTITION BY REPORT_ID
                                                                        ORDER BY
                                                                           SEQUENCE_NUM ASC, REPORT_PARAMETER_ID) - 1)
                                                                        AS rec_rank FROM report_parameter;                                                                        
BEGIN

FOR C1_R IN C1 LOOP

UPDATE report_parameter
SET sequence_num = c1_r.rec_rank
WHERE report_parameter_id = c1_r.report_parameter_id;

END LOOP;

END;
/
