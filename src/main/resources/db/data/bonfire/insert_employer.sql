-- 
--EMPLOYER 
-- 
INSERT INTO employer 
            (employer_id,name,is_active,status_reason,federal_tax_id,state_tax_id,country_id,addr1,addr2,addr3,city,state,postal_code,phone_nbr,created_by,date_created,modified_by,date_modified,version)
VALUES      (5000,'Bonfire Uber Corp',1,'nothing','543535345','54353',(SELECT country_id 
                                                                       FROM   country 
                                                                       WHERE  country_code = 'us'),'7700 bush lake Rd',NULL,NULL,'minnesota','us_mn','44444','123456789',0,To_date('14-JUN-2005 14:37:26', 'DD-MM-YYYY HH24:MI:SS'),NULL,NULL,0) 
/