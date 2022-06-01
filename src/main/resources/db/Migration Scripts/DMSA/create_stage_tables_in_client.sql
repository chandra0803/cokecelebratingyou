-- SEQUENCES
CREATE SEQUENCE stg_sa_programs_id_seq;
CREATE SEQUENCE milestones_id_seq;
CREATE SEQUENCE recipients_id_seq;

--CMX
CREATE TABLE stg_cmx_key1
(
    id VARCHAR2(50) NOT NULL,
    name VARCHAR2(255) NOT NULL,
    value VARCHAR2(4000),   --EXTRA COLUMN to insurt values into 'string' table
    projectId VARCHAR2(50),
    tags VARCHAR2(50),
    createdAt DATE NOT NULL,
    updatedAt DATE NOT NULL,
    doNotTranslate VARCHAR2(50) DEFAULT 'false',
    metadata VARCHAR2(1000) DEFAULT '[]'
);

CREATE TABLE stg_gc_giftcodes1
(
    companyid         VARCHAR2 (50) NOT NULL,
    awardid           VARCHAR2 (50) NOT NULL,
    giftcodeid        VARCHAR2 (50) NOT NULL,
    requestid         VARCHAR2 (50) NOT NULL,
    countrycode       VARCHAR2 (25) NOT NULL,
    batchid           NUMBER (18) NOT NULL,
    programnumber     VARCHAR2 (10) NOT NULL,
    levelname         VARCHAR2 (25) NOT NULL,
    giftcode          VARCHAR2 (25) NOT NULL,
    referencenumber   VARCHAR2 (25) NOT NULL,
    expiredat         DATE,
    description       VARCHAR2 (50),
    user_id           NUMBER (18),  -- EXTRA COLUMN
    claim_id          NUMBER (18),  -- EXTRA COLUMN
    firstname         VARCHAR2 (50),
    lastname          VARCHAR2 (50),
    locale            VARCHAR2 (25),
    requestedby       VARCHAR2 (25) NOT NULL,
    status            VARCHAR2 (25) NOT NULL,
    billprofiles      VARCHAR2 (3000),
    createdat         DATE NOT NULL,
    updatedat         DATE NOT NULL,
    orgunitname       VARCHAR2 (25),
    statusupdatedat   DATE,
    returnedat        DATE
);

CREATE TABLE stg_sa_persons1
(
    id                VARCHAR2 (50) NOT NULL,
    purl_contributor_id NUMBER(18),
    user_id           NUMBER(18), --EXTRA COLUMN to match DM user_id
    username          VARCHAR2 (50),
    firstname         VARCHAR2 (50),
    lastname          VARCHAR2 (50),
    emailaddress      VARCHAR2 (100),
    localecode        VARCHAR2 (5),
    countryiso3code   VARCHAR2 (50),
    companyid         VARCHAR2 (50) NOT NULL,
    departmentname    VARCHAR2 (100),
    orgunitname       VARCHAR2 (100),
    characteristics   VARCHAR2 (3000) DEFAULT '{}',
    createdat         DATE NOT NULL,
    updatedat         DATE NOT NULL,
    rosterpersonid    VARCHAR2 (255),
    avatarurl         VARCHAR2 (255),
    acceptedtcat      DATE,
    canauthenticate    VARCHAR2 (10)
);

CREATE TABLE stg_sa_programs1
(
    id                                     NUMBER(18),
    companyid                              VARCHAR2 (50) NOT NULL,
    programid                              NUMBER(18) NOT NULL,  --EXTRA COLUMN promotion_id 

from promotion_table to track DM
    is_include_purl                        NUMBER(1),  --EXTRA COLUMN to know purl or 

celebration 
    program_uuid                           VARCHAR2 (50) NOT NULL,
    programname                            VARCHAR2 (1000) NOT NULL,
    programstartdate                       DATE NOT NULL,
    programenddate                         DATE,
    programstate                           VARCHAR2 (25) NOT NULL,
    awardtype                              VARCHAR2 (25) NOT NULL,
    isbillprofileactive                    VARCHAR2 (10) NOT NULL,
    billprofiles                           VARCHAR2 (3000),
    giftcodeawardid                        VARCHAR2 (50),
    awardintervaltype                      VARCHAR2 (25) NOT NULL,
    awardintervalfield                     VARCHAR2 (50),
    awardintervalnumber                    NUMBER (18),
    awardintervalpart                      VARCHAR2 (25),
    recipientemail                         VARCHAR2 (1000),
    giftcodereminderemail                  VARCHAR2 (1000),
    gcrememailsenddaysafter                NUMBER (18), 
    giftcodeexpirationemail                VARCHAR2 (1000),
    gcexpemailsenddaysprior                NUMBER (18),
    contributionemail                      VARCHAR2 (3000),
    createdat                              DATE NOT NULL,
    updatedat                              DATE NOT NULL,
    programsetup                           VARCHAR2(1000),
    gcrememailfrequency                    VARCHAR2 (25),
    gcrememailsendtime                     VARCHAR2 (25),
    gcrememailsendstartdate                DATE,
    gcrememailsendenddate                  DATE,
    celebrationsettings                    CLOB,
    manageremail                           VARCHAR2 (1000),
    branding                               VARCHAR2 (1000),
    hierarchyid                            VARCHAR2 (50),
    emaildisplayname                       VARCHAR2 (1000),
    signatureimageurl                      VARCHAR2 (500)
);

CREATE TABLE STG_SA_RECIPIENTS1
(
  ID                   NUMBER(18),
  USER_TYPE            VARCHAR2(50 CHAR),
  CLAIM_ID             NUMBER(18),
  USER_ID              NUMBER(18),
  PURL_RECIPIENT_ID    NUMBER(18),
  CELEBRATION_MANAGER_MESSAGE_ID NUMBER(18),
  COMPANYID            VARCHAR2(50 CHAR),
  RECIPIENTID          VARCHAR2(50 CHAR),
  RECIPIENTSTATE       VARCHAR2(25 CHAR),
  RECIPIENTSTATUS      VARCHAR2(25 CHAR),
  PROGRAMID            VARCHAR2(50 CHAR),
  MILESTONECODE        VARCHAR2(25 CHAR),
  AWARDDATE            DATE,
  AWARDEXPIRATIONDATE  DATE,
  GIFTCODEID           VARCHAR2(50 CHAR),
  GIFTCODEREFERENCE    VARCHAR2(10 CHAR),
  CREATEDBY            VARCHAR2(25 CHAR),
  CREATEDREFERENCE     VARCHAR2(64 CHAR),
  CREATEDAT            DATE,
  UPDATEDAT            DATE,
  AWARDDATETIMEZONE    VARCHAR2(50 CHAR)        DEFAULT 'America/Chicago',
  CELEBRATIONID        VARCHAR2(50 CHAR),
  CELEBRATIONSETTINGS  CLOB,
  PARTICIPANTPERSONID  VARCHAR2(50 CHAR),
  MANAGER1PERSONID     VARCHAR2(50 CHAR),
  MANAGER2PERSONID     VARCHAR2(50 CHAR),
  CELEBRATIONYEAR      NUMBER(18),
  EMAILSETTINGS        VARCHAR2(3000 CHAR),
  AWARDEVENTSENTAT     DATE
);

-- Channels
CREATE TABLE stg_chan_channels
(
    id VARCHAR2(50) NOT NULL,
    isDeleted VARCHAR2(10) DEFAULT 'false',
    createdAt DATE NOT NULL,
    updatedAt DATE NOT NULL,
    companyId VARCHAR2(50) NOT NULL
);

CREATE TABLE stg_chan_messages
(
    id                     VARCHAR2 (50) NOT NULL,
    channelId              VARCHAR2 (50) NOT NULL,
    contributorName        VARCHAR2 (100) NOT NULL,
    contributorReference   VARCHAR2 (64) NOT NULL,
    text                   CLOB,
    effects                VARCHAR2 (100),
    replyToId              VARCHAR2 (50),
    threadId               VARCHAR2 (50) NOT NULL,
    isDeleted              VARCHAR2 (10) DEFAULT 'false',
    createdAt              DATE NOT NULL,
    updatedAt              DATE NOT NULL,
    companyId              VARCHAR2 (50) NOT NULL,
    isFlagged              VARCHAR2 (10) DEFAULT 'false',
    attachments            VARCHAR2 (1000) DEFAULT '[]',
    isPrivate              VARCHAR2 (10) DEFAULT 'false',
    contributorStatus      VARCHAR2 (15) DEFAULT 'peer'
);

--CMX
CREATE TABLE stg_cmx_key
(
    id VARCHAR2(50) NOT NULL,
    name VARCHAR2(255) NOT NULL,
    --value VARCHAR2(4000),   --EXTRA COLUMN to insurt values into 'string' table
    projectId VARCHAR2(50),
    tags VARCHAR2(50),
    createdAt DATE NOT NULL,
    updatedAt DATE NOT NULL,
    doNotTranslate VARCHAR2(50) DEFAULT 'false',
    metadata VARCHAR2(1000) DEFAULT '[]'
);

CREATE TABLE stg_cmx_string
(
    keyId       VARCHAR2 (50) NOT NULL,
    locale      VARCHAR2 (5) NOT NULL,
    companyId   VARCHAR2 (50) NOT NULL,
    VALUE       VARCHAR2 (4000),
    createdAt   DATE NOT NULL,
    updatedAt   DATE NOT NULL
);

-- GIFT CODE
CREATE TABLE stg_gc_awards
(
    companyid VARCHAR2(50) NOT NULL,
    awardid VARCHAR2(50) NOT NULL,
    programid VARCHAR2(50) NOT NULL,
    createdat DATE NOT NULL,
    updatedat DATE NOT NULL,
    browsecode VARCHAR2 (25),
    billprofiles VARCHAR2 (3000)
);

CREATE TABLE stg_gc_countries
(
    companyid VARCHAR2(50) NOT NULL,
    awardid VARCHAR2(50) NOT NULL,
    countrycode VARCHAR2(50) NOT NULL,
    programnumber VARCHAR2(50) NOT NULL,
    createdat DATE NOT NULL,
    updatedat DATE NOT NULL,
    CONSTRAINT countries_pkey PRIMARY KEY (awardid, countrycode)
);

CREATE TABLE stg_gc_giftcodes
(
    companyid         VARCHAR2 (50) NOT NULL,
    awardid           VARCHAR2 (50) NOT NULL,
    giftcodeid        VARCHAR2 (50) NOT NULL,
    requestid         VARCHAR2 (50) NOT NULL,
    countrycode       VARCHAR2 (25) NOT NULL,
    batchid           NUMBER (18) NOT NULL,
    programnumber     VARCHAR2 (10) NOT NULL,
    levelname         VARCHAR2 (25) NOT NULL,
    giftcode          VARCHAR2 (25) NOT NULL,
    referencenumber   VARCHAR2 (25) NOT NULL,
    expiredat         DATE,
    description       VARCHAR2 (50),
   -- user_id           NUMBER (18),  -- EXTRA COLUMN
   -- claim_id          NUMBER (18),  -- EXTRA COLUMN
    firstname         VARCHAR2 (50),
    lastname          VARCHAR2 (50),
    locale            VARCHAR2 (25),
    requestedby       VARCHAR2 (25) NOT NULL,
    status            VARCHAR2 (25) NOT NULL,
    billprofiles      VARCHAR2 (3000),
    createdat         DATE NOT NULL,
    updatedat         DATE NOT NULL,
    orgunitname       VARCHAR2 (25),
    statusupdatedat   DATE,
    returnedat        DATE
);

CREATE TABLE stg_gc_levels
(
    id VARCHAR2(50) NOT NULL,
    companyid VARCHAR2(50) NOT NULL,
    awardid VARCHAR2(50) NOT NULL,
    levelcode VARCHAR2(25),
    levelname VARCHAR2(1000),
    defaultitem VARCHAR2(1000),
    createdat DATE NOT NULL,
    updatedat DATE NOT NULL
);

-- SERVICE AWARDS
CREATE TABLE stg_sa_persons
(
    id                VARCHAR2 (50) NOT NULL,
    --user_id           NUMBER(18), --EXTRA COLUMN to match DM user_id
    username          VARCHAR2 (50),
    firstname         VARCHAR2 (50),
    lastname          VARCHAR2 (50),
    emailaddress      VARCHAR2 (100),
    localecode        VARCHAR2 (5),
    countryiso3code   VARCHAR2 (50),
    companyid         VARCHAR2 (50) NOT NULL,
    departmentname    VARCHAR2 (100),
    orgunitname       VARCHAR2 (100),
    characteristics   VARCHAR2 (3000) DEFAULT '{}',
    createdat         DATE NOT NULL,
    updatedat         DATE NOT NULL,
    rosterpersonid    VARCHAR2 (255),
    avatarurl         VARCHAR2 (255),
    acceptedtcat      DATE,
    canauthenticate   VARCHAR2 (10)
);

CREATE TABLE STG_SA_PERSONS_TYPE
(
  ID                 VARCHAR2(50 CHAR),
  USER_ID            NUMBER(18),
  CLAIM_ID           NUMBER(18),
  PURL_RECIPIENT_ID  NUMBER(18),
  CELEBRATION_MANAGER_MESSAGE_ID NUMBER(18),
  STATE              VARCHAR2(50 CHAR),
  AWARD_DATE         DATE,
  OM_LEVEL_NAME      VARCHAR2(255 CHAR),
  PROGRAMID          NUMBER(18),
  USER_TYPE          VARCHAR2(50 CHAR),
  USERNAME           VARCHAR2(50 CHAR),
  FIRSTNAME          VARCHAR2(50 CHAR),
  LASTNAME           VARCHAR2(50 CHAR),
  EMAILADDRESS       VARCHAR2(100 CHAR),
  LOCALECODE         VARCHAR2(5 CHAR),
  COUNTRYISO3CODE    VARCHAR2(50 CHAR),
  COMPANYID          VARCHAR2(50 CHAR),
  DEPARTMENTNAME     VARCHAR2(100 CHAR),
  ORGUNITNAME        VARCHAR2(100 CHAR),
  CHARACTERISTICS    VARCHAR2(3000 CHAR)        DEFAULT '{}',
  CREATEDAT          DATE,
  UPDATEDAT          DATE,
  ROSTERPERSONID     VARCHAR2(255 CHAR),
  AVATARURL          VARCHAR2(255 CHAR),
  ACCEPTEDTCAT       DATE,
  anniversary_num_years NUMBER(5)
);

CREATE TABLE stg_sa_programs
(
    id                                     NUMBER(18),
    companyid                              VARCHAR2 (50) NOT NULL,
--    programid                              NUMBER(18) NOT NULL,  --EXTRA COLUMN promotion_id 

from promotion_table to track DM
--    is_include_purl                        NUMBER(1),  --EXTRA COLUMN to know purl or 

celebration 
    program_uuid                           VARCHAR2 (50) NOT NULL,
    programname                            VARCHAR2 (1000) NOT NULL,
    programstartdate                       DATE NOT NULL,
    programenddate                         DATE,
    programstate                           VARCHAR2 (25) NOT NULL,
    awardtype                              VARCHAR2 (25) NOT NULL,
    isbillprofileactive                    VARCHAR2 (10) NOT NULL,
    billprofiles                           VARCHAR2 (3000),
    giftcodeawardid                        VARCHAR2 (50),
    awardintervaltype                      VARCHAR2 (25) NOT NULL,
    awardintervalfield                     VARCHAR2 (50),
    awardintervalnumber                    NUMBER (18),
    awardintervalpart                      VARCHAR2 (25),
    recipientemail                         VARCHAR2 (1000),
    giftcodereminderemail                  VARCHAR2 (1000),
    gcrememailsenddaysafter                NUMBER (18),
    giftcodeexpirationemail                VARCHAR2 (1000),
    gcexpemailsenddaysprior                NUMBER (18),
    contributionemail                      VARCHAR2 (3000),
    createdat                              DATE NOT NULL,
    updatedat                              DATE NOT NULL,
    programsetup                           VARCHAR2(1000),
    gcrememailfrequency                    VARCHAR2 (25),
    gcrememailsendtime                     VARCHAR2 (25),
    gcrememailsendstartdate                DATE,
    gcrememailsendenddate                  DATE,
    celebrationsettings                    CLOB,
    manageremail                           VARCHAR2 (1000),
    branding                               VARCHAR2 (1000),
    hierarchyid                            VARCHAR2 (50),
    emaildisplayname                       VARCHAR2 (1000),
    signatureimageurl                      VARCHAR2 (500)
);

CREATE TABLE stg_sa_celebrationspersons
(
    celebrationid VARCHAR2(50) NOT NULL,
    contributionid VARCHAR2(50) NOT NULL,
    personid VARCHAR2(50) NOT NULL,
    role VARCHAR2(50) DEFAULT 'peer',
    createdat DATE NOT NULL,
    updatedat DATE NOT NULL,
    mobilecode VARCHAR2(14) NOT NULL,
    invitationsentat DATE,
    inviterpersonid VARCHAR2(255),
    CONSTRAINT celebrationspersons_pkey PRIMARY KEY (celebrationid, personid)
);

CREATE TABLE stg_sa_milestones
(
    id NUMBER(18) NOT NULL,
    companyid VARCHAR2(50) NOT NULL,
    programid VARCHAR2(50) NOT NULL,
    milestoneid VARCHAR2(50) NOT NULL,
    milestonecode VARCHAR2(25) NOT NULL,
    milestonename VARCHAR2(1000) NOT NULL,
    milestoneaward VARCHAR2(1000) NOT NULL,
    milestonestate VARCHAR2(25) NOT NULL,
    ecard VARCHAR2(1000),
    celebrationtype VARCHAR2(25) NOT NULL,
    contributiontype VARCHAR2(25) NOT NULL,
    createdat DATE NOT NULL,
    updatedat DATE NOT NULL,
    maxaward NUMBER(18) DEFAULT 0,
    messagelevellabel VARCHAR2(1000),
    webmessageimageurl VARCHAR2(500),
    emailmessageimageurl VARCHAR2(500),
    countrycode VARCHAR2(3)
);

--DROP TABLE stg_sa_recipients
CREATE TABLE stg_sa_recipients
(
    id NUMBER(18) NOT NULL,
--    user_type VARCHAR2(50) NOT NULL,  --EXTRA COLUMN
--    claim_id NUMBER(18),   --EXTRA COLUMN
--    user_id  NUMBER(18),  --EXTRA COLUMN
--    purl_recipient_id NUMBER(18), --EXTRA COLUMN
    companyid VARCHAR2(50) NOT NULL,
    recipientid VARCHAR2(50) NOT NULL,
    recipientstate VARCHAR2(25) NOT NULL,
    recipientstatus VARCHAR2(25) NOT NULL,
    programid VARCHAR2(50) NOT NULL,
    milestonecode VARCHAR2(25),
    awarddate DATE,
    awardexpirationdate DATE,
    giftcodeid VARCHAR2(50),
    giftcodereference VARCHAR2(10),
    createdby VARCHAR2(25) NOT NULL,
    createdreference VARCHAR2(64),
    createdat DATE NOT NULL,
    updatedat DATE NOT NULL,
    awarddatetimezone VARCHAR2(50) DEFAULT 'America/Chicago',
    celebrationid VARCHAR2(50),
    celebrationsettings CLOB,
    participantpersonid VARCHAR2(50),
    manager1personid VARCHAR2(50),
    manager2personid VARCHAR2(50),
    celebrationyear NUMBER(18),
    emailsettings VARCHAR2(3000),
    awardeventsentat DATE
);