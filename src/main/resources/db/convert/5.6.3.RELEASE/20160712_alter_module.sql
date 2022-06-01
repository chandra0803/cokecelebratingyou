UPDATE MODULE_APP SET AVAILABLE_SIZES = '4X4,4X2,2X2', UI_APP_NAME = 'nominations' WHERE MODULE_APP_ID = 3003
/
INSERT INTO MODULE_APP
   (MODULE_APP_ID,NAME,DESCRIPTION,IS_MOBILE_ENABLED,AVAILABLE_SIZES,AUDIENCE_TYPE, APP_AUDIENCE_TYPE, TILE_MAPPING_TYPE,  UI_APP_NAME,ADMIN_AUDIENCE_SETUP, CREATED_BY, DATE_CREATED,VERSION)
VALUES
   (3055, 'In Progress Nomination', 'Desc - In Progress Nomination',0, '2X1','allactivepaxaudience','nominationsProgressModule','nominationsProgressModule', 'nominations',0,5662, sysdate, 1)
/
INSERT INTO MODULE_APP
   (MODULE_APP_ID,NAME,DESCRIPTION,IS_MOBILE_ENABLED,AVAILABLE_SIZES,AUDIENCE_TYPE, APP_AUDIENCE_TYPE, TILE_MAPPING_TYPE,  UI_APP_NAME,ADMIN_AUDIENCE_SETUP, CREATED_BY, DATE_CREATED,VERSION)
VALUES
   (3056, 'Nominations Approval Module', 'Desc - Nominations Approval Module',0, '4X2,2X1','allactivepaxaudience','nominationsApprovalModule','nominationsApprovalModule', 'nominations',0,5662, sysdate, 1)
/
INSERT INTO MODULE_APP
   (MODULE_APP_ID,NAME,DESCRIPTION,IS_MOBILE_ENABLED,AVAILABLE_SIZES,AUDIENCE_TYPE, APP_AUDIENCE_TYPE, TILE_MAPPING_TYPE,  UI_APP_NAME,ADMIN_AUDIENCE_SETUP, CREATED_BY, DATE_CREATED,VERSION)
VALUES
   (3057, 'Nominations Winner Module', 'Desc - Nominations Winner Module',0, '4X4,4X2,2X2','allactivepaxaudience','nominationsWinnersModule','nominationsWinnersModule', 'nominations',0,5662, sysdate, 1)
/