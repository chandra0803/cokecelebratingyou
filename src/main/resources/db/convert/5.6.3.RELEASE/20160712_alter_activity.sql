ALTER TABLE ACTIVITY
ADD APPROVAL_ROUND NUMBER(10)
/
ALTER TABLE ACTIVITY
ADD COUNTRY_ID NUMBER(12)
/
ALTER TABLE ACTIVITY
ADD CASH_AWARD_QTY NUMBER(18,4)
/
ALTER TABLE activity ADD CONSTRAINT activity_country_id_fk
FOREIGN KEY (country_id) REFERENCES country(country_id)
/
CREATE INDEX ACTIVITY_COUNTRY_FK_idx ON ACTIVITY (COUNTRY_ID)
/
COMMENT ON COLUMN activity.approval_round IS 'Approval round'
/