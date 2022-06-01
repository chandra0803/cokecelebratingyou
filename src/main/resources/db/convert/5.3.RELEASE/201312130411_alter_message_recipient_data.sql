BEGIN
  
  /* Alter mailing_recipient_data.data_value data type from VARCHAR2 to CLOB */
  
  EXECUTE IMMEDIATE 'ALTER TABLE mailing_recipient_data ADD (data_value1 CLOB)';
  EXECUTE IMMEDIATE 'UPDATE mailing_recipient_data
                        SET data_value1 = data_value';
  EXECUTE IMMEDIATE 'ALTER TABLE mailing_recipient_data DROP COLUMN data_value';  
  EXECUTE IMMEDIATE 'ALTER TABLE mailing_recipient_data RENAME COLUMN data_value1 TO data_value';
END;
/