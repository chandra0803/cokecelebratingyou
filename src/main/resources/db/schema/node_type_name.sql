CREATE OR REPLACE VIEW NODE_TYPE_NAME
(
   NODE_TYPE_ID,
   CM_NAME
)
AS
   (SELECT nt.node_type_id node_type_id,
           SUBSTR  (UPPER (fnc_cms_asset_code_val_extr (nt.cm_asset_code,name_cm_key,'en_US')),--bug # 57361
                   1,
                   40)
              cm_name
      FROM node_type nt)
/
