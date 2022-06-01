 CREATE OR REPLACE PROCEDURE  prc_getGQPaxMiniProfile(
                     p_in_promotion_id      IN VARCHAR,
                     p_in_user_id           IN VARCHAR,
                     p_in_locale            IN VARCHAR,
                     p_out_returncode       OUT VARCHAR,
                     p_out_data             OUT SYS_REFCURSOR
 ) IS
v_partner_audience_type VARCHAR2(100);
v_stage     VARCHAR2 (100);
/******************************************************************************
   NAME:       prc_getGQPaxMiniProfile
   PURPOSE:    

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        4/8/2015   Suresh J       1. Created this procedure.
              12/24/2015 gorantla       Bug 78926 Inactive Partners are reflecting on GQ final calculation reports
******************************************************************************/
BEGIN
    v_stage := 'Getting value for v_partner_audience_type';
    select partner_audience_type into v_partner_audience_type from promotion where promotion_id = p_in_promotion_id;   

    DELETE temp_table_session;
    INSERT INTO temp_table_session(asset_code, cms_name, cms_code)
    SELECT asset_code,cms_value,key FROM vw_cms_asset_value where key='COUNTRY_NAME' and locale = p_in_locale
    UNION
    SELECT asset_code,cms_name,cms_code
    FROM vw_cms_code_value E
    WHERE asset_code in ( 'picklist.department.type.items','picklist.positiontype.items') and e.locale=p_in_locale;

IF  v_partner_audience_type = 'nodebasedpartners' THEN
    v_stage := 'Processing nodebasedpartners';
    OPEN p_out_data FOR
    SELECT  au.user_id,
                    au.first_name,
                    au.last_name,
                    p.avatar_small,
                    p.allow_public_recognition,
                    p.allow_public_information,
                   n.name primary_node,
                    c.country_code,
--                    (select fnc_cms_asset_code_val_extr(c.cm_asset_code, 'COUNTRY_NAME', p_in_locale)  from dual)  AS country_name
                   (select cms_name from temp_table_session where asset_code=c.cm_asset_code) as country_name,
                   (select cms_name from temp_table_session where asset_code='picklist.positiontype.items' and cms_code=emp.position_type) as position_type, 
                   (select cms_name from temp_table_session where asset_code='picklist.department.type.items' and cms_code=emp.department_type) as department_type
    FROM application_user au,
         participant p,
         user_node un,
         node n,
         user_address ua,
         country c,
         (select user_id,position_type,department_type from participant_employer where termination_date is null) emp
    WHERE au.user_id = p.user_id
    AND   au.user_id = ua.user_id
    AND   ua.is_primary = 1
    AND   c.country_id = ua.country_id
    AND   un.node_id = n.node_id
    AND   UN.IS_PRIMARY = 1
    AND   un.user_id = au.user_id
    AND   au.user_id = emp.user_id
    AND   p.status = 'active'  --12/24/2018
    AND   EXISTS 
            (SELECT un.user_id
              FROM user_node un
             WHERE     EXISTS
                          (SELECT un2.node_id
                             FROM user_node un2
                            WHERE     un2.node_id = un.node_id
                                  AND un2.user_id = p_in_user_id)
                   AND un.user_id <> p_in_user_id
                   AND un.user_id = au.user_id)
 ORDER BY  au.last_name; 
 p_out_returncode := 0;
 
ELSIF v_partner_audience_type = 'userCharacteristics' THEN
    v_stage := 'Processing userCharacteristics';
    OPEN p_out_data FOR
    SELECT  au.user_id,
                    au.first_name,
                    au.last_name,
                    p.avatar_small,
                    p.allow_public_recognition,
                    p.allow_public_information,
                   n.name primary_node,
                    c.country_code,
--                    (select fnc_cms_asset_code_val_extr(c.cm_asset_code, 'COUNTRY_NAME', p_in_locale)  from dual)  AS country_name
                   (select cms_name from temp_table_session where asset_code=c.cm_asset_code) as country_name,
                   (select cms_name from temp_table_session where asset_code='picklist.positiontype.items' and cms_code=emp.position_type) as position_type, 
                   (select cms_name from temp_table_session where asset_code='picklist.department.type.items' and cms_code=emp.department_type) as department_type
    FROM application_user au,
         participant p,
         user_node un,
         node n,
         user_address ua,
         country c,
         (select user_id,position_type,department_type from participant_employer where termination_date is null) emp         
    WHERE au.user_id = p.user_id
    AND   au.user_id = ua.user_id
    AND   ua.is_primary = 1
    AND   c.country_id = ua.country_id
    AND   un.node_id = n.node_id
    AND   UN.IS_PRIMARY = 1
    AND   un.user_id = au.user_id
    AND   au.user_id = emp.user_id    
    AND   p.status = 'active'    --12/24/2018
    AND   EXISTS 
            (SELECT u.user_id
              FROM user_characteristic u
             WHERE     EXISTS
                          (SELECT uc.characteristic_id, uc.characteristic_value
                             FROM user_characteristic uc
                            WHERE     uc.user_id = p_in_user_id
                                  AND EXISTS
                                         (SELECT pg.partner_characteristic
                                            FROM promo_goalquest pg
                                           WHERE     pg.promotion_id = p_in_promotion_id
                                                 AND pg.partner_characteristic =
                                                        uc.characteristic_id)
                                  AND uc.characteristic_id = u.characteristic_id
                                  AND uc.characteristic_value = u.characteristic_value)
                   AND u.user_id <> p_in_user_id
                   AND u.user_id = au.user_id )
    ORDER BY  au.last_name;                                    
    p_out_returncode := 0;
    
ELSIF v_partner_audience_type = 'specifyaudience' THEN
    v_stage := 'Processing specifyaudience';
    OPEN p_out_data FOR
    SELECT  au.user_id,
                    au.first_name,
                    au.last_name,
                    p.avatar_small,
                    p.allow_public_recognition,
                    p.allow_public_information,
                   n.name primary_node,
                    c.country_code,
--                    (select fnc_cms_asset_code_val_extr(c.cm_asset_code, 'COUNTRY_NAME', p_in_locale)  from dual)  AS country_name
                   (select cms_name from temp_table_session where asset_code=c.cm_asset_code) as country_name,   
                   (select cms_name from temp_table_session where asset_code='picklist.positiontype.items' and cms_code=emp.position_type) as position_type, 
                   (select cms_name from temp_table_session where asset_code='picklist.department.type.items' and cms_code=emp.department_type) as department_type
    FROM application_user au,
         participant p,
         user_node un,
         node n,
         user_address ua,
         country c,
         (select user_id,position_type,department_type from participant_employer where termination_date is null) emp         
    WHERE au.user_id = p.user_id
    AND   au.user_id = ua.user_id
    AND   ua.is_primary = 1
    AND   c.country_id = ua.country_id
    AND   un.node_id = n.node_id
    AND   UN.IS_PRIMARY = 1
    AND   un.user_id = au.user_id
    AND   au.user_id = emp.user_id    
    AND   p.status = 'active'   --12/24/2018
    AND   EXISTS 
            (select pa.user_id 
            from participant_audience 
            pa where exists (select p.audience_id 
                                from promo_audience p 
                                where p.promo_audience_type = 'PARTNER' 
                                      and p.promotion_id = p_in_promotion_id 
                                      and audience_id = pa.audience_id)
                     and pa.user_id <> p_in_user_id
                     and pa.user_id = au.user_id)
    ORDER BY  user_id;                           
    p_out_returncode := 0;
END IF;

   EXCEPTION
     WHEN NO_DATA_FOUND THEN
       prc_execution_log_entry (
       'prc_getGQPaxMiniProfile',
       1,
       'ERROR',
       v_stage || p_in_promotion_id || ' ' || SQLERRM,
       NULL);

     WHEN OTHERS THEN
       p_out_returncode := 99;
       prc_execution_log_entry (
       'prc_getGQPaxMiniProfile',
       1,
       'ERROR',
       v_stage || p_in_promotion_id || ' ' || SQLERRM,
       NULL);

END prc_getGQPaxMiniProfile;
/