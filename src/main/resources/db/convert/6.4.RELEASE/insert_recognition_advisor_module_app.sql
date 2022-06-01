--liquibase formatted sql

--changeset ramesh:1
--comment  Insert system variable for Manager Recognition Advisor tile
Insert into MODULE_APP (MODULE_APP_ID,NAME,DESCRIPTION,AUDIENCE_TYPE,APP_AUDIENCE_TYPE,TILE_MAPPING_TYPE,UI_APP_NAME,TEMPLATE_NAME,VIEW_NAME,ADMIN_AUDIENCE_SETUP,IS_MOBILE_ENABLED,AVAILABLE_SIZES,SORT_ORDER,MODULE_TYPE,IS_ACTIVE,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
values(MODULE_APP_PK.nextval,'Manager Recognition Advisor','Desc - Manager tile for RA','allactivepaxaudience','recognitionadvisor','recognitionadvisor','raModule',null,null,0,0,'4X4,4X2,2X2,2X1,1X1',null,'standard',1,5662,sysdate,null,null,1);
--rollback DELETE FROM MODULE_APP WHERE TILE_MAPPING_TYPE='recognitionAdvisorModule';

--changeset kothanda:2
--comment  name changes for audience type & tile mapping type
Update MODULE_APP set APP_AUDIENCE_TYPE = 'raModule',TILE_MAPPING_TYPE = 'raModule' where NAME = 'Manager Recognition Advisor';
--rollback Update MODULE_APP SET APP_AUDIENCE_TYPE = 'recognitionadvisor',TILE_MAPPING_TYPE = 'recognitionadvisor' where NAME = 'Manager Recognition Advisor';

--changeset sivanand:3
--comment audience type and tile mapping name changes for FED
Update MODULE_APP set APP_AUDIENCE_TYPE = 'recognitionAdvisorModule', TILE_MAPPING_TYPE = 'recognitionAdvisorModule', UI_APP_NAME = 'recognitionAdvisorModule' where NAME = 'Manager Recognition Advisor';
--rollback Update MODULE_APP SET APP_AUDIENCE_TYPE = 'raModule',TILE_MAPPING_TYPE = 'raModule', UI_APP_NAME = 'raModule' where NAME = 'Manager Recognition Advisor';

--changeset gorantla:4
--comment Create temporary table for recognition advisor
CREATE GLOBAL TEMPORARY TABLE GTT_RA_PROMOTION
(
  PROMOTION_ID                  NUMBER(18),
  PROMOTION_STATUS              VARCHAR2(30 CHAR),
  PRIMARY_AUDIENCE_TYPE         VARCHAR2(40 CHAR),
  SECONDARY_AUDIENCE_TYPE       VARCHAR2(40 CHAR),
  ORGL_SECONDARY_AUDIENCE_TYPE  VARCHAR2(40 CHAR)
)
ON COMMIT PRESERVE ROWS;
--rollback DROP TABLE GTT_RA_PROMOTION;

--changeset gorantla:5
--comment Create index for recognition advisor
CREATE INDEX GTT_RA_PROMO_IDX ON GTT_RA_PROMOTION
(PROMOTION_ID);
--rollback DROP INDEX GTT_RA_PROMO_IDX;

--changeset gorantla:6
--comment Create table for recognition advisor
CREATE GLOBAL TEMPORARY TABLE GTT_RA_LVL1_RECEIVER       
(
  USER_ID  NUMBER(18)
)
ON COMMIT PRESERVE ROWS
NOCACHE
--rollback DROP TABLE GTT_RA_LVL1_RECEIVER;

--changeset gorantla:7
--comment Create table for recognition advisor
CREATE GLOBAL TEMPORARY TABLE GTT_RA_RECEIVER
(
  USER_ID  NUMBER(18)
)
ON COMMIT PRESERVE ROWS
NOCACHE
--rollback DROP TABLE GTT_RA_RECEIVER;

--changeset gorantla:8
--comment Create table for recognition advisor
CREATE GLOBAL TEMPORARY TABLE GTT_RA_NODE
(
  NODE_ID         NUMBER(18),
  NODE_LEVEL      NUMBER(18),
  PARENT_NODE_ID  NUMBER(18),
  MGR_ID          NUMBER(18),
  NEW_LEVEL       NUMBER(18),
  CURR_NODE       NUMBER(18)
)
ON COMMIT PRESERVE ROWS
NOCACHE
--rollback DROP TABLE GTT_RA_NODE;

--changeset gorantla:9
--comment Create index for recognition advisor
CREATE INDEX GTT_RA_NODE_IDX ON GTT_RA_NODE
(NODE_ID);
--rollback DROP INDEX GTT_RA_NODE_IDX;

--changeset gorantla:10
--comment Create table for recognition advisor
CREATE GLOBAL TEMPORARY TABLE GTT_RA_PROGRAM
(
  PROMOTION_ID             NUMBER(18),
  PROMOTION_NAME           VARCHAR2(1000 CHAR),
  PROMOTION_STATUS         VARCHAR2(30 CHAR),
  AWARD_BUDGET_MASTER_ID   NUMBER(18),
  PRIMARY_AUDIENCE_TYPE    VARCHAR2(40 CHAR),
  SECONDARY_AUDIENCE_TYPE  VARCHAR2(40 CHAR)
)
ON COMMIT PRESERVE ROWS
NOCACHE
--rollback DROP TABLE GTT_RA_PROGRAM;

--changeset gorantla:11
--comment Create index for recognition advisor
CREATE INDEX GTT_RA_PROG_IDX ON GTT_RA_PROGRAM
(PROMOTION_ID);
--rollback DROP INDEX GTT_RA_PROG_IDX;
