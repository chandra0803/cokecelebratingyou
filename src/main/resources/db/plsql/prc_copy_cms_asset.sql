CREATE OR REPLACE PROCEDURE prc_copy_cms_asset
( p_from_asset_code  IN     cms_asset.code %TYPE,
   p_to_asset_code  IN     cms_asset.code %TYPE,
   p_out_return_code OUT   NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('prc_copy_cms_asset');
   c_release_level      CONSTANT execution_log.release_level%type := '1.0';

   v_msg                execution_log.text_line%TYPE;
   v_cms_seq_id     NUMBER;    
   v_content_seq_id     NUMBER;     
  
   CURSOR c1(p_in_from_asset_code IN VARCHAR2) is
   SELECT * FROM cms_content_key WHERE asset_id IN (SELECT id FROM cms_asset WHERE code = p_in_from_asset_code);
   
   CURSOR c2(p_in_content_key_id IN NUMBER) is
   SELECT * FROM cms_content WHERE content_key_id = p_in_content_key_id;      
  
BEGIN

   SELECT cms_generic_pk_sq.NEXTVAL INTO v_cms_seq_id
     FROM dual;  
   v_msg := 'Insert cms_asset';
   
   INSERT INTO cms_asset
   (ID, entity_version, code, NAME, is_public,
    section_id, asset_type_id, pax_or_admin, module, product_version,
    created_by, date_created)
 (
SELECT v_cms_seq_id,
       entity_version,
       p_to_asset_code,
       name,
       is_public,
       section_id,
       asset_type_id,
       pax_or_admin,
       module,
       product_version,
       c_process_name,
       SYSDATE FROM cms_asset WHERE code=p_from_asset_code);
       
         
        FOR C1_R IN C1(p_from_asset_code) LOOP
        
        SELECT cms_generic_pk_sq.NEXTVAL INTO v_content_seq_id
         FROM dual;
             
           v_msg := 'Insert cms_content_key';
           
           INSERT INTO cms_content_key
         (  id,
            entity_version,
            sort_order,
            guid,
            asset_id,
            created_by,
            date_created
         )         
         (SELECT  v_content_seq_id,
            0,             -- entity_version
            sort_order,
            SYS_GUID(),    -- guid
            v_cms_seq_id,
            c_process_name,  -- created_by,
            SYSDATE    -- date_created
         FROM cms_content_key WHERE id = c1_r.id);
         
            INSERT INTO cms_content_key_audience_lnk
         (  content_key_id,
            audience_id
         )
         SELECT v_content_seq_id,
                      audience_id
        FROM
                 cms_content_key_audience_lnk
        WHERE content_key_id = c1_r.id;
        
        FOR C2_R IN C2(c1_r.id) LOOP
        -- add content records
         v_msg := 'INSERT cms_content';
         INSERT INTO cms_content
         (  id,
            entity_version,
            locale,
            version,
            content_status,
            added_by,
            added_timestamp,
            guid,
            content_version,
            content_key_id,
            created_by,
            date_created
         )
         (SELECT cms_generic_pk_sq.NEXTVAL,
            entity_version,
            locale,
            version,
            content_status,
            added_by,
            added_timestamp,
            SYS_GUID(),
            content_version,
            v_content_seq_id,            
            c_process_name,  -- created_by,
            SYSDATE    -- date_created
            FROM cms_content WHERE id = c2_r.id);
         
      -- add content data records
      v_msg := 'INSERT cms_content_data';     
         INSERT INTO cms_content_data
         (  content_id,
            key,
            value
         )
         (  -- select a row for each key field value
            SELECT cms_generic_pk_sq.CURRVAL,
                   key,
                   value
              FROM cms_content_data WHERE content_id = c2_r.id
         );
        
        END LOOP;
        
        END LOOP;
    
   p_out_return_code :=0;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg||SQLERRM, NULL);
      p_out_return_code :=99;

END prc_copy_cms_asset;
/