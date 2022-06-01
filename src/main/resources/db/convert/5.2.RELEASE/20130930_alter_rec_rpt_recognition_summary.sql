ALTER TABLE rpt_recognition_summary 
ADD ( 
  	SUM_TYPE                      VARCHAR2(49),
  	KEY_FIELD_HASH                RAW(16)
    )
/
COMMENT ON COLUMN RPT_RECOGNITION_SUMMARY.KEY_FIELD_HASH    IS 'An MD5 hash of key fields PROMOTION_ID, AWARD_TYPE, BEHAVIOR, PAX_STATUS, JOB_POSITION, DEPARTMENT used to speed index efficiency.'
/
COMMENT ON COLUMN RPT_RECOGNITION_SUMMARY.RECORD_TYPE       IS 'Team/node summary with hierarchy level prepended (field retained for backwards compatability).'
/
DROP INDEX RPT_RECOGNITION_SUM_IDX2
/
CREATE UNIQUE INDEX RPT_RECOGNITION_SUM_IDX2  ON rpt_recognition_summary (detail_node_id, giver_recvr_type, sum_type, date_approved,promotion_id, award_type, behavior, pax_status, job_position, department)
/