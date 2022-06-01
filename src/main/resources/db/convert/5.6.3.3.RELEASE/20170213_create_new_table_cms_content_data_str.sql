CREATE TABLE cms_content_data_str
(
  CONTENT_ID    NUMBER(19)       NOT NULL,
  KEY           VARCHAR2(40)     NOT NULL,
  VALUE_STR     VARCHAR2(300 CHAR)
)
/
CREATE UNIQUE INDEX cms_content_data_str_PK
   ON cms_content_data_str
   (content_id,
    key
   )
/
CREATE INDEX cms_content_data_str_IDX2
   ON cms_content_data_str
   (content_id,
    key,
    value_str
   )
/
CREATE INDEX cms_content_data_str_IDX3
   ON cms_content_data_str
   (key,
    content_id,
    value_str
   )
/
ALTER TABLE cms_content_data_str ADD
( CONSTRAINT cms_content_data_str_PK 
  PRIMARY KEY (content_id, key)
  USING INDEX cms_content_data_str_PK
)
/
ALTER TABLE cms_content_data_str ADD
( CONSTRAINT cms_content_data_str_FK 
  FOREIGN KEY (content_id, key)
  REFERENCES cms_content_data (content_id, key)
  ON DELETE CASCADE
)
/
-- initialize the CMS content data string table
MERGE INTO cms_content_data_str d
USING (  -- convert source clob to string
         SELECT cd.content_id,
                cd.key,
                SUBSTR(DBMS_LOB.SUBSTR (cd.value, 300, 1), 1, 300) AS value_str
           FROM cms_content_data cd
      ) s
   ON (   d.content_id = s.content_id
      AND d.key = s.key
      )
 WHEN MATCHED THEN
   UPDATE
      SET value_str = s.value_str
    WHERE d.value_str != s.value_str
 WHEN NOT MATCHED THEN
   INSERT
   (content_id,
    key,
    value_str
   )
   VALUES
   (s.content_id,
    s.key,
    s.value_str
   )
/
CREATE OR REPLACE TRIGGER cms_content_data_aiu
   AFTER INSERT OR UPDATE
   ON cms_content_data
   REFERENCING NEW AS NEW OLD AS OLD
   FOR EACH ROW
BEGIN
   MERGE INTO cms_content_data_str d
   USING (  -- convert source clob to string
            SELECT :NEW.content_id AS content_id,
                   :NEW.key AS key,
                   SUBSTR(DBMS_LOB.SUBSTR (:NEW.value, 300, 1), 1, 300) AS value_str
              FROM dual
         ) s
      ON (   d.content_id = s.content_id
         AND d.key = s.key
         )
    WHEN MATCHED THEN
      UPDATE
         SET value_str = s.value_str
       WHERE d.value_str != s.value_str
    WHEN NOT MATCHED THEN
      INSERT
      (content_id,
       key,
       value_str
      )
      VALUES
      (s.content_id,
       s.key,
       s.value_str
      );

END ;
/
