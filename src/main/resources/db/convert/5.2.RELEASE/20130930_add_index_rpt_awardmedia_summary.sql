DROP INDEX RPT_AWARDMEDIA_SUM_IDX2
/
CREATE INDEX RPT_AWARDMEDIA_SUM_IDX2 ON rpt_awardmedia_summary (detail_node_id, record_type, media_type, promotion_id, pax_status, job_position, department)
/