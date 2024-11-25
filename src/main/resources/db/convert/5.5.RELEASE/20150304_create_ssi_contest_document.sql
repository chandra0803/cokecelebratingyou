CREATE SEQUENCE ssi_contest_document_pk_sq INCREMENT BY 1 START WITH 5000
/
CREATE TABLE ssi_contest_document
(
  ssi_contest_document_id	    NUMBER(18) NOT NULL,
  ssi_contest_id	    		NUMBER(18) NOT NULL,
  attachment_name 				VARCHAR2(100),
  attachment_display_name 		VARCHAR2(100),
  locale        				VARCHAR2(400),
  created_by            		NUMBER(18) NOT NULL,  
  date_created          		DATE NOT NULL,
  modified_by					NUMBER(18),
  date_modified					DATE, 
  version               		NUMBER(18) NOT NULL,
  CONSTRAINT ssi_contest_document_pk PRIMARY KEY (ssi_contest_document_id)
)
/
ALTER TABLE ssi_contest_document ADD CONSTRAINT ssi_contest_doc_contest_fk
  FOREIGN KEY (ssi_contest_id) REFERENCES ssi_contest (ssi_contest_id)
/