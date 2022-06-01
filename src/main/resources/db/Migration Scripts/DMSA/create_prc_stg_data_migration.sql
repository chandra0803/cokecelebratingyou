CREATE OR REPLACE PROCEDURE prc_stg_data_migration 
IS 
   /*********************************************************************************
   -- Purpose:
   -- This procedure is to populate the data from client to staged schema AEIW which is the source for informatica
      to move data from on-prem database to AWS staged schema DMSADBNP
   -- Person           Date        Comments
   -- ---------       ----------   -----------------------------------------------------
   -- Gorantla        03/04/2019   Initial Version
   *********************************************************************************/ 

   c_process_name                CONSTANT execution_log.process_name%TYPE  := 'PRC_STG_DATA_MIGRATION' ;
   c_release_level               CONSTANT execution_log.release_level%TYPE := 1 ;
   v_stage                                execution_log.text_line%TYPE;
   
BEGIN        

INSERT INTO stg_cmx_key
  SELECT id,
         name,
         --value,
         projectId,
         tags,
         createdAt,
         updatedAt,
         doNotTranslate,
         metadata
    FROM stg_cmx_key1;
    
INSERT INTO stg_gc_giftcodes
     SELECT companyid,       
            awardid,         
            giftcodeid,      
            requestid,       
            countrycode,     
            batchid,         
            programnumber,   
            levelname,       
            giftcode,        
            referencenumber, 
            expiredat,       
            description,     
           -- user_id       
           -- claim_id      
            firstname,       
            lastname,        
            locale,          
            requestedby,     
            status,          
            billprofiles,    
            createdat,       
            updatedat,       
            orgunitname,     
            statusupdatedat, 
            returnedat  
  FROM stg_gc_giftcodes1;  
  
insert into stg_sa_persons
SELECT id,           
    --user_id,       
    username,        
    firstname,
    lastname,        
    emailaddress,    
    localecode,      
    countryiso3code, 
    companyid,       
    departmentname,  
    orgunitname,     
    characteristics, 
    createdat,       
    updatedat,       
    rosterpersonid,  
    avatarurl,       
    acceptedtcat,
    canauthenticate    
  FROM stg_sa_persons1;
  
insert into stg_sa_programs  
SELECT id,
    companyid,
--    programid,
--    is_include_purl, 
    program_uuid,
    programname,
    programstartdate,
    programenddate,
    programstate,
    awardtype,
    isbillprofileactive,
    billprofiles,
    giftcodeawardid,
    awardintervaltype,
    awardintervalfield,
    awardintervalnumber,
    awardintervalpart,
    recipientemail,
    giftcodereminderemail,
    gcrememailsenddaysafter,
    giftcodeexpirationemail,
    gcexpemailsenddaysprior,
    contributionemail,
    createdat,
    updatedat,
    programsetup,
    gcrememailfrequency,
    gcrememailsendtime,
    gcrememailsendstartdate,
    gcrememailsendenddate,
    celebrationsettings,
    manageremail,
    branding,
    hierarchyid,
    emaildisplayname,
    signatureimageurl
  FROM stg_sa_programs1;
  
insert into stg_sa_recipients
     SELECT id,
--    user_type,
--    claim_id,
--    user_id,
--    purl_recipient_id,
    companyid,
    recipientid,
    recipientstate,
    recipientstatus,
    programid,
    milestonecode,
    awarddate,
    awardexpirationdate,
    giftcodeid,
    giftcodereference,
    createdby,
    createdreference,
    createdat,
    updatedat,
    awarddatetimezone,
    celebrationid,
    celebrationsettings,
    participantpersonid,
    manager1personid,
    manager2personid,
    celebrationyear,
    emailsettings,
    awardeventsentat
  FROM stg_sa_recipients1; 

EXCEPTION 
  WHEN OTHERS THEN
       prc_execution_log_entry (c_process_name,c_release_level,
                                'Error',v_stage || SQLCODE || ':' || SQLERRM,NULL);
      ROLLBACK;   
END;
/