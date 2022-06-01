CREATE SEQUENCE rp_work_happier_dtl_id_pk_sq INCREMENT BY 1 START WITH 1000
/
CREATE TABLE rpt_work_happier_detail
(rp_work_happier_dtl_id NUMBER(18)      NOT NULL,
 user_id                NUMBER(18),
 node_id                NUMBER(18),
 node_name              VARCHAR2(2000),
 score                  NUMBER(10),
 month_created          DATE            NOT NULL,
 date_created           DATE            NOT NULL,
 created_by             VARCHAR2(30)    NOT NULL,
 date_modified          DATE,
 modified_by            VARCHAR2(30),
 CONSTRAINT rp_work_happier_dtl_id_pk PRIMARY KEY (rp_work_happier_dtl_id)
 )
/