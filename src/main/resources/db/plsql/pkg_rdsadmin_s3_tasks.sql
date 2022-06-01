CREATE OR REPLACE PACKAGE pkg_rdsadmin_s3_tasks IS
/*------------------------------------------------------------------------------
-- Purpose: To move the files from AWS RDS directory to S3 bucket
--
-- Called by: 
--
--------------------------------------------------------------------------------
-- Gorantla    03/14/2019  initial
--
------------------------------------------------------------------------------*/

  FUNCTION rds_version RETURN VARCHAR2;


  FUNCTION download_from_s3 (p_bucket_name IN varchar2,
                              p_directory_name IN varchar2
  ) RETURN VARCHAR2;

  FUNCTION download_from_s3 (p_bucket_name IN varchar2,
                              p_directory_name IN varchar2,
                              p_s3_prefix IN varchar2
  ) RETURN VARCHAR2;

  FUNCTION upload_to_s3 (p_bucket_name IN varchar2,
                              p_directory_name IN varchar2,
                              p_s3_prefix IN varchar2,
                              p_prefix IN varchar2
  ) RETURN VARCHAR2;

  FUNCTION upload_to_s3 (p_bucket_name IN varchar2,
                              p_directory_name IN varchar2,
                              p_s3_prefix IN varchar2
  ) RETURN VARCHAR2;

  FUNCTION upload_to_s3 (p_bucket_name IN varchar2,
                              p_directory_name IN varchar2
  ) RETURN VARCHAR2;

END pkg_rdsadmin_s3_tasks;
/
CREATE OR REPLACE PACKAGE BODY pkg_rdsadmin_s3_tasks IS
/*------------------------------------------------------------------------------
-- Purpose: To move the files from AWS RDS directory to S3 bucket
--
-- Called by: 
--
--------------------------------------------------------------------------------
-- Gorantla    03/14/2019  initial
--
------------------------------------------------------------------------------*/

  GV_RDS_VERSION                 CONSTANT VARCHAR2(10) := '1.0';

  FUNCTION RDS_VERSION RETURN VARCHAR2
  IS
  BEGIN
    RETURN GV_RDS_VERSION;
  END RDS_VERSION;

  FUNCTION GET_CURRENT_TIME_MS
    RETURN NUMBER
  IS
    V_TS NUMBER;
  BEGIN
    
    SELECT EXTRACT(DAY FROM(SYS_EXTRACT_UTC(SYSTIMESTAMP) - TO_TIMESTAMP('1970-01-01', 'YYYY-MM-DD'))) * 86400000
      + TO_NUMBER(TO_CHAR(SYS_EXTRACT_UTC(SYSTIMESTAMP), 'SSSSSFF3')) INTO V_TS FROM DUAL;
    RETURN V_TS;
  END GET_CURRENT_TIME_MS;
  
  FUNCTION GET_CURRENT_SID
    RETURN NUMBER
  IS
    V_SID_ID NUMBER;
  BEGIN
    SELECT SYS_CONTEXT('USERENV','SID') INTO V_SID_ID FROM dual;
    
    RETURN V_SID_ID;
  END GET_CURRENT_SID;

  PROCEDURE ASSERT_NOT_NULL (P_VALUE IN VARCHAR2, P_NAME IN VARCHAR2)
  IS
  BEGIN
    IF (P_VALUE IS NULL) THEN
      RAISE_APPLICATION_ERROR(-20001,'Error: NULL value passed in for '||P_NAME||'.');
    END IF;
  END ASSERT_NOT_NULL;

  
  
  
  FUNCTION DOWNLOAD_FROM_S3
                            (P_BUCKET_NAME IN VARCHAR2,
                            P_DIRECTORY_NAME IN VARCHAR2,
                            P_S3_PREFIX IN VARCHAR2)
    RETURN VARCHAR2
  IS
    V_NEXT_ID VARCHAR2(4000);
    V_JSON VARCHAR2(4000);
    V_SANITIZED_JSON VARCHAR2(4000);
    V_DYNAMIC_SQL VARCHAR2(4000);
    V_DIRPATH ALL_DIRECTORIES.DIRECTORY_PATH%TYPE;
    V_MARKER_FILE UTL_FILE.FILE_TYPE;
    V_HAS_PRIV NUMBER;
    NO_PRIV EXCEPTION;
  BEGIN
    ASSERT_NOT_NULL(P_BUCKET_NAME, 'p_bucket_name');
    ASSERT_NOT_NULL(P_DIRECTORY_NAME, 'p_directory_name');

    BEGIN
      SELECT DIRECTORY_PATH INTO V_DIRPATH FROM ALL_DIRECTORIES WHERE UPPER(DIRECTORY_NAME) = UPPER(P_DIRECTORY_NAME);

      SELECT COUNT(*) INTO V_HAS_PRIV FROM ALL_TAB_PRIVS WHERE
          TABLE_NAME = P_DIRECTORY_NAME AND PRIVILEGE = 'WRITE' AND GRANTEE IN (SELECT USER FROM DUAL);
      IF (V_HAS_PRIV = 0) THEN
        RAISE NO_PRIV;
      END IF;

      V_NEXT_ID := GET_CURRENT_TIME_MS || '-' || GET_CURRENT_SID;
      V_JSON := '{"id":"' || V_NEXT_ID || '", "taskClass":"DATA_INGESTION", "taskType": "DOWNLOAD_FROM_S3", ' ||
       '"params":{"bucketName": "' || P_BUCKET_NAME || '", "prefix": "' || P_S3_PREFIX || '", "s3DownloadLocation": "' || V_DIRPATH || '"}}';

      V_MARKER_FILE := UTL_FILE.FOPEN('RDS$DB_TASKS', V_NEXT_ID, 'w');
      UTL_FILE.PUT(V_MARKER_FILE, V_JSON);
      UTL_FILE.FCLOSE(V_MARKER_FILE);
      RETURN V_NEXT_ID;

    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20002, 'Unable to find Oracle directory: ' || P_DIRECTORY_NAME);
      WHEN NO_PRIV THEN
        RAISE_APPLICATION_ERROR(-20003, 'Current user does not have WRITE privilege for directory ' || P_DIRECTORY_NAME);
      WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20001, 'Internal error: Failed to kick off execution of the task');
    END;
  END DOWNLOAD_FROM_S3;

  FUNCTION DOWNLOAD_FROM_S3
                            (P_BUCKET_NAME IN VARCHAR2,
                            P_DIRECTORY_NAME IN VARCHAR2)
    RETURN VARCHAR2
  IS
  BEGIN
    RETURN DOWNLOAD_FROM_S3(P_BUCKET_NAME, P_DIRECTORY_NAME, '');
  END DOWNLOAD_FROM_S3;

  
  
  
  FUNCTION UPLOAD_TO_S3
                            (P_BUCKET_NAME IN VARCHAR2,
                            P_DIRECTORY_NAME IN VARCHAR2,
                            P_S3_PREFIX IN VARCHAR2,
                            P_PREFIX IN VARCHAR2)
    RETURN VARCHAR2
  IS
    V_NEXT_ID VARCHAR2(4000);
    V_JSON VARCHAR2(4000);
    V_SANITIZED_JSON VARCHAR2(4000);
    V_DYNAMIC_SQL VARCHAR2(4000);
    V_DIRPATH ALL_DIRECTORIES.DIRECTORY_PATH%TYPE;
    V_MARKER_FILE UTL_FILE.FILE_TYPE;
    V_HAS_PRIV NUMBER;
    NO_PRIV EXCEPTION;
  BEGIN
    ASSERT_NOT_NULL(P_BUCKET_NAME, 'p_bucket_name');
    ASSERT_NOT_NULL(P_DIRECTORY_NAME, 'p_directory_name');

    BEGIN
      SELECT DIRECTORY_PATH INTO V_DIRPATH FROM ALL_DIRECTORIES WHERE UPPER(DIRECTORY_NAME) = UPPER(P_DIRECTORY_NAME);

      SELECT COUNT(*) INTO V_HAS_PRIV FROM ALL_TAB_PRIVS WHERE
          TABLE_NAME = P_DIRECTORY_NAME AND PRIVILEGE = 'READ' AND GRANTEE IN (SELECT USER FROM DUAL);
      IF (V_HAS_PRIV = 0) THEN
        RAISE NO_PRIV;
      END IF;

      V_NEXT_ID := GET_CURRENT_TIME_MS || '-' || GET_CURRENT_SID;
      V_JSON := '{"id":"' || V_NEXT_ID || '", "taskClass":"DATA_INGESTION", "taskType": "UPLOAD_TO_S3", ' ||
        '"params":{"bucketName": "' || P_BUCKET_NAME || '", "s3DirectoryPrefix": "' || P_S3_PREFIX ||
        '", "prefix": "' || P_PREFIX || '", "s3UploadLocation": "' || V_DIRPATH || '"}}';

      V_MARKER_FILE := UTL_FILE.FOPEN('RDS$DB_TASKS', V_NEXT_ID, 'w');
      UTL_FILE.PUT(V_MARKER_FILE, V_JSON);
      UTL_FILE.FCLOSE(V_MARKER_FILE);
      RETURN V_NEXT_ID;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20002, 'Unable to find Oracle directory: ' || P_DIRECTORY_NAME);
      WHEN NO_PRIV THEN
        RAISE_APPLICATION_ERROR(-20003, 'Current user does not have READ privilege for directory ' || P_DIRECTORY_NAME);
      WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20001, 'Internal error: Failed to kick off execution of the task');
    END;
  END UPLOAD_TO_S3;

  FUNCTION UPLOAD_TO_S3
                            (P_BUCKET_NAME IN VARCHAR2,
                            P_DIRECTORY_NAME IN VARCHAR2,
                            P_S3_PREFIX IN VARCHAR2)
    RETURN VARCHAR2
  IS
  BEGIN
    RETURN UPLOAD_TO_S3(P_BUCKET_NAME, P_DIRECTORY_NAME, P_S3_PREFIX, '');
  END UPLOAD_TO_S3;

  FUNCTION UPLOAD_TO_S3
                            (P_BUCKET_NAME IN VARCHAR2,
                            P_DIRECTORY_NAME IN VARCHAR2)
    RETURN VARCHAR2
  IS
  BEGIN
    RETURN UPLOAD_TO_S3(P_BUCKET_NAME, P_DIRECTORY_NAME, '', '');
  END UPLOAD_TO_S3;

END pkg_rdsadmin_s3_tasks;
/