INSERT INTO supplier 
            (supplier_id,supplier_name,supplier_type,description,status,allow_partner_sso,catalog_url,statement_url,cm_asset_code,image_cm_key,title_cm_key,desc_cm_key,button_cm_key,created_by,date_created,version)
     VALUES (supplier_pk_sq.NEXTVAL,'BI Bank','internal','The BI Bank Account System','active',0,'http://www.biworldwide.com/bank','http://www.biworldwide.com/statement','supplier.awards.page.10000861','IMAGE_NAME','SUPPLIER_NAME','SUPPLIER_DESCRIPTION','BUTTON_NAME',0,SYSDATE,0)
/
INSERT INTO supplier 
            (supplier_id,supplier_name,supplier_type,description,status,allow_partner_sso,catalog_url,catalog_target_id,statement_url,statement_target_id,cm_asset_code,image_cm_key,title_cm_key,desc_cm_key,button_cm_key,created_by,date_created,version)
     VALUES (supplier_pk_sq.NEXTVAL,'BII','external','BI International - supplies Europe','active',0,'https://www.awardperqsinternational.com/login/LoginSL.asp','566','https://www.awardperqsinternational.com/login/LoginSL.asp','578','supplier.awards.page.10000863','IMAGE_NAME','SUPPLIER_NAME','SUPPLIER_DESCRIPTION','BUTTON_NAME',0,SYSDATE,0)
/
INSERT INTO supplier 
            (supplier_id,supplier_name,supplier_type,description,status,allow_partner_sso,created_by,date_created,version)
     VALUES (supplier_pk_sq.NEXTVAL,'Payroll Extract','internal','BI Supplied Accounts with International Address','active',0,0,SYSDATE,0)
/