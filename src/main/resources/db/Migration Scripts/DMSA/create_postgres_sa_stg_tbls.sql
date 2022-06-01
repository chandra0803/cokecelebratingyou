-- Service awards db
CREATE TABLE "DMSADBNP".stg_sa_persons as select * from persons where 1 = 2; 
ALTER TABLE "DMSADBNP".stg_sa_programs RENAME programid TO program_uuid;
CREATE TABLE "DMSADBNP".stg_sa_milestones as select * from milestones where 1 = 2; 
CREATE TABLE "DMSADBNP".stg_sa_recipients as select * from recipients where 1 = 2; 
CREATE TABLE "DMSADBNP".stg_sa_celebrationspersons as select * from celebrationspersons where 1 = 2; 
CREATE TABLE "DMSADBNP".stg_sa_programs
(
    id integer,
    companyid uuid,
    program_uuid uuid,
    programname jsonb,
    programstartdate timestamp with time zone,
    programenddate timestamp with time zone,
    programstate character varying(25) COLLATE pg_catalog."default",
    awardtype character varying(25) COLLATE pg_catalog."default",
    isbillprofileactive boolean,
    billprofiles jsonb,
    giftcodeawardid uuid,
    awardintervaltype character varying(25) COLLATE pg_catalog."default",
    awardintervalfield character varying(50) COLLATE pg_catalog."default",
    awardintervalnumber integer,
    awardintervalpart character varying(25) COLLATE pg_catalog."default",
    recipientemail jsonb,
    giftcodereminderemail jsonb,
    gcrememailsenddaysafter integer,
    giftcodeexpirationemail jsonb,
    gcexpemailsenddaysprior integer,
    contributionemail jsonb,
    createdat timestamp with time zone,
    updatedat timestamp with time zone,
    programsetup jsonb,
    gcrememailfrequency character varying(25) COLLATE pg_catalog."default",
    gcrememailsendtime character varying(25) COLLATE pg_catalog."default",
    gcrememailsendstartdate timestamp with time zone,
    gcrememailsendenddate timestamp with time zone,
    celebrationsettings jsonb,
    manageremail jsonb,
    branding jsonb,
    hierarchyid uuid,
    emaildisplayname jsonb,
    signatureimageurl character varying(500) COLLATE pg_catalog."default",
    emailheaderimageurl character varying(1000) COLLATE pg_catalog."default"
);